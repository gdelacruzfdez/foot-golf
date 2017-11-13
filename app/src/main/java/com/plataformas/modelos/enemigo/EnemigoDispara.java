package com.plataformas.modelos.enemigo;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Gonzalo on 21/10/2017.
 */

public class EnemigoDispara extends AbstractEnemigo {

    double cadencia = 1000;
    public double ultimoDisparo = 0;
    public boolean disparo = true;

    public EnemigoDispara(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 64, 64);

        this.x = xInicial;
        this.y = yInicial - altura / 2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 20;
        velocidadX = 1.5;

        inicializar();
    }

    @Override
    public void inicializar() {
        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright_1),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun_1),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright_1),
                ancho, altura,
                4, 6, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie_1),
                ancho, altura,
                4, 6, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);


        sprite = caminandoDerecha;
    }

    @Override
    public void actualizar(long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == INACTIVO && finSprite == true) {
            estado = ELIMINAR;
        }

        if (estado == INACTIVO) {
            if (velocidadX > 0)
                sprite = sprites.get(MUERTE_DERECHA);
            else
                sprite = sprites.get(MUERTE_IZQUIERDA);

        } else {

            if (velocidadX > 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
            }
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
        }

        if (System.currentTimeMillis() - ultimoDisparo > cadencia) {
            disparo = true;
        }
    }
}
