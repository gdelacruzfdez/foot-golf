package com.plataformas;

import android.content.Context;
import android.graphics.Canvas;
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

    //Controles
    private Pad pad;
    private BotonSaltar botonSaltar;
    private BotonDisparar botonDisparar;
    private IconoVida[] iconosVida;
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

    public void procesarEventosTouch() {
        boolean pulsacionPadMover = false;

        for (int i = 0; i < 6; i++) {
            if (accion[i] != NO_ACTION) {

                if (accion[i] == ACTION_DOWN) {
                    if (nivel.nivelPausado)
                        nivel.nivelPausado = false;
                }

                if (botonDisparar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonDispararPulsado = true;
                    }
                }


                if (botonSaltar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonSaltarPulsado = true;
                    }
                }

                if (pad.estaPulsado(x[i], y[i])) {

                    float orientacion = pad.getOrientacionX(x[i]);

                    // Si almenosuna pulsacion está en el pad
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        nivel.orientacionPad = orientacion;
                    }
                }
            }
        }
        if (!pulsacionPadMover) {
            nivel.orientacionPad = 0;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla", "Tecla pulsada: " + keyCode);
        if (keyCode == 32) {
            nivel.orientacionPad = -0.5f;
        }
        if (keyCode == 29) {
            nivel.orientacionPad = 0.5f;
        }
        if (keyCode == 47) {
            nivel.orientacionPad = 0;
        }
        if (keyCode == 51) {
            nivel.botonSaltarPulsado = true;
        }
        if (keyCode == 62) {
            nivel.botonDispararPulsado = true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 32 || keyCode == 29) {
            nivel.orientacionPad = 0;
        }
        return super.onKeyDown(keyCode, event);
    }


    public Marcador getMarcador() {
        return marcador;
    }

    protected void inicializar() throws Exception {
        nivel = new Nivel(context, numeroNivel);
        pad = new Pad(context);
        botonSaltar = new BotonSaltar(context);
        botonDisparar = new BotonDisparar(context);
        nivel.gameView = this;

        iconosVida = new IconoVida[3];

        if(marcador == null) {
            marcador = new Marcador(context, GameView.pantallaAncho * 0.9, GameView.pantallaAlto * 0.1);
        }

        iconosVida[0] = new IconoVida(context, GameView.pantallaAncho * 0.05,
                GameView.pantallaAlto * 0.1);
        iconosVida[1] = new IconoVida(context, GameView.pantallaAncho * 0.15,
                GameView.pantallaAlto * 0.1);
        iconosVida[2] = new IconoVida(context, GameView.pantallaAncho * 0.25,
                GameView.pantallaAlto * 0.1);


    }

    public void actualizar(long tiempo) throws Exception {
        if (!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
        }
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        if (!nivel.nivelPausado) {
            pad.dibujar(canvas);
            botonSaltar.dibujar(canvas);
            botonDisparar.dibujar(canvas);
            marcador.dibujar(canvas);
            for (int i = 0; i < nivel.getJugador().vidas; i++)
                iconosVida[i].dibujar(canvas);

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

