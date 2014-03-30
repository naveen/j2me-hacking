package thinlet;

public class TableLayout extends Layout {

	private int top, left, bottom, right; 
	private int hgap, vgap; 
	
	public TableLayout() {
	}
	
	public TableLayout(int hgap, int vgap,
				int top, int left, int bottom, int right) {
		this.hgap = hgap; this.vgap = vgap;
		this.top = top; this.left = left; this.bottom = bottom; this.right = right;
	}
	
	public void setInsets(int top, int left, int bottom, int right) {
		this.top = top; this.left = left; this.bottom = bottom; this.right = right;
	}

	public void setGap(int hgap, int vgap) { this.hgap = hgap; this.vgap = vgap; }
	
	public Widget.Metrics getPreferredSize(Widget target, int preferredWidth) {
		return measure(target, false);
	}
	
	public void doLayout(Widget target) {
		measure(target, true);
	}
	
	private Widget.Metrics measure(Widget target, boolean layout) {
		int w = target.getWidth() - left - right;
		int[] colheights = null;
		int ix = 0, iy = 0, ncol = 0, nrow = 0;
		Widget.Metrics first = null, current = null;
		
		for (Widget widget = target.getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Break) { 
				if (ix != 0) { ix = 0; iy++; } continue;
			}
			
			Widget.Metrics metrics = widget.getPreferredSize(w);
			if (first == null) { first = current = metrics; }
				else { current.next = metrics; current = metrics; }
			
			Constraint constraint = widget.getConstraint();
			int colspan = constraint.getColSpan(), rowspan = constraint.getRowSpan();
			if (colheights != null) for (int j = 0; j < colspan; j++) {
				if (colheights[ix + j] > iy) {
					ix += (j + 1); j = -1; 
				}
			}
			
			metrics.x = ix; metrics.y = iy; 
			ncol = Math.max(ncol, ix + colspan); 
			nrow = Math.max(nrow, iy + rowspan);
			
			if (rowspan > 1) {
				if (colheights == null) {
					colheights = new int[ncol];
				} else if (colheights.length < ncol) {
					int[] newheights = new int[ncol];
					System.arraycopy(colheights, 0, newheights, 0, colheights.length);
					colheights = newheights;
				}
				for (int j = 0; j < colspan; j++) {
					colheights[ix + j] = iy + rowspan; 
				}
			}
			
			ix += colspan; 
		}

		int[] colwidths = new int[ncol], colweights = new int[ncol],
			rowheights = new int[nrow], rowweights = new int[nrow];
		align(first, colweights, null, true);
		align(first, colwidths, colweights, true);
		align(first, rowweights, null, false);
		align(first, rowheights, rowweights, false);
		
		if (!layout) return target.new Metrics(sum(colwidths, 0, ncol, hgap) + left + right,
			sum(rowheights, 0, nrow, vgap) + top + bottom,
			((nrow > 0) ? 12 : 0) + top);
		
		for (Widget.Metrics metrics = first; metrics != null; metrics = metrics.next) {
			Widget widget = metrics.getWidget();
			Constraint constraint = widget.getConstraint();
			int x = left + sum(colwidths, 0, metrics.x, hgap) + ((metrics.x > 0) ? hgap : 0),
				y = top + sum(rowheights, 0, metrics.y, vgap) + ((metrics.y > 0) ? vgap : 0),
				width = sum(colwidths, metrics.x, constraint.getColSpan(), hgap),
				height = sum(rowheights, metrics.y, constraint.getRowSpan(), vgap);
				height = metrics.getHeight(); 
			widget.setBounds(x, y, width, height);
		}
		return null;
	}
	
	private static final void align(Widget.Metrics first, int[] values, int[] weights, boolean horizontal) {
		for (int size = 1, next = 0; size != 0; size = next, next = 0) {
			for (Widget.Metrics metrics = first; metrics != null; metrics = metrics.next) {
				Constraint constraint = metrics.getWidget().getConstraint();
				int span = horizontal ? constraint.getColSpan() : constraint.getRowSpan();
				if (span == size) {
					int value = (weights != null) ?
						(horizontal ? metrics.getWidth() : metrics.getHeight()) :
							(horizontal ? constraint.getWeightX() : constraint.getWeightY());
					distribute(values, horizontal ? metrics.x : metrics.y, span,
						(weights != null) ? weights : values, value);
				}
				else if ((span > size) && ((next == 0) || (next > span))) next = span;
			}
		}
	}
	
	private static final void distribute(int[] values, int from, int length, int[] weights, int value) {
		if (length == 1) {
			values[from] = Math.max(values[from], value); return;
		}
		int diff = value, sum = 0;
		for (int i = from, n = from + length; i < n; i++) {
			if (diff <= values[i]) return;
			diff -= values[i]; sum += weights[i];
		}
		for (int i = from, n = from + length - 1; i < n; i++) {
			if (weights[i] == 0) continue;
			int d = weights[i], inc = d * diff / sum;
			values[i] += inc;
			if (sum <= d) return;
			diff -= inc; sum -= d;
		}
		values[from + length - 1] += diff;
	}
	
	private static final int sum(int[] values, int from, int length, int gap) {
		int sum = 0;
		for (int i = 0; i < length; i++) sum += values[from + i];
		if (length > 1) sum += (length - 1) * gap;
		return sum;
	}
}
