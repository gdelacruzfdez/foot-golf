package com.plataformas.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

import java.util.HashMap;

/**
 * Created by Gonzalo on 20/10/2017.
 */

public abstract class AbstractEnemigo extends Modelo implements EnemigoInterface {

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;


    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";

    public static final int IZQUIERDA = -1;
    public static final int DERECHA = 1;
    public int orientacion;

    Sprite sprite;
    HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public double velocidadX;

    public AbstractEnemigo(Context context, double x, double y, int altura, int ancho) {
        super(context, x, y, altura, ancho);
    }


    @Override
    public void girar() {
        velocidadX = velocidadX * -1;
    }

    @Override
    public void destruir() {
        velocidadX = 0;
        estado = INACTIVO;
    }

    @Override
    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }

    public int getEstado() {
        return estado;
    }

    public double getVelocidadX() {
        return velocidadX;
    }
}
