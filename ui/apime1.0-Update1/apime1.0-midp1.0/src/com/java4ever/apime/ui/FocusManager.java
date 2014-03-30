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

import com.java4ever.apime.ui.event.*;

/**
 * Gestor de foco.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class FocusManager
{
	/**
	 * Instancia global.
	 */
	public static final FocusManager instance=new FocusManager();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Componente que tiene el foco o 'null' si no hay ninguno.
	 *
	 * @see IComponent
	 */
	IComponent focus;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el gestor.
	 */
	private FocusManager()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Transfiere el foco a un componente.
	 *
	 * @param component Componente al que pasar el foco (puede ser 'null')
	 *
	 * @return True si ha podido transferir o mantener el foco
	 *
	 * @see IComponent
	 */
	boolean transferFocus(IComponent component)
	{
		if (component==null||component.enabled)
		{
			if (component!=focus)
			{
				if (focus!=null) focus.processFocusEvent(Event.FOCUS_LOST);
				focus=component;
				if (focus!=null) focus.processFocusEvent(Event.FOCUS_GAINED);
			}
			CursorManager.instance.setEnabled(focus==null||focus.cursor_enabled==IComponent.CURSOR_ENABLED_ALWAYS);
			return true;
		}
		else return (component==focus);
	}
}