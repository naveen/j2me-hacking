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
package com.java4ever.apime.ui.event;

import com.java4ever.apime.ui.*;

/**
 * Listener de componente.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public interface ComponentListener
{
	/**
	 * Invocado cuando un componente se hace invisible.
	 *
	 * @param component Componente que ha efectuado el evento
	 *
	 * @see IComponent
	 */
	public void componentHidden(IComponent component);

	/**
	 * Invocado cuando un componente cambia de posición.
	 *
	 * @param component Componente que ha efectuado el evento
	 *
	 * @see IComponent
	 */
	public void componentMoved(IComponent component);

	/**
	 * Invocado cuando un componente cambia su tamaño.
	 *
	 * @param component Componente que ha efectuado el evento
	 *
	 * @see IComponent
	 */
	public void componentResized(IComponent component);

	/**
	 * Invocado cuando un componente se hace visible.
	 *
	 * @param component Componente que ha efectuado el evento
	 *
	 * @see IComponent
	 */
	public void componentShown(IComponent component);
}