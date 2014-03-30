package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class is used to represent grid cells visibility. Using the class it is possible
 * to determine what cells are visible, locations of the visible cells and so on.
 */
public class CellsVisibility
{
  protected Point fr = null;
  protected Point fc = null;
  protected Point lr = null;
  protected Point lc = null;

 /**
  * Gets the first visible cell.
  * @return the first visible cell's row and column. org.zaval.port.j2me.Point class is used to represent
  * first visible cell where <code>x</code> field is the cell row and <code>y</code> field
  * is the cell column.
  */
  public Point getFirstCell() {
    return hasVisibleCells()?new Point(fr.x, fc.x):null;
  }

 /**
  * Gets the last visible cell.
  * @return the last visible cell's row and column. org.zaval.port.j2me.Point class is used to represent
  * last visible cell where <code>x</code> field is the cell row and <code>y</code> field
  * is the cell column.
  */
  public Point getLastCell() {
    return hasVisibleCells()?new Point(lr.x, lc.x):null;
  }

 /**
  * Gets the first visible cell location.
  * @return a first visible cell location. org.zaval.port.j2me.Point class is used to represent
  * the location where <code>x</code> field is the x coordinate and <code>y</code> field
  * is the y coordinate.
  */
  public Point getFirstCellLoc() {
    return hasVisibleCells()?new Point(fc.y, fr.y):null;
  }

 /**
  * Gets the last visible cell location.
  * @return a last visible cell location. org.zaval.port.j2me.Point class is used to represent
  * the location where <code>x</code> field is the x coordinate and <code>y</code> field
  * is the y coordinate.
  */
  public Point getLastCellLoc() {
    return hasVisibleCells()?new Point(lc.y, lr.y):null;
  }

 /**
  * Clears the cells visibility.
  */
  public void cancelVisibleCells() {
    fr = null;
  }

 /**
  * Checks if there are any visible cells.
  * @return <code>true</code> if there are visible cells; otherwise <code>false</code>.
  */
  public boolean hasVisibleCells () {
    return (fr != null && fc != null&&
            lr != null && lc != null   );
  }
}
