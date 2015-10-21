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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import Model.Artist;
import Model.Track;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistTopSongsFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView list;
    String test2 = "false";
    Context thiscontext;
    TextView text;
    Model.Tracks tracks; //= new Model.Tracks();
    Tracks tr;
    String para;
    String artistName;
    SharedPreferences appPrefs;

    public ArtistTopSongsFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i("dro1", "dro1");
            appPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getActivity());
            Gson g = new Gson();
            String myArtists = appPrefs.getString("tracksKey", "");
            tracks = g.fromJson(myArtists, Model.Tracks.class);


        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tracks = new Model.Tracks();
        Log.i("artist21", "artist21");
        Gson g = new Gson();
        Bundle args = getArguments();
        String key = args.getString("artistsKey");
        Log.i("artist21A", "artist21A");
        Artist ar = g.fromJson(key, Artist.class);
        artistName = ar.getName();
        Log.i("artist22", "artist22");

        thiscontext = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_artist_top_songs, container, false);
        list = (ListView) rootView.findViewById(R.id.listview_details);
        text = (TextView) rootView.findViewById(R.id.warning_text);
        Log.i("artist23", "artist23");
        if (isOnline()) {
            Log.i("check23", ar.getId());
            new fetchDataforDetails().execute(ar.getId());
        } else {
            Toast.makeText(getActivity(), "You Do not have Internet connection", Toast.LENGTH_SHORT).show();

        }


        list.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        OnselectedArtistListener listener = (OnselectedArtistListener) getActivity();
        listener.selectedSongListener(position, tracks, artistName);


        String name = tracks.tracks.get(position).getName();
        Log.i("check11", name);

        String url = tr.tracks.get(position).preview_url;
        // Boolean bol=tr.tracks.get(position).is_playable;
        // String t=bol.toString();
        //  String ty="f"+t+ url  ;
        // Toast.makeText(getActivity(), ty, Toast.LENGTH_LONG).show();
        // MediaPlayer myMediaPlayer = new MediaPlayer();
        // myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // try {
        //    myMediaPlayer.setDataSource(url);
        //    myMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        // } catch (IOException e) {
        //     Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG);

        // }
        // myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

        //     @Override
        //    public void onPrepared(MediaPlayer player) {
        //  player.start();

        //   }

        //});
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        SharedPreferences appPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity());

        Log.i("artist7", "artist7");
        SharedPreferences.Editor editor = appPrefs.edit();

        Gson g = new Gson();
        String artsistsObject = g.toJson(tracks);
        editor.remove("tracksKey");
        editor.putString("tracksKey", artsistsObject);
        editor.commit();
        outState.putString("tracksKey", artsistsObject);
        Log.i("dro2", "dro2");
    }


    public class fetchDataforDetails extends AsyncTask<String, Void, Model.Tracks> {
        Track track;
        //  ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            Log.i("artist24", "artist24");
            //  progDailog = new ProgressDialog(thiscontext);
            super.onPreExecute();
            //   progDailog = new ProgressDialog(thiscontext);
            //   progDailog.setMessage("Loading...");
            //  progDailog.setIndeterminate(false);
            //  progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Log.i("artist25", "artist25");
            //    progDailog.setCancelable(true);
            //  progDailog.show();
        }

        @Override
        protected Model.Tracks doInBackground(String... params) {
            Log.i("artist26", "artist26");
            tracks = new Model.Tracks();
            para = params[0].toString();
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            try {
                tr = spotify.getArtistTopTrack(para);

                Bitmap bitmap;
                String test = tr.tracks.get(0).name;
                Log.e("bruc", "bruc");
                for (int i = 0; i < tr.tracks.size(); i++) {
                    track = new Track();
                    track.setName(tr.tracks.get(i).name);
                    track.setAlbum(tr.tracks.get(i).album.name);
                    track.setPriview_url(tr.tracks.get(i).preview_url);

                    try {
                        track.setUrl(tr.tracks.get(i).album.images.get(0).url);
                    } catch (Exception e) {
                        track.setUrl("http://i.imgur.com/Br2qgeJ.jpg");

                    }
                    Log.e("thisTest2", test);
                    try {
                        bitmap = Glide.
                                with(thiscontext).
                                load(track.getUrl()).
                                asBitmap().
                                into(100, 100).
                                get();
                        track.setAlbumImage(bitmap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tracks.tracks.add(track);
                }
            } catch (Exception e) {
                test2 = "true";

            }


            return tracks;
        }

        protected void onPostExecute(Model.Tracks result) {
            if (test2.equalsIgnoreCase("true")) {
                text.setText("This artist does not have Top Tracks.");
                //    progDailog.cancel();
            } else {
                //   progDailog.cancel();
                DetailsPageAdapter adapter = new DetailsPageAdapter(thiscontext, result);


                list.setAdapter(adapter);

            }


        }
    }


}
