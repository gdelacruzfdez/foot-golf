package com.plataformas.modelos.enemigo;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;
import com.plataformas.modelos.Modelo;
import com.plataformas.modelos.Nivel;

import java.util.HashMap;

/**
 * Created by Gonzalo on 07/12/2017.
 */


public class Pinchos extends Modelo {

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int OCULTO = 0;
    public static final int ACTIVANDOSE = 2;
    public static final int OCULTANDOSE = 3;

    public static final String ACTIVOS = "activos";
    public static final String INACTIVOS = "inactivos";
    public static final String ACTIVACION = "activacion";
    public static final String DESACTIVACION = "desactivacion";

    Sprite sprite;
    HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    double duracion = 4000;
    public double ultimaActividad = 0;



    public Pinchos(Context context, double x, double y) {
        super(context, x, y, 32, 40);
        this.y = y - altura / 2;



        inicializar();

    }

    public void inicializar() {
        Sprite activos = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.pinchos),
                ancho, altura,
                6, 1, true);
        sprites.put(ACTIVOS, activos);

        Sprite inactivos = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.pinchos_ocultos),
                ancho, altura,
                6, 1, true);
        sprites.put(INACTIVOS, inactivos);

        Sprite activacion = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.pinchos_saliendo),
                ancho, altura,
                8, 7, false);
        sprites.put(ACTIVACION, activacion);

        Sprite desactivacion = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.pinchos_oculatandose),
                ancho, altura,
                8, 7, false);
        sprites.put(DESACTIVACION, desactivacion);

        sprite=activos;

    }

    @Override
    public void actualizar(long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == OCULTANDOSE && finSprite == true)
        {

            sprite=sprites.get(INACTIVOS);

            Sprite desactivacion = new Sprite(
                    CargadorGraficos.cargarDrawable(context, R.drawable.pinchos_oculatandose),
                    ancho, altura,
                    8, 7, false);
            sprites.put(DESACTIVACION, desactivacion);

            estado = OCULTO;

            ultimaActividad=System.currentTimeMillis();
        }
        else if (estado == ACTIVANDOSE&& finSprite == true)
        {

            sprite=sprites.get(ACTIVOS);


            Sprite activacion = new Sprite(
                    CargadorGraficos.cargarDrawable(context, R.drawable.pinchos_saliendo),
                    ancho, altura,
                    8, 7, false);
            sprites.put(ACTIVACION, activacion);

            estado = ACTIVO;

            ultimaActividad=System.currentTimeMillis();
        }
        else if(estado == OCULTO) {



            if (System.currentTimeMillis() - ultimaActividad > duracion  )
            {

                    sprite=sprites.get(ACTIVACION);
                    estado=ACTIVANDOSE;

            }

        }
        else if(estado==ACTIVO){


            if (System.currentTimeMillis() - ultimaActividad > duracion )
            {

                sprite=sprites.get(DESACTIVACION);
                estado=OCULTANDOSE;
            }
        }


    }


    public void dibujar(Canvas canvas) {

        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }
}

