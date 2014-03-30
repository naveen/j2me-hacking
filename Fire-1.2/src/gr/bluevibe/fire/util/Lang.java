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
 * Created on Sep 16, 2006
 *
 */
package gr.bluevibe.fire.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;

/**
 * The Lang class in an easy to use internationallization (i18n) implementation.
 * The default language is English. Every key is the english version of the required text. 
 * If the key string maps to to a translation then the translation is returned. Othewise the key itself is returned.
 * 
 * The class can load files with "key=value" pairs of strings on each line. 
 * The bundles can be loaded (using the FireIO class) either from a file inside the jar, or from a remote location 
 * or from a record in the midlets record store.
 *   
 * @author padeler
 *
 */
public final class Lang
{
	public static final String defaultLang = "EN";
	
	private static final String languageFile = "language_file";
	private static String lang="EN";

	private static Hashtable bundle=null;
	
	private Lang()
	{

	}
	
	/**
	 * Returns a string indicating the current language.
	 * @return
	 */
	public static String getLang()
	{
		return lang;
	}
	
	/**
	 * Sets a resource bundle.
	 * @param lang The string naming the language of the bundle
	 * @param resource The resource string.
	 * @throws Exception
	 */
	public static void setBundle(String lang,String resource) throws Exception
	{
		if(lang==null)
		{ // delete current bundle
			try{
				FireIO.deleteFile(languageFile);
			}catch(Exception e){}
			Lang.lang="EN";
			Lang.bundle= null;
			return;
		}
		
		if(resource==null) return;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		dout.writeUTF(lang);
		dout.writeUTF(resource);
		dout.flush();
		
		bundle =parseResource(resource);
		Lang.lang = lang;
		FireIO.write(languageFile,bout.toByteArray());
	}
	
	/**
	 * Loads the stored bundle.
	 *
	 */
	public static void loadBundle()
	{
		try{
				byte []buf = FireIO.read(languageFile);
				if(buf!=null)
				{
					DataInputStream din = new DataInputStream(new ByteArrayInputStream(buf));
					String language = din.readUTF();
					String resource= din.readUTF();
					bundle = parseResource(resource);
					lang = language; 
				}
			}catch(Throwable e)
			{
				System.out.println("Resource Bundle load failed. "+e.getMessage());
			}
	}
	
	
	private static Hashtable parseResource(String r) throws Exception
	{
		//System.out.println("Parsing Resource:\n"+r);
		Hashtable res = new Hashtable();
		int idx=0,next;
		int lidx;
		String line=null,key,val;
		try{
			while((next = r.indexOf("\n",idx))>-1)
			{
				// get each line			
				line = r.substring(idx,next).trim();
				idx = next+1;
	
				if(line.length()==0) continue;
				
				// split line in key value pair.
				lidx = line.indexOf("=");
				if(lidx==-1)
				{
					continue; // ignore line
				}
				key = line.substring(0,lidx).trim();
				val = line.substring(lidx+1).trim();
				res.put(key,val);
			}
			//get last line:
			line = r.substring(idx).trim();

			// split line in key value pair.
			lidx = line.indexOf("=");
			if(lidx==-1)
			{
//				System.out.println("Ignoring Resource line:\n "+line);
			}
			else
			{
				key = line.substring(0,lidx).trim();
				val = line.substring(lidx+1).trim();
				res.put(key,val);
			}
		}catch(Exception e)
		{
			throw new Exception("Resource parsing failed near line:\n"+line);
		}
		return res;
	}
	
	
	/**
	 * Returns the string translation that maps to the key, or the key itself if the translation was not found. 
	 * @param key
	 * @return A translation of key
	 */
	public static String get(String key)
	{
		String res=null;
		if(bundle!=null)
		{
			res = (String)bundle.get(key);
			if(res!=null)
			{
				return res;
			}
		}
		return key;
	}
}