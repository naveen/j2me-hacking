package thinlet;

import java.awt.*;
import java.awt.event.*;

public class TabbedPane extends Widget {
	
	private Widget tabs;
	private Tab selected;
	
	private static final Color light = new Color(0xACB9C8),
		dark = new Color(0x93A1B1), border = new Color(0x838F9E);
	
	public TabbedPane() {
		add(tabs = new Widget());
		
		add(new Tab("List", new List()));
		add(new Tab("Tree", new Tree()));
		add(new Tab("Table", new Table()));
		
		Constraint constraint = new Constraint();
		constraint.setWeightX(1); constraint.setWeightY(1);
		setConstraint(constraint);
	}
	
	public void add(Tab tab) {
		tabs.add(tab);
		if (selected == null) { selected = tab; add(tab.content); }
	}
	
	private void setSelected(Tab tab) {
		if (selected == tab) return;
		if (selected != null) selected.content.remove();
		selected = tab;
		if (selected != null) add(selected.content);
		repaint(); 
	}
	
	public Widget.Metrics getPreferredSize(int preferredWidth) {
		return measure(false);
	}
	
	public void doLayout() {
		measure(true);
	}
	
	private Widget.Metrics measure(boolean layout) {
		int th = 0, cw = 0, ch = 0;
		for (Widget tab = tabs.getChild(); tab != null; tab = tab.getNext()) {
			Metrics tmetrics = tab.getPreferredSize(-1);
			th = Math.max(th, tmetrics.getHeight());
			Widget.Metrics cmetrics = ((Tab) tab).content.getPreferredSize(-1);
			cw = Math.max(cw, cmetrics.getWidth()); ch = Math.max(ch, cmetrics.getHeight());
		}
		if (!layout) return new Metrics(cw, th + 4 + ch);
		
		int x = 4;
		tabs.setBounds(0, 0, getWidth(), th);
		for (Widget tab = tabs.getChild(); tab != null; tab = tab.getNext()) {
			Metrics tmetrics = tab.getPreferredSize(-1);
			tab.setBounds(x, 0, tmetrics.getWidth(), th); x += tmetrics.getWidth() + 1;
			((Tab) tab).content.setBounds(0, th + 4, getWidth(), getHeight() - th - 4);
		}
		return null;
	}
	
	protected void paint(Graphics g, Widget widget) {
		if (widget == tabs) {
			g.setColor(border);
			g.drawLine(0, widget.getHeight() - 1, widget.getWidth() - 1, widget.getHeight() - 1);
		}
	}
	
	private static final Layout layout = new InlineLayout(3, 6, 3, 6); 

	public class Tab extends Widget {
		
		{ setLayout(layout); }
		
		
		private Widget content;
		
		public Tab(String text, Widget content) {
			
			add(new Text(text));
			this.content = content;
		}
		
		protected void process(AWTEvent e) {
			switch (e.getID()) {
			case MouseEvent.MOUSE_ENTERED:
			case MouseEvent.MOUSE_EXITED:
				if (selected != this) repaint();
				break;
			case MouseEvent.MOUSE_PRESSED:
				if (selected != this) setSelected(this);
					else if (!TabbedPane.this.isFocused()) TabbedPane.this.requestFocus();
				break;
			}
		}
		
		protected void doLayout() {
			super.doLayout();
			
		}
		
		protected void paint(Graphics g) {
			if (selected == this) g.setColor(Color.white);
				else if (isMouseInside()) g.setColor(light);
				else ((Graphics2D) g).setPaint(new GradientPaint(0, 1, light, 0, getHeight() - 2 + 3, dark));
			g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2 + 3, 6, 6);
			g.setColor(border);
			if (selected != this) g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1 + 4, 8, 8);
			if ((selected == this) && TabbedPane.this.isFocused()) {
				g.setColor(Color.orange);
				g.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5 + 4, 6, 6);
			}
			g.setColor(Color.black);
			





		}
		
		protected void process(AWTEvent e, Widget widget) {
			switch (e.getID()) {
			case MouseEvent.MOUSE_ENTERED: case MouseEvent.MOUSE_EXITED:
			case MouseEvent.MOUSE_PRESSED: case MouseEvent.MOUSE_RELEASED:
				widget.repaint();
			}
		}
		
		protected void paint(Graphics g, Widget widget) {
			g.setColor((widget.isMouseInside() && widget.isPressedInside()) ?
				Color.red.darker() : Color.red);
			
			g.fillOval(1, 1, widget.getWidth() - 2, widget.getHeight() - 2);
			g.setColor(Color.gray);
			
			g.drawOval(0, 0, widget.getWidth() - 1, widget.getHeight() - 1);
			g.setColor(widget.isMouseInside() ? Color.white : Color.lightGray);
			g.drawLine(3, 3, widget.getWidth() - 4, widget.getHeight() - 4);
			g.drawLine(widget.getWidth() - 4, 3, 3, widget.getHeight() - 4);
		}
	}
}
