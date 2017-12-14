package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void JugarOnClick(View view) {
        Intent homeIntent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    public void OpcionesOnClick(View view) {
        Intent homeIntent = new Intent(MenuActivity.this, OpcionesActivity.class);
        startActivity(homeIntent);
        finish();
    }

    public void SalirOnClick(View view) {
        System.exit(0);

    }


}
