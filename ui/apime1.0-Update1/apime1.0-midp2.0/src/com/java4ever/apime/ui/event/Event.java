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

/**
 * Tipos de evento.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public interface Event
{
	public static final int ACTION_PERFORMED    = 0;
	//
	public static final int COMPONENT_HIDDEN    = 1;
	public static final int COMPONENT_MOVED     = 2;
	public static final int COMPONENT_RESIZED   = 3;
	public static final int COMPONENT_SHOWN     = 4;
	//
	public static final int COMPONENT_ADDED     = 5;
	public static final int COMPONENT_REMOVED   = 6;
	//
	public static final int FOCUS_GAINED        = 7;
	public static final int FOCUS_LOST          = 8;
	//
	public static final int KEY_PRESSED         = 9;
	public static final int KEY_RELEASED        = 10;
	public static final int KEY_TYPED			= 11;
	//
	public static final int MOUSE_ENTERED		= 12;
	public static final int MOUSE_EXITED     	= 13;
	public static final int MOUSE_PRESSED     	= 14;
	public static final int MOUSE_RELEASED    	= 15;
	public static final int MOUSE_CLICKED    	= 16;
	public static final int MOUSE_MOVED     	= 17;
	public static final int MOUSE_DRAGGED		= 18;
	//
	public static final int TEXT_VALUE_CHANGED	= 19;
}