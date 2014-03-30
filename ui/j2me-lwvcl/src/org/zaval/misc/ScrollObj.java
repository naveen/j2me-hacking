package org.zaval.misc;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface is used to describe and control an object that can be scrolled.
 * Principally, there are two ways to organize the scrolling for a light weight
 * component:
 * <ul>
 *   <li>
 *     By moving the component (changing the component location). For example, if we want
 *     to organize scrolling for org.zaval.port.j2me.Component, we should use <code>setLocation</code>
 *     method to scroll the component inside a parent container.
 *   </li>
 *   <li>
 *     By moving the component view. For example, if we want to create scrolling
 *     for org.zaval.port.j2me.Component, we should override <code>paint</code> method
 *     of the component and move view inside the method basing on the scrolling offsets.
 *   </li>
 * </ul>
 * The interface helps to support both ways.
 */
public interface ScrollObj
{
 /**
  * Gets the scroll object location.
  * @return a scroll object location.
  */
  Point getSOLocation();

 /**
  * Sets the specified scroll object location. The method defines a mechanism that will be used to
  * scrool the object.
  * @param x the specified x coordinate.
  * @param y the specified y coordinate.
  */
  void setSOLocation(int x, int y);

 /**
  * Gets the scroll object size. The size is a size that the scroll object wants to have.
  * @return a scroll object size.
  */
  Dimension getSOSize();

 /**
  * Sets the specified scroll manager for the scroll object. The manager reference should
  * be stored and used by the scrolled object to move itself inside the scrolled area.
  * @param m the specified scroll manager.
  */
  void setScrollMan(ScrollMan m);

 /**
  * Tests if the scrolled component performs scrolling by changing its location or moving view.
  * @return <code>true</code> if the scroll component organizes scrolling by moving
  * its view; otherwise <code>false</code>.
  */
  boolean moveContent();
}
