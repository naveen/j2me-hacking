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

import java.util.*;
import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.font.*;

/**
 * Fuente.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public abstract class IFont
{
	private static Hashtable fonts=new Hashtable();
	
	static
	{
		addFont("default",new NativeFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_MEDIUM));
		addFont("small",new NativeFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Nombre.
	 */
	private String name;

	/**
	 * Buffer pregenerado para el cálculo rápido de líneas.
	 */
	private static short lines[]=new short[256];

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el ancho de un caracter.
	 *
	 * @param c Caracter
	 */
	public abstract int charWidth(char c);

	/**
	 * Devuelve la altura de la fuente.
	 */
 	public abstract int getHeight();

 	/**
 	 * Devuelve el espacio entre líneas.
 	 */
 	public abstract int getLeading();

 	/**
 	 * Dibuja una subcadena de texto (1 línea).
 	 *
 	 * @param g Contexto gráfico
 	 * @param s Subcadena de texto
 	 * @param offset Indice del primer caracter
 	 * @param length Longitud de la subcadena
 	 * @param x Coordenada X
 	 * @param y Coordenada Y
 	 * @param anchor Punto de referencia
 	 *
 	 * @see Graphics
 	 */
	public abstract void drawSubstring(Graphics g,String s,int offset,int length,int x,int y,int anchor);

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el nombre de la fuente.
	 */
 	public String getName()
 	{
 		return name;
 	}
 	
 	/**
 	 * Devuelve el ancho en pixels de una cadena de texto.
 	 *
 	 * @param s Cadena de texto
 	 */
 	public int stringWidth(String s)
 	{
 		return substringWidth(s,0,s.length());
 	}
 	
 	/**
 	 * Devuelve el ancho en pixels de una subcadena de texto.
 	 *
 	 * @param s Subcadena de texto
 	 * @param offset Indice del primer caracter
 	 * @param length Longitud de la subcadena
 	 */
 	public int substringWidth(String s,int offset,int length)
 	{
 		int w=0;
 		for (int i=0;i<length;i++) w+=charWidth(s.charAt(offset+i));
 		return w;
 	}

	/**
	 * Devuelve la altura de una línea (height+leading).
	 *
	 * @see #getHeight()
	 * @see #getLeading()
	 */
 	public int getLineHeight()
 	{
 		return getHeight()+getLeading();
 	}
 	
 	/**
 	 * Dibuja una cadena de texto (1 línea).
 	 *
 	 * @param g Contexto gráfico
 	 * @param s Cadena de texto
 	 * @param x Coordenada X
 	 * @param y Coordenada Y
 	 * @param anchor Punto de referencia
 	 *
 	 * @see Graphics
 	 */
 	public void drawString(Graphics g,String s,int x,int y,int anchor)
 	{
 		drawSubstring(g,s,0,s.length(),x,y,anchor);
 	}

	/**
	 * Escribe un texto (puede ser multilínea).
	 *
	 * @param g Contexto gráfico
	 * @param s Texto a escribir
	 * @param width Ancho de la zona visual
	 * @param height Alto de la zona visual
	 * @param halign Alineación horizontal
	 * @param valign Alineación vertical
	 *
	 * @see IFont
	 */
	public synchronized void drawString(Graphics g,String s,int width,int height,int halign,int valign)
	{
		if (s.length()>0) drawString(g,s,calculateLines(s,width,lines),width,height,halign,valign);
	}

	/**
	 * Escribe un texto (puede ser multilínea).
	 *
	 * @param g Contexto gráfico
	 * @param s Texto a escribir
	 * @param lines Indices/Contador de las líneas
	 * @param width Ancho de la zona visual
	 * @param height Alto de la zona visual
	 * @param halign Alineación horizontal
	 * @param valign Alineación vertical
	 *
	 * @see #calculateLines
	 */
	public void drawString(Graphics g,String s,short lines[],int width,int height,int halign,int valign)
	{
		if (s.length()==0) return;
    	// Características de la fuente.
    	int line_height=getLineHeight();
   		// Calcula las coordenadas iniciales.
   		int x=0,y=0;
   		if (halign!=Graphics.LEFT)
   		{
   			x=width;
   			if (halign==Graphics.HCENTER) x>>=1;
   		}
    	if (valign!=Graphics.TOP)
    	{
    		y=height-((line_height*lines[0])-getLeading());
    		if (valign==Graphics.VCENTER) y>>=1;
    	}
    	// Dibuja las líneas.
		int clip_y1=g.getClipY()-getHeight();
		int clip_y2=g.getClipY()+g.getClipHeight();
   		for (int i=1;i<lines.length;i+=2,y+=line_height)
   		{
			if (y>=clip_y1)
			{
				if (y<clip_y2) drawSubstring(g,s,lines[i],lines[i+1],x,y,halign|Graphics.TOP);
					else break;
			}
   		}
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Trocea una cadena en líneas.
	 *
	 * @param s Cadena a trocear
	 * @param width Ancho de la zona visual
	 *
	 * @return Array de datos [0-lineas] ([1-índice][2-contador]) ([3-índice][4-contador]) ...
	 */
	public synchronized short[] calculateLines(String s,int width)
	{
		calculateLines(s,width,lines);
		//
		short temp[]=new short[(lines[0]<<1)+1];
		System.arraycopy(lines,0,temp,0,temp.length);
		return temp;
	}

	/**
	 * Trocea una cadena en líneas.
	 *
	 * @param s Cadena a trocear
	 * @param width Ancho de la zona visual
	 * @param lines Array donde almacenar los datos
	 *
	 * @return Array de datos [0=lineas] ([1=índice][2=contador]) ([3=índice][4=contador]) ...
	 */
	private short[] calculateLines(String s,int width,short lines[])
	{
		lines[0]=0;
		// Calcula las líneas.
		int word_ini=-1,line_ini=0,line_width=0;
		for (int i=0;i<s.length();i++)
		{
			char c=s.charAt(i);
			// Salto de línea.
			if (c=='\n')
			{
				if (line_width>=0)
				{
					lines[++lines[0]]=(short)line_ini;
					lines[++lines[0]]=(short)(i-line_ini);
				}
				word_ini=-1;
				line_ini=i+1;
				line_width=0;
			}
			else
			{
				// Tamaño de la letra.
				int cw=charWidth(c);
				if (line_width+cw>width)
				{
					if (word_ini>line_ini)
					{
						lines[++lines[0]]=(short)line_ini;
						lines[++lines[0]]=(short)(word_ini-line_ini);
						line_ini=word_ini;
						line_width=substringWidth(s,word_ini,i-word_ini+1);
					}
					else
					{
						lines[++lines[0]]=(short)line_ini;
						lines[++lines[0]]=(short)(i-line_ini);
						line_ini=i;
						line_width=cw;
					}
				}
				else line_width+=cw;
				// Inicio de palabra.
				if (word_ini==-1&&c!=' ') word_ini=i;
					else if (word_ini!=-1&&c==' ') word_ini=-1;
			}
		}
		if (line_width>0)
		{
			lines[++lines[0]]=(short)line_ini;
			lines[++lines[0]]=(short)(s.length()-line_ini);
		}
		// Devuelve las líneas.
		lines[0]>>=1;
		return lines;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Añade una fuente (Por defecto hay 2 predefinidas: "default" y "small").
	 *
	 * @param name Nombre
	 * @param font Fuente
	 */
	public static void addFont(String name,IFont font)
	{
		font.name=name;
		fonts.put(name,font);
	}

	/**
	 * Devuelve una fuente (Por defecto hay 2 predefinidas: "default" y "small").
	 *
	 * @param name Nombre de la fuente
	 *
	 * @return Fuente con ese nombre o la fuente por defecto si no existe
	 */
	public static IFont getFont(String name)
	{
		Object font=fonts.get(name);
		return (font!=null?(IFont)font:(IFont)fonts.get("default"));
	}

	/**
	 * Elimina una fuente.
	 *
	 * @param name Nombre de la fuente
	 */
	public static void removeFont(String name)
	{
		fonts.remove(name);
	}
}