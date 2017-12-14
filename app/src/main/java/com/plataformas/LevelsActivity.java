package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
    }

    public void VolverOnClick(View view) {
        Intent homeIntent = new Intent(LevelsActivity.this, MenuActivity.class);
        startActivity(homeIntent);
        finish();
    }

    public void NivelOnClick(View view) {
        Intent homeIntent = new Intent(LevelsActivity.this, MainActivity.class);


        Button b = (Button) findViewById(view.getId());
        homeIntent.putExtra("LEVEL", Integer.parseInt(b.getText().toString()));
        startActivity(homeIntent);
        finish();
    }
}
