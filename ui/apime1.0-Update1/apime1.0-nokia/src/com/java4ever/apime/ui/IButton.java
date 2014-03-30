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

import com.java4ever.apime.ui.event.*;


/**
 * Botón con 2 estados.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IButton extends ILabel
{
	/**
	 * Indice visual para el estado normal (0).
	 */
	public static final int NORMAL		= 0;

	/**
	 * Indice visual para el estado selected (1).
	 */
	public static final int SELECTED	= 1;

	/**
	 * Indice visual para el estado rollover (2).
	 */
	public static final int ROLLOVER	= 2;

	/**
	 * Indice visual para el estado pressed (3).
	 */
	public static final int PRESSED		= 3;

	/**
	 * Indice visual para el estado disabled (4).
	 */
	public static final int DISABLED	= 4;
	
	/*************************************************************************/

	/**
	 * Colores según el estado del botón.
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	int colors[];

	/**
	 * Imágenes según el estado del botón (pueden ser 'null').
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	Image images[];

	/**
	 * Indica si está seleccionado.
	 */
	protected boolean selected;

	/**
	 * Indica si está el cursor sobre el botón.
	 */
	protected boolean rollover;

	/**
	 * Indica si está pretado.
	 */
	protected boolean pressed;
	
	/**
	 * Grupo en el que se encuentra el botón o 'null' si no está en ninguno.
	 *
	 * @see ButtonGroup
	 */
	ButtonGroup group;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el botón.
	 *
	 * @param text Texto del botón
	 */
	public IButton(String text)
	{
		this(text,null);
	}

	/**
	 * Crea el botón (ajusta el tamaño al de la imagen)..
	 *
	 * @param image Imagen
	 */
	public IButton(Image image)
	{
		this("",image);
	}

	/**
	 * Crea el botón.
	 *
	 * @param text Texto del botón
	 * @param image Imagen
	 */
	public IButton(String text,Image image)
	{
		super(text,image);
		//
		images=new Image[5];
		images[NORMAL]=image;
		if (image!=null) setSize(image.getWidth(),image.getHeight());
			else halign=Graphics.HCENTER;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Asigna un color de fondo.
	 *
	 * @param index Indice visual
	 * @param color Color
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	public void setBackground(int index,int color)
	{
		colors[index]=color;
		repaint();
	}

	/**
	 * Devuelve un color de fondo.
	 *
	 * @param index Indice visual
	 *
	 * @return Color
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	public int getBackground(int index)
	{
		return colors[index];
	}

	/**
	 * Asigna una imagen.
	 *
	 * @param index Indice visual
	 * @param image Imagen (puede ser 'null')
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	public void setImage(int index,Image image)
	{
		images[index]=image;
		repaint();
	}

	/**
	 * Devuelve una imagen.
	 *
	 * @param index Indice visual
	 *
	 * @return Imagen o 'null' si no tiene
	 *
	 * @see #NORMAL
	 * @see #SELECTED
	 * @see #ROLLOVER
	 * @see #PRESSED
	 * @see #DISABLED
	 */
	public Image getImage(int index)
	{
		return images[index];
	}

	/**
	 * Selecciona/Deselecciona el botón.
	 *
	 * @param selected True para seleccionarlo
	 */
	public void setSelected(boolean selected)
	{
		if (this.selected!=selected&&(group==null||selected||group.selected!=this))
		{
			this.selected=selected;
			if (group!=null&&selected) group.update(this);
			repaint();
			processActionEvent(null);
		}
	}
	
	/**
	 * Devuelve si el botón está seleccionado.
	 */
	public boolean isSelected()
	{
		return selected;
	}
	
	public void setEnabled(boolean enabled)
	{
		if (!enabled) pressed=rollover=false;
		super.setEnabled(enabled);
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_IBUTTON;
	}

	public void processSkinProperties()
	{
		super.processSkinProperties();
		//
		int id=getSkinId();
		colors=new int[5];
		colors[NORMAL]=skin.getInt(id,Skin.PROPERTY_IBUTTON_NORMAL);
		colors[SELECTED]=skin.getInt(id,Skin.PROPERTY_IBUTTON_SELECTED);
		colors[ROLLOVER]=skin.getInt(id,Skin.PROPERTY_IBUTTON_ROLLOVER);
		colors[PRESSED]=skin.getInt(id,Skin.PROPERTY_IBUTTON_PRESSED);
		colors[DISABLED]=skin.getInt(id,Skin.PROPERTY_IBUTTON_DISABLED);
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paint(Graphics g)
	{
		updateVisualStatus();
		super.paint(g);
	}

	/**
	 * Actualiza el estado visual (color/imagen) según el estado del botón.
	 */
	protected void updateVisualStatus()
	{
		// Comprueba el tipo de imagen a visualizar.
    	if (!enabled&&images[DISABLED]!=null) image=images[DISABLED];
    		else if (pressed&&images[PRESSED]!=null) image=images[PRESSED];
    		else if (rollover&&images[ROLLOVER]!=null) image=images[ROLLOVER];
    		else if (selected&&images[SELECTED]!=null) image=images[SELECTED];
    		else image=images[NORMAL];
		// Selecciona el tipo de color a visualizar.
    	if (!enabled) background=colors[DISABLED];
    		else if (pressed) background=colors[PRESSED];
    		else if (rollover) background=colors[ROLLOVER];
    		else if (selected) background=colors[SELECTED];
    		else background=colors[NORMAL];
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processMouseEvent(int type,int x,int y)
	{
		if (type!=Event.MOUSE_MOVED)
		{
			if (type==Event.MOUSE_RELEASED)
			{
				pressed=false;
				if (rollover&&(group==null||!selected)) setSelected(!selected);
			}
			else if (type==Event.MOUSE_PRESSED) pressed=true;
			else if (type==Event.MOUSE_ENTERED) rollover=true;
			else if (type==Event.MOUSE_EXITED) pressed=rollover=false;
			else if (type==Event.MOUSE_DRAGGED) pressed=rollover=contains(x,y);
			//
			repaint();
		}
		//
		super.processMouseEvent(type,x,y);
	}
}