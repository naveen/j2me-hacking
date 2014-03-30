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

import javax.microedition.lcdui.*;

import com.java4ever.apime.math.*;

/**
 * Gestor de repintado.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class RepaintManager
{
	/**
	 * Instancia global.
	 */
	public static final RepaintManager instance=new RepaintManager();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Actualización visual (foreground, background, font, ...).
	 */
	public static final int VISUAL  = 0;

	/**
	 * Actualización de las coordenadas (location, size, visible, ...).
	 */
	public static final int BOUNDS  = 1;

	/*************************************************************************/

	private Rectangle damage=new Rectangle(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
	private Rectangle temp=new Rectangle();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el gestor.
	 */
	private RepaintManager()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Repinta un componente (VISUAL).
	 *
	 * @param component Componente
	 *
	 * @see IComponent
	 * @see #VISUAL
	 */
	public synchronized void repaint(IComponent component)
	{
		repaint(component,VISUAL);
	}

	/**
	 * Repinta una zona de un componente (VISUAL).
	 *
	 * @param component Componente
	 * @param x Coordenada X local
	 * @param y Coordenada Y local
	 * @param width Ancho
	 * @param height Alto
	 *
	 * @see IComponent
	 * @see #VISUAL
	 */
	public synchronized void repaint(IComponent component,int x,int y,int width,int height)
	{
		repaint(component.screen.x+x,component.screen.y+y,width,height);
	}

	/**
	 * Repinta un componente.
	 *
	 * @param component Componente
	 * @param type Tipo de repintado
	 *
	 * @see IComponent
	 * @see #VISUAL
	 * @see #BOUNDS
	 */
	public synchronized void repaint(IComponent component,int type)
	{
		if (component.getRoot()!=null||(component instanceof IToolTip))
		{
			if (type==BOUNDS)
			{
				repaint(component.clipping);
				if (component.visible)
				{
					if (component instanceof IContainer) updateContainer((IContainer)component);
						else if (!(component instanceof IToolTip)) updateComponent(component);
				}
				else component.clipping.setBounds(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
			}
			repaint(component.clipping);
		}
		else if (type==BOUNDS)
		{
			repaint(component.clipping);
			component.clipping.setBounds(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
		}
	}

	/**
	 * Repinta el área indicada.
	 *
	 * @param bounds Area a repintar
	 *
	 * @see Rectangle
	 */
	public synchronized void repaint(Rectangle bounds)
	{
		repaint(bounds.x,bounds.y,bounds.width,bounds.height);
	}

	/**
	 * Repinta el área indicada.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param width Ancho
	 * @param height Alto
	 */
	public synchronized void repaint(int x,int y,int width,int height)
	{
		int rx=Math.min(damage.x,x);
		int ry=Math.min(damage.y,y);
		damage.setBounds(rx,ry,Math.max(damage.x+damage.width,x+width)-rx,Math.max(damage.y+damage.height,y+height)-ry);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Indica si necesita repintar.
	 */
	synchronized boolean needRepaint()
	{
		return (damage.width>0);
	}

	/**
	 * Prepara el repintado.
	 *
	 * @param g Contexto gráfico
	 * @param finish True para indicar que es final de repintado
	 */
	synchronized void prepareRepaint(Graphics g,boolean finish)
	{
		if (finish)
		{
			g.setClip(0,0,Short.MAX_VALUE,Short.MAX_VALUE);
			damage.setBounds(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
		}
		else g.setClip(damage.x,damage.y,damage.width,damage.height);
	}

	private void updateComponent(IComponent component)
	{
		IContainer parent=component.parent;
		if (parent!=null)
		{
			// Inicializa los valores nulos.
			if (component.foreground==0) component.foreground=parent.foreground;
			if (component.background==0) component.background=parent.background;
			if (component.font==null) component.font=parent.font;
			// Comprueba si el componente está dentro de la zona visible.
			temp.setBounds(0,0,parent.bounds.width,parent.bounds.height);
			if (component.visible&&temp.intersects(component.bounds))
			{
				// Calcula las coordenadas en pantalla.
				component.screen.setLocation(parent.screen.x+component.bounds.x,parent.screen.y+component.bounds.y);
				// Calcula la zona visual.
				component.clipping.setBounds(component.screen.x,component.screen.y,component.bounds.width,component.bounds.height);
				component.clipping.intersection(parent.clipping,component.clipping);
			}
			else component.clipping.setBounds(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
		}
	}

	private void updateContainer(IContainer container)
	{
		updateComponent(container);
		// Recorre los componentes.
		for (int i=container.components.size()-1;i>=0;i--)
		{
			IComponent component=(IComponent)container.components.elementAt(i);
			if (component instanceof IContainer) updateContainer((IContainer)component);
				else updateComponent(component);
		}
	}
}