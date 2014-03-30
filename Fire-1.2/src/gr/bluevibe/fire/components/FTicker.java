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


import gr.bluevibe.fire.displayables.FireScreen;

import javax.microedition.lcdui.Graphics;
/**
 * FTicket implements j2me ticker functionallity for Panels
 *  
 * @author padeler
 *
 */
public class FTicker extends Component
{
	private String text="";
	
	private int totalWidth;
	private int pos,step;
	/**
	 * Creates a ticker with the given String.
	 * @param t the scrolling String. 
	 */
	public FTicker(String t)
	{
		setText(t);
	}

	public FTicker()
	{}

	public String getText()
	{
		return text;
	}


	/**
	 * Set the text of the ticker.
	 * @param t
	 */
	public void setText(String t)
	{
		if(t!=null)
		{
			text=  t.replace('\n',' ');
			text = text.replace('\t',' ');
		}
		else
		{
			text="";
		}
	}
	
	public boolean isAnimated()
	{
		return true;
	}
	
	public void paint(Graphics g)
	{
//		g.setColor(0xFFFF0000);
//		g.fillRect(0,0,getWidth(),getHeight());
		g.setFont(FireScreen.defaultTickerFont);
		g.setColor(FireScreen.defaultTickerColor);
		g.drawString(text,pos,0,Graphics.TOP|Graphics.LEFT);
		
	}
	
	int count=0;
	int max = 1;
	
	public boolean clock()
	{
		count++;
		if(count>max)
		{
			count=0;
			pos-=step;
			if(pos<-totalWidth) pos = getWidth();
			return true;
		}
		return false;
	}
	
	public void validate()
	{
		int strW = FireScreen.defaultTickerFont.stringWidth(text);
		totalWidth = strW;
		step = (5*getWidth())/100;
		pos = getWidth();
	}
	

}
