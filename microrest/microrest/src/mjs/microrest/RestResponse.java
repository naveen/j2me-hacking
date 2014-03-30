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

	$Id: RestResponse.java 229 2007-06-05 19:50:46Z marlonj $
	
*/

package mjs.microrest;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

/**
 * Rest response 
 *
 * The rest response already parse to get the diferent elements
 */
public class RestResponse
{
	/**
	 * The root element of the response
	 */
	private RestResult rootElement;
	
	/**
	 * Create a response with the stream
	 *
	 * @param inputStream response stream
	 */
	public RestResponse(InputStream inputStream) throws RestException
	{
		processResponse(inputStream);
	}
	
	/**
	 * Process the response
	 *
	 * @param inputStream response stream
	 */
	private void processResponse(InputStream inputStream) throws RestException
	{
		try
		{
			// Create the XML parser
			KXmlParser parser = new KXmlParser();
			parser.setInput(new InputStreamReader(inputStream));
		
			// Process childs of the response
			rootElement = processChild(parser,null);
		}
		catch(Exception e)
		{
			throw new RestException(e.getMessage());
		}
	}
	
	/**
	 * Process a child of the parent element
	 *
	 * @param parser XML parser
	 * @param parent Parent element
	 */
	private RestResult processChild(KXmlParser parser, RestResult parent) 
		throws RestException
	{
		// Don't exit
		boolean exit = false;		
		
		// Current element
		RestResult element = null;

		do
		{
			try
			{
				// Get the next part of the document
				int eventType = parser.next();
			
				switch(eventType)
				{
					// Create the current element
					case XmlPullParser.START_TAG :
				
						// Create the element with the tag name
						element = new RestResult(parser.getName());
					
						// Add all the attributes
						int attributes = parser.getAttributeCount();
					
						for(int i=0; i<attributes; i++)
							element.setAttribute(parser.getAttributeName(i),
								parser.getAttributeValue(i));
							
						// If the element has parent add to the childs
						if(parent != null)
							parent.add(element);
					
						// Get the childs of this element
						processChild(parser,element);
					
						break;
					
					// Set the text of the parent 
					case XmlPullParser.TEXT : 
						parent.setText(parser.getText());
						break;														

					// If the element is close or the document is end
					// stop parsing					
					case XmlPullParser.END_TAG : 
					case XmlPullParser.END_DOCUMENT : 
						exit = true;
						break;
	    	 	}
			}
			catch(Exception e)
			{
				throw new RestException(e.getMessage());
			}
    	 	
		}
		while(!exit);
		
		// Return the current element
		return element;
	}
	
	/**
	 * Return the text value or attribute value specified by the xpath
	 *
	 * @param path xpath of the element
	 */
	public String get(String path) throws RestException
	{
		return rootElement.get(path);
	}
	
	/**
	 * Return a representation of the response
	 *
	 * @return The response representation like a String
	 */
	public String toString()
	{
		return rootElement.toString();
	}
	
	/**
	 * Returns the root element of the result
	 *
	 * @return root element of the result
	 */
	protected RestResult getResult()
	{
		return rootElement;
	}
	
	/**
	 * Return an array with the results available in the path specified
	 *
	 * @param path xpath of the element
	 * @return The array of result of the path especified
	 */
	public RestResult[] getResults(String xpath) throws RestException
	{
		return rootElement.getResults(xpath);
	}			
}
