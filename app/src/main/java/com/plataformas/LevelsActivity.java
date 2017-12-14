package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LevelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
    }

    public void VolverOnClick(View view) {
        Intent homeIntent = new Intent(LevelsActivity.this, MenuActivity.class);
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString("levels", "0");
        //Add the bundle to the intent
        homeIntent.putExtras(bundle);


        startActivity(homeIntent);
       // finish();
    }

    public void NivelOnClick(View view) {
        Intent homeIntent = new Intent(LevelsActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
