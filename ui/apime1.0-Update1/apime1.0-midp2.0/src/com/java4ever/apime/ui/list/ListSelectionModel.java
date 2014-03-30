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
 * Modelo de selección para una lista.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 *
 * @see IList
 */
public class ListSelectionModel
{
	/**
	 * Modo para indicar que sólo puede haber una dato seleccionado.
	 */
	public static int SINGLE_SELECTION		= 0;

	/**
	 * Modo para indicar que puede haber varios datos seleccionados.
	 */
	public static int MULTIPLE_SELECTION	= 1;
	
	/*************************************************************************/

	/**
	 * Modo de selección.
	 *
	 * @see #SINGLE_SELECTION
	 * @see #MULTIPLE_SELECTION
	 */
	int mode;

	/**
	 * Vector de índices seleccionados.
	 */
	protected Vector indices;

	/**
	 * Listener de cambio de selección.
	 *
	 * @see ActionListener
	 */
	ActionListener action_listener;

	/*************************************************************************/
	/*************************************************************************/
	
	/**
	 * Crea el modelo de selección.
	 *
	 * @param mode Modo de selección
	 *
	 * @see #SINGLE_SELECTION
	 * @see #MULTIPLE_SELECTION
	 */
    public ListSelectionModel(int mode)
    {
    	this.mode=mode;
    	this.indices=new Vector();
    }

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el modo de selección.
	 *
	 * @param mode Modo de selección
	 *
	 * @see #SINGLE_SELECTION
	 * @see #MULTIPLE_SELECTION
	 */
	public void setSelectionMode(int mode)
	{
		this.mode=mode;
		deselectAll();
	}

	/**
	 * Devuelve el modo de selección.
	 *
	 * @see #SINGLE_SELECTION
	 * @see #MULTIPLE_SELECTION
	 */
	public int getSelectionMode()
	{
		return mode;
	}

	/**
	 * Selecciona un índice.
	 *
	 * @param index Indice a seleccionar (>=0)
	 */
	public void selectIndex(int index)
	{
		if (index>=0)
		{
			Integer i=new Integer(index);
			if (!isSelectedIndex(index))
			{
				if (mode==SINGLE_SELECTION) indices.removeAllElements();
				indices.addElement(i);
			}
			processActionEvent(i);
		}
	}

	/**
	 * Deselecciona un índice.
	 *
	 * @param index Indice a deseleccionar
	 */
	public void deselectIndex(int index)
	{
		Integer i=new Integer(index);
		if (isSelectedIndex(index)&&mode!=SINGLE_SELECTION)
			indices.removeElement(i);
		processActionEvent(i);
	}

	/**
	 * Deselecciona todos los índices.
	 */
	public void deselectAll()
	{
		indices.removeAllElements();
		processActionEvent(null);
	}

	/**
	 * Selecciona sólo un índice.
	 *
	 * @param index Indice a seleccionar
	 */
	public void setSelectedIndex(int index)
	{
		indices.removeAllElements();
		selectIndex(index);
	}

	/**
	 * Devuelve el primer índice seleccionado.
	 *
	 * @return Indice o -1 si no hay ninguno
	 */
	public int getSelectedIndex()
	{
		return (indices.size()>0?((Integer)indices.firstElement()).intValue():-1);
	}

	/**
	 * Devuelve un array con los índices seleccionados.
	 */
	public int[] getSelectedIndices()
	{
		int temp[]=new int[indices.size()];
		for (int i=0;i<indices.size();i++) temp[i]=((Integer)indices.elementAt(i)).intValue();
		return temp;
	}
	
	/**
	 * Devuelve si el índice está seleccionado.
	 */
	public boolean isSelectedIndex(int index)
	{
		return indices.contains(new Integer(index));
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
	 * @param data Indice que ha efectuado la acción (pueder ser 'null')
	 */
	protected void processActionEvent(Object data)
	{
		if (action_listener!=null) action_listener.actionPerformed(this,data);
	}
}