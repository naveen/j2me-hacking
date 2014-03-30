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

package com.hipoqih.plugin.UI;

import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;
import com.hipoqih.plugin.Web.*;
import com.hipoqih.plugin.*;

public class MainFormUI extends Form implements CommandListener
{
	public static MIDletExiter m;
	javax.microedition.lcdui.StringItem strComStatus = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.ImageItem img = new ImageItem("", null, ImageItem.LAYOUT_DEFAULT, "", ImageItem.PLAIN);
	javax.microedition.lcdui.Image imageOn;
	javax.microedition.lcdui.Image imageOff;
	javax.microedition.lcdui.Spacer spacer0 = new Spacer(0, 0);
	javax.microedition.lcdui.StringItem strLatitude = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strLongitude = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strLatitudeData = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strLongitudeData = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.Spacer spacer1 = new Spacer(0, 0);
	javax.microedition.lcdui.StringItem strFromUser = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strDistance = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strFromUserData = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strDistanceData = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.StringItem strAlert = new StringItem("", "", StringItem.PLAIN);
	javax.microedition.lcdui.Spacer spacer2 = new Spacer(0, 0);
	javax.microedition.lcdui.Command cmdConnect = new Command("Connect", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdDisconnect = new Command("Disconnect", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdMap = new Command("Map", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdSettings = new Command("Settings", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdShowLog = new Command("Log", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdZoomIn = new Command("Zoom In", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdZoomOut = new Command("Zoom out", Command.SCREEN, 1);
	javax.microedition.lcdui.Command cmdExitMap = new Command("Exit", Command.SCREEN, 1);
	
	private boolean isConnected = false;
	static public final javax.microedition.lcdui.Command cmdExit = new Command("Exit", Command.EXIT, 0);
	javax.microedition.lcdui.Image image1;
	javax.microedition.lcdui.Command cmdAbout = new Command("About Hipoqih", Command.SCREEN, 1);
	static public final javax.microedition.lcdui.Command cmdConfigurar = new Command("Properties", Command.SCREEN, 0);
	private WebTimerTask webTimerTask = new WebTimerTask();
	private UITimerTask uiTimerTask = new UITimerTask();
	private Timer timer = new Timer();
	private double latitude = 0;
	private double longitude = 0;
	private double lastSentLon = 0;
	private double lastSentLat = 0;
	boolean gpsConnected = false;
	
	public MainFormUI()
	{
		super("HipoqihPlugin");
		setCommandListener(this);
		try
		{
			initModel();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	protected void initModel() throws Exception 
	{
		strComStatus.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		strComStatus.setLabel("");
		strComStatus.setText("Disconnected");
		strComStatus.setLayout(Item.LAYOUT_2);
		strComStatus.setPreferredSize(-1, -1);
		this.append(strComStatus);
		imageOff = Image.createImage("/off.bmp");
		imageOn = Image.createImage("/on.bmp");
		img.setImage(imageOff);
		img.setLabel("");
		img.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		img.setAltText("");
		this.append(img);
		spacer0.setPreferredSize(10, 10);
		spacer0.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(spacer0);
		strLatitude.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		strLatitude.setPreferredSize(80, -1);
		strLatitude.setLabel("");
		strLatitude.setText("Latitude");
		strLatitude.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		this.append(strLatitude);
		strLongitude.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		strLongitude.setPreferredSize(80, -1);
		strLongitude.setLabel("");
		strLongitude.setText("Longitude");
		strLongitude.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		this.append(strLongitude);
		strLatitudeData.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		strLatitudeData.setPreferredSize(80, -1);
		strLatitudeData.setLabel("");
		strLatitudeData.setText("Waiting...");
		strLatitudeData.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		this.append(strLatitudeData);
		strLongitudeData.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		strLongitudeData.setPreferredSize(80, -1);
		strLongitudeData.setLabel("");
		strLongitudeData.setText("Waiting...");
		strLongitudeData.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		this.append(strLongitudeData);
		spacer1.setPreferredSize(10, 10);
		spacer1.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(spacer1);
		strFromUser.setText("");
		strFromUser.setLabel("");
		strFromUser.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		strFromUser.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		this.append(strFromUser);
		strFromUserData.setText("User");
		strFromUserData.setLabel("");
		strFromUserData.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		strFromUserData.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		this.append(strFromUserData);
		strDistance.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		strDistance.setLabel("");
		strDistance.setText("From:  ");
		strDistance.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		this.append(strDistance);
		strDistanceData.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		strDistanceData.setLabel("");
		strDistanceData.setText("  x meters");
		strDistanceData.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
		this.append(strDistanceData);
		spacer2.setPreferredSize(10, 10);
		spacer2.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_BEFORE | Item.LAYOUT_2);
		this.append(spacer2);
		strAlert.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		strAlert.setLabel("");
		strAlert.setText("This is the alert message.");
		strAlert.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL));
		strAlert.setPreferredSize(-1, -1);
		this.append(strAlert);
		this.addCommand(cmdConnect);
		this.addCommand(cmdDisconnect);
		this.addCommand(cmdMap);
		this.addCommand(cmdSettings);
		this.addCommand(cmdShowLog);
		this.addCommand(cmdAbout);
		this.addCommand(cmdExit);		
		timer.schedule(uiTimerTask, 0, 2000);
	}
	
	public void commandAction(Command command, Displayable displayable)
    {
		// EXIT command
		if (command == cmdExit)
		{
			m.exit();
		}		
		else if ( command == cmdConnect )
		{
			if ( !isConnected )
			{
				try
				{
					if (connectToWeb())
					{
						uiTimerTask.cancel();
						webTimerTask = new WebTimerTask();
						timer.schedule(webTimerTask, 0, State.connectionPeriod);
						isConnected = true;
						strComStatus.setText("Connected");
						img.setImage(imageOn);
					}
				}
				catch(Exception ex)
				{
					System.out.println(Thread.currentThread().getName() + ": cmdConnect");
					System.out.println(ex.toString());
					ex.printStackTrace();
				}
			}
		}
		else if ( command == cmdDisconnect)
		{
			if (isConnected)
			{
				try
				{
					isConnected = false;
					HipoWeb.sendWebDisconnection();
					strComStatus.setText("Disconnected");
					img.setImage(imageOff);
					webTimerTask.cancel();
					uiTimerTask = new UITimerTask();
					timer.schedule(uiTimerTask, 0, 2000);
				}
				catch(Exception ex)
				{
					System.out.println(Thread.currentThread().getName() + ": cmdDisconnect");
					System.out.println(ex.toString());
					ex.printStackTrace();
				}
			}
		}
		else if (command == cmdMap)
		{
			showMap();
		}
		else if (command == cmdZoomIn)
		{
			if (State.zoom > 500)
			{
				State.zoom /= 2;
				showMap();
			}
		}
		else if (command == cmdZoomOut)
		{
			if (State.zoom < 256000)
			{
				State.zoom *= 2;
				showMap();
			}
		}
		else if(command == cmdExitMap)
		{
			State.display.setCurrent(this);
		}
		else if (command == cmdSettings)
		{
			SettingsFormUI set = new SettingsFormUI(this);
			State.display.setCurrent(set);
		}
		else if (command == cmdShowLog)
		{
			strAlert.setText(State.getLog());
		}
    }
	
	public void setLocation(double lat, double lon, boolean connected)
	{
		latitude = lat;
		longitude = lon;
		gpsConnected = connected;
	}
	
	private void showMap()
	{
		if (strLatitudeData.getText().trim().length() == 0 || strLongitudeData.getText().trim().length() == 0)
		{
			Alert alertScreen = new Alert("Error");
			alertScreen.setString("There are no position coordinates");
			alertScreen.setTimeout(Alert.FOREVER);
			State.display.setCurrent(alertScreen);
		}
		try
		{
			MapCanvas map = new MapCanvas();
			int width = map.getWidth();
			int height = map.getHeight();
			Image image = getMap(height, width);
			map.setMap(image);
			map.addCommand(cmdZoomIn);
			map.addCommand(cmdZoomOut);
			map.addCommand(cmdExitMap);
			map.setCommandListener(this);
			State.display.setCurrent(map);
		}
		catch(IOException ioe)
		{
			System.out.println("IOEx:" + ioe.getMessage());
			ioe.printStackTrace();
		}
		catch(Exception ex)
		{
			System.out.println("Ex:" + ex.getMessage());
			ex.printStackTrace();
		}	
	}
	
	private boolean connectToWeb()
	{
		boolean result = false; 
		try
		{
			int webResult =  HipoWeb.sendWebReg(State.user, State.password);
			Alert alertScreen;
			switch(webResult)
			{
			case WebResult.BAD_RESPONSE:
			case WebResult.ALERT_ERROR:
				alertScreen = new Alert("Error");
				alertScreen.setString("There was an error accessing hipoqih");
				alertScreen.setTimeout(Alert.FOREVER);
				State.display.setCurrent(alertScreen);
				break;
			case WebResult.CODE_ERROR:
				alertScreen = new Alert("Error");
				alertScreen.setString("Wrong user or password");
				alertScreen.setTimeout(Alert.FOREVER);
				State.display.setCurrent(alertScreen);
				break;
			case WebResult.UNKNOWN_MESSAGE_TYPE:
				alertScreen = new Alert("Error");
				alertScreen.setString("There was an error accessing hipoqih");
				alertScreen.setTimeout(Alert.FOREVER);
				State.display.setCurrent(alertScreen);
				break;
			case WebResult.ALERT_OK:
				if (HipoAlert.IsPositional)
				{
					Date hoy = new Date();
					String textoAviso = Tools.fechaToString(hoy.getTime()) + "\n" +
								HipoAlert.Login + " is at " + HipoAlert.Distance + " meters."; 
					strAlert.setText(textoAviso);
				}
				else
				{
					Date hoy = new Date();
					String textoAviso = Tools.fechaToString(hoy.getTime()) + "\n" +
										HipoAlert.Text;
					strFromUserData.setText(HipoAlert.Login);
					strDistanceData.setText(HipoAlert.Distance +  " meters");
					strAlert.setText(textoAviso);
				}
				result = true;
				break;
			case WebResult.CODE_OK:
				result = true;
				break;
			}
		}
		catch (IOException ex)
		{
			strAlert.setText("error");
		}
		
		return result;
	}
	
	class WebTimerTask extends TimerTask
	{
		public void run()
		{
			try
			{
				sendPosition();
			}
			catch(Exception ex)
			{
				System.out.println(Thread.currentThread().getName() + ": sendPosition");
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
   	 	}
    }
	
	class UITimerTask extends TimerTask
	{
		public void run()
		{
			try
			{
				updateCoorUI();
			}
			catch(Exception ex)
			{
				System.out.println(Thread.currentThread().getName() + ": updateCoorUI");
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
  	 	}
	}
	
	private void updateCoorUI()
	{
		if (gpsConnected)
		{
			String lat = Double.toString(latitude);
			if (lat.length() > 8)
				lat = lat.substring(0, 8);
			String lon = Double.toString(longitude);
			if (lon.length() > 8)
				lon = lon.substring(0, 8);
			strLatitudeData.setText(lat);
			strLongitudeData.setText(lon);
		}
		else
		{
			strLatitudeData.setText("Waiting...");
			strLongitudeData.setText("Waiting...");
		}
	}
	
	
    private void sendPosition()
    {
    	if (!State.connected)
    	{
    		connectToWeb();
    	}
    	if (State.positionSource.equals("GPS"))
    	{
    		if (gpsConnected)
    		{
    			updateCoorUI();
    			if (Math.abs(latitude - lastSentLat) > 0.00001 || Math.abs(longitude - lastSentLon) > 0.00001)
    			{
    				try
    				{
    					HipoWeb.sendWebPos(strLatitudeData.getText(), strLongitudeData.getText());
    				}
    				catch(IOException ioe)
    				{
    					isConnected = false;
    					img.setImage(imageOff);
    					System.out.println(ioe.getMessage());
    					ioe.printStackTrace();
    				}
    			}
    		}
    		else
    		{
    			strLatitudeData.setText("Waiting...");
    			strLongitudeData.setText("Waiting...");
    		}
    	}
    }
    
    private Image getMap(int height, int width) throws IOException
    {
   		double longitude = Double.parseDouble(strLongitudeData.getText());
   		double latitude = Double.parseDouble(strLatitudeData.getText());

    	double multipliedLon = longitude * 1000000;
    	if (multipliedLon < 0)
    		multipliedLon = multipliedLon + 1073741824 + 1073741824 + 1073741824 + 1073741824;
    	multipliedLon = Math.floor(multipliedLon);
    	
    	double multipliedLat = latitude * 1000000;
    	if (multipliedLat < 0)
    		multipliedLat = multipliedLat + 1073741824 + 1073741824 + 1073741824 + 1073741824;
    	multipliedLat = Math.floor(multipliedLat);
    	
    	String urlLatitude = Long.toString((new Double(multipliedLat)).longValue());
    	String urlLongitude = Long.toString((new Double(multipliedLon)).longValue());
    	
    	String url = "http://maps.google.com/mapdata?Point=b&Point.latitude_e6=" + urlLatitude + 
    				"&Point.longitude_e6=" + urlLongitude + 
    				"&Point.iconid=15&Point=e&latitude_e6=" + urlLatitude + 
    				"&longitude_e6=" + urlLongitude + 
    				"&zm=" + Integer.toString(State.zoom) + "&w=" + width + "&h=" + height + "&cc=EN&min_priority=2";
    	return HipoWeb.sendWebRequestImage(url);
    }
}
