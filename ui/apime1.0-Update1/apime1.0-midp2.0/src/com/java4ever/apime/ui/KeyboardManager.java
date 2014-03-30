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

import java.io.*;
import javax.microedition.lcdui.*;

import com.java4ever.apime.ui.event.*;

/**
 * Gestor de teclado.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class KeyboardManager
{
	/**
	 * Instancia global.
	 */
	public static final KeyboardManager instance=new KeyboardManager();

	/*************************************************************************/
	/*************************************************************************/
	
	public static final int INPUT_LOWERCASE	= 0;
	public static final int INPUT_UPPERCASE	= 1;
	public static final int INPUT_NUMBER	= 2;

	/*************************************************************************/

	/**
	 * Tiempo mínimo de retardo (ms.).
	 */
	public static int DELAY_MIN;

	/**
	 * Tiempo máximo de retardo (ms.).
	 */
	public static int DELAY_MAX;

	/**
	 * Decremento del retardo (ms.).
	 */
	public static int DELAY_DEC;

	/*************************************************************************/
	
	/**
	 * Tipo de entrada de carateres.
	 *
	 * @see #INPUT_LOWERCASE
	 * @see #INPUT_UPPERCASE
	 * @see #INPUT_NUMBER
	 */
	private int input=INPUT_LOWERCASE;

	// Mapa de códigos/caracteres.
	private char keymap[][];
	// Variables del evento.
	private int code=Integer.MIN_VALUE,action;
	private char table[];
	// Variables de repetición.
	private int index,pressed_timer=Integer.MIN_VALUE,released_timer=Integer.MIN_VALUE,delay;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el gestor.
	 */
	private KeyboardManager()
	{
		try
		{
			setKeyboard(null);
		}
		catch (Exception ex)
		{
		}
	}

	/*************************************************************************/
	/*************************************************************************/
	
	/**
	 * Cambia el teclado.
	 *
	 * @param data Datos del teclado ('null' para asignar el teclado por defecto)
	 */
	public void setKeyboard(byte data[]) throws IOException
	{
		// Crea el skin por defecto si es necesario.
		if (data==null) data=new byte[]{0,7,68,101,102,97,117,108,116,0,0,0,50,0,0,1,-12,0,0,0,100,-1,-1,-1,-6,-1,-1,-1,-7,-1,-1,-1,-5,-1,-1,-1,-8,-1,-1,-1,-50,-1,-1,-1,-10,-1,-1,-1,-11,0,2,32,48,0,1,49,0,4,97,98,99,50,0,4,100,101,102,51,0,4,103,104,105,52,0,4,106,107,108,53,0,4,109,110,111,54,0,5,112,113,114,115,55,0,4,116,117,118,56,0,5,119,120,121,122,57,0,29,46,44,63,33,39,34,45,40,41,64,47,58,95,59,43,38,37,42,61,40,41,91,93,123,125,92,126,35,124};
		DataInputStream dais=new DataInputStream(new ByteArrayInputStream(data));
		// Nombre del teclado.
		dais.readUTF();
		// Velocidad del teclado.
		DELAY_MIN=dais.readInt();
		DELAY_MAX=dais.readInt();
		DELAY_DEC=dais.readInt();
		// Códigos extendidos de teclado.
		ApplicationCanvas.KEY_SOFTKEY1=dais.readInt();
		ApplicationCanvas.KEY_SOFTKEY2=dais.readInt();
		ApplicationCanvas.KEY_SOFTKEY3=dais.readInt();
		ApplicationCanvas.KEY_CLEAR=dais.readInt();
		ApplicationCanvas.KEY_ABC=dais.readInt();
		ApplicationCanvas.KEY_SEND=dais.readInt();
		ApplicationCanvas.KEY_END=dais.readInt();
		// Tabla de caracteres para cada tecla.
		keymap=new char[12][];
		for (int i=0;i<keymap.length-1;i++) keymap[i]=dais.readUTF().toCharArray();
		keymap[11]=new char[]{'\u0000'};
		// Invalida el evento.
		code=action=pressed_timer=released_timer=Integer.MIN_VALUE;
	}

	/**
	 * Asigna el tipo de entrada de carateres.
	 *
	 * @param input Tipo
	 *
	 * @see #INPUT_LOWERCASE
	 * @see #INPUT_UPPERCASE
	 * @see #INPUT_NUMBER
	 */
	public void setInput(int input)
	{
		this.input=input;
	}

	/**
	 * Devuelve el tipo de entrada de carateres.
	 *
	 * @see #INPUT_LOWERCASE
	 * @see #INPUT_UPPERCASE
	 * @see #INPUT_NUMBER
	 */
	public int getInput()
	{
		return input;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Transfiere un evento de teclado al componente que tiene el foco.
	 *
	 * @param code Código de la tecla
	 * @param action Acción asociada al código de la tecla
	 * @param pressed True si la tecla está pulsada
	 */
	void transferKeyEvent(int code,int action,boolean pressed)
	{
		if ((code==Canvas.KEY_POUND||code==ApplicationCanvas.KEY_ABC)&&pressed)
		{
			input=(input+1)%3;
		}
		if (FocusManager.instance.focus!=null)
		{
			if (pressed)
			{
				// Comprueba si es cambio de código.
				if (code!=this.code&&this.code!=Integer.MIN_VALUE)
				{
					FocusManager.instance.focus.processKeyEvent(Event.KEY_TYPED,this.code,this.action,getChar());
					pressed_timer=released_timer=Integer.MIN_VALUE;
				}
				// Almacena los datos del evento.
				this.code=code;
				this.action=action;
				// Busca la tabla de la tecla correspondiente al código.
				if (code>=Canvas.KEY_NUM0&&code<=Canvas.KEY_NUM9) table=keymap[code-'0'];
					else table=keymap[code==Canvas.KEY_STAR?10:11];
				// Envía el evento correspondiente.
				index=(released_timer!=Integer.MIN_VALUE?((index+1)%table.length):0);
				FocusManager.instance.focus.processKeyEvent(Event.KEY_PRESSED,code,action,getChar());
				// Inicializa el tiempo.
				pressed_timer=0;
				released_timer=Integer.MIN_VALUE;
				delay=DELAY_MAX;
			}
			else
			{
				// Envía el evento correspondiente.
				FocusManager.instance.focus.processKeyEvent(Event.KEY_RELEASED,code,action,getChar());
				// Inicializa el tiempo.
				if (code==this.code)
				{
					pressed_timer=Integer.MIN_VALUE;
					released_timer=0;
					delay=DELAY_MAX;
				}
			}
		}
	}

	/**
	 * Actualiza el gestor.
	 *
	 * @param delay Tiempo transcurrido desde el frame anterior (en ms.)
	 */
	void update(int delay)
	{
		if (FocusManager.instance.focus!=null)
		{
			if (pressed_timer!=Integer.MIN_VALUE)
			{
				pressed_timer+=delay;
				if (pressed_timer>this.delay)
				{
					pressed_timer=0;
					// Pasa al siguiente carácter de la tecla.
					index=(index+1)%table.length;
					// Envía el evento.
					FocusManager.instance.focus.processKeyEvent(Event.KEY_PRESSED,code,action,getChar());
					// Acelera la repetición en caso de que no sea una tecla con caracteres legibles.
					if (getChar()==0) this.delay=Math.max(DELAY_MIN,this.delay-DELAY_DEC);
				}
			}
			else if (released_timer!=Integer.MIN_VALUE)
			{
				released_timer+=delay;
				if (released_timer>this.delay)
				{
					// Envía el evento.
					FocusManager.instance.focus.processKeyEvent(Event.KEY_TYPED,code,action,getChar());
					// Invalida el evento.
					code=action=pressed_timer=released_timer=Integer.MIN_VALUE;
				}
			}
		}
		// Invalida el evento.
		else code=action=pressed_timer=released_timer=Integer.MIN_VALUE;
	}

	/*************************************************************************/
	/*************************************************************************/

	private char getChar()
	{
		if (table!=null)
		{
			char c=table[Math.min(index,table.length-1)];
			if (input==INPUT_NUMBER) return ((code>=Canvas.KEY_NUM0&&code<=Canvas.KEY_NUM9)?(char)code:c);
				else return ((input==INPUT_UPPERCASE)?Character.toUpperCase(c):Character.toLowerCase(c));
		}
		else return 0;
	}
}