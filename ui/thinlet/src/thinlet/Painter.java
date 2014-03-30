package thinlet;

import java.awt.*;

public class Painter {

	
	
	public void paint(Graphics g, Widget target) {
	}
	
	static void paint(Graphics g, int x, int y, int width, int height,
			Color border, Color c1, Color c2,
			boolean top, boolean left, boolean bottom, boolean right, int radius) {
		g.setColor(border);
		g.drawRect(x, y, width - 1, height - 1);
		((Graphics2D) g).setPaint(new GradientPaint(0, y + 1, c1, 0, y + height - 2, c2));
		
		g.fillRect(x + 1, y + 1, width - 2, height - 2);
	}
}
