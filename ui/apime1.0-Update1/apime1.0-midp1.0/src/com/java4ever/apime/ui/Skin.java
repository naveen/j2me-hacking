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

import java.io.*;

/**
 * Skin.
 */
public class Skin
{
	// Keys.
	public static final int KEY_DEFAULTLISTCELLRENDERER = 0;
	public static final int KEY_IBUTTON                 = 1;
	public static final int KEY_ICHECKBOX               = 2;
	public static final int KEY_ICOMBOBOX               = 3;
	public static final int KEY_ICOMPONENT              = 4;
	public static final int KEY_ICONTAINER              = 5;
	public static final int KEY_ILABEL                  = 6;
	public static final int KEY_ILIST                   = 7;
	public static final int KEY_IPROGRESSBAR            = 8;
	public static final int KEY_IROOT                   = 9;
	public static final int KEY_ISCROLLBAR              = 10;
	public static final int KEY_ISCROLLBAR$SCROLLBUTTON = 11;
	public static final int KEY_ISCROLLPANE             = 12;
	public static final int KEY_ISCROLLPANE$VIEWPORT    = 13;
	public static final int KEY_ITEXTAREA               = 14;
	public static final int KEY_ITEXTCOMPONENT          = 15;
	public static final int KEY_ITEXTFIELD              = 16;
	public static final int KEY_ITOOLTIP                = 17;
	public static final int KEY_TOOLTIPMANAGER          = 18;
	// Properties.
	public static final int PROPERTY_IBUTTON_DISABLED               = 0;
	public static final int PROPERTY_IBUTTON_NORMAL                 = 1;
	public static final int PROPERTY_IBUTTON_PRESSED                = 2;
	public static final int PROPERTY_IBUTTON_ROLLOVER               = 3;
	public static final int PROPERTY_IBUTTON_SELECTED               = 4;
	public static final int PROPERTY_ICOMPONENT_BACKGROUND          = 5;
	public static final int PROPERTY_ICOMPONENT_BORDER              = 6;
	public static final int PROPERTY_ICOMPONENT_CURSOR              = 7;
	public static final int PROPERTY_ICOMPONENT_CURSOR_ENABLED      = 8;
	public static final int PROPERTY_ICOMPONENT_ENABLED             = 9;
	public static final int PROPERTY_ICOMPONENT_FOREGROUND          = 10;
	public static final int PROPERTY_ICOMPONENT_PARENT              = 11;
	public static final int PROPERTY_ICOMPONENT_VISIBLE             = 12;
	public static final int PROPERTY_IPROGRESSBAR_PROGRESS_COLOR    = 13;
	public static final int PROPERTY_ISCROLLBAR_AUTOSCROLL          = 14;
	public static final int PROPERTY_ISCROLLBAR_THICK               = 15;
	public static final int PROPERTY_ITEXTAREA_PACK                 = 16;
	public static final int PROPERTY_ITEXTAREA_WRAP                 = 17;
	public static final int PROPERTY_ITEXTFIELD_CARET_BLINK         = 18;
	public static final int PROPERTY_ITEXTFIELD_CARET_COLOR         = 19;
	public static final int PROPERTY_TOOLTIPMANAGER_DISMISS         = 20;
	public static final int PROPERTY_TOOLTIPMANAGER_INITIAL         = 21;
	public static final int PROPERTY_TOOLTIPMANAGER_ENABLED         = 22;

	/*************************************************************************/

	private String name;
	private int table[][];
	private int values_int[];
	private String values_string[];

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea un skin.
	 *
	 * @param data Datos del skin
	 */
	public Skin(byte data[]) throws IOException
	{
		DataInputStream dais=new DataInputStream(new ByteArrayInputStream(data));
		// Nombre.
		name=dais.readUTF();
		// Tabla de keys/data.
		table=new int[dais.readUnsignedShort()][];
		for (int i=0;i<table.length;i++)
		{
			table[i]=new int[dais.readUnsignedShort()];
			for (int j=0;j<table[i].length;j++) table[i][j]=dais.readInt();
		}
		// Valores de tipo 'int'.
		values_int=new int[dais.readUnsignedByte()];
		for (int i=0;i<values_int.length;i++) values_int[i]=dais.readInt();
		// Valores de tipo 'float'.
		dais.readUnsignedByte();
		// Valores de tipo 'string'.
		values_string=new String[dais.readUnsignedByte()];
		for (int i=0;i<values_string.length;i++) values_string[i]=dais.readUTF();
		// Valores de tipo 'binary'.
		dais.readUnsignedByte();
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el nombre del skin.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Devuelve el valor de una propiedad con valor booleano.
	 *
	 * @param key Clave
	 * @param property Propiedad
	 *
	 * @return Valor
	 */
	public boolean getBoolean(int key,int property)
	{
		return (getInt(key,property)!=0);
	}

	/**
	 * Devuelve el valor de una propiedad con valor entero.
	 *
	 * @param key Clave
	 * @param property Propiedad
	 *
	 * @return Valor
	 */
	public int getInt(int key,int property)
	{
		return values_int[searchValueIndex(key,property)];
	}

	/**
	 * Devuelve el valor de una propiedad.
	 *
	 * @param key Clave
	 * @param property Propiedad
	 *
	 * @return Valor
	 */
	public String getString(int key,int property)
	{
		return values_string[searchValueIndex(key,property)];
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Busca el índice de un valor.
	 *
	 * @param key Clave
	 * @param property Propiedad
	 */
	private int searchValueIndex(int key,int property)
	{
		if (key>=0&&key<table.length)
		{
			int k=key;
			do
			{
				int data[]=table[k];
				k=-1;
				for (int i=0;i<data.length;i++)
				{
					int d=data[i];
					int p=(d&0xffff);
					if (p==property) return (d>>>16);
						else if (p==PROPERTY_ICOMPONENT_PARENT) k=(d>>>16);
				}
			}
			while (k!=-1);
		}
		throw new IllegalArgumentException("key="+key+" / property="+property);
	}
}