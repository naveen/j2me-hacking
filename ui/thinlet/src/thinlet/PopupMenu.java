package thinlet;

import java.awt.*;
import java.awt.event.*;

public class PopupMenu extends Widget {

	public static final int POPUP_CLOSED = ActionEvent.ACTION_LAST + 1;
	private transient Widget selected;
	private Widget invoker;
	
	{ setLayout(null); }

	public PopupMenu() {
	}
	
	void setSelected(Widget menu) {
		if (selected != menu) {
			setSelected(selected, false);
			selected = menu;
			setSelected(selected, true);
		}
	}
	
	private static void setSelected(Widget menu, boolean selected) {
		if (menu != null) {
			if (menu instanceof MenuItem) ((MenuItem) menu).setSelected(selected);
				else ((Menu) menu).setSelected(selected);
		}
	}
	
	public void show(Widget invoker, int vgap, boolean stretch,
			boolean horizontal, int hgap) {
		RootPane desktop = invoker.getDesktop();
		int dw = desktop.getWidth(), dh = desktop.getHeight();
		Metrics metrics = getPreferredSize(dw);
		int x = 0, y = 0,
			width = metrics.getWidth(), height = Math.min(metrics.getHeight(), dh);
		if (stretch) width = Math.max(width, invoker.getWidth());
		
		for (Widget widget = invoker; widget != desktop; widget = widget.getParent()) {
			x += widget.getX(); y += widget.getY();
		}
		
		int ih = invoker.getHeight();
		if (height <= dh - y - ih - vgap) y += ih + vgap; 
			else if (height <= y - vgap) y -= height + vgap; 
				else y = dh - height; 
		
		if (horizontal) {
			int iw = invoker.getWidth();
			if (width <= dw - x - iw - hgap) x += iw + hgap; 
			else if (width <= x - hgap) x -= width + hgap; 
				else x = dw - width; 
		}
		
		this.invoker = invoker;
		setBounds(x, y, width, height);
		desktop.addPopup(this);
	}
	
	public void remove() {
		if (getParent() == null) return;
		setSelected(null);
		super.remove();
		invoker.process(new ActionEvent(this, POPUP_CLOSED, null));
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MenuItem.ITEM_SELECTED:
			remove();
			invoker.process(e);
		}
	}
	
	protected void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		int width = 0, height = 0;
		for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
			Metrics metrics = widget.getPreferredSize(preferredWidth - 2);
			width = Math.max(width, metrics.getWidth());
			height += metrics.getHeight();
		}
		return new Metrics(width + 2, height + 2);
	}
	
	protected void doLayout() {
		int y = 0;
		for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
			Metrics metrics = widget.getPreferredSize(getWidth() - 2);
			widget.setBounds(1, 1 + y, getWidth() - 2, metrics.getHeight());
			y += metrics.getHeight();
		}
	}
}
