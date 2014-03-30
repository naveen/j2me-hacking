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
import javax.microedition.lcdui.*;

import com.java4ever.apime.math.*;
import com.java4ever.apime.ui.event.*;

/**
 * Contenedor de componentes.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IContainer extends IComponent
{
	/**
	 * Componentes.
	 */
	Vector components=new Vector();

	/**
	 * Listener de contenedor.
	 *
	 * @see ContainerListener
	 */
	ContainerListener container_listener;

	// Variables para optimizar la velocidad.
	private static Rectangle temp_clip=new Rectangle(),temp_component=new Rectangle();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Constructor.
	 */
	public IContainer()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Añade un componente.
	 *
	 * @param component Componente a añadir
	 */
	public void add(IComponent component)
	{
		insert(component,Integer.MAX_VALUE);
	}

	/**
	 * Añade un componente.
	 *
	 * @param component Componente a añadir
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	public void add(IComponent component,int x,int y)
	{
		insert(component,x,y,component.bounds.width,component.bounds.height,Integer.MAX_VALUE);
	}

	/**
	 * Añade un componente.
	 *
	 * @param component Componente a añadir
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param width Ancho
	 * @param height Alto
	 */
	public void add(IComponent component,int x,int y,int width,int height)
	{
		insert(component,x,y,width,height,Integer.MAX_VALUE);
	}

	/**
	 * Inserta un componente.
	 *
	 * @param component Componente a añadir
	 * @param index Indice donde se inserta
	 */
	public void insert(IComponent component,int index)
	{
		insert(component,component.bounds.x,component.bounds.y,component.bounds.width,component.bounds.height,index);
	}

	/**
	 * Inserta un componente.
	 *
	 * @param component Componente a añadir
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param index Indice donde se inserta
	 */
	public void insert(IComponent component,int x,int y,int index)
	{
		insert(component,x,y,component.bounds.width,component.bounds.height,index);
	}

	/**
	 * Inserta un componente.
	 *
	 * @param component Componente a añadir
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param index Indice donde se inserta
	 */
	public void insert(IComponent component,int x,int y,int width,int height,int index)
	{
		if (index<0||index>components.size()) index=components.size();
		if (component.parent!=null) component.parent.remove(component);
		component.setBounds(x,y,width,height);
		components.insertElementAt(component,index);
		component.parent=this;
		processContainerEvent(Event.COMPONENT_ADDED,component);
	}

	/**
	 * Elimina un componente.
	 *
	 * @param index Indice del componente a eliminar
	 */
	public void remove(int index)
	{
		IComponent component=getComponentAt(index);
		if (component!=null)
		{
			if (component.isFocusOwner()) FocusManager.instance.transferFocus(null);
			component.parent=null;
			components.removeElementAt(index);
			processContainerEvent(Event.COMPONENT_REMOVED,component);
		}
	}

	/**
	 * Elimina un componente.
	 *
	 * @param component Componente a eliminar
	 *
	 * @see IComponent
	 */
	public void remove(IComponent component)
	{
		remove(components.indexOf(component));
	}

	/**
	 * Elimina todos los componentes.
	 */
	public void removeAll()
	{
		while (!components.isEmpty()) remove(0);
	}

	/**
	 * Devuelve el número de componentes que contiene.
	 */
	public int getComponentCount()
	{
		return components.size();
	}

	/**
	 * Devuelve el componente situado en el índice indicado.
	 *
	 * @param index Indice del componente
	 *
	 * @return Componente o 'null' si no existe el índice indicado
	 */
	public IComponent getComponentAt(int index)
	{
		if (index>=0&&index<components.size()) return (IComponent)components.elementAt(index);
			else return null;
	}

	/**
	 * Devuelve el componente que contiene las coordenadas indicadas.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 *
	 * @return Componente o 'null' si no hay ninguno
	 */
	public IComponent getComponentAt(int x,int y)
	{
		for (int i=0;i<components.size();i++)
		{
			IComponent component=(IComponent)components.elementAt(i);
			if (component.bounds.contains(x,y)) return component;
		}
		return null;
	}

	/**
	 * Ajusta el tamaño del contenedor al de los componentes.
	 */
	public void pack()
	{
		int width=0,height=0;
		for (int i=0;i<components.size();i++)
		{
			IComponent c=(IComponent)components.elementAt(i);
			width=Math.max(width,c.bounds.x+c.bounds.width);
			height=Math.max(height,c.bounds.y+c.bounds.height);
		}
		setSize(width,height);
	}

	/**
	 * Asigna el listener de contenedor.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see ContainerListener
	 */
	public void setContainerListener(ContainerListener listener)
	{
		this.container_listener=listener;
	}

	/**
	 * Devuelve el listener de contenedor o 'null' si no tiene.
	 *
	 * @see ActionListener
	 */
	public ContainerListener getContainerListener()
	{
		return container_listener;
	}
 	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ICONTAINER;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Pinta el contenedor.
	 *
	 * @param g Contexto gráfico
	 */
	public void paintComponent(Graphics g)
	{
		// Almacena el clipping actual.
		int clip_x=g.getClipX();
		int clip_y=g.getClipY();
		int clip_width=g.getClipWidth();
		int clip_height=g.getClipHeight();
		// Recorre los componentes.
		for (int i=components.size()-1;i>=0;i--)
		{
			IComponent component=(IComponent)components.elementAt(i);
			// Comprueba si es visible.
			if (component.clipping.width>0)
			{
				int cx=component.bounds.x;
				int cy=component.bounds.y;
				temp_clip.setBounds(clip_x-cx,clip_y-cy,clip_width,clip_height);
				temp_component.setBounds(component.clipping.x-component.screen.x,component.clipping.y-component.screen.y,component.clipping.width,component.clipping.height);
				if (temp_component.intersection(temp_clip,temp_component).width>0)
				{
					g.translate(cx,cy);
					g.setClip(temp_component.x,temp_component.y,temp_component.width,temp_component.height);
					component.paint(g);
					g.translate(-cx,-cy);
				}
			}
		}
		// Restaura el clipping.
		g.setClip(clip_x,clip_y,clip_width,clip_height);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Procesa un evento de contenedor.
	 *
	 * @param type Tipo de evento
	 *
	 * @see Event#COMPONENT_ADDED
	 * @see Event#COMPONENT_REMOVED
	 */
	protected void processContainerEvent(int type,IComponent component)
	{
		RepaintManager.instance.repaint(component,RepaintManager.BOUNDS);
		//
		if (container_listener!=null)
		{
			if (type==Event.COMPONENT_ADDED) container_listener.componentAdded(this,component);
				else if (type==Event.COMPONENT_REMOVED) container_listener.componentRemoved(this,component);
		}
	}
}