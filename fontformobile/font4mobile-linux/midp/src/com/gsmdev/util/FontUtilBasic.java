package com.gsmdev.util;
/*
 *  font4mobile version 0.25
 * 
 *  (c) 2007 www.gsmdev.com
 * 
 *  visit www.gsmdev.com for updates
 * 
 *  For MIDP 1.0 & 2.0 compatible devices
 *  without transparency support 
 *  
 *  author: Adam Babol (adam.babol@gmail.com)
 *  
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class FontUtilBasic {
		
	private int imgHeight;
	
	private char[] chars;
	
	private byte[] charsWidth;
	
	private Image[] charImage;
	
	private static FontUtilBasic instance = null;
	
	private String lastStr="";
	
	private Image lastImg;
	
	/**
	 * Crate instance of class
	 */
	private FontUtilBasic() {		
	}
		
	/**
	 * Initialize data, load information about characters from file
	 * 
	 * @throws IOException 
	 */
	public static void initialize(String fileName) throws IOException {
		instance = new FontUtilBasic();
		Image img = null;
		InputStream is = instance.getClass().getResourceAsStream(fileName);
		DataInputStream dis = new DataInputStream(is);
		int len = dis.readInt();
		instance.chars = new char[len];
		instance.charImage = new Image[len];
		instance.charsWidth = new byte[len];				
		for ( int i = 0; i<len; i++ ) {			
			instance.chars[i] = dis.readChar();			
			instance.charsWidth[i] = dis.readByte();								
		}		
		int imgSize = dis.readInt();
		byte[] tmpBuffer = new byte[imgSize];
		dis.read(tmpBuffer, 0, imgSize);
		
		img = Image.createImage(tmpBuffer, 0, imgSize);
		if ( img != null ) {
			instance.imgHeight = img.getHeight();						
			int x = 0;							
			for (int i = 0; i<len; i++) {
				instance.charImage[i] = Image.createImage(instance.charsWidth[i], instance.imgHeight);
				Graphics g = instance.charImage[i].getGraphics();
				g.setClip(0, 0, instance.charsWidth[i], instance.imgHeight);			
				g.drawImage(img, -x, 0, Graphics.TOP|Graphics.LEFT);
				x += instance.charsWidth[i];
				g=null;
			}
		}
		tmpBuffer=null;
		img=null;
	}
	
	/**
	 * Returns instance of class
	 * 
	 * @return instance of class
	 */
	public static FontUtilBasic getInstance() {		
		return instance;
	}
	
	/**
	 * Render given text as image using font
	 * 
	 * @param text text to render
	 * @return rendered text as image
	 */
	public Image renderText(String text) {
		if(text.equals(lastStr))//Eliminate possible creation of already created image
		return lastImg;
		
		Image result;
		//calculate image size
		int width = 0;
		int txtLength = text.length();
		int[] indxs = new int[txtLength];		
		for ( int i = 0; i < txtLength; i++ ) {											
			int val = charIndex(text.charAt(i));
			indxs[i] = val;
			if ( val != -1 ) {
				width += charsWidth[val];				
			}
		}		
		result = Image.createImage(width, imgHeight);
		int resultWidth = result.getWidth();
		Graphics g = result.getGraphics();		
		int x = 0;
		for ( int i = 0; i < indxs.length; i++ ) {
			int val = indxs[i];			
			if ( val != -1 ) {				
				g.setClip(x, 0, charsWidth[val], imgHeight);			
				g.drawImage(charImage[val], x, 0, 20);
				g.setClip(0, 0, resultWidth, imgHeight);
				x += charsWidth[val];
			}
		}
		lastStr=text;
		lastImg=Image.createImage(result);
		return lastImg;
	}
			
	/**
	 * Returns position of char in CHAR_TAB array
	 * 
	 * @param ch character to find
	 * @return position of char in characters array
	 */
	private int charIndex(char ch) {
		int i = 0;
		boolean end = false;
		while ( !end && i < chars.length ) {			
			if ( chars[i] == ch ) {
				end = true;				
			} else {
				i++;
			}
		}		
		if (!end) i = -1;
		return i;
	}
	
	/**
	 * Gets font height
	 * 
	 * @return font height in pixels
	 */
	public int getFontHeight() {
		return imgHeight;
	}

	/**
	 * Gets the width of specified character
	 * 
	 * @param ch - the character to be measured 
	 * @return width of the given character
	 */
	public int charWidth(char ch) {		
		int index = charIndex(ch);
		if ( index != -1 ) {
			return charsWidth[index];
		} else return 0;		
	}
	
	/**
	 * Gets the total width for the specified string
	 * 	
	 * @param str the string to be measured
	 * @return the total width
	 */
	public int stringWidth(String str) {
		int sum = 0;
		return sum;
	}
		
}






