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
 * Rect�ngulo.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class Rectangle extends Point
{
	/**
	 * Ancho.
	 */
	public int width;

	/**
	 * Alto.
	 */
	public int height;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea un rect�ngulo.
	 */
	public Rectangle()
	{
		this(0,0,0,0);
	}

	/**
	 * Crea un rect�ngulo.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param width Ancho
	 * @param height Alto
	 */
	public Rectangle(int x,int y,int width,int height)
	{
		super(x,y);
		setSize(width,height);
	}

	/**
	 * Crea un rect�ngulo.
	 *
	 * @param r Rect�ngulo que contiene los datos
	 */
	public Rectangle(Rectangle r)
	{
		this(r.x,r.y,r.width,r.height);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el tama�o del rect�ngulo.
	 *
	 * @param width Ancho
	 * @param height Alto
	 */
	public void setSize(int width,int height)
	{
		this.width=width;
		this.height=height;
	}

	/**
	 * Cambia las coordenadas y el tama�o del rect�ngulo.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param width Ancho
	 * @param height Alto
	 */
	public void setBounds(int x,int y,int width,int height)
	{
		setLocation(x,y);
		setSize(width,height);
	}

	/**
	 * Cambia las coordenadas y el tama�o del rect�ngulo.
	 *
	 * @param r Rect�ngulo que contiene los datos
	 */
	public void setBounds(Rectangle r)
	{
		setBounds(r.x,r.y,r.width,r.height);
	}

    /**
     * Comprueba si contiene las coordenadas indicadas.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     *
     * @return True si el rect�ngulo contiene las coordenadas
     */
    public boolean contains(int x,int y)
    {
		return ((x>=this.x)&&(x<(this.x+this.width))&&(y>=this.y)&&(y<(this.y+this.height)));
    }

    /**
     * Comprueba si contiene las coordenadas indicadas.
     *
     * @param p Coordenadas
     *
     * @return True si el rect�ngulo contiene las coordenadas
     *
     * @see Point
     */
    public boolean contains(Point p)
    {
		return contains(p.x,p.y);
    }

    /**
     * Comprueba si hay intersecci�n con otro rect�ngulo.
     *
     * @param r Rect�ngulo
     *
     * @return True si hay intersecci�n
     */
    public boolean intersects(Rectangle r)
    {
    	return ((r.x+r.width>x)&&(r.x<x+width)&&(r.y+r.height>y)&&(r.y<y+height));
    }

    /**
     * Calcula la intersecci�n con otro rect�ngulo.
     *
     * @param r Rect�ngulo
     *
     * @return Nuevo rect�ngulo con los datos de la intersecci�n
     */
    public Rectangle intersection(Rectangle r)
    {
    	return intersection(r,new Rectangle());
    }

    /**
     * Calcula la intersecci�n con otro rect�ngulo.
     *
     * @param r Rect�ngulo
     * @param dst Rect�ngulo donde almacena los datos de la intersecci�n
     *
     * @return Rect�ngulo con los datos de la intersecci�n (0,0,0,0 si no intersecta)
     */
    public Rectangle intersection(Rectangle r,Rectangle dst)
    {
	    if (intersects(r))
	    {
			int x1=Math.max(x,r.x);
			int y1=Math.max(y,r.y);
			int x2=Math.min(x+width,r.x+r.width);
			int y2=Math.min(y+height,r.y+r.height);
			dst.setBounds(x1,y1,x2-x1,y2-y1);
	    }
	    else dst.setBounds(0,0,0,0);
	    //
		return dst;
    }

	/*************************************************************************/
	/*************************************************************************/

	public String toString()
	{
		return getClass().getName()+" [x="+x+",y="+y+",width="+width+",height="+height+"]";
	}
}