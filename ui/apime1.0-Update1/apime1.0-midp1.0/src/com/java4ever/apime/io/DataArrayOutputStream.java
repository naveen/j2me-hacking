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
 * Stream de escritura en un array de bytes.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class DataArrayOutputStream extends DataOutputStream
{
	/**
	 * Crea el stream.
	 */
	public DataArrayOutputStream()
	{
		super(new ByteArrayOutputStream());
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el número de bytes escritos.
	 */
	public int size()
	{
		return (((ByteArrayOutputStream)out).size());
	}
		
	/**
	 * Devuelve el array de bytes.
	 *
	 * @return Array de bytes leídos con el stream
	 */
	public byte[] toByteArray()
	{
		return (((ByteArrayOutputStream)out).toByteArray());
	}
}
