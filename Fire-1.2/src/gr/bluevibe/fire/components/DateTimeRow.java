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
 * Created on Sep 20, 2006
 *
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.FString;

import java.util.Calendar;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class DateTimeRow extends Component
{
	private static final int DAY=0;
	private static final int MONTH=1;
	private static final int YEAR=2;
	private static final int HOUR=3;
	private static final int MINUTE=4;
	
	private static final int SPACER=5;
	private static final int PADLEFT=3;
	private static final int PADTOP=3;
	

	
	private boolean showTime=false;
	private Calendar date;
	private FString label=null;
	
	private int pointer=DAY;
	
	
	private int dayWidth=0;
	private int monthWidth=0;
	private int yearWidth=0;
	private int hourWidth=0;
	private int minuteWidth=0;
	


	public DateTimeRow(Calendar date,boolean showTime)
	{
		this.date = date;
		this.showTime=showTime;
	}
	
	public void paint(Graphics g)
	{

		int strHeight = FireScreen.defaultTickerFont.getHeight();

		int xpos = PADLEFT;
		int ypos = PADTOP;
		int selColor = FireScreen.defaultPointerColor;
		if(FireScreen.selectedLinkBgColor!=null) selColor= FireScreen.selectedLinkBgColor.intValue();
		
		if(label!=null)
		{
			g.setFont(label.getFont());
			g.setColor(0x00000000);
			Vector lines = label.getFormatedText();
			int rowHeight = label.getRowHeight();
			
			for(int i =0 ;i<lines.size();++i)
			{
				String str = (String)lines.elementAt(i);
				g.drawString(str,xpos,ypos+(i*(rowHeight+FString.LINE_DISTANCE)),Graphics.TOP|Graphics.LEFT);
			}
			
			ypos+=label.getHeight()+PADTOP;
		}	

		g.setFont(FireScreen.defaultTickerFont);
		// day
		g.setColor(0xFFFFFFFF);
		g.fillRect(xpos, ypos, dayWidth, strHeight);
		if (pointer == DAY)
			g.setColor(selColor);
		else
			g.setColor(0x00000000);

		int d = date.get(Calendar.DAY_OF_MONTH)+1;
		String day;
		if(d<10) day = "0"+d;
		else day = d+"";
		g.drawString(day , xpos + 1, ypos + 1, Graphics.TOP | Graphics.LEFT);
		g.setColor(0x00000000);
		g.drawRect(xpos, ypos, dayWidth, strHeight);

		
		xpos += dayWidth + 2;
		g.drawString("/", xpos , ypos + 1, Graphics.TOP | Graphics.LEFT);
		xpos+=SPACER;
		
		// month
		g.setColor(0xFFFFFFFF);
		g.fillRect(xpos, ypos, monthWidth,  strHeight);
		if (pointer == MONTH)
			g.setColor(selColor);
		else
			g.setColor(0x00000000);
		int m = date.get(Calendar.MONTH)+1;
		String month;
		if(m<10) month = "0"+m;
		else month= m+"";
		g.drawString(month, xpos + 1, ypos + 1, Graphics.TOP | Graphics.LEFT);
		g.drawRect(xpos, ypos, monthWidth,  strHeight);

		xpos += monthWidth + 2;
		g.drawString("/", xpos , ypos + 1, Graphics.TOP | Graphics.LEFT);
		xpos+=SPACER;
		
		// year
		g.setColor(0xFFFFFFFF);
		g.fillRect(xpos, ypos, yearWidth,  strHeight);
		if (pointer == YEAR)
			g.setColor(selColor);
		else
			g.setColor(0x00000000);
		g.drawString(date.get(Calendar.YEAR) + "", xpos + 1, ypos + 1, Graphics.TOP | Graphics.LEFT);
		g.drawRect(xpos, ypos, yearWidth,  strHeight);

		xpos += yearWidth ;

		if (showTime)
		{
			xpos+=SPACER+ SPACER;
			int h = 0;
			m = 0;
			
			try
			{
				h = date.get(Calendar.HOUR_OF_DAY);
				m = date.get(Calendar.MINUTE);
			} catch (Throwable e)
			{
				try
				{
					date.set(Calendar.HOUR_OF_DAY, 0);
					date.set(Calendar.MINUTE, 0);
				} catch (Throwable ee)
				{
				}
			}
			
			String hour;
			if(h<10) hour = "0"+h;
			else hour = h+"";

			// hour
			g.setColor(0xFFFFFFFF);
			g.fillRect(xpos, ypos, hourWidth,  strHeight);
			if (pointer == HOUR)
				g.setColor(selColor);
			else
				g.setColor(0x00000000);
			g.drawString(hour, xpos + 1, ypos + 1, Graphics.TOP | Graphics.LEFT);
			g.drawRect(xpos, ypos, hourWidth,  strHeight);

			xpos += hourWidth+2 ;
			g.drawString(":", xpos , ypos + 1, Graphics.TOP | Graphics.LEFT);
			xpos+=SPACER;
			
			String min;
			if(m<10) min = "0"+m;
			else min = m+"";
			
			// minute
			g.setColor(0xFFFFFFFF);
			g.fillRect(xpos, ypos, minuteWidth,  strHeight);
			if (pointer == MINUTE)
				g.setColor(selColor);
			else
				g.setColor(0x00000000);
			g.drawString(min, xpos + 1, ypos + 1, Graphics.TOP | Graphics.LEFT);
			g.drawRect(xpos, ypos, minuteWidth,  strHeight);

			xpos += minuteWidth;
		}

		if (isSelected())
		{
			if (FireScreen.selectedLinkBgColor != null)
			{
				g.setColor(FireScreen.selectedLinkBgColor.intValue());
			}
			else
			{
				g.setColor(FireScreen.linkColor);
			}
			g.drawRect(PADLEFT, ypos, xpos-PADLEFT, strHeight);
		}
	}
	
	public boolean pointerEvent(int x, int y)
	{
		int xpos= PADLEFT + dayWidth;
		
		if(x<xpos)
		{
			if(pointer!=DAY) pointer=DAY;
			else return keyEvent(Canvas.FIRE);
			return true;
		}
		xpos+=SPACER+monthWidth;
		if(x<xpos)
		{
			if(pointer!=MONTH) pointer=MONTH;
			else return keyEvent(Canvas.FIRE);
			return true;
		}
		xpos+=SPACER+yearWidth;
		if(x<xpos)
		{
			if(pointer!=YEAR) pointer=YEAR;
			else return keyEvent(Canvas.FIRE);
			return true;
		}
		if(showTime)
		{
			xpos+=SPACER+SPACER+hourWidth;
			if(x<xpos)
			{
				if(pointer!=HOUR) pointer=HOUR;
				else return keyEvent(Canvas.FIRE);
				return true;
			}
			xpos+=SPACER+minuteWidth;
			if(x<xpos)
			{
				if(pointer!=MINUTE) pointer=MINUTE;
				else return keyEvent(Canvas.FIRE);
				return true;
			}
		}
		return false;
	}
	
	
	public boolean keyEvent(int key)
	{
		if(key==Canvas.FIRE)
		{
			switch (pointer)
			{
				case DAY:
					int day = date.get(Calendar.DAY_OF_MONTH);
					day++;
					day %=31;
					try{date.set(Calendar.DAY_OF_MONTH,day);}catch(Throwable e){ // o minas den exei 31.
						day=0;
						date.set(Calendar.DAY_OF_MONTH,day);
					}
				break;
				case MONTH:
					int m = date.get(Calendar.MONTH);
					m++;
					m%=12;
					date.set(Calendar.MONTH,m);
				break;
				case YEAR:
					int y = date.get(Calendar.YEAR);
					y++;
					if(y>2010) y = 2006;
					date.set(Calendar.YEAR,y);
				break;
				case HOUR:
					int h = date.get(Calendar.HOUR_OF_DAY);
					h++;
					h %= 24;
					date.set(Calendar.HOUR_OF_DAY,h);
				break;
				case MINUTE:
					int min = date.get(Calendar.MINUTE);
					min++;
					min %=60;
					date.set(Calendar.MINUTE,min);
				break;
				default:
					break;
			}
			return true;
		}
		else if(key==Canvas.UP || key==Canvas.DOWN)
		{
			setSelected(!isSelected());
			return isSelected();
		}
		else if(key==Canvas.LEFT)
		{
			int t = MINUTE;
			if(!showTime) t =YEAR;
			pointer--;
			if(pointer<0) pointer=t;
			return true;
		}
		else if(key==Canvas.RIGHT)
		{
			int mod = 5;
			if(!showTime) mod =3;
			pointer++;
			pointer %= mod;
			return true;
		}
		return false; // any other key exits focus
	}
	
	public void validate()
	{
		int w = getWidth();
		int h =  (FireScreen.defaultTickerFont.getHeight()) + (2*PADTOP);
		if(label!=null)
		{
			label.format(w,false);
			h+=label.getHeight()+PADTOP;
		}
		if(h<getMinHeight()) h = getMinHeight();
		setHeight(h);
				
		Font f = FireScreen.defaultTickerFont;
		
		dayWidth = f.stringWidth("33")+2;
		monthWidth = f.stringWidth("22")+2;
		yearWidth = f.stringWidth("20088")+2;
		hourWidth = f.stringWidth("44")+2;
		minuteWidth = f.stringWidth("66")+2;
		pointer = DAY;
	}
	
	public boolean isTraversable()
	{
		return true;
	}

	public Calendar getDate()
	{
		return date;
	}

	public void setDate(Calendar date)
	{
		this.date = date;
		fireValidateEvent();
	}

	public boolean isShowTime()
	{
		return showTime;
	}

	public void setShowTime(boolean showTime)
	{
		this.showTime = showTime;
		fireValidateEvent();
	}

	public String getLabel()
	{
		return label.getText();
	}

	public void setLabel(String label)
	{
		this.label = new FString();
		this.label.setText(label);
		this.label.setFont(FireScreen.defaultLabelFont);
		fireValidateEvent();
	}
}
