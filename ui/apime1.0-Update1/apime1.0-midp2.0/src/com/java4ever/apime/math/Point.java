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
package com.java4ever.apime.math;

/**
 * Punto.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class Point
{
	/**
	 * Coordenada X.
	 */
	public int x;

	/**
	 * Coordenada Y.
	 */
	public int y;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea un punto.
	 */
	public Point()
	{
		this(0,0);
	}

	/**
	 * Crea un punto.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	public Point(int x,int y)
	{
		setLocation(x,y);
	}

	/**
	 * Crea un punto.
	 *
	 * @param p Punto que contiene los datos
	 */
	public Point(Point p)
	{
		this(p.x,p.y);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia las coordenadas del punto.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	public void setLocation(int x,int y)
	{
		this.x=x;
		this.y=y;
	}

	/**
	 * Cambia las coordenadas del punto.
	 *
	 * @param p Punto que contiene los datos
	 */
	public void setLocation(Point p)
	{
		setLocation(p.x,p.y);
	}

	/*************************************************************************/
	/*************************************************************************/

	public String toString()
	{
		return getClass().getName()+" [x="+x+",y="+y+"]";
	}
}