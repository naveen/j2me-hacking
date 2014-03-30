package com.java4ever.apime.demo.keyboarddemo;

import com.java4ever.apime.demo.*;
import com.java4ever.apime.io.*;
import com.java4ever.apime.ui.*;
import com.java4ever.apime.ui.event.*;

public class KeyboardDemo extends ApimeDemo implements ActionListener
{
	private IComboBox keyboards;
	private ITextField text;

	/*************************************************************************/
	/*************************************************************************/

	protected void initCode()
	{
		super.initCode();
		// Exit.
		IButton exit=getExitButton();
		getRoot().add(exit,getRoot().getWidth()-3-exit.getWidth(),3);
		// Input.
		ILabel input=getInputLabel();
		getRoot().add(input,exit.getX()-input.getWidth()-2,3);
		// ComboBox.
		keyboards=new IComboBox();
		keyboards.setText("Keyboards");
		keyboards.getModel().addElement("Default");
		keyboards.getModel().addElement("Spanish");
		keyboards.getModel().addElement("Siemens");
		keyboards.setSelectedIndex(0);
		keyboards.setActionListener(this);
		getRoot().add(keyboards,10,25,getRoot().getWidth()-20,18);
		// TextField.
		getRoot().add(text=new ITextField(""),10,50,getRoot().getWidth()-20,18);
		text.requestFocus();
	}

	/*************************************************************************/
	/*************************************************************************/

	public void actionPerformed(Object object,Object data)
	{
		try
		{
			if (keyboards.getSelectedIndex()==0) KeyboardManager.instance.setKeyboard(null);
				else if (keyboards.getSelectedIndex()==1) KeyboardManager.instance.setKeyboard(IOUtil.loadFile("/keyboarddemo/default_es.kbd"));
				else if (keyboards.getSelectedIndex()==2) KeyboardManager.instance.setKeyboard(IOUtil.loadFile("/keyboarddemo/siemens.kbd"));
			text.requestFocus();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}