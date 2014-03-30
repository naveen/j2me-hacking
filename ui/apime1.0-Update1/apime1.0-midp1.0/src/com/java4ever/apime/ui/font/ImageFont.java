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
 * Fuente generada a partir de una imagen.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ImageFont extends IFont
{
	private Image image;
	private int width,height,leading;
	private byte widths[];
	private char init,end;
	private short positions[];

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la fuente (monospaced).
	 *
	 * @param image Imagen
	 * @param width Ancho de cada caracter
	 * @param height Alto de cada caracter
	 * @param leading Espacio entre líneas
	 * @param init Código del primer caracter
	 * @param end Código del último caracter
	 */
	public ImageFont(Image image,int width,int height,int leading,char init,char end)
	{
		this.width=(byte)width;
		init(image,height,leading,init,end);
	}

	/**
	 * Crea la fuente.
	 *
	 * @param image Imagen
	 * @param widths Array con el ancho de cada caracter
	 * @param height Alto de cada caracter
	 * @param leading Espacio entre líneas
	 * @param init Código del primer caracter
	 * @param end Código del último caracter
	 */
	public ImageFont(Image image,byte widths[],int height,int leading,char init,char end)
	{
		this.widths=widths;
		init(image,height,leading,init,end);
	}

	private void init(Image image,int height,int leading,char init,char end)
	{
		// Características de la fuente.
		this.image=image;
		this.height=height;
		this.leading=leading;
		this.init=init;
		this.end=end;
		// Posición de cada caracter dentro de la imagen.
		positions=new short[end-init+1];
		for (char i=init,x=0,y=0;i<=end;i++)
		{
			int cw=charWidth(i);
			if (x+cw>image.getWidth())
			{
				x=0;
				y+=height+1;
			}
			positions[i-init]=(short)((y<<8)|x);
			x+=cw;
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	public int substringWidth(String s,int offset,int length)
	{
		if (widths==null) return (length*width);
			else return super.substringWidth(s,offset,length);
	}

	public int charWidth(char c)
	{
		if (widths==null) return width;
			else return ((c>=init&&c<=end)?widths[c-init]:widths[0]);
	}

	public int getHeight()
	{
		return height;
	}

	public int getLeading()
	{
		return leading;
	}

	public void drawSubstring(Graphics g,String s,int offset,int length,int x,int y,int anchor)
	{
		if (length==0) return;
		// Almacena el clipping actual.
		int clip_x=g.getClipX();
		int clip_y=g.getClipY();
		int clip_width=g.getClipWidth();
		int clip_height=g.getClipHeight();
		// Ajusta las coordenadas y comprueba si hay caracteres dentro de la zona de clipping.
		int clip_limit_y=clip_y+clip_height;
		if ((anchor&Graphics.TOP)==0) y-=(((anchor&Graphics.BOTTOM)!=0)?(height):(height>>1));
		if (y>clip_limit_y||(y+height)<clip_y) return;
		//
		int clip_limit_x=clip_x+clip_width;
		int text_width=substringWidth(s,offset,length);
		if ((anchor&Graphics.LEFT)==0) x-=(((anchor&Graphics.RIGHT)!=0)?(text_width):(text_width>>1));
		if (x>clip_limit_x||(x+text_width)<clip_x) return;
		// Recorre la cadena.
		for (int i=0;i<length;i++)
		{
			char c=s.charAt(i+offset);
			int width=charWidth(c);
			// Chequeo de clipping para cada caracter.
			if (x+width>=clip_x)
			{
				if (x<clip_limit_x)
				{
					if (c>=init&&c<=end)
					{
						int pos=positions[c-init];
						g.clipRect(x,y,width,height);
						g.drawImage(image,x-(pos&0xff),y-(pos>>>8),Graphics.LEFT|Graphics.TOP);
						g.setClip(clip_x,clip_y,clip_width,clip_height);
					}
				}
				else break;
			}
			x+=width;
		}
	}
}