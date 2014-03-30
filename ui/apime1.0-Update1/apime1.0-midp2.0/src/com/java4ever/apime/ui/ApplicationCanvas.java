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

import com.java4ever.apime.math.*;
import com.java4ever.apime.ui.event.*;

/**
 * Canvas para la aplicación, con doble buffer si el móvil no tiene.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ApplicationCanvas extends Canvas
{
	/**
	 * Código para la tecla SOFT1.
	 */
	public static int KEY_SOFTKEY1;

	/**
	 * Código para la tecla SOFT2.
	 */
	public static int KEY_SOFTKEY2;

	/**
	 * Código para la tecla SOFT3.
	 */
	public static int KEY_SOFTKEY3;

	/**
	 * Código para la tecla CLEAR.
	 */
	public static int KEY_CLEAR;

	/**
	 * Código para la tecla ABC.
	 */
	public static int KEY_ABC;

	/**
	 * Código para la tecla SEND.
	 */
	public static int KEY_SEND;

	/**
	 * Código para la tecla END.
	 */
	public static int KEY_END;

	/*************************************************************************/

	// Pantallazo inicial.
	private Image splash;
	private Application application;
	// Variables de doble buffer.
	private Image buffer;
	private Graphics gbuffer;
	// Componentes de estado.
	private IComponent rollovered,pressed;
	private Cursor cursor;
	private Rectangle cursor_damage;
	// Variables para los eventos.
	private int events[],event_init,event_index;
	private IComponent events_pointer[];
	// Contenedor padre.
	IContainer root;
	Skin skin;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el canvas.
	 */
	ApplicationCanvas()
	{
		cursor_damage=new Rectangle();
		setFullScreenMode(true);
	}

	/*************************************************************************/
	/*************************************************************************/
	
	/**
	 * Inicializa.
	 *
	 * @param application aplicación
	 * @param splash Imagen o 'null'
	 *
	 * @see Application
	 */
	void init(Application application,String splash)
	{
		this.application=application;
		// Crea el doble buffer si es necesario.
		if (!isDoubleBuffered())
		{
			buffer=Image.createImage(getWidth(),getHeight());
			gbuffer=buffer.getGraphics();
		}
		// Splash Screen.
		try
		{
			if (splash!=null)
			{
				this.splash=Image.createImage(splash);
				repaint();
				serviceRepaints();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		// Inicializa el skin.
		setSkin(null);
		// Elimina el Splash Screen.
		splash=null;
	}

	/**
	 * Inicializa los eventos.
	 */
	void initEvents()
	{
		events=new int[16];
		events_pointer=new IComponent[16];
	}

	/**
	 * Actualiza el código.
	 *
	 * @param delay Tiempo transcurrido desde el frame anterior (en ms.)
	 */
	void update(int delay)
	{
		// Actualiza el rollover.
		if (RepaintManager.instance.needRepaint()&&pressed==null) updateRollover();
		// Actualiza los eventos.
		for (int event_end=event_index;event_init!=event_end;event_init=(event_init+1)&0xf)
		{
			int event=events[event_init];
			int type=event&3;
			if (type==0) doKeyReleased(event>>2);
				else if (type==1) doKeyPressed(event>>2);
				else if (type==2&&events_pointer[event_init]!=null&&events_pointer[event_init]!=rollovered) doKeyPressed(getKeyCode(FIRE));
				else if (type==3&&pressed!=null&&pressed.enabled) pressed.processMouseEvent(Event.MOUSE_DRAGGED,(event>>17)-pressed.screen.x,((event>>2)&0x7fff)-pressed.screen.y);
		}
		// Actualiza el movimiento del cursor.
		if (CursorManager.instance.update(delay))
		{
			RepaintManager.instance.repaint(cursor_damage);
			// Movimiento o Drag.
			if (pressed==null)
			{
				// Actualiza el rollover.
				boolean changed=updateRollover();
				// Movimiento dentro del componente.
				if (!changed&&rollovered.enabled) rollovered.processMouseEvent(Event.MOUSE_MOVED,CursorManager.instance.pos.x-rollovered.screen.x,CursorManager.instance.pos.y-rollovered.screen.y);
			}
			else if (pressed.enabled) pressed.processMouseEvent(Event.MOUSE_DRAGGED,CursorManager.instance.pos.x-pressed.screen.x,CursorManager.instance.pos.y-pressed.screen.y);
			// Transfiere el tooltip.
			ToolTipManager.instance.transferToolTip(rollovered,true);
		}
		else
		{
			// FIX: Repinta el área dañada por el cursor (entrada en un componente que no tiene CURSOR_ENABLED_ALWAYS).
			if (!CursorManager.instance.enabled&&cursor_damage.width>0) RepaintManager.instance.repaint(cursor_damage);
			// Transfiere el tooltip.
			ToolTipManager.instance.transferToolTip(rollovered,pressed!=null);
		}
		// Cursor.
		if (CursorManager.instance.enabled)
		{
			Cursor c=((rollovered!=null&&rollovered.enabled)?rollovered.cursor:Cursor.DEFAULT);
			if (cursor!=c)
			{
				cursor=c;
				RepaintManager.instance.repaint(cursor_damage);
			}
		}
		else cursor=null;
		// Actualiza los managers.
		KeyboardManager.instance.update(delay);
		ToolTipManager.instance.update(delay,getWidth(),getHeight());
		// Actualiza el rollover.
		if (RepaintManager.instance.needRepaint())
		{
			if (pressed==null) updateRollover();
			repaint();
			serviceRepaints();
		}
	}

	/**
	 * Cambia el skin.
	 *
	 * @param skin Skin ('null' para asignar el skin por defecto)
	 *
	 * @see Skin
	 */
	void setSkin(Skin skin)
	{
		try
		{
			// Crea el skin por defecto si es necesario.
			if (skin==null) skin=new Skin(new byte[]{0,7,68,101,102,97,117,108,116,0,19,0,8,0,0,0,0,0,1,0,1,0,2,0,2,0,3,0,3,0,4,0,4,0,5,0,6,0,6,0,10,0,1,0,11,0,9,0,0,0,0,0,7,0,1,0,2,0,2,0,3,0,3,0,7,0,4,0,6,0,6,0,0,0,7,0,1,0,10,0,6,0,11,0,8,0,0,0,0,0,7,0,1,0,2,0,2,0,3,0,3,0,7,0,4,0,5,0,6,0,6,0,10,0,1,0,11,0,1,0,1,0,11,0,8,0,8,0,5,0,5,0,6,0,1,0,7,0,9,0,8,0,9,0,9,0,6,0,10,-1,-1,0,11,0,9,0,12,0,1,0,4,0,11,0,1,0,15,0,11,0,3,0,6,0,6,0,0,0,7,0,4,0,11,0,4,0,1,0,5,0,6,0,6,0,6,0,11,0,3,0,13,0,4,0,1,0,5,0,6,0,6,0,6,0,10,0,5,0,11,0,5,0,1,0,5,0,6,0,6,0,5,0,11,0,9,0,14,0,10,0,15,0,2,0,6,0,10,0,1,0,11,0,1,0,5,0,11,0,3,0,1,0,5,0,6,0,6,0,5,0,11,0,3,0,16,0,11,0,5,0,16,0,9,0,17,0,1,0,4,0,11,0,7,0,1,0,5,0,6,0,6,0,2,0,7,0,11,0,8,0,15,0,11,0,12,0,18,0,7,0,19,0,7,0,13,0,5,0,3,0,6,0,5,0,9,0,6,0,10,0,14,0,11,0,5,0,12,0,9,0,16,0,3,0,14,0,20,0,15,0,21,0,9,0,22,16,-1,-65,-68,-120,-1,-1,-1,-1,-1,-119,0,0,-1,-35,88,0,-1,-75,1,1,0,0,0,0,-1,0,0,0,-1,-35,16,0,0,-1,-1,-1,0,0,0,1,0,0,0,9,0,0,0,2,0,0,1,44,-1,-1,-54,-128,0,0,11,-72,0,0,1,-12,0,3,0,4,104,97,110,100,0,7,100,101,102,97,117,108,116,0,4,116,101,120,116,0});
		}
		catch (Exception ex)
		{
			// No debería de saltar ninguna excepción.
			ex.printStackTrace();
		}
		// Inicializa los valores estáticos.
		IComponent.skin=skin;
		IScrollBar.AUTOSCROLL=skin.getBoolean(Skin.KEY_ISCROLLBAR,Skin.PROPERTY_ISCROLLBAR_AUTOSCROLL);
		IScrollBar.THICK=skin.getInt(Skin.KEY_ISCROLLBAR,Skin.PROPERTY_ISCROLLBAR_THICK);
		// Inicializa los managers.
		CursorManager.instance.init(getWidth()-1,getHeight()-1);
		ToolTipManager.instance.init(getWidth());
		// Crea el contenedor principal.
		root=new IRoot(getWidth(),getHeight());
		this.skin=skin;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Actualiza el rollover.
	 *
	 * @return Devuelve si ha cambiado el componente de rollover
	 */
	private boolean updateRollover()
	{
		IComponent component=getComponentAtScreen(root,CursorManager.instance.pos.x,CursorManager.instance.pos.y);
		if (component!=rollovered)
		{
			if (rollovered!=null&&rollovered.enabled) rollovered.processMouseEvent(Event.MOUSE_EXITED,0,0);
			rollovered=component;
			if (rollovered.enabled) rollovered.processMouseEvent(Event.MOUSE_ENTERED,CursorManager.instance.pos.x-rollovered.screen.x,CursorManager.instance.pos.y-rollovered.screen.y);
			return true;
		}
		else return false;
	}

	/**
	 * Devuelve el componente que contiene las coordenadas de pantalla indicadas.
	 *
	 * @param root Container donde buscar
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 *
	 * @return Componente
	 *
	 * @see IComponent
	 * @see IContainer
	 */
	private IComponent getComponentAtScreen(IContainer root,int x,int y)
	{
		for (int i=0;i<root.components.size();i++)
		{
			IComponent component=(IComponent)root.components.elementAt(i);
			if (component.visible&&component.enabled&&component.clipping.contains(x,y))
			{
				if (component instanceof IContainer) return getComponentAtScreen((IContainer)component,x,y);
					else return component;
			}
		}
		return root;
	}

	/*************************************************************************/
	/*************************************************************************/

	protected void paint(Graphics g)
	{
		if (buffer==null) gbuffer=g;
		//
		if (root!=null)
		{
			RepaintManager.instance.prepareRepaint(gbuffer,false);
			// Root.
			root.paint(gbuffer);
			// Tooltip.
			if (ToolTipManager.instance.tooltip.visible)
			{
				int x=ToolTipManager.instance.tooltip.bounds.x;
				int y=ToolTipManager.instance.tooltip.bounds.y;
				gbuffer.translate(x,y);
				ToolTipManager.instance.tooltip.paint(gbuffer);
				gbuffer.translate(-x,-y);
			}
			RepaintManager.instance.prepareRepaint(gbuffer,true);
			// Cursor.
			if (cursor!=null)
			{
				int x=CursorManager.instance.pos.x-cursor.hotspot.x;
				int y=CursorManager.instance.pos.y-cursor.hotspot.y;
				gbuffer.drawImage(cursor.image,x,y,Graphics.LEFT|Graphics.TOP);
				cursor_damage.setBounds(x,y-cursor.hotspot.y,cursor.image.getWidth(),cursor.image.getHeight());
			}
			else cursor_damage.setBounds(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);
		}
		else
		{
			gbuffer.setColor(0);
			gbuffer.fillRect(0,0,getWidth(),getHeight());
			if (splash!=null) gbuffer.drawImage(splash,(getWidth()-splash.getWidth())>>1,(getHeight()-splash.getHeight())>>1,Graphics.LEFT|Graphics.TOP);
		}
		// Pinta el doble buffer.
		if (buffer!=null)
		{
			g.setClip(0,0,getWidth(),getHeight());
			g.drawImage(buffer,0,0,Graphics.LEFT|Graphics.TOP);
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	private boolean isActionKey(int code)
	{
		return (code<KEY_NUM0||code>KEY_NUM9);
	}

	private boolean isFireKey(int code,int action)
	{
		if (code==KEY_CLEAR&&(FocusManager.instance.focus instanceof ITextField)) return false;
			else return ((action==FIRE&&isActionKey(code))||code==KEY_SOFTKEY1||code==KEY_SOFTKEY2);
	}

	protected void pointerPressed(int x,int y)
	{
		if (events!=null)
		{
			IComponent temp=FocusManager.instance.focus;
			CursorManager.instance.setLocation(x,y);
			RepaintManager.instance.repaint(cursor_damage);
			updateRollover();
			pressed=rollovered;
			keyPressed(getKeyCode(FIRE));
			if (temp!=null&&temp.cursor_enabled!=IComponent.CURSOR_ENABLED_ALWAYS)
			{
				events[event_index]=(((CursorManager.instance.pos.x<<15)|CursorManager.instance.pos.y)<<2)|2;
				events_pointer[event_index]=temp;
				event_index=(event_index+1)&0xf;
			}
		}
	}

	protected void pointerReleased(int x,int y)
	{
		if (events!=null) keyReleased(getKeyCode(FIRE));
	}

	protected void pointerDragged(int x,int y)
	{
		if (events!=null&&pressed!=null&&pressed.enabled)
		{
			CursorManager.instance.setLocation(x,y);
			RepaintManager.instance.repaint(cursor_damage);
			events[event_index]=(((CursorManager.instance.pos.x<<15)|CursorManager.instance.pos.y)<<2)|3;
			event_index=(event_index+1)&0xf;
		}
	}

	protected void keyPressed(int code)
	{
		if (events!=null)
		{
			events[event_index]=(code<<2)|1;
			event_index=(event_index+1)&0xf;
		}
	}

	protected void keyReleased(int code)
	{
		if (events!=null)
		{
			events[event_index]=(code<<2);
			event_index=(event_index+1)&0xf;
		}
	}

	private void doKeyPressed(int code)
	{
		int action=getGameAction(code);
		// Acciones con el cursor activado.
		if (CursorManager.instance.enabled)
		{
			// Movimiento.
			int move_x=CursorManager.MOVE_IGNORE,move_y=CursorManager.MOVE_IGNORE;
			if (isActionKey(code))
			{
				if (action==LEFT) move_x=CursorManager.MOVE_LU;
					else if (action==RIGHT) move_x=CursorManager.MOVE_RD;
					else if (action==UP) move_y=CursorManager.MOVE_LU;
					else if (action==DOWN) move_y=CursorManager.MOVE_RD;
			}
			CursorManager.instance.move(move_x,move_y);
			// Click.
			if (isFireKey(code,action))
			{
				if (rollovered!=null&&FocusManager.instance.transferFocus(rollovered))
				{
					pressed=rollovered;
					pressed.processMouseEvent(Event.MOUSE_PRESSED,CursorManager.instance.pos.x-rollovered.screen.x,CursorManager.instance.pos.y-rollovered.screen.y);
				}
			}
			// Tecla.
			else if (move_x==CursorManager.MOVE_IGNORE&&move_y==CursorManager.MOVE_IGNORE)
			{
				KeyboardManager.instance.transferKeyEvent(code,action,true);
			}
		}
		// Acciones con el cursor desactivado.
		else
		{
			// Reactiva el cursor.
			if (isFireKey(code,action)) CursorManager.instance.setEnabled(rollovered!=null&&rollovered.cursor_enabled!=IComponent.CURSOR_ENABLED_NEVER);
			//
			if (!CursorManager.instance.enabled)
			{
				KeyboardManager.instance.transferKeyEvent(code,action,true);
			}
			else FocusManager.instance.transferFocus(null);
		}
	}

	private void doKeyReleased(int code)
	{
		int action=getGameAction(code);
		// Acciones con el cursor activado.
		if (CursorManager.instance.enabled)
		{
			// Movimiento.
			int move_x=CursorManager.MOVE_IGNORE,move_y=CursorManager.MOVE_IGNORE;
			if (isActionKey(code))
			{
				if (action==LEFT||action==RIGHT) move_x=CursorManager.MOVE_STOP;
					else if (action==UP||action==DOWN) move_y=CursorManager.MOVE_STOP;
			}
			CursorManager.instance.move(move_x,move_y);
			// Click.
			if (isFireKey(code,action))
			{
				if (pressed!=null&&pressed.enabled)
				{
					int x=CursorManager.instance.pos.x-pressed.screen.x;
					int y=CursorManager.instance.pos.y-pressed.screen.y;
					pressed.processMouseEvent(Event.MOUSE_RELEASED,x,y);
					pressed.processMouseEvent(Event.MOUSE_CLICKED,x,y);
				}
				pressed=null;
				updateRollover();
			}
			// Tecla.
			else if (move_x==CursorManager.MOVE_IGNORE&&move_y==CursorManager.MOVE_IGNORE)
			{
				KeyboardManager.instance.transferKeyEvent(code,action,false);
			}
		}
		// Acciones con el cursor desactivado.
		else
		{
			KeyboardManager.instance.transferKeyEvent(code,action,false);
		}
	}

	/*************************************************************************/
	/*************************************************************************/
	
	protected void showNotify()
	{
		if (events!=null)
		{
			int event=events[(event_index-1)&0xf];
			int type=event&3;
			if (type==1) keyReleased(event>>2);
				else if (type!=0) pointerReleased(event>>17,(event>>2)&0x7fff);
			application.pause(false);
			root.repaint();
		}
	}

	protected void hideNotify()
	{
		if (events!=null) application.pause(true);
	}
}