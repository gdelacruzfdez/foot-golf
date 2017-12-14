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

public class Lanzador extends Modelo {

    public static final String DESACTIVADO = "LANZADOR_DESACTIVADO";
    public static final String ACTIVADO = "LANZADOR_ACTIVADO";
    public boolean activado = false;
    Sprite sprite;
    HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public Lanzador(Context context, double x, double y) {
        super(context, x, y, 32, 40);
        this.y = y - altura / 2;

        inicializar();
    }

    public void inicializar() {
        Sprite desactivado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.impulso_1),
                ancho, altura,
                1, 1, true);
        sprites.put(DESACTIVADO, desactivado);

        Sprite activado = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.impulso_2),
                ancho, altura,
                1, 1, true);
        sprites.put(ACTIVADO, activado);
        sprite = desactivado;
    }

    @Override
    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }


    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);

        if (activado) {
            sprite = sprites.get(ACTIVADO);
        } else {
            sprite = sprites.get(DESACTIVADO);
        }

    }
}

