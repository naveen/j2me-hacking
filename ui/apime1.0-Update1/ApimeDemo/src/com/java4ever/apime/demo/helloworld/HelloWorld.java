package com.java4ever.apime.demo.helloworld;

import javax.microedition.lcdui.*;

import com.java4ever.apime.demo.*;
import com.java4ever.apime.ui.*;

public class HelloWorld extends ApimeDemo
{
	protected void initCode()
	{
		super.initCode();
		
		// Exit.
		IButton exit=getExitButton();
		getRoot().add(exit,getRoot().getWidth()-3-exit.getWidth(),3);
		// Hello World!
		ILabel label=new ILabel("Hello World!");
		label.setAlignment(Graphics.HCENTER,Graphics.VCENTER);
		getRoot().add(label,0,0,getRoot().getWidth(),getRoot().getHeight());
	}
}