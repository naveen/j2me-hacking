/*
 * Created by Javier Cancela
 * Copyright (C) 2007 hipoqih.com, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, If not, see <http://www.gnu.org/licenses/>.*/

package com.hipoqih.plugin.s60_2nd;

import com.hipoqih.plugin.*;
import com.hipoqih.plugin.UI.MainFormUI;
import com.hipoqih.plugin.s60_2nd.gps.BluetoothConnection;
import com.hipoqih.plugin.s60_2nd.gps.Coordinates;
import com.hipoqih.plugin.s60_2nd.gps.GPS;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Xavi
 *
 */
public class HipoqihMIDlet extends MIDlet implements CommandListener, MIDletExiter, Runnable
{
	private Command deviceSelection;
	private MainFormUI mainFormUI = null;
	private BluetoothConnection bt = new BluetoothConnection();
	private GPS gps = new GPS(bt); 
	private List listDevices = null;
	Thread thread = new Thread(this);

	public HipoqihMIDlet () throws Exception 
	{ 
		State.display = Display.getDisplay(this);
	}
	
	public void startApp() 
	{
		try 
		{
			// Initialize any exisitng security info
			// that might be stored in RMS stores.
			State.getInstance().loadConfiguration();
	    } 
		catch (Exception e) 
	    {
	      e.printStackTrace();
	    }
		
	    SplashScreen splash = new SplashScreen (this);
	    splash.setFullScreenMode(true);
	    State.display.setCurrent(splash);
	}
	
	public void exit()
	{
		destroyApp(false);
	    notifyDestroyed();
	}
	
	public void nextDisplay()
	{
		bt.searchDevices();
		listDevices = bt.getDevicesList();
		deviceSelection = new Command("Select", Command.SCREEN, 3);
		listDevices.addCommand(deviceSelection);
		listDevices.setCommandListener(this);
        State.display.setCurrent(listDevices);
	}

	public void notifyBTProblem(String message)
	{
		Alert alert = new Alert("Error");
		alert.setString(message);
		State.display.setCurrent(alert);
	}
	
	public void pauseApp() 
	{
	}
	 
	public void destroyApp(boolean unconditional) 
	{
		try
		{
			State.recordStore.closeRecordStore();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void commandAction(Command command, Displayable screen) 
	{
		if (command == deviceSelection)
		{
			MainFormUI.m = this;
	       	mainFormUI = new MainFormUI();
	       	bt.connectDevice(listDevices.getSelectedIndex());
	       	thread.start();
			State.display.setCurrent(mainFormUI);
		}
	}
	
	public void run()
	{
		while(true)
		{
			Coordinates coor = gps.getCoordinates();
			mainFormUI.setLocation(coor.getLatitude(), coor.getLongitude(), true);
		}
	}
}
