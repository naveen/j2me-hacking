package thinlet;

import java.awt.*;
import java.awt.event.*;

public class List extends ScrollPanel {
	
	public List() {
		for (int i = 0; i < 8; i++) {
			Row row = new Row();
			
			row.add(new Text(" Row-" + (char) ('A' + i)));
			addItem(row);
		}
	}
	
	protected void process(AWTEvent e) {
		switch (e.getID()) {
		case MouseWheelEvent.MOUSE_WHEEL:
			System.out.println(e);
		}
	}
	
	public void addItem(Widget widget) {
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
