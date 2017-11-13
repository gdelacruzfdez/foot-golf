package com.plataformas.modelos.enemigo;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Gonzalo on 21/10/2017.
 */

public class EnemigoSalto extends AbstractEnemigo {

    public static final String SALTANDO_DERECHA = "saltando_derecha";
    public static final String SALTANDO_IZQUIERDA = "saltando_izquierda";


    int cadenciaSalto = 2000;
    public double ultimoSalto = 0;
    public boolean salto = true;
    public boolean enElAire = false;
    public float velocidadY;
    public float velocidadSalto = -8;

    public EnemigoSalto(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        this.x = xInicial;
        this.y = yInicial - altura / 2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 25;
        cAbajo = 25;
        velocidadX = 1.2;
        inicializar();
    }

    public void saltar(long tiempo) {
        if (tiempo - ultimoSalto > cadenciaSalto) {
            salto = true;
            ultimoSalto = tiempo;
        }
    }

    public void inicializar() {
        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrunright),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemyrun),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);


        sprite = caminandoDerecha;
    }

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

        if (salto) {
            salto = false;
            enElAire = true;
            velocidadY = velocidadSalto;
        }

    }
}
