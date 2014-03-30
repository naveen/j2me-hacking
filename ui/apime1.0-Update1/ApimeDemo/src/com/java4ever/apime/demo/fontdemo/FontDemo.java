package com.java4ever.apime.demo.fontdemo;

import javax.microedition.lcdui.*;

import com.java4ever.apime.demo.*;
import com.java4ever.apime.ui.*;
import com.java4ever.apime.ui.font.*;

public class FontDemo extends ApimeDemo
{
	protected void initCode()
	{
		super.initCode();
		// Exit.
		IButton exit=getExitButton();
		getRoot().add(exit,getRoot().getWidth()-3-exit.getWidth(),3);
		// Fonts.
		IFont.addFont("NativeFont",new NativeFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
		IFont.addFont("ImageFont",new ImageFont(loadImage("/fontdemo/ifont.png"),14,14,2,'A','Z'));
		// Native Font.
		ILabel label1=new ILabel("Native Font");
		label1.setForeground(0xff00a000);
		label1.setFont(IFont.getFont("NativeFont"));
		label1.setAlignment(Graphics.HCENTER,Graphics.VCENTER);
		getRoot().add(label1,0,0,getRoot().getWidth(),getRoot().getHeight()>>1);
		// Image Font.
		ILabel label2=new ILabel("IMAGE FONT");
		label2.setFont(IFont.getFont("ImageFont"));
		label2.setAlignment(Graphics.HCENTER,Graphics.VCENTER);
		getRoot().add(label2,0,getRoot().getHeight()>>1,getRoot().getWidth(),getRoot().getHeight()>>1);
	}
}