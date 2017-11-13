package com.plataformas.modelos.controles;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.modelos.Modelo;

public class IconoVida extends Modelo {

    public IconoVida(Context context, double x, double y) {
        super(context, x, y, 40,40);
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.life);
    }
}
