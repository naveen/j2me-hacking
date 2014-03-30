package com.java4ever.apime.demo.skindemo;

import com.java4ever.apime.demo.*;
import com.java4ever.apime.io.*;
import com.java4ever.apime.ui.*;
import com.java4ever.apime.ui.event.*;

public class SkinDemo extends ApimeDemo implements ActionListener
{
	private IComboBox skins;
	private int skin;

	/*************************************************************************/
	/*************************************************************************/

	protected void initCode()
	{
		super.initCode();
		init();
	}

	private void init()
	{
		getRoot().removeAll();
		// Exit.
		IButton exit=getExitButton();
		getRoot().add(exit,getRoot().getWidth()-3-exit.getWidth(),3);
		// ComboBox.
		skins=new IComboBox();
		skins.setText("Skins");
		skins.getModel().addElement("Default");
		skins.getModel().addElement("Blue");
		skins.setSelectedIndex(skin);
		skins.setActionListener(this);
		getRoot().add(skins,10,25,getRoot().getWidth()-20,18);
	}

	/*************************************************************************/
	/*************************************************************************/

	public void actionPerformed(Object object,Object data)
	{
		try
		{
			if (skins.getSelectedIndex()==0)
			{
				skin=0;
				setSkin(null);
				getExitButton().setImage(IButton.NORMAL,loadImage("/exit_n.png"));
				getExitButton().setImage(IButton.ROLLOVER,loadImage("/exit_r.png"));
			}
			else if (skins.getSelectedIndex()==1)
			{
				skin=1;
				setSkin(new Skin(IOUtil.loadFile("/skindemo/blue.skn")));
				getExitButton().setImage(IButton.NORMAL,loadImage("/skindemo/exitblue_n.png"));
				getExitButton().setImage(IButton.ROLLOVER,loadImage("/skindemo/exitblue_r.png"));
			}
			// Procesa el skin de los componentes.
			init();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}