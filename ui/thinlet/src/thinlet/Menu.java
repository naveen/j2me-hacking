package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Menu extends Widget {

	private static final Layout layout = new InlineLayout(2, 6, 2, 6 + 8 + 6),
		barlayout = new InlineLayout(3, 6, 2, 6);
	{ setLayout(layout); }
	
	private PopupMenu popup;
	private transient boolean selected;
	private transient boolean insidePopup;
	
	public Menu() {
	}
	
	void added() {
		insidePopup = (getParent() instanceof PopupMenu);
		if (!insidePopup) setLayout(barlayout);
	}
	
	public Widget add(Widget widget) {
		if (widget instanceof PopupMenu) {
			popup = (PopupMenu) widget;
		} else {
			super.add(widget);
		}
		return this;
	}
	
	void setSelected(boolean selected) {
		if (popup != null) {
			if (selected) {
				if (insidePopup) popup.show(this, -getHeight() - 1, false, true, -6);
					else popup.show(this, 0, false, false, 0);
			} else {
				popup.remove();
			}
		}
		this.selected = selected;
		repaint();
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_ENTERED: case DRAG_ENTERED:
			if (insidePopup) select(this);
			else if (((MenuBar) getParent()).getSelected() != null) select(this); break;
		case MouseEvent.MOUSE_PRESSED:
			if (!insidePopup && ((MenuBar) getParent()).getSelected() == null) select(this); break;
		case PopupMenu.POPUP_CLOSED:
			select(null); break;
		case MenuItem.ITEM_SELECTED:
			if (insidePopup) getParent().process(e);
		}
	}
	
	private void select(Menu menu) {
		if (insidePopup) ((PopupMenu) getParent()).setSelected(menu);
			else ((MenuBar) getParent()).setSelected(menu);
	}
	
	protected void paint(Graphics g) {
		if (insidePopup) {
			g.setColor(selected ? new Color(0xc0c0ff) : new Color(0xeeeeee));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.darkGray);
			int x = getWidth() - 8 - 6, y = (getHeight() - 8) / 2;
			g.fillPolygon(new int [] { x, x + 8, x }, new int [] { y, y + 4, y + 8 }, 3);
		}
		else {
			if (selected) {
				g.setColor(Color.lightGray);
				g.drawRect(0, 0, getWidth() - 1, getHeight());
				g.setColor(new Color(0xc0c0ff));
				g.fillRect(1, 1, getWidth() - 2, getHeight() - 1);
			}
		}
		g.setColor(Color.black);
	}
}
