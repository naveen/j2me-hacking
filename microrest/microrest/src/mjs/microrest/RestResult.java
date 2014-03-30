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

	$Id: RestResult.java 229 2007-06-05 19:50:46Z marlonj $
	
*/

package mjs.microrest;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class RestResult
{
	/**
	 * Result element name
	 */
	private String name;
	
	/**
	 * Result element text
	 */
	private String text;
	
	/**
	 * Result subelements
	 */
	private Vector childs;
	
	/**
	 * Result element attributes
	 */
	private Hashtable attributes;
	
	/**
	 * Create a result element with the specifie name 
	 *
	 * @param name The element name
	 */
	public RestResult(String name)
	{
		this.name = name;
		
		childs = new Vector();
		attributes = new Hashtable();
	}
	
	/**
	 * Return the name of the element
	 *
	 * @return Name of the element
	 */	
	public String getName()
	{
		return name;
	}
	
	/**
	 * Change the name of the element 
	 *
	 * @param name The ne name of the element
	 */	
	protected void setName(String name)
	{	
		this.name = name;
	}
	
	/**
	 * Return the text associated with the element
	 *
	 * @return Text of the element
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Updates the text of the element
	 *
	 * @param text The new text
	 */
	protected void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * Add a new subelement to the element
	 *
	 * @param child subelement
	 */	 
	protected void add(RestResult child)
	{
		childs.addElement(child);
	}
	
	/**
	 * Return the subelement with the specified name
	 *
	 * @param name Name of the subelement
	 * @return the subelement with the specified name, if not element is found 
	 *			null is return
	 */
	public RestResult getChild(String name)
	{
		// Check the name of each subelement
		for(Enumeration e=childs.elements(); e.hasMoreElements(); )
		{
			RestResult child = (RestResult) e.nextElement();
			
			// If the element has the specified name return it
			if(name.equals(child.getName()))
				return child;
		}
		
		// If no element is found
		return null;
	}
	
	/**
	 * Return the value of the attribute with the name specified, if no 
	 * attribute is found, return null
	 *
	 * @param name Attribute name
	 * @return Attribute values if the attribute is found, null otherwise
	 */
	public String getAttributeValue(String name)
	{
		return (String) attributes.get(name);
	}	
	
	/**
	 * Change the attribute value
	 *
	 * @param name Attribute name
	 * @param value Attribute value
	 */
	protected void setAttribute(String name, String value)
	{
		attributes.put(name,value);
	}
	
	/**
	 * Return the text value or attribute value specified by the xpath
	 *
	 * @param path xpath of the element
	 */
	public String get(String path) throws RestException
	{
		// Return the value
		return (String) getObject(path);
	}
  	
	/**
	 * Return the element specified by the xpath
	 *
	 * @param path xpath of the element
	 * @return the object representing the element on the path, could be a
	 * 		string if the request is an attribute or text, or can be a
	 * 		RestResult if the request is an element
	 */
	 private Object getObject(String path) throws RestException
	 {
	 	// Current element and value	
		Object value = null;				
		RestResult element = null;
		
		// Get the parts of the path
		String[] parts = split(path);		
		
		// Get the elements specified by the path
		for(int i=0; i<parts.length; i++)
		{
			// get the name 
			String pathName = parts[i];
			
			// If the part is text() 
			// return the text of the current element
			if(pathName.equals("text()"))
			{
				value = element.getText();
				break;
			}
			// If the part have the form @attribute 
			// return the attribute value in the current element
			else if(pathName.startsWith("@"))
			{
				value = element.getAttributeValue(pathName.substring(1));
				break;
			}
			else
			{
				// If the element is root
				// and has the name of the part set like current element
				if(element == null && pathName.equals(name))
					element = this;
				// If last element get all the childs with the pathName
				else if(i == parts.length - 1)
				{
					value = element.getChilds(pathName);
					break;
				}
				// Get a single element with the pathName
				// to continue searching					
				else
					element = element.getChild(pathName);
					
				// If the element is not found
				if(element == null)
					throw new RestException("Element " + pathName 
						+ " Not Found");
			}
		}
		
		// Return the value 
		return value;	
	}
	
	/**
	 * Split the path into an array eith the parts of the path
	 *
	 * @param path The xpath
	 * @return an array with the parts
	 */
	private String[] split(String path)
	{
		// Create an empty part vector
		Vector parts = new Vector();
		
		// Use the '/' like separator
		char separator = '/';
		
		// index of the separtor
		int index;
		// The first character is '/' avoid
		int oldIndex = 1; 
		
		// If separator is found
		while((index = path.indexOf(separator,oldIndex)) != -1)
		{
			// Add the part of the path
			parts.addElement(path.substring(oldIndex,index));
			oldIndex = index + 1;
		}
		
		// Add the last path element
		parts.addElement(path.substring(oldIndex));
		
		// Create an array an copy the parts
		String[] array = new String[parts.size()];
		parts.copyInto(array);
		
		// Return the array
		return array;
	}	

	/**
	 * Return a string representation of the element
	 *
	 * @return String representation of the element
	 */	
	public String toString()
	{
		return name + " : \n" 
			+ attributes + "\n" 
			+ text + "\n" 
			+ childs;
	}	
	
	/**
	 * Return the subelement with the specified name
	 *
	 * @param name Name of the subelement
	 * @return the subelement with the specified name, if not element is found
	 * null is return
	 */
	 public RestResult[] getChilds(String name)
	 {
	 	// Create a vector to add the childs 
	 	Vector vector = new Vector();
	 	
	 	// Check the name of each subelement
	 	for(Enumeration e=childs.elements(); e.hasMoreElements(); )
	 	{
	 		RestResult child = (RestResult) e.nextElement();
	 		
	 		// If the element has the specified name return it
	 		if(name.equals(child.getName()))
	 			vector.addElement(child);
	 	}
  		
  		// Copy the elements to the array
  		RestResult[] array = new RestResult[vector.size()];
  		vector.copyInto(array);
  		return array;
  	}  			
  	
	/**
	 * Return an array with the results available in the path specified
	 *
	 * @param path xpath of the element
	 * @return The array of result of the path especified	 
	 */
	 public RestResult[] getResults(String xpath) throws RestException
	 {
	 	return (RestResult[]) getObject(xpath);
	 }	  	
}
