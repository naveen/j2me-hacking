package thinlet;

public class Pane extends Widget {
	
	private static final Layout layout = new TableLayout(4, 4, 4, 4, 4, 4);
	{ setLayout(layout); }
	
	public Pane() {
	}
}
