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
package com.java4ever.apime.util;

import java.util.*;

/**
 * Utilidades de texto.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class TextUtil
{
	/**
	 * Devuelve una cadena troceada.
	 *
	 * @param s Cadena
	 * @param separator Separador
	 *
	 * @return Array con los trozos de la cadena
	 */
	public static String[] split(String s,char separator)
	{
		Vector v=new Vector();
		for (int ini=0,end=0;ini<s.length();ini=end+1)
		{
			end=s.indexOf(separator,ini);
			if (end==-1) end=s.length();
			//
			String st=s.substring(ini,end);
			if (st.length()>0) v.addElement(st);
		}
		//
		String temp[]=new String[v.size()];
		v.copyInto(temp);
		return temp;
	}
	
	/**
	 * Reemplaza las sentencias Unicode de una cadena por su correspondiente caracter.
	 *
	 * @param s Cadena a comprobar
	 *
	 * @return Cadena
	 */
	public static String replaceUnicode(String s)
	{
		for (int index=s.indexOf("\\u");index!=-1;index=s.indexOf("\\u",index+1))
		{
			try
			{
				char c=(char)(Integer.parseInt(s.substring(index+2,index+6),16)&0xffff);
				s=s.substring(0,index)+c+s.substring(index+6);
			}
			catch (Exception ex)
			{
			}
		}
		return s;
	}
}