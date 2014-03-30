package thinlet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

class RootPane extends Widget {

	private static RootPane rootPane;
	private Wrapper wrapper;
	private transient Widget mouseinside, mousepressed;
	private transient boolean focused;
	private transient Widget focusowner;
	private transient int popupCount;
	
	RootPane(Widget content) {
		rootPane = this;
		wrapper = new Wrapper();
		add(content);
	}
	
	void addPopup(Widget widget) {
		add(widget); popupCount++;
		widget.repaint();
	}

	void removed(Widget widget) {
		popupCount--;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return getChild().getPreferredSize(preferredWidth);
	}
	
	static FontMetrics getFontMetrics() {
		return rootPane.wrapper.getFontMetrics(rootPane.wrapper.getFont());
	}
	
	protected void doLayout() {
		getChild().setBounds(0, 0, getWidth(), getHeight());
	}
	
	protected void repaint(int x, int y, int width, int height) {
		wrapper.repaint(x, y, width, height);
	}
	
	public Component getComponent() {
		return wrapper;
	}
	
	RootPane getDesktop() {
		return this;
	}
	
	void requestFocus(Widget widget) {
		if (focusowner != widget) {
			Widget previous = focusowner;
			focusowner = widget;
			if (focused) {
				if (previous != null) previous.process(
					new FocusEvent(wrapper, FocusEvent.FOCUS_LOST));
				if (focusowner != null) focusowner.process(
					new FocusEvent(wrapper, FocusEvent.FOCUS_GAINED));
			}
		}
		if (!focused) wrapper.requestFocus();
	}

	protected Widget getMouseInside() { return mouseinside; }
	protected Widget getMousePressed() { return mousepressed; }
	protected Widget getFocused() { return focused ? focusowner : null; }
	
	private class Wrapper extends JComponent  {
		
		private Wrapper() {
			enableEvents(AWTEvent.FOCUS_EVENT_MASK | AWTEvent.KEY_EVENT_MASK |
				AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK |
				AWTEvent.MOUSE_WHEEL_EVENT_MASK);
		}
		
		



		
		public void doLayout() {
			RootPane.this.setBounds(0, 0, getWidth(), getHeight());
		}
		
		protected void processEvent(AWTEvent e) {
			int id = e.getID();
			if (id == MouseEvent.MOUSE_PRESSED) {
				if ((popupCount > 0) && (mouseinside == getChild())) 
					while (popupCount > 0) getChild().getNext().remove();
				mousepressed = mouseinside;
				post(mouseinside, id, (MouseEvent) e);
			}
			else if (id == MouseEvent.MOUSE_RELEASED) {
				Widget pressed = mousepressed;
				mousepressed = null;
				MouseEvent me = (MouseEvent) e;
				post(pressed, id, me);
				if ((pressed != mouseinside) && (mouseinside != null)) {
					post(mouseinside, DRAG_RELEASED, me);
					post(mouseinside, MouseEvent.MOUSE_ENTERED, me);
				}
			}
			else if (id == MouseEvent.MOUSE_ENTERED) {
				MouseEvent me = (MouseEvent) e;
				mouseinside = getWidgetAt(me.getX(), me.getY());
				post(mouseinside, ((mouseinside == mousepressed) ||
					(mousepressed == null)) ? id : DRAG_ENTERED, me);
			}
			else if (id == MouseEvent.MOUSE_EXITED) {
				Widget inside = mouseinside;
				mouseinside = null;
				post(inside, ((mouseinside == mousepressed) ||
					(mousepressed == null)) ? id : DRAG_EXITED, (MouseEvent) e);
			}
			else if (id == MouseEvent.MOUSE_MOVED) {
				Widget inside = mouseinside;
				MouseEvent me = (MouseEvent) e;
				mouseinside = getWidgetAt(me.getX(), me.getY());
				if (inside != mouseinside) {
					post(inside, MouseEvent.MOUSE_EXITED, me);
					if (mouseinside != null) post(mouseinside, MouseEvent.MOUSE_ENTERED, me);
				} else {
					post(mouseinside, id, me);
				}
			}
			else if (id == MouseEvent.MOUSE_DRAGGED) {
				Widget inside = mouseinside;
				MouseEvent me = (MouseEvent) e;
				mouseinside = getWidgetAt(me.getX(), me.getY());
				if (inside != mouseinside) {
					if (inside != null) post(inside, (inside == mousepressed) ?
						MouseEvent.MOUSE_EXITED : DRAG_EXITED, me);
					if (mouseinside != null) post(mouseinside, (mouseinside == mousepressed) ?
						MouseEvent.MOUSE_ENTERED : DRAG_ENTERED, me);
				} else {
					post(mousepressed, id, me);
					if ((mouseinside != null) && (mousepressed != mouseinside))
						post(mouseinside, DRAG_MOVED, me);
				}
			}
			else if (id == MouseEvent.MOUSE_WHEEL) {
				post(mouseinside, id, (MouseEvent) e);
			}
			
			else if (id == KeyEvent.KEY_PRESSED) {
				if (focusowner != null) focusowner.process((KeyEvent) e);
			}
			
			else if (id == FocusEvent.FOCUS_GAINED) {
				focused = true; if (focusowner != null) focusowner.process(e);
			}
			else if (id == FocusEvent.FOCUS_LOST) {
				focused = false; if (focusowner != null) focusowner.process(e);
			}
		}

		private void post(Widget widget, int id, MouseEvent me) {
			me = new MouseEvent(me.getComponent(), id,
				me.getWhen(), me.getModifiers(), me.getX(), me.getY(),
				me.getClickCount(), me.isPopupTrigger());
			for (Widget w = widget; w != RootPane.this; w = w.getParent()) {
				if (w == null) return;
				me.translatePoint(-w.getX(), -w.getY());
			}
			widget.process(me);
		}
		
		
		protected void paintComponent(Graphics g) { 
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight()); 
			
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			RootPane.this.paintImpl(g);
		}
	}
}
