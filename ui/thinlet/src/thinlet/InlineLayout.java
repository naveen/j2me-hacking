package thinlet;

public class InlineLayout extends Layout {
	
	public static final int LEFT = 0, CENTER = 1, RIGHT = 2, JUSTIFY = 3; 
	
	private int top, left, bottom, right; 
	private boolean singleLine;
	private int alignment; 
	private int indent; 
	
	public InlineLayout() {
	}
	
	public InlineLayout(int top, int left, int bottom, int right) {
		this.top = top; this.left = left; this.bottom = bottom; this.right = right;
	}
	
	public void setInsets(int top, int left, int bottom, int right) {
		this.top = top; this.left = left; this.bottom = bottom; this.right = right;
	}
	
	boolean isSingleLine() { return singleLine; }
	public void setSingleLine(boolean singleLine) { this.singleLine = singleLine; }
	
	int getAlignment() { return alignment; }
	public void setAlignment(int alignment) { this.alignment = alignment; }
	
	int getIndent() { return indent; }
	public void setIndent(int indent) { this.indent = indent; }
	
	public Widget.Metrics getPreferredSize(Widget target, int preferredWidth) {
		return measure(target, false, preferredWidth);
	}
	
	public void doLayout(Widget target) {
		measure(target, true, target.getWidth());
	}
	
	private Widget.Metrics measure(Widget target, boolean layout, int preferredWidth) {
		int w = preferredWidth - left - right, x = indent, height = 0, ascent = -1, descent = 0;
		Widget.Metrics first = null, current = null;
		for (Widget widget = target.getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Break) { 
				continue;
			}

			Widget.Metrics metrics = widget.getPreferredSize(w);
			if (first == null) { first = current = metrics; }
				else { current.next = metrics; current = metrics; }

			x += metrics.getWidth();
			Constraint constraint = widget.getConstraint();
			if (constraint.getVerticalAlignment() == Constraint.BASELINE) {
				int bl = metrics.getBaseline();
				if (bl != -1) { 
					ascent = Math.max(bl, ascent);
					descent = Math.max(metrics.getHeight() - bl, descent);
				} else { 
					ascent = Math.max(metrics.getHeight(), ascent);
				}
			} else { 
				height = Math.max(metrics.getHeight(), height);
			}
		}
		
		if (!layout) return target.new Metrics(x + left + right,
			Math.max(height, ascent + descent) + top + bottom,
			(ascent != -1) ? (top + ascent) : top);
		
		if (ascent != -1) {
			if (ascent + descent < height) ascent = (height + ascent - descent + 1) / 2; 
				else height = ascent + descent;
		}
		x = ((alignment == RIGHT) ? (w - x) : (alignment == CENTER) ? ((w - x) / 2) : 0) + indent;
		
		for (Widget.Metrics metrics = first; metrics != null; metrics = metrics.next) {
			Widget widget = metrics.getWidget();
			int y = 0, h = metrics.getHeight();
			switch (widget.getConstraint().getVerticalAlignment()) {
				case Constraint.TOP: break;
				case Constraint.BOTTOM: y = height - h; break;
				case Constraint.MIDDLE: y = (height - h) / 2; break;
				case Constraint.FILL: h = height; break;
				case Constraint.BASELINE:
					int bl = metrics.getBaseline();
					y = ascent - ((bl != -1) ? bl : h);
			}
			widget.setBounds(left + x, top + y, metrics.getWidth(), h);
			x += metrics.getWidth();
		}
		return null;
	}
}
	










	



		




















			





			


















