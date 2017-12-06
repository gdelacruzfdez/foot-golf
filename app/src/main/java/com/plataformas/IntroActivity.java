package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class IntroActivity extends Activity {

    private static int SPLASH_TIME_OUT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);/*
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

        try {
            VideoView videoView = new VideoView(this);
            setContentView(videoView);
            Uri path = Uri.parse("android.resources://" + getPackageName()+"/" + R.raw.intro);
            videoView.setVideoURI(path);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoView.start();
        } catch (Exception e) {
        }
    }

    public void onClick(View v) {
        jump();
    }

    private void jump() {
        if (isFinishing())
            return;
        Intent homeIntent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
