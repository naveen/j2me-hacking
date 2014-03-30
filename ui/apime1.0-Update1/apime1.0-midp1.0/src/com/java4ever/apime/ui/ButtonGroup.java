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

/**
 * Agrupación de botones.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 *
 * @see IButton
 */
public class ButtonGroup
{
	/**
	 * Botón seleccionado.
	 *
	 * @see IButton
	 */
	IButton selected;

	// Lista de botones.
	private Vector buttons=new Vector();

	/*************************************************************************/
	/*************************************************************************/

	public ButtonGroup()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Añade un botón al grupo.
	 *
	 * @param button Botón
	 *
	 * @see IButton
	 */
	public void add(IButton button)
	{
		button.group=this;
    	buttons.addElement(button);
    	//
    	if (selected==null&&button.selected) selected=button;
    		else if (button.selected) button.setSelected(false);
	}

	/**
	 * Elimina un botón del grupo.
	 *
	 * @param button Botón
	 *
	 * @see IButton
	 */
	public void remove(IButton button)
	{
		button.group=null;
		buttons.removeElement(button);
		//
		if (selected==button) selected=null;
	}

	/**
	 * Devuelve el botón seleccionado o 'null' si no hay ninguno.
	 *
	 * @see IButton
	 */
	public IButton getSelected()
	{
		return selected;
	}

	/**
	 * Actualiza el estado de los botones.
	 *
	 * @param button Botón que realiza el cambio (se activa)
	 *
	 * @see IButton
	 */
	void update(IButton button)
	{
		selected=button;
		//
		for (int i=0;i<buttons.size();i++)
		{
			IButton b=(IButton)buttons.elementAt(i);
			if (b!=button) b.setSelected(false);
		}
	}
}