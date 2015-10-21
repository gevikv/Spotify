package com.example.gevik.gevikspotify;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

import Model.Artist;

public class MainActivity extends Activity implements OnselectedArtistListener {
    private boolean mTwoPane;
    PlayerFragment playerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.Detailscontainer) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                this.getFragmentManager().beginTransaction()
                        .add(R.id.container, new ListFragment())
                        .commit();
            }

        } else {
            mTwoPane = false;
            this.getFragmentManager().beginTransaction()
                    .add(R.id.container, new ListFragment())
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedArtistListener(int position, Artist art) {
        Log.i("artist15", "artist15");
        Gson g = new Gson();
        String artsistsObject = g.toJson(art);
        Log.i("artist16", "artist16");
        Bundle args = new Bundle();
        args.putString("artistsKey", artsistsObject);

        Log.i("artist17", "artist17");

        ArtistTopSongsFragment atp = new ArtistTopSongsFragment();
        atp.setArguments(args);
        Log.i("artist18", "artist18");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Log.i("artist19", "artist19");
        if (mTwoPane == true) {


            ft.replace(R.id.Detailscontainer, atp, "ArtistDetails");
        } else {

            ft.replace(R.id.container, atp, "ArtistDetails");

        }

        ft.addToBackStack(ListFragment.class.getName());

        Log.i("artist20", "artist20");
        if (isOnline()) {


            ft.commit();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();


        }


    }

    @Override
    public void selectedSongListener(int position, Model.Tracks tr, String artistName) {
        String name = tr.tracks.get(0).getName();

        PlayerFragment playerFragment;
        Gson g = new Gson();
        String artsistsObject = g.toJson(tr);
        Log.i("artist45", "artist45");
        Bundle args = new Bundle();
        args.putString("tracksKey", artsistsObject);
        args.putInt("trackPosition", position);
        args.putString("ArtistName", artistName);
        // playerFragment=null;
        playerFragment = new PlayerFragment();
        playerFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        if (mTwoPane == true) {
            //  ft.replace(R.id.container3, playerFragment, "Tracks");
            // ft.addToBackStack(PlayerFragment.class.getName());
            if (isOnline()) {

                if ((getFragmentManager().findFragmentById(R.id.container3) instanceof PlayerFragment)) {
                    getFragmentManager().beginTransaction().remove(playerFragment);

                    getFragmentManager().beginTransaction().add(R.id.container3, playerFragment, "hello").addToBackStack("hello").commit();
                } else
                    getFragmentManager().beginTransaction().add(R.id.container3, playerFragment, "hello").addToBackStack("hello").commit();
                //   ft.commit();
            } else {

                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();

            }


        } else {
            ft.replace(R.id.container, playerFragment, "Tracks");
            ft.addToBackStack(PlayerFragment.class.getName());
            if (isOnline()) {
                ft.commit();
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
