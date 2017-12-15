package com.plataformas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    GameView gameView = null;
    int level;

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();


        level = getIntent().getIntExtra("LEVEL",0);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        gameView = new GameView(this, getBall());
        setContentView(gameView);
        gameView.numeroNivel = level;
        gameView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(MainActivity.this, LevelsActivity.class);
        startActivity(homeIntent);
        finish();
        System.gc();

        synchronized (gameView.gameloop) {
            gameView.context = null;
            gameView.gameloop.setRunning(false);
            gameView = null;
        }

    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    private String getBall(){
        SharedPreferences sharedPref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        String result = sharedPref.getString("pelota", "f");
        return result;
    }




}