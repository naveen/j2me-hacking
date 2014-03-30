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
 * Panel con scroll.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IScrollPane extends IContainer
{
	/**
	 * Indica que nunca muestre las barra de scroll.
	 */
	public static final int SCROLLBAR_NEVER     = 0;

	/**
	 * Indica que muestre las barra de scroll cuando sean necesarias.
	 */
	public static final int SCROLLBAR_AS_NEEDED = 1;

	/**
	 * Indica que siempre muestre las barra de scroll.
	 */
	public static final int SCROLLBAR_ALWAYS    = 2;
	
	/*************************************************************************/
	
	/**
	 * Barra de scroll horizontal.
	 *
	 * @see IScrollBar
	 */
	IScrollBar hsb;

	/**
	 * Barra de scroll vertical.
	 *
	 * @see IScrollBar
	 */
	IScrollBar vsb;
	
	//
	private Viewport viewport;
	private int policy_hsb,policy_vsb;
	private IComponent component;
	ScrollListener listener=new ScrollListener();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el panel con las barras de scroll visibles cuando sean necesarias.
	 *
	 * @param component Componente que va dentro del panel
	 *
	 * @see #SCROLLBAR_NEVER
	 * @see #SCROLLBAR_AS_NEEDED
	 * @see #SCROLLBAR_ALWAYS
	 *
	 * @see IScrollBar
	 */
	public IScrollPane(IComponent component)
	{
		this(component,SCROLLBAR_AS_NEEDED,SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Crea el panel con las barras de scroll.
	 *
	 * @param component Componente que va dentro del panel
	 * @param policy_hsb Tipo de visualización para las barra de scroll horizontal
	 * @param policy_vsb Tipo de visualización para las barra de scroll vertical
	 *
	 * @see #SCROLLBAR_NEVER
	 * @see #SCROLLBAR_AS_NEEDED
	 * @see #SCROLLBAR_ALWAYS
	 *
	 * @see IScrollBar
	 */
	public IScrollPane(IComponent component,int policy_hsb,int policy_vsb)
	{
		this.policy_hsb=policy_hsb;
		this.policy_vsb=policy_vsb;
		// Crea los componentes internos.
		add(hsb=new IScrollBar(IScrollBar.HORIZONTAL,0));
		add(vsb=new IScrollBar(IScrollBar.VERTICAL,0));
		hsb.visible=vsb.visible=false;
		// Asigna el componente principal.
		add(viewport=new Viewport());
		this.component=component;
		// Crea el listener.
		hsb.action_listener=vsb.action_listener=listener;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el componente que tiene el panel.
	 *
	 * @param component Componente a visualizar
	 */
	public void setComponent(IComponent component)
	{
		IComponent actual=viewport.getComponentAt(0);
		if (actual!=component)
		{
			if (actual!=null) actual.component_listener=null;
			viewport.removeAll();
			viewport.add(this.component=component);
		}
		//
		component.setSize(Math.max(component.getWidth(),viewport.getWidth()),Math.max(component.getHeight(),viewport.getHeight()));
		component.component_listener=listener;
	}

	/**
	 * Devuelve la barra de scroll horizontal.
	 *
	 * @see IScrollBar
	 */
	public IScrollBar getHorizontalScrollBar()
	{
		return hsb;
	}

	/**
	 * Devuelve la barra de scroll vertical.
	 *
	 * @see IScrollBar
	 */
	public IScrollBar getVerticalScrollBar()
	{
		return vsb;
	}

	/**
	 * Hace scroll a las coordenadas indicadas.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	public void scrollTo(int x,int y)
	{
		int offset=component.bounds.width-viewport.bounds.width;
		hsb.setOffset(offset>0?((x*100)/offset):0);
		//
		offset=component.bounds.height-viewport.bounds.height;
		vsb.setOffset(offset>0?((y*100)/offset):0);
	}

	public void setSize(int width,int height)
	{
		super.setSize(width,height);
		hsb.setLocation(0,height-IScrollBar.THICK);
		vsb.setLocation(width-IScrollBar.THICK,0);
		recalculate(true);
		setComponent(component);
	}

	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		hsb.setEnabled(enabled);
		vsb.setEnabled(enabled);
	}

	private void recalculate(boolean recalculate_viewport)
	{
		if (recalculate_viewport)
		{
			// Comprueba si tiene que visualizar las barras de scroll.
			hsb.visible=(policy_hsb!=SCROLLBAR_NEVER)&&((component.bounds.width>viewport.bounds.width)||(policy_hsb==SCROLLBAR_ALWAYS));
			vsb.visible=(policy_vsb!=SCROLLBAR_NEVER)&&((component.bounds.height>viewport.bounds.height)||(policy_vsb==SCROLLBAR_ALWAYS));
			int limit_x=bounds.width-vsb.bounds.width;
			int limit_y=bounds.height-hsb.bounds.height;
			if (hsb.visible||vsb.visible)
			{
				if (hsb.visible&&!vsb.visible) vsb.visible=(policy_vsb!=SCROLLBAR_NEVER)&&(component.bounds.height>limit_y);
					else if (vsb.visible&&!hsb.visible) hsb.visible=(policy_hsb!=SCROLLBAR_NEVER)&&(component.bounds.width>limit_x);
			}
			// Recalcula los tamaños.
			viewport.setSize(vsb.visible?(limit_x+1):bounds.width,hsb.visible?(limit_y+1):bounds.height);
			hsb.setSize(viewport.bounds.width);
			vsb.setSize(viewport.bounds.height);
			if (component.bounds.width!=0) hsb.setPercentage((viewport.bounds.width*100)/component.bounds.width);
			if (component.bounds.height!=0) vsb.setPercentage((viewport.bounds.height*100)/component.bounds.height);
		}
		//
		int offset_x=Math.min(0,viewport.bounds.width-component.bounds.width);
		int offset_y=Math.min(0,viewport.bounds.height-component.bounds.height);
		//
		component.bounds.setLocation((offset_x*hsb.offset)/100,(offset_y*vsb.offset)/100);
		processComponentEvent(Event.COMPONENT_RESIZED);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea un panel con scroll para un componente (con barras visibles cuando son necesarias).
	 *
	 * @param component Componente
	 *
	 * @see IScrollBar
	 */	
	public static IScrollPane createScrollPane(IComponent component)
	{
		return new IScrollPane(component,IScrollPane.SCROLLBAR_AS_NEEDED,IScrollPane.SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Crea un panel con scroll vertical para un componente (sin barra horizontal y barra vertical siempre visible).
	 *
	 * @param component Componente
	 *
	 * @see IScrollBar
	 */	
	public static IScrollPane createVerticalScrollPane(IComponent component)
	{
		return new IScrollPane(component,IScrollPane.SCROLLBAR_NEVER,IScrollPane.SCROLLBAR_ALWAYS);
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ISCROLLPANE;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Contenedor para el componente principal del ScrollPane.
	 */
	public static class Viewport extends IContainer
	{
		public Viewport()
		{
			setBackground(0xff7f7f7f);
		}
		
		public int getSkinId()
		{
			return Skin.KEY_ISCROLLPANE$VIEWPORT;
		}
	}
	
	private class ScrollListener implements ActionListener,ComponentListener
	{
		public void actionPerformed(Object obj,Object data)
		{
			recalculate(false);
		}

		public void componentHidden(IComponent component)
		{
		}

		public void componentMoved(IComponent component)
		{
		}

		public void componentResized(IComponent component)
		{
			recalculate(true);
		}

		public void componentShown(IComponent component)
		{
		}
	}
}