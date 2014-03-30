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
 * Etiqueta de texto (1 línea).
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ILabel extends ITextComponent
{
	/**
	 * Imagen ('null' si no tiene).
	 */
	Image image;
	
	private short lines[]=new short[]{1,0,0};

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea una etiqueta con el texto indicado.
	 *
	 * @param text Texto
	 */
	public ILabel(String text)
	{
		this(text,null);
	}

	/**
	 * Crea una etiqueta que representa una imagen (ajusta el tamaño al de la imagen).
	 *
	 * @param image Imagen
	 */
	public ILabel(Image image)
	{
		this("",image);
		setSize(image.getWidth(),image.getHeight());
	}

	/**
	 * Crea una etiqueta con el texto y la imagen indicada.
	 *
	 * @param text Texto
	 * @param image Imagen
	 */
	public ILabel(String text,Image image)
	{
		super(text);
		this.image=image;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia la imagen (puede ser 'null').
	 *
	 * @param image Imagen
	 */
	public void setImage(Image image)
	{
		if (this.image!=image)
		{
			this.image=image;
			repaint();
		}
	}

	/**
	 * Devuelve la imagen.
	 */
	public Image getImage()
	{
		return image;
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ILABEL;
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		int offset_x=0;
		// Imagen.
		if (image!=null)
		{
			g.drawImage(image,0,(bounds.height-image.getHeight())>>1,Graphics.LEFT|Graphics.TOP);
			offset_x=image.getWidth();
		}
		// Texto
		if (text.length()>0)
		{
			g.translate(2+offset_x,2);
			lines[2]=(short)text.length();
			font.drawString(g,text,lines,bounds.width-offset_x-4,bounds.height-4,halign,valign);
			g.translate(-(2+offset_x),-2);
		}
	}
}