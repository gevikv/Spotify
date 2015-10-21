package com.example.gevik.gevikspotify;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    View rootView;
    Model.Tracks trks;
    int position;
    ImageView albumImage;
    TextView name_textView;
    TextView album_name_textView;
    TextView song_name_textView;
    static MediaPlayer myMediaPlayer;
    Button play_btn;
    Button next_btn;
    Button previous_btn;
    String artistName;
    int length;
    boolean pause = false;
    SeekBar seekBar;
    TextView progressTimer_text;
    TextView end_timer;
    Handler seekHandler = new Handler();
    ProgressDialog progDailog;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_player, container, false);
        Log.i("check1", "check1");
        progDailog = new ProgressDialog(getActivity());


        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progDailog.setCancelable(true);
        progDailog.show();


        Gson g = new Gson();
        Bundle args = getArguments();
        position = args.getInt("trackPosition");
        artistName = args.getString("ArtistName");
        String key = args.getString("tracksKey");
        trks = g.fromJson(key, Model.Tracks.class);
        String name = trks.tracks.get(position).getName();

        name_textView = (TextView) rootView.findViewById(R.id.artist_name);
        album_name_textView = (TextView) rootView.findViewById(R.id.Album_Name);
        song_name_textView = (TextView) rootView.findViewById(R.id.song_name);
        albumImage = (ImageView) rootView.findViewById(R.id.Album_Image);
        play_btn = (Button) rootView.findViewById(R.id.play_btn);
        previous_btn = (Button) rootView.findViewById(R.id.preview_btn);
        next_btn = (Button) rootView.findViewById(R.id.next_btn);
        progressTimer_text = (TextView) rootView.findViewById(R.id.timer_progress);
        end_timer = (TextView) rootView.findViewById(R.id.timer_end);
        end_timer.setText("0:30");
        play_btn.setOnClickListener(this);
        previous_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        name_textView.setText(artistName);

        album_name_textView.setText(trks.tracks.get(position).getAlbum());
        song_name_textView.setText(trks.tracks.get(position).getName());
        albumImage.setImageBitmap(trks.tracks.get(position).getAlbumImage());
        seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);
        seekBar.setMax(30000);

        initPlayer("play");

        long time = myMediaPlayer.getDuration();
        String time2 = "time=" + time;
        Log.i("duration", time2);
        // progDailog.cancel();

        updateSeekBar();
        seekBar.setOnSeekBarChangeListener(this);


        return rootView;
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
        }
    };

    private void updateSeekBar() {
        seekBar.setProgress(myMediaPlayer.getCurrentPosition());
        String timer = null;


        seekHandler.postDelayed(run, 1000);

        int timerInt = (int) Math.round((double) myMediaPlayer.getCurrentPosition() / 1000);


        timer = "0:" + timerInt;
        progressTimer_text.setText(timer);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void updateUi() {
        name_textView.setText(artistName);
        album_name_textView.setText(trks.tracks.get(position).getAlbum());
        song_name_textView.setText(trks.tracks.get(position).getName());
        albumImage.setImageBitmap(trks.tracks.get(position).getAlbumImage());


    }

    private void initPlayer(String action) {
        if (isOnline()) {

            if (myMediaPlayer != null) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                myMediaPlayer = null;

            }
            Log.i("check2", "check2");
            myMediaPlayer = null;
            myMediaPlayer = new MediaPlayer();
            if (myMediaPlayer.isPlaying()) {
                myMediaPlayer.stop();

            }

            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                String url = "url is  " + trks.tracks.get(position).getPriview_url();
                Log.i("urlis", url);
                myMediaPlayer.setDataSource(trks.tracks.get(position).getPriview_url());
                Log.i("check3", "check3");
                myMediaPlayer.prepareAsync();
                // might take long! (for buffering, etc)


            } catch (IOException e) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG);

            }
            try {
                myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer player) {
                        if (player == myMediaPlayer)
                            myMediaPlayer.start();
                        progDailog.cancel();
                        Log.i("check4", "check4");

                    }

                });
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(),"NETWORK Erroe try again",Toast.LENGTH_SHORT);
            }

        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();


        }
    }


    public void pause() {
        myMediaPlayer.pause();
        play_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        length = myMediaPlayer.getCurrentPosition();

        pause = true;


    }

    public void onDestroy() {
        super.onDestroy();
        myMediaPlayer.stop();

    }

    public void onPause() {
        super.onPause();
        myMediaPlayer.pause();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_btn:

                if (position > 0) {
                    position = position - 1;
                    pause = false;
                    play_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause_btn));
                    updateUi();
                    initPlayer("preview");
                } else
                    Toast.makeText(getActivity(), "This is the first Track", Toast.LENGTH_SHORT).show();
                break;

            case R.id.next_btn:
                if (trks.tracks.size() - 1 > position) {
                    position = position + 1;
                    pause = false;
                    play_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause_btn));
                    updateUi();
                    initPlayer("next");
                } else
                    Toast.makeText(getActivity(), "This is the Last Track", Toast.LENGTH_SHORT).show();
                break;
            case R.id.play_btn:
                if (pause == false) {
                    pause();
                } else
                    start();

                break;


        }
    }

    private void start() {
        myMediaPlayer.seekTo(length);
        play_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause_btn));
        pause = false;
        myMediaPlayer.start();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {


    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int i = seekBar.getProgress();
        myMediaPlayer.seekTo(i);


    }
}
