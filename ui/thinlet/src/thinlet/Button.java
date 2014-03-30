package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Button extends Widget {
	
	private static final Layout layout = new InlineLayout(3, 9, 3, 9);
	{ setLayout(layout); }
	
	private Listener actionListener;
	
	public Button() {
	}
	
	public Button(String text) {
		add(new Text(text));
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
		case MouseEvent.MOUSE_EXITED:
			repaint();
			break;
		case MouseEvent.MOUSE_PRESSED:
			requestFocus();
			repaint();
			break;
		case MouseEvent.MOUSE_RELEASED:
			if (isMouseInside()) {
				Listener.invoke(actionListener);
				repaint();
			}
			break;
		case FocusEvent.FOCUS_GAINED:
		case FocusEvent.FOCUS_LOST:
			repaint();
		}
	}
	
	private static final Color light = new Color(0xACB9C8),
		dark = new Color(0x93A1B1), border = new Color(0x838F9E);
	
	protected void paint(Graphics g) {
		if (isMouseInside()) g.setColor(isPressedInside() ? dark : light);
			else ((Graphics2D) g).setPaint(new GradientPaint(0, 1, light, 0, getHeight() - 2, dark));
			
		g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);
		g.setColor(border);
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		if (isFocused()) {
			g.setColor(Color.orange);


			g.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 6, 6);
		}
		g.setColor(Color.black);
	}
}
