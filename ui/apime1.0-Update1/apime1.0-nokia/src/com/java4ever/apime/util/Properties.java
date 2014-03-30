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

import java.io.*;
import java.util.*;

import com.java4ever.apime.io.*;

/**
 * Tabla de propiedades (permite caracteres Unicode en los valores).
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class Properties extends Hashtable
{
	/**
	 * Crea la tabla de propiedades.
	 */
	public Properties()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el valor de una propiedad con valor booleano (0/1).
	 *
	 * @param property Nombre de la propiedad
	 *
	 * @return Valor
	 */
	public boolean getBoolean(String property)
	{
		return (getInt(property)>0);
	}

	/**
	 * Devuelve el valor de una propiedad con valor entero.
	 *
	 * @param property Nombre de la propiedad
	 *
	 * @return Valor
	 */
	public int getInt(String property)
	{
		return getInt(property,10);
	}

	/**
	 * Devuelve el valor de una propiedad con valor entero hexadecimal.
	 *
	 * @param property Nombre de la propiedad
	 *
	 * @return Valor
	 */
	public int getHex(String property)
	{
		return getInt(property,16);
	}

	/**
	 * Devuelve el valor de una propiedad con valor entero.
	 *
	 * @param property Nombre de la propiedad
	 * @param radix Base del valor entero
	 *
	 * @return Valor
	 */
	public int getInt(String property,int radix)
	{
		return (int)Long.parseLong(getString(property),radix);
	}

	/**
	 * Devuelve el valor de una propiedad.
	 *
	 * @param property Nombre de la propiedad
	 *
	 * @return Valor o 'null' si no existe
	 */
	public String getString(String property)
	{
		return (containsKey(property)?(String)get(property):null);
	}

	/**
	 * Devuelve los valores de una propiedad que están separados por ','.
	 *
	 * @param property Nombre de la propiedad
	 *
	 * @return Array con los valores
	 */
	public String[] getValues(String property)
	{
		return TextUtil.split(getString(property),',');
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Carga las propiedades de un stream de lectura.
	 *
	 * @param is Stream de lectura
	 */
	public void load(InputStream is) throws IOException
	{
		String lines[]=TextUtil.split(new String(IOUtil.loadFile(is)),'\n');
		for (int i=0;i<lines.length;i++)
		{
			String line=lines[i].trim();
			if (!line.startsWith("#"))
			{
				int index=line.indexOf("=");
				if (index!=-1) put(line.substring(0,index).trim(),TextUtil.replaceUnicode(line.substring(index+1).trim()));
			}
		}
	}
}