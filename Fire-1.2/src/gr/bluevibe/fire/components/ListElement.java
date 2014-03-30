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

import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.FString;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class ListElement extends Component
{
	
	private Object id;
	private boolean checked=false;

	private int color=0xFFFFFFFF;
	private Integer bgColor=new Integer(0x00000000);
	
	private FString text = new FString();
	private boolean onPointer=false;

	public ListElement()
	{
	}
	
	
	public ListElement(String text,Object id,boolean checked)
	{
		this.text.setText(text);
		this.id=id;
		this.checked=checked;
	}

	public void paint(Graphics g)
	{
		int txtCol;
		Integer bgCol = null;
		if(isOnPointer())
		{
			bgCol = FireScreen.selectedLinkBgColor;
			txtCol = FireScreen.selectedLinkColor;
		}
		else  
		{
			bgCol = bgColor;
			txtCol = color;
		}
		Vector lines = text.getFormatedText();
		int rowHeight = text.getRowHeight();
		g.setFont(text.getFont());
		for(int i =0 ;i<lines.size();++i)
		{
			String str = (String)lines.elementAt(i);
			if(bgCol!=null)
			{
				g.setColor(bgCol.intValue());
				g.fillRect(1,1+(i*(rowHeight+FString.LINE_DISTANCE)),text.getRowWidth(str),rowHeight);
			}
			g.setColor(txtCol);
			g.drawString(str,2,1+(i*(rowHeight+FString.LINE_DISTANCE)),Graphics.TOP|Graphics.LEFT);
		}
	}
	
	public void validate()
	{
		int w = getWidth();
		setMinWidth(w);
		text.format(w,false);
		int textHeight = text.getHeight();
		setHeight(textHeight);
		if(getMinHeight()>getHeight())
		{
			setHeight(getMinHeight());
		}
	}
	
	public Object getId()
	{
		return id;
	}

	public void setId(Object id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text.getText();
	}

	public void setText(String text)
	{
		this.text.setText(text);
	}
	
	public void setFont(Font f)
	{
		this.text.setFont(f);
	}

	public boolean isOnPointer()
	{
		return onPointer;
	}

	public void setOnPointer(boolean onPointer)
	{
		this.onPointer = onPointer;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public Integer getBgColor()
	{
		return bgColor;
	}

	public void setBgColor(Integer bgColor)
	{
		this.bgColor = bgColor;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

}
