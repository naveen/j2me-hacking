package org.zaval.lw;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This class implements layout manager interface. The layout manager divides the container area
 * into certain quantity of virtual rows and columns and every cell is used to place a
 * child component. A column preferred width is calculated as maximal preferred width among
 * all cells of the column. A row preferred height is calculated as maximal preferred height
 * among all cells of the row.
 * <p>
 * If the virtual cell space is larger than the component preferred size than the additional
 * constraints can be defined. Use LwConstraints class as the input argument of <code>add</code>
 * container method to describe the cell constraints. Using the constraints you can define
 * vertical and horizontal alignments, insets, filling rules. The table below illustrates the
 * layout manager usage (the samples use grid layout manager with two columns and two rows):
 * <table border="1" width="100%" cellspacing="1">
 *   <tr class="tb3">
 *     <td align="center"><b>Constraints</b></td>
 *     <td align="center"><b>Sample App</b></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>Default constraints (fill=LwToolkit.HORIZONTAL|LwToolkit.VERTICAL).</td>
 *     <td><img src="images/GridLayout1.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>Cell[1][1] uses fill=LwToolkit.HORIZONTAL, vertical alignment is LwToolkit.CENTER.</td>
 *     <td align="center"><img src="images/GridLayout2.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       Cell[1][1] uses fill=LwToolkit.NONE, vertical alignment is LwToolkit.CENTER and horizontal
 *       alignment is LwToolkit.CENTER.
 *
 *     </td>
 *     <td align="center"><img src="images/GridLayout3.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       Cell[1][1] uses fill=LwToolkit.NONE, vertical alignment is LwToolkit.TOP and horizontal
 *       alignment is LwToolkit.LEFT.
 *
 *     </td>
 *     <td align="center"><img src="images/GridLayout4.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       Cell[1][1] uses fill=LwToolkit.VERTICAL, horizontal alignment is LwToolkit.RIGHT.
 *     </td>
 *     <td align="center"><img src="images/GridLayout5.gif"></td>
 *   </tr>
 * </table>
 * <p>
 * Additionally you can use stretching mask that says if the virtual cells should be stretched
 * according to the target container size vertically or horizontally.
 */
public class LwGridLayout
implements LwLayout, org.zaval.misc.PosInfo
{
  private int       cols, rows, mask, count;
  private Hashtable args;

 /**
  * Constructs a new grid layout with the specified number of rows and columns.
  * @param r the specified number of rows.
  * @param c the specified number of columns.
  */
  public LwGridLayout(int r, int c) {
    this(r, c, 0);
  }

 /**
  * Constructs a new grid layout with the specified number of rows, columns and the layout stretching mask.
  * @param r the specified number of rows.
  * @param c the specified number of columns.
  * @param m the layout stretching mask. The mask indicates if it is necessary to stretch
  * virtual cells' sizes according to a parent area size. Use LwToolkit.HORIZONTAL, LwToolkit.VERTICAL or
  * LwToolkit.NONE values to construct the mask value.
  */
  public LwGridLayout(int r, int c, int m)
  {
    rows = r;
    cols = c;
    mask = m;
  }

 /**
  * Calculates the preferred size dimension for the layout container.
  * The method calculates "pure" preferred size, it means that an insets
  * of the container is not considered.
  * @param c the layout container.
  */
  public Dimension calcPreferredSize (LayoutContainer c) {
    return new Dimension(getColSizes(c)[cols], getRowSizes(c)[rows]);
  }

 /**
  * Lays out the child layoutable components inside the layout container.
  * @param c the layout container that needs to be laid out.
  */
  public void layout(LayoutContainer c)
  {
    int[]   colSizes = getColSizes(c);
    int[]   rowSizes = getRowSizes(c);
    Insets  insets   = c.getInsets();
    Point   offset   = c.getLayoutOffset();

    if ((mask & LwToolkit.HORIZONTAL) > 0)
    {
      int dw = c.getWidth() - insets.left - insets.right - colSizes[cols];
      for (int i=0; i<cols; i++)
        colSizes[i] = colSizes[i] + (colSizes[i] != 0?(dw*colSizes[i])/colSizes[cols]:0);
    }

    if ((mask & LwToolkit.VERTICAL) > 0)
    {
      int dh = c.getHeight() - insets.top - insets.bottom - rowSizes[rows];
      for (int i=0; i<rows; i++)
        rowSizes[i] = rowSizes[i] + (rowSizes[i] != 0?(dh*rowSizes[i])/rowSizes[rows]:0);
    }

    int yy = insets.top, cc = 0;
    for (int i = 0; i < rows && cc < count; i++)
    {
      int xx = insets.left;
      for (int j = 0; j < cols && cc < count; j++, cc++)
      {
        Layoutable l = c.get(cc);
        if (l.isVisible())
        {
          LwConstraints arg = getConstraints(l, args);
          Dimension     d   = l.getPreferredSize();

          int cellW = colSizes[j];
          int cellH = rowSizes[i];

          if (arg.insets != null)
          {
            cellW -=  (arg.insets.left + arg.insets.right);
            cellH -=  (arg.insets.top  + arg.insets.bottom);
          }

          if ((LwToolkit.HORIZONTAL & arg.fill) > 0) d.width  = cellW;
          if ((LwToolkit.VERTICAL   & arg.fill) > 0) d.height = cellH;

          Point p = LwToolkit.getLocation(d, arg.ax, arg.ay, cellW, cellH);

          l.setSize    (d.width, d.height);
          l.setLocation(xx + offset.x + (arg.insets==null?0:arg.insets.left) + p.x,
                        yy + offset.y + (arg.insets==null?0:arg.insets.top) + p.y);
          xx += colSizes[j];
        }
      }
      yy += rowSizes[i];
    }
  }

 /**
  * Invoked when the specified layoutable component is added to the layout container, that
  * uses the layout manager. The specified constraints, layoutable component and child index
  * are passed as arguments into the method. The method stores the specified constraints for
  * the component if the constraints are defined.
  * @param id the layoutable component constraints.
  * @param b the layoutable component.
  * @param index the child index.
  */
  public void componentAdded(Object id, Layoutable b, int index)
  {
    count++;
    if (count > cols * rows) throw new IllegalArgumentException();
    if (id != null)
    {
      if (args == null) args = new Hashtable();
      args.put(b, id);
    }
  }

 /**
  * Invoked when the specified layoutable component is removed from the layout
  * container, that uses the layout manager. The manager removes appropriate constraints
  * for the given component if the constraints have been defined.
  * @param lw the layoutable component to be removed
  * @param index the child index.
  */
  public void componentRemoved(Layoutable lw, int index) {
    count--;
    if (args != null) args.remove(lw);
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define number of lines. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @return a number of lines. The method returns number of rows as the number of lines.
  */
  public int getLines   () {
    return (cols == 0 || rows == 0 || count == 0)?0:(count-1)/cols + 1;
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define the line size. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @param line the line number.
  * @return a size of the specified line. The method returns number of columns as the size
  * for any line number.
  */
  public int getLineSize(int line) {
    return (line < getLines() - 1)?cols:(count-1)%cols + 1;
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define max offset. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @return a max offset. The max offset is computed as "(rows * columns - 1)" value.
  */
  public int getMaxOffset() {
    return count - 1;
  }

  private int[] getRowSizes(LayoutContainer c)
  {
    int[] res = new int[rows + 1];
    for (int i = 0; i<rows; i++)
    {
      res[i]     = getRowSize(i, c);
      res[rows] += res[i];
    }
    return res;
  }

  private int[] getColSizes(LayoutContainer c)
  {
    int[] res = new int[cols + 1];
    for (int i = 0; i<cols; i++)
    {
      res[i]     = getColSize(i, c);
      res[cols] += res[i];
    }
    return res;
  }

  private int getRowSize(int row, LayoutContainer c)
  {
    int max = 0, s = MathBox.indexByPoint(row, 0, cols);
    for (int i = s; i<c.count() && i < s + cols; i++)
    {
      Layoutable a = c.get(i);
      if (a.isVisible())
      {
        LwConstraints arg = getConstraints(a, args);
        Dimension d = a.getPreferredSize();
        if (arg.insets != null) d.height += (arg.insets.top + arg.insets.bottom);
        max = Math.max (d.height, max);
      }
    }
    return max;
  }

  private int getColSize(int col, LayoutContainer c)
  {
    int max = 0, r = 0, i = 0;
    while ((i = MathBox.indexByPoint(r, col, cols)) < c.count())
    {
      Layoutable a = c.get(i);
      if (a.isVisible())
      {
        LwConstraints arg = getConstraints(a, args);
        Dimension     d   = a.getPreferredSize();
        if (arg.insets != null) d.width += (arg.insets.left + arg.insets.right);
        max = Math.max (d.width, max);
      }
      r++;
    }
    return max;
  }

  private static LwConstraints getConstraints (Layoutable l, Hashtable args) {
    LwConstraints arg = args != null?(LwConstraints)args.get(l):DEF_CONSTR;
    return arg != null?arg:DEF_CONSTR;
  }

  private static final LwConstraints DEF_CONSTR = new LwConstraints();
}


