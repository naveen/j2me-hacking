package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class implements layout manager interface. Every following component is laid out under
 * previous component. The preferred width is calculated as a maximum among child components.
 */
public class LwListLayout
implements LwLayout, org.zaval.misc.PosInfo
{
  private int count, gap;

 /**
  * Constructs the layout manager.
  */
  public LwListLayout() {
    this (0);
  }

 /**
  * Constructs the layout manager with the specified gap. The gap is an indent between
  * laid out components.
  * @param gap the specified gap.
  */
  public LwListLayout(int gap) {
    this.gap = gap;
  }

 /**
  * Invoked when the specified layoutable component is added to the layout container, that
  * uses the layout manager. The specified constraints, layoutable component and child index
  * are passed as arguments into the method.
  * @param id the layoutable component constraints.
  * @param lw the layoutable component.
  * @param index the child index.
  */
  public void componentAdded   (Object id, Layoutable lw, int index) { count++; }

 /**
  * Invoked when the specified layoutable component is removed from the layout
  * container, that uses the layout manager.
  * @param lw the layoutable component to be removed
  * @param index the child index.
  */
  public void componentRemoved (Layoutable lw, int index) { count--; }

 /**
  * Calculates the preferred size dimensions for the layout container.
  * The method calculates "pure" preferred size, it means that an insets
  * of the container is not considered.
  * @param lw the layout container.
  */
  public Dimension calcPreferredSize(LayoutContainer lw)
  {
    int w = 0, h = 0, c = 0;
    for (int i=0; i<lw.count(); i++)
    {
      Layoutable cc = lw.get(i);
      if (cc.isVisible())
      {
        Dimension d = cc.getPreferredSize();
        h += d.height;
        if (w < d.width) w = d.width;
        c ++;
      }
    }
    return new Dimension(w, h + c*gap);
  }

 /**
  * Lays out the child layoutable components inside the layout container.
  * @param lw the layout container that needs to be laid out.
  */
  public void layout(LayoutContainer lw)
  {
    Insets ins    = lw.getInsets();
    Point  offset = lw.getLayoutOffset();
    int x = ins.left + offset.x, y = ins.top + offset.y, psw = lw.getWidth() - ins.left - ins.right;

    int count = lw.count();
    for (int i=0; i<count; i++)
    {
      Layoutable cc = lw.get(i);
      if (cc.isVisible())
      {
        Dimension d = cc.getPreferredSize();
        cc.setSize(psw, d.height);
        cc.setLocation(x, y);
        y += (d.height + gap);
      }
    }
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define the line size. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @param line the line number.
  * @return a size of the specified line. The method returns "1" as the result for every line number.
  */
  public int getLineSize(int line) {
    return 1;
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define number of lines. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @return a number of lines. The method returns number the owner components.
  */
  public int getLines() {
    return count;
  }

 /**
  * Implements org.zaval.misc.PosInfo interface method to define max offset. The
  * implementation provides ability to navigate over the layout manager owner components using
  * org.zaval.misc.PosController class.
  * @return a max offset.
  */
  public int getMaxOffset() {
    return count - 1;
  }
}

