package com.gsmdev.util.example;
import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.gsmdev.util.FontUtil;

public class MyCanvas extends Canvas {

	protected void paint(Graphics g) {
		try {
			FontUtil.initialize("/font.bin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		FontUtil fu = FontUtil.getInstance();
						
		g.setColor(0xffff00);
		g.fillRect(0, 0, getWidth()/2, getHeight());
		g.setColor(0x00ffff);
		g.fillRect(getWidth()/2, 0, getWidth()/2, getHeight());
		
		Image image = fu.renderTransparentText("www.gsmdev.com", 0xffffff);
		
		g.drawImage(image, getWidth()/2, getHeight()/2, Graphics.VCENTER|Graphics.HCENTER);
		
		image = fu.renderText("font4mobile");
		
		g.drawImage(image, getWidth()/2, 0, Graphics.HCENTER|Graphics.TOP);		
	}

}
