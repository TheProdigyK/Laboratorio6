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

public class    Plano {
    /* Las coordenadas cartesianas (x, y, z) */
    private float vertices[] = new float[]{

            // Abajo
            -2, 0, -2, // 0 0
            2, 0, -2, // 1 1
            2, 0, 2, // 5 2
            -2, 0, 2, // 4 3
    };

    /* Los colores x c/vértice (r,g,b,a) */
    byte maxColor = (byte) 255;
    private float texture[] = {
            0.0f, 1.0f, /* Front. */
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };
    /* Indices */
    private short indices[] = new short[]{
            0, 1, 2, 0, 2, 3, // Frente
    };

    private FloatBuffer textureBuffer;
    private int[] textures = new int[1];
    private FloatBuffer bufVertices;
    private ByteBuffer bufColores;
    private ShortBuffer bufIndices;
    private int ind_sz = 0;
    public Plano() {
        /* Lee los vértices */

        int n = 4;
        ind_sz = n * n * 6;
        ByteBuffer bufByte = ByteBuffer.allocateDirect(n * n * 12 * 4);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden de byte nativo
        bufVertices = bufByte.asFloatBuffer(); // Convierte de byte a float
        //bufVertices.put(vertices);
        int zt = 0;

        float d = 4f / n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                float cx = -2 + i * d;
                float cy = -2 + j * d;

                bufVertices.put(zt++, cx);
                bufVertices.put(zt++, 0);
                bufVertices.put(zt++, cy);

                bufVertices.put(zt++, cx + d);
                bufVertices.put(zt++, 0);
                bufVertices.put(zt++, cy);

                bufVertices.put(zt++, cx + d);
                bufVertices.put(zt++, 0);
                bufVertices.put(zt++, cy + d);

                bufVertices.put(zt++, cx);
                bufVertices.put(zt++, 0);
                bufVertices.put(zt++, cy + d);

            }
        }

        bufVertices.rewind(); // puntero al principio del buffer

        /* Lee los indices */
        bufByte = ByteBuffer.allocateDirect(n * n * 6 * 2);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden de byte nativo
        bufIndices = bufByte.asShortBuffer(); // Convierte de byte a short
        short cnt = 0 ;
        zt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                short aa = (short) (0 + cnt * 4), bb = (short)(1 + cnt * 4), cc = (short)(2 + cnt * 4), dd = (short)(3 + cnt * 4);
                cnt++;
                bufIndices.put(zt++, aa);
                bufIndices.put(zt++, bb);
                bufIndices.put(zt++, cc);

                bufIndices.put(zt++, aa);
                bufIndices.put(zt++, cc);
                bufIndices.put(zt++, dd);
            }
        }
        bufIndices.rewind(); // puntero al principio del buffer

        bufByte = ByteBuffer.allocateDirect(n * n * 8 * 4);
        bufByte.order(ByteOrder.nativeOrder());
        textureBuffer = bufByte.asFloatBuffer();
        //textureBuffer.put(texture);
        zt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                textureBuffer.put(zt++, 0);
                textureBuffer.put(zt++, 1);

                textureBuffer.put(zt++, 1);
                textureBuffer.put(zt++, 1);

                textureBuffer.put(zt++, 0);
                textureBuffer.put(zt++, 0);

                textureBuffer.put(zt++, 1);
                textureBuffer.put(zt++, 0);
            }
        }
        textureBuffer.position(0);
    }

    public void loadGLTexture(GL10 gl, Context context) {
        // loading texture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.grass);

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
        /* Se habilita el acceso al arreglo de vértices */
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        /* Se habilita el acceso al arreglo de colores */
        /* Se especifica los datos del arreglo de vértices */
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices);
        /* Se especifica los datos del arreglo de colores */
         /* Se dibuja el cubo */
        gl.glDrawElements(GL10.GL_TRIANGLES, ind_sz,
                GL10.GL_UNSIGNED_SHORT, bufIndices);
        /* Se deshabilita el acceso a los arreglos */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
