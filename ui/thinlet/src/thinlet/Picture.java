package thinlet;

import java.awt.*;
import java.awt.image.*;
import java.net.*;




public class Picture extends Widget implements ImageObserver {
	
	private static final Constraint constraint = new Constraint();
	static { constraint.setVerticalAlignment(Constraint.MIDDLE); }
	{ setConstraint(constraint); }
	
	private Image image;
	
	public Picture() {
	}
	
	public Picture(String source) {
		setSource(source);
	}
	
	public void setSource(String source) {
		URL url =
			getClass().getClassLoader().getResource(source); 
			
		if (url == null) throw new IllegalArgumentException(source);
		image = Toolkit.getDefaultToolkit().getImage(url);
		





		while (image.getWidth(this) < 0 || image.getHeight(this) < 0); 
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(image.getWidth(this), image.getHeight(this));
	}
	
	protected Widget getWidgetAt(int mx, int my) { return null; }
	protected boolean isFocusable() { return false; }
	
	protected void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}

	public boolean imageUpdate(Image image, int info, int x, int y, int width, int height) {
		if (((info & ImageObserver.ALLBITS) > 0) || ((info & ImageObserver.FRAMEBITS) > 0)) repaint();




		return true;
	}
}
