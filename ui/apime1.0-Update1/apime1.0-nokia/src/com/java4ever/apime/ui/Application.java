/*
 * Apime v1.0 - Framework for j2me applications.
 *
 * Copyright (c) 2004 Carlos Araiz (caraiz@java4ever.com)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.java4ever.apime.ui;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

/**
 * Clase base para crear aplicaciones.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public abstract class Application extends MIDlet implements Runnable
{
	private boolean paused;
	private long fps_delay;
	private String splash;
	private Thread thread;
	private ApplicationCanvas canvas;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la aplicación a los FPS indicados.
	 *
	 * @param fps Frames por segundo deseados
	 * @param splash Splash screen (puede ser 'null')
	 */
	public Application(int fps,String splash)
	{
		setFPS(fps);
		this.splash=splash;
	}

	protected void startApp()
	{
		// Crea el canvas y el hilo principal.
		if (canvas==null)
		{
			Display.getDisplay(this).setCurrent(canvas=new ApplicationCanvas());
			thread=new Thread(this);
			thread.start();
		}
		pause(false);
	}

	protected void pauseApp()
	{
		pause(true);
	}

	protected void destroyApp(boolean unconditional)
	{
	}

	/**
	 * Llamado cuando cambia el estado de pausa.
	 *
	 * @param pause True para indicar que entra en pausa
	 */
	protected synchronized void pause(boolean pause)
	{
		paused=pause;
	}

	/**
	 * Sale de la aplicación.
	 */
	public void exit()
	{
		thread=null;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el contenedor principal.
	 *
	 * @see IContainer
	 */
	public IContainer getRoot()
	{
		return canvas.root;
	}

	/**
	 * Cambia los frames por segundo de la aplicación.
	 *
	 * @param fps Frames por segundo deseados
	 */
	public void setFPS(int fps)
	{
		fps_delay=1000/fps;
	}
	
	/**
	 * Cambia el skin (Debería de ser llamado antes de crear los componentes).
	 *
	 * @param skin Skin ('null' para asignar el skin por defecto)
	 *
	 * @see Skin
	 */
	public void setSkin(Skin skin)
	{
		canvas.setSkin(skin);
	}

	/**
	 * Devuelve el skin actual.
	 *
	 * @see Skin
	 */
	public Skin getSkin()
	{
		return canvas.skin;
	}

	/*************************************************************************/
	/*************************************************************************/

	public void run()
	{
		// Inicializa el canvas y el código.
		while (!canvas.isShown()) thread.yield();
		canvas.init(this,splash);
		initCode();
		canvas.initEvents();
		splash=null;
		System.gc();
		// Bucle principal.
		long frame_time=System.currentTimeMillis(),fps_fix=0;
		while (thread!=null)
		{
			// Calcula el tiempo transcurrido desde el último frame.
			long t=System.currentTimeMillis();
			int delay=(int)(t-frame_time);
			frame_time=t;
			if (!paused)
			{
				// Actualiza el código.
				updateCode(delay);
				// Actualiza el canvas.
				canvas.update(delay);
			}
			// Tiempo de espera para obtener FPS constantes.
			try
			{
				t=System.currentTimeMillis();
				long d=fps_delay-(t-frame_time)-fps_fix;
				if (d>0)
				{
					thread.sleep(d);
					fps_fix=(System.currentTimeMillis()-t)-d;
				}
				else
				{
					thread.yield();
					fps_fix=0;
				}
			}
			catch (Exception ex)
			{
			}
		}
		// Finaliza.
		destroyCode();
		notifyDestroyed();
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Inicializa el código.
	 */
	protected abstract void initCode();

	/**
	 * Actualiza el código.
	 *
	 * @param delay Tiempo transcurrido desde el frame anterior (en ms.).
	 */
	protected abstract void updateCode(int delay);

	/**
	 * Elimina el código.
	 */
	protected abstract void destroyCode();
}