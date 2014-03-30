package org.zaval.lw;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;



/**
 * This is implementation of the light weight container interface that has to be used
 * to develop own light weight containers. The container implementation inherits
 * the light weight component implementation. Draw attention at the following features:
 * <ul>
 *   <li>
 *     The implementation uses a layout manager to layout child components. The layout
 *     manager cannot be <code>null</code>.
 *   </li>
 *   <li>
 *     You can define a preferred size for the container by
 *     <code>calcPreferredSize</code> method as it needs for LwCanvas.
 *     By default container calculates its preferred size by a layout manager.
 *   </li>
 *   <li>
 *     You can override <code>recalc</code> method to calculate the container metrical characteristics
 *     that have an influence on the container preferred size.
 *   </li>
 *   <li>
 *     This implementation of the container uses LwRasterLayout as default layout manager.
 *     The layout manager uses original sizes and locations (that have been set by
 *     <code>setSize</code> and <code>setLocation</code> methods of the components)
 *     to layout child components.
 *   </li>
 * </ul>
 */
public class LwPanel
extends LwCanvas
implements LwContainer
{
  protected Vector children;
  private LwLayout layout;
  private int cpsWidth = -1, cpsHeight;

 /**
  * Constructs the container.
  */
  public LwPanel() {
    setLwLayout(getDefaultLayout());
  }

  public /*C#virtual*/ void paintOnTop(Graphics g) {}

 /**
  * Adds the specified lightweight component with the specified constraints as a child
  * of this container. The method calls <code> componentAdded </code> method its
  * layout manager to inform the layout manager that the new child has been added with
  * the given constraints.
  * @param s the object expressing layout constraints for this.
  * @param c the lightweight component to be added.
  */
  public void add(Object s, LwComponent c) {
    insert(count(), s, c);
  }

 /**
  * Adds the specified lightweight component as a child of this container.
  * The method calls <code> componentAdded </code> method its layout manager to inform
  * the layout manager that the new child has been added.
  * @param c the lightweight component to be added.
  */
  public void add(LwComponent c) {
    insert(count(), null, c);
  }

 /**
  * Inserts the specified lightweight component as a child of this container
  * at the specified position in the container list. The method calls
  * <code>componentAdded</code> method its layout manager to inform the layout manager
  * that the new child has been added.
  * @param i the position in the container list at which to insert
  * the component.
  * @param d the lightweight component to be added.
  */
  public void insert(int i, LwComponent d) {
    insert(i, null, d);
  }

 /**
  * Inserts the specified lightweight component with the specified constraints as a child
  * of this container at the specified position in the container list. The method calls
  * <code>componentAdded</code> method its layout manager to inform the layout manager
  * that the new child has been added with the given constraints.
  * @param i the position in the container list at which to insert
  * the component.
  * @param s the object expressing layout contraints for this.
  * @param d the lightweight component to be added.
  */
  public /*C#virtual*/ void insert(int i, Object s, LwComponent d)
  {
    d.setLwParent(this);
    if (children == null) children = new Vector(5);
    children.insertElementAt(d, i);
    layout.componentAdded(s, d, i);
    invalidate();

    //!!! Fix to layout
    if (d.getWidth() > 0 && d.getHeight() > 0) d.repaint();
    else repaint(d.getX(), d.getY(), 1, 1);
  }

 /**
  * Removes the specified component from this container.
  * The layout manager of this container is informed by calling
  * <code>componentRemoved</code> method of the manager.
  * @param c the specified component to be removed.
  */
  public void remove(LwComponent c) {
    remove(indexOf(c));
  }

 /**
  * Removes the component at the specified index from this container.
  * The layout manager of this container is informed by calling
  * <code>componentRemoved</code> method of the manager.
  * @param i the index of the component to be removed.
  */
  public /*C#virtual*/ void remove(int i)
  {
    if (children != null)
    {
      LwComponent obj = (LwComponent)children.elementAt(i);
      obj.setLwParent(null);
      children.removeElementAt(i);
      layout.componentRemoved(obj, i);

      invalidate();
      if (obj.isVisible()) repaint(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
    }
  }

 /**
  * Removes all child components from this container.
  * The layout manager of this container is informed by calling
  * <code>componentRemoved</code> method of the manager for every child that has been
  * removed.
  */
  public /*C#virtual*/ void removeAll()
  {
    if (count() > 0)
    {
      int size = children.size(), mx1 = Integer.MAX_VALUE, my1 = mx1, mx2 = 0, my2 = 0;
      for (;size>0;size--)
      {
        LwComponent child = (LwComponent)children.elementAt(size-1);

        if(child.isVisible())
        {
          int xx = child.getX(), yy = child.getY();
          mx1 = Math.min (mx1, xx);
          my1 = Math.min (my1, yy);
          mx2 = Math.max (mx2, xx + child.getWidth());
          my2 = Math.max (my2, yy + child.getHeight());
        }

        children.removeElementAt(size-1);
        layout.componentRemoved(child, size-1);
      }
      invalidate();
      repaint(mx1, my1, mx2 - mx1, my2 - my1);
    }
  }

 /**
  * Searches the specified component among this container children and returns
  * an index of the component in the child list. If the component has not been found
  * than the method returns -1.
  * @param c the component to get index.
  * @return the child index.
  */
  public int indexOf(LwComponent c) {
    return children == null?-1:children.indexOf(c);
  }

 /**
  * Gets a child component at the given index.
  * @param i the specified index of the child to be returned.
  * @return a child component at the specified index.
  */
  public Layoutable get(int i) {
    return (Layoutable)children.elementAt(i);
  }

 /**
  * Returns the number of child components in this container.
  * @return a number of child components in this container.
  */
  public int count() {
    return children == null?0:children.size();
  }

 /**
  * Sets the layout manager for this container. It is impossible to use <code>null</code>
  * as a layout manager, in this case IllegalArgumentException will be thrown.
  * @param m the specified layout manager.
  */
  public /*C#virtual*/ void setLwLayout(LwLayout m)
  {
    if (m == null) throw new IllegalArgumentException();
    if (layout != m)
    {
      layout = m;
      invalidate();
    }
  }

 /**
  * Gets the layout manager of this container.
  * @return a layout manager.
  */
  public LwLayout getLwLayout() {
    return layout;
  }

 /**
  * Sets the background color of this container. Additionally the method sets the background color
  * for every child component.
  * @param c the color to become this component background color.
  */
  public /*C#override*/ void setBackground(Color c)
  {
    super.setBackground(c);
    if (children != null)
      for (int i=0; i<children.size(); i++)
        ((LwComponent)children.elementAt(i)).setBackground(c);
  }

 /**
  * Sets the opaque of this component. Use <code> false </code>
  * argument value to make a transparent component from this component.
  * Additionally the method sets the specified opaque for every child component.
  * @param b the opaque flag.
  */
  public /*C#override*/ void setOpaque(boolean b)
  {
    super.setOpaque(b);
    if (children != null)
      for (int i=0; i<children.size(); i++)
        ((LwComponent)children.elementAt(i)).setOpaque(b);
  }

 /**
  * Determines if the component or an immediate child component contains the
  * (xx,&nbsp;yy) location in its visible part and if so, returns the component.
  * @param xx the x coordinate.
  * @param yy the y coordinate.
  * @return    the component or sub-component that contains the (x,&nbsp;y) location;
  *            <code>null</code> if the location is outside of this component visible part.
  */
  public /*C#override*/ LwComponent getLwComponentAt(int xx, int yy)
  {
    LwComponent c = super.getLwComponentAt(xx, yy);
    if (children != null && c != null)
    {
      for (int i=children.size()-1;i >= 0;i--)
      {
        LwComponent d = (LwComponent)children.elementAt(i);
        d = d.getLwComponentAt(xx - d.getX(), yy - d.getY());
        if (d != null) return d;
      }
    }
    return c;
  }

 /**
  * Sets the specified child component to be painted over other child components.
  * @param c the specified child component.
  */
  public void toFront(LwComponent c)
  {
    int index = indexOf (c);
    if (index < (count()-1))
    {
      children.removeElementAt(index);
      children.addElement(c);
      c.repaint();
    }
  }

  public /*C#override*/ Dimension getPreferredSize ()
  {
    if (cpsWidth < 0)
    {
      recalc();
      Dimension d = getPSImpl();
      cpsWidth  = d.width;
      cpsHeight = d.height;
      return d;
    }
    return new Dimension(cpsWidth, cpsHeight);
  }

 /**
  * Invoked with <code>validate</code> method only if the component is not valid.
  * The method should not be overridden for this container like it can be done for LwCanvas,
  * because the method starts validating and layouting of the child components.
  */
  public /*C#override*/ void validate ()
  {
    if (cpsWidth < 0) getPreferredSize();

    if (!isValid() && isVisible())
    {
      if (width > 0 && height > 0)
      {
        layout.layout(this);
        if (children != null)
          for (int i=0; i<children.size(); i++)
            ((LwComponent)children.elementAt(i)).validate();
        laidout();
      }
      isValidValue = true;
    }
  }

  public /*C#virtual*/ boolean isInvalidatedByChild(LwComponent c) {
    return true;
  }

  public /*C#override*/ void setSize(int w, int h)
  {
    if (w != width || h != height)
    {
      super.setSize(w, h);
      isValidValue = false;
    }
  }

 /**
  * Returns the offset. The offset can be used with a layout manager to offset child components
  * locations of the container according to the offset values. The offset is represented
  * with org.zaval.port.j2me.Point class, the <code>x</code> is offset for x-coordinates and <code>y</code>
  * is offset for y-coordinates. The ability to offset child components with a layout manager
  * is used with the library to organize scrolling.
  * @return an offset to move children.
  */
  public /*C#virtual*/ Point getLayoutOffset() {
    return new Point();
  }

  public /*C#override*/ void invalidate() {
    super.invalidate();
    cpsWidth = -1;
  }


 /**
  * Gets the "pure" preferred size for this component. The method has not be overridden,
  * because it determines the preferred size by the layout manager.
  * @return a "pure" preferred size.
  */
  protected /*C#override*/ Dimension calcPreferredSize() {
    return layout.calcPreferredSize(this);
  }

 /**
  * Gets the default layout manager that is set with the container during initialization.
  * This implementation of the method returns LwRastLayout as the default layout manager, the
  * layout manager is got as a static object by "layout.raster" key.
  * @return a layout manager.
  */
  protected /*C#virtual*/ LwLayout getDefaultLayout() {
    return (LwLayout)LwToolkit.getStaticObj("layout.raster");
  }

 /**
  * The method for internal usage only !
  */
  protected /*C#virtual*/ void updateCashedPs(int w, int h) {
    if (w >= 0 && psWidth  < 0) cpsWidth  = w;
    if (h >= 0 && psHeight < 0) cpsHeight = h;
  }


 /**
  * Invoked whenever the container layouting procedure has been finished.
  */
  protected /*C#virtual*/ void laidout() {}
}

