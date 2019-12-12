package com.example.laboratorio6;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

public class SonidoSoundPool {
	SoundPool soundPool;
	private int sonidoID = -1;

	public SonidoSoundPool(Context contexto, String nombreArchivo) {
		/* Se crea el objeto SoundPool(max, tipo, calidad) */
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		try {
			/* Provee el acceso a la carpeta assets */
			AssetManager am = contexto.getAssets();
			/* Abre el archivo */
			AssetFileDescriptor afd = am.openFd(nombreArchivo);
			/* Lee el archivo (descripcion, prioridad) */
			sonidoID = soundPool.load(afd, 1);
		} catch (IOException e) {
			// "No se puede cargar el archivo de efecto de sonido, " +
			// e.getMessage());
			e.printStackTrace();
		}
	}

	public void play(int x) {
		/*
		 * reproduce(sonidoID, volumen izq, volumen der, prioridad, repetici�n,
		 * velocidad) reproduce(id, [0..1], [0..1], [0..1], [-1 = c/repetici�n 0
		 * = s/repetici�n], [0.5 .. 2] 1 = normal)
		 */
		soundPool.play(sonidoID, x, x, 0, 0, 1);
	}
}