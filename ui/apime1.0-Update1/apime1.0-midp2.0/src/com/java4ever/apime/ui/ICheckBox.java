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
 * CheckBox.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ICheckBox extends IButton
{
	/**
	 * Crea el checkbox.
	 *
	 * @param text Texto del checkbox
	 */
	public ICheckBox(String text)
	{
		this(text,null,null);
	}
	
	/**
	 * Crea el checkbox (ajusta el tamaño al de la imagen normal).
	 *
	 * @param normal Imagen para el estado normal del checkbox
	 * @param selected Imagen para el estado selected del checkbox
	 */
	public ICheckBox(Image normal,Image selected)
	{
		this("",normal,selected);
	}

	/**
	 * Crea el checkbox.
	 *
	 * @param text Texto del checkbox
	 * @param normal Imagen para el estado normal del checkbox
	 * @param selected Imagen para el estado selected del checkbox
	 */
	public ICheckBox(String text,Image normal,Image selected)
	{
		super(text,normal);
		//
		images[SELECTED]=selected;
		halign=Graphics.LEFT;
	}
	
	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ICHECKBOX;
	}

	/*************************************************************************/
	/*************************************************************************/
	
	public void paintComponent(Graphics g)
	{
		if (image==null)
		{
			int x=4;
			int y=(bounds.height-11)>>1;
			g.translate(x,y);
			g.setColor(foreground);
			if (group==null) paintBox(g);
				else paintCircle(g);
			g.translate(-x,-y);
			//
			g.translate(16,0);
			super.paintComponent(g);
			g.translate(-16,0);
		}
		else super.paintComponent(g);
	}

	/**
	 * Pinta la caja (ICheckBox sin grupo).
	 */
	private void paintBox(Graphics g)
	{
		g.drawRect(0,0,10,10);
		if (selected)
		{
			g.drawLine(1,1,9,9);
			g.drawLine(1,9,9,1);
		}
	}

	/**
	 * Pinta el círculo (ICheckBox con grupo).
	 */
	private void paintCircle(Graphics g)
	{
		g.drawArc(0,0,10,10,0,360);
		if (selected) g.fillArc(3,3,5,5,0,360);
	}

	protected void updateVisualStatus()
	{
		super.updateVisualStatus();
		//
		foreground=background;
		background=0x00ffffff;
	}
}