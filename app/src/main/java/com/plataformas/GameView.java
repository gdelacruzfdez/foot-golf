package com.plataformas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.plataformas.modelos.Nivel;
import com.plataformas.modelos.controles.BotonDisparar;
import com.plataformas.modelos.controles.BotonSaltar;
import com.plataformas.modelos.controles.IconoVida;
import com.plataformas.modelos.controles.Marcador;
import com.plataformas.modelos.controles.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Marcador marcador;

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    private Nivel nivel;
    public int numeroNivel = 0;

    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];


    boolean duranteTiro = false;

    public void procesarEventosTouch() {
        boolean pulsacionPadMover = false;


        for (int i = 0; i < 6; i++) {
            if (accion[i] != NO_ACTION) {

                if (accion[i] == ACTION_DOWN) {
                    if (nivel.nivelPausado)
                        nivel.nivelPausado = false;
                    duranteTiro = true;
                    nivel.xInicioTiro = x[i];
                    nivel.yInicioTiro = y[i];
                }
                if (accion[i] == ACTION_MOVE) {
                    nivel.xFinalTiro = x[i];
                    nivel.yFinalTiro = y[i];

                }
                if (accion[i] == ACTION_UP) {
                    nivel.xFinalTiro = x[i];
                    nivel.yFinalTiro = y[i];
                    duranteTiro = false;
                    nivel.disparado = false;

                }


            }
        }
    }


    public Marcador getMarcador() {
        return marcador;
    }

    protected void inicializar() throws Exception {
        nivel = new Nivel(context, numeroNivel);
        nivel.gameView = this;


        if (marcador == null) {
            marcador = new Marcador(context, GameView.pantallaAncho * 0.9, GameView.pantallaAlto * 0.1);
        }


    }

    public void actualizar(long tiempo) throws Exception {
        if (!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
        }
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);

        if (!nivel.nivelPausado) {
            marcador.dibujar(canvas);

            if (duranteTiro) {
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                float xPelota = (float) nivel.getPelota().getCoordenadaXDibujarPelota();
                float yPelota = (float) nivel.getPelota().getCoordenadaYDibujarPelota();

                float xPuntoFinal = xPelota + nivel.xInicioTiro - nivel.xFinalTiro;
                float yPuntoFinal = yPelota + nivel.yInicioTiro - nivel.yFinalTiro;
                double distance = Math.sqrt(Math.pow(xPelota - xPuntoFinal, 2) + Math.pow(yPelota - yPuntoFinal, 2));
                if (distance > nivel.MAX_POWER) {
                    float ratio = (float) (nivel.MAX_POWER / distance);
                    xPuntoFinal = (1 - ratio) * xPelota + ratio * xPuntoFinal;
                    yPuntoFinal = (1 - ratio) * yPelota + ratio * yPuntoFinal;
                }
                canvas.drawLine(xPelota, yPelota, xPuntoFinal, yPuntoFinal, p);
            }

        }
    }

    public void nivelCompleto() throws Exception {

        if (numeroNivel < 1) { // Número Máximo de Nivel
            numeroNivel++;
        } else {
            numeroNivel = 0;
        }
        inicializar();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            } catch (InterruptedException e) {
            }
        }
    }

}

