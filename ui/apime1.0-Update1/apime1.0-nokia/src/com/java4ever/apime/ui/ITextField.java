/*
 * Apime v1.0 - Framework for j2me applications.
 *
 * Copyright (c) 2004 Carlos Araiz (caraiz@java4ever.com)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.java4ever.apime.ui;

import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.event.*;

/**
 * Campo de texto.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ITextField extends ITextComponent
{
	/**
	 * Velocidad de parpadeo del cursor (en ms.).
	 */
	int caret_blink;

	/**
	 * Color del cursor.
	 */
	int caret_color;

	/**
	 * Posición del cursor.
	 */
	int caret_position;

	// Variables de control.
	private CaretTimer caret_timer;
	private int maxlength;
	private boolean editing;
	private String password;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el campo de texto.
	 *
	 * @param text Texto
	 */
	public ITextField(String text)
	{
		this(text,Short.MAX_VALUE);
	}

	/**
	 * Crea el campo de texto.
	 *
	 * @param text Texto
	 * @param maxlength Número máximo de caracteres
	 */
	public ITextField(String text,int maxlength)
	{
		this(text,maxlength,false);
	}

	/**
	 * Crea el campo de texto.
	 *
	 * @param text Texto
	 * @param maxlength Número máximo de caracteres
	 */
	public ITextField(String text,int maxlength,boolean password)
	{
		super(text);
		//
		this.maxlength=maxlength;
		if (password) this.password="";
		setText(text,false);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia el texto colocando el cursor al inicio.
	 *
	 * @param text Nuevo texto
	 */
	public void setText(String text)
	{
		setText(text,true);
	}

	/**
	 * Cambia el texto.
	 *
	 * @param text Nuevo texto
	 * @param reset_caret True para poner el cursor al inicio
	 */
	protected void setText(String text,boolean reset_caret)
	{
		if (text.length()>maxlength) text=text.substring(0,maxlength);
		super.setText(text);
		if (reset_caret) setCaretPosition(0);
		updatePassword();
	}

	/**
	 * Velocidad de parpadeo del cursor.
	 *
	 * @param blink Velocidad en ms.
	 */
	public void setCaretBlink(int blink)
	{
		this.caret_blink=Math.max(0,blink);
		repaint();
	}

	/**
	 * Devuelve al velocidad de parpadeo del cursor.
	 */
	public int getCaretBlink()
	{
		return caret_blink;
	}

	/**
	 * Color del cursor.
	 *
	 * @param color Color
	 */
	public void setCaretColor(int color)
	{
		this.caret_color=color;
		repaint();
	}

	/**
	 * Devuelve el color del cursor.
	 */
	public int getCaretColor()
	{
		return caret_color;
	}

	/**
	 * Posición del cursor.
	 *
	 * @param position Posición [0..tamaño del texto]
	 */
	public void setCaretPosition(int position)
	{
		// Comprueba los límites.
		if (position<0) position=0;
			else if (position>text.length()) position=text.length();
		// Actualiza.
		caret_position=position;
		updatePassword();
		repaint();
	}

	/**
	 * Devuelve la posición del cursor.
	 */
	public int getCaretPosition()
	{
		return caret_position;
	}

	/**
	 * Devuelve el caracter que está en el cursor.
	 *
	 * @return Caracter o 0 si no hay ninguno
	 */
	public char getCharAtCaret()
	{
		return (caret_position<text.length()?text.charAt(caret_position):(char)0);
	}
	
	private void updatePassword()
	{
		if (password!=null)
		{
			int caret=(isFocusOwner()?caret_position:-1);
			password="";
			for (int i=0;i<text.length();i++) password+=(i==caret?text.charAt(i):'*');
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	public int getSkinId()
	{
		return Skin.KEY_ITEXTFIELD;
	}

	public void processSkinProperties()
	{
		super.processSkinProperties();
		//
		int id=getSkinId();
		caret_blink=skin.getInt(id,Skin.PROPERTY_ITEXTFIELD_CARET_BLINK);
		caret_color=skin.getInt(id,Skin.PROPERTY_ITEXTFIELD_CARET_COLOR);
	}

	/*************************************************************************/
	/*************************************************************************/

	public void paintComponent(Graphics g)
	{
		// Calcula la coordenada del cursor.
		String text=(password!=null?password:this.text);
		int caret_x=font.substringWidth(text,0,caret_position);
		// Calcula el scroll en X.
		int width=bounds.width-4;
		int limit_x=font.stringWidth(text);
		int scroll_x=0;
		if (limit_x>width)
		{
			scroll_x=Math.max(0,caret_x-(width>>1));
			if (limit_x-scroll_x<=width) scroll_x=limit_x-width;
		}
		//
		g.translate(-scroll_x+2,2);
		// Pinta el texto.
		int y=((bounds.height-4-font.getHeight())>>1);
		font.drawString(g,text,0,y,Graphics.LEFT|Graphics.TOP);
		// Pinta el cursor.
		if (isCaretVisible()) paintCaret(g,caret_x,y,getCharAtCaret());
		//
		g.translate(scroll_x-2,-2);
	}

	/**
	 * Devuelve si el cursor está visible o no.
	 */
	protected boolean isCaretVisible()
	{
		return (enabled&&caret_timer!=null&&caret_timer.visible);
	}

	/**
	 * Pinta el cursor (Sobreescribir para cambiar el skin).
	 *
	 * @param g Contexto gráfico
	 * @param x Coordenada X (left)
	 * @param y Coordenada Y (top)
	 * @param character Caracter sobre el que está (0 si no está sobre ninguno)
	 */
	protected void paintCaret(Graphics g,int x,int y,char character)
	{
		g.setColor(caret_color);
		g.drawLine(x-1,y,x-1,y+font.getHeight());
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void processKeyEvent(int type,int code,int action,char character)
	{
		if (type==Event.KEY_PRESSED)
		{
			if (character==0)
			{
				// Movimiento del cursor.
				if (action==ApplicationCanvas.LEFT) setCaretPosition(caret_position-1);
					else if (action==ApplicationCanvas.RIGHT) setCaretPosition(caret_position+1);
					else if (code==ApplicationCanvas.KEY_CLEAR)
					{
						if (caret_position<text.length())
						{
							setText(text.substring(0,caret_position)+text.substring(caret_position+1),false);
						}
						else if (text.length()>0)
						{
							setCaretPosition(caret_position-1);
							setText(text.substring(0,caret_position),false);
						}
					}
			}
			else if (code!=ApplicationCanvas.KEY_POUND)
			{
				if (!editing&&text.length()<maxlength)
				{
					editing=true;
					setText(text.substring(0,caret_position)+character+text.substring(caret_position),false);
				}
				else if (editing) setText(text.substring(0,caret_position)+character+text.substring(caret_position+1),false);
			}
			// Reinicia el parpadeo del cursor.
			caret_timer.reset();
		}
		else if (type==Event.KEY_TYPED)
		{
			if (character>0&&code!=ApplicationCanvas.KEY_POUND)
			{
				editing=false;
				caret_timer.reset();
				setCaretPosition(caret_position+1);
			}
		}
		//
		super.processKeyEvent(type,code,action,character);
	}

	protected void processFocusEvent(int type)
	{
		editing=false;
		setCaretPosition(0);
		//
		if (type==Event.FOCUS_GAINED)
		{
			caret_timer=new CaretTimer();
			caret_timer.start();
		}
		else if (type==Event.FOCUS_LOST)
		{
			caret_timer.reset();
			caret_timer=null;
			processActionEvent(null);
		}
		//
		super.processFocusEvent(type);
	}

	/*************************************************************************/
	/*************************************************************************/

	private class CaretTimer extends Thread
	{
		public boolean visible;

		public synchronized void reset()
		{
			visible=false;
			notify();
		}

		public synchronized void run()
		{
			visible=true;
			while (isFocusOwner()&&caret_blink>0)
			{
				try
				{
					repaint();
					wait(caret_blink);
					visible=!visible;
				}
				catch (Exception ex)
				{
				}
			}
		}
	}
}