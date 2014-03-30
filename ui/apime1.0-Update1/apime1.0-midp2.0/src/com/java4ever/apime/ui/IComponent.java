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
 * Clase base para crear componentes.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public abstract class IComponent
{
	/**
	 * Cursor desactivado siempre.
	 */
	public static final int CURSOR_ENABLED_NEVER    = 0;

	/**
	 * Cursor activado siempre.
	 */
	public static final int CURSOR_ENABLED_ALWAYS	= 1;

	/**
	 * Cursor activado/desactivado cuando se pulsa sobre el componente.
	 */
	public static final int CURSOR_ENABLED_SWITCH	= 2;

	/*************************************************************************/

	/**
	 * Bounds.
	 */
	Rectangle bounds=new Rectangle();

	/**
	 * Color de primer plano (0 indica que no tiene color asignado).
	 */
	int foreground;

	/**
	 * Color de fondo (0 indica que no tiene color asignado).
	 */
	int background;

	/**
	 * Borde (0 indica que no tiene).
	 */
	int border;

	/**
	 * Fuente ('null' indica que no tiene fuente asignada).
	 *
	 * @see IFont
	 */
	IFont font;

	/**
	 * Cursor.
	 *
	 * @see Cursor
	 */
	Cursor cursor;

	/**
	 * Indica si el cursor está activo cuando el componente tiene el foco.
	 *
	 * @see #CURSOR_ENABLED_NEVER
	 * @see #CURSOR_ENABLED_ALWAYS
	 * @see #CURSOR_ENABLED_SWITCH
	 */
	int cursor_enabled;

	/**
	 * Texto del tooltip ('null' si no tiene).
	 *
	 * @see IToolTip
	 */
	String tooltip;

	/**
	 * Indica si está activado.
	 */
	boolean enabled;

	/**
	 * Indica si es visible.
	 */
	boolean visible;

	/**
	 * Contenedor padre ('null' si no tiene).
	 *
	 * @see IContainer
	 */
	IContainer parent;

	/**
	 * Listener de acciones.
	 *
	 * @see ActionListener
	 */
	ActionListener action_listener;

	/**
	 * Listener de componente.
	 *
	 * @see ComponentListener
	 */
	ComponentListener component_listener;

	/**
	 * Listener de foco.
	 *
	 * @see FocusListener
	 */
	FocusListener focus_listener;

	/**
	 * Listener de teclado.
	 *
	 * @see KeyListener
	 */
	KeyListener key_listener;

	/**
	 * Listener de ratón.
	 *
	 * @see MouseListener
	 */
	MouseListener mouse_listener;

	/**
	 * Coordenadas en pantalla.
	 *
	 * @see Point
	 */
	Point screen=new Point();

	/**
	 * Instancia global del skin.
	 */
	public static Skin skin;

	// Variables para optimizar la velocidad.
	Rectangle clipping=new Rectangle(Short.MAX_VALUE,Short.MAX_VALUE,-Short.MAX_VALUE,-Short.MAX_VALUE);

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Constructor.
	 */
	protected IComponent()
	{
		processSkinProperties();
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Cambia la posición del componente.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
	public void setLocation(int x,int y)
	{
		bounds.setLocation(x,y);
		processComponentEvent(Event.COMPONENT_MOVED);
	}

	/**
	 * Devuelve la posición del componente.
	 *
	 * @see Point
	 */
	public Point getLocation()
	{
		return new Point(bounds.x,bounds.y);
	}

	/**
	 * Cambia el tamaño del componente.
	 *
	 * @param width Ancho
	 * @param height Alto
	 */
	public void setSize(int width,int height)
	{
		bounds.setSize(Math.max(1,width),Math.max(1,height));
		processComponentEvent(Event.COMPONENT_RESIZED);
	}

	/**
	 * Devuelve el tamaño del componente.
	 *
	 * @see Point
	 */
	public Point getSize()
	{
		return new Point(bounds.width,bounds.height);
	}

	/**
	 * Cambia la posición y el tamaño del componente.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 * @param width Ancho
	 * @param height Alto
	 */
	public void setBounds(int x,int y,int width,int height)
	{
		setLocation(x,y);
		setSize(width,height);
	}

	/**
	 * Devuelve la posición y el tamaño del componente.
	 *
	 * @see Rectangle
	 */
	public Rectangle getBounds()
	{
		return new Rectangle(bounds);
	}

	/**
	 * Devuelve la coordenada X del componente.
	 */
	public int getX()
	{
		return bounds.x;
	}

	/**
	 * Devuelve la coordenada Y del componente.
	 */
	public int getY()
	{
		return bounds.y;
	}

	/**
	 * Devuelve la anchura del componente.
	 */
	public int getWidth()
	{
		return bounds.width;
	}

	/**
	 * Devuelve la altura del componente.
	 */
	public int getHeight()
	{
		return bounds.height;
	}

	/**
	 * Cambia el color de primer plano.
	 *
	 * @param foreground Color ARGB (0 indica que no tiene color asignado)
	 */
	public void setForeground(int foreground)
	{
		this.foreground=foreground;
		repaint();
	}

	/**
	 * Devuelve el color de primer plano.
	 */
	public int getForeground()
	{
		return foreground;
	}

	/**
	 * Cambia el color de fondo.
	 *
	 * @param background Color ARGB (0 indica que no tiene color asignado)
	 */
	public void setBackground(int background)
	{
		this.background=background;
		repaint();
	}

	/**
	 * Devuelve el color de fondo.
	 */
	public int getBackground()
	{
		return background;
	}

	/**
	 * Cambia el color del borde.
	 *
	 * @param border Color ARGB (0 indica que no tiene)
	 */
	public void setBorder(int border)
	{
		this.border=border;
		repaint();
	}

	/**
	 * Devuelve el color del borde.
	 */
	public int getBorder()
	{
		return border;
	}

	/**
	 * Cambia el tipo de letra.
	 *
	 * @param font Fuente
	 *
	 * @see IFont
	 */
	public void setFont(IFont font)
	{
		this.font=font;
		repaint();
	}

	/**
	 * Devuelve el tipo de letra.
	 *
	 * @see IFont
	 */
	public IFont getFont()
	{
		return font;
	}

	/**
	 * Cambia el cursor.
	 *
	 * @param cursor Cursor
	 *
	 * @see Cursor
	 */
	public void setCursor(Cursor cursor)
	{
		this.cursor=cursor;
	}

	/**
	 * Devuelve el cursor.
	 *
	 * @see Cursor
	 */
	public Cursor getCursor()
	{
		return cursor;
	}

	/**
	 * Cambia el tipo de cursor.
	 *
	 * @param cursor_enabled Tipo de cursor
	 *
	 * @see #CURSOR_ENABLED_NEVER
	 * @see #CURSOR_ENABLED_ALWAYS
	 * @see #CURSOR_ENABLED_SWITCH
	 */
	public void setCursorEnabled(int cursor_enabled)
	{
		this.cursor_enabled=cursor_enabled;
	}

	/**
	 * Devuelve el tipo de cursor.
	 *
	 * @see #CURSOR_ENABLED_NEVER
	 * @see #CURSOR_ENABLED_ALWAYS
	 * @see #CURSOR_ENABLED_SWITCH
	 */
	public int getCursorEnabled()
	{
		return cursor_enabled;
	}

	/**
	 * Cambia el texto del tooltip.
	 *
	 * @param tooltip Texto (pueder ser 'null')
	 */
	public void setToolTip(String tooltip)
	{
		this.tooltip=tooltip;
	}

	/**
	 * Devuelve el texto del tooltip o 'null' si no tiene.
	 */
	public String getToolTip()
	{
		return tooltip;
	}

	/**
	 * Activa/Desactiva el componente.
	 *
	 * @param enabled True para activarlo
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled=enabled;
		repaint();
	}

	/**
	 * Devuelve si el componente está activo.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Activa/Desactiva la visibilidad del componente.
	 *
	 * @param visible True para activarla
	 */
	public void setVisible(boolean visible)
	{
		this.visible=visible;
		processComponentEvent(visible?Event.COMPONENT_SHOWN:Event.COMPONENT_HIDDEN);
	}

	/**
	 * Devuelve si el componente es visible.
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * Repinta el componente.
	 */
	public void repaint()
	{
		RepaintManager.instance.repaint(this);
	}

	/**
	 * Petición de foco para el componente.
	 *
	 * @return True si ha podido recibir o mantener el foco
	 */
	public boolean requestFocus()
	{
		return FocusManager.instance.transferFocus(this);
	}

	/**
	 * Devuelve si el componente tiene el foco.
	 */
	public boolean isFocusOwner()
	{
		return (FocusManager.instance.focus==this);
	}

	/**
	 * Asigna el listener de acciones.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see ActionListener
	 */
	public void setActionListener(ActionListener listener)
	{
		action_listener=listener;
	}

	/**
	 * Devuelve el listener de acciones o 'null' si no tiene.
	 *
	 * @see ActionListener
	 */
	public ActionListener getActionListener()
	{
		return action_listener;
	}

	/**
	 * Asigna el listener de componente.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see ComponentListener
	 */
	public void setComponentListener(ComponentListener listener)
	{
		component_listener=listener;
	}

	/**
	 * Devuelve el listener de componente o 'null' si no tiene.
	 *
	 * @see ComponentListener
	 */
	public ComponentListener getComponentListener()
	{
		return component_listener;
	}

	/**
	 * Asigna el listener de foco.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see FocusListener
	 */
	public void setFocusListener(FocusListener listener)
	{
		focus_listener=listener;
	}

	/**
	 * Devuelve el listener de foco o 'null' si no tiene.
	 *
	 * @see FocusListener
	 */
	public FocusListener getFocusListener()
	{
		return focus_listener;
	}

	/**
	 * Asigna el listener de teclado.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see KeyListener
	 */
	public void setKeyListener(KeyListener listener)
	{
		key_listener=listener;
	}

	/**
	 * Devuelve el listener de teclado o 'null' si no tiene.
	 *
	 * @see FocusListener
	 */
	public KeyListener getKeyListener()
	{
		return key_listener;
	}

	/**
	 * Asigna el listener de ratón.
	 *
	 * @param listener Listener (pueder ser 'null')
	 *
	 * @see MouseListener
	 */
	public void setMouseListener(MouseListener listener)
	{
		mouse_listener=listener;
	}

	/**
	 * Devuelve el listener de ratón o 'null' si no tiene.
	 *
	 * @see MouseListener
	 */
	public MouseListener getMouseListener()
	{
		return mouse_listener;
	}

	/**
	 * Devuelve el contenedor padre o 'null' si no tiene.
	 *
	 * @see IContainer
	 */
	public IContainer getParent()
	{
		return parent;
	}

	/**
	 * Devuelve el contenedor principal o 'null' si no está en pantalla.
	 *
	 * @see IContainer
	 */
	protected IContainer getRoot()
	{
		IContainer parent=this.parent;
		while (parent!=null&&parent.parent!=null) parent=parent.parent;
		return ((parent instanceof IRoot)?(IRoot)parent:null);
	}

	/**
	 * Devuelve el panel con scroll del componente.
	 *
	 * @return Panel con scroll o 'null' si no tiene
	 *
	 * @see IScrollPane
	 */
	public IScrollPane getScrollPane()
	{
		if (parent instanceof IScrollPane.Viewport) return (IScrollPane)(parent.parent);
			else return null;
	}

	/**
	 * Devuelve si las coordenadas relativas están dentro del componente.
	 *
	 * @param x Coordenada X
	 * @param y Coordenada Y
	 */
    public boolean contains(int x,int y)
    {
		return (x>=0&&x<bounds.width&&y>=0&&y<bounds.height);
    }

	/**
	 * Devuelve la posición del componente en la pantalla.
	 */
	public Point getLocationOnScreen()
	{
		return new Point(screen.x,screen.y);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el Id del componente dentro del skin.
	 */
	public int getSkinId()
	{
		return Skin.KEY_ICOMPONENT;
	}
	/**
	 * Procesa las propiedades del skin para el componente.
	 */
	public void processSkinProperties()
	{
		int id=getSkinId();
		foreground=skin.getInt(id,Skin.PROPERTY_ICOMPONENT_FOREGROUND);
		foreground=skin.getInt(id,Skin.PROPERTY_ICOMPONENT_FOREGROUND);
		background=skin.getInt(id,Skin.PROPERTY_ICOMPONENT_BACKGROUND);
		border=skin.getInt(id,Skin.PROPERTY_ICOMPONENT_BORDER);
		cursor=Cursor.getCursor(skin.getString(id,Skin.PROPERTY_ICOMPONENT_CURSOR));
		cursor_enabled=skin.getInt(id,Skin.PROPERTY_ICOMPONENT_CURSOR_ENABLED);
		enabled=skin.getBoolean(id,Skin.PROPERTY_ICOMPONENT_ENABLED);
		visible=skin.getBoolean(id,Skin.PROPERTY_ICOMPONENT_VISIBLE);
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Pinta el componente (background-paintComponent()-border).
	 *
	 * @param g Contexto gráfico
	 *
	 * @see #paintComponent
	 */
	public void paint(Graphics g)
	{
		// Si no es transparente borra.
		if ((background&0xff000000)!=0)
		{
			g.setColor(background);
			g.fillRect(0,0,bounds.width,bounds.height);
		}
		// Pinta el componente.
		g.setColor(foreground);
		paintComponent(g);
		// Pinta el borde.
		if ((border&0xff000000)!=0)
		{
			g.setColor(border);
			g.drawRect(0,0,bounds.width-1,bounds.height-1);
		}
	}

	/**
	 * Pinta el componente.
	 *
	 * @param g Contexto gráfico
	 */
	public abstract void paintComponent(Graphics g);

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Procesa un evento de acción.
	 *
	 * @param data Datos relativos al evento (pueder ser 'null')
	 */
	protected void processActionEvent(Object data)
	{
		if (action_listener!=null) action_listener.actionPerformed(this,data);
	}

	/**
	 * Procesa un evento de componente.
	 *
	 * @param type Tipo de evento
	 *
	 * @see Event#COMPONENT_HIDDEN
	 * @see Event#COMPONENT_MOVED
	 * @see Event#COMPONENT_RESIZED
	 * @see Event#COMPONENT_SHOWN
	 */
	protected void processComponentEvent(int type)
	{
		RepaintManager.instance.repaint(this,RepaintManager.BOUNDS);
		//
		if (component_listener!=null)
		{
			if (type==Event.COMPONENT_HIDDEN) component_listener.componentHidden(this);
				else if (type==Event.COMPONENT_MOVED) component_listener.componentMoved(this);
				else if (type==Event.COMPONENT_RESIZED) component_listener.componentResized(this);
				else if (type==Event.COMPONENT_SHOWN) component_listener.componentShown(this);
		}
	}

	/**
	 * Procesa un evento de foco.
	 *
	 * @param type Tipo de evento
	 *
	 * @see Event#FOCUS_GAINED
	 * @see Event#FOCUS_LOST
	 */
	protected void processFocusEvent(int type)
	{
		if (focus_listener!=null)
		{
			if (type==Event.FOCUS_GAINED) focus_listener.focusGained(this);
				else if (type==Event.FOCUS_LOST) focus_listener.focusLost(this);
		}
	}

	/**
	 * Procesa un evento de teclado.
	 *
	 * @param type Tipo de evento
     * @param code Código de la tecla
	 * @param action Acción asociada al código de la tecla
	 * @param character Caracter asociado al código de la tecla (0 si no tiene)
	 *
	 * @see Event#KEY_PRESSED
	 * @see Event#KEY_RELEASED
	 * @see Event#KEY_TYPED
	 */
	protected void processKeyEvent(int type,int code,int action,char character)
	{
		if (key_listener!=null)
		{
			if (type==Event.KEY_PRESSED) key_listener.keyPressed(this,code,action,character);
				else if (type==Event.KEY_RELEASED) key_listener.keyReleased(this,code,action,character);
				else if (type==Event.KEY_TYPED) key_listener.keyTyped(this,code,action,character);
		}
	}

	/**
	 * Procesa un evento de ratón.
	 *
	 * @param type Tipo de evento
	 * @param x Coordenada X relativa al componente
	 * @param y Coordenada Y relativa al componente

	 * @see Event#MOUSE_ENTERED
	 * @see Event#MOUSE_EXITED
	 * @see Event#MOUSE_PRESSED
	 * @see Event#MOUSE_RELEASED
	 * @see Event#MOUSE_CLICKED
	 * @see Event#MOUSE_MOVED
	 * @see Event#MOUSE_DRAGGED
	 */
	protected void processMouseEvent(int type,int x,int y)
	{
		if (mouse_listener!=null)
		{
			if (type==Event.MOUSE_ENTERED) mouse_listener.mouseEntered(this,x,y);
				else if (type==Event.MOUSE_EXITED) mouse_listener.mouseExited(this,x,y);
				else if (type==Event.MOUSE_PRESSED) mouse_listener.mousePressed(this,x,y);
				else if (type==Event.MOUSE_RELEASED) mouse_listener.mouseReleased(this,x,y);
				else if (type==Event.MOUSE_CLICKED) mouse_listener.mouseClicked(this,x,y);
				else if (type==Event.MOUSE_MOVED) mouse_listener.mouseMoved(this,x,y);
				else if (type==Event.MOUSE_DRAGGED) mouse_listener.mouseDragged(this,x,y);
		}
	}

	/*************************************************************************/
	/*************************************************************************/

	public String toString()
	{
		return getClass().getName()+" [x="+bounds.x+",y="+bounds.y+",width="+bounds.width+",height="+bounds.height+"]";
	}
}