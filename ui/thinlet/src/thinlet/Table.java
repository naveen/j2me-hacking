package thinlet;

public class Table extends ScrollPanel {
	
	public Table() {
		addHeader(new Head("Text"));
		addHeader(new Head("Check"));
		addHeader(new Head("Slider"));
		for (int i = 1; i <= 6; i++) {
			Row row = new Row(); row.setLayout(null);
			row.add(new Text("Label-" + i));
			CheckBox checkBox = new CheckBox(); checkBox.add(new Text("Check-" + i));
			row.add(checkBox);
			row.add(new Slider());
			addRow(row);
		}
	}
	
	public void addHeader(Widget widget) {
		getHeader().add(widget);
	}
	
	public void addRow(Widget widget) {
		getContent().add(widget);
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(256, 96);
	}
	
	protected Metrics layoutContent(int preferredWidth) {
		int ncol = 0, nrow = 0;
		for (Widget widget = getContent().getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Row) {
				int cols = 0;
				for (Widget item = widget.getChild(); item != null; item = item.getNext()) {
					cols++;
				}
				ncol = Math.max(ncol, cols);
			}
			nrow++;
		}
		
		int x = 0, y = 0;
		for (Widget widget = getContent().getChild(); widget != null; widget = widget.getNext()) {
			widget.setBounds(0, 18 * y, preferredWidth, 18); y++;
			if (widget instanceof Row) {
				x = 0;
				for (Widget item = widget.getChild(); item != null; item = item.getNext()) {
					item.setBounds(80 * x, 0, 80, 18); x++;
				}
			}
		}
		return new Metrics(preferredWidth, nrow * 18);
	}
}
