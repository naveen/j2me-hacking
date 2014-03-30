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
 * Created on Sep 14, 2006
 *
 */
package gr.bluevibe.fire.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

/**
 * FireIO is a utility class containing a set of I/O supporting methods.
 * It also acts as a cache for loaded images. If an image is loaded once and is later requested again, the old instance will be returned.
 * This minimizes the memory requirements of an application GUI, without extra efford on the developer side.  
 * @author padeler
 *
 */
public final class FireIO
{
	
	private static final boolean NORECORDSTORE=false;
	
	private static final int BUFSIZE=500;

	private static FireIO instance=null;
	
	private Hashtable localFileMap;
	private Hashtable buffer;
	private int bufferSize=0;
	private String downloadLocation=null;
	
	
	/**
	 * 
	 * @param localFileMap
	 * @param bufferSize
	 * @param downloadLocation
	 */
	private FireIO(Hashtable localFileMap, int bufferSize, String downloadLocation)
	{
		this.localFileMap = localFileMap;
		this.bufferSize = bufferSize;
		this.downloadLocation = downloadLocation;
		buffer = new Hashtable();
	}

	private FireIO() throws Exception
	{
		localFileMap = new Hashtable();
	}
	
	/**
	 * Initializes the FireIO singleton. All the parameters are used by the image loading methods in this class. 
	 * 
	 * @param localFileMap A developer provided map, for image files in the jar (or on a remote location)
	 * @param bufferSize The number of image instances to be kept in the image cache
	 * @param downloadLocation The location of the image repository. 
	 * If downloadLocation!=null and the image filename corresponding to the image key requested is not found, 
	 * the image is downloaded from the downloadLocation/filename. 
	 */
	public static void setup(Hashtable localFileMap,int bufferSize,String downloadLocation)
	{
		instance = new FireIO(localFileMap,bufferSize,downloadLocation);		
	}
	
	public static Vector list()
	{
		return instance.listInternal();
	}
	
	public static void deleteAllFiles()
	{
		Vector allFiles = list();
		for(int i =0 ;i<allFiles.size();++i)
		{
			String file = (String)allFiles.elementAt(i);
			deleteFile(file);
		}
	}
	
