package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Gonzalo de la Cruz on 17/10/2017.
 */

public class Recolectable extends Modelo {

    private Sprite sprite;
    public static final String ANIMACION_RECOLECTABLE = "ANIMACION_RECOLECTABLE";
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public Recolectable(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 32, 32);

        this.x = xInicial;
        this.y = yInicial - altura / 2;



        inicializar();
    }

    public void inicializar() {
        Sprite animacion = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.gem),
                ancho, altura,
                4, 8, true);
        sprites.put(ANIMACION_RECOLECTABLE, animacion);

        sprite = animacion;
    }

    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }

    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
    }
}
