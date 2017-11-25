package com.plataformas.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.plataformas.modelos.Modelo;

/**
 * Created by Gonzalo on 25/11/2017.
 */

public class ContadorTiros extends Modelo {

    int limiteDeTiros = 0;
    int numeroDeTiros = 0;


    public ContadorTiros(Context context, double x, double y, int limiteDeTiros) {
        super(context, x, y, 60, 60);
        this.limiteDeTiros = limiteDeTiros;
    }

    @Override
    public void dibujar(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        canvas.drawText("Tiros:" + String.valueOf(numeroDeTiros) + "/" + limiteDeTiros, (int) x, (int) y, paint);
    }

    public void incrementarTiros() {
        this.numeroDeTiros++;
    }

    public boolean limiteDeTirosAlcanzado() {
        return limiteDeTiros == numeroDeTiros;
    }

    public void reiniciarContador() {
        this.numeroDeTiros = 0;
    }
}
