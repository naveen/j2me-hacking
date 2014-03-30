package thinlet;

import java.awt.*;
import java.awt.event.*;

public class MenuItem extends Widget {
	
	private static final Layout layout = new InlineLayout(2, 6, 2, 6);
	{ setLayout(layout); }

	public static final int ITEM_SELECTED = ActionEvent.ACTION_LAST + 2;

	private transient boolean selected;
	private Listener actionListener;
	
	public MenuItem() {
	}
	
	public void addActionListener(Listener listener) {
		actionListener = Listener.add(actionListener, listener);
	}
	
	public void removeActionListener(Listener listener) {
		actionListener = Listener.remove(actionListener, listener);
	}
	
	void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
	
	boolean isSelected() {
		return selected;
	}

	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_ENTERED:
		case DRAG_ENTERED:
			((PopupMenu) getParent()).setSelected(this);
			break;
		case MouseEvent.MOUSE_EXITED:
		case DRAG_EXITED:
			((PopupMenu) getParent()).setSelected(null);
			break;
		case MouseEvent.MOUSE_RELEASED:
			if (!isMouseInside()) return;
		case DRAG_RELEASED:
			Listener.invoke(actionListener);
			getParent().process(new ActionEvent(this, ITEM_SELECTED, null));
			((PopupMenu) getParent()).remove();
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(isSelected() ? new Color(0xc0c0ff) : new Color(0xeeeeee));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
	}
}
