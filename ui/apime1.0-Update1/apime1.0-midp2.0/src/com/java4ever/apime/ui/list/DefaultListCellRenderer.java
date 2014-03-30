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

import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.*;

/**
 * Renderer por defecto para cada celda de una lista.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 *
 * @see IList
 */
public class DefaultListCellRenderer extends IButton implements ListCellRenderer
{
    public DefaultListCellRenderer()
    {
		super("");
		setSize(0,16);
	    setAlignment(Graphics.LEFT,Graphics.VCENTER);
    }

	/*************************************************************************/
	/*************************************************************************/

    public IComponent getListCellRendererComponent(IList list,Object value,int index,boolean selected,boolean rollover)
    {
    	setText(value.toString());
    	this.rollover=rollover;
    	this.selected=selected;
		return this;
    }

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_DEFAULTLISTCELLRENDERER;
	}
}