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

import com.java4ever.apime.math.*;

/**
 * Gestor de ratón.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class CursorManager
{
	/**
	 * Instancia global.
	 */
	public static final CursorManager instance=new CursorManager();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Constante para parar el movimiento.
	 */
	static final int MOVE_STOP		= 0;

	/**
	 * Constante para el movimiento izquierda/arriba.
	 */
	static final int MOVE_LU		= -1;

	/**
	 * Constante para el movimiento derecha/abajo.
	 */
	static final int MOVE_RD		= 1;

	/**
	 * Constante para no cambiar el movimiento.
	 */
	static final int MOVE_IGNORE	= 2;

	/*************************************************************************/

	/**
	 * Coordenadas globales.
	 *
	 * @see Point
	 */
	Point pos=new Point();

	/**
	 * Activado.
	 */
	boolean enabled=true;

	// Variables de movimiento.
	private Point limit=new Point(),posFP=new Point(),movement=new Point();
	private int movement_timer=Integer.MIN_VALUE;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el gestor.
	 */
	private CursorManager()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Activa/Desactiva el control con el cursor.
	 *
	 * @param enabled True para activarlo
	 */
	public void setEnabled(boolean enabled)
	{
		if (!(this.enabled=enabled)) move(MOVE_STOP,MOVE_STOP);
	}
	
	/**
	 * Devuelve si el control del cursor está activado.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Inicializa el gestor.
	 *
	 * @param width Ancho de la zona visual
	 * @param height Alto de la zona visual
	 */
	void init(int width,int height)
	{
		limit.setLocation((width-1)<<10,(height-1)<<10);
		setLocation(width>>1,height>>1);
		// Cursores por defecto.
		byte _default[]=new byte[]{-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,10,0,0,0,19,4,3,0,0,0,91,-59,-127,-35,0,0,0,9,80,76,84,69,0,-1,107,0,0,0,-1,-1,-1,44,39,-47,43,0,0,0,1,116,82,78,83,0,64,-26,-40,102,0,0,0,52,73,68,65,84,120,94,69,-52,-79,13,0,64,8,66,81,-35,0,110,3,-36,127,-56,-117,-65,-111,-30,21,36,80,-86,-115,-15,9,-125,35,12,-114,48,56,-62,-96,-81,95,-67,107,87,-17,0,57,60,-37,31,64,23,5,-54,-13,-104,-74,-67,0,0,0,0,73,69,78,68,-82,66,96,-126};
		byte _hand[]=new byte[]{-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,16,0,0,0,20,4,3,0,0,0,118,76,-96,68,0,0,0,9,80,76,84,69,0,-1,107,0,0,0,-1,-1,-1,44,39,-47,43,0,0,0,1,116,82,78,83,0,64,-26,-40,102,0,0,0,56,73,68,65,84,120,94,-115,-58,-127,9,0,32,12,3,-63,100,-125,-90,27,-24,-2,67,106,-47,22,65,4,-97,16,14,-128,-80,98,-73,15,-56,68,11,120,-101,3,-75,17,127,33,58,-47,-97,96,2,-98,96,65,-111,21,6,-82,-81,12,-63,-64,61,71,22,0,0,0,0,73,69,78,68,-82,66,96,-126};
		byte _text[]=new byte[]{-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,9,0,0,0,15,4,3,0,0,0,-60,-26,-8,62,0,0,0,6,80,76,84,69,0,-1,107,0,0,0,-125,36,14,65,0,0,0,1,116,82,78,83,0,64,-26,-40,102,0,0,0,32,73,68,65,84,120,94,-83,-52,-79,17,0,0,8,-125,64,-36,64,-10,95,86,-17,-78,66,10,40,31,29,23,-8,90,-113,121,60,-42,1,89,-63,-97,-27,95,0,0,0,0,73,69,78,68,-82,66,96,-126};
		Cursor.addCursor("default",new Cursor(Image.createImage(_default,0,_default.length),0,0));
		Cursor.addCursor("hand",new Cursor(Image.createImage(_hand,0,_hand.length),5,0));
		Cursor.addCursor("text",new Cursor(Image.createImage(_text,0,_text.length),5,0));
	}

	/**
	 * Cambia las coordenadas del cursor.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	void setLocation(int x,int y)
	{
		move(MOVE_STOP,MOVE_STOP);
		pos.setLocation(x,y);
		posFP.setLocation(x<<10,y<<10);
	}

	/**
	 * Mueve el cursor.
	 *
	 * @param x Dirección en la coordenada X
	 * @param y Dirección en la coordenada Y
	 *
	 * @see #MOVE_LU
	 * @see #MOVE_RD
	 * @see #MOVE_STOP
	 * @see #MOVE_IGNORE
	 */
	void move(int x,int y)
	{
		if (x==MOVE_IGNORE) x=movement.x;
		if (y==MOVE_IGNORE) y=movement.y;
		//
		if (x!=movement.x||y!=movement.y)
		{
			movement.setLocation(x,y);
			if (x!=MOVE_STOP||y!=MOVE_STOP)
			{
				movement_timer=0;
				pos.setLocation(pos.x<<10,pos.y<<10);
			}
			else movement_timer=Integer.MIN_VALUE;
		}
	}

	/**
	 * Actualiza el cursor.
	 *
	 * @param delay Tiempo transcurrido desde el frame anterior (en ms.)
	 *
	 * @return True si ha cambiado de posición
	 */
	boolean update(int delay)
	{
		if (movement_timer!=Integer.MIN_VALUE)
		{
			// Coordenadas actuales.
			int x=pos.x;
			int y=pos.y;
			// Desplazamiento según la aceleración.
			movement_timer+=delay;
			int inc=Math.min(300,30+((250*movement_timer)>>10))*delay;
			posFP.x+=(inc*movement.x);
			posFP.y+=(inc*movement.y);
			// Calcula las nuevas coordenadas.
			if (posFP.x<0) posFP.x=0;
				else if (posFP.x>limit.x) posFP.x=limit.x;
			if (posFP.y<0) posFP.y=0;
				else if (posFP.y>limit.y) posFP.y=limit.y;
			pos.setLocation(posFP.x>>10,posFP.y>>10);
			//
			return (x!=pos.x||y!=pos.y);
		}
		else return false;
	}
}