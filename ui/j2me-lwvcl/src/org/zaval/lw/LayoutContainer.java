package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is basic interface for components that can contain Layoutable components as its children.
 * The lightweight layout managers are defined for a component that implements the interface.
 * Actually, layout managers are used to layout the layout container child components.
 */
public interface LayoutContainer
extends Layoutable
{
 /**
  * Gets the child element at the given index.
  * @param index the index of the child to be returned.
  * @return a child element at the specified index.
  */
  Layoutable get(int index);

 /**
  * Returns the number of child elements in this container.
  * @return a number of child elements in this container.
  */
  int count();

 /**
  * Returns the offset. The offset can be used with a layout manager to offset child components
  * locations of the container according to the offset values. The offset is represented
  * with org.zaval.port.j2me.Point class, the <code>x</code> is offset for x-coordinates and <code>y</code>
  * is offset for y-coordinates. The ability to offset child components with a layout manager
  * is used with the library to organize scrolling.
  * @return an offset to move children.
  */
  Point getLayoutOffset();
}


