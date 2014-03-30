package thinlet;

import java.awt.*;

public class MenuBar extends Widget {
	
	private static final Layout layout = new InlineLayout(0, 0, 1, 0);
	{ setLayout(layout); }
	
	private transient Menu selected;
	
	public MenuBar() {
	}
	
	void setSelected(Menu menu) {
		if (selected != menu) {
			if (selected != null) selected.setSelected(false);
			selected = menu;
			if (selected != null) selected.setSelected(true);
		}
	}
	
	Menu getSelected() {
		return selected;
	}
	
	protected void paint(Graphics g) {
		g.setColor(new Color(0xe6edf7));
		g.fillRect(0, 0, getWidth(), getHeight() - 1);
		g.setColor(Color.lightGray);
		g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
		g.setColor(Color.darkGray);
	}
}
