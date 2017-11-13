package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Gonzalo on 21/10/2017.
 */

public class DisparoEnemigo extends Modelo {

    private Sprite sprite;
    public double velocidadX = 10;

    public DisparoEnemigo(Context context, double xInicial, double yInicial, double velocidadXEnemigo) {
        super(context, xInicial, yInicial, 32, 40);

        if (velocidadXEnemigo < 0)
            velocidadX = velocidadX * -1;


        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        inicializar();
    }

    public void inicializar() {
        sprite = new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.animacion_disparo1),
                ancho, altura,
                24, 4, true);
    }

    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }
}

