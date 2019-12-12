package com.example.laboratorio6;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class MD2 {
	
	class EncabezadoDeArchivo {
		public int ident;				// es igual a "IDP2"
		public int version;				// versi�n md2. es igual a 8
		public int anchoTextura;		// ancho de la textura
		public int altoTextura;			// altura de la textura
		public int numBytesPorCuadro;	// el numero de bytes por cuadro
		public int numTexturas;			// n�mero de texturas
		public int numVertices;			// n�mero de v�rtices
		public int numCoordTextura;		// n�mero de coordenadas de textura
		public int numTriangulos;		// n�mero de tri�ngulos
		public int numComandosDeGL;		// n�mero de comandos del opengl
		public int numCuadros;			// n�mero total de cuadros
		public int desplTextura;		// desplazamiento de las texturas (64 bytes cada uno)
		public int desplCoordTextura;	// desplazamiento de las coordenadas de textura
		public int desplTriangulos;		// desplazamiento de los tri�ngulos
		public int desplCuadros;		// desplazamiento de los datos de los cuadros
		public int desplComandosGL;		// desplazamiento de los comandos opengl
		public int desplFinArchivo;		// desplazamiento del final del archivo

		@Override
		public String toString() {
			String s = ident + " " + version + " " + anchoTextura + " " + altoTextura + " " + numBytesPorCuadro + " " +
					numTexturas + " " + numVertices + " " +	numCoordTextura + " " + numTriangulos + " " + numComandosDeGL + " " +
					numCuadros + " " + desplTextura + " " +	desplCoordTextura + " " + desplTriangulos + " " + desplCuadros + " " +
					desplComandosGL + " " + desplFinArchivo;
			return s;
		}

	}
	
	public class MD2Vertice {
		public float[] posicion = new float[3];
		public float[] normal = new float[3];
	}
	
	public class MD2Cuadro {
		public String nombre;
		MD2Vertice vertices[];
	}
	
	public class MD2Triangulo {
		public short[] indiceVertice = new short[3];
		public short[] indiceCoordTextura = new short[3];
	}

	public class MD2CoordTextura {
		float u;
		float v;
	}
	
	// tabla de las normales extraido del demo md2bump de Mark Kilgard
	private static final float[] tablaNormal = {
		 -0.525731f,  0.000000f,  0.850651f,
		 -0.442863f,  0.238856f,  0.864188f,
		 -0.295242f,  0.000000f,  0.955423f,
		 -0.309017f,  0.500000f,  0.809017f,
		 -0.162460f,  0.262866f,  0.951056f,
		  0.000000f,  0.000000f,  1.000000f,
		  0.000000f,  0.850651f,  0.525731f,
		 -0.147621f,  0.716567f,  0.681718f,
		  0.147621f,  0.716567f,  0.681718f,
		  0.000000f,  0.525731f,  0.850651f,
		  0.309017f,  0.500000f,  0.809017f,
		  0.525731f,  0.000000f,  0.850651f,
		  0.295242f,  0.000000f,  0.955423f,
		  0.442863f,  0.238856f,  0.864188f,
		  0.162460f,  0.262866f,  0.951056f,
		 -0.681718f,  0.147621f,  0.716567f,
		 -0.809017f,  0.309017f,  0.500000f,
		 -0.587785f,  0.425325f,  0.688191f,
		 -0.850651f,  0.525731f,  0.000000f,
		 -0.864188f,  0.442863f,  0.238856f,
		 -0.716567f,  0.681718f,  0.147621f,
		 -0.688191f,  0.587785f,  0.425325f,
		 -0.500000f,  0.809017f,  0.309017f,
		 -0.238856f,  0.864188f,  0.442863f,
		 -0.425325f,  0.688191f,  0.587785f,
		 -0.716567f,  0.681718f, -0.147621f,
		 -0.500000f,  0.809017f, -0.309017f,
		 -0.525731f,  0.850651f,  0.000000f,
		  0.000000f,  0.850651f, -0.525731f,
		 -0.238856f,  0.864188f, -0.442863f,
		  0.000000f,  0.955423f, -0.295242f,
		 -0.262866f,  0.951056f, -0.162460f,
		  0.000000f,  1.000000f,  0.000000f,
		  0.000000f,  0.955423f,  0.295242f,
		 -0.262866f,  0.951056f,  0.162460f,
		  0.238856f,  0.864188f,  0.442863f,
		  0.262866f,  0.951056f,  0.162460f,
		  0.500000f,  0.809017f,  0.309017f,
		  0.238856f,  0.864188f, -0.442863f,
		  0.262866f,  0.951056f, -0.162460f,
		  0.500000f,  0.809017f, -0.309017f,
		  0.850651f,  0.525731f,  0.000000f,
		  0.716567f,  0.681718f,  0.147621f,
		  0.716567f,  0.681718f, -0.147621f,
		  0.525731f,  0.850651f,  0.000000f,
		  0.425325f,  0.688191f,  0.587785f,
		  0.864188f,  0.442863f,  0.238856f,
		  0.688191f,  0.587785f,  0.425325f,
		  0.809017f,  0.309017f,  0.500000f,
		  0.681718f,  0.147621f,  0.716567f,
		  0.587785f,  0.425325f,  0.688191f,
		  0.955423f,  0.295242f,  0.000000f,
		  1.000000f,  0.000000f,  0.000000f,
		  0.951056f,  0.162460f,  0.262866f,
		  0.850651f, -0.525731f,  0.000000f,
		  0.955423f, -0.295242f,  0.000000f,
		  0.864188f, -0.442863f,  0.238856f,
		  0.951056f, -0.162460f,  0.262866f,
		  0.809017f, -0.309017f,  0.500000f,
		  0.681718f, -0.147621f,  0.716567f,
		  0.850651f,  0.000000f,  0.525731f,
		  0.864188f,  0.442863f, -0.238856f,
		  0.809017f,  0.309017f, -0.500000f,
		  0.951056f,  0.162460f, -0.262866f,
		  0.525731f,  0.000000f, -0.850651f,
		  0.681718f,  0.147621f, -0.716567f,
		  0.681718f, -0.147621f, -0.716567f,
		  0.850651f,  0.000000f, -0.525731f,
		  0.809017f, -0.309017f, -0.500000f,
		  0.864188f, -0.442863f, -0.238856f,
		  0.951056f, -0.162460f, -0.262866f,
		  0.147621f,  0.716567f, -0.681718f,
		  0.309017f,  0.500000f, -0.809017f,
		  0.425325f,  0.688191f, -0.587785f,
		  0.442863f,  0.238856f, -0.864188f,
		  0.587785f,  0.425325f, -0.688191f,
		  0.688191f,  0.587785f, -0.425325f,
		 -0.147621f,  0.716567f, -0.681718f,
		 -0.309017f,  0.500000f, -0.809017f,
		  0.000000f,  0.525731f, -0.850651f,
		 -0.525731f,  0.000000f, -0.850651f,
		 -0.442863f,  0.238856f, -0.864188f,
		 -0.295242f,  0.000000f, -0.955423f,
		 -0.162460f,  0.262866f, -0.951056f,
		  0.000000f,  0.000000f, -1.000000f,
		  0.295242f,  0.000000f, -0.955423f,
		  0.162460f,  0.262866f, -0.951056f,
		 -0.442863f, -0.238856f, -0.864188f,
		 -0.309017f, -0.500000f, -0.809017f,
		 -0.162460f, -0.262866f, -0.951056f,
		  0.000000f, -0.850651f, -0.525731f,
		 -0.147621f, -0.716567f, -0.681718f,
		  0.147621f, -0.716567f, -0.681718f,
		  0.000000f, -0.525731f, -0.850651f,
		  0.309017f, -0.500000f, -0.809017f,
		  0.442863f, -0.238856f, -0.864188f,
		  0.162460f, -0.262866f, -0.951056f,
		  0.238856f, -0.864188f, -0.442863f,
		  0.500000f, -0.809017f, -0.309017f,
		  0.425325f, -0.688191f, -0.587785f,
		  0.716567f, -0.681718f, -0.147621f,
		  0.688191f, -0.587785f, -0.425325f,
		  0.587785f, -0.425325f, -0.688191f,
		  0.000000f, -0.955423f, -0.295242f,
		  0.000000f, -1.000000f,  0.000000f,
		  0.262866f, -0.951056f, -0.162460f,
		  0.000000f, -0.850651f,  0.525731f,
		  0.000000f, -0.955423f,  0.295242f,
		  0.238856f, -0.864188f,  0.442863f,
		  0.262866f, -0.951056f,  0.162460f,
		  0.500000f, -0.809017f,  0.309017f,
		  0.716567f, -0.681718f,  0.147621f,
		  0.525731f, -0.850651f,  0.000000f,
		 -0.238856f, -0.864188f, -0.442863f,
		 -0.500000f, -0.809017f, -0.309017f,
		 -0.262866f, -0.951056f, -0.162460f,
		 -0.850651f, -0.525731f,  0.000000f,
		 -0.716567f, -0.681718f, -0.147621f,
		 -0.716567f, -0.681718f,  0.147621f,
		 -0.525731f, -0.850651f,  0.000000f,
		 -0.500000f, -0.809017f,  0.309017f,
		 -0.238856f, -0.864188f,  0.442863f,
		 -0.262866f, -0.951056f,  0.162460f,
		 -0.864188f, -0.442863f,  0.238856f,
		 -0.809017f, -0.309017f,  0.500000f,
		 -0.688191f, -0.587785f,  0.425325f,
		 -0.681718f, -0.147621f,  0.716567f,
		 -0.442863f, -0.238856f,  0.864188f,
		 -0.587785f, -0.425325f,  0.688191f,
		 -0.309017f, -0.500000f,  0.809017f,
		 -0.147621f, -0.716567f,  0.681718f,
		 -0.425325f, -0.688191f,  0.587785f,
		 -0.162460f, -0.262866f,  0.951056f,
		  0.442863f, -0.238856f,  0.864188f,
		  0.162460f, -0.262866f,  0.951056f,
		  0.309017f, -0.500000f,  0.809017f,
		  0.147621f, -0.716567f,  0.681718f,
		  0.000000f, -0.525731f,  0.850651f,
		  0.425325f, -0.688191f,  0.587785f,
		  0.587785f, -0.425325f,  0.688191f,
		  0.688191f, -0.587785f,  0.425325f,
		 -0.955423f,  0.295242f,  0.000000f,
		 -0.951056f,  0.162460f,  0.262866f,
		 -1.000000f,  0.000000f,  0.000000f,
		 -0.850651f,  0.000000f,  0.525731f,
		 -0.955423f, -0.295242f,  0.000000f,
		 -0.951056f, -0.162460f,  0.262866f,
		 -0.864188f,  0.442863f, -0.238856f,
		 -0.951056f,  0.162460f, -0.262866f,
		 -0.809017f,  0.309017f, -0.500000f,
		 -0.864188f, -0.442863f, -0.238856f,
		 -0.951056f, -0.162460f, -0.262866f,
		 -0.809017f, -0.309017f, -0.500000f,
		 -0.681718f,  0.147621f, -0.716567f,
		 -0.681718f, -0.147621f, -0.716567f,
		 -0.850651f,  0.000000f, -0.525731f,
		 -0.688191f,  0.587785f, -0.425325f,
		 -0.587785f,  0.425325f, -0.688191f,
		 -0.425325f,  0.688191f, -0.587785f,
		 -0.425325f, -0.688191f, -0.587785f,
		 -0.587785f, -0.425325f, -0.688191f,
		 -0.688191f, -0.587785f, -0.425325f
	};
	
	EncabezadoDeArchivo encabezado = new EncabezadoDeArchivo();
	MD2Cuadro[] cuadros;
	MD2Triangulo triangulos[];
	MD2CoordTextura coordtexturas[];
	int cuadroInicial, cuadroFinal;
	
	/* C�digo o handle de la textura */
	int codigo[] = new int[1];
	
	float tiempo;
	
	FloatBuffer bufVertices;
	FloatBuffer bufNormales;
	FloatBuffer bufTextura;
	
	private byte[] leeBytes (InputStream in) throws IOException {
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int leeBytes = 0;
		while ((leeBytes = in.read(buffer)) > 0) {
			salida.write(buffer, 0, leeBytes);
		}

		salida.close();
		return salida.toByteArray();
	}
	
	private void leeEncabezado (byte[] bytes) throws IOException {
		FormatoLittleEndian in = new FormatoLittleEndian(new ByteArrayInputStream(bytes));
		encabezado.ident      			= in.readInt();
	    encabezado.version    			= in.readInt();
	    encabezado.anchoTextura  		= in.readInt();
	    encabezado.altoTextura 			= in.readInt();
	    encabezado.numBytesPorCuadro  	= in.readInt();
	    encabezado.numTexturas  		= in.readInt();
	    encabezado.numVertices 			= in.readInt();
	    encabezado.numCoordTextura     	= in.readInt();
	    encabezado.numTriangulos   		= in.readInt();
	    encabezado.numComandosDeGL 		= in.readInt();
	    encabezado.numCuadros 			= in.readInt();
	    encabezado.desplTextura  		= in.readInt();
	    encabezado.desplCoordTextura    = in.readInt();
	    encabezado.desplTriangulos   	= in.readInt();
	    encabezado.desplCuadros 		= in.readInt();
	    encabezado.desplComandosGL 		= in.readInt();
	    encabezado.desplFinArchivo    	= in.readInt();
		in.close();
	}
	
	private void leeCoordTextura (byte[] bytes) throws IOException {
		FormatoLittleEndian in = new FormatoLittleEndian(new ByteArrayInputStream(bytes));
		in.skip(encabezado.desplCoordTextura);
		coordtexturas = new MD2CoordTextura[encabezado.numCoordTextura];
		float ancho = encabezado.anchoTextura;
		float alto = encabezado.altoTextura;
		for (int i = 0; i < encabezado.numCoordTextura; i++) {
			MD2CoordTextura coordtextura = new MD2CoordTextura();
			short u = in.readShort();
			short v = in.readShort();
			coordtextura.u = u / ancho;
			coordtextura.v = 1 - v / alto;
			coordtexturas[i] = coordtextura;
		}
		in.close();
	}
	
	private void leeTriangulos ( byte[] bytes) throws IOException {
		FormatoLittleEndian in = new FormatoLittleEndian(new ByteArrayInputStream(bytes));
		in.skip(encabezado.desplTriangulos);
		triangulos = new MD2Triangulo[encabezado.numTriangulos];
		for (int i = 0; i < encabezado.numTriangulos; i++) {
			MD2Triangulo triangulo = new MD2Triangulo();
			triangulo.indiceVertice[0] = in.readShort();
			triangulo.indiceVertice[1] = in.readShort();
			triangulo.indiceVertice[2] = in.readShort();
			triangulo.indiceCoordTextura[0] = in.readShort();
			triangulo.indiceCoordTextura[1] = in.readShort();
			triangulo.indiceCoordTextura[2] = in.readShort();
			triangulos[i] = triangulo;
		}
		in.close();
	}
	
	private void leeCuadros (byte[] bytes) throws IOException {
		FormatoLittleEndian in = new FormatoLittleEndian(new ByteArrayInputStream(bytes));
		in.skip(encabezado.desplCuadros);
		cuadros = new MD2Cuadro[encabezado.numCuadros];
		byte[] nombre = new byte[16];
		for (int i = 0; i < encabezado.numCuadros; i++) {
			MD2Cuadro cuadro = new MD2Cuadro();
			
			cuadro.vertices = new MD2Vertice[encabezado.numVertices];

			float escalaX = in.readFloat();
			float escalaY = in.readFloat();
			float escalaZ = in.readFloat();
			
			float trasladaX = in.readFloat();
			float trasladaY = in.readFloat();
			float trasladaZ = in.readFloat();
		
			in.read(nombre);
			int n = 0;
			for (int j = 0; j < nombre.length; j++)
				if (nombre[j] == 0) {
					n = j - 1;
					break;
				}
			cuadro.nombre = new String(nombre, 0, n);
			
			for (int j = 0; j < encabezado.numVertices; j++) {
				MD2Vertice vertice = new MD2Vertice();
				
				int x = in.read();
				int y = in.read();
				int z = in.read();
				
				float x_ = x * escalaX + trasladaX;
				float y_ = y * escalaY + trasladaY;
				float z_ = z * escalaZ + trasladaZ;

				vertice.posicion[0] = x_;
				vertice.posicion[1] = y_;
				vertice.posicion[2] = z_;
				
				int iNormal = in.read(); // indice de la tabla normal
				
				vertice.normal[0] = tablaNormal[3 * iNormal];
				vertice.normal[1] = tablaNormal[3 * iNormal + 1];
				vertice.normal[2] = tablaNormal[3 * iNormal + 2];

				cuadro.vertices[j] = vertice;
			}
			
			cuadros[i] = cuadro;
		}

		in.close();
	}

	Textura textura;
	public boolean leeArchivoMD2(Context contexto, GL10 gl, String nombreArchivoMD2, String nombreArchivoTextura) throws IOException {
		
		/* Lee el archivo .md2 del directorio de assets Android */
		
		InputStream input = contexto.getAssets().open(nombreArchivoMD2);
		byte[] bytes = leeBytes(input);
		
		// Se lee el encabezado
		leeEncabezado(bytes);
		
		// Se lee la textura
		textura = new Textura(gl, contexto, nombreArchivoTextura);
	    
	    // Se leen las coordenadas de textura
	    leeCoordTextura (bytes);
		
	    // Se leen los triangulos
	    leeTriangulos(bytes);
	    
		// Se leen los Cuadros
	    leeCuadros (bytes);
	    
		ByteBuffer bufByte = ByteBuffer.allocateDirect(encabezado.numTriangulos * 3 * 3 * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufVertices = bufByte.asFloatBuffer();
     	
		bufByte = ByteBuffer.allocateDirect(encabezado.numTriangulos * 3 * 3 * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufNormales = bufByte.asFloatBuffer();
		
		bufByte = ByteBuffer.allocateDirect(encabezado.numTriangulos * 3 * 2 * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufTextura = bufByte.asFloatBuffer();
	    
		return true;
	}

	public void animacion(String nombre) {
		/*
		 * Los nombres de los cuadros normalmente comienzan con el nombre de la animaci�n
		 * en la que se encuentran, por ejemplo "run", y estan seguidos por un car�cter no
		 * alfab�tico "_" y el n�mero del cuadro. Normalmente, indican el n�mero de
		 * cuadro en la animaci�n, por ejemplo "run_1", "run_2", etc.
		 */
		int n = 0;
		boolean encontro = false;
		n = nombre.length();
		for (int i = 0; i < encabezado.numCuadros; i++) {
			MD2Cuadro cuadro = cuadros[i];
			if (cuadro.nombre.length() > n && cuadro.nombre.substring(0, n).compareTo(nombre) == 0
					&& !Character.isLetter(cuadro.nombre.charAt(n))) {
				if (!encontro) {
					encontro = true;
					cuadroInicial = i;
				} else {
					cuadroFinal = i;
				}
			} else if (encontro) {
				break;
			}
		}
	}
	
	public void dibuja(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textura.getCodigoTextura());
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		// Calcula los dos cuadros entre los que se est� interpolando
		int iCuadro1 = (int)(tiempo * (cuadroFinal - cuadroInicial + 1)) + cuadroInicial;
		if (iCuadro1 > cuadroFinal) {
			iCuadro1 = cuadroInicial;
		}
		int iCuadro2;
		if (iCuadro1 < cuadroFinal) {
			iCuadro2 = iCuadro1 + 1;
		} else {
			iCuadro2 = cuadroInicial;
		}
		MD2Cuadro cuadro1 = cuadros[iCuadro1];
		MD2Cuadro cuadro2 = cuadros[iCuadro2];

		// Calcula la fracci�n entre los dos cuadros
		float frac =
			(tiempo - (float)(iCuadro1 - cuadroInicial) /
			 (float)(cuadroFinal - cuadroInicial + 1)) * (cuadroFinal - cuadroInicial + 1);
		

		// Dibuja el modelo como una interpolaci�n entre dos cuadros
		
		bufNormales.clear();
		bufTextura.clear();
		bufVertices.clear();
		
		float posicion[] = new float[3];
		float normal[] = new float[3];
		for (int i = 0; i < encabezado.numTriangulos; i++) {
			MD2Triangulo triangulo = triangulos[i];
			for (int j = 0; j < 3; j++) {
				int iVertice = triangulo.indiceVertice[j];
				
				MD2Vertice v1 = cuadro1.vertices[iVertice];
				MD2Vertice v2 = cuadro2.vertices[iVertice];
				
				posicion[0] = v1.posicion[0] * (1 - frac) + v2.posicion[0] * frac;
				posicion[1] = v1.posicion[1] * (1 - frac) + v2.posicion[1] * frac;
				posicion[2] = v1.posicion[2] * (1 - frac) + v2.posicion[2] * frac;

				normal[0] = v1.normal[0] * (1 - frac) + v2.normal[0] * frac;
				normal[1] = v1.normal[1] * (1 - frac) + v2.normal[1] * frac;
				normal[2] = v1.normal[2] * (1 - frac) + v2.normal[2] * frac;
				
				if (normal[0] == 0 && normal[1] == 0 && normal[2] == 0) {
					normal[0] = 0;
					normal[1] = 0;
					normal[2] = 1;
				}
				
				int iCoordTextura = triangulo.indiceCoordTextura[j];
				
				MD2CoordTextura coordtextura = coordtexturas[iCoordTextura];

				bufVertices.put(posicion[0]);
				bufVertices.put(posicion[1]);
				bufVertices.put(posicion[2]);
				
				bufNormales.put(normal[0]);
				bufNormales.put(normal[1]);
				bufNormales.put(normal[2]);
				
				bufTextura.put(coordtextura.u);
				bufTextura.put(coordtextura.v);
			}
		}
		
		bufNormales.rewind();
		bufTextura.rewind();
		bufVertices.rewind();

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNormales);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, bufTextura);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, encabezado.numTriangulos * 3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	}
	
	public void avanza(float dt) {
		if (dt < 0) {
			return;
		}
		
		tiempo = tiempo + dt;
		if (tiempo < 1000000000) {
			tiempo = tiempo - (int)tiempo;
		}
		else {
			tiempo = 0;
		}
	}
}
