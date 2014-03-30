package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class implements layout manager interface. The layout manager uses original
 * locations (that have been set using <code>setLocation</code> method) and original
 * sizes (that have been set using <code>setSize</code> method) or preferred sizes to layout
 * child components. It is possible to align child components sizes according to the
 * parent component area, in this case the child component will be sized to have right bottom
 * corner in the right bottom corner of the parent container.
 */
public class LwRasterLayout
implements LwLayout
{
 /**
  * Defines child widths alignment by the parent component.
  */
  public static final int W_BY_PARENT = 1;

 /**
  * Defines child heights alignment by the parent component.
  */
  public static final int H_BY_PARENT = 2;

 /**
  * Defines children preferred sizes usage as the child sizes.
  */
  public static final int USE_PS_SIZE = 4;

  private int flag;

 /**
  * Constructs the layout manager that uses child components locations and sizes to layout its.
  * This layout is analog of <code>null</code> layout for Java AWT Library.
  */
  public LwRasterLayout() {
    this(0);
  }

 /**
  * Constructs the layout manager with the specified layout flag. The flag defines
  * what how the child components should be sized. The following constants (or its
  * combination) can be used as the flag value:
  * <ul>
  *   <li>
  *    W_BY_PARENT - to align child components widths by parent size.
  *   </li>
  *   <li>
  *    H_BY_PARENT - to align child components heights by parent size.
  *   </li>
  *   <li>
  *    USE_PS_SIZE - to use preferred sizes as the child components sizes.
  *   </li>
  * </ul>
  * @param f the specified layout flag.
  */
  public LwRasterLayout(int f) {
    flag = f;
  }

 /**
  * Invoked when the specified layoutable component is added to the layout container
  * (that uses the layout manager). The specified constraints, layoutable component
  * and child index are passed as arguments into the method.
  * @param id the layoutable component constraints.
  * @param lw the layoutable component.
  * @param index the child index.
  */
  public void  componentAdded(Object id, Layoutable lw, int index) {}

 /**
  * Invoked when the specified layoutable component is removed from the layout
  * container, that uses the layout manager.
  * @param lw the layoutable component to be removed
  * @param index the child index.
  */
  public void  componentRemoved(Layoutable lw, int index) {}

 /**
  * Calculates the preferred size dimensions for the layout container.
  * The method calculates "pure" preferred size, it means that an insets
  * of the container is not considered as a part of the size. The layout
  * manager calculates the preferred size as maximal location of the right
  * bound for a visible child component.
  * @param c the layout container.
  */
  public Dimension calcPreferredSize(LayoutContainer c)
  {
    Dimension m = new Dimension();
    boolean   b = (flag & USE_PS_SIZE) > 0;
    for (int i=0; i<c.count(); i++)
    {
      Layoutable el = c.get(i);
      if (el.isVisible())
      {
        Point p = el.getLocation();
        Dimension ps = b?el.getPreferredSize():el.getSize();
        p.x += ps.width;
        p.y += ps.height;
        if (p.x > m.width ) m.width  = p.x;
        if (p.y > m.height) m.height = p.y;
      }
    }
    return m;
  }

 /**
  * Lays out the child layoutable components inside the layout container.
  * The layout algorithm just sizes child components if the layout flag indicates
  * to use preferred sizes or align child sizes and moves location of children using an offset that
  * is provided by <code>getLayoutOffset</code> method of the container.
  * @param c the layout container that needs to be laid out.
  */
  public void layout(LayoutContainer c)
  {
    Point     offs = c.getLayoutOffset();
    Insets    ins  = c.getInsets();
    int       r = c.getWidth() - ins.right, b = c.getHeight() - ins.bottom;
    boolean   usePsSize = (flag & USE_PS_SIZE) > 0;

    for (int i=0; i<c.count(); i++)
    {
      Layoutable el = c.get(i);
      if (el.isVisible())
      {
        Point p = el.getLocation();
        p.x += offs.x;
        p.y += offs.y;
        el.setLocation(p.x, p.y);

        Dimension ps = usePsSize?el.getPreferredSize():el.getSize();
        if ((flag & W_BY_PARENT)>0) ps.width  = r - p.x;
        if ((flag & H_BY_PARENT)>0) ps.height = b - p.y;
        el.setSize(ps.width, ps.height);
      }
    }
  }
}



