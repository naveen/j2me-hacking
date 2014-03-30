package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Row extends Widget {
	
	private static final Layout layout = new InlineLayout(2, 2, 3, 2);
	{ setLayout(layout); }
	
	private boolean selected;
	
	public Row() {
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			selected = !selected; repaint(); break;
		case MouseWheelEvent.MOUSE_WHEEL:
			System.out.println("> " + e);
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(new Color(0xeeeeee));
		g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		if (selected) {
			g.setColor(new Color(0xe0e0ff));
			g.fillRect(0, 0, getWidth(), getHeight() - 1);
		}
		g.setColor(Color.black);
	}
}
