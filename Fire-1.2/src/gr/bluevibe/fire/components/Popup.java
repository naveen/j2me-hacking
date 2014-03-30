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
 * Created on Aug 25, 2006
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.displayables.FireScreen;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Popup extends Panel
{
	private int posX,posY;
	private int topOffset = 1;
	private int bottomOffset = 1;
	private Font componentFont = FireScreen.defaultPopupFont;

	public Popup()
	{
		super();
	}
	
	
	public void paint(Graphics g)
	{
		viewportHeight = (getHeight()-bottomOffset-topOffset);
		int ypos=topOffset;
		int htmp=0;
		Component row;
		int width = getWidth();
		int height = getHeight();
		int i;
		int offX = g.getTranslateX();
		int offY = g.getTranslateY();

		g.setColor(FireScreen.defaultColor);
		g.fillRect(0,0,width,height);
		g.setColor(0x00000000);
		g.drawRect(0,0,width-1,height-1);
	//	g.fillRect(0,0,width,topOffset);
	//	g.fillRect(0,height-bottomOffset,width,height);
		g.drawLine(width-FireScreen.SCROLLBAR_WIDTH+3,0,width-FireScreen.SCROLLBAR_WIDTH+3,height);
		
		
		for(i =0 ;i<rows.size() && ypos<(verticalOffset+height);++i)
		{
			row = (Component)rows.elementAt(i);
			htmp = row.getHeight();
			if(ypos+htmp>verticalOffset)
			{ // arxizoume na zografizoume apo auto edo to row.
				
				int trY = ypos-verticalOffset;
				int clipYa=0;

				if(trY<topOffset)
				{
					clipYa = topOffset- trY;
				}
				int clipYb=htmp-clipYa;
				
				if(trY+htmp>viewportHeight)
				{
					clipYb = clipYb - ((trY+htmp)-(height-bottomOffset)); 
				}
				
				g.translate(-g.getTranslateX()+offX,-g.getTranslateY()+offY);

				g.translate(0,trY);
				g.setClip(0,clipYa,width-FireScreen.SCROLLBAR_WIDTH,clipYb);
				row.paint(g);
			}
			ypos +=htmp;
		}

		/* Zografizoume ta ypolipa komatia tou interface. */
		g.translate(-g.getTranslateX()+offX,-g.getTranslateY()+offY);
		g.setClip(0,0,width,height);
		
		
		if(internalHeight>viewportHeight)
		{
			if(animateDown || animateUp)
			{
				scrollY = (((verticalOffset+viewportHeight/2) * viewportHeight)/internalHeight) + topOffset;
			}
			if(scrollY<=FireScreen.getTopOffset()) scrollY=topOffset;
			if(scrollY>(getHeight()-bottomOffset-FireScreen.SCROLLBAR_HEIGHT)) scrollY = height-FireScreen.SCROLLBAR_HEIGHT-bottomOffset;
			
			g.setColor(FireScreen.defaultScrollColor);
			g.fillRect(width-FireScreen.SCROLLBAR_WIDTH+3,scrollY,FireScreen.SCROLLBAR_WIDTH-3,FireScreen.SCROLLBAR_HEIGHT);
		}	
	}
	public synchronized int add(Component cmp)
	{
		if(cmp instanceof Row)
		{
			((Row)cmp).setFont(componentFont);
		}
		return super.add(cmp);
	}
	
	public boolean pointerEvent(int x, int y)
	{
		
		int height = getHeight();
		int width = getWidth();
		
		if(y>height || y<0 || x>width || x<0)
		{ // close popup
			control.closePopup();
			return false; // to repaint tha gini sto closePopup , opote den einai anangi na to ksanakanoume. 
		}
		else if(y>topOffset && x<width-FireScreen.SCROLLBAR_WIDTH)
		{ // event se kapoio component tou Popup
			int i,tmpHeight=0;
			Component c=null;
			for(i=0;i<rows.size();++i)
			{
				c=(Component)rows.elementAt(i);
				tmpHeight+=c.getHeight();
				if((tmpHeight-verticalOffset+topOffset)>=y) break;
			}
			if(pointerPos==i) // fire event.
			{
				tmpHeight = tmpHeight-c.getHeight();
				c.pointerEvent(x,y+verticalOffset-tmpHeight-topOffset);
				return true;
			}
			if(i==rows.size())
			{ // no component in tap location.
				return true;
			}
			
			// selection event.
			if(pointerPos>=0)
			{ // deselect old selected component
				Component old = (Component)rows.elementAt(pointerPos);
				old.setSelected(false);
			}
			pointerPos=i;
			Component sel = (Component)rows.elementAt(pointerPos);
			sel.setSelected(true);
			return true;
		}
		else if(internalHeight>viewportHeight)
		{ // scrollbar action.
			if(y>scrollY) 
			{
				return keyEvent(Canvas.RIGHT);

			}
			if(y<scrollY)
			{
				return keyEvent(Canvas.LEFT);
			}
		}
		return false;
	}
	
	public boolean keyEvent(int key)
	{
		if(key!=Canvas.UP && key!=Canvas.DOWN && key!=Canvas.LEFT && key!=Canvas.RIGHT && key!=Canvas.FIRE)
		{
			control.closePopup();
			return false;
		}
		return super.keyEvent(key);
	}


	public int getPosX()
	{
		return posX;
	}

	public void setPosX(int posX)
	{
		this.posX = posX;
	}

	public int getPosY()
	{
		return posY;
	}

	public void setPosY(int posY)
	{
		this.posY = posY;
	}


	public Font getFont()
	{
		return componentFont;
	}


	public void setFont(Font componentFont)
	{
		this.componentFont = componentFont;
	}


	public int getTopOffset()
	{
		return topOffset;
	}


	public void setTopOffset(int topOffset)
	{
		this.topOffset = topOffset;
	}


	public int getBottomOffset()
	{
		return bottomOffset;
	}


	public void setBottomOffset(int bottomOffset)
	{
		this.bottomOffset = bottomOffset;
	}
	
	public void validate()
	{
		super.validate();
		
		// resize the popup to minimum height
		int th = internalHeight+topOffset+bottomOffset;
		if(th<getHeight())
		{
			setHeight(th);
		}
		// resize the popup to minimum width
		int minW=0;
		for(int i=0;i<rows.size();++i)
		{
			Component c = (Component)rows.elementAt(i);
			if(c.getMinWidth()>minW) minW = c.getMinWidth();
		}
		setWidth(minW);
	}
	
}