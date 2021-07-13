package com.example.android.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class FlashActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        //instantiate videoview
        videoView=findViewById(R.id.videoView);

        //parse video in raw folder to uri
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.microsoft_logo_black);

        //set uri to videoview
        videoView.setVideoURI(uri);

        //set video on loop
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        //start video
        videoView.start();

        //making this activity a splash activity (2 seconds) using runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FlashActivity.this,Login_Activity.class));
            }
        },2000);
    }
}