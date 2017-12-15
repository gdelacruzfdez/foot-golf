package com.plataformas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PelotasActivity extends Activity {


    Button fut;
    Button bask;
    Button tenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelotas);
        fut = (Button) findViewById(R.id.bFut);
        bask = (Button) findViewById(R.id.bBask);
        tenis = (Button) findViewById(R.id.bTenis);


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
/*
        switch (cond) {

            case "f":
                fut.setBackground(R.drawable.futa);
                bask.setBackground(R.drawable.bascket);
                tenis.setBackground(R.drawable.tenis);
                break;
            case "t":

                fut.setBackground(R.drawable.fut);
                bask.setBackground(R.drawable.bascketa);
                tenis.setBackground(R.drawable.tenis);
                break;
            case "b":

                fut.setBackground(R.drawable.fut);
                bask.setBackground(R.drawable.bascket);
                tenis.setBackground(R.drawable.tenisa);
                break;

*/
    }

    private void asignarActual(String cond) {
        Context context = this;
        SharedPreferences sharedPrefs = getSharedPreferences("DatosSP", context.MODE_PRIVATE);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("pelota", cond);
        editor.commit();

    }


}
