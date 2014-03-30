package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Anchor extends Widget {
	
	private static final Layout layout = new InlineLayout();
	{ setLayout(layout); }
	
	private Listener actionListener;
	
	public Anchor() {
	}
	
	public void addActionListener(Listener listener) {
		actionListener = Listener.add(actionListener, listener);
	}
	
	public void removeActionListener(Listener listener) {
		actionListener = Listener.remove(actionListener, listener);
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_ENTERED:
			setCursor(Cursor.HAND_CURSOR);
			repaint();
			break;
		case MouseEvent.MOUSE_EXITED:
			setCursor(Cursor.DEFAULT_CURSOR);
			repaint();
			break;
		case MouseEvent.MOUSE_RELEASED:
			if (isMouseInside()) {
				Listener.invoke(actionListener);
			}
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(isMouseInside() ? new Color(0x3366cc) : Color.darkGray);
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
	}
}
