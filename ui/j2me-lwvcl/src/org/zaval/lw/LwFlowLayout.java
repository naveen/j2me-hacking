package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class implements layout manager interface. The implementation places child components
 * according to the following three parameters : direction (can be LwToolkit.HORIZONTAL or LwToolkit.VERTICAL), vertical alignment
 * (LwToolkit.TOP, LwToolkit.BOTTOM, LwToolkit.CENTER) and horizontal alignment (LwToolkit.LEFT,
 * LwToolkit.RIGHT, LwToolkit.CENTER).
 * <p>
 * The first parameter defines direction, if the direction is LwToolkit.VERTICAL than every following
 * component will be added under the previous, otherwise every following component will be added
 * to the right of the previous.
 * <p>
 * The vertical and horizontal alignments define how the child components will be laid out
 * relatively the parent container. The table below shows the samples of the layout manager
 * usage:
 * <table border="1" width="100%" cellspacing="1">
 *   <tr class="tb3">
 *     <td align="center"><b>Direction</b></td>
 *     <td align="center"><b>Horizontal alignment</b></td>
 *     <td align="center"><b>Vertical alignment</b></td>
 *     <td align="center"><b>Sample App</b></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">LwToolkit.HORIZONTAL</td>
 *     <td align="center">LwToolkit.LEFT</td>
 *     <td align="center">LwToolkit.TOP</td>
 *     <td align="center"><img src="images/FlowLayout1.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">LwToolkit.VERTICAL</td>
 *     <td align="center">LwToolkit.LEFT</td>
 *     <td align="center">LwToolkit.TOP</td>
 *     <td align="center"><img src="images/FlowLayout2.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">LwToolkit.VERTICAL</td>
 *     <td align="center">LwToolkit.RIGHT</td>
 *     <td align="center">LwToolkit.TOP</td>
 *     <td align="center"><img src="images/FlowLayout3.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">LwToolkit.HORIZONTAL</td>
 *     <td align="center">LwToolkit.CENTER</td>
 *     <td align="center">LwToolkit.CENTER</td>
 *     <td align="center"><img src="images/FlowLayout4.gif"></td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">LwToolkit.HORIZONTAL</td>
 *     <td align="center">LwToolkit.CENTER</td>
 *     <td align="center">LwToolkit.BOTTOM</td>
 *     <td align="center"><img src="images/FlowLayout5.gif"></td>
 *   </tr>
 * </table>
 */
public class LwFlowLayout
implements LwLayout
{
  private int ax, ay, direction, gap;

 /**
  * Constructs a new flow layout manager with default parameters.
  * In this case the direction property is LwToolkit.HORIZONTAL, the horizontal alignment is
  * LwToolkit.LEFT and the vertical alignment is LwToolkit.TOP.
  */
  public LwFlowLayout() {
    this(LwToolkit.LEFT, LwToolkit.TOP, LwToolkit.HORIZONTAL);
  }

 /**
  * Constructs a new flow layout manager with the specified horizontal and vertical
  * alignments. In this case the direction property is LwToolkit.HORIZONTAL.
  * @param ax the specified horizontal alignment.
  * @param ay the specified vertical alignment.
  */
  public LwFlowLayout (int ax, int ay) {
    this(ax, ay, LwToolkit.HORIZONTAL);
  }

 /**
  * Constructs a new flow layout manager with the specified horizontal,  vertical
  * alignments and the direction.
  * @param ax the specified horizontal alignment.
  * @param ay the specified vertical alignment.
  * @param dir the specified direction.
  */
  public LwFlowLayout (int ax, int ay, int dir) {
    this(ax, ay, dir, 0);
  }

 /**
  * Constructs a new flow layout manager with the specified horizontal,  vertical
  * alignments, the direction and the given gap.
  * @param ax the specified horizontal alignment.
  * @param ay the specified vertical alignment.
  * @param dir the specified direction.
  * @param g the specified gap.
  */
  public LwFlowLayout (int ax, int ay, int dir, int g)
  {
    if (dir != LwToolkit.HORIZONTAL && dir != LwToolkit.VERTICAL) throw new IllegalArgumentException();
    this.ax = ax;
    this.ay = ay;
    direction = dir;
    gap = g;
  }

 /**
  * Calculates the preferred size dimensions for the layout container.
  * The method calculates "pure" preferred size, it means that an insets
  * of the container is not considered.
  * @param target the layout container.
  */
  public Dimension calcPreferredSize (LayoutContainer target) {
    return calcPreferredSize (target, direction, gap);
  }

 /**
  * Lays out the child layoutable components inside the layout container.
  * @param c the layout container that needs to be laid out.
  */
  public void layout(LayoutContainer c)
  {
    Dimension psSize = calcPreferredSize (c, direction, gap);
    Insets    ins    = c.getInsets();
    Point     p      = LwToolkit.getLocation(psSize, ax, ay, c.getWidth() - ins.left - ins.right,
                                                             c.getHeight() - ins.top - ins.bottom);
    for (int i=0; i<c.count(); i++)
    {
      Layoutable a = c.get(i);
      if (a.isVisible())
      {
        Dimension d  = a.getPreferredSize();
        if (direction == LwToolkit.HORIZONTAL)
        {
          a.setLocation(p.x + ins.left, (psSize.height - d.height)/2 + p.y + ins.top);
          p.x += (d.width + gap);
        }
        else
        {
          a.setLocation(p.x + (psSize.width - d.width)/2 + ins.left, p.y + ins.top);
          p.y += d.height + gap;
        }
        a.setSize(d.width, d.height);
      }
    }
  }

 /**
  * Gets the gap.
  * @return a gap.
  */
  public int getGap () {
    return gap;
  }

 /**
  * Invoked when the specified layoutable component is added to the layout container
  * (that uses the layout manager). The specified constraints, layoutable component
  * and child index are passed as arguments into the method. The layout manager doesn't
  * use any constraints, so the method is empty for the manager.
  * @param id the layoutable component constraints.
  * @param lw the layoutable component.
  * @param index the child index.
  */
  public void  componentAdded  (Object id, Layoutable lw, int index) {}

 /**
  * Invoked when the specified layoutable component is removed from the layout
  * container, that uses the layout manager.
  * @param lw the layoutable component to be removed
  * @param index the child index.
  */
  public void  componentRemoved(Layoutable lw, int index) {}

  private static Dimension calcPreferredSize (LayoutContainer c, int direction, int gap)
  {
    Dimension m  = new Dimension();
    int       cc = 0;
    for (int i = 0; i<c.count(); i++)
    {
      Layoutable a = c.get(i);
      if (a.isVisible())
      {
        Dimension d = a.getPreferredSize();
        if (direction == LwToolkit.HORIZONTAL)
        {
          m.width  += d.width;
          m.height  = Math.max (d.height, m.height);
        }
        else
        {
          m.width   = Math.max (d.width, m.width);
          m.height += d.height;
        }
        cc++;
      }
    }

    if (direction == LwToolkit.HORIZONTAL) m.width   += (gap * (cc - 1));
    else                                   m.height  += (gap * (cc - 1));
    return m;
  }
}



