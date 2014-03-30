/*
 * Created by Javier Cancela
 * Copyright (C) 2007 hipoqih.com, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, If not, see <http://www.gnu.org/licenses/>.*/

package com.hipoqih.plugin.UI;

import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStoreException;
import com.hipoqih.plugin.*;

public class SettingsFormUI extends Form implements CommandListener 
{
	private StringItem labelUser = new StringItem("", "User :", StringItem.PLAIN);
	private TextField textUser = new TextField("", State.user, 16, TextField.NON_PREDICTIVE);
	private StringItem labelPass = new StringItem("", "Password:", StringItem.PLAIN);
	private TextField textPass = new TextField("", State.password, 16, TextField.NON_PREDICTIVE | TextField.PASSWORD);
	private StringItem labelPeriod = new StringItem("", "Connection period (seconds):", StringItem.PLAIN);
	private TextField textPeriod = new TextField("", Integer.toString(State.connectionPeriod / 1000), 16, TextField.NUMERIC);
	private Command cmdSave = new Command("Save", Command.SCREEN, 1);
	private Command cmdCancel = new Command("Cancel", Command.SCREEN, 1);
	private MainFormUI mainForm = null;

	public SettingsFormUI(MainFormUI mf)
	{
		super("HipoqihPlugin");
		setCommandListener(this);
		mainForm = mf;
		labelUser.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		this.append(labelUser);
		textUser.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(textUser);
		labelPass.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_2);
		this.append(labelPass);
		textPass.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(textPass);
		labelPeriod.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(labelPeriod);
		textPeriod.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP | Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
		this.append(textPeriod);
		this.addCommand(cmdSave);
		this.addCommand(cmdCancel);
	}

	public void commandAction(Command command, Displayable displayable)
    {
		if (command == cmdSave)
		{
			State.user = textUser.getString();
			State.password = textPass.getString();
			int period = Integer.parseInt(textPeriod.getString());
			if (period < 1)
				period = 1;
			State.connectionPeriod = period * 1000;
			try
			{
				Tools.updateRecord(RecordTypes.USER, State.user);
				Tools.updateRecord(RecordTypes.PASSWORD, State.password);
				Tools.updateRecord(RecordTypes.CONNECTIONPERIOD, Integer.toString(State.connectionPeriod));
			}
			catch(RecordStoreException rse)
			{
				Alert alertScreen = new Alert("Error");
				alertScreen.setString("There was an error storing the data");
				alertScreen.setTimeout(Alert.FOREVER);
			}
			State.display.setCurrent(mainForm);
		}
		if (command == cmdCancel)
		{
			State.display.setCurrent(mainForm);
		}
    }
}
