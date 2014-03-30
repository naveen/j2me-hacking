package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface is used to provide a view for the specified grid cell, it
 * allows you to customize grid cells' views.
 */
public interface LwGridViewProvider
{
 /**
  * Gets the view for the specified cell and the given data model value.
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @param o the specified data model value.
  * @return a view.
  */
  LwView getView(int row, int col, Object o);

 /**
  * Gets the horizontal alignment for the specified grid cell.
  * The method should return one of the following values:
  * <ul>
  *   <li>LwToolkit.CENTER</li>
  *   <li>LwToolkit.LEFT</li>
  *   <li>LwToolkit.RIGHT</li>
  * </ul>
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @return a horizontal alignment.
  */
  int getXAlignment(int row, int col);

 /**
  * Gets the vertical alignment for the specified grid cell.
  * The method should return one of the following values:
  * <ul>
  *   <li>LwToolkit.CENTER</li>
  *   <li>LwToolkit.TOP</li>
  *   <li>LwToolkit.BOTTOM</li>
  * </ul>
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @return a vertical alignment.
  */
  int getYAlignment(int row, int col);

 /**
  * Gets the specified grid cell color. The color is used to fill the
  * cell background. If the cell has not own background than the method
  * should return <code>null</code>.
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @return a color.
  */
  Color getCellColor (int row, int col);
}



