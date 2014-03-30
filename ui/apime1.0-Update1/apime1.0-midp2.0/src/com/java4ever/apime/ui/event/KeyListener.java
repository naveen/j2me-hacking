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
 * Listener de teclado.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public interface KeyListener
{
	/**
	 * Invocado cuando una tecla es pulsada.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param code C�digo de la tecla
	 * @param action Acci�n asociada al c�digo de la tecla
	 * @param character Caracter asociado al c�digo de la tecla (0 si no tiene)
	 *
	 * @see IComponent
	 */
	public void keyPressed(IComponent component,int code,int action,char character);

	/**
	 * Invocado cuando una tecla es soltada.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param code C�digo de la tecla
	 * @param action Acci�n asociada al c�digo de la tecla
	 * @param character Caracter asociado al c�digo de la tecla (0 si no tiene)
	 *
	 * @see IComponent
	 */
	public void keyReleased(IComponent component,int code,int action,char character);

	/**
	 * Invocado cuando una tecla es pulsada y luego soltada.
	 *
	 * @param component Componente que ha efectuado el evento
	 * @param code C�digo de la tecla
	 * @param action Acci�n asociada al c�digo de la tecla
	 * @param character Caracter asociado al c�digo de la tecla (0 si no tiene)
	 *
	 * @see IComponent
	 */
	public void keyTyped(IComponent component,int code,int action,char character);
}