package org.zaval.data;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This class is an implementation of the <code>MatrixModel</code> interface to work with
 * matrix-like structures.
 */
public class Matrix
implements MatrixModel
{
   private Object[][] objs;
   private int        rows, cols, alloc_rows, alloc_cols;
   private Vector support;

  /**
   * Constructs the matrix component with the specified number of rows an columns.
   * @param rows the specified number of rows.
   * @param cols the specified number of columns.
   */
   public Matrix(int rows, int cols) {
     setSize(rows, cols);
   }

  /**
   * Gets the number of rows.
   * @return a number of rows.
   */
   public int getRows () {
     return rows;
   }

  /**
   * Gets the number of columns.
   * @return a number of columns.
   */
   public int getCols () {
     return cols;
   }

  /**
   * Sets the number of rows.
   * @param rows the specified number of rows.
   */
   public void setRows (int rows) {
     setSize(rows, cols);
   }

  /**
   * Sets the number of columns.
   * @param cols the specified number of columns.
   */
   public void setCols (int cols) {
     setSize(rows, cols);
   }

  /**
   * Sets the specified matrix dimension.
   * @param rows the specified number of rows.
   * @param cols the specified number of columns.
   */
   public void setSize (int rows, int cols)
   {
     if (rows != this.rows || cols != this.cols)
     {
       if (alloc_cols < cols||
           alloc_rows < rows  )
       {
          Object[][] nobjs = new Object[rows][cols]; /*java*/

          /*C#object[][] nobjs = new object[rows][];*/
          /*C#for (int i=0;i<rows;i++) nobjs[i] = new object[cols];*/

          alloc_cols = cols;
          alloc_rows = rows;
          if (objs != null) copy (objs, nobjs);
          objs = nobjs;
       }
       else
       {
         if (cols < this.cols)
         {
           for (int i=cols; i<this.cols; i++)
             for (int j=0; j<this.rows; j++)
               objs[j][i] = null;
         }

         if (rows < this.rows)
         {
           for (int i=0; i<cols; i++)
             for (int j=rows; j<this.rows; j++)
               objs[j][i] = null;
         }
       }

       int pc = this.cols;
       int pr = this.rows;
       this.cols = cols;
       this.rows = rows;
       perform (pr, pc, null, true);
     }
   }

  /**
   * Gets the matrix dimension. The result is represented with org.zaval.port.j2me.Dimension
   * class where <code>width</code> field correspond to number of rows and <code>height</code>
   * field correspond to number of columns.
   * @return a matrix dimension.
   */
   public Dimension getSize () {
     return new Dimension (rows, cols);
   }

  /**
   * Updates the specified cell with the specified value. If the specified row or column
   * is out of bounds the matrix dimension than the matrix size will be extended
   * automatically.
   * @param row the specified row.
   * @param col the specified column.
   * @param obj the specified value to update the cell value.
   */
   public void put(int row, int col, Object obj)
   {
      int nr = getRows();
      int nc = getCols();
      if (row >= nr) nr += (row - nr + 1);
      if (col >= nc) nc += (col - nc + 1);
      setSize(nr, nc);

      Object old = objs[row][col];
      if ((obj == null && obj != old      )||
          (obj != null && !obj.equals(old))  )
      {
        objs[row][col] = obj;
        perform(row, col, old, false);
      }
   }

  /**
   * Updates a cell with the specified value at the specified index. Any cell of the
   * matrix object can be identified by row and column or by index. The index for the
   * specified cell (row, col) is calculated using following formula:
   * <code>((row*columns) + col)</code>. If the index is out of bounds the matrix dimension
   * than the matrix size will be extended automatically.
   * @param index the specified index.
   * @param obj the specified value to update the cell value.
   */
   public Point put(int index, Object obj)
   {
      Point p = MathBox.index2point(index, getCols());
      put (p.x, p.y, obj);
      return p;
   }

  /**
   * Gets the value of the specified cell.
   * @param row the specified row.
   * @param col the specified column.
   * @return a value.
   */
   public Object get(int row, int col) {
     return objs[row][col];
   }

  /**
   * Gets the value at the specified cell index.
   * @param index the specified cell index.
   * @return a value.
   */
   public Object get(int index) {
     Point p = MathBox.index2point(index, getCols());
     return objs[p.x][p.y];
   }

   public void removeRows(int begrow, int count)
   {
     if (begrow < 0 || begrow + count > rows) throw new IllegalArgumentException();

     for (int i=(begrow + count); i<rows; i++, begrow++)
     {
       for (int j=0; j<cols; j++)
       {
         objs[begrow][j] = objs[i][j];
         objs[i][j] = null;
       }
     }
     rows -= count;
     perform(rows + count, cols, null, true);
   }

   public void removeCols(int begcol, int count)
   {
     if (begcol< 0 || begcol + count > cols) throw new IllegalArgumentException();

     for (int i=(begcol+count); i<cols; i++, begcol++)
     {
       for (int j=0; j<rows; j++)
       {
         objs[j][begcol] = objs[j][i];
         objs[j][i]      = null;
       }
     }
     cols -= count;
     perform(rows, cols + count, null, true);
   }

  /**
   * Adds the matrix listener to be notified whenever the matrix cell has been updated or
   * the matrix dimension has been changed.
   * @param m the matrix listener.
   */
   public void addMatrixListener (MatrixListener m) {
     if (support == null) support = new Vector(1);
     if (!support.contains(m)) support.addElement(m);
   }

  /**
   * Removes the matrix listener.
   * @param m the matrix listener.
   */
   public void removeMatrixListener (MatrixListener m) {
     if (support != null) support.removeElement(m);
   }

  /**
   * Fires the matrix event to the registered matrix listeners.
   * @param row the specified row of the cell that has been updated or previous rows number.
   * @param col the specified column of the cell that has been updated or previous columns number.
   * @param prevValue the specified previous cell value.
   * @param isResized the specified event type. Use <code>true</code> if the matrix has been resized
   * and use <code>false</code> if the matrix cell has been modified.
   */
   protected /*C#virtual*/ void perform(int row, int col, Object prevValue, boolean isResized)
   {
     if (support != null)
       for (int i=0; i < support.size(); i++)
       {
         if (isResized) ((MatrixListener)support.elementAt(i)).matrixResized(this, row, col);
         else           ((MatrixListener)support.elementAt(i)).cellModified (this, row, col, prevValue);
       }
   }

   private void copy (Object[][] s, Object[][] d)
   {
      int sr = s != null?s.length:0;
      int dr = d != null?d.length:0;

      int rows = Math.min(sr, dr);
      int cols = Math.min(sr > 0?((Object[])s[0]).length:0,
                          dr > 0?((Object[])d[0]).length:0);

      for (int i=0; i<rows; i++)
        for (int j=0; j<cols; j++)
          d[i][j] = s[i][j];
   }
}
