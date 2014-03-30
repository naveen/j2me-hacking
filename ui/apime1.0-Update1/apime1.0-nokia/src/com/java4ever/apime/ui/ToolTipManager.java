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

/**
 * Gestor de tooltip.
 *
 * @author Carlos Araiz
 *
 * @version 1.0
 */
public class ToolTipManager
{
	/**
	 * Instancia global.
	 */
	public static final ToolTipManager instance=new ToolTipManager();

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Tiempo que tarda en aparecer el tooltip (ms.).
	 */
	public static int INITIAL;

	/**
	 * Tiempo que tarda en desaparecer el tooltip (ms.).
	 */
	public static int DISMISS;

	/*************************************************************************/

	/**
	 * Componente visual para el tooltip.
	 *
	 * @see IToolTip
	 */
	IToolTip tooltip;
	
	/**
	 * Indica si está activada la visualización de tooltips.
	 */
	private boolean enabled;

	// Control de visualización.
	private IComponent actual;
	private int timer;

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Crea el gestor.
	 */
	private ToolTipManager()
	{
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Devuelve el componente visual para el tooltip.
	 *
	 * @see IToolTip
	 */
	public IToolTip getToolTip()
	{
		return tooltip;
	}

	/**
	 * Activa/Desactiva los tooltips.
	 *
	 * @param enabled True para activarlo
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled=enabled;
	}
	
	/**
	 * Devuelve si los tooltips están activados.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/*************************************************************************/
	/*************************************************************************/

	/**
	 * Inicializa el gestor.
	 *
	 * @param width Ancho de la zona visual
	 */
	void init(int width)
	{
		tooltip=new IToolTip(width);
		// Procesa las propiedades del skin.
		INITIAL=IComponent.skin.getInt(Skin.KEY_TOOLTIPMANAGER,Skin.PROPERTY_TOOLTIPMANAGER_INITIAL);
		DISMISS=IComponent.skin.getInt(Skin.KEY_TOOLTIPMANAGER,Skin.PROPERTY_TOOLTIPMANAGER_DISMISS);
		enabled=IComponent.skin.getBoolean(Skin.KEY_TOOLTIPMANAGER,Skin.PROPERTY_TOOLTIPMANAGER_ENABLED);
	}

	/**
	 * Transfiere el tooltip a un componente.
	 *
	 * @param component Componente al que pasar el tooltip (puede ser 'null')
	 * @param reset True para reiniciar el control de tiempo
	 *
	 * @see IComponent
	 */
	void transferToolTip(IComponent component,boolean reset)
	{
		if (enabled&&component!=null&&component.tooltip!=null)
		{
			if (component!=actual)
			{
				tooltip.setText(component.tooltip);
				actual=component;
				timer=0;
			}
			else if (reset) timer=(timer<INITIAL?0:60000);
		}
		else actual=null;
	}

	/**
	 * Actualiza el gestor.
	 *
	 * @param delay Tiempo transcurrido desde el frame anterior (en ms.)
	 * @param limit_x Coordenada X límite
	 * @param limit_y Coordenada Y límite
	 */
	void update(int delay,int limit_x,int limit_y)
	{
		if (enabled&&actual!=null&&CursorManager.instance.enabled)
		{
			timer+=delay;
			boolean v=(timer>INITIAL&&timer<(DISMISS+INITIAL));
			if (!tooltip.visible&&v) recalculate(limit_x,limit_y);
			if (tooltip.visible!=v) tooltip.setVisible(v);
		}
		else
		{
			timer=Integer.MAX_VALUE;
			if (tooltip.visible) tooltip.setVisible(false);
		}
	}

	private void recalculate(int limit_x,int limit_y)
	{
		Cursor c=actual.cursor;
		tooltip.recalculate();
		tooltip.setLocation(CursorManager.instance.pos.x,CursorManager.instance.pos.y-c.hotspot.y+c.image.getHeight());
		int lx=tooltip.bounds.x+tooltip.bounds.width;
		int ly=tooltip.bounds.y+tooltip.bounds.height;
		if (lx>limit_x) tooltip.bounds.x-=(lx-limit_x);
		if (ly>limit_y)	tooltip.bounds.y=Math.max(0,CursorManager.instance.pos.y-c.hotspot.y-tooltip.bounds.height);
		tooltip.clipping.setBounds(tooltip.bounds);
	}
}