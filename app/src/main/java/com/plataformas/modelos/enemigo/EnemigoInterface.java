package com.plataformas.modelos.enemigo;

import android.graphics.Canvas;

/**
 * Created by Gonzalo on 20/10/2017.
 */

public interface EnemigoInterface {

    void inicializar();

    void girar();

    void actualizar(long tiempo);

    void destruir();

    void dibujar(Canvas canvas);

}
