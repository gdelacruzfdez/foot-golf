package com.plataformas.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

/**
 * Created by Gonzalo on 07/12/2017.
 */


public class Pinchos extends Modelo {
    public Pinchos(Context context, double x, double y) {
        super(context, x, y, 32, 40);
        this.y = y - altura / 2;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pinchos);
    }


    public void dibujar(Canvas canvas) {
        int yArriva = (int) y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }
}

