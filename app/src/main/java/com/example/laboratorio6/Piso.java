package com.example.laboratorio6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * Clase Cubo (OpenGL 1.x)
 * 
 * @author Jhonny Felipez
 * @version 3.0 02/04/2014
 * 
 */
public class Piso {

	/**
	 *    3 ---------- 2
	 *     |          | 
	 *     |          | 
	 *     |          |
	 *     |          |
	 *    0 ---------- 1  
	 */	
	
	private float vertices[] = new float[42 * 6];
	private byte colores[] = new byte[42 * 8];
	private FloatBuffer bufVertices;
	private ByteBuffer bufColores;
	public Piso() {

		int i = 0;
		int j = 0;
		for (float x = -10; x <= 10; x++){
			vertices[i  ] = x; vertices[i+1] = -1; vertices[i+2] =  10;
			vertices[i+3] = x; vertices[i+4] = -1; vertices[i+5] = -10;
			i = i + 6;
			colores[j  ] = (byte)255; colores[j+1] = (byte)255; colores[j+2] = (byte)255; colores[j+3] = 1;
			colores[j+4] = (byte)255; colores[j+5] = (byte)255; colores[j+6] = (byte)255; colores[j+7] = 1;
			j = j + 8;
		}
		for (float z = 10; z >= -10; z--){
			vertices[i  ] = -10; vertices[i+1] = -1; vertices[i+2] = z;
			vertices[i+3] =  10; vertices[i+4] = -1; vertices[i+5] = z;
			i = i + 6;
			colores[j  ] = (byte)255; colores[j+1] = (byte)255; colores[j+2] = (byte)255; colores[j+3] = 1;
			colores[j+4] = (byte)255; colores[j+5] = (byte)255; colores[j+6] = (byte)255; colores[j+7] = 1;
			j = j + 8;
		}
		
		/* Lee los v�rtices */
		ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
		bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden de byte nativo
		bufVertices = bufByte.asFloatBuffer(); // Convierte de byte a float
		bufVertices.put(vertices);
		bufVertices.rewind(); // puntero al principio del buffer
		
		/* Lee los colores */
		bufColores = ByteBuffer.allocateDirect(colores.length);
		bufColores.put(colores);
		bufColores.position(0); // puntero al principio del buffer
	}
	
	public void dibuja(GL10 gl) {

		/* Se activa el arreglo de v�rtices */
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		/* Se activa el arreglo de colores */
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		/* Se especifica los datos del arreglo de v�rtices */
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices);
		
		/* Se especifica los datos del arreglo de colores */
		gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores);
		
		/* Renderiza las primitivas desde los datos de los arreglos (vertices e indices) */
		gl.glDrawArrays(GL10.GL_LINES, 0, 84);

		/* Se desactiva el arreglo de v�rtices */
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		/* Se desactiva el arreglo de colores */
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

	}
}
