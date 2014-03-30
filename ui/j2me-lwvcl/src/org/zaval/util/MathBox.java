package org.zaval.util;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class provides set of useful math methods.
 */
public class MathBox
{
 /**
  * Returns the dimension that has <code>width</code>, <code>height</code>
  * fields calculated as maximal values for the
  * specified dimension objects.
  * @param a the specified dimension object.
  * @param b the specified dimension object.
  * @return a maximal dimension object.
  */
  public static Dimension max (Dimension a, Dimension b) {
    return new Dimension (Math.max (a.width, b.width), Math.max (a.height, b.height));
  }

 /**
  * Computes and returns the row and column for the specified offset and the given
  * number of columns. The sample below shows the offset
  * meaning:
  * <br>
  * <table border="1" cellspacing="1">
  *   <tr  class="tb2">
  *     <td>row=0, column=0, offset=0</td>
  *     <td>row=0, column=1, offset=1</td>
  *     <td>row=0, column=2, offset=2</td>
  *   </tr>
  *   <tr  class="tb3">
  *     <td>row=1, column=0, offset=3</td>
  *     <td>row=1, column=1, offset=4</td>
  *     <td>row=1, column=2, offset=5</td>
  *   </tr>
  *   <tr  class="tb2">
  *     <td>row=2, column=0, offset=6</td>
  *     <td>row=2, column=1, offset=7</td>
  *     <td>row=2, column=2, offset=8</td>
  *   </tr>
  * </table>
  * @param offset the specified offset.
  * @param cols the number of columns.
  * @return a row and column. The result is represented with org.zaval.port.j2me.Point class
  * where the <code>x</code> field corresponds to the row and the <code>y</code> field
  * correspond to the column.
  */
  public static Point index2point(int offset, int cols) {
    if (offset < 0 || cols == 0) throw new IllegalArgumentException();
    return new Point((offset / cols), (offset % cols));
  }

 /**
  * Converts the specified row and column to the offset. The following formula is used to
  * calculate the offset: <b>(row*columns) + column</b>
  * @param row the specified row.
  * @param col the specified column.
  * @param cols the number of columns.
  * @return an offset.
  */
  public static int indexByPoint(int row, int col, int cols) {
    return (cols <= 0)?-1:((row*cols) + col);
  }

 /**
  * Tests if the specified bit mask has the given bit set.
  * @param bits the specified bit mask.
  * @param mask the specified bit to be test.
  * @return <code>true</code> if the bit "ON"  for the given bit mask;
  * otherwise <code>false</code>.
  */
  public static boolean checkBit(int bits, int mask) {
    return (bits & mask) > 0;
  }

 /**
  * Sets the bits value for the specified bit mask and returns the new bit mask.
  * @param bits the specified bit mask.
  * @param mask the specified bits to be set.
  * @param b use <code>true</code> to set the specified bits to
  * <code>1</code>; otherwise use <code>false</code>.
  * @return a new bit mask.
  */
  public static int getBits(int bits, int mask, boolean b) {
    return (b?(bits | mask):(bits & ~mask));
  }

 /**
  * Gets the intersection rectangle for the two specified rectangular areas.
  */
  public static void intersection(int x1, int y1, int w1, int h1,
                                  int x2, int y2, int w2, int h2, Rectangle r)
  {
    r.x      = Math.max(x1, x2);
    r.width  = Math.min(x1 + w1, x2 + w2) - r.x;
    r.y      = Math.max(y1, y2);
    r.height = Math.min(y1 + h1, y2 + h2) - r.y;
  }

 /**
  * Unites the two specified rectangular and returns the result.
  * @param x1 the top-left x coordinate of the first rectangular area.
  * @param y1 the top-left y coordinate of the first rectangular area.
  * @param w1 the width of the first rectangular area.
  * @param h1 the height of the first rectangular area.
  * @param x2 the top-left x coordinate of the second rectangular area.
  * @param y2 the top-left y coordinate of the second rectangular area.
  * @param w2 the width of the second rectangular area.
  * @param h2 the height of the second rectangular area.
  * @param r the result.
  */
  public static void unite (int x1, int y1, int w1, int h1,
                            int x2, int y2, int w2, int h2, Rectangle r)
  {
     r.x      = Math.min(x1, x2);
     r.y      = Math.min(y1, y2);
     r.width  = Math.max(x1 + w1, x2 + w2) - r.x;
     r.height = Math.max(y1 + h1, y2 + h2) - r.y;
  }
}

