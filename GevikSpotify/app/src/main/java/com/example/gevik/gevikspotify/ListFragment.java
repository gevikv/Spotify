package com.example.gevik.gevikspotify;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import Model.Artist;
import Model.Artists;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView list;
    Context thiscontext;
    Artists artists;
    Artists art;
    SharedPreferences appPrefs;
    EditText text;
    String userSearch;
    String notfind = "false";


    public ListFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    setRetainInstance(true);
        // retain this fragment when activity is re-initialized
        artists = new Artists();
        Log.i("artist38", "artist38");

        if (savedInstanceState != null) {
            Log.i("artist35", "artist35");
            appPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getActivity());
            Gson g = new Gson();
            String myArtists = appPrefs.getString("artistsKey", "");
            art = g.fromJson(myArtists, Artists.class);
            artists = art;
            Log.i("saveEnd", "saveENDf");

        } else {
            Log.i("artist36", "artist36");


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("artist39", "artist39");
        thiscontext = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        text = (EditText) rootView.findViewById(R.id.editText);
        list = (ListView) rootView.findViewById(R.id.listview_spotify);
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //  Toast.makeText(getActivity(), "clciked", Toast.LENGTH_SHORT).show();
                    if (isOnline()) {
                        if (text.getText().toString() != null && !text.getText().toString().isEmpty()) {
                            searchClicked();
                            return true;
                        }
                    } else {

                        Toast.makeText(getActivity(), "You Do not have Internet connection", Toast.LENGTH_SHORT).show();
                    }

                }

                return false;
            }
        });

        //  Log.i("artist","artistf");
        if (isOnline()) {
            if (art != null && art.equals(artists)) {
                Log.e("artist2", "artist2");
                FirstPageAdapter adapter2 = new FirstPageAdapter(thiscontext, artists);
                list.setAdapter(adapter2);
            } else {
                if (userSearch != null)
                    if (isOnline()) {
                        new fetchDatafromCloud().execute(userSearch);
                    } else {
                        Toast.makeText(getActivity(), "You Do not have Internet connection", Toast.LENGTH_SHORT).show();

                    }
            }
        } else {
            Toast.makeText(getActivity(), "You Do not have Internet connection", Toast.LENGTH_SHORT).show();
        }

        Log.i("artist4", "artist4");
        list.setOnItemClickListener(this);


        return rootView;


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void searchClicked() {
        Log.i("artist5", "artist5");
        notfind = "false";

        userSearch = text.getText().toString();
        if (isOnline()) {
            new fetchDatafromCloud().execute(userSearch);
        } else {
            Toast.makeText(getActivity(), "You Do not have Internet connection", Toast.LENGTH_SHORT).show();


        }
    }

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        Log.i("artist6", "artist6");
        SharedPreferences appPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity());

        Log.i("artist7", "artist7");
        SharedPreferences.Editor editor = appPrefs.edit();

        Gson g = new Gson();
        String artsistsObject = g.toJson(artists);
        editor.remove("artistKey");
        editor.putString("artistsKey", artsistsObject);
        editor.commit();
        outState.putString("artist", artsistsObject);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("artist8", "artist8");
        Artist ar = artists.artistss.get(position);
        String test = ar.getName();

        OnselectedArtistListener listener = (OnselectedArtistListener) getActivity();
        Log.i("artist9", "artist9");

        listener.selectedArtistListener(position, ar);

    }


    public class fetchDatafromCloud extends AsyncTask<String, Void, Artists> {

        Artist artist;


        @Override
        protected Artists doInBackground(String... params) {
            Log.i("artist10", "artist10");
            artists = new Artists();
            ArtistsPager results = null;
            //   Tracks tracks=new Tracks();   //this is model tracks
            try {
                String para = params[0].toString();
                SpotifyApi api = new SpotifyApi();


                SpotifyService spotify = api.getService();
                results = spotify.searchArtists(para);
            } catch (Exception e) {
                e.printStackTrace();


            }

            if (results.artists.items.size() < 1) {
                notfind = "true";

            }
            Bitmap bitmap;
            Log.i("artist11", "artist11");
            for (int i = 0; i < results.artists.items.size(); i++) {
                artist = new Artist();

                artist.setName(results.artists.items.get(i).name);
                artist.setId(results.artists.items.get(i).id);

                try {

                    artist.setUrl(results.artists.items.get(i).images.get(0).url);
                } catch (Exception e) {
                    artist.setUrl("http://i.imgur.com/Br2qgeJ.jpg");

                }
                Log.i("artist12", "artist12");
                String url = artist.getUrl();
                Log.i("artist112", "artist112");
                try {
                    Log.i("artist112A", "artist112A");
                    bitmap = Glide.
                            with(thiscontext).
                            load(url).
                            asBitmap().
                            into(100, 100).
                            get();
                    Log.i("artist112B", "artist112B");
                    artist.setArtistImage(bitmap);
                } catch (InterruptedException e) {
                    Log.i("artist113", "artist113");
                    artist.setArtistImage(artists.artistss.get(i - 1).getArtistImage());
                } catch (Exception e) {
                    Log.i("artist113A", "artist113A");
                    e.printStackTrace();
                }
                Log.i("artist13", "artist13");

                artists.artistss.add(artist);
            }

            return artists;
        }

        protected void onPostExecute(Artists result) {
            if (notfind.equalsIgnoreCase("true")) {
                Toast.makeText(getActivity(), "No Result was found ...", Toast.LENGTH_SHORT).show();
            }
            Log.i("artist14", "artist14");

            FirstPageAdapter adapter = new FirstPageAdapter(thiscontext, result);

            list.setAdapter(adapter);

            Log.i("artist14A", "artist14A");
            artists = result;


        }

    }


}
