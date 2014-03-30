package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This class is used as the views container to provide the following abilities:
 * <ul>
 *   <li>
 *     Store border, face and background views for a given owner component.
 *   </li>
 *   <li>
 *     Calculate the common preferred size of the set.
 *   </li>
 *   <li>
 *     Validate the views.
 *   </li>
 * </ul>
 */
public class LwViewMan
implements Validationable
{
  private Layoutable parent;
  private LwView bg, border, view;
  private int width, height;
  private boolean isValidValue;

 /**
  * Sets the owner component for this view manager.
  * @param p the specified owner component.
  */
  protected void setParent(Layoutable p)
  {
    if (p != parent)
    {
      parent = p;
      invalidate();
    }
  }

 /**
  * Gets the owner component for this view manager.
  * @return an owner component.
  */
  public Layoutable getParent () {
    return parent;
  }

 /**
  * Gets the view that should be used as a background view for the owner component.
  * @return an background view.
  */
  public LwView getBg() {
    return bg;
  }

 /**
  * Gets the view that should be used as a border view for the owner component.
  * @return a border view.
  */
  public LwView getBorder() {
    return border;
  }

 /**
  * Gets the view that should be used as a face view for the owner component.
  * @return a face view.
  */
  public LwView getView() {
    return view;
  }

 /**
  * Sets the background view for the owner component by the specified view name.
  * The method tries to resolve the string name by the <code>getView</code> method.
  * This implementation of the method uses the name as a key to find the view
  * among static objects. The library defines some of standard views as static
  * objects (for example border views, button views, checkbox views and so on) and
  * lightweight components use the standard views to paint itself.
  * @param s the name of the background view.
  */
  public void setBg(String s) {
    setBg(getView(s));
  }

 /**
  * Sets the border view for the owner component by the specified view name.
  * The method tries to resolve the string name by the <code>getView</code> method.
  * This implementation of the method uses the name as a key to find the view
  * among static objects. The library defines some of standard views as  static
  * objects (for example border views, button views, checkbox views and so on) and
  * lightweight components use the standard views to paint itself.
  * @param s the name of the border view.
  */
  public void setBorder(String s) {
    setBorder(getView(s));
  }

 /**
  * Sets the face view for the owner component by the specified view name.
  * The method tries to resolve the string name by the <code>getView</code> method.
  * This implementation of the method uses the name as a key to find the view
  * among static objects. The library defines some of standard views as static
  * objects (for example border views, button views, checkbox views and so on) and
  * lightweight components use the standard views to paint itself.
  * @param s the name of the face view.
  */
  public void setView (String s) {
    setView(getView(s));
  }

 /**
  * Sets the background view.
  * @param s the background view.
  */
  public void setBg(LwView s)
  {
    if (s != bg)
    {
      bg = s;
      invalidate();
    }
  }

 /**
  * Sets the border view.
  * @param s the border view.
  */
  public void setBorder(LwView s)
  {
    if (s != border)
    {
      LwView old = border;
      border = s;
      if (!equalsMetric(old, s)) invalidate();
    }
  }

 /**
  * Sets the face view.
  * @param s the face view.
  */
  public void setView(LwView s)
  {
    if (s != view)
    {
      LwView old = view;
      view = s;
      if (s != null) s.ownerChanged(this);
      if (!equalsMetric(old, s)) invalidate();
    }
  }

 /**
  * Invalidates the view manager. The view manager performs invalidation of the owner
  * component if it is not <code>null</code>.
  */
  public /*C#virtual*/ void invalidate() {
    if (parent != null) parent.invalidate();
    isValidValue = false;
  }

 /**
  * Calculates the view manager metrics. The method is invoked during validation of the
  * view manager if it is necessary. The main purpose of the method to calculate preferred
  * size and insets of the view manager basing on the views set (border, background and
  * face views).
  */
 /**
  * Returns the calculated preferred width.
  * @return a calculated preferred width.
  */
  public /*C#virtual*/ int getPWidth() {
    validate();
    return width;
  }

 /**
  * Returns the calculated preferred height.
  * @return a calculated preferred height.
  */
  public /*C#virtual*/ int getPHeight() {
    validate();
    return height;
  }

  public boolean isValid()  {
    return isValidValue;
  }

  public /*C#virtual*/ void validate()
  {
    if (!isValidValue)
    {
      width  = 0;
      height = 0;
      int size = getSize();
      for (int i=0; i<size; i++)
      {
        LwView s = getView(i);
        if (s != null)
        {
          s.validate();
          int type = s.getType();
          if (type == LwView.ORIGINAL || type == LwView.STRETCH)
          {
            Dimension sps = s.getPreferredSize();
            width  = Math.max(width, sps.width);
            height = Math.max(height, sps.height);
          }
        }
      }
      isValidValue = true;
    }
  }

 /**
  * Resolves the view by the specified view name. The method binds a view with a name.
  * This class implementation uses the name as a static object key, so the method returns
  * a view by the name from the set of static objects. The method provides ability to
  * implement own mapping mechanism. For example, LwButton component uses "button.on"
  * and "button.off" names for the face view depending on the button state, so it is
  * possible to override the method for a new view manager implementation to change
  * the button face, but the library provides LwAdvViewMan implementation to support
  * custom views.
  * @param key the name of the view.
  * @return a view.
  */
  protected /*C#virtual*/ LwView getView(String key) {
    return LwToolkit.getView(key);
  }

 /**
  * Returns all views that are provided with the view manager. The method is used with
  * <code>validate</code> method to compute insets and preferred size of the view
  * manager.
  * @return an array of views.
  */
  protected /*C#virtual*/ LwView getView(int index)
  {
    switch (index)
    {
      case 0: return bg;
      case 1: return border;
      case 2: return view;
    }
    throw new IndexOutOfBoundsException();
  }

 /**
  * Gets a number of views that the manager contains.
  * @return the number of views.
  */
  protected /*C#virtual*/ int getSize() {
    return 3;
  }

  private boolean equalsMetric(LwView v1, LwView v2)
  {
    return  (v1 == v2 ||
             (v1 != null && v2 != null && v1.getPreferredSize().equals(v2.getPreferredSize()) &&
              v1.getTop   () == v2.getTop   () &&
              v1.getBottom() == v2.getBottom() &&
              v1.getLeft  () == v2.getLeft  () &&
              v1.getRight () == v2.getRight ()   ));
  }
}
