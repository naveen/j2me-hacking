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
 * Created on May 24, 2006
 */
package gr.bluevibe.fire.components;

import gr.bluevibe.fire.util.Lang;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;

public class RowTextBox extends TextBox implements CommandListener
{
	private Row row;
	public RowTextBox(Row row)
	{
		super("", row.getText(), row.getTextBoxSize(), row.getTextBoxConstrains());
		this.row=row;
		Command ok = new Command(Lang.get("Ok"),Command.BACK,1);
		this.addCommand(ok);
		this.setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d)
	{
		row.textUpdate(getString());
	}
}