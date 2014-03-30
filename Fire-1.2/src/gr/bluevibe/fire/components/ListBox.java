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
package gr.bluevibe.fire.components;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ListBox extends Component
{
	private String id;
	
	
	private boolean multiple=false;
	private Font font = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);;
	private int color=0x00000000;
	private Integer bgColor=null;
	private Integer filled = null;
	private boolean border= false;
	private Vector elements = new Vector();

	private Image selectedBullet=null;
	private Image bullet=null;
	
	private int bulletW = 10;
	private int bulletH = 10;
	
	
	/* support vars */
	private int pointerPos=-1;
	
	public ListBox()
	{
	}
	
	public void add(ListElement el)
	{
		el.setColor(color);
		el.setFont(font);
		el.setBgColor(bgColor);
		
		elements.addElement(el);
		int id = elements.size()-1;
		el.set_componentID(id);
	}
	
	public void paint(Graphics g)
	{
		int width = getWidth();
		int  height = getHeight();
				
		if(filled!=null)
		{
			g.setColor(filled.intValue());
			g.fillRect(0,0,width,height);
		}
		
		// draw each element.
		int voffset=0;
		for(int i =0;i<elements.size();++i)
		{
			ListElement el = (ListElement)elements.elementAt(i);
			int trY = voffset;
			int trX =bulletW;
			if(el.isChecked())
			{
				if(selectedBullet!=null)
				{
					g.drawImage(selectedBullet,0,trY,Graphics.TOP|Graphics.LEFT);
				}
				else
				{
					g.setColor(0x0000FF00);
					g.fillRoundRect(0+2,trY+2,bulletW-4,bulletH-4,10,10);
				}
			}
			else
			{
				if(bullet!=null)
				{
					g.drawImage(bullet,0,trY,Graphics.TOP|Graphics.LEFT);
				}
			}
			g.translate(trX,trY);
			if(pointerPos==i) el.setOnPointer(true);
			el.paint(g);
			if(pointerPos==i) el.setOnPointer(false);
			g.translate(-trX,-trY);
			voffset+=el.getHeight();
		}

		if(border)
		{
			g.setColor(0x00000000);
			g.drawRect(0,0,getWidth()-1,getHeight()-1);
		}
	}
	
	public void validate()
	{
		int width = getWidth();
		setMinWidth(width);
		// kitame ta elements
		int height=0;
		for(int i =0 ;i<elements.size();++i)
		{
			Component c = (Component)elements.elementAt(i);
			c.setWidth(width - bulletW);
			c.setMinHeight(bulletH);
			c.validate();
			height+=c.getHeight();
		}
		
		setHeight(height);
		
		if(getMinHeight()>getHeight())
		{
			setHeight(getMinHeight());
		}
	}
	
	public boolean pointerEvent(int x, int y)
	{
		int tmpHeight=0,i;
		for(i =0 ;i<elements.size();++i)
		{
			ListElement e = (ListElement)elements.elementAt(i);
			tmpHeight+=e.getHeight();
			if(tmpHeight>=y) break;
		}
		if(i<elements.size())
		{
			pointerPos=i;
			check();
			generateEvent();
		}
		return true;
	}
	
	public boolean keyEvent(int key)
	{
		switch(key)
		{
			case Canvas.UP:
				if(pointerPos<=0) {pointerPos=-1 ;return false;}
				pointerPos--;
				return true;
			case Canvas.DOWN:
				if(pointerPos>=elements.size()-1) {pointerPos=elements.size() ;return false;}
				pointerPos++;
				return true;
			case Canvas.FIRE:
			{
				if(pointerPos>=0 && pointerPos<elements.size())
				{
					check();
				}
				generateEvent();
				return true;
			}
		}
		return true;
	}
	
	private void check()
	{
		if(pointerPos<0 || pointerPos>=elements.size()) return;
		
		ListElement e = (ListElement)elements.elementAt(pointerPos);
		if(multiple)
		{
			e.setChecked(!e.isChecked());
			return;
		}
		// mporoume na exoume mono ena checked.
		if(e.isChecked()) return;
		e.setChecked(true);
		
		for(int i=0;i<pointerPos;++i)
		{
			((ListElement)(elements.elementAt(i))).setChecked(false);
		}
		for(int i=pointerPos+1;i<elements.size();++i)
		{
			((ListElement)(elements.elementAt(i))).setChecked(false);
		}
	}
	
	public boolean isBorder()
	{
		return border;
	}

	public void setBorder(boolean border)
	{
		this.border = border;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public Integer getFilled()
	{
		return filled;
	}

	public void setFilled(Integer filled)
	{
		this.filled = filled;
	}

	public Font getFont()
	{
		return font;
	}

	public void setFont(Font font)
	{
		this.font = font;
	}

	public boolean isMultiple()
	{
		return multiple;
	}

	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}


	public Integer getBgColor()
	{
		return bgColor;
	}
	
	public Vector getCheckedElements()
	{ 
		Vector res = new Vector();
		for(int i =0;i<elements.size();++i)
		{
			ListElement e = (ListElement)elements.elementAt(i);
			if(e.isChecked())
			{
				res.addElement(e);
			}
		}
		return res;
	}



	public void setBgColor(Integer bgColor)
	{
		this.bgColor = bgColor;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	public boolean isTraversable()
	{
		return true;		
	}

	public Image getBullet()
	{
		return bullet;
	}

	public void setBullet(Image bullet)
	{
		this.bullet = bullet;
		if(bullet!=null)
		{
			bulletW = bullet.getWidth();
			bulletH = bullet.getHeight();
		}
	}

	public Image getSelectedBullet()
	{
		return selectedBullet;
	}

	public void setSelectedBullet(Image selectedBullet)
	{
		this.selectedBullet = selectedBullet;
	}
}
