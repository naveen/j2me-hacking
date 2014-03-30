package thinlet;

import java.awt.*;
import java.awt.event.*;

public class CheckBox extends Widget {
	
	private static final Layout layout = new InlineLayout(0, 18, 0, 0);
	{ setLayout(layout); }
	
	private boolean selected;
	private Listener actionListener;
	
	public CheckBox() {
		selected = true;
	}
	
	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			Listener.invoke(actionListener);
			repaint();
		}
	}
	
	public boolean isSelected() {
		return selected;
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
			if (isMouseInside()) setSelected(!selected);
			break;
		case FocusEvent.FOCUS_GAINED:
		case FocusEvent.FOCUS_LOST:
			repaint();
		}
	}
	
	protected void paint(Graphics g) {
		int dy = (getHeight() - 14) / 2;
		g.setColor(Color.lightGray);
		g.drawRect(0, dy, 14 - 1, 14 - 1);
		if (isFocused()) {
			g.setColor(Color.orange);
			g.drawRect(2, dy + 2, 14 - 5, 14 - 5);
		}
		if (selected) {
			g.setColor(Color.darkGray);
			g.fillRect(3, dy + 14 - 9, 2, 6);
			g.drawLine(3, dy + 14 - 4, 14 - 4, dy + 3);
			g.drawLine(4, dy + 14 - 4, 14 - 4, dy + 4);
		}
		g.setColor(Color.black);
	}
	
	public Widget getClone() {
		CheckBox checkBox = new CheckBox();
		checkBox.selected = selected;
		checkBox.actionListener = actionListener;
		return checkBox;
	}
}
