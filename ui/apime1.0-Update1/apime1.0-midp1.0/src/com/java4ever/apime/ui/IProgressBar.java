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

/**
 * Barra de progreso.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IProgressBar extends ILabel
{
	/**
	 * Texto (si hay un '%' lo reemplaza por 'porcentage%').
	 */
	String text;

	/**
	 * Valor actual.
	 */
	int value;

	/**
	 * Valor máximo.
	 */
	int max;

	/**
	 * Color de la barra de progreso.
	 */
	int progress_color;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la barra.
	 *
	 * @param text Texto (si hay un '%' lo reemplaza por 'porcentaje%')
	 * @param max Valor máximo
	 */
	public IProgressBar(String text,int max)
	{
		super("");
		//
		this.max=max;
		halign=Graphics.HCENTER;
		setText(text);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el texto.
	 *
	 * @param text Texto (si hay un '%' lo reemplaza por 'porcentaje%')
	 */
	public void setText(String text)
	{
		this.text=text;
		//
		int index=text.indexOf("%");
		if (index==-1) super.setText(text);
			else super.setText(text.substring(0,index)+getPercentage()+"%"+text.substring(index+1));
	}

	/**
	 * Devuelve el texto.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Asigna el valor actual.
	 *
	 * @param value Valor
	 */
	public void setValue(int value)
	{
		this.value=Math.min(value,max);
		setText(text);
	}

	/**
	 * Devuelve el valor actual.
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Asigna el valor máximo.
	 *
	 * @param max Valor
	 */
	public void setMax(int max)
	{
		this.max=max;
		value=Math.min(value,max);
		setText(text);
	}

	/**
	 * Devuelve el valor máximo.
	 */
	public int getMax()
	{
		return max;
	}

	/**
	 * Incrementa el progreso.
	 */
	public void inc()
	{
		setValue(value+1);
	}

	/**
	 * Devuelve el porcentaje actual [0..100].
	 */
	public int getPercentage()
	{
		return (value*100)/max;
	}

	/**
	 * Cambia el color de la barra de progreso.
	 *
	 * @param color Color
	 */
	public void setProgressColor(int color)
	{
		this.progress_color=color;
	}

	/**
	 * Devuelve el color de la barra de progreso.
	 */
	public int getProgressColor()
	{
		return progress_color;
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_IPROGRESSBAR;
	}

	public void processSkinProperties()
	{
		super.processSkinProperties();
		//
		progress_color=skin.getInt(getSkinId(),Skin.PROPERTY_IPROGRESSBAR_PROGRESS_COLOR);
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		// Rellena la barra.
		g.setColor(progress_color);
		g.fillRect(0,0,((bounds.width-1)*getPercentage())/100,bounds.height-1);
		// Pinta el texto.
		g.setColor(foreground);
		super.paintComponent(g);
	}
}