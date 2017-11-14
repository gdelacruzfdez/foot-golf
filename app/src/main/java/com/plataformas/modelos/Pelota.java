package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Gonzalo on 13/11/2017.
 */

public class Pelota extends Modelo {

    public static final String PELOTA_MOVIMIENTO = "Pelota_movimiento";
    public static final String PELOTA_PARADA = "Pelota_parada";
    double velocidadX;
    float velocidadY; // actual


    boolean enMovimiento = false;
    public boolean enElAire; // está en el aire

    double xInicial;
    double yInicial;

    private Sprite sprite;
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

    public Pelota(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 22, 22);
        inicializar();

        this.xInicial = xInicial;
        this.yInicial = yInicial - altura / 2;

        this.x = this.xInicial;
        this.y = this.yInicial;
    }

    public void inicializar() {
        Sprite pelota_movimiento = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ball_sprite),
                ancho, altura,
                9, 3, true);
        sprites.put(PELOTA_MOVIMIENTO, pelota_movimiento);

        Sprite pelota_parada = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.ball_sprite_parado),
                ancho, altura,
                1, 1, true);
        sprites.put(PELOTA_PARADA, pelota_parada);

        sprite = pelota_parada;
    }


    public void dibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY, false);
    }

    public int getCoordenadaXDibujarPelota(){
        return (int) (x - Nivel.scrollEjeX);
    }

    public int getCoordenadaYDibujarPelota(){
        return (int) y - Nivel.scrollEjeY;
    }

    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
    }

    public void restablecerPosicionInicial() {
        this.x = xInicial;
        this.y = yInicial;
    }

    public void setxInicial(double xInicial) {
        this.xInicial = xInicial;
    }

    public void setyInicial(double yInicial) {
        this.yInicial = yInicial;
    }
}
