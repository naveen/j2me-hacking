/*
 *
 * The contents of this file are subject to the GNU Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific terms governing rights and limitations
 * under the License.
 *
 * Developed by Michael Juntao Yuan 2002.
 *
 * */
package com.hipoqih.plugin;

import javax.microedition.lcdui.*;

public class SplashScreen extends Canvas implements Runnable
{
	private MIDletExiter m;
	Image img = null;
	boolean firstExec = true;
	
	public SplashScreen (MIDletExiter me) 
	{
		m = me;
		try
		{
			img=Image.createImage("/hipoqihSplash.PNG");
		}
		catch(Exception e){}
		Thread th=new Thread(this);
		th.start();
	}

	public void paint (Graphics g) 
	{
		int w = getWidth ();
		int h = getHeight ();

		try 
		{
			g.drawImage(img, w/2, h/2, Graphics.VCENTER | Graphics.HCENTER);
		} 
		catch (Exception e) 
		{
			g.drawString(e.getMessage(), w/2, h/2, Graphics.BASELINE | Graphics.HCENTER);
		}
		
	}
	
	public void run()
	{
		while(firstExec)
		{
			firstExec = false;
			
			try
			{
				Thread.sleep(3000);
			}
			catch(Exception e){}
		}
		m.nextDisplay();
	}
	
	public void keyPressed(int keyCode)
	{
		m.nextDisplay();
	}
}
