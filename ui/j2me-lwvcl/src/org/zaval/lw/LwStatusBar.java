package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is status bar light weight component. The component is a light weight container that
 * uses special layout manager to place status bar components, so don't set any other
 * layout manager for the container. The container places child components according to
 * its percentage constraints. The constraints should be passed to the <code>add</code> or
 * <code>insert</code> methods of the container as an Integer object where the integer value
 * defines the percent. The percent determines width of the child relatively the container width.
 * The sample below illustrates the component usage
 * <pre>
 *   ...
 *   LwStatusBar status = new LwStatusBar();
 *   status.add (new Integer(30), new LwLabel("Label 1"));
 *   status.add (new Integer(70), new LwLabel("Label 2"));
 *   ...
 * </pre>
 * <p>
 * The child components should have identical borders, so the component has the special
 * <code>setBorderView</code> method that defines the common border view. The view
 * will set for all added components.
 */
public class LwStatusBar
extends LwPanel
{
  private LwView borderView;

 /**
  * Constructs the class instance.
  */
  public LwStatusBar(){
    this(2);
  }

 /**
  * Constructs the class with the specified horizontal gap between child components.
  * @param gap the specified horizontal gap.
  */
  public LwStatusBar(int gap)
  {
    setBorderView(LwToolkit.getView("stbar.br"));
    setInsets(gap, 0, 0, 0);
    setLwLayout(new LwPercentLayout(LwToolkit.HORIZONTAL, gap));
  }

 /**
  * Sets the border view that should be applied for all child components. The method
  * sets the border view for all child components that have been added to the container
  * before.
  * @param v the specified border view.
  */
  public void setBorderView(LwView v)
  {
    if (v != borderView)
    {
      borderView = v;
      for (int i=0; i<count(); i++)
        ((LwComponent)get(i)).getViewMan(true).setBorder(borderView);
      repaint();
    }
  }

  public /*C#override*/ void insert (int i, Object s, LwComponent d) {
   ((LwComponent)d).getViewMan(true).setBorder(borderView);
    super.insert(i, s, d);
  }
}



