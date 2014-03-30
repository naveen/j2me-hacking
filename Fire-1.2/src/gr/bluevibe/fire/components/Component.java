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
 * Created on May 17, 2006
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.util.CommandListener;
import gr.bluevibe.fire.util.ComponentListener;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

/**
 * Fire Components are pretty much the same as Components in j2se.
 * Every component has a paint method and it is expected to draw its contents inside a rectangle of given width/height.
 * A component can have commands associated with it.
 *   
 * @author padeler
 *
 */
public class Component
{
	private int _componentID=-1;
	
	private int height=-1;
	private int width=-1;
	private int minWidth;
	private int minHeight;
	
	private Command cmd=null;
	private CommandListener listener=null;
	private boolean selected=false;
	
	
	private ComponentListener validator = null;
	
	public Component()
	{
	}
	
	/**
	 * Paint is called by the container of the component to allow it to draw itself on Graphics g
	 * The drawable area on g is (0,0,width,height).
	 * 
	 * @param g the area on witch the component will draw it self.
	 */
	public void paint(Graphics g)
	{	
	}
	
	/**
	 * If a component is traversable it can have commands associated with it and receive key and tap events (and vice versa).
	 * @return
	 */
	public boolean isTraversable()
	{
		if(cmd==null) return false;
		return true;
	}
	
	/**
	 * If a componets states that it is animated, it will receive periodically clock events in order to update its animation.
	 * @return
	 */
	public boolean isAnimated()
	{
		return false;
	}
	
	/**
	 * Animated components receive clock events in order to update their animation.
	 * @return true if repainting is needed after the clock event.
	 */
	public boolean clock() {return false;}
	
	/**
	 * Sets this component on/off selected mode. 
	 * @param v
	 */
	public void setSelected(boolean v){selected = v;}
	
	public boolean isSelected(){return selected;}
	
	/**
	 * Key events are propagated from FireScreen down to the components of the panel or the popup that contains them.
	 * If a component if not traversable it will not recieve key events.
	 * @param key
	 * @return
	 */
	public boolean keyEvent(int key){return false;}
	public boolean pointerEvent(int x,int y){return false;}
	
	/**
	 * A validate event requests from the component to recalculate its internal properties suck as width/height etc.
	 *
	 */
	public void validate(){}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	
	public int getMinHeight()
	{
		return minHeight;
	}

	public void setMinHeight(int minHeight)
	{
		this.minHeight = minHeight;
	}

	public int getMinWidth()
	{
		return minWidth;
	}

	public void setMinWidth(int minWidth)
	{
		this.minWidth = minWidth;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}
	
	/**
	 * Add a command to the components. If a components has a command assosiated with it, then it is considered traversable.
	 * @param c
	 */
	public void addCommand(Command c)
	{
		cmd=c;
	}
	
	public void setCommandListener(CommandListener listener)
	{
		this.listener=listener;
	}
	
	protected final boolean generateEvent()
	{
		if(listener!=null && cmd!=null)
		{
			listener.commandAction(cmd,this);
			return true;
		}
		return false;
	}

	int get_componentID()
	{
		return _componentID;
	}

	void set_componentID(int _componentid)
	{
		_componentID = _componentid;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Component)
		{
			Component c = (Component)o;
			if(c.get_componentID()==get_componentID()) return true;
		}
		return false;
	}
	
	void addValidateListener(ComponentListener validator)
	{
		this.validator = validator;
	}
	
	protected void fireValidateEvent()
	{
		if(validator!=null)
		{
			validator.internalValidateEvent(this);
		}
	}
	
	protected void setCurrent(Displayable d)
	{
		if(validator!=null)
		{
			validator.setCurrent(d);
		}
	}
	
	protected void setContainerCurrent()
	{
		if(validator!=null)
		{
			validator.setContainerCurrent();
		}
	}
}