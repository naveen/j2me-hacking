package org.zaval.data.event;

import java.util.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is a utility class that can be used by a class that generates TextEvent to support
 * list of text listeners.
 */
public class TextListenerSupport
{
  private Vector v;

  public void addListener(TextListener l) {
    if (v == null) v = new Vector(1);
    if (!v.contains(l)) v.addElement(l);
  }

  public void removeListener(TextListener l) {
    if (v != null) v.removeElement(l);
  }

  public void perform (TextEvent e)
  {
    if (v != null)
    {
      int id = e.getID();
      for (int i=0; i<v.size(); i++)
      {
        TextListener l = (TextListener)v.elementAt(i);
        switch (id)
        {
          case TextEvent.INSERTED: l.textInserted(e); break;
          case TextEvent.REMOVED : l.textRemoved (e); break;
          case TextEvent.UPDATED : l.textUpdated (e); break;
        }
      }
    }
  }
}



