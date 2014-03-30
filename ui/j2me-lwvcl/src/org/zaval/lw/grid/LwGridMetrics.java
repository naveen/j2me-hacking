package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface is used to get values for basic metrical characteristics of the
 * grid component. For example, it is possible to get columns widths or rows heights
 * and so on. The interface is supposed to be used with components that implement
 * decorative elements for the grid component (for example grid caption).
 */
public interface LwGridMetrics
{
 /**
  * Gets the specified column preferred width.
  * @param col the specified grid column.
  * @return a preferred width of the specified column.
  */
  int getColPSWidth(int col);

 /**
  * Gets the specified row preferred height.
  * @param row the specified grid row.
  * @return a preferred height of the specified row.
  */
  int getRowPSHeight(int row);

 /**
  * Gets the number of the grid rows.
  * @return a number of the grid rows.
  */
  int getGridRows();

 /**
  * Gets the number of the grid columns.
  * @return a number of the grid columns.
  */
  int getGridCols();

 /**
  * Gets the specified row height.
  * @param row the specified grid row.
  * @return a height of the specified row.
  */
  int getRowHeight(int row);

 /**
  * Gets the specified column width.
  * @param col the specified grid column.
  * @return a width of the specified column.
  */
  int getColWidth (int col);

 /**
  * Sets the specified height for the given row.
  * @param row the specified row.
  * @param h the specified height.
  */
  void setRowHeight(int row, int h);

 /**
  * Sets the specified width for the given column.
  * @param col the specified column.
  * @param w the specified width.
  */
  void setColWidth (int col, int w);

 /**
  * Returns an x origin of the grid component. The origin defines an offset of the component view
  * relatively the component point of origin.
  * @return an origin of the component.
  */
  int getXOrigin ();

 /**
  * Returns an y origin of the grid component. The origin defines an offset of the component view
  * relatively the component point of origin.
  * @return an origin of the component.
  */
  int getYOrigin ();

 /**
  * Gets the cells insets. The insets defines top, left, right and bottom indents
  * inside grid cells.
  * @return a cells insets.
  */
  Insets getCellInsets ();

 /**
  * Gets the gap. The gap defines size of area that is used to paint horizontal and
  * vertical grid lines.
  * @return a gap.
  */
  int getNetGap();

 /**
  * Gets the x coordinate of the specified grid column.
  * @param col the specified column.
  * @return a <code>x</code> coordinate of the specified grid column.
  */
  int getColX(int col);

 /**
  * Gets the y coordinate of the specified grid row.
  * @param row the specified row.
  * @return an <code>y</code> coordinate of the specified grid row.
  */
  int getRowY(int row);

 /**
  * Gets the grid visibility.
  * @return a grid visibility.
  */
  CellsVisibility getCellsVisibility();

 /**
  * Gets the metric type.
  * @return <code>true</code> if the preferred size metric type is used;
  * <code>false</code> otherwise.
  */
  boolean isUsePsMetric();
}