	private Vector listInternal()  
	{
		Vector res = new Vector();
		try{
			String []names = RecordStore.listRecordStores();
			if(names!=null)
			{
				for(int i =0 ;i<names.length;++i)
				{
					String fileName = names[i];
					res.addElement(fileName);
				}
			}
		}catch(Exception e)
		{
			System.out.println("FFS List Files Error: "+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean deleteFile(String file)
	{
		try{
			instance.deleteFileInternal(file);
			return true;
		}catch(Exception e)
		{
			System.out.println("Failed to delete file "+file);
			//e.printStackTrace();
		}
		return false;
	}
	
	private synchronized void deleteFileInternal(String file) throws Exception
	{
		RecordStore.deleteRecordStore(file);
	}
	
	
	int[] recordStoreSize(String name)
	{
		RecordStore rs =null;
		try{
			rs = RecordStore.openRecordStore(name,false);
			return new int[]{rs.getSize(),rs.getSizeAvailable()};
		}catch(Exception e)
		{
			System.out.println("Failed to get size for RecordStore "+name);
		}finally{
			try{if(rs!=null) rs.closeRecordStore();}catch(Throwable e){}
		}
		return new int[]{0,0};
	}
	
	public static int total()
	{
		String recordStores[] = RecordStore.listRecordStores();
		if(recordStores!=null)
		{
			int sum=0;
			for(int i =0 ;i<recordStores.length;++i)
			{
				sum+= instance.recordStoreSize(recordStores[i])[0];
			}
			return sum;
		}
		return 0;
	}
	
	public static int size(String file)
	{
		return instance.recordStoreSize(file)[0];
	}
	
	public static int free()
	{
		String recordStores[] = RecordStore.listRecordStores();
		if(recordStores!=null && recordStores.length>0)
		{
				return instance.recordStoreSize(recordStores[0])[1];
		}
		return 0;
	}	
	
	public static void write(String file, byte buffer[]) throws Exception
	{
		instance.writeInternal(file,buffer);
	}
	
	
	private void writeInternal(String file,byte[] buffer) throws Exception
	{
		RecordStore fr=null;
		try{
			fr = RecordStore.openRecordStore(file,true,RecordStore.AUTHMODE_PRIVATE,true);
			RecordEnumeration re = fr.enumerateRecords(null,null,false);
			if(re.hasNextElement())
			{
				int id = re.nextRecordId();
				fr.deleteRecord(id);	
			}
			fr.addRecord(buffer,0,buffer.length);
		}finally{
			 try{if(fr!=null)fr.closeRecordStore();}catch(Exception e){}
		}
	}
	
	public static byte []read(String f) throws Exception
	{
		return instance.readInternal(f);		
	}
	
	private byte []readInternal(String f) throws Exception
	{
		RecordStore fr=null;
		try{
			fr = RecordStore.openRecordStore(f,false,RecordStore.AUTHMODE_PRIVATE,false);
			RecordEnumeration re = fr.enumerateRecords(null,null,false);
			if(re.hasNextElement())
			{
				return re.nextRecord();
			}
			return null;
		}finally{
			 try{if(fr!=null)fr.closeRecordStore();}catch(Exception e){}
		}
	}

	public static Image getLocalImage(String id)
	{
		return instance.getLocalImageInternal(id);
	}
	
	private Image getLocalImageInternal(String id)
	{
//		System.out.println("Request local image "+id);
		if(buffer.size()>0)
		{
			Image img = (Image)buffer.get(id);
			if(img!=null) // cache hit
			{
				return img;
			}
		}
		
		String file = (String) localFileMap.get(id);
		if (file == null)
			return null;

		// not found in buffer, try record store.
		if(!NORECORDSTORE)
		{
			try{
				byte buf[] = readInternal(id);
				if(buf!=null)
				{
	//				System.out.println("File "+id+" found in record store");
					ByteArrayInputStream bin = new ByteArrayInputStream(buf);
					Image img = Image.createImage(bin);
					if(buffer.size()>=bufferSize)
					{
						buffer = new Hashtable();
					}
					buffer.put(id,img);
					return img;
				}
			}catch(Throwable e)
			{
				// file not found in record store.
			}
		}
//		 file not found in buffer or record store, try jar.
		Class c = this.getClass();
		InputStream is = c.getResourceAsStream("/" + file);
		try
		{
			Image img = Image.createImage(is);
			if(buffer.size()>=bufferSize)
			{
				buffer = new Hashtable();
			}
			buffer.put(id,img);
			return img;
		} catch (Exception e)
		{// load from local file failed.	
		} finally
		{
			if (is != null)
			{
				try{	is.close();	} catch (Throwable e)	{	}
			}
		}
		
		// try the web.
		if(downloadLocation!=null)
		{
			try{
				byte []res = makeRequest(downloadLocation+"/"+file,null,null);
				if(res!=null)
				{
					ByteArrayInputStream bin = new ByteArrayInputStream(res);
					Image img = Image.createImage(bin);
					if(buffer.size()>=bufferSize)
					{
						buffer = new Hashtable();
					}
					buffer.put(id,img);
					// try to save image in the record store for later use.
					System.out.println("Image downloaded "+res.length);
					try{
						if(!NORECORDSTORE)
						{
							writeInternal(id,res);
						}
					}catch(Throwable e)
					{
						System.out.println("Failed to store image.");
					}
					return img;
				}
			}catch(Throwable e)
			{
				e.printStackTrace();
				// download failed.
			} 
		}		
		// everything failed. just return null.
		System.out.println("Failed to load media with id " + id + " file: " + file);
		return null;
	}

	public static byte[] makeRequest(String url, Hashtable headerFields,byte[]data) throws IOException
	{
		System.out.println("Request to url "+url);
		HttpConnection conn = (HttpConnection)Connector.open(url,Connector.READ_WRITE);	
		conn.setRequestMethod(HttpConnection.POST);
		conn.setRequestProperty("Content-Disposition","form-data");
		conn.setRequestProperty("Content-Type","application/octet-stream");
	
		if(headerFields!=null)
		{
			Enumeration en = headerFields.keys();
			while(en.hasMoreElements())
			{
				String key = (String)en.nextElement();
				String val = (String)headerFields.get(key);
				conn.setRequestProperty(key,val);
			}
		}
		
		if(data!=null) // exoume na grapsoume kiolas.
		{
			OutputStream out = conn.openOutputStream();
			out.write(data);
			out.flush();
		}
		
		int code = conn.getResponseCode();
		
		if(code==HttpConnection.HTTP_OK)
		{
			InputStream in = conn.openInputStream();
			// prepei na diavasoume to payload.
			byte []res = new byte[BUFSIZE];
			byte temp[] = new byte[1]; // XXX voithitikos buffer gia to workaround gia ta nokia6680, 6681, n91 kai merika alla, to read() 
			int pos =0;                                                                                         // den doulevei sosta sto InputStream.
			byte c;
			while((in.read(temp,0,1))!=-1)
			{
				c=temp[0];
				res[pos] = (byte)(0x000000FF & c);
				pos++;
				if(pos==res.length)
				{ // resize array.
					byte t[] = new byte[res.length+BUFSIZE];
					System.arraycopy(res,0,t,0,pos);
					res = t;
				}
			}
			if(pos<res.length)
			{ // resize array.
				byte t[] = new byte[pos];
				System.arraycopy(res,0,t,0,pos);
				res = t;
			}			
			return res;
		}
		// if responce != HTTP_OK
		System.out.println("Server Responded with error: "+code);
		return null;
	}
}