package thinlet;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;




public class Widget implements Serializable {
	
	private Widget parent, next, child;
	private transient int x, y, width, height;
	
	private transient Layout layout;
	private transient Constraint constraint;
	private transient Painter painter;
	
	public Widget add(Widget widget) {
		if (widget.parent != null) widget.remove();
		if (child == null) {
			child = widget;
		} else {
			Widget last = child; while (last.next != null) last = last.next;
			last.next = widget;
		}
		widget.parent = this;
		widget.added();
		return this;
	}
	
	public void append(Widget widget) {
		if (widget.parent != null) widget.remove();
		widget.next = next; next = widget;
		widget.parent = parent;
		widget.added();
	}
	
	public void remove() {
		if (parent == null) return;
		repaint();
		if (parent.child == this) {
			parent.child = next;
		} else {
			Widget prev = parent.child; while (prev.next != this) prev = prev.next;
			prev.next = next; next = null;
		}
		Widget prevparent = parent;
		parent = null;
		prevparent.removed(this);
	}
	
	void added() {}
	void removed(Widget widget) {}
	
	public void removeAll() {
		if (child != null) {
			for (Widget w = child; w != null; w = w.next) w.parent = null;
			child = null;
		}
	}

	protected Widget getParent() {
		return parent;
	}
	
	protected Widget getNext() {
		return next;
	}
	
	void setNext(Widget next) {
		this.next = next;
	}
	
	protected Widget getChild() {
		return child;
	}
	






	
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	private static final Constraint defaultconstraint = new Constraint();
	{ setConstraint(defaultconstraint); }
	
	Constraint getConstraint() { return constraint; }
	public void setConstraint(Constraint constraint) { this.constraint = constraint; }
	
	public void setPainter(Painter painter) {
		this.painter = painter;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		if (layout == null) return new Metrics(0, 0);
		return layout.getPreferredSize(this, preferredWidth);
	}
	
	public class Metrics {

		private int width, height, baseline;
		protected Metrics next;
		protected int x, y;
		
		public Metrics(int width, int height) {
			this(width, height, -1);
		}
		
		public Metrics(int width, int height, int baseline) {
			this.width = width; this.height = height; this.baseline = baseline;
		}

		public int getWidth() { return width; }
		public int getHeight() { return height; }
		public int getBaseline() { return baseline; }
		public Widget getWidget() { return Widget.this; }
	}
	
	public void setBounds(int x, int y, int width, int height) {
		this.x = x; this.y = y;
		if ((this.width != width) || (this.height != height)) {
			this.width = width; this.height = height;
			doLayout();
		}
		else doLayout(); 
	}

	protected int getX() { return x; }
	protected int getY() { return y; }
	protected int getWidth() { return width; }
	protected int getHeight() { return height; }
	
	protected void doLayout() {
		if (layout != null) layout.doLayout(this);
	}
	
	protected void repaint() {
		repaint(0, 0, width, height);
	}
	
	protected void repaint(int x, int y, int width, int height) {
		if (parent != null) parent.repaint(this.x + x, this.y + y, width, height); 
	}
	
	protected void revalidate() {
		getDesktop().doLayout(); getDesktop().repaint(); 
	}

	protected void paint(Graphics g) { if (parent != null) parent.paint(g, this); }
	protected void paint(Graphics g, Widget widget) { if (parent != null) parent.paint(g, widget); }
	
	protected void paintImpl(Graphics g) {
		Rectangle clip = g.getClipBounds();
		if ((width > 0) && (height > 0))
			if ((clip.x + clip.width < x) || (clip.x > x + width) ||
				(clip.y + clip.height < y) || (clip.y > y + height)) return;
		g.translate(x, y);
		if ((width > 0) && (height > 0)) g.clipRect(0, 0, width, height);
		if (painter != null) painter.paint(g, this); else paint(g);
		Color color = g.getColor(); Font font = g.getFont();
		for (Widget widget = getChild(); widget != null; widget = widget.getNext()) {
			g.setColor(color); g.setFont(font); 
			widget.paintImpl(g);
		}
		g.translate(-x, -y);
		g.setClip(clip);
	}
	
	public Component getComponent() {
		return (parent != null) ? parent.getComponent() :
			new RootPane(this).getComponent();
	}
	
	RootPane getDesktop() {
		return (parent != null) ? parent.getDesktop() : null;
	}
	
	protected Widget getWidgetAt(int mx, int my) {
		if ((mx >= x) && (my >= y) && (mx < x + width) && (my < y + height)) {
			Widget inside = getWidgetAt(getChild(), mx - x, my - y);
			return (inside != null) ? inside : this;
		}
		return null;
	}

	private Widget getWidgetAt(Widget widget, int mx, int my) {
		if (widget == null) return null;
		Widget inside = getWidgetAt(widget.getNext(), mx, my);
		return (inside != null) ? inside : widget.getWidgetAt(mx, my);
	}
	
	protected void requestFocus() {
		getDesktop().requestFocus(this);
	}
	
	protected boolean isFocusable() { return true; }

	protected static final int DRAG_ENTERED = MouseEvent.MOUSE_LAST + 1;
	protected static final int DRAG_EXITED = MouseEvent.MOUSE_LAST + 2;
	protected static final int DRAG_MOVED = MouseEvent.MOUSE_LAST + 3;
	protected static final int DRAG_RELEASED = MouseEvent.MOUSE_LAST + 4;
	
	protected void process(AWTEvent e) { if (parent != null) parent.process(e, this); }
	protected void process(AWTEvent e, Widget widget) { if (parent != null) parent.process(e, widget); }
	



	
	protected boolean isMouseInside() {
		return (getMouseInside() == this);
	}
	
	protected boolean isPressedInside() {
		return (getMousePressed() == this);
	}
	
	protected boolean isFocused() {
		return (getFocused() == this);
	}
	
	protected Widget getMouseInside() { return (parent != null) ? parent.getMouseInside() : null; }
	protected Widget getMousePressed() { return (parent != null) ? parent.getMousePressed() : null; }
	protected Widget getFocused() { return (parent != null) ? parent.getFocused() : null; }

	protected Timer startTimer(int delay, int period, Object parameter) {
		return new Repeater(this, delay, period, parameter).getTimer();
	}
	
	protected boolean timerEvent(Object parameter) {
		return false;
	}
	
	private static class Repeater implements ActionListener {
		
		private Widget widget;
		private Object parameter;
		private Timer timer;
		
		private Repeater(Widget widget, int delay, int period, Object parameter) {
			this.widget = widget;
			this.parameter = parameter;
			timer = new Timer(period, this);
			timer.setInitialDelay(delay);
			timer.start();
		}
		
		private Timer getTimer() {
			return timer;
		}
		
		public void actionPerformed(ActionEvent e) {
			if (!widget.timerEvent(parameter)) {
				timer.stop();
			}
		}
	}
	
	public void popup(Widget invoker, int x, int y) {
		RootPane desktop = invoker.getDesktop();
		for (Widget widget = invoker; widget != desktop; widget = widget.getParent()) {
			x += widget.getX(); y += widget.getY();
		}
		Metrics metrics = getPreferredSize(desktop.getWidth());
		setBounds(x, y, metrics.getWidth(), metrics.getHeight());
		desktop.addPopup(this);
	}
	
	protected void setCursor(int type) {
		getComponent().setCursor(Cursor.getPredefinedCursor(type));
	}
	
	public Widget getClone() {
		throw new IllegalArgumentException();
	}
}
