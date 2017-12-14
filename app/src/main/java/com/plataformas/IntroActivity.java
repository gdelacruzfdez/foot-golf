package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class IntroActivity extends Activity {

    private static int SPLASH_TIME_OUT = 4000;

    boolean creado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jump();
            }
        }, SPLASH_TIME_OUT);


    }

    public void onClick(View v) {
        jump();
    }

    private void jump() {
        if (creado == false) {
            creado = true;
            Intent homeIntent = new Intent(IntroActivity.this, MenuActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }
}
