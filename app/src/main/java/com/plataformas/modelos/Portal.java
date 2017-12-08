package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by torre on 08/12/2017.
 */

public class Portal extends Modelo {

    //nace normalmete como entrada
    public int orden = ENTRADA;
    public int IDUnion;
    public static final int ENTRADA= 1;
    public static final int SALIDA = 0;

    public Portal(Context context, double x, double y)
    {

        super(context, x, y, 32, 32);
        this.y =  y - altura/2;

        this.imagen = CargadorGraficos.cargarDrawable(context, R.drawable.entrada_portal);
    }

    public void dibujar(Canvas canvas) {
        int yArriva = (int) y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);

    }

//Numero que comparten salida y entrada
    public void setIDUnion(int IDUnion) {
        this.IDUnion = IDUnion;
    }

    @Override
    public void actualizar(long tiempo) {
        if (orden==SALIDA) {
            this.imagen = CargadorGraficos.cargarDrawable(context, R.drawable.salida_portal);
        }
    }
//asegura el orden como salida y cambia su sprite
    public void construirComoSalida()
    {
        orden=SALIDA;
    }




}
