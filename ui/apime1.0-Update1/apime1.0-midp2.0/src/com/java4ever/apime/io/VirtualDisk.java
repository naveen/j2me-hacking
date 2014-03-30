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
import java.util.*;
import javax.microedition.rms.*;

/**
 * Disco virtual para almacenar ficheros.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class VirtualDisk
{
	/**
	 * Instancia global ('null' por defecto).
	 */
	public static VirtualDisk instance;

	/*************************************************************************/
	/*************************************************************************/

	private RecordStore rs;
	private Hashtable table;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el disco virtual (Usado para optimizar el tamaño al hacer shrink).
	 */
	private VirtualDisk()
	{
	}

	/**
	 * Crea el disco virtual.
	 *
	 * @param name Nombre interno
	 */
	private VirtualDisk(String name) throws IOException
	{
		try
		{
			rs=RecordStore.openRecordStore(name,true);
			// Crea la tabla de ficheros (si no existe).
			if (rs.getNumRecords()==0) rs.addRecord(new byte[]{0,0},0,2);
			// Lee la tabla de ficheros.
			table=new Hashtable();
			loadFileTable();
		}
		catch (Exception ex)
		{
			throw new IOException("Error creating VirtualDisk");
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el disco virtual y lo asigna a la variable global.
	 *
	 * @see #instance
	 */
	public static void create() throws IOException
	{
		if (instance==null) instance=new VirtualDisk("apime");
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve la lista de ficheros.
	 */
	public Enumeration dir()
	{
		return table.keys();
	}

	/**
	 * Devuelve si existe un fichero.
	 *
	 * @param file Nombre del fichero
	 */
	public boolean exists(String file)
	{
		return (getFileId(file)!=-1);
	}

	/**
	 * Carga un fichero.
	 *
	 * @param file Nombre del fichero
	 *
	 * @return Array de bytes con los datos del fichero
	 */
	public byte[] load(String file) throws IOException
	{
		try
		{
			return rs.getRecord(getFileId(file));
		}
		catch (Exception ex)
		{
			throw new IOException("Error loading \""+file+"\"");
		}
	}

	/**
	 * Guarda un fichero.
	 *
	 * @param file Nombre del fichero
	 * @param data Array de bytes con los datos del fichero
	 */
	public void save(String file,byte data[]) throws IOException
	{
		try
		{
	    	int id=getFileId(file);
	    	if (id==-1)
	    	{
	    		id=rs.addRecord(data,0,data.length);
	    		table.put(file,new Integer(id));
	    		saveFileTable();
	    	}
	    	else rs.setRecord(id,data,0,data.length);
	    }
		catch (Exception ex)
		{
			throw new IOException("Error saving \""+file+"\"");
		}
	}

	/**
	 * Borra un fichero.
	 *
	 * @param file Nombre del fichero
	 */
	public void delete(String file) throws IOException
	{
		try
		{
			int id=getFileId(file);
			if (id!=-1)
			{
				rs.deleteRecord(getFileId(file));
				table.remove(file);
				saveFileTable();
			}
			else throw new IOException("Error deleting \""+file+"\"");
	    }
		catch (Exception ex)
		{
			throw new IOException("Error deleting \""+file+"\"");
		}
	}

	/**
	 * Borra todos los ficheros.
	 */
	public void deleteAll() throws IOException
	{
		try
		{
			for (Enumeration e=table.elements();e.hasMoreElements();)
				rs.deleteRecord(((Integer)e.nextElement()).intValue());
			table.clear();
			saveFileTable();
	    }
		catch (Exception ex)
		{
			throw new IOException("Error deleting files.");
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	private int getFileId(String file)
	{
		Object o=table.get(file);
		return (o!=null?((Integer)o).intValue():-1);
	}

	private void loadFileTable() throws IOException,RecordStoreException
	{
		table.clear();
		DataArrayInputStream dais=new DataArrayInputStream(rs.getRecord(1));
		int size=dais.readUnsignedShort();
		for (int i=0;i<size;i++) table.put(dais.readUTF(),new Integer(dais.readInt()));
		dais.close();
	}

	private void saveFileTable() throws IOException,RecordStoreException
	{
		DataArrayOutputStream daos=new DataArrayOutputStream();
		daos.writeShort(table.size());
		for (Enumeration e=table.keys();e.hasMoreElements();)
		{
			String file=(String)e.nextElement();
			daos.writeUTF(file);
			daos.writeInt(getFileId(file));
		}
		rs.setRecord(1,daos.toByteArray(),0,daos.size());
		daos.close();
	}
}