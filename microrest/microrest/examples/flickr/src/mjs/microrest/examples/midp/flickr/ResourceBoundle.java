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

	$Id: ResourceBoundle.java 226 2007-06-04 19:01:01Z marlonj $
	
*/

package mjs.microrest.examples.midp.flickr;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Resource Bundle 
 */

public class ResourceBoundle
{
	/**
	 * Resources 
	 */	 
	private Hashtable resources;

	/**
	 * Don't allow create empty resource boundles
	 */	 
	private ResourceBoundle()
	{
	}

	/**
	 * Create a resource boundle eith the given hashtable 
	 *
	 * @param resources Current resources
	 */
	private ResourceBoundle(Hashtable resources)
	{
		this.resources = resources;
	}

	/**
	 * Return the resource value associated with the specified key
	 *
	 * @return The value associated with the key
	 */	 
	public String getString(String key)
	{
		return (String) resources.get(key);
	}
	
	/**
	 * Load the resources form a file in the resource midlet directory
	 *
	 * @param baseName base name to load the resource file
	 * @param locale localization to append to the file
	 *
	 * @return The resource boundle or null if any exists
	 */	 
	public static ResourceBoundle getFromPropertyFile(String baseName, String locale)
	{
		// Language and country for the resources
		String language = null;
		String country = null;

		// If locale is specified
		if(locale != null)
		{
			// Locale specified like language-country
			int index = locale.indexOf("-");

			// Get the language
			if(index > -1)
				language = locale.substring(0,index);
			else
				index = -1;

			// Get the country
			country = locale.substring(index+1);
		}

		// Create an array with the possible names of the resource
		String[] resourceNames = new String[3];

		// If language is specified
		if(language != null)
		{
			// If the country is specified, create the resource name
			if(country != null)
				resourceNames[0] = "/" + baseName + "_" + language + "_" + country + ".properties";

			// create the resource name with language only
			resourceNames[1] = "/" + baseName + "_" + language + ".properties";
		}

		// create the resource name with only the base name
		resourceNames[2] = "/" + baseName + ".properties";
		
		// Try to load all the properties
		try
		{
			// Stream to load data
			InputStream in = null;

			// Check any resource and try to open, if can be open continue to load properties
			for(int i=0; i<resourceNames.length; i++)
			{
			// The resource name is available
				if(resourceNames[i] != null)
				{
					// Get the class loaded from the same package
					in = baseName.getClass().getResourceAsStream(resourceNames[i]);

					// If the resource can be open, don't open more
					if(in != null)
						break;
				}
			}

			// If the resource can be found return null
			if(in == null)
				return null;

			// Properties
			Hashtable resources = new Hashtable();

			// The current char
			int c;

			// The output to write the line
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			while(true)
			{
				// Read the char
				c = in.read();

				// If the char is line change or end of file
				if(c == '\n' || c == '\r' || c == -1)
				{
					// Convert the line to string
					String line = out.toString();

					// Get the = index
					int index = line.indexOf("=");

					// If the line don't contains = discard
					if(index != -1)
					{
						// Get the key and value
						String key = line.substring(0,index).trim();
						String value = line.substring(index+1).trim();

						// Add propertie
						resources.put(key,value);
					}

					// Reset the line
					out.reset();
				}
				else
					// Write the char read it to the line
					out.write(c);

				// If is the end of file, break the while
				if(c == -1)
					break;
			}

			// Return the resource boundle with the hashtable created
			return new ResourceBoundle(resources);
		}
		catch(Exception e)
		{
		}

		// If any error return null 
		return null;
	}

	/**
	 * Returns an enumeration of the keys.
	 *
	 * @return an enumeration of the keys.
	 */

	public Enumeration getKeys()
	{
		return resources.keys();
	}
}
