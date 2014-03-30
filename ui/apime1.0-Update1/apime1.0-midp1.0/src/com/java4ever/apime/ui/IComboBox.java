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

import com.java4ever.apime.ui.event.*;
import com.java4ever.apime.ui.list.*;

/**
 * ComboBox.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IComboBox extends IButton
{
	/**
	 * Número de filas visibles.
	 */
	private int rows=4;
	
	// Lista interna.
	private IScrollPane scroll;
	private IList list;
	 
	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el combobox.
	 */
	public IComboBox()
	{
		super("",null);
		halign=Graphics.LEFT;
		// Lista interna.
		list=new IList(ListSelectionModel.SINGLE_SELECTION);
		list.action_listener=new ActionListener()
			{
				public void actionPerformed(Object object,Object data)
				{
					processActionEvent(data,true);
				}
			};
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el modelo de datos.
	 *
	 * @param model Modelo de datos
	 *
	 * @see ListModel
	 */
	public void setModel(ListModel model)
	{
		list.setModel(model);
		repaint();
	}

	/**
	 * Devuelve el modelo de datos.
	 *
	 * @see ListModel
	 */
	public ListModel getModel()
	{
		return list.getModel();
	}

	/**
	 * Cambia el renderer para cada celda.
	 *
	 * @param renderer Renderer
	 *
	 * @see ListCellRenderer
	 */
	public void setCellRenderer(ListCellRenderer renderer)
	{
		list.setCellRenderer(renderer);
		repaint();
	}

	/**
	 * Devuelve el renderer para cada celda.
	 *
	 * @see ListCellRenderer
	 */
	public ListCellRenderer getCellRenderer()
	{
		return list.getCellRenderer();
	}

	/**
	 * Cambia el número de filas visibles.
	 *
	 * @param rows Número de filas
	 */
	public void setVisibleRowCount(int rows)
	{
		this.rows=rows;
	}

	/**
	 * Devuelve el número de filas visibles.
	 */
	public int getVisibleRowCount()
	{
		return rows;
	}

	/**
	 * Selecciona un índice.
	 *
	 * @param index Indice a seleccionar
	 */
	public void setSelectedIndex(int index)
	{
		list.selection.setSelectedIndex(index);
	}

	/**
	 * Devuelve el índice seleccionado.
	 *
	 * @return Indice
	 */
	public int getSelectedIndex()
	{
		return list.selection.getSelectedIndex();
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ICOMBOBOX;
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		int index=list.selection.getSelectedIndex();
		if (index!=-1)
		{
			IComponent c=list.renderer.getListCellRendererComponent(list,list.model.elementAt(index),index,true,rollover);
			c.setSize(bounds.width,bounds.height);
			c.setFont(font);
			c.paint(g);
		}
		else super.paintComponent(g);
		//
		paintArrow(g);
	}

	/**
	 * Pinta la flecha.
	 */	
	protected void paintArrow(Graphics g)
	{
		int x=bounds.width-15;
		int y=(bounds.height-5)>>1;
		//
		g.translate(x,y);
		g.setColor(foreground);
		for (int i=0;i<5;i++) g.drawLine(i,i,9-i,i);
		g.translate(-x,-y);
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processActionEvent(Object data)
	{
		processActionEvent(data,false);
	}
	
	private void processActionEvent(Object data,boolean selection)
	{
		if (selection||scroll!=null)
		{
			if (scroll!=null)
			{
				scroll.parent.remove(scroll);
				scroll=null;
			}
			super.processActionEvent(data);
			repaint();
		}
		else
		{
			IContainer root=getRoot();
			int y=screen.y+bounds.height-1;
			int height_total=getHeight()*list.model.size();
			int height=Math.min(height_total,getHeight()*rows);
			if (y+height>root.bounds.height) y=Math.max(0,y-height-bounds.height+2);
			list.setCellHeight(bounds.height);
			if (height<height_total) scroll=IScrollPane.createVerticalScrollPane(list);
				else scroll=new IScrollPane(list,IScrollPane.SCROLLBAR_NEVER,IScrollPane.SCROLLBAR_NEVER);
			root.insert(scroll,screen.x,y,bounds.width,height,0);
			list.scrollToSelectedIndex();
		}
	}
}