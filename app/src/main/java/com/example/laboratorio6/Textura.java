package com.example.laboratorio6;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Clase Textura (OpenGL 1.x)
 * 
 * @author Jhonny Felipez
 * @version 1.0 13/04/2016
 *
 */
public class Textura {
	
	private Context contexto;
	private String nombreArchivo;
	private float ancho;
	private float alto;
	private int codigo;
	
	public Textura(GL10 gl, Context contexto, String nombreArchivo){
		this.contexto = contexto;
		this.nombreArchivo = nombreArchivo;
		leeTextura(gl);
	}
	
	/**
	 * Lee la textura
	 * 
	 * @param gl - El contexto GL
	 * @param contexto - El contexto de la Actividad
	 */
	public void leeTextura(GL10 gl) {
		
		/* C�digos de la textura */
		int codigos[] = new int[1];

		try {
			/* Obtiene la textura del directorio de assets Android */
			InputStream is = contexto.getAssets().open(nombreArchivo);

			/* Decodifica un flujo de entrada en un mapa de bits. */
			Bitmap textura = BitmapFactory.decodeStream(is);

			/* Genera un nombre (c�digo) para la textura */
			gl.glGenTextures(1, codigos, 0);
			
			codigo = codigos[0];

			/* Se asigna un nombre (c�digo) a la textura */
			gl.glBindTexture(GL10.GL_TEXTURE_2D, codigos[0]);

			/* Para que el patr�n de textura se agrande y se acomode a una �rea
			 * grande */
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
			//		GL10.GL_NEAREST);

			/* Para que el patr�n de textura se reduzca y se acomode a una �rea
			 * peque�a */
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
			//		GL10.GL_NEAREST);

			/* Para no repetir la textura tanto en s y t dentro el rango */
//			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
//					GL10.GL_CLAMP_TO_EDGE);
//			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
//					GL10.GL_CLAMP_TO_EDGE);
			
			/* Para repetir la textura tanto en s y t dentro el rango */
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_REPEAT,
			//		GL10.GL_CLAMP_TO_EDGE);
			//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_REPEAT,
			//		GL10.GL_CLAMP_TO_EDGE);

			/* Determina la formato y el tipo de la textura. Carga la textura en
			 * el buffer de textura */
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textura, 0);

			/* Asignaci�n de textura a cero */
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			
			ancho = textura.getWidth();
			alto = textura.getHeight();

			/* Recicla la textura, porque los datos ya fueron cargados al OpenGL */
			textura.recycle();

			/* Cierra el archivo */
			is.close();

			is = null;

		} catch (IOException e) {
			Log.d("Rectangulo", "No puede cargar " + nombreArchivo);
			throw new RuntimeException("No puede cargar " + nombreArchivo);
		}
	}
	
	public float getAncho() {
		return ancho;
	}
	
	public float getAlto() {
		return alto;
	}

	public int getCodigoTextura() {
		return codigo;
	}
	
	public void inicializa(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		int[] codigos = { codigo };
		gl.glDeleteTextures(1, codigos, 0);
	}
}
