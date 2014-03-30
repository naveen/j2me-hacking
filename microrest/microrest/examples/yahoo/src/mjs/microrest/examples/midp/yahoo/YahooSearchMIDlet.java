/*
	yahoo Search MIDlet

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

	$Id: YahooSearchMIDlet.java 228 2007-06-05 19:39:32Z marlonj $
	
*/

package mjs.microrest.examples.midp.yahoo;

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
import mjs.microrest.RestResult;
import mjs.microrest.RestResultListener;
import mjs.microrest.RestException;

public class YahooSearchMIDlet extends MIDlet 
	implements CommandListener, RestResultListener
{
	/**
	 * The device diplay
	 */
	private Display display;
	
	/**
	 * The query 
	 */
	private TextField queryField;
	
	/**
	 * Zip
	 */
	private TextField zipField;	
	
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
	 * The yahoo Application ID
	 */
	private String yahooApplicationID;
	
	/**
	 * Create the Echo MIDlet
	 */
	public YahooSearchMIDlet()
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
			zipField = new TextField("ZIP:","90210",50,TextField.NUMERIC);
			queryField = new TextField("Search:","bakery",50,TextField.ANY);
			resultItem = new StringItem("Result:","");
			
			// Create the commands
			exitCommand = new Command("Exit",Command.EXIT,1);
			requestCommand = new Command("Send Request",Command.OK,1);
			
			// Create the form and add the items
			Form form = new Form("Yahoo");
			form.append(queryField);
			form.append(zipField);
			form.append(resultItem);			
			form.addCommand(exitCommand);
			form.addCommand(requestCommand);
			form.setCommandListener(this);
			
			// Show the form
			display.setCurrent(form);
			
			// Grab the API key from the resource file
			ResourceBoundle resourceBoundle = 
				ResourceBoundle.getFromPropertyFile("yahoo",null);			
			yahooApplicationID = resourceBoundle.getString("yahoo.appid");
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
			sendRequest();
	}
	
	/**
	 * Send the request to the server
	 */
	public void sendRequest()
	{
		try
		{
			// Get the values to send 
			String query = queryField.getString();
			String zip = zipField.getString();
					
			// Create a rest request
			RestRequest restRequest = 
				new RestRequest("http://local.yahooapis.com/LocalSearchService/V3/localSearch");
								
			// Set the listener of the resutl
			restRequest.setRestResultListener(this);
						
			// Set the parameters
			restRequest.setParameter("appid",yahooApplicationID);
			restRequest.setParameter("query",query);
			restRequest.setParameter("zip",zip);
					
			// Send the request
			restRequest.send();				
		}
		catch(RestException e)
		{
			// Show the exception message
			resultItem.setText("Exception: " + e.getMessage());
		}		
	}
	
	/**
	 * Called when a result is available.
	 *
	 * @param restRequest The request object
	 * @param restResponse The response object
	 * @param restResult The result
	 */
	public void resultAvailable(RestRequest restRequest, 
		RestResponse restResponse, RestResult restResult)
	{
		try
		{
			// Create a string with the result from the server
			StringBuffer result = new StringBuffer();
				
			result.append("num results: " + 
				restResponse.get("/ResultSet/@totalResultsReturned"));
     
			RestResult[] results = restResponse.getResults("/ResultSet/Result");
			
			for(int i=0; i<results.length; i++)
			{
				result.append(results[i].get("/Result/@id") + "\n");
				result.append(results[i].get("/Result/Title/text()") + "\n");
				result.append(results[i].get("/Result/Address/text()") + "\n");
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

	/**
	 * Report an error in the request
	 *
	 * @param restRequest The request object
	 * @param message The message to report
	 */	
	public void reportError(RestRequest restRequest, String message)
	{
		// Show the error message
		resultItem.setText("Error: " + message);
	}
}
