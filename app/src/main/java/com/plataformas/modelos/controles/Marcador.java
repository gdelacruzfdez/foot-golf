package com.plataformas.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.plataformas.GameView;
import com.plataformas.modelos.Modelo;

public class Marcador extends Modelo {


    private int puntos = 0;


    public Marcador(Context context, double x, double y) {
        super(context, x, y, 40, 40);
    }

    @Override
    public void dibujar(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        canvas.drawText(String.valueOf(puntos), (int) x, (int) y, paint);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void incrementarPuntos() {
        puntos++;
    }
}