package org.zaval.lw;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is layout manager to place child components according to its percentage constraints.
 */
public class LwPercentLayout
implements LwLayout
{
  private int   gap, dir;
  private Vector v;

 /**
  * Constructs the layout manager. The LwToolkit.HORIZONTAL orientation will be used.
  * as default.
  */
  public LwPercentLayout() {
    this (LwToolkit.HORIZONTAL, 2);
  }

 /**
  * Constructs the layout manager with the specified orientation and the given gap.
  * Use one of the following values as the orientation:  LwToolkit.HORIZONTAL or LwToolkit.VERTICAL.
  * @param dir the specified orientation.
  * @param gap the specified gap.
  */
  public LwPercentLayout(int dir, int gap)
  {
    if (dir != LwToolkit.HORIZONTAL && dir != LwToolkit.VERTICAL)
      throw new IllegalArgumentException();
    this.dir = dir;
    this.gap = gap;
  }

  public void componentAdded(Object id, Layoutable lw, int index)
  {
    if (id == null) throw new IllegalArgumentException();
    if (v == null) v = new Vector();
    v.insertElementAt(id, index);
  }

  public void componentRemoved(Layoutable lw, int index) {
    v.removeElementAt(index);
  }

  public void layout(LayoutContainer target)
  {
    Insets ins = target.getInsets();
    int size = target.count();
    int rs   = (-gap*(size==0?0:size-1)), loc = 0;

    if (dir == LwToolkit.HORIZONTAL)
    {
      rs  += target.getWidth () - ins.left - ins.right;
      loc =  ins.left;
    }
    else
    {
      rs += target.getHeight() - ins.top - ins.bottom;
      loc =  ins.top;
    }

    for (int i=0; i<size; i++)
    {
      Layoutable l = target.get(i);
      int        constr = ((Integer)v.elementAt(i)).intValue();

      if (dir == LwToolkit.HORIZONTAL)
      {
        int ns = ((size - 1) == i)?target.getWidth() - ins.right - loc:(rs * constr)/100;
        l.setLocation(loc, ins.top);
        l.setSize(ns, target.getHeight() - ins.top - ins.bottom);
        loc += (ns + gap);
      }
      else
      {
        int ns = ((size - 1) == i)?target.getHeight() - ins.bottom - loc:(rs * constr)/100;
        l.setLocation(ins.left, loc);
        l.setSize(target.getWidth() - ins.left - ins.right, ns);
        loc += (ns + gap);
      }
    }
  }

  public Dimension calcPreferredSize(LayoutContainer target)
  {
    int max = 0, size = target.count();
    for (int i=0; i<size; i++)
    {
      Dimension d = target.get(i).getPreferredSize();
      if (dir == LwToolkit.HORIZONTAL)
      {
        if (d.height > max) max = d.height;
      }
      else
      {
        if (d.width > max) max = d.width;
      }
    }
    return (dir == LwToolkit.HORIZONTAL)?new Dimension (target.getWidth() + gap*(size==0?0:size-1), max)
                                        :new Dimension (max, target.getHeight() + gap*(size==0?0:size-1));
  }
}



