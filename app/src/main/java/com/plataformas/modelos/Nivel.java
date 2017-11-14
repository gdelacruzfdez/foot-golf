package com.plataformas.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.plataformas.GameView;
import com.plataformas.R;
import com.plataformas.gestores.CargadorGraficos;
import com.plataformas.gestores.Utilidades;
import com.plataformas.modelos.enemigo.AbstractEnemigo;
import com.plataformas.modelos.enemigo.Enemigo;
import com.plataformas.modelos.enemigo.EnemigoDispara;
import com.plataformas.modelos.enemigo.EnemigoInterface;
import com.plataformas.modelos.enemigo.EnemigoSalto;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    public GameView gameView;
    private Context context = null;
    private int numeroNivel;
    private Fondo[] fondos;
    private Tile[][] mapaTiles;
    private Pelota pelota;

    private List<CheckPoint> checkpoints;
    private List<EnemigoInterface> enemigos;
    private List<DisparoJugador> disparosJugador;
    private List<DisparoEnemigo> disparosEnemigos;
    private List<Recolectable> recolectables;
    private Meta meta;


    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;
    private float velocidadGravedad = 0.8f;
    private float velocidadMaximaCaida = 10;

    public Bitmap mensaje;
    public boolean nivelPausado;

    public boolean inicializado;


    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar() throws Exception {
        scrollEjeX = 0;
        scrollEjeY = 0;
        mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.description);
        nivelPausado = true;

        enemigos = new LinkedList<EnemigoInterface>();
        disparosJugador = new LinkedList<DisparoJugador>();
        disparosEnemigos = new LinkedList<>();
        recolectables = new ArrayList<>();
        checkpoints = new ArrayList<>();
        fondos = new Fondo[2];
        fondos[0] = new Fondo(context, CargadorGraficos.cargarBitmap(context,
                R.drawable.capa1), 0);
        fondos[1] = new Fondo(context, CargadorGraficos.cargarBitmap(context,
                R.drawable.capa2), 1f);

        inicializarMapaTiles();
    }


    public void actualizar(long tiempo) throws Exception {
        if (inicializado) {

            for (EnemigoInterface enemigo : enemigos) {
                AbstractEnemigo e = (AbstractEnemigo) enemigo;
                e.actualizar(tiempo);
                if (e instanceof EnemigoDispara) {
                    EnemigoDispara enemigoDispara = (EnemigoDispara) e;
                    if (enemigoDispara.disparo) {
                        disparosEnemigos.add(new DisparoEnemigo(context, e.x, e.y, e.velocidadX));
                        enemigoDispara.disparo = false;
                        enemigoDispara.ultimoDisparo = System.currentTimeMillis();
                    }
                }

            }

            for (DisparoEnemigo disparo : disparosEnemigos) {
                disparo.actualizar(tiempo);
            }


            for (DisparoJugador disparoJugador : disparosJugador) {
                disparoJugador.actualizar(tiempo);
            }




            for (Recolectable r : recolectables) {
                r.actualizar(tiempo);
            }

            for (CheckPoint c : checkpoints) {
                c.actualizar(tiempo);
            }

            pelota.actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondos[0].dibujar(canvas);
            fondos[1].dibujar(canvas);
            dibujarTiles(canvas);

            for (DisparoJugador disparoJugador : disparosJugador) {
                disparoJugador.dibujar(canvas);
            }

            for (DisparoEnemigo disparoEnemigo : disparosEnemigos) {
                disparoEnemigo.dibujar(canvas);
            }

            pelota.dibujar(canvas);

            for (EnemigoInterface enemigo : enemigos) {
                enemigo.dibujar(canvas);
            }

            meta.dibujar(canvas);

            for (Recolectable r : recolectables) {
                r.dibujar(canvas);
            }

            for (CheckPoint c : checkpoints) {
                c.dibujar(canvas);
            }

            if (nivelPausado) {
                // la foto mide 480x320
                Rect orgigen = new Rect(0, 0,
                        480, 320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                Rect destino = new Rect((int) (GameView.pantallaAncho / 2 - 480 / 2),
                        (int) (GameView.pantallaAlto / 2 - 320 / 2),
                        (int) (GameView.pantallaAncho / 2 + 480 / 2),
                        (int) (GameView.pantallaAlto / 2 + 320 / 2));
                canvas.drawBitmap(mensaje, orgigen, destino, null);
            }

        }
    }

    private void dibujarTiles(Canvas canvas) {

        int tileXJugador = (int) pelota.x / Tile.ancho;
        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(pelota.x - scrollEjeX));
        izquierda = Math.max(0, izquierda); // Que nunca sea < 0, ej -1

        if (pelota.x <
                anchoMapaTiles() * Tile.ancho - GameView.pantallaAncho * 0.3)
            if (pelota.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                fondos[0].mover((int) pelota.x - GameView.pantallaAncho * 0.7 - scrollEjeX);
                fondos[1].mover((int) pelota.x - GameView.pantallaAncho * 0.7 - scrollEjeX);
                scrollEjeX += (int) ((pelota.x - scrollEjeX) - GameView.pantallaAncho * 0.7);
            }


        if (pelota.x > GameView.pantallaAncho * 0.3)
            if (pelota.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                fondos[0].mover((int) pelota.x - GameView.pantallaAncho * 0.3 - scrollEjeX);
                fondos[1].mover((int) pelota.x - GameView.pantallaAncho * 0.3 - scrollEjeX);
                scrollEjeX -= (int) (GameView.pantallaAncho * 0.3 - (pelota.x - scrollEjeX));
            }


        if (pelota.y > GameView.pantallaAlto * 0.3) {
            if (pelota.y - scrollEjeY > GameView.pantallaAlto * 0.7) {
                scrollEjeY -= (int) (GameView.pantallaAlto * 0.7 - (pelota.y - scrollEjeY));
            }
        }

        if (pelota.y < altoMapaTiles() * Tile.altura - GameView.pantallaAlto * 0.3) {
            if (pelota.y - scrollEjeY < GameView.pantallaAlto * 0.3) {
                scrollEjeY += (int) ((pelota.y - scrollEjeY) - GameView.pantallaAlto * 0.3);
            }
        }


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);


        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo

                    mapaTiles[x][y].imagen.setBounds(
                            (x * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura) + Tile.altura - scrollEjeY);

                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private float tilesEnDistanciaX(double distanciaX) {
        return (float) distanciaX / Tile.ancho;
    }


    public int anchoMapaTiles() {
        return mapaTiles.length;
    }

    public int altoMapaTiles() {

        return mapaTiles[0].length;
    }


    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }


    private Tile inicializarTile(char codigoTile, int x, int y) {
        switch (codigoTile) {
            case 'S':
                int xCentroAbajoTileS = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileS = y * Tile.altura + Tile.altura;
                enemigos.add(new EnemigoSalto(context, xCentroAbajoTileS, yCentroAbajoTileS));
                return new Tile(null, Tile.PASABLE);
            case 'P':
                int xCentroAbajoTileP = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileP = y * Tile.altura + Tile.altura;
                pelota = new Pelota(context, xCentroAbajoTileP, yCentroAbajoTileP);
                return new Tile(null, Tile.PASABLE);
            case 'D':
                int xCentroAbajoTileD = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileD = y * Tile.altura + Tile.altura;
                enemigos.add(new EnemigoDispara(context, xCentroAbajoTileD, yCentroAbajoTileD));
                return new Tile(null, Tile.PASABLE);
            case 'A':
                int xCentroAbajoTileA = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileA = y * Tile.altura + Tile.altura;
                checkpoints.add(new CheckPoint(context, xCentroAbajoTileA, yCentroAbajoTileA));
                return new Tile(null, Tile.PASABLE);
            case 'M':
                int xCentroAbajoTileM = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileM = y * Tile.altura + Tile.altura;
                meta = new Meta(context, xCentroAbajoTileM, yCentroAbajoTileM);
                return new Tile(null, Tile.PASABLE);
            case 'R':
                int xCentroAbajoTileR = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileR = y * Tile.altura + Tile.altura;
                recolectables.add(new Recolectable(context, xCentroAbajoTileR, yCentroAbajoTileR));
                return new Tile(null, Tile.PASABLE);
            case 'E':
                // Enemigo
                // Posición centro abajo
                int xCentroAbajoTileE = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileE = y * Tile.altura + Tile.altura;
                enemigos.add(new Enemigo(context, xCentroAbajoTileE, yCentroAbajoTileE));
                return new Tile(null, Tile.PASABLE);
            case '.':
                // en blanco, sin textura
                return new Tile(null, Tile.PASABLE);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.musgo), Tile.SOLIDO);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }



    private void aplicarReglasMovimiento() throws Exception {

        int tileXJugadorIzquierda
                = (int) (pelota.x - (pelota.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (pelota.x + (pelota.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (pelota.y + (pelota.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) pelota.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (pelota.y - (pelota.altura / 2 - 1)) / Tile.altura;

        for (Iterator<EnemigoInterface> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            AbstractEnemigo enemigo = (AbstractEnemigo) iterator.next();

            if (enemigo.estado == Enemigo.ELIMINAR) {
                iterator.remove();
                continue;
            }


            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;
            int tileXEnemigoDerecha =
                    (int) (enemigo.x + (enemigo.ancho / 2 - 1)) / Tile.ancho;

            int tileYEnemigoInferior =
                    (int) (enemigo.y + (enemigo.altura / 2 - 1)) / Tile.altura;
            int tileYEnemigoCentro =
                    (int) enemigo.y / Tile.altura;
            int tileYEnemigoSuperior =
                    (int) (enemigo.y - (enemigo.altura / 2 - 1)) / Tile.altura;


            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda) {

                if (pelota.colisiona(enemigo)) {
                    //TODO FINALIZAR JUEGO O REINICIAR NIVEL, PIERDE
                }
            }


            if (enemigo.velocidadX > 0) {
                //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
                if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision ==
                                Tile.SOLIDO) {

                    enemigo.x += enemigo.velocidadX;

                    // Sino, me acerco al borde del que estoy
                } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1) {

                    int TileEnemigoDerecho = tileXEnemigoDerecha * Tile.ancho + Tile.ancho;
                    double distanciaX = TileEnemigoDerecho - (enemigo.x + enemigo.ancho / 2);

                    if (distanciaX > 0) {
                        double velocidadNecesaria = Math.min(distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }

                    // No hay Tile, o es el final del mapa
                } else {
                    enemigo.girar();
                }
            }

            if (enemigo.velocidadX < 0) {
                // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                if (tileXEnemigoIzquierda - 1 >= 0 &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda - 1][tileYEnemigoInferior + 1].tipoDeColision
                                == Tile.SOLIDO) {

                    enemigo.x += enemigo.velocidadX;

                    // Solido / borde del tile acercarse.
                } else if (tileXEnemigoIzquierda - 1 >= 0) {

                    int TileEnemigoIzquierdo = tileXEnemigoIzquierda * Tile.ancho;
                    double distanciaX = (enemigo.x - enemigo.ancho / 2) - TileEnemigoIzquierdo;

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girar();
                    }
                } else {
                    enemigo.girar();
                }
            }

            if (enemigo instanceof EnemigoSalto) {
                EnemigoSalto es = (EnemigoSalto) enemigo;
                es.saltar(System.currentTimeMillis());

                // Gravedad enemigo
                if (es.enElAire) {
                    // Recordar los ejes:
                    // - es para arriba       + es para abajo.
                    es.velocidadY += velocidadGravedad;
                    if (es.velocidadY > velocidadMaximaCaida) {
                        es.velocidadY = velocidadMaximaCaida;
                    }
                }

                // Hacia arriba
                if (es.velocidadY < 0) {
                    // Tile superior PASABLE
                    // Podemos seguir moviendo hacia arriba
                    if (tileYEnemigoSuperior - 1 >= 0 &&
                            mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior - 1].tipoDeColision
                                    == Tile.PASABLE
                            && mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior - 1].tipoDeColision
                            == Tile.PASABLE) {

                        es.y += es.velocidadY;

                        // Tile superior != de PASABLE
                        // O es un tile SOLIDO, o es el TECHO del mapa
                    } else {

                        // Si en el propio tile del jugador queda espacio para
                        // subir más, subo
                        int TileEnemigoBordeSuperior = (tileYEnemigoSuperior) * Tile.altura;
                        double distanciaY = (es.y - es.altura / 2) - TileEnemigoBordeSuperior;

                        if (distanciaY > 0) {
                            es.y += Utilidades.proximoACero(-distanciaY, es.velocidadY);

                        } else {
                            // Efecto Rebote -> empieza a bajar;
                            es.velocidadY = velocidadGravedad;
                            es.y += es.velocidadY;
                        }

                    }
                }
                // Hacia abajo
                if (es.velocidadY >= 0) {
                    // Tile inferior PASABLE
                    // Podemos seguir moviendo hacia abajo
                    // NOTA - El ultimo tile es especial (caer al vacío )
                    if (tileYEnemigoInferior + 1 <= altoMapaTiles() - 1 &&
                            mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior + 1].tipoDeColision
                                    == Tile.PASABLE
                            && mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior + 1].tipoDeColision
                            == Tile.PASABLE) {
                        // si los dos están libres cae

                        es.y += es.velocidadY;
                        es.enElAire = true; // Sigue en el aire o se cae
                        // Tile inferior SOLIDO
                        // El ULTIMO, es un caso especial

                    } else if (tileYEnemigoInferior + 1 <= altoMapaTiles() - 1 &&
                            (mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior + 1].tipoDeColision
                                    == Tile.SOLIDO ||
                                    mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior + 1].tipoDeColision ==
                                            Tile.SOLIDO)) {

                        // Con que uno de los dos sea solido ya no puede caer
                        // Si en el propio tile del jugador queda espacio para bajar más, bajo
                        int TileEnemigoBordeInferior =
                                tileYEnemigoInferior * Tile.altura + Tile.altura;

                        double distanciaY =
                                TileEnemigoBordeInferior - (es.y + es.altura / 2);

                        es.enElAire = true; // Sigue en el aire o se cae
                        if (distanciaY > 0) {
                            es.y += Math.min(distanciaY, es.velocidadY);

                        } else {
                            // Toca suelo, nos aseguramos de que está bien
                            es.y = TileEnemigoBordeInferior - es.altura / 2;
                            es.velocidadY = 0;
                            es.enElAire = false;
                        }

                        // Esta cayendo por debajo del ULTIMO
                        // va a desaparecer y perder.
                    } else {

                        es.y += es.velocidadY;
                        es.enElAire = true;
                        es.destruir();
                    }
                }
            }


        }

        // Gravedad Jugador
        if (pelota.enElAire) {
            // Recordar los ejes:
            // - es para arriba       + es para abajo.
            pelota.velocidadY += velocidadGravedad;
            if (pelota.velocidadY > velocidadMaximaCaida) {
                pelota.velocidadY = velocidadMaximaCaida;
            }
        }


// derecha o parado

        if (pelota.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision == Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                pelota.x += pelota.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (pelota.x + pelota.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, pelota.velocidadX);
                    pelota.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    pelota.x = TileJugadorBordeDerecho - pelota.ancho / 2;
                }
            }
        }
// izquierda
        if (pelota.velocidadX <= 0) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                pelota.x += pelota.velocidadX;

                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (pelota.x - pelota.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, pelota.velocidadX);
                    pelota.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    pelota.x = TileJugadorBordeIzquierdo + pelota.ancho / 2;
                }
            }
        }


        // Hacia arriba
        if (pelota.velocidadY < 0) {
            // Tile superior PASABLE
            // Podemos seguir moviendo hacia arriba
            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {

                pelota.y += pelota.velocidadY;

                // Tile superior != de PASABLE
                // O es un tile SOLIDO, o es el TECHO del mapa
            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (pelota.y - pelota.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    pelota.y += Utilidades.proximoACero(-distanciaY, pelota.velocidadY);

                } else {
                    // Efecto Rebote -> empieza a bajar;
                    pelota.velocidadY = velocidadGravedad;
                    pelota.y += pelota.velocidadY;
                }

            }
        }

        // Hacia abajo
        if (pelota.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            // NOTA - El ultimo tile es especial (caer al vacío )
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres cae

                pelota.y += pelota.velocidadY;
                pelota.enElAire = true; // Sigue en el aire o se cae
                // Tile inferior SOLIDO
                // El ULTIMO, es un caso especial

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    (mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.SOLIDO ||
                            mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO)) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;


                double distanciaY =
                        TileJugadorBordeInferior - (pelota.y + pelota.altura / 2);

                pelota.enElAire = true; // Sigue en el aire o se cae
                if (distanciaY > 0) {
                    pelota.y += Math.min(distanciaY, pelota.velocidadY);

                } else {
                    // Toca suelo, nos aseguramos de que está bien
                    pelota.y = TileJugadorBordeInferior - pelota.altura / 2;
                    pelota.velocidadY = 0;
                    pelota.enElAire = false;
                }

                // Esta cayendo por debajo del ULTIMO
                // va a desaparecer y perder.
            } else {

                pelota.y += pelota.velocidadY;
                pelota.enElAire = true;

                if (pelota.y + pelota.altura / 2 > GameView.pantallaAlto) {
                    // ha perdido
                    scrollEjeX = 0;
                    scrollEjeY = 0;
                    pelota.restablecerPosicionInicial();
                    nivelPausado = true;
                    mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                }

            }
        }

        for (Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext(); ) {

            DisparoJugador disparoJugador = iterator.next();

            int tileXDisparo = (int) disparoJugador.x / Tile.ancho;
            int tileYDisparoInferior =
                    (int) (disparoJugador.y + disparoJugador.cAbajo) / Tile.altura;

            int tileYDisparoSuperior =
                    (int) (disparoJugador.y - disparoJugador.cArriba) / Tile.altura;

            for (EnemigoInterface enemigo : enemigos) {
                if (disparoJugador.colisiona((AbstractEnemigo) enemigo)) {
                    enemigo.destruir();
                    iterator.remove();
                    break;
                }
            }


            if (disparoJugador.velocidadX > 0) {
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE) {

                    disparoJugador.x += disparoJugador.velocidadX;

                } else if (tileXDisparo <= anchoMapaTiles() - 1) {

                    int TileDisparoBordeDerecho = tileXDisparo * Tile.ancho + Tile.ancho;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparoJugador.x + disparoJugador.cDerecha);

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }

            // izquierdaa
            if (disparoJugador.velocidadX <= 0) {
                if (tileXDisparo - 1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE) {

                    disparoJugador.x += disparoJugador.velocidadX;

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if (tileXDisparo >= 0) {
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo * Tile.ancho;
                    double distanciaX =
                            (disparoJugador.x - disparoJugador.cIzquierda) - TileDisparoBordeIzquierdo;

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparoJugador.velocidadX);
                        disparoJugador.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }


        }


        for (Iterator<DisparoEnemigo> iterator = disparosEnemigos.iterator(); iterator.hasNext(); ) {

            DisparoEnemigo disparo = iterator.next();

            int tileXDisparo = (int) disparo.x / Tile.ancho;
            int tileYDisparoInferior =
                    (int) (disparo.y + disparo.cAbajo) / Tile.altura;

            int tileYDisparoSuperior =
                    (int) (disparo.y - disparo.cArriba) / Tile.altura;

            if (disparo.colisiona(pelota)) {
                //TODO FINALIZAR JUEGO
            }


            if (disparo.velocidadX > 0) {
                // Tiene delante un tile pasable, puede avanzar.
                if (tileXDisparo + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoInferior].tipoDeColision
                                == Tile.PASABLE &&
                        mapaTiles[tileXDisparo + 1][tileYDisparoSuperior].tipoDeColision
                                == Tile.PASABLE) {

                    disparo.x += disparo.velocidadX;

                } else if (tileXDisparo <= anchoMapaTiles() - 1) {

                    int TileDisparoBordeDerecho = tileXDisparo * Tile.ancho + Tile.ancho;
                    double distanciaX =
                            TileDisparoBordeDerecho - (disparo.x + disparo.cDerecha);

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Math.min(distanciaX, disparo.velocidadX);
                        disparo.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }

            // izquierdaa
            if (disparo.velocidadX <= 0) {
                if (tileXDisparo - 1 >= 0 &&
                        tileYDisparoSuperior < altoMapaTiles() - 1 &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoSuperior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXDisparo - 1][tileYDisparoInferior].tipoDeColision ==
                                Tile.PASABLE) {

                    disparo.x += disparo.velocidadX;

                    // No tengo un tile PASABLE detras
                    // o es el INICIO del nivel o es uno SOLIDO
                } else if (tileXDisparo >= 0) {
                    // Si en el propio tile del jugador queda espacio para
                    // avanzar más, avanzo
                    int TileDisparoBordeIzquierdo = tileXDisparo * Tile.ancho;
                    double distanciaX =
                            (disparo.x - disparo.cIzquierda) - TileDisparoBordeIzquierdo;

                    if (distanciaX > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, disparo.velocidadX);
                        disparo.x += velocidadNecesaria;
                    } else {
                        iterator.remove();
                        continue;
                    }
                }
            }


        }

        if (pelota.colisiona(meta)) {
            gameView.nivelCompleto();
        }

        for (Iterator<Recolectable> iterator = recolectables.iterator(); iterator.hasNext(); ) {
            Recolectable r = iterator.next();
            if (pelota.colisiona(r)) {
                gameView.getMarcador().incrementarPuntos();
                iterator.remove();
            }
        }

        for (Iterator<CheckPoint> iterator = checkpoints.iterator(); iterator.hasNext(); ) {
            CheckPoint c = iterator.next();
            if (pelota.colisiona(c) && !c.isAlcanzado()) {
                c.alcanzar();
                pelota.setxInicial(c.x);
                pelota.setyInicial(c.y - (c.altura / 2));
            }
        }

    }

    public Pelota getPelota() {
        return this.pelota;
    }
}

