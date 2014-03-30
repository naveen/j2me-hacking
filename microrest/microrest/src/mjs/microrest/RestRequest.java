/*

	MicroRest - Rest Library for J2ME

	Copyright (c) 2007 Mary Jane Soft - Marlon J. Manrique
	
	http://mjs.darkgreenmedia.com
	http://marlonj.darkgreenmedia.com

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License as published by the Free Software Foundation; either
	version 2.1 of the License, or (at your option) any later version.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	Lesser General Public License for more details.

	You should have received a copy of the GNU Lesser General
	Public License along with this library; if not, write to the
	Free Software Foundation, Inc., 59 Temple Place, Suite 330,
	Boston, MA  02111-1307  USA

	$Id: RestRequest.java 228 2007-06-05 19:39:32Z marlonj $
	
*/

package mjs.microrest;

import com.j2medeveloper.util.UrlUtils;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * REST Request
 *
 * Contains all the elements requiered to perform a rest request, the service url, 
 * and parameters, it returns a RestResponse
 */
public class RestRequest
{
	/**
	 * GET method to the request
	 */
	public final static int GET = 1;
	
	/**
	 * POST method to the request
	 */
	public final static int POST = 2;
	
	/**
	 * The url of the service
	 */
	private String serviceURL;
	
	/**
	 * The parameter of the request
	 */
	private Hashtable parameters;
	
	/**
	 * Method to perform the request
	 */	
	private int method;
	
	/**
	 * Result Listener
	 */
	private RestResultListener resultListener;
	
	/**
	 * Create a request to the given service
	 *
	 * @param serviceURL The url of the service
	 */	
	public RestRequest(String serviceURL)
	{
		this(serviceURL,RestRequest.GET);
	}
	
	/**
	 * Create a request to the given service
	 *
	 * @param serviceURL The url of the service
	 * @param method Request method
	 */	
	public RestRequest(String serviceURL, int method)
	{
		this.serviceURL = serviceURL;		
		parameters = new Hashtable();
		
		// Set Method
		setMethod(method);
	}
	
	/**
	 * Change the request method to GET or POST
	 *
	 * @param method GET or POST method
	 */
	public void setMethod(int method)
	{
		if(method < GET || method > POST)
			throw new IllegalArgumentException("Method not valid : " + method);
			
		this.method = method;
	}

	/**
	 * Set the parameter of the request
	 *
	 * @param name Parameter name
	 * @param value Parameter value
	 */
	public void setParameter(String name, String value)
	{
		parameters.put(name,value);
	}
	
	/**
	 * Send the request and wait for the response
	 *
	 * @return The response
	 */
	public RestResponse sendAndWait() throws RestException
	{
		// Send the request in the same thread
		return sendRequest();
	}
	
	/**
	 * Send the request in another thread
	 */
	public void send() throws RestException
	{
		// Check if someone is listening
		if(resultListener == null)
			throw new RestException("No result listener registered");
	
		// Create a new Thread
		new Thread()
		{
			public void run()
			{				
				try
				{
					// Send the request in another thread
					sendAndWait();
				}
				catch(Exception e)
				{
					// Report the error to the listener
					resultListener.reportError(RestRequest.this,e.getMessage());
				}
			}
		}
		.start();
	}
	
	/**
	 * Sends the request to the server
	 *
	 * @return The response parse
	 */
	private RestResponse sendRequest() throws RestException
	{
		// The response
		RestResponse restResponse = null;
		
		// Get the complete URL with parameters if any		
		StringBuffer urlBuffer = new StringBuffer(serviceURL);
		
		// Add the parameters to the url if the method is GET
		if(method == GET)
			urlBuffer.append(getParameters());
		
		try
		{
			// Create the connection
			HttpConnection connection = 
				(HttpConnection) Connector.open(urlBuffer.toString());
				
			// If method is post send the parameters in the stream
			if(method == POST)
				sendParameters(connection);
				
			// Get the server response
			int responseCode = connection.getResponseCode();
		
			// Check if the response is OK
			if(responseCode != HttpConnection.HTTP_OK)
				throw new RestException("HTTP response (" 
					+ responseCode + ") " + connection.getResponseMessage());
				
			// Grab the response
			restResponse = 	new RestResponse(connection.openInputStream());
		
			// If a listener is registered, a result is available
			if(resultListener != null)
				resultListener.resultAvailable(this,restResponse,
					restResponse.getResult());
		}
		catch(IOException e)
		{
			throw new RestException(e.getMessage());
		}
		
		return restResponse;
	}
	
	/**
	 * Create a url query with the parameters of the request
	 *
	 * @return query with the parameters
	 */	
	private String getParameters()
	{
		// Is the first parameter
		boolean first = true;
		
		// The query
		StringBuffer params = new StringBuffer();
		
		// If any parameter add query
		if(method == GET && parameters.size() > 0)
			params.append("?");
			
		// For each parameter add name and value
		for(Enumeration names = parameters.keys(); names.hasMoreElements(); )
		{
			// Separate parameters with &
			if(!first)
				params.append("&");
			else
				first = false;

			// Get parameter values					
			String name = (String) names.nextElement();
			String value = (String) parameters.get(name);
				
			// Add the parameter to the url
			params.append(UrlUtils.encodeURL(name));
			params.append("=");
			params.append(UrlUtils.encodeURL(value));
		}

		// Return the query		
		return params.toString();
	}	
	
	/**
	 * Sets a listener for RestResults to produced by the request, replacing any 
	 * previous RestResultListener. A null reference is allowed and has the 
	 * effect of removing any existing listener
	 *
	 * @param resultListener The new listener or null
	 */
	public void setRestResultListener(RestResultListener resultListener)
	{
		this.resultListener = resultListener;
	}
	
	/**
	 * Send the parameters to the server using POST
	 *
	 * @param connection HttpConnection with the server
	 */
	private void sendParameters(HttpConnection connection) throws IOException
	{			
		// Content Type and Data to send 
		String contentType = "application/x-www-form-urlencoded";
		String rawData = getParameters();
		
		// Set connection  to POST
		connection.setRequestMethod(HttpConnection.POST);
		connection.setRequestProperty("Content-Type",contentType);		
		
		// Open the stream and send the data
		OutputStream outputStream = connection.openOutputStream();
		outputStream.write(rawData.getBytes());
		outputStream.flush();
	}
}
