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
 * Created on Oct 22, 2005
 */
package gr.bluevibe.fire.displayables;


import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * SplashScreen is a utility class for displaying a Splash screen on application Start-up. 
 * It can display a logo image and a title on a White background, 
 * and an animated "Loading..." string. The animation will start when the FireScreen is initialised.
 * 
 */
public class SplashScreen extends Canvas
{
	private int count=0;
	private Font loadfont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
	private Font titlefont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
	
	private Image logo=null;
	private String title;
	public SplashScreen()
	{
		super();
	}
	
	public void paint(Graphics g)
	{
		int width;
		int height;
		width = getWidth();
		height =getHeight();

		g.setColor(0xFFFFFFFF);

		g.fillRect(0, 0, width, height);

		g.setColor(0x00000000);

		String txt = "Loading";
		int llen = loadfont.stringWidth(txt)/2;
		int tlen=0;
		int imx=0,imy=0;
		if(logo!=null)
		{
			imx = logo.getWidth()+4;
			imy = logo.getHeight();
		}
		if(title!=null)
		{
			tlen = titlefont.stringWidth(title)/2;
		}
		else
		{
			imx = logo.getWidth()/2;
		}
		int logoy = titlefont.getHeight(); 
		int voff = logoy;
		if(voff<imy) voff = imy;
		
		for(int i=0;i<(count%4);++i)
		{
			txt +=".";
		}
		count++;
		if(logo!=null)
		{
			g.drawImage(logo,width/2 -tlen-imx,height/2 -voff,Graphics.TOP|Graphics.LEFT);
		}
		if(title!=null)
		{
			g.setFont(titlefont);
			g.drawString(title, (width / 2)-tlen, height / 2 -voff/2-logoy/2, Graphics.TOP | Graphics.LEFT);
		}
		g.setFont(loadfont);
		g.drawString(txt, (width / 2)-llen, height / 2, Graphics.TOP | Graphics.LEFT);
	}
	public boolean clock()
	{
		return true;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Image getLogo()
	{
		return logo;
	}

	public void setLogo(Image logo)
	{
		this.logo = logo;
	}
}