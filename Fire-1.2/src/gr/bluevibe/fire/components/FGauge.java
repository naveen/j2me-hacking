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
 * Created on May 20, 2006
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.displayables.FireScreen;

import javax.microedition.lcdui.Graphics;

public class FGauge extends Component
{
	private static int [][]ballPossitions =new int[][]{
		
		{3,0,0x00ea9608},
		{3,1, 0x00db7706},
		{2,3, 0x00d76f06},
		{1,3, 0x00a11c02},
		{-1,3, 0x00a52003},
		{-2,3, 0x00b13004},
		{-3,1, 0x00c44c05},
		{-3,0, 0x00c95406},
		{-3,-2, 0x00d76f06},
		{-2,-3, 0x00db7706},
		{0,-3,0x00ea9608},
		{1,-3, 0x00f2a909},
		{3,-2, 0x00f7b609},
		{3,-1, 0x00f7b609}
	};

	private int currentPos=0;
	private int radious=4,tail=5;
	
	public FGauge()
	{
	}
	
	public boolean isAnimated()
	{
		return true;
	}
	
	public boolean clock()
	{
		currentPos++;
		if (currentPos ==ballPossitions.length)
		{
			currentPos= 0;
		}
		return true;
	}
	
	public void paint(Graphics g)
	{
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(FireScreen.defaultFilledRowColor);
		g.fillRect(0,0,width,height);
		

		int start1,end1,start2=0,end2=0;
		int tailpos = currentPos-tail;
		if(tailpos<0)
		{
			tailpos = ballPossitions.length+tailpos;
		}

		if(tailpos<currentPos)
		{
			start1 = tailpos;
			end1 = currentPos;
		}
		else
		{
			start1 = tailpos;
			end1 =  ballPossitions.length;
			start2 = 0;
			end2= currentPos;
		}
		
		
		for(int i=start1;i<end1;++i)
		{	
			int x =ballPossitions[i][0]-radious + width/2 ;
			int y =ballPossitions[i][1]-radious + height/2;
			int dim = 2*radious;
			g.setColor(ballPossitions[i][2]);
			g.fillArc(x,y,dim,dim,0,360);
			g.setColor(0x00000000);
			//g.drawArc(x,y,dim,dim,0,360);
		}
		for(int i=start2;i<end2;++i)
		{	
			int x =ballPossitions[i][0]-radious + width/2 ;
			int y =ballPossitions[i][1]-radious + height/2;
			int dim = 2*radious;
			g.setColor(ballPossitions[i][2]);
			g.fillArc(x,y,dim,dim,0,360);
			g.setColor(0x00000000);
			//g.drawArc(x,y,dim,dim,0,360);
		}
		
		g.setColor(FireScreen.defaultBorderColor);
		g.drawRect(0,0,width,height);

	}
	
}
