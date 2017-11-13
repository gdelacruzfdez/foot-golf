package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;

/**
 * Created by Gonzalo on 20/10/2017.
 */

public class CheckPoint extends Modelo {

    private boolean alcanzado = false;

    public CheckPoint(Context context, double x, double y) {
        super(context, x, y, 32, 32);
        this.y =  y - altura/2;

        this.imagen = CargadorGraficos.cargarDrawable(context, R.drawable.aro_rojo);
    }

    public void dibujar(Canvas canvas) {
        int yArriva = (int) y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);

    }

    public boolean isAlcanzado() {
        return alcanzado;
    }


    @Override
    public void actualizar(long tiempo) {
        if (isAlcanzado()) {
            this.imagen = CargadorGraficos.cargarDrawable(context, R.drawable.aro_verde);
        }
    }

    public void alcanzar() {
        this.alcanzado = true;
    }
}
