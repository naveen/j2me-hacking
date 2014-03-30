package org.zaval.lw;

import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is an implementation of the LwFocusManager for the library.
 * According to the implementation the light weight component can be a focus owner
 * if the component <code>canHaveFocus</code> method returns <code>true</code> and
 * the component is enabled and visible.
 * <p>
 * The focus manager implements the mouse listener interface to deliver focus to a light
 * weight component. The manager controls (by implementing LwComponentListener,
 * LwContainerListener listeners interfaces) when the focus owner component is disabled,
 * hidden or removed from the parent component to take away the focus if it is necessary.
 * <p>
 * The manager implements keyboard listener interface to change focus owner when the "TAB"
 * key has been pressed.
 * <p>
 * The manager provides following methods that are used by other components of the
 * library:
 * <ul>
 *   <li>
 *     <code>requestFocus</code>. The method is called to try pass the focus to the specified
 *     component or some other component. The method tests if the specified component
 *     can have focus by the <code>isFocusable</code> method.
 *   </li>
 *   <li>
 *     <code>hasFocus</code>. The method is called to test if the component has the focus or not.
 *   </li>
 *   <li>
 *     <code>getFocusOwner</code>. The method returns a component which has the focus.
 *   </li>
 *   <li>
 *     <code>findFocusable</code>. The method looks for a component that can get the focus.
 *   </li>
 * </ul>
 */
public class LwFocusManager
implements LwManager, LwMouseListener, LwKeyListener
{
  private LwComponent focusOwner;

 /**
  * Requests the input focus for the specified component. The component can be the focus owner
  * if it is visible and enabled. If the focus owner has been changed than appropriate focus
  * lost and gained event will be performed and passed to LwEventManager.
  * @param c the specified component.
  */
  public void requestFocus(LwComponent c)
  {
    if (c != focusOwner && (c == null || isFocusable(c)))
    {
      LwComponent oldFocusOwner = focusOwner;
      if  (c != null)
      {
        LwComponent nf = LwToolkit.getEventManager().getEventDestination(c);
        if (nf == null || oldFocusOwner == nf) return;
        focusOwner = nf;
      }
      else focusOwner = c;

      if (oldFocusOwner != null)
      {
        LwToolkit.getEventManager().perform(new LwFocusEvent(oldFocusOwner, LwFocusEvent.FOCUS_LOST));

        //System.out.println ("LwFocusManager.requestFocus() old " + LwToolkit.getAbsLocation(oldFocusOwner));
        oldFocusOwner.repaint();
      }

      if (focusOwner != null)
      {
        LwToolkit.getEventManager().perform(new LwFocusEvent(focusOwner, LwFocusEvent.FOCUS_GAINED));


        //System.out.println ("LwFocusManager.requestFocus() " + LwToolkit.getAbsLocation(focusOwner));

        focusOwner.repaint();
      }
    }
  }

 /**
  * Looks for a component that can be the focus owner starting from the specified component.
  * @param c the specified starting component.
  * @return the component that can be the focus owner or <code>null</code> if the component has
  * not been found.
  */
  public LwComponent findFocusable(LwComponent c) {
    return (isFocusable(c)?c:fd(c, 0));
  }

 /**
  * Tests if the specified component has the focus.
  * @param c the specified component.
  * @return  <code>true</code> if the component is a focus owner; otherwise
  * <code>false</code>.
  */
  public boolean hasFocus(LwComponent c) {
    return focusOwner == c;
  }

 /**
  * Gets the focus owner component.
  * @return a focus owner component.
  */
  public LwComponent getFocusOwner() {
    return focusOwner;
  }

 /**
  * Invoked when the mouse has been pressed on a light weight component.
  * The manager uses the method to set the source component as the focus owner if
  * it is possible.
  * @param e the specified mouse event.
  */
  public void mousePressed(LwMouseEvent e) {
    if (LwToolkit.isActionMask(e.getMask()))
      requestFocus(e.getLwComponent());
  }

 /**
  * Invoked when the mouse has been clicked on a light weight component.
  * @param e the specified mouse event.
  */
  public void mouseClicked(LwMouseEvent e) {}

 /**
  * Invoked when the mouse enters a light weight component.
  * @param e the specified mouse event.
  */
  public void mouseEntered(LwMouseEvent e) {}

 /**
  * Invoked when the mouse exits a light weight component.
  * @param e the specified mouse event.
  */
  public void mouseExited(LwMouseEvent e) {}

 /**
  * Invoked when the mouse has been released on a light weight component.
  * @param e the specified mouse event.
  */
  public void mouseReleased(LwMouseEvent e) {}

  public void keyPressed (LwKeyEvent e)
  {
    //!!!
    //if (LwToolkit.PASSFOCUS_KEY == LwToolkit.getKeyType(e.getKeyCode(), e.getMask()))
    if (e.getKeyCode() == LwToolkit.VK_ENTER)
    {
      LwComponent cc = ff (e.getLwComponent());
      if (cc != null) requestFocus(cc);
    }
  }

  public void keyReleased(LwKeyEvent e) {}
  public void keyTyped   (LwKeyEvent e) {}

  public void dispose() {}

 /**
  * Tests if the component can have the focus.
  * @param c the specified component to test.
  * @return <code>true</code> if the component can have the focus.
  */
  protected /*C#virtual*/ boolean isFocusable (LwComponent c) {
    return c.isEnabled() && c.canHaveFocus();
  }

  private void freeFocus(LwComponent target) {
    if (target == focusOwner) requestFocus(null);
  }

  //
  //  The method looks for the next focus owner component starting from the given index
  //  in the given target cintainer component.
  //
  private static LwComponent fd(LwComponent target, int index)
  {
    if (target instanceof LwContainer)
    {
      LwContainer container    = (LwContainer)target;
      boolean     isNComposite = !(target instanceof LwComposite);
      for (int i=index; i<container.count(); i++)
      {
        LwComponent cc = (LwComponent)container.get(i);
        if (cc.isEnabled() && cc.isVisible() && cc.getWidth() > 0 && cc.getHeight() > 0 &&
           (isNComposite || !((LwComposite)container).catchInput(cc))&&
           (cc.canHaveFocus() || (cc = fd(cc, 0)) != null)             )
          return cc;
      }
    }
    return null;
  }

  private static LwComponent ff(LwComponent c)
  {
    LwComponent top = c;
    while (!(top instanceof LwLayer)) top = top.getLwParent();
    top = ((LwLayer)top).getFocusRoot();

    for (int index = 0;c != top.getLwParent();)
    {
      LwComponent cc = fd(c, index);
      if (cc != null) return cc;
      cc = c;
      c  = c.getLwParent();
      if (c != null) index = 1 + ((LwContainer)c).indexOf (cc);
    }
    return fd(top, 0);
  }
}

