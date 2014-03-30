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
import com.java4ever.apime.ui.event.*;
import com.java4ever.apime.ui.list.*;

/**
 * Lista.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class IList extends IComponent
{
	/**
	 * Modelo de datos.
	 *
	 * @see ListModel
	 */
	ListModel model;

	/**
	 * Modelo de selección.
	 *
	 * @see ListSelectionModel
	 */
	ListSelectionModel selection;
	
	/**
	 * Renderer para cada celda.
	 *
	 * @see ListCellRenderer
	 */
	ListCellRenderer renderer;
	 
	// Variables de control.
	private int cell_height=16;
	private int rollover=-1;
	private Point min_size=new Point();
	private ListListener listener=new ListListener();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea la lista (Selección múltiple).
	 */
	public IList()
	{
		this(ListSelectionModel.MULTIPLE_SELECTION);
	}

	/**
	 * Crea la lista.
	 *
	 * @param mode Modo de selección
	 *
	 * @see ListSelectionModel
	 */
	public IList(int mode)
	{
		setModel(new ListModel());
		setSelectionModel(new ListSelectionModel(mode));
		setCellRenderer(new DefaultListCellRenderer());
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
		if (this.model!=null) this.model.setActionListener(null);
		this.model=model;
		this.model.setActionListener(listener);
		recalculate();
	}

	/**
	 * Devuelve el modelo de datos.
	 *
	 * @see ListModel
	 */
	public ListModel getModel()
	{
		return model;
	}

	/**
	 * Cambia el modelo de selección.
	 *
	 * @param selection Modelo de selección
	 *
	 * @see ListSelectionModel
	 */
	public void setSelectionModel(ListSelectionModel selection)
	{
		if (this.selection!=null) this.selection.setActionListener(null);
		this.selection=selection;
		this.selection.setActionListener(listener);
		repaint();
	}

	/**
	 * Devuelve el modelo de selección.
	 *
	 * @see ListSelectionModel
	 */
	public ListSelectionModel getSelectionModel()
	{
		return selection;
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
		this.renderer=renderer;
		repaint();
	}

	/**
	 * Devuelve el renderer para cada celda.
	 *
	 * @see ListCellRenderer
	 */
	public ListCellRenderer getCellRenderer()
	{
		return renderer;
	}

	/**
	 * Cambia la altura de cada celda.
	 *
	 * @param height Alto
	 */
	public void setCellHeight(int height)
	{
		this.cell_height=height;
		recalculate();
	}

	/**
	 * Devuelve la altura de cada celda.
	 */
	public int getCellHeight()
	{
		return cell_height;
	}

	/**
	 * Hace scroll hasta el índice indicado.
	 *
	 * @param index Indice
	 */
	public void scrollTo(int index)
	{
		IScrollPane sp=getScrollPane();
		if (sp!=null)
		{
			rollover=-1;
			sp.scrollTo(0,(cell_height*index)-(parent.bounds.height>>1)+(cell_height>>1));
		}
	}
	
	/**
	 * Hace scroll hasta el índice seleccionado.
	 */
	public void scrollToSelectedIndex()
	{
		scrollTo(selection.getSelectedIndex());
	}

	public void setSize(int width,int height)
	{
		if (min_size.x==0||min_size.y==0) min_size.setLocation(width,height);
    	super.setSize(Math.max(min_size.x,width),Math.max(min_size.y,height));
	}

	private void recalculate()
	{
		setSize(bounds.width,cell_height*model.size());
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ILIST;
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		for (int i=0;i<model.size();i++)
		{
			int y=i*cell_height;
			if (y+cell_height<g.getClipY()) continue;
				else if (y>g.getClipY()+g.getClipHeight()) break;
			//
			g.translate(0,y);
			IComponent c=renderer.getListCellRendererComponent(this,model.elementAt(i),i,selection.isSelectedIndex(i),(rollover==i));
			c.setBounds(0,0,bounds.width,cell_height);
			c.setFont(font);
			c.setEnabled(enabled);
			c.paint(g);
			g.translate(0,-y);
		}
	}

	private void repaintCell(int index)
	{
		RepaintManager.instance.repaint(this,0,index*cell_height,bounds.width,cell_height);
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processMouseEvent(int type,int x,int y)
	{
		if (type==Event.MOUSE_CLICKED)
		{
			// Selecciona/Deselecciona.
			int index=y/cell_height;
			if (index>=0)
			{
				if (!selection.isSelectedIndex(index)) selection.selectIndex(index);
					else selection.deselectIndex(index);
			}
			rollover=index;
			repaintCell(rollover);
		}
		else if (type==Event.MOUSE_ENTERED||type==Event.MOUSE_MOVED)
		{
			int r=y/cell_height;
			if (rollover!=r)
			{
				repaintCell(rollover);
				rollover=r;
				repaintCell(rollover);
			}
		}
		else if (type==Event.MOUSE_EXITED)
		{
			repaintCell(rollover);
			rollover=-1;
		}
		//
		super.processMouseEvent(type,x,y);
	}

	/*************************************************************************/
	/*************************************************************************/
	
	private class ListListener implements ActionListener
	{
		public void actionPerformed(Object object,Object data)
		{
			if (object==model)
			{
				selection.deselectAll();
				recalculate();
			}
			else processActionEvent(null);
		}
	}
}