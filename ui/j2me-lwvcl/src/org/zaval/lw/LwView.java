package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This abstract class is used as base for lightweight views implementations.
 * A view is used to provide painting for a decorative element
 * (box, border and so on). To develop a view it is necessary to pass through
 * the following steps:
 * <ul>
 *   <li>
 *     Inherit the class for your view.
 *   </li>
 *   <li>
 *     Implement the <code>paint</code> method to define the face of the view.
 *   </li>
 *   <li>
 *     Override (if it is necessary) the <code>recalc</code> method to calculate the view
 *     metrics. For example, you can calculate "pure" preferred size inside the method or
 *     some metrical characteristics that are used to calculate the preferred size.
 *   </li>
 *   <li>
 *     Override the <code>calcPreferredSize</code> method to define the "pure" preferred size.
 *     It is important because if your view has ORIGINAL type than its preferred size
 *     will be used to calculate owner component preferred size.
 *   </li>
 *   <li>
 *     Override (if it is necessary) the <code>getTop</code>, <code>getBottom</code>,
 *     <code>getLeft</code> and <code>getRight</code> methods to define the insets.
 *   </li>
 * </ul>
 */
public abstract class LwView
implements Validationable
{
 /**
  * The stretch type of the view.
  */
  public static final int STRETCH  = 1;

 /**
  * The mosaic type of the view.
  */
  public static final int MOSAIC   = 2;

 /**
  * The original type of the view.
  */
  public static final int ORIGINAL = 3;

  private int type = ORIGINAL;
  private boolean isValidValue;

 /**
  * Constructs the view. The view will use mosaic type as a default view type.
  */
  public LwView() {
    this(MOSAIC);
  }

 /**
  * Constructs the view with the specified view type.
  * @param type the view type.
  */
  public LwView(int type) {
    setType(type);
  }

 /**
  * Gets the type of the view.
  * @return a type of the view.
  */
  public int getType() {
    return type;
  }

 /**
  * Sets the specified type of the view. The view can have stretch, mosaic or
  * original type (the types are defined with the appropriate constants of
  * the class), otherwise the method performs IllegalArgumentException
  * exception. The type defines an algorithm to paint the
  * view on the target component surface, the short descriptions of the possible
  * view types are shown below:
  * <ul>
  *   <li>
  *     ORIGINAL. The <code>paint</code> method of the view gets own preferred size as
  *     the view size.
  *   </li>
  *   <li>
  *     MOSAIC. The <code>paint</code> method of the view gets own preferred size as
  *     the view size, but the view will be painted as many times as it can be placed inside
  *     the owner component area.
  *   </li>
  *   <li>
  *     STRETCH. The view will be stretched on the owner component area.
  *   </li>
  * </ul>
  * @param t the specified view type.
  */
  public void setType(int t)
  {
    if (t != STRETCH && t != MOSAIC && t != ORIGINAL) throw new IllegalArgumentException();
    if (type != t)
    {
      type = t;
      invalidate();
    }
  }

 /**
  * Gets the top indent.
  * @return a top indent.
  */
  public /*C#virtual*/ int getTop() {
    return 0;
  }

 /**
  * Gets the left indent.
  * @return a left indent.
  */
  public /*C#virtual*/ int getLeft() {
    return 0;
  }

 /**
  * Gets the bottom indent.
  * @return a bottom indent.
  */
  public /*C#virtual*/ int getBottom() {
    return 0;
  }

 /**
  * Gets the right indent.
  * @return a right indent.
  */
  public /*C#virtual*/ int getRight() {
    return 0;
  }

 /**
  * Gets the view preferred size. The size is calculated as amount of a "pure"
  * preferred size (that is returned with <code>calcPreferredSize</code> method)
  * and the view insets. The preferred size is used with an owner component to
  * compute own preferred size.
  * @return a preferred size of the view.
  */
  public /*C#virtual*/ Dimension getPreferredSize () {
    validate();
    return calcPreferredSize ();
  }

 /**
  * Calculates and returns the view preferred size. The method has not to use
  * the view insets to compute the preferred size.
  * @return a "pure" preferred size of the view.
  */
  protected /*C#virtual*/ Dimension calcPreferredSize () {
    return new Dimension();
  }

 /**
  * Invoked with <code>validate</code> method if the view is invalid. The method
  * can be overridden to calculate metrical characteristics of the view and the method
  * is called only if it is necessary, so you should not care about
  * superfluous calling and computing.
  */
  protected /*C#virtual*/ void recalc() {}

 /**
  * Paints the view using the preferred size of the view. The location where the
  * view has to be painted is determined with <code>x</code> and <code>y</code>
  * coordinates.
  * @param g the specified context to be used for painting.
  * @param x the x coordinate.
  * @param y the y coordinate.
  * @param d the owner component.
  */
  public /*C#virtual*/ void paint(Graphics g, int x, int y, Object d)
  {
    //???
    validate();
    Dimension size = calcPreferredSize ();
    paint(g, x, y, size.width, size.height, d);
  }

  public /*C#virtual*/ void invalidate () {
    isValidValue = false;
  }

  public /*C#virtual*/ void validate ()
  {
    if (!isValidValue)
    {
      recalc();
      isValidValue = true;
    }
  }

  public boolean isValid () {
    return isValidValue;
  }


 /**
  * Paints the view using the given width and height. The location where the
  * view has to be painted is determined with the <code>x</code> and <code>y</code>
  * coordinates. This abstract method has to be implemented to define a "face" for the view.
  * @param g the specified context to be used for painting.
  * @param x the x coordinate.
  * @param y the y coordinate.
  * @param w the width of the view.
  * @param h the height of the view.
  * @param d the owner component.
  */
  public abstract void paint(Graphics g, int x, int y, int w, int h, Object d);

 /**
  * The method is called whenever the view owner has been changed.
  * It allows you to store the owner reference for a given view if it is necessary.
  * For example, if the view preferred size has been changed so in this case
  * the view owner component should be invalidated, because the owner preferred
  * size depends on the view.
  * @param v the new view owner.
  */
  protected /*C#virtual*/ void ownerChanged(Validationable v) {}
}

