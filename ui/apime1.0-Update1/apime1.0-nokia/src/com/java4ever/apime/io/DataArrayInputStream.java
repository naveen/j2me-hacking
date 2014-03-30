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
package com.java4ever.apime.io;

import java.io.*;

/**
 * Stream de lectura de un array de bytes.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class DataArrayInputStream extends DataInputStream
{
	/**
	 * Array de bytes.
	 */
	protected byte data[];
	
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el stream para un array de bytes.
	 *
	 * @param data Array de byte
	 */
	public DataArrayInputStream(byte data[])
	{
		super(new ByteArrayInputStream(data));
		this.data=data;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el array de bytes con el que se ha creado el stream.
	 */
	public byte[] getData()
	{
		return data;
	}
	
	/**
	 * Devuelve un array con los datos que quedan por leer.
	 *
	 * @return Si no se ha leído nada devuelve el array de bytes con el que se ha creado el stream
	 */
	public byte[] readData() throws IOException
	{
		return readData(Integer.MAX_VALUE);
	}
	
	/**
	 * Devuelve un array con los datos leídos.
	 *
	 * @param limit Número máximo de bytes a leer
	 *
	 * @return Si no se ha leído nada y 'limit>=data.length' devuelve el array de bytes con el que se ha creado el stream
	 */
	public byte[] readData(int limit) throws IOException
	{
		byte temp[]=data;
		//
		limit=Math.min(limit,available());
		if (limit<data.length) read(temp=new byte[limit]);
			else skip(temp.length);
		//
		return temp;
	}
}