package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.util.*;


/**
 * This is basic interface for components that are going to be laid out with the library layout
 * managers. The main purpose of the interface is to separate layouting functionality from
 * the lightweight component functionality. It means that it is possible to re-use the set of
 * layout managers provided by the library for any other components that implement the Layoutable
 * interface.
 */
public interface Layoutable
extends Validationable
{
 /**
  * Gets the location of this component point specifying the component top-left corner.
  * The location is relative to the parent component coordinate space.
  * @return an instance of <code>Point</code> representing the top-left corner
  * of the component bounds in the coordinate space of the component parent.
  */
  Point getLocation();

 /**
  * Gets the <code>x</code> location of this component specifying the component top-left corner.
  * The location is relative to the parent component coordinate space.
  * @return a <code>x</code> coordinate representing the top-left corner
  * of the component bounds in the coordinate space of the component parent.
  */
  int getX();

 /**
  * Gets the <code>y</code> location of this component specifying the component top-left corner.
  * The location is relative to the parent component coordinate space.
  * @return an <code>y</code> coordinate representing the top-left corner
  * of the component bounds in the coordinate space of the component parent.
  */
  int getY();

 /**
  * Gets the width of this component.
  * @return a width of the component.
  */
  int getWidth();

 /**
  * Gets the height of this component.
  * @return a height of the component.
  */
  int getHeight();


 /**
  * Sets the new location for this component. The top-left corner of
  * the new location is specified by the <code>x</code> and <code>y</code>
  * parameters in the coordinate space of this component parent.
  * @param x the <i>x</i>-coordinate of the new location
  * top-left corner in the parent's coordinate space.
  * @param y the <i>y</i>-coordinate of the new location
  * top-left corner in the parent's coordinate space.
  */
  void setLocation(int x, int y);

 /**
  * Returns a size of this component. The size is represented using the
  * <code>org.zaval.port.j2me.Dimension</code> class.
  * @return a <code>Dimension</code> object that indicates the
  * size of this component.
  */
  Dimension getSize();

 /**
  * Sets the specified size for this component.
  * @param w the width of this component.
  * @param h the height of this component.
  */
  void setSize(int w, int h);

 /**
  * Gets the preferred size of this component. The preferred size is the size that the component wants to have.
  * @return a dimension object indicating this component preferred size.
  */
  Dimension getPreferredSize();

 /**
  * Determines if this component is visible. The component is visible
  * if the visibility flag is <code>true</code>.
  * @return <code>true</code> if the component is visible;
  * <code>false</code> otherwise.
  */
  boolean isVisible();

 /**
  * Gets the bounds of this component. The bounds are represented using the <code>org.zaval.port.j2me.Rectangle</code> class.
  * @return a rectangle indicating this component's bounds.
  */
  Rectangle getBounds();

 /**
  * Determines the insets of this component. Take care that insets for lightweight
  * components differ from Java AWT components. The lightweight insets
  * define indents from "left", "top", "right" and "bottom" of the component view.
  * @return the insets of this component.
  */
  Insets getInsets();
}


