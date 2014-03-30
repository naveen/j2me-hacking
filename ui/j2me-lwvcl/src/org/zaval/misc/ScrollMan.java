package org.zaval.misc;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * The interface is used by a scrolled object to move itself inside the scroll area.
 */
public interface ScrollMan
{
 /**
  * Moves the scrolled object inside the scroll area  to the specified location.
  * @param x the x coordinate of the specified location.
  * @param y the y coordinate of the specified location.
  */
  void moveScrolledObj(int x, int y);

 /**
  * Makes visible the specified part of the scrolled object inside the scroll area.
  * @param x the x coordinate of the specified part.
  * @param y the y coordinate of the specified part.
  * @param w the width of the specified part.
  * @param h the height of the specified part.
  */
  void makeVisible (int x, int y, int w, int h);
}
