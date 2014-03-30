package thinlet;

import java.awt.*;

public class ProgressBar extends Widget {
	
	private int minimum;
	private int maximum = 100;
	private int value = 33;
	
	public ProgressBar() {
	}

	public void setMinimum(int minimum) {
		if (this.minimum != minimum) {
			this.minimum = minimum; repaint();
		}
	}

	public int getMinimum() { return minimum; }
	
	public void setMaximum(int maximum) {
		if (this.maximum != maximum) {
			this.maximum = maximum; repaint();
		}
	}

	public int getMaximum() { return maximum; }

	public void setValue(int value) {
		if (this.value != value) {
			this.value = value; repaint();
		}
	}

	public int getValue() { return value; }
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(96, 8);
	}
	
	protected boolean isFocusable() { return false; }
	
	protected void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int w = getWidth(), h = getHeight(),
			length = (value - minimum) * w / (maximum - minimum);
		
		g.setColor(new Color(0xa0a0a0));
		g.drawRect(0, 0, w - 1, h - 1);
		g2.setPaint(new GradientPaint(
			0, 1, new Color(0xc0c0c0), 0, h - 2, new Color(0xe0e0e0)));
		g.fillRect(1, 1, w - 2, h - 2);

		g.setColor(new Color(0xe06000));
		g.drawRect(0, 0, length - 1, h - 1);
		g2.setPaint(new GradientPaint(
			0, 1, new Color(0xf0c070), 0, h - 2, new Color(0xf07030)));
		g.fillRect(1, 1, length - 2, h - 2);
	}
	




}
