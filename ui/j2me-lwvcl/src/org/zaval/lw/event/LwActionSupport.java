package org.zaval.lw.event;

import java.util.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is utility class that can be used by a class that generates action events to support
 * list of LwActionListener classes.
 */
public class LwActionSupport
{
  private Vector v;

 /**
  * Adds the specified action listener to the listeners list.
  * @param l the specified listener.
  */
  public void addListener(LwActionListener l) {
    if (v == null) v = new Vector(1);
    if (!v.contains(l)) v.addElement(l);
  }

 /**
  * Removes the specified listener from the listeners list.
  * @param l the specified listener.
  */
  public void removeListener(LwActionListener l) {
    if (v != null) v.removeElement(l);
  }

 /**
  * Fires the specified action event to every member of the listeners list.
  * @param src the specified event source.
  * @param data the specified event related data.
  */
  public void perform(Object src, Object data)
  {
    if (v != null)
      for (int i=0;i<v.size();i++)
        ((LwActionListener)v.elementAt(i)).actionPerformed(src, data);
  }
}
