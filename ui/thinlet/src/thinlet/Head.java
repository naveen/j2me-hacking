package thinlet;

import java.awt.*;

public class Head extends Widget {
	
	public Head() {
	}
	
	public Head(String text) {
		this();
		add(new Text(text));
	}
	
	protected void paint(Graphics g) {
		g.setColor(new Color(0x91a0c0));
		g.drawLine(0, 0, getWidth() - 1, 0);
		((Graphics2D) g).setPaint(new GradientPaint(0, 1, new Color(0xa1b0cf),
			0, getHeight() - 1, new Color(0x7185ab)));
		g.fillRect(0, 1, getWidth(), getHeight() - 1);
		g.setColor(Color.white);
	}
}
