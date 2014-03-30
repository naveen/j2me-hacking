package org.zaval.data;

import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface provides ability to manipulate with matrix-like data structures.
 */
public interface MatrixModel
{
 /**
  * Gets the number of rows.
  * @return a number of rows.
  */
  int getRows ();

 /**
  * Gets the number of columns.
  * @return a number of columns.
  */
  int getCols ();

 /**
  * Gets the value of the specified cell.
  * @param row the specified row.
  * @param col the specified column.
  * @return a value.
  */
  Object get(int row, int col);

 /**
  * Updates the specified cell with the specified value.
  * @param row the specified row.
  * @param col the specified column.
  * @param obj the specified value to update the cell value.
  */
  void put(int row, int col, Object obj);

 /**
  * Removes the specified number of rows starting from the given row.
  * @param begrow the first removed row.
  * @param count the number of rows to be removed starting from the first row.
  */
  void removeRows(int begrow, int count);

 /**
  * Removes the specified number of columns starting from the given column.
  * @param begcol the first removed column.
  * @param count the number of columns to be removed starting from the first column.
  */
  void removeCols(int begcol, int count);

 /**
  * Adds the matrix listener to be notified whenever the matrix cell has been updated or
  * the matrix dimension has been changed.
  * @param l the matrix listener.
  */
  void addMatrixListener   (MatrixListener l);

 /**
  * Removes the matrix listener.
  * @param l the matrix listener.
  */
  void removeMatrixListener(MatrixListener l);
}

