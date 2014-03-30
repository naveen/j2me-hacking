package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This class can be used for creating lightweight containers that:
 * <ul>
 *   <li>Want to have focus.</li>
 *   <li>
 *     Have a special child component that can be selected with rectangle frame if the container
 *     has focus. Use the <code>setAsFocusComponent</code>
 *     method to define the child component. The child is called focus indicator component.
 *   </li>
 * </ul>
 * <p>
 * This class is a composite component that catches all the child components events.
 */
public class LwActContainer
extends LwPanel
implements LwComposite
{
  private Color        rectColor = Color.gray;
  private LwComponent  focusComponent;

 /**
  * Constructs the container with the specified child component. The child is used
  * to indicate whenever the component has focus.
  * @param t the specified child component.
  */
  public LwActContainer(LwComponent t)
  {
    if (t != null)
    {
      setInsets(4,4,4,4);
      add (t);
      setAsFocusComponent(0);
    }
  }

  public /*C#override*/ boolean canHaveFocus() {
    return true;
  }

 /**
  * Gets the child component that is used to indicate whenever the component has focus.
  * @return a child focus indicator component.
  */
  public LwComponent getFocusComponent() {
    return focusComponent;
  }

 /**
  * Sets a child focus indicator component by the specified child index. Use <code>-1</code> value
  * to assign <code>null</code> value to the focus indicator component.
  * @param index the specified child index to be set as the focus indicator.
  */
  public void setAsFocusComponent(int index) {
    focusComponent = (index >= 0)?(LwComponent)get(index):null;
  }

 /**
  * Sets the specified color to paint rectangle frame around the focus indicator component in case
  * if the container has set focus indicator component.
  * @param c the specified color.
  */
  public void setRectColor(Color c)
  {
    if (c == null) throw new IllegalArgumentException();
    if (!c.equals(rectColor))
    {
      rectColor = c;
      repaint();
    }
  }

 /**
  * Gets the border color that is used to paint rectangle frame around the focus idicator
  * component.
  * @return a border color.
  */
  public Color getRectColor() {
    return rectColor;
  }

  public /*C#virtual*/ boolean catchInput(LwComponent child) {
    return true;
  }

  public /*C#override*/ void paintOnTop (Graphics g)
  {
    if (focusComponent != null && hasFocus())
    {
      g.setColor(rectColor);
      g.drawRect(focusComponent.getX(), focusComponent.getY(),
                 focusComponent.getWidth()-1, focusComponent.getHeight()-1);
    }
  }

  public /*C#override*/ void remove(int i) {
    if (get(i) == focusComponent) focusComponent = null;
    super.remove(i);
  }

  public /*C#override*/ void removeAll() {
    focusComponent = null;
    super.removeAll();
  }

  protected /*C#override*/ LwLayout getDefaultLayout ()  {
    return (LwLayout)LwToolkit.getStaticObj("acont.layout");
  }
}
