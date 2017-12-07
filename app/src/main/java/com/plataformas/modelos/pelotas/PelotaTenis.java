package com.plataformas.modelos.pelotas;

import android.content.Context;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

/**
 * Created by Gonzalo on 07/12/2017.
 */

public class PelotaTenis extends Pelota {

    public static final double VALOR_ROZAMIENTO = 0.7f;
    public static final double FACTOR_REDUCCION_VELOCIDAD = 4.75;
    public static final double FACTOR_REBOTE = 1.75;

    public PelotaTenis(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial);
        this.rozamiento = VALOR_ROZAMIENTO;
        this.factorReduccionVelocidad = FACTOR_REDUCCION_VELOCIDAD;
        this.factorRebote = FACTOR_REBOTE;
    }

    @Override
    public void inicializar() {
        Sprite pelota_movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ball_tennis1),
                ancho, altura,
                1, 1, true);
        sprites.put(PELOTA_MOVIMIENTO, pelota_movimiento);

        Sprite pelota_parada = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ball_tennis1),
                ancho, altura,
                1, 1, true);
        sprites.put(PELOTA_PARADA, pelota_parada);

        sprite = pelota_parada;
    }
}
