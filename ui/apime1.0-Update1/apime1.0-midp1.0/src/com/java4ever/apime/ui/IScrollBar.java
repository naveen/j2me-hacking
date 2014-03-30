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
 * Barra de scroll.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IScrollBar extends IContainer
{
	/**
	 * Barra de scroll horizontal.
	 */
	public static final int HORIZONTAL	= 0;

	/**
	 * Barra de scroll vertical.
	 */
	public static final int VERTICAL	= 1;

	/**
	 * Grosor de la barra.
	 */
	public static int THICK;

	/**
	 * Indica si el scroll es automático (no hay que hacer drag).
	 */
	public static boolean AUTOSCROLL;

	/*************************************************************************/

	/**
	 * Desplazamiento [0..100].
	 */
	int offset;

	/**
	 * Porcentage visual [0..100].
	 */
	int percentage=100;

	// Variables de control.
	private int type,limit;
	private IButton button;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la barra.
	 *
	 * @param type Tipo (horizontal/vertical)
	 * @param size Tamaño
	 *
	 * @see #HORIZONTAL
	 * @see #VERTICAL
	 */
	public IScrollBar(int type,int size)
	{
		this.type=type;
		// Crea el botón interno.
		add(button=new ScrollButton(),0,0,THICK,THICK);
		button.setMouseListener(new ScrollListener());
		// Ajusta el tamaño de la barra.
		setSize(size);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el tamaño.
	 *
	 * @param size Tamaño
	 */
	public void setSize(int size)
	{
		setSize(size,size);
	}
	
	public void setSize(int width,int height)
	{
		if (type==HORIZONTAL) super.setSize(width,THICK);
			else super.setSize(THICK,height);
		recalculate(true);
	}

	/**
	 * Posiciona la barra.
	 *
	 * @param offset Desplazamiento [0..100]
	 */
	public void setOffset(int offset)
	{
		// Comprueba los límites.
		if (offset<0) offset=0;
			else if (offset>100) offset=100;
		// Comprueba si ha cambiado.
		if (this.offset!=offset)
		{
			this.offset=offset;
			recalculate(false);
			//
			processActionEvent(null);
		}
	}

	/**
	 * Devuelve la posición de la barra [0..100].
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Porcentaje visual de la barra.
	 *
	 * @param percentage Porcentaje [0..100]
	 */
	public void setPercentage(int percentage)
	{
		// Comprueba los límites.
		if (percentage<0) percentage=0;
			else if (percentage>100) percentage=100;
		// Comprueba si ha cambiado.
		if (this.percentage!=percentage)
		{
			this.percentage=percentage;
			recalculate(true);
		}
	}

	/**
	 * Devuelve el porcentaje visual de la barra [0..100].
	 */
	public int getPercentage()
	{
		return percentage;
	}

	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		button.setEnabled(enabled);
	}

	private void recalculate(boolean size)
	{
		int x=button.bounds.x;
		int y=button.bounds.y;
		int width=button.bounds.width;
		int height=button.bounds.height;
		// Calcula el nuevo tamaño y posición.
		if (type==HORIZONTAL)
		{
			if (size)
			{
				width=Math.max(THICK,(bounds.width*percentage)/100);
				limit=bounds.width-width;
			}
			x=(limit*offset)/100;
		}
		else
		{
			if (size)
			{
				height=Math.max(THICK,(bounds.height*percentage)/100);
				limit=bounds.height-height;
			}
			y=(limit*offset)/100;
		}
		// Ajusta la barra.
		if (size) button.setBounds(x,y,width,height);
			else button.setLocation(x,y);
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ISCROLLBAR;
	}

	/*************************************************************************/
	/*************************************************************************/

	private class ScrollButton extends IButton
	{
		public ScrollButton()
		{
			super("");
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			//
			int center_x=bounds.width>>1;
			int center_y=bounds.height>>1;
			if (type==HORIZONTAL)
			{
				g.drawLine(center_x-2,bounds.y+2,center_x-2,bounds.height-3);
				g.drawLine(center_x,bounds.y+2,center_x,bounds.height-3);
				g.drawLine(center_x+2,bounds.y+2,center_x+2,bounds.height-3);
			}
			else
			{
				g.drawLine(2,center_y-2,bounds.width-3,center_y-2);
				g.drawLine(2,center_y,bounds.width-3,center_y);
				g.drawLine(2,center_y+2,bounds.width-3,center_y+2);
			}
		}

		public int getSkinId()
		{
			return Skin.KEY_ISCROLLBAR$SCROLLBUTTON;
		}
	}

	private class ScrollListener extends MouseAdapter
	{
		private Point drag=new Point(-1,-1);
		private int dir,offset,init;

		public void mousePressed(IComponent component,int x,int y)
		{
			drag.setLocation(x,y);
		}

		public void mouseEntered(IComponent component,int x,int y)
		{
			drag.setLocation(x,y);
			offset=-1;
			init=(type==HORIZONTAL?x:y);
		}

		public void mouseMoved(IComponent component,int x,int y)
		{
			// Comprueba que es movimiento en dirección de hacer drag.
			if (AUTOSCROLL&&((type==HORIZONTAL&&x!=init)||(type==VERTICAL&&y!=init)))
			{
				int pos=(type==HORIZONTAL?component.bounds.x:component.bounds.y);
				mouseDragged(component,x,y);
				// Comprueba si el componente no ha cambiado de posición.
				if ((type==HORIZONTAL&&pos==component.bounds.x)||(type==VERTICAL&&pos==component.bounds.y))
				{
					int coord=(type==HORIZONTAL?x:y);
					// Comprueba si hay cambio de dirección en el movimiento del cursor.
					if (offset!=-1)
					{
						int d=(offset<coord?1:-1);
						if (d!=dir) drag.setLocation(x,y);
						dir=d;
					}
					else dir=0;
					//
					init=offset=coord;
				}
				else offset=-1;
			}
		}

		public void mouseDragged(IComponent component,int x,int y)
		{
			int offset=0;
			if (type==HORIZONTAL) offset=button.bounds.x+(x-drag.x);
				else offset=button.bounds.y+(y-drag.y);
			// Calcula el desplazamiento.
			if (limit>0) setOffset((offset*100)/limit);
				else setOffset(0);
		}
	}
}