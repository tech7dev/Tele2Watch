package com.tech7.tele2watch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.tech7.tele2watch.R;

public class TvShowActivity extends AppCompatActivity {

    private Boolean wifiConnected = false;
    private Boolean mobileConnected = false;
    private Button btnTryAgain;

    public static final String EXTRA_GROUP_TITLE = "com.tech7.tele2watch.EXTRA_GROUP_TITLE";  //title of video
    public static final String EXTRA_TVG_LOGO = "com.tech7.tele2watch.EXTRA_TVG_LOGO";  //tv logo
    public static final String EXTRA_URL = "com.tech7.tele2watch.EXTRA_URL"; //Url

    private Toolbar toolbar;
    private ActionBar actionBar;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    //public static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
    //private String m3u8url = "http://static.france24.com:80/live/F24_FR_HI_HLS/live_tv.m3u8?fluxustv.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);

        //check internet connection
        checkNetworkConnection();

    }

    private void checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //internet connection is ready
        if (networkInfo != null && networkInfo.isConnected()) {
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (wifiConnected || mobileConnected) {
                Intent intent = getIntent();
                if(intent.hasExtra(EXTRA_URL)) {
                    Log.d("TvShowActivity-URL", intent.getStringExtra(EXTRA_GROUP_TITLE));
                    setTitle(intent.getStringExtra(EXTRA_GROUP_TITLE));
                    initExoPlayer(intent.getStringExtra(EXTRA_URL));
                }

                //hide actionbar if orientation is landscape
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getSupportActionBar().hide();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                else {
                    getSupportActionBar().show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        }
        else{
            //not connected
            // Display the panel
            findViewById(R.id.networkPanel).setVisibility(View.VISIBLE);
            btnTryAgain = findViewById(R.id.btnTryAgain);
            btnTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display the panel
                    findViewById(R.id.networkPanel).setVisibility(View.INVISIBLE);
                    checkNetworkConnection();
                }
            });
        }
    }

    private void initExoPlayer(String JSON_URL_M3U8) {
        playerView = findViewById(R.id.exo_player);
        //1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new
                AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new
                DefaultTrackSelector(videoTrackSelectionFactory);

        //--------------HLS--------------------
        // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "app-name"));
        // HLS
        // Create a HLS media source pointing to a playlist uri.
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(JSON_URL_M3U8));
         //Create a player instance.
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
         //Prepare the player with the media source.

        //Prepare Player------------------------
        simpleExoPlayer.prepare(hlsMediaSource);
        playerView.setPlayer(simpleExoPlayer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }

}
