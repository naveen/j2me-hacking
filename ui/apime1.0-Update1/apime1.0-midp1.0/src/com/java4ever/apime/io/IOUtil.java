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
import javax.microedition.io.*;

/**
 * Utilidades I/O.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IOUtil
{
	/**
	 * Tamaño del buffer de lectura (por defecto 4096).
	 */
	public static int BUFFER_SIZE	= 4096;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve la extensión de un fichero.
	 *
	 * @param file Nombre del fichero
	 *
	 * @return Extensión o cadena vacía si no tiene
	 */
	public static String getFileExtension(String file)
	{
		int index=file.lastIndexOf('.');
		return (index!=-1?file.substring(index+1):"");
	}

	/**
	 * Abre un stream de lectura.
	 *
	 * @param file Nombre del fichero (Resource/VirtualDisk o Connection)
	 *
	 * @see VirtualDisk
	 *
	 * @return Stream de lectura
	 */
	public static InputStream openInputStream(String file) throws IOException
	{
		try
		{
			// Resource o VirtualDisk.
			InputStream is=null;
			if (file.charAt(0)=='/')
			{
				is=file.getClass().getResourceAsStream(file);
				if (is==null) is=new DataArrayInputStream(VirtualDisk.instance.load(file));
			}
			// Connection.
			else is=Connector.openInputStream(file);
			// Devuelve el stream de lectura.
			return is;
		}
		catch (Exception ex)
		{
			throw new IOException("File \""+file+"\" not found");
		}
	}

	/**
	 * Cierra un stream de lectura.
	 *
	 * @param is Stream de lectura
	 */
	public static void closeInputStream(InputStream is)
	{
		try
		{
			is.close();
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Carga un fichero.
	 *
	 * @param file Nombre del fichero
	 *
	 * @return Array de bytes con los datos del fichero
	 */
	public static byte[] loadFile(String file) throws IOException
	{
		return loadFile(openInputStream(file));
	}

	/**
	 * Carga un fichero (optimizado para DataArrayInputStream).
	 *
	 * @param is Stream de lectura (lo cierra al finalizar)
	 *
	 * @return Array de bytes con los datos del fichero
	 *
	 * @see DataArrayInputStream
	 */
	public static byte[] loadFile(InputStream is) throws IOException
	{
		try
		{
			return read(is,Integer.MAX_VALUE);
		}
		finally
		{
			closeInputStream(is);
		}
	}

	/**
	 * Lee datos de un stream de lectura (optimizado para DataArrayInputStream).
	 *
	 * @param is Stream de lectura
	 * @param limit Número máximo de bytes
	 *
	 * @return Array de bytes
	 *
	 * @see DataArrayInputStream
	 */
	public static byte[] read(InputStream is,int limit) throws IOException
	{
		if (!(is instanceof DataArrayInputStream))
		{
			byte buffer[]=new byte[BUFFER_SIZE];
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			for (int l=-1;(l=is.read(buffer,0,Math.min(limit-baos.size(),buffer.length)))>0;baos.write(buffer,0,l));
			baos.flush();
			return baos.toByteArray();
		}
		else return ((DataArrayInputStream)is).readData(limit);
	}
}