package com.example.laboratorio6;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.media.MediaPlayer;

/**
 * Transformaciones y animación 3D en OpenGL ES 1.x.
 *
 * @author Jhonny Felipez
 * @version 1.0 02/04/2014
 *
 */

public class MainActivity extends Activity {
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Ventana sin título */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* Se establece las banderas de la ventana de esta Actividad */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /* Se crea el objeto GLSurfaceView */
        GLSurfaceView superficie = new GLSurfaceView(this);

        player = MediaPlayer.create(this,R.raw.ost);
        player.start();

        /* Se crea el objeto Renderiza */
        Renderiza renderiza = new Renderiza(this);

        /* GLSurfaceView <- Renderiza : Inicia el renderizado */
        superficie.setRenderer(renderiza);

        /*
         * Activity <- GLSurfaceView  : Coloca la Vista de la Superficie del
         * OpenGL como un Contexto de ésta Actividad.
         */
        setContentView(superficie);
        // setContentView(R.layout.activity_main);
    }
}