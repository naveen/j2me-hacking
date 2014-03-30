package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * Defines the interface for classes that know how to layout Layoutable components
 * inside a LayoutContainer container.
 */

public interface LwLayout
{
 /**
  * Invoked when the specified layoutable component was added to the layout container
  * (that uses the layout manager). The specified constraints, layoutable component
  * and child index are passed as arguments into the method.
  * @param id the layoutable component constraints.
  * @param lw the layoutable component that has been added.
  * @param index the child index.
  */
  void componentAdded(Object id, Layoutable lw, int index);

 /**
  * Invoked when the specified layoutable component was removed from the layout
  * container, that uses the layout manager.
  * @param lw the layoutable component that has been removed.
  * @param index the child component index.
  */
  void componentRemoved(Layoutable lw, int index);

 /**
  * Calculates the preferred size dimension for the layout container.
  * The method has to calculate "pure" preferred size, it means that container insets
  * should not be taken into account.
  * @param target the layout container.
  * @return  a "pure" preferred size.
  */
  Dimension calcPreferredSize(LayoutContainer target);

 /**
  * Lays out the child layoutable components inside the layout container.
  * @param target the layout container.
  */
  void layout(LayoutContainer target);
}
