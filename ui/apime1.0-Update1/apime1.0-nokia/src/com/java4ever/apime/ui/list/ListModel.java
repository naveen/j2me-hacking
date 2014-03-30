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
package com.java4ever.apime.ui.list;

import java.util.*;

import com.java4ever.apime.ui.*;
import com.java4ever.apime.ui.event.*;

/**
 * Modelo de datos para una lista.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 *
 * @see IList
 */
public class ListModel
{
	/**
	 * Vector de datos.
	 */
	protected Vector data;

	/**
	 * Listener de cambio de datos.
	 *
	 * @see ActionListener
	 */
	ActionListener action_listener;

	/*************************************************************************/
	/*************************************************************************/
	
    public ListModel()
    {
    	data=new Vector();
    }

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Añade un dato al final de la lista.
	 *
	 * @param element Dato a añadir
	 */
	public void addElement(Object element)
	{
		insertElementAt(element,data.size());
	}

	/**
	 * Inserta un dato en la posición indicada de la lista.
	 *
	 * @param element Dato a insertar
	 * @param index Posición donde insertarlo (lo inserta al final si index<0||index>número items)
	 */
	public void insertElementAt(Object element,int index)
	{
		data.insertElementAt(element,index);
		//
		processActionEvent(element);
	}
	
	/**
	 * Elimina un dato de la lista.
	 *
	 * @param element Dato a eliminar
	 */
	public void removeElement(Object element)
	{
		removeElementAt(data.indexOf(element));
	}

	/**
	 * Elimina un dato de la lista.
	 *
	 * @param index Posición del dato a eliminar
	 */
	public void removeElementAt(int index)
	{
		Object element=elementAt(index);
		data.removeElementAt(index);
		//
		processActionEvent(element);
	}
	
	/**
	 * Elimina todos los datos de la lista.
	 */
	public void removeAllElements()
	{
		data.removeAllElements();
		//
		processActionEvent(null);
	}
	
	/**
	 * Devuelve un dato de la lista.
	 *
	 * @param index Posición del dato
	 *
	 * @return Dato
	 */
	public Object elementAt(int index)
	{
		return data.elementAt(index);
	}

	/**
	 * Devuelve el número de datos de la lista.
	 */
	public int size()
	{
		return data.size();
	}

	/**
	 * Asigna el listener de acciones.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see ActionListener
	 */
	public void setActionListener(ActionListener listener)
	{
		action_listener=listener;
	}

	/**
	 * Devuelve el listener de acciones o 'null' si no tiene.
	 *
	 * @see ActionListener
	 */
	public ActionListener getActionListener()
	{
		return action_listener;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Procesa un evento de acción.
	 *
	 * @param data Elemento que ha efectuado la acción (pueder ser 'null')
	 */
	protected void processActionEvent(Object data)
	{
		if (action_listener!=null) action_listener.actionPerformed(this,data);
	}
}