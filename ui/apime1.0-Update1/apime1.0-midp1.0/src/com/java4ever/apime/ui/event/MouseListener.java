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
 * Listener de ratón.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public interface MouseListener
{
	/**
	 * Invocado cuando el ratón entra en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mouseEntered(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón sale de un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente (0)
	 * @param y Coordenada Y relativa al componente (0)
	 *
	 * @see IComponent
	 */
	public void mouseExited(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón es pulsado en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mousePressed(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón es soltado en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mouseReleased(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón es pulsado y luego soltado en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mouseClicked(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón es movido en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mouseMoved(IComponent component,int x,int y);

	/**
	 * Invocado cuando el ratón es pulsado y movido en un componente.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente
	 *
	 * @see IComponent
	 */
	public void mouseDragged(IComponent component,int x,int y);
}