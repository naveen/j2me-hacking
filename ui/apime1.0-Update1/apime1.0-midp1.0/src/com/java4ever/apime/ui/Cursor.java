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

import java.util.*;
import javax.microedition.lcdui.*;

import com.java4ever.apime.math.*;

/**
 * Puntero del ratón.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class Cursor
{
	/**
	 * Cursores.
	 */
	static Hashtable cursors=new Hashtable();
	
	/**
	 * Cursor por defecto.
	 */
	static Cursor DEFAULT;

	/*************************************************************************/

	/**
	 * Imagen del cursor.
	 */
	Image image;

	/**
	 * Coordenadas del punto de acción.
	 *
	 *  @see Point
	 */
	Point hotspot;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el puntero.
	 *
	 * @param image Imagen
	 * @param x Coordenada X del punto de acción
	 * @param y Coordenada Y del punto de acción
	 */
	public Cursor(Image image,int x,int y)
	{
		this.image=image;
		hotspot=new Point(x,y);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Añade un cursor.
	 *
	 * @param name Nombre
	 * @param cursor Cursor
	 */
	public static void addCursor(String name,Cursor cursor)
	{
		cursors.put(name,cursor);
		if (name.compareTo("default")==0) DEFAULT=cursor;
	}

	/**
	 * Devuelve un cursor.
	 *
	 * @param name Nombre del cursor
	 *
	 * @return Cursor con ese nombre o el cursor por defecto si no existe
	 */
	public static Cursor getCursor(String name)
	{
		Object obj=cursors.get(name);
		return (obj!=null?(Cursor)obj:DEFAULT);
	}

	/**
	 * Elimina un cursor.
	 *
	 * @param name Nombre del cursor
	 */
	public static void removeCursor(String name)
	{
		cursors.remove(name);
	}
}