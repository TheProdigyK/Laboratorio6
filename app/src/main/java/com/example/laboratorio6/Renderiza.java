package com.example.laboratorio6;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

/**
 * Clase Renderiza (OpenGL 1.x)
 * 
 * @author Jhonny Felipez
 * @version 1.0 01/01/2015
 *
 */
public class Renderiza implements Renderer {
	
	/* Objeto */

	private Cubo cubo;
	private Piso piso;
	private float rotY;
	private Plano plano;
	private Cielo cielo;
	private Arbol arbol;

	private final int szArbol = 9;

	private final int GRADOS = 360/6;		/* Grados */

	private float dxHorse = 0.0f;
	private float dzHorse = 0.0f;
	private float curxHorse = 0f;
	private float curzHorse = 6f;
	private float velHorse = 0.1f;
	private float angleHorse = 217;


	private float dxHorse_2 = 0.0f;
	private float dzHorse_2 = 0.0f;
	private float curxHorse_2 = 0f;
	private float curzHorse_2 = -6f;
	private float velHorse_2 = 0.1f;
	private float angleHorse_2 = 115;

	private float dxBird = 0.0f;
	private float dzBird = 0.0f;
	private float curxBird = 0f;
	private float curzBird = -12f;
	private float velBird = 0.1f;
	private float angleBird = 115;

	private float dxBird_2 = 0.0f;
	private float dzBird_2 = 0.0f;
	private float curxBird_2 = 0f;
	private float curzBird_2 = 12f;
	private float velBird_2 = 0.1f;
	private float angleBird_2 = 115;

	private float arboles_dx[] = new float[szArbol];
	private float arboles_dy[] = new float[szArbol];

	MD2 md2 = new MD2();
	boolean estaBienElModelo;

	MD2 Waterput = new MD2();
	boolean estaBienElModelo_Waterput;

	MD2 bird = new MD2();
	boolean estaBienElModelo_bird;

	private final int szPalm = 7;
	//MD2 Palmera[] = new MD2[szPalm];
	MD2 Palmera = new MD2();
	boolean estaBienElModelo_Palmera;

	float angulo = 30;
	Context contexto;
	String nombreCuadro = "stand";


	// LISTA DE RECTANGULOS

	ArrayList <RectanguloChoque> Rect = new ArrayList<>();
	
	public Renderiza(Context contexto){
		this.contexto = contexto;
	}

	public void setNombreCuadro(String nombreCuadro){
		this.nombreCuadro = nombreCuadro;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {


		cubo = new Cubo();
		piso = new Piso();
		plano = new Plano();
		cielo = new Cielo();
		arbol = new Arbol();
		try {
			estaBienElModelo = md2.leeArchivoMD2(contexto, gl, "horse.md2", "horse.jpg");
		} catch (IOException e) {
			System.err.println("Error parsing :");
			e.printStackTrace();
		}

		try {
			estaBienElModelo_Waterput =
					Waterput.leeArchivoMD2(contexto, gl, "waterput.md2", "waterput.png");
		} catch (IOException e) {
			System.err.println("Error parsing :");
			e.printStackTrace();
		}

		for (int i = 0; i < szArbol; i++) {
			arboles_dx[i] = (float) (Math.random()* 23 - 11.5);
			arboles_dy[i] = (float) (Math.random()* 23 - 11.5);
			Rect.add(new RectanguloChoque(arboles_dx[i] - 0.6f, arboles_dy[i] - 0.6f, 1.2f, 1.2f));
		}

		try {
			estaBienElModelo_bird =
					bird.leeArchivoMD2(contexto, gl, "bird_final.md2", "bird_final.png");
		} catch (IOException e) {
			System.err.println("Error parsing :");
			e.printStackTrace();
		}

		/*try {
			estaBienElModelo_Palmera =
					Palmera.leeArchivoMD2(contexto, gl, "banana.md2", "banana.jpg");
		} catch (IOException e) {
			System.err.println("Error parsing :");
			e.printStackTrace();
		}*/




		Rect.add(new RectanguloChoque(3f, -1, 2f, 2f));

		arbol.loadGLTexture(gl, this.contexto);
		cielo.loadGLTexture(gl, this.contexto);
		plano.loadGLTexture(gl, this.contexto);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		//gl.glEnable(GL10.GL_LIGHTING);
		//gl.glEnable(GL10.GL_LIGHT0);
		gl.glEnable(GL10.GL_NORMALIZE);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glShadeModel(GL10.GL_SMOOTH);
		//gl.glClearColor(0, 0, 0, 0);
		gl.glClearColor(176/255f, 196/255f, 222/256f, 0);
	}

	private boolean choque_caballos = false;

	@Override
	public void onDrawFrame(GL10 gl) {
		if (estaBienElModelo)
			md2.animacion(nombreCuadro);

		if (estaBienElModelo_bird)
			bird.animacion("fly");

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();


		//CIELO
		gl.glPushMatrix();
		gl.glTranslatef(0, -2, -50);
		gl.glScalef(20f, 40f, 0f);
		cielo.dibuja(gl);
		gl.glPopMatrix();

		//ESCENARIO
		//-------------------
		gl.glTranslatef(0,-1.5f,-15);
		gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);

		//ILUMINACION

		/*float luz_ambiente[] = {0.3f, 0.3f, 0.3f, 1.0f};
		float luz_difusa[] = {0.7f, 0.7f, 0.7f, 1.0f};
		float luz_posicion[] = {1f, 1f, 1f, 0.0f};
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, luz_ambiente, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, luz_difusa, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, luz_posicion, 0);*/

		//CUBO PRUEBA
		gl.glPushMatrix();
		gl.glTranslatef(5,0,1);
		gl.glScalef(0.1f, 0.1f, 0.1f);
		//cubo.dibuja(gl);
		gl.glPopMatrix();


		// OBJETOS DENTRO DEL ESCENARIO
		gl.glPushMatrix();
		//gl.glLineWidth(5);
		//gl.glColor4f(1, 0.5f, 0, 0);

		// PLANO
		gl.glPushMatrix();
		gl.glTranslatef(0,-1,0);
		gl.glScalef(6,0,6);
		plano.dibuja(gl);
		gl.glPopMatrix();

		/* Definici�n del color de la luz */
		/*float luz_ambiente[] = {0.3f, 0.3f, 0.3f, 1.0f};
		float luz_difusa[] = {0.7f, 0.7f, 0.7f, 1.0f};
		float luz_posicion[] = {-0.2f, 0.3f, 1, 0.0f};
		//float luz_posicion[] = {-0.2f, 0.3f, 1, 0.0f};
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, luz_ambiente, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, luz_difusa, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, luz_posicion, 0);*/


		 //choque_caballos = distance(curxHorse, curzHorse, curxHorse_2, curzHorse_2) < 2f;

		 /*if (choque_caballos) {
			 dzHorse = -(float) (Math.sin(Math.toRadians(angleHorse)) * velHorse);
			 dxHorse = (float) (Math.cos(Math.toRadians(angleHorse)) * velHorse);
			 dzHorse_2 = -(float) (Math.sin(Math.toRadians(angleHorse_2)) * velHorse_2);
			 dxHorse_2 = (float) (Math.cos(Math.toRadians(angleHorse_2)) * velHorse_2);
			 while(distance(curxHorse + dxHorse , curzHorse + dzHorse, curxHorse_2 + dxHorse_2, curzHorse_2 + dzHorse_2) < 2.f) {
				angleHorse = (float) (Math.random() * 360);
				angleHorse_2 = (float) (Math.random() * 360);
			 }

		 }*/

		//CABALLO
		if (estaBienElModelo) {
			gl.glPushMatrix();
			gl.glTranslatef(curxHorse,0,curzHorse);
			gl.glTranslatef(0, -1.0f, 0);
			gl.glRotatef(angleHorse, 0.0f, 1.0f, 0.0f); // DIRECCION DE VISTA DEL CABALLO
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // PONE AL CABALLO EN POSICION CORRECTA
			gl.glScalef(0.025f, 0.025f, 0.025f);
			md2.dibuja(gl);
			gl.glPopMatrix();
			dzHorse = -(float) (Math.sin(Math.toRadians(angleHorse)) * velHorse);
			dxHorse = (float) (Math.cos(Math.toRadians(angleHorse)) * velHorse);
			if (fueraDeRango(curxHorse + dxHorse, curzHorse + dzHorse) || choque(curxHorse + dxHorse, curzHorse + dzHorse)) {
				angleHorse = (float) (Math.random() * 360);
			}
			else {
				curxHorse += dxHorse;
				curzHorse += dzHorse;
			}
		}

		//CABALLO 2
		if (estaBienElModelo) {
			gl.glPushMatrix();
			gl.glTranslatef(curxHorse_2,0,curzHorse_2);
			gl.glTranslatef(0, -1.0f, 0);
			gl.glRotatef(angleHorse_2, 0.0f, 1.0f, 0.0f); // DIRECCION DE VISTA DEL CABALLO
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // PONE AL CABALLO EN POSICION CORRECTA
			gl.glScalef(0.025f, 0.025f, 0.025f);
			md2.dibuja(gl);
			gl.glPopMatrix();
			dzHorse_2 = -(float) (Math.sin(Math.toRadians(angleHorse_2)) * velHorse_2);
			dxHorse_2 = (float) (Math.cos(Math.toRadians(angleHorse_2)) * velHorse_2);
			if (fueraDeRango(curxHorse_2 + dxHorse_2, curzHorse_2 + dzHorse_2)
					|| choque(curxHorse_2 + dxHorse_2, curzHorse_2 + dzHorse_2)) {
				angleHorse_2 = (float) (Math.random() * 360);
			}
			else {
				curxHorse_2 += dxHorse_2;
				curzHorse_2 += dzHorse_2;
			}
		}

		//BIRD 1
		if (estaBienElModelo_bird) {
			gl.glPushMatrix();
			gl.glTranslatef(curxBird,0,curzBird);
			gl.glTranslatef(0, 8.0f, 0);
			gl.glRotatef(angleBird, 0.0f, 1.0f, 0.0f); // DIRECCION DE VISTA DEL CABALLO
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // PONE AL CABALLO EN POSICION CORRECTA
			gl.glScalef(0.001f, 0.001f, 0.001f);
			bird.dibuja(gl);
			gl.glPopMatrix();
			dzBird = -(float) (Math.sin(Math.toRadians(angleBird)) * velBird);
			dxBird = (float) (Math.cos(Math.toRadians(angleBird)) * velBird);
			if (fueraDeRango(curxBird + dxBird, curzBird + dzBird)) {
				angleBird = (float) (Math.random() * 360);
			}
			else {
				curxBird += dxBird;
				curzBird += dzBird;
			}
		}

		//BIRD 2
		if (estaBienElModelo_bird) {
			gl.glPushMatrix();
			gl.glTranslatef(curxBird_2,0,curzBird_2);
			gl.glTranslatef(0, 8.0f, 0);
			gl.glRotatef(angleBird_2, 0.0f, 1.0f, 0.0f); // DIRECCION DE VISTA DEL CABALLO
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // PONE AL CABALLO EN POSICION CORRECTA
			gl.glScalef(0.001f, 0.001f, 0.001f);
			bird.dibuja(gl);
			gl.glPopMatrix();
			dzBird_2 = -(float) (Math.sin(Math.toRadians(angleBird_2)) * velBird_2);
			dxBird_2 = (float) (Math.cos(Math.toRadians(angleBird_2)) * velBird_2);
			if (fueraDeRango(curxBird_2 + dxBird_2, curzBird_2 + dzBird_2)) {
				angleBird_2 = (float) (Math.random() * 360);
			}
			else {
				curxBird_2 += dxBird_2;
				curzBird_2 += dzBird_2;
			}
		}


		//POZO
		if (estaBienElModelo_Waterput) {
			gl.glPushMatrix();
			gl.glTranslatef(4, -1.0f, 0);
			gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(0.025f, 0.025f, 0.025f);
			Waterput.dibuja(gl);
			gl.glPopMatrix();
		}

		for (int arb = 0; arb < szArbol; arb++) {
			gl.glPushMatrix();
			gl.glTranslatef(arboles_dx[arb], 2.5f, arboles_dy[arb]);
			gl.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
			//gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(1.85f, 1.85f, 1.85f);
			arbol.dibuja(gl);
			gl.glPopMatrix();
		}
		//angulo = angulo + 0.7f;
		/*if (angulo > 360) {
			angulo = angulo - 360;
		}*/
		gl.glPopMatrix();
		// Avanza la animacion
		if (estaBienElModelo){
			md2.avanza(0.015f);
			bird.avanza(0.015f);
			//md2.avanza(0.025f);

		}

		gl.glFlush();
		rotY = rotY + 0.2f;
	}

	private boolean choque(float xp, float yp) {
		for (RectanguloChoque xz : Rect) {
			if (xz.enRectangulo(xp, yp))
				return true;
		}
		return false;
	}

	private boolean fueraDeRango(float x, float z) {
		return x >= 12.0f || x <= -12.0f || z >= 12.0f || z <= -12.0f;
	}

	private double distance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//GLU.gluPerspective(gl, 45, (float)w / (float)h, 1, 200);
		GLU.gluPerspective(gl, 60, w / (float)h, 1, 100);
		GLU.gluLookAt(gl, 0,1, 10, 0, 0, 0, 0, 1, 0);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
