package thinlet;

import java.awt.*;

public class Text extends Widget {
	
	private String text;
	
	public Text(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		FontMetrics fm = RootPane.getFontMetrics();
		return new Metrics(fm.stringWidth(text),
			fm.getAscent() + fm.getDescent(), fm.getAscent());
	}
	
	protected Widget getWidgetAt(int mx, int my) { return null; }
	protected boolean isFocusable() { return false; }
	
	protected void paint(Graphics g) {
		g.drawString(text, 0, g.getFontMetrics().getAscent());
	}
	

		

		



		




}
