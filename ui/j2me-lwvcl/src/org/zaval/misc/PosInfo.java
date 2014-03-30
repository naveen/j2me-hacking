package org.zaval.misc;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This interface is used to provide information for a PosController. The information will
 * be used with the controller to organize virtual cursor support.
 */
public interface PosInfo
{
 /**
  * Gets the number of lines.
  * @return a number of lines.
  */
  int getLines   ();

 /**
  * Gets the line size for the specified line number.
  * @param line the specified line number.
  * @return a size of the line.
  */
  int getLineSize(int line);

 /**
  * Gets the maximal offset. The method can return -1 as the result, in this case the pos
  * controller that uses the pos info will try to calculate the maximal offset itself basing
  * on the line number and the lines sizes.
  * @return a maximal offset.
  */
  int getMaxOffset();
}
