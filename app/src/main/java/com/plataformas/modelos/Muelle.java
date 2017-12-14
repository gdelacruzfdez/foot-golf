package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Gonzalo on 14/12/2017.
 */

public class Muelle extends Modelo {

    int numeroRebotes = 0;
    int maximoRebotes = 3;

    public Muelle(Context context, double x, double y) {
        super(context, x, y, 32, 40);
        this.y = y - altura / 2;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.muelle);

    }


    @Override
    public void dibujar(Canvas canvas) {
        int yArriva = (int) y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }

    public boolean haLlegadoAlMaximoDeRebotes() {
        return numeroRebotes >= maximoRebotes;
    }

    public void reiniciarRebotes() {
        this.numeroRebotes = 0;
    }

}