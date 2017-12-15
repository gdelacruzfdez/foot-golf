package com.plataformas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class OpsActivity extends Activity {

    int i;
    ImageButton ig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ops);

        ig = (ImageButton) findViewById(R.id.musicBoton);

        i = 0;

        if (i == 0) {
            ig.setImageResource(R.drawable.boton_musica_activada);
        } else {
            ig.setImageResource(R.drawable.boton_musica_desactivada);
        }
    }

    public void MusicaOnClick(View view) {
        if (i == 0) {
            i = 1;
            ig.setImageResource(R.drawable.boton_musica_desactivada);

            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        } else {
            i = 0;
            ig.setImageResource(R.drawable.boton_musica_activada);

            AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }

    public void PelotasOnClick(View view) {
        Intent homeIntent = new Intent(OpsActivity.this, PelotasActivity.class);
        startActivity(homeIntent);
        finish();
    }

    public void VolverOnClick(View view) {
        Intent homeIntent = new Intent(OpsActivity.this, MenuActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
