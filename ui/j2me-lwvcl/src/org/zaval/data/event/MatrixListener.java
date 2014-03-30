package org.zaval.data.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is listener interface for receiving matrix events.
 */
public interface MatrixListener
extends EventListener
{
 /**
  * Invoked when a dimension of the matrix model (number of rows or number of columns) has
  * been changed.
  * @param target the target matrix model.
  * @param prevRows the previous number of rows that the matrix model had before.
  * @param prevCols the previous number of columns that the matrix model had before.
  */
  void matrixResized(Object target, int prevRows, int prevCols);

 /**
  * Invoked when a cell of the matrix model has been updated.
  * @param target the target matrix model.
  * @param row the row of the matrix model cell that has been updated.
  * @param col the column of the matrix model cell that has been updated.
  * @param prevValue the previous value of the modified cell.
  */
  void cellModified (Object target, int row, int col, Object prevValue);
}
