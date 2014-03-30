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
 * Area de texto.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ITextArea extends ITextField
{
	/**
	 * Indica si corta las líneas.
	 */
	boolean wrap;

	/**
	 * Indica si ajusta el tamaño al del texto.
	 */
	boolean pack;

	// Variables de control.
	private Point min_size=new Point();
	private short lines[];
	private boolean recalculate=true;
	private Point caret_line=new Point(),caret_xy=new Point();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el área de texto.
	 *
	 * @param text Texto
	 */
	public ITextArea(String text)
	{
		super(text);
		//
		halign=Graphics.LEFT;
		valign=Graphics.TOP;
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void setText(String text,boolean caret)
	{
		super.setText(text,caret);
		recalculate();
	}
	
	public void setSize(int width,int height)
	{
		if (min_size.x==0||min_size.y==0) min_size.setLocation(width,height);
    	if (pack) super.setSize(width,height);
    		else super.setSize(Math.max(min_size.x,width),Math.max(min_size.y,height));
	}

	/**
	 * Indica si corta las líneas.
	 *
	 * @param wrap True para cortar las líneas
	 */
	public void setWrap(boolean wrap)
	{
		this.wrap=wrap;
		recalculate();
	}

	/**
	 * Devuelve si corta las líneas.
	 */
	public boolean isWrap()
	{
		return wrap;
	}

	/**
	 * Indica si ajusta el tamaño al del texto.
	 *
	 * @param pack True para ajustar
	 */
	public void setPack(boolean pack)
	{
		this.pack=pack;
		recalculate();
	}

	/**
	 * Devuelve si ajusta el tamaño al del texto.
	 */
	public boolean isPack()
	{
		return pack;
	}

	public void setCaretPosition(int position)
	{
		super.setCaretPosition(position);
		// Calcula las coordenadas visuales.
		if (!recalculate)
		{
			IScrollPane sp=getScrollPane();
			int scroll_x=0,scroll_y=0;
			// Calcula.
			if (caret_position>0)
			{
				// Línea.
				if (caret_position<text.length())
				{
					for (int y=1;y<lines.length;y+=2)
					{
						int ini=lines[y];
						int fin=ini+lines[y+1];
						//if (caret_position<(lines[y]+lines[y+1]))
						if (caret_position>=ini&&caret_position<fin)
						{
							//caret_line.y=(y-1)>>1;
							caret_line.y=(y-1)>>1;
							break;
						}
					}
				}
				else caret_line.y=lines[0]-1;
				// Columna.
				int index=lines[(caret_line.y<<1)+1];
				int count=lines[(caret_line.y<<1)+2];
				caret_line.x=caret_position-index;
				// Coordenadas.
				caret_xy.setLocation(font.substringWidth(text,index,caret_line.x),caret_line.y*font.getLineHeight());
				// Calcula el scroll.
				if (sp!=null)
				{
					// Calcula el scroll en X.
					int width=parent.bounds.width-4;
					int limit_x=font.substringWidth(text,index,count);
					if (limit_x>width)
					{
						scroll_x=Math.max(0,caret_xy.x-(width>>1));
						if (limit_x-scroll_x<=width) scroll_x=limit_x-width;
					}
					// Calcula el scroll en Y.
					int height=parent.bounds.height-4;
					int limit_y=lines[0]*font.getLineHeight();
					if (limit_y>height)
					{
						scroll_y=Math.max(0,caret_xy.y+(font.getLineHeight()>>1)-(height>>1));
						if (limit_y-scroll_y<=height) scroll_y=limit_y-height;
					}
				}
			}
			else
			{
				caret_line.setLocation(0,0);
				caret_xy.setLocation(0,0);
			}
			// Ajusta el scroll.
			if (sp!=null) sp.scrollTo(scroll_x,scroll_y);
		}
	}

	/**
	 * Recalcula el tamaño.
	 */
	protected void recalculate()
	{
		recalculate=true;
		if (font==null) return;
		// Precacula las líneas.
		int width=0,height=0;
		if (text.length()>0)
		{
			lines=font.calculateLines(text,wrap?min_size.x-4:Short.MAX_VALUE);
			// Tamaño total del texto.
			for (int i=1;i<lines.length;i+=2) width=Math.max(width,font.substringWidth(text,lines[i],lines[i+1]));
			height=((font.getLineHeight()*lines[0])-font.getLeading());
		}
		else lines=new short[1];
    	// Tamaño del componente.
		setSize(width+4,height+4);
    	// Actualiza el cursor.
    	recalculate=false;
    	setCaretPosition(caret_position);
		repaint();
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ITEXTAREA;
	}

	public void processSkinProperties()
	{
		super.processSkinProperties();
		//
		int id=getSkinId();
		wrap=skin.getBoolean(id,Skin.PROPERTY_ITEXTAREA_WRAP);
		pack=skin.getBoolean(id,Skin.PROPERTY_ITEXTAREA_PACK);
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		g.translate(2,2);
		// Recalcula las líneas.
		if (recalculate) recalculate();
		// Pinta el texto.
		font.drawString(g,text,lines,bounds.width-4,bounds.height-4,enabled?Graphics.LEFT:halign,enabled?Graphics.TOP:valign);
		// Pinta el cursor.
		if (isCaretVisible()) paintCaret(g,caret_xy.x,caret_xy.y,getCharAtCaret());
		//
		g.translate(-2,-2);
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processKeyEvent(int type,int code,int action,char character)
	{
		if (type==Event.KEY_PRESSED&&character==0&&!recalculate)
		{
			// Movimiento del cursor.
			if (action==ApplicationCanvas.UP&&caret_line.y>0)
			{
				int index_up=lines[((caret_line.y-1)<<1)+1];
				int count_up=lines[((caret_line.y-1)<<1)+2];
				//
				setCaretPosition(index_up+Math.min(caret_line.x,count_up-1));
			}
			else if (action==ApplicationCanvas.DOWN&&caret_line.y<(lines[0]-1))
			{
				int index_down=lines[((caret_line.y+1)<<1)+1];
				int count_down=lines[((caret_line.y+1)<<1)+2];
				//
				setCaretPosition(index_down+Math.min(caret_line.x,count_down-1));
			}
		}
		//
		super.processKeyEvent(type,code,action,character);
	}
}