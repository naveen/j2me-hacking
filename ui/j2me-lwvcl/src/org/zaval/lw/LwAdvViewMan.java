package org.zaval.lw;

import java.util.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class extends the basic view manager functionality. It provides ability to bind a view
 * with a given name. The view manager can be used to change standard view faces for some lightweight
 * components. For example, for LwButton component using the view manager it is possible to
 * re-bind "button.off" and "button.on" face views to some custom views, by <code>put</code>
 * method of the view manager. The sample below illustrates the usage:
 * <pre>
 *   ...
 *   LwButton button = new LwButton("Ok");
 *   LwAdvViewMan man = new LwAdvViewMan();
 *   man.put("button.off", LwImgRender("off.gif"));
 *   man.put("button.on", LwImgRender("on.gif"));
 *   button.setViewMan(man);
 *   ...
 * </pre>
 * In the sample above the "off.gif" image will be used when the button is un-pressed and
 * the "on.gif" image when the button is pressed.
 */
public class LwAdvViewMan
extends LwViewMan
{
  private static final int DEF_CAPACITY = 2;

  private LwView[] skins;
  private String[] keys;
  private int      size;

 /**
  * Binds the specified name with the given view.
  * @param id the name to bind with the view.
  * @param s  the view to be bound.
  */
  public void put(String id, LwView s)
  {
    int i = indexOf(id);
    if (i < 0)
    {
      if (skins == null)
      {
        skins = new LwView[DEF_CAPACITY];
        keys  = new String[DEF_CAPACITY];
      }
      else
      if (size == skins.length)
      {
        LwView[] nskins = new LwView[skins.length + DEF_CAPACITY];
        String[] nkeys  = new String[keys.length + DEF_CAPACITY];
        System.arraycopy(skins, 0, nskins, 0, skins.length);
        System.arraycopy(keys, 0, nkeys, 0, keys.length);
        skins = nskins;
        keys  = nkeys;
      }
      skins[size] = s;
      keys [size] = id;
      size++;
    }
    else skins[i] = s;
    invalidate();
  }

 /**
  * Removes the specified binding.
  * @param id the name to remove the view binding.
  */
  public void remove(String id)
  {
    int j = indexOf(id);
    if (j >= 0)
    {
      skins[j] = null;
      keys [j] = null;
      size--;
      for (int i=j;i<size;i++)
      {
        skins[i] = skins[i+1];
        keys [i] = keys [i+1];
      }
      invalidate();
    }
  }

 /**
  * The method is overridden to return appropriate view by the specified name basing on
  * the binding hash table. The table content is defined by the <code>put</code> and
  * <code>remove</code> methods of the view manager.
  * @param key the name of the view.
  * @return a view.
  */
  protected /*C#override*/ LwView getView(String key)
  {
    int i = indexOf(key);
    return (i < 0)?super.getView(key):skins[i];
  }

  protected /*C#override*/ int getSize() {
    return super.getSize() + size;
  }

  protected /*C#override*/ LwView getView(int index)
  {
    int ss = super.getSize();
    return index < ss?super.getView(index):skins[index-ss];
  }

  private int indexOf (String s)
  {
    for (int i=0; i<size; i++)
      if (s.equals(keys[i])) return i;
    return -1;
  }
}
