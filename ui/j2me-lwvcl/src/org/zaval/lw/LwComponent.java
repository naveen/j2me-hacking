package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is base interface for the library components. Any GUI component of the library should
 * implement the interface. Actually the interface is analog of the org.zaval.port.j2me.Component class
 * provided by the Java AWT library. The lightweight library provides an implementation of the
 * interface - LwCanvas - that is more handy for usage. To create a lightweight component
 * just inherit the class. The another variation of the interface is the lightweight container
 * interface - LwContainer. The interface extends the component interface and has implementation
 * to create own lightweight containers - LwPanel.
 */
public interface LwComponent
extends Layoutable
{
  /**
   * Returns the bounding rectangle of the visible part for the component.
   * @return a visible part bounding rectangle.
   */
   Rectangle getVisiblePart();

  /**
   * Determines if the component or an immediate child component contains the
   * (x,&nbsp;y) location in its visible part and if so, returns the component.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @return    the component or subcomponent that contains the (x,&nbsp;y) location;
   *            <code>null</code> if the location is outside this component.
   */
   LwComponent getLwComponentAt(int x, int y);

  /**
   * Gets the lightweight parent of this component. It is supposed that the parent implements
   * LwContainer interface.
   * @return the parent container of this component.
   */
   LwComponent getLwParent();

  /**
   * Sets the lightweight parent of this component. It is supposed that the parent implements
   * LwContainer interface. The method is provided for lightweight core usage to support components
   * hierarchy, not for applications that base on the library. Don't touch
   * the method.
   * @param p the specified parent container.
   * @return a parent component of this lightweight component.
   */
   void setLwParent(LwComponent p);

  /**
   * Enables or disables this component. An enabled component can participate
   * in events handling and performing processes. Component is enabled
   * by default.
   * @param b if the value is <code>true</code> - enables the component;
   * otherwise disables this component.
   */
   void setEnabled(boolean b);

  /**
   * Shows or hides this lightweight component depending on the value of parameter
   * <code>b</code>.
   * @param b if it is <code>true</code>, shows this component;
   * otherwise, hides this component.
   */
   void setVisible(boolean b);

  /**
   * Sets the background color of this component. The color is going to be used to
   * fill the component background.
   * @param c the color to become this component background color.
   */
   void setBackground(Color c);

  /**
   * Returns a view manager of the component. The view manager can be <code>null</code>.
   * The input argument <code>autoCreate</code> defines if the view manager has to be created
   * automatically in a case if it has not been determined before. It means, if the argument is
   * <code>true</code> and the view manager is <code>null</code>, than the component will
   * try to create and initialize its view manager by a default view manager. If the argument
   * is <code>false</code> than the method returns the component view manager as is.
   * @param autoCreate the flag defines if the view manager should be created
   * automatically.
   * @return a view manager for the component.
   */
   LwViewMan getViewMan(boolean autoCreate);

  /**
   * Sets the specified view manager for the component. The view manager defines set of views
   * that are used as a part of the component face.
   * @param man the view manager to set for the component.
   */
   void setViewMan(LwViewMan man);

  /**
   * Specifies if the component can have focus.
   * @return <code>true</code> if the component can have the focus.
   */
   boolean canHaveFocus();

 /**
  * Paints this component.
  * @param g the graphics context to be used for painting.
  */
  void paint(Graphics g);

 /**
  * Updates this component. The calling of the method precedes
  * the calling of <code>paint</code> method and it is performed with
  * <code>repaint</code> method. The method can be used to fill the
  * component with the background color if the component is opaque.
  * @param g the specified context to be used for updating.
  */
  void update(Graphics g);

 /**
  * Performs repainting process of this component. The method causes
  * calling of <code>update</code> and than <code> paint </code> methods.
  */
  void repaint ();

 /**
  * Performs repainting process of the specified rectangular area of this component. The method causes
  * calling of <code>update</code> and than <code> paint </code> methods.
  * @param x  the <i>x</i> coordinate.
  * @param y  the <i>y</i> coordinate.
  * @param w  the width.
  * @param h  the height.
  */
  void  repaint (int x, int y, int w, int h);

 /**
  * Gets the opaque of this component. If the method returns
  * <code>false</code> than the component is transparent, in this case
  * <code>update</code> method is not be called during painting process.
  * @return  <code>true</code> if the component is opaque; otherwise
  * <code>false</code>.
  */
  boolean isOpaque();

 /**
  * Sets the opaque of this component. Use <code> false </code>
  * argument value to make a transparent component from this component.
  * @param b the opaque flag.
  */
  void  setOpaque(boolean b);

 /**
  * Returns an origin of the component. The origin defines an offset of the component view
  * relatively the component point of origin. The method can be implemented to organize
  * scrolling of the component view.
  * @return an origin of the component.
  */
  Point getOrigin();

 /**
  * Gets the background color of this component.
  * @return a component background color.
  */
  Color getBackground();

 /**
  * Determines whether this component is enabled. If the method returns
  * <code>true</code> than the component is enabled and can participate in event
  * handling and performing processes. Components are enabled initially by default.
  * @return <code>true</code> if the component is enabled; <code>false</code> otherwise.
  */
  boolean isEnabled();

 /**
  * Sets the specified insets for the component.
  * @param top the top indent.
  * @param left the left indent.
  * @param bottom the bottom indent.
  * @param right the right indent.
  */
  void setInsets(int top, int left, int bottom, int right) ;
}
