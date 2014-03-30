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
package com.java4ever.apime.ui.font;

import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.*;

/**
 * Fuente nativa.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class NativeFont extends IFont
{
	private Font font;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la fuente.
	 *
	 * @param face Tipo
	 * @param style Estilo
	 * @param size Tamaño
	 *
	 * @see Font
	 */
	public NativeFont(int face,int style,int size)
	{
		font=Font.getFont(face,style,size);
	}

	/*************************************************************************/
	/*************************************************************************/

	public int substringWidth(String s,int offset,int length)
	{
		return font.substringWidth(s,offset,length);
	}

	public int charWidth(char c)
	{
		return font.charWidth(c);
	}

	public int getHeight()
	{
		return font.getHeight();
	}

	public int getLeading()
	{
		return 0;
	}

	public void drawSubstring(Graphics g,String s,int offset,int length,int x,int y,int anchor)
	{
		if (length>0)
		{
			g.setFont(font);
			if ((anchor&Graphics.VCENTER)!=0)
			{
				y-=(getHeight()>>1);
				anchor=(anchor&(~Graphics.VCENTER))|Graphics.TOP;
			}
			g.drawSubstring(s,offset,length,x,y,anchor);
		}
	}
}