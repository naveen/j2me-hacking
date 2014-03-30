package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Node extends Widget {
	
	private static final Layout layout = new InlineLayout(2, 12, 3, 2);
	{ setLayout(layout); }
	
	private boolean expanded;
	private boolean selected;
	private Widget node;
	private int nodecount;

	public Node() {
	}
	
	public void addNode(Widget widget) {
		if (node == null) {
			node = widget;
		} else {
			Widget last = node; while (last.getNext() != null) last = last.getNext();
			last.setNext(widget);
		}
		nodecount++;
	}
	
	public void setExpanded(boolean expanded) {
		if (this.expanded == expanded) return;
		this.expanded = expanded;
		if (expanded) { 
			for (Widget widget = node, current = this; widget != null;) {
				Widget next = widget.getNext();
				current.append(widget);
				current = widget; widget = next;
			}
		} else { 
			Widget widget = node; for (int i = 0; i < nodecount; i++) {
				if (widget instanceof Node) ((Node) widget).setExpanded(false);
				Widget next = widget.getNext();
				widget.remove();
				widget.setNext(next);
				widget = next;
				
			}
		}
		revalidate();
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			if ((node != null) && (((MouseEvent) e).getX() < 12)) setExpanded(!expanded);
				else { selected = !selected; repaint(); }
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(new Color(0xeeeeee));
		g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		if (selected) {
			g.setColor(new Color(0xe0e0ff));
			g.fillRect(0, 0, getWidth(), getHeight() - 1);
		}
		if (node != null) {
			g.setColor(Color.darkGray);
			int tx = 2, ty = (getHeight() - 8 - 1) / 2;
			g.fillPolygon(expanded ? new int [] { tx, tx + 4, tx + 8 } : new int [] { tx, tx + 8, tx },
				expanded ? new int [] { ty, ty + 8, ty } : new int [] { ty, ty + 4, ty + 8 }, 3);
		}
		g.setColor(Color.black);
	}
}
