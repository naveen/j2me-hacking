package thinlet;

import java.awt.*;
import java.awt.event.*;

public class Slider extends Widget {
	
	private int minimum;
	private int maximum = 100;
	private int value;
	private Listener changeListener;
	
	public Slider() {
	}

	public void setMinimum(int minimum) {
		if (this.minimum != minimum) {
			this.minimum = minimum;
			repaint();
		}
	}

	public int getMinimum() {
		return minimum; }
	
	public void setMaximum(int maximum) {
		if (this.maximum != maximum) {
			this.maximum = maximum;
			repaint();
		}
	}

	public int getMaximum() {
		return maximum;
	}

	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;
			Listener.invoke(changeListener);
			repaint();
		}
	}

	public int getValue() {
		return value;
	}
	
	public void addChangeListener(Listener listener) {
		changeListener = Listener.add(changeListener, listener);
	}
	
	public void removeChangeListener(Listener listener) {
		changeListener = Listener.remove(changeListener, listener);
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(96, 12);
	}
	
	protected void process(AWTEvent e) {
		if ((e.getID() == MouseEvent.MOUSE_PRESSED) ||
				(e.getID() == MouseEvent.MOUSE_DRAGGED)) {
			setValue(Math.max(minimum, Math.min(
				minimum + (((MouseEvent) e).getX() - 6) *
					(maximum - minimum) / (getWidth() - 12), maximum)));
		}
	}
	
	protected void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int w = getWidth(), y = (getHeight() - 12) / 2,
			length = (value - minimum) * (w - 12) / (maximum - minimum);
		
		g2.setPaint(new GradientPaint(
			0, 3, new Color(0xc0c0c0), 0, 9, new Color(0xe0e0e0)));
		g.fillRoundRect(1, y + 3, w - 2, 6, 4, 4);
		g.setColor(new Color(0xa0a0a0));
		g.drawRoundRect(0, y + 2, w - 1, 7, 6, 6);
		
		g2.setPaint(new GradientPaint(
			0, 1, new Color(0xf0c070), 0, 10, new Color(0xf07030)));
		g.fillOval(length, y, 12, 12);
		g.setColor(new Color(0xe06000));
		g.drawOval(length, y, 11, 11);
	}
}
