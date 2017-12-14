package com.plataformas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PelotasActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelotas);
    }

    public void VolverOnClick(View view) {
        Intent homeIntent = new Intent(PelotasActivity.this, OpsActivity.class);
        startActivity(homeIntent);
        finish();
    }

}
