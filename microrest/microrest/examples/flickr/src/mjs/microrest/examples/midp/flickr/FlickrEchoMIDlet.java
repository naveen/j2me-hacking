/*
	Flickr Echo MIDlet

	Copyright (c) 2007 Mary Jane Soft - Marlon J. Manrique
	
	http://mjs.darkgreenmedia.com
	http://marlonj.darkgreenmedia.com

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

	$Id: FlickrEchoMIDlet.java 227 2007-06-05 07:57:30Z marlonj $
	
*/

package mjs.microrest.examples.midp.flickr;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import javax.microedition.midlet.MIDlet;

import mjs.microrest.RestRequest;
import mjs.microrest.RestResponse;
import mjs.microrest.RestException;

public class FlickrEchoMIDlet extends MIDlet implements CommandListener
{
	/**
	 * The device diplay
	 */
	private Display display;
	
	/**
	 * The value to send
	 */
	private TextField valueField;
	
	/**
	 * The result send by the server
	 */
	private StringItem resultItem;
	
	/**
	 * The exit command
	 */ 
	private Command exitCommand;
	
	/**
	 * The request command
	 */
	private Command requestCommand;
	
	/**
	 * The Flickr API key 
	 */
	private String flickrKey;
	
	/**
	 * Create the Echo MIDlet
	 */
	public FlickrEchoMIDlet()
	{		
	}
	
	/**
	 * Start the application, create the main form and show it
	 */
	public void startApp()
	{
		// If first time the display is not set
		if(display == null)
		{
			// Grab the midlet display
			display = Display.getDisplay(this);
			
			// Create the items
			valueField = new TextField("Value:","",50,TextField.ANY);
			resultItem = new StringItem("Result:","");
			
			// Create the commands
			exitCommand = new Command("Exit",Command.EXIT,1);
			requestCommand = new Command("Send Request",Command.OK,1);
			
			// Create the form and add the items
			Form form = new Form("Flickr");
			form.append(valueField);
			form.append(resultItem);			
			form.addCommand(exitCommand);
			form.addCommand(requestCommand);
			form.setCommandListener(this);
			
			// Show the form
			display.setCurrent(form);
			
			// Grab the API key from the resource file
			ResourceBoundle resourceBoundle = 
				ResourceBoundle.getFromPropertyFile("flickr",null);			
			flickrKey = resourceBoundle.getString("flickr.api_key");
		}
	}
	
	/**
	 * Pause the application
	 */	
	public void pauseApp()
	{
	}
	
	/**
	 * Destroy the application
	 *
	 * @param uncoditional Destroy the application
	 */	
	public void destroyApp(boolean unconditional)
	{		
	}
	
	/**
	 * And action is performed
	 *
	 * @param command The command
	 * @param displayable The current display
	 */
	public void commandAction(Command command, Displayable displayable)
	{
		// If exit close the application
		if(command == exitCommand)
			notifyDestroyed();
		// Else the request command was pressed
		else
			requestInfo();
	}
	
	/**
	 * Send the rest requets to the server using a new thread
	 */
	private void requestInfo()
	{
		// Create a new thread with the request
		new Thread()
		{
			public void run()
			{	
				sendRequest();	
			}
		}.start();
	}
	
	/**
	 * Send the request to the server
	 */
	public void sendRequest()
	{
		try
		{
			// Get the values to send 
			String value = valueField.getString();
				
			// Create a rest request
			RestRequest restRequest = 
				new RestRequest("http://api.flickr.com/services/rest/");
					
			// Set the parameters
			restRequest.setParameter("method","flickr.test.echo");
			restRequest.setParameter("value",value);
			restRequest.setParameter("api_key",flickrKey);
				
			// Send the request and wait for the response
			RestResponse restResponse = restRequest.sendAndWait();
				
			// Create a string with the result from the server
			StringBuffer result = new StringBuffer();
				
			// Get the stat value
			String stat = restResponse.get("/rsp/@stat");
				
			// If stat is ok , show the response
			if(stat == "ok")
			{
				result.append("Stat: " + 
					restResponse.get("/rsp/@stat") + "\n");
				result.append("Method: " + 
					restResponse.get("/rsp/method/text()") + "\n");
				result.append("Value: " + 
					restResponse.get("/rsp/value/text()") + "\n");
			}								
			// If stat is fail , show the error message
			else
			{
				result.append("Error Code: " + 
					restResponse.get("/rsp/err/@code") + "\n");
				result.append("Error: " + 
					restResponse.get("/rsp/err/@msg") + "\n");						
			}
				
			// Show the result
			resultItem.setText(result.toString());	
		}
		catch(RestException e)
		{
			// Show the exception message
			resultItem.setText("Exception: " + e.getMessage());
		}		
	}
}
