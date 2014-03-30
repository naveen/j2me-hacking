package thinlet;

public class Spacer extends Widget {
	
	private int width;
	
	public Spacer() {
	}
	
	public Spacer(int width) {
		setWidth(width);
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(width, 0);
	}
	
	protected Widget getWidgetAt(int mx, int my) { return null; }
	protected boolean isFocusable() { return false; }
}
