package com.example.laboratorio6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Cielo {
    /**
     * 3 ---------
     * 2
     * /| /|
     * / | / |
     * 7 --------- 6 |
     * | | | |
     * | 0 ------|-- 1
     * | / | /
     * |/ |/
     * 4 --------- 5
     */
    /* Las coordenadas cartesianas (x, y, z) */
    private float vertices[] = new float[]{

            // Frente
            -1, -1, 1, // 4 0
            1, -1, 1, // 5 1
            1, 1, 1, // 6 2
            -1, 1, 1, // 7 3
// Atr
            -1, 1, -1, // 3 4
            1, 1, -1, // 2 5
            1, -1, -1, // 1 6
            -1, -1, -1, // 0 7
// Izquierda
            -1, -1, -1, // 0 8
            -1, -1, 1, // 4 9
            -1, 1, 1, // 7 10
            -1, 1, -1, // 3 11
// Derecha
            1, -1, 1, // 5 12
            1, -1, -1, // 1 13
            1, 1, -1, // 2 14
            1, 1, 1, // 6 15
// Abajo
            -1, -1, -1, // 0 16
            1, -1, -1, // 1 17
            1, -1, 1, // 5 18
            -1, -1, 1, // 4 19
// Arriba
            -1, 1, 1, // 7 20
            1, 1, 1, // 6 21
            1, 1, -1, // 2 22
            -1, 1, -1 // 3 23

    };
    /* Los colores x c/v�rtice (r,g,b,a) */
    byte maxColor = (byte) 255;
    /* Indices */
    private short indices[] = new short[]{
            0, 1, 2, 0, 2, 3, // Frente
            4, 5, 6, 4, 6, 7, // Atr�s
            8, 9, 10, 8, 10, 11, // Izquierda
            12, 13, 14, 12, 14, 15, // Derecha
            16, 17, 18, 16, 18, 19, // Abajo
            20, 21, 22, 20, 22, 23 // Arriba
    };


    private float texture[] = {
            0.5f, 0.5f, /* Back. */
            0.0f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f, /* Front. */
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f, /* Left. */
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f, /* Right. */
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 1.0f, /* Top. */
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f, /* Bottom. */
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    private FloatBuffer textureBuffer;
    private int[] textures = new int[1];
    private FloatBuffer bufVertices;
    private ByteBuffer bufColores;
    private ShortBuffer bufIndices;

    public Cielo() {
        /* Lee los v�rtices */
        ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden de byte nativo
        bufVertices = bufByte.asFloatBuffer(); // Convierte de byte a float
        bufVertices.put(vertices);
        bufVertices.rewind(); // puntero al principio del buffer

        bufByte = ByteBuffer.allocateDirect(indices.length * 2);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden de byte nativo
        bufIndices = bufByte.asShortBuffer(); // Convierte de byte a short
        bufIndices.put(indices);
        bufIndices.rewind(); // puntero al principio del buffer

        bufByte = ByteBuffer.allocateDirect(texture.length * 4);
        bufByte.order(ByteOrder.nativeOrder());
        textureBuffer = bufByte.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    public void loadGLTexture(GL10 gl, Context context) {
        // loading texture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.sky);

        // generate one texture pointer
        gl.glGenTextures(1, textures, 0);
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // create nearest filtered texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Clean up
        bitmap.recycle();
    }

    public void dibuja(GL10 gl) {
    	gl.glColor4f(1, 1, 1, 0);
        /* Se habilita el acceso al arreglo de v�rtices */
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        /* Se habilita el acceso al arreglo de colores */
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        /* Se especifica los datos del arreglo de v�rtices */
        //gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices);
        /* Se especifica los datos del arreglo de colores */
        //gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores);
        /* Se dibuja el cubo */
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, bufIndices);
//        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
        /* Se deshabilita el acceso a los arreglos */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}