package thinlet;

import java.awt.*;
import java.awt.event.*;

public class ComboBox extends Widget {
	
	private static final Layout layout = new InlineLayout(3, 6, 3, 16);
	{ setLayout(layout); }
	
	private PopupMenu popup;
	
	public ComboBox() {
	}
	
	public Widget add(Widget widget) {
		if (widget instanceof PopupMenu) {
			popup = (PopupMenu) widget;
		} else {
			super.add(widget);
		}
		return this;
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			if (popup != null) {
				popup.show(this, 1, true, false, 0);
				repaint();
			}
			break;
		case MouseEvent.MOUSE_ENTERED:
		case MouseEvent.MOUSE_EXITED:
			if ((popup == null) || (popup.getParent() == null))
				repaint();
			break;
		case PopupMenu.POPUP_CLOSED: 
			repaint();
			break;
		case MenuItem.ITEM_SELECTED: 
			System.out.println(e.getSource());
		}
	}
	
	private static final Color light = new Color(0xACB9C8),
		dark = new Color(0x93A1B1), border = new Color(0x838F9E);
	
	protected void paint(Graphics g) {
		if ((popup != null) && (popup.getParent() != null)) g.setColor(dark);
			else if (isMouseInside()) g.setColor(light);
			else ((Graphics2D) g).setPaint(new GradientPaint(0, 1, light, 0, getHeight() - 2, dark));
		g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
		g.setColor(border);
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		g.setColor(Color.darkGray);
		int tx = getWidth() - 12, ty = (getHeight() - 8) / 2;
		g.fillPolygon(new int [] { tx, tx + 4, tx + 8 }, new int [] { ty, ty + 8, ty }, 3);
		g.setColor(Color.black);
	}
}
