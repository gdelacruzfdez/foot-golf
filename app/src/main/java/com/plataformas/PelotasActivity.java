package com.plataformas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PelotasActivity extends Activity {


    Button futbol;
    Button basket;
    Button tenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelotas);
        futbol = (Button) findViewById(R.id.bFut);
        basket = (Button) findViewById(R.id.bBask);
        tenis = (Button) findViewById(R.id.bTenis);

        SharedPreferences sharedPref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        String result = sharedPref.getString("pelota", "f");
        paintButtons(result.toString());


    }

    public void VolverOnClick(View view) {
        Intent homeIntent = new Intent(PelotasActivity.this, OpsActivity.class);
        startActivity(homeIntent);
        finish();
    }


    public void CambiarPelotaOnClick(View view) {
        Button b = (Button) findViewById(view.getId());
        String cond = b.getText().toString();
        asignarActual(cond);

        paintButtons(cond);
    }

    private void paintButtons(String cond) {
        switch (cond) {
            case "f":
                futbol.setBackgroundResource(R.drawable.futa);
                basket.setBackgroundResource(R.drawable.bascket);
                tenis.setBackgroundResource(R.drawable.tenis);
                break;
            case "t":
                futbol.setBackgroundResource(R.drawable.fut);
                basket.setBackgroundResource(R.drawable.bascket);
                tenis.setBackgroundResource(R.drawable.tenisa);
                break;
            case "b":
                futbol.setBackgroundResource(R.drawable.fut);
                basket.setBackgroundResource(R.drawable.bascketa);
                tenis.setBackgroundResource(R.drawable.tenis);
                break;


        }
    }

    private void asignarActual(String cond) {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pelota", cond);
        editor.commit();
    }


}
