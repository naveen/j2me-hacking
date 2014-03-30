package thinlet;

import java.awt.*;

public class Constraint {

	public static final int LEFT = 0, RIGHT = 1, CENTER = 2, 
		BASELINE = 0, TOP = 1, BOTTOM = 2, MIDDLE = 3, 
		FILL = 4; 

	private int colspan = 1, rowspan = 1;
	private int weightx, weighty;
	private Insets padding;
	private int halign, valign;
	private int width = -1, height = -1;
	
	int getColSpan() { return colspan; }
	public void setColSpan(int colspan) { this.colspan = colspan; }
	
	int getRowSpan() { return rowspan; }
	public void setRowSpan(int rowspan) { this.rowspan = rowspan; }
	
	int getWeightX() { return weightx; }
	public void setWeightX(int weightx) { this.weightx = weightx; }
	
	int getWeightY() { return weighty; }
	public void setWeightY(int weighty) { this.weighty = weighty; }
	
	Insets getPadding() { return padding; }
	public void setPadding(int top, int left, int bottom, int right) {
		padding = new Insets(top, left, bottom, right);
	}
	
	int getHorizontalAlignment() { return halign; }
	public void setHorizontalAlignment(int halign) { this.halign = halign; }

	int getVerticalAlignment() { return valign; }
	public void setVerticalAlignment(int valign) { this.valign = valign; }

	int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }

	int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }
}
