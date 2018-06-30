package com.androfast.pc.apploginandroidpostgresql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class Mostrar extends  YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {

        public static final String DEVELOPER_KEY = ConfigYoutube.DEVELOPER_KEY;
        private static String VIDEO_ID = ConfigYoutube.YOUTUBE_VIDEO_CODE;
        private static final int RECOVERY_DIALOG_REQUEST = 1;
        YouTubePlayerFragment myYouTubePlayerFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        myYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
        .findFragmentById(R.id.youtubeplayerfragment);

        myYouTubePlayerFragment.initialize(ConfigYoutube.DEVELOPER_KEY, Mostrar.this);
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider,
        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
        errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
        String errorMessage = String.format(
        "There was an error initializing the YouTubePlayer (%1$s)",
        errorReason.toString());
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                    boolean wasRestored) {
        if (!wasRestored) {
               // this.player = player;
                 player.loadVideo(VIDEO_ID);
          }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
// Retry initialization if user performed a recovery action
        getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
          }
        }

        protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayer.Provider) findViewById(R.id.youtubeplayerfragment);
          }
        }