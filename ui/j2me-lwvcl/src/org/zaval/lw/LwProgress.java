package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This is progress bar component that can be used to display progress status for
 * some processes. The progress status is changed starting from zero to a maximal value. The
 * maximal value can be set by the <code>setMaxValue</code> method and current status can be
 * controlled by the <code>getValue</code> and <code>setValue</code> methods. Use the
 * <code>addActionListener</code> and <code>removeActionListener</code> methods to listen
 * whenever the current progress status has been changed. The action event returns previous
 * value by the data argument.
 * <p>
 * The progress bar component can be oriented one of the following manner:
 * <ul>
 *   <li>LwToolkit.HORIZONTAL (the default orientation) </li>
 *   <li>LwToolkit.VERTICAL</li>
 * </ul>
 * Use the <code>setOrientation</code> method to define the progress bar orientation.
 * <p>
 * Use the <code>setView</code> method to define title and bundle views for the component. The title
 * view is rendered at the center of the progress bar component.
 */
public class LwProgress
extends LwCanvas
{
 /**
  * The title view id.
  */
  public static final int TITLE_VIEW = 0;

 /**
  * The bundle view id.
  */
  public static final int BUNDLE_VIEW = 1;

  private int value = 0, bundleSize = 6, prevBundleSize, gap = 2, orient = LwToolkit.HORIZONTAL, maxValue = 20;
  private Color color  = LwToolkit.darkBlue;
  private LwActionSupport support;
  private LwView[] views = new LwView[2];

 /**
  * Constructs the progress bar.
  */
  public LwProgress() {
    getViewMan(true).setBorder(LwToolkit.getView("pr.br"));
  }

 /**
  * Sets the specified orientation for the component. Use LwToolkit.HORIZONTAL or
  * LwToolkit.VERTICAL constants as the orientation value.
  * @param o the specified orientation.
  */
  public void setOrientation(int o)
  {
    if (o != LwToolkit.HORIZONTAL && o != LwToolkit.VERTICAL) throw new IllegalArgumentException();
    if (o != orient)
    {
      orient = o;
      vrp();
    }
  }

 /**
  * Gets the current progress value.
  * @return a current progress value.
  */
  public int getValue () {
    return value;
  }

 /**
  * Sets the bundle size. The bundle size makes sense only in a case when the bundle view has not been
  * specified.
  * @param size the bundle size.
  */
  public void setBundleSize (int size)
  {
    if (views[BUNDLE_VIEW] != null) throw new IllegalArgumentException();

    if (size != bundleSize)
    {
      bundleSize = size;
      vrp();
    }
  }

 /**
  * Sets the bundle color. The bundle color makes sense only in a case when the bundle view has not been
  * specified.
  * @param c the bundle color.
  */
  public void setBundleColor (Color c)
  {
    if (!c.equals(color))
    {
      color = c;
      repaint();
    }
  }

 /**
  * Sets the maximal value.
  * @param m the maximal value.
  */
  public void setMaxValue (int m)
  {
    if (m != maxValue)
    {
      maxValue = m;
      setValue(getValue());
      vrp();
    }
  }

 /**
  * Sets the current value.
  * @param p the current value.
  */
  public void setValue (int p)
  {
    p = p % (maxValue + 1);
    if (value != p)
    {
      int old = value;
      value = p;
      if (support != null) support.perform(this, new Integer(old));
      repaint();
    }
  }

 /**
  * Sets the gap. The gap defines indent between progress bar bundles.
  * @param g the gap.
  */
  public void setGap (int g)
  {
    if (gap != g)
    {
      gap = g;
      vrp();
    }
  }

 /**
  * Gets the specified element view.
  * @param type the element type. Use the BUNDLE_VIEW or TITLE_VIEW as the argument value.
  * @return a view.
  */
  public LwView getView (int type) {
    return views[type];
  }

 /**
  * Sets the specified view for the specified progress bar element.
  * @param type the element type. Use the BUNDLE_VIEW or TITLE_VIEW as the argument value.
  * @param v the specified title view.
  */
  public void setView (int type, LwView v)
  {
    LwView view = views[type];
    if (view != v)
    {
      if (type == BUNDLE_VIEW)
      {
        if (view == null) prevBundleSize = bundleSize;
        if (v    == null) bundleSize     = prevBundleSize;
      }

      if (view != null) view.ownerChanged(null);
      views[type] = v;
      if (v != null) v.ownerChanged(this);
      vrp();
    }
  }

  public /*C#override*/ void paint(Graphics g)
  {
    int rs = (orient == LwToolkit.HORIZONTAL)?width - getLeft() - getRight():height - getTop() - getBottom();
    if (rs > bundleSize)
    {
      int vLoc = (rs * value)/maxValue;

      int x  = left, y = height - bottom;
      int wh = orient == LwToolkit.HORIZONTAL?height - top - bottom:width - left - right;
      g.setColor(color);

      LwView bundle = getView(BUNDLE_VIEW);

      while (x < (vLoc + left) && height - vLoc - bottom < y)
      {
        if (orient == LwToolkit.HORIZONTAL)
        {
          if (bundle != null)  bundle.paint (g, x, top, bundleSize, wh, this);
          else                 g.fillRect(x, top, bundleSize, wh);
          x += (bundleSize + gap);
        }
        else
        {
          if (bundle != null)  bundle.paint (g, left, y - bundleSize, wh, bundleSize, this);
          else                 g.fillRect(left, y - bundleSize, wh, bundleSize);
          y -= (bundleSize + gap);
        }
      }

      if (views[TITLE_VIEW] != null)
      {
        Dimension ps = views[TITLE_VIEW].getPreferredSize();
        Point      p = LwToolkit.getLocation(ps, LwToolkit.CENTER, LwToolkit.CENTER,
                                                 width, height);
        views[TITLE_VIEW].paint (g, p.x, p.y, ps.width, ps.height, this);
      }
    }
  }

 /**
  * Adds the specified action listener to receive action events from this component.
  * @param l the specified action listener.
  */
  public void addActionListener(LwActionListener l) {
    if (support == null) support = new LwActionSupport();
    support.addListener(l);
  }

 /**
  * Removes the specified action listener.
  * @param l the specified action listener.
  */
  public void removeActionListener(LwActionListener l) {
    if (support != null) support.removeListener(l);
  }

  protected /*C#override*/ void recalc()
  {
    if (views[BUNDLE_VIEW] != null)
    {
      Dimension ps = views[BUNDLE_VIEW].getPreferredSize();
      bundleSize = (orient == LwToolkit.HORIZONTAL)?ps.width:ps.height;
    }
  }

  protected /*C#override*/ Dimension calcPreferredSize()
  {
    int v1 = (maxValue * bundleSize) + (maxValue - 1)*gap;
    Dimension ps = views[BUNDLE_VIEW] != null?views[BUNDLE_VIEW].getPreferredSize():null;
    Dimension d  = (orient == LwToolkit.HORIZONTAL)? new Dimension(v1, ps == null?bundleSize:ps.height):
                                                     new Dimension(ps == null?bundleSize:ps.width, v1);
    return (views[TITLE_VIEW] != null)?MathBox.max (d, views[TITLE_VIEW].getPreferredSize()):d;
  }
}
