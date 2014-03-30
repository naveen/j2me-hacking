package thinlet;




public class Tree extends ScrollPanel {

	public Tree() {
		for (int i = 1; i <= 3; i++) addNode(createNode(0, "Node", i));
	}
	
	private static final Node createNode(int level, String text, int index) {
		Node node = new Node();
		node.add(new Text(text = text + '-' + index));
		if (level < 3) for (int i = 1; i <= 3; i++)
			node.addNode(createNode(level + 1, text, i));
		return node;
	}
	
	public void addNode(Widget widget) {
		getContent().add(widget);
	}
	
	protected Metrics layoutContent(int preferredWidth) {
		int y = 0, baseline = -1;
		for (Widget widget = getContent().getChild(); widget != null; widget = widget.getNext()) {
			Metrics metrics = widget.getPreferredSize(preferredWidth);
			widget.setBounds(0, y, preferredWidth, metrics.getHeight());
			if (y == 0) baseline = metrics.getBaseline();
			y += metrics.getHeight();
		}
		return new Metrics(preferredWidth, y, baseline);
	}
}
