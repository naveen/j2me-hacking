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
 * Created on May 17, 2006
 */
package gr.bluevibe.fire.util;

import java.util.Vector;

import javax.microedition.lcdui.Font;

/**
 * FString is a utility class that splits a string into lines, according to a specified width.
 * @author padeler
 *
 */
public class FString
{
	public static final int LINE_DISTANCE = 0; // 0 pixels between lines.
	
	private Font font = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
	private String text="";
	
	private Vector formatedText = new Vector();
	
	private int width=0;
	private boolean formated=false;
	
	public FString()
	{
	}
	
	public void format(int width,boolean forced)
	{
		formatedText = new Vector();		
		
		this.width=width;
		
		if(text.equals("")) return;
		
		int minWidth = font.charWidth('W');
		
		if(width<minWidth)
		{ // den xoraei tpt, den emfanizoume tpt.
			formated=true;
			if(!forced)
			{
				this.width=0;
			}
			return;
		}
		
		
		// aferoume ta \n kai \t.
		String tmp = text.replace('\n',' ');
		tmp = tmp.replace('\t',' ');
		tmp = tmp.trim();
		//tmp+=" ";

		int textWidth = font.stringWidth(tmp);

		if(width>(textWidth+minWidth))
		{
			if(!forced)
			{
				this.width  = textWidth+minWidth + minWidth;
			}
		}
		
		int mw = width;
		
		int pos=0;
		int lastPos=-1;
		String line="",word;
		int length=0,tl=0;
		while((pos=text.indexOf(' ',lastPos+1))>-1)
		{
			if(lastPos!=-1)
			{
				word = text.substring(lastPos,pos);
			}
			else
			{
				word = text.substring(0,pos);	
			}
			tl = font.stringWidth(word);
			if(length + tl<mw)
			{
				length +=tl;
				line +=word;
			}
			else
			{
				if(line.length()>0)
				{
					formatedText.addElement(line);
				}
				line = word.trim();
				
				if(tl>mw)
				{ // h leksi einai poli megali , prepei anagastika na xoristi.
					int l=0,cw;
					word = word.trim();
					StringBuffer tmpWord= new StringBuffer(50);
					for(int i=0;i<word.length();++i)
					{
						char c =word.charAt(i);
						cw = font.charWidth(c);
						l += cw;
						if(l<mw)
						{
							tmpWord.append(c);
						}
						else
						{
							l = cw;
							formatedText.addElement(tmpWord.toString());
							tmpWord=new StringBuffer();
							tmpWord.append(c);
						}
					}
					line = tmpWord.toString();
					tl = font.stringWidth(line);
				}			
				length=tl;
			}
			lastPos = pos;
		}
		if(lastPos<(text.length()-1))
		{// get last word.
			if(lastPos==-1) lastPos=0;
			
			word = text.substring(lastPos);
			tl = font.stringWidth(word);
			if(length + tl<mw)
			{
				length +=tl;
				line +=word;
			}
			else
			{
				if(line.length()>0)
				{
					formatedText.addElement(line);
				}
				line = word.trim();
				
				if(tl>mw)
				{ // h leksi einai poli megali , prepei anagastika na xoristi.
					int l=0,cw;
					word = word.trim();
					StringBuffer tmpWord= new StringBuffer(50);
					for(int i=0;i<word.length();++i)
					{
						char c =word.charAt(i);
						cw = font.charWidth(c);
						l += cw;
						if(l<mw)
						{
							tmpWord.append(c);
						}
						else
						{
							l = cw;
							formatedText.addElement(tmpWord.toString());
							tmpWord=new StringBuffer();
							tmpWord.append(c);
						}
					}
					line = tmpWord.toString();
					tl = font.stringWidth(line);
				}				
				length=tl;
			}
		}
		if(line.length()>0)
		{
			formatedText.addElement(line);
		}
		formated=true;
	}

	public Font getFont()
	{
		return font;
	}
	
	public int getHeight()
	{
		int fontHeight = font.getHeight();
		if(formatedText.size()>0)
		{
			return (fontHeight+LINE_DISTANCE)*formatedText.size();
		}
		return fontHeight;
	}
	
	
	public int getRowHeight()
	{
		int fontHeight = font.getHeight();
		return fontHeight;
	}
	
	public int getRowWidth(String row)
	{
		return font.stringWidth(row);
	}

	public void setFont(Font font)
	{
		formated=false;
		formatedText=new Vector();
		this.font = font;
	}
	
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		formated=false;
		formatedText=new Vector();
		this.text = text;
	}

	public Vector getFormatedText()
	{
		return formatedText;
	}

	public boolean isFormated()
	{
		return formated;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}	
}