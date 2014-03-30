/*
 * Fire (Flexible Interface Rendering Engine) is a set of graphics widgets for creating GUIs for j2me applications. 
 * Copyright (C) 2006  Bluebird co (www.bluebird.gr)
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

/*
 * Created on May 24, 2006
 */
package gr.bluevibe.fire.util;

import gr.bluevibe.fire.components.Component;

import javax.microedition.lcdui.Displayable;

/**
 * A Component thats acts as a panel should implement this interface to intercept events fired by its components.
 * @see gr.bluevibe.fire.components.Panel
 * @author padeler
 *
 */
public interface ComponentListener
{
	/**
	 * A components that requires (Validation) redrawing can request it from its container using this method. 
	 * @param c the issuer of the Validate event
	 */
	public void internalValidateEvent(Component c);
	/**
	 * A component can request the display of a Displayable from the container using this method.
	 * @param c the Displayable to be set current.
	 */
	public void setCurrent(Displayable c);
	/**
	 * A component can request that is container will be made current, using this method.
	 */
	public void setContainerCurrent();
}
