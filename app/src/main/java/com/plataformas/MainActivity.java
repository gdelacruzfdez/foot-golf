package com.plataformas;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    GameView gameView = null;
    int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        //level =Integer.parseInt(bundle.getString("levels"));


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        gameView = new GameView(this);
        setContentView(gameView);
        gameView.numeroNivel = 0;
        gameView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.gc();

        synchronized(gameView.gameloop)
        {
            gameView.context = null;
            gameView.gameloop.setRunning(false);
            gameView = null;
        }
    }
}