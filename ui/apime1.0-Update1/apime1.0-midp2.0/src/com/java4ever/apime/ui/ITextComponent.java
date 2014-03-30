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
import com.java4ever.apime.ui.event.*;

/**
 * Componente de texto.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public abstract class ITextComponent extends IComponent
{
	/**
	 * Texto.
	 */
	String text;

	/**
	 * Alineación horizontal.
	 */
	int halign;

	/**
	 * Alineación vertical.
	 */
	int valign;

	/**
	 * Listener de texto.
	 *
	 * @see TextListener
	 */
	TextListener text_listener;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el componente de texto.
	 *
	 * @param text Texto
	 */
	public ITextComponent(String text)
	{
		this.text=text;
		halign=Graphics.LEFT;
		valign=Graphics.VCENTER;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el texto.
	 *
	 * @param text Nuevo texto
	 */
	public void setText(String text)
	{
		this.text=text;
		repaint();
		processTextEvent();
	}

	/**
	 * Devuelve el texto.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Cambia la alineación del texto.
	 *
	 * @param halign Alineación horizontal
	 * @param valign Alineación vertical
	 *
	 * @see Graphics
	 */
	public void setAlignment(int halign,int valign)
	{
		this.halign=halign;
		this.valign=valign;
		repaint();
	}

	/**
	 * Devuelve la alineación del texto.
	 *
	 * @see Point
	 * @see Graphics
	 */
	public Point getAlignment()
	{
		return new Point(halign,valign);
	}

	/**
	 * Cambia la alineación horizontal del texto.
	 *
	 * @param align Alineación
	 *
	 * @see Graphics
	 */
	public void setHorizontalAlignment(int align)
	{
		setAlignment(align,valign);
	}

	/**
	 * Devuelve la alineación horizontal del texto.
	 *
	 * @see Graphics
	 */
	public int getHorizontalAlignment()
	{
		return halign;
	}

	/**
	 * Cambia la alineación vertical del texto.
	 *
	 * @param align Alineación
	 *
	 * @see Graphics
	 */
	public void setVerticalAlignment(int align)
	{
		setAlignment(halign,align);
	}

	/**
	 * Devuelve la alineación vertical del texto.
	 *
	 * @see Graphics
	 */
	public int getVerticalAlignment()
	{
		return valign;
	}

	/**
	 * Asigna el listener de texto.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see TextListener
	 */
	public void setTextListener(TextListener listener)
	{
		text_listener=listener;
	}

	/**
	 * Devuelve el listener de texto o 'null' si no tiene.
	 *
	 * @see TextListener
	 */
	public TextListener getTextListener()
	{
		return text_listener;
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ITEXTCOMPONENT;
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processTextEvent()
	{
		if (text_listener!=null) text_listener.textValueChanged(this);
	}
}