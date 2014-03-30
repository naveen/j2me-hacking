package org.zaval.lw.event;

import java.util.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is one of the core classes of the light weight library that is used to define
 * lightweight events delivering strategy. Actually all light weight components events
 * at first are passed to the manager and after that the manager decides how the events
 * should be delivered.
 * <p>
 * The manager provides listeners support for following events types:
 * <ul>
 *   <li>
 *     Lightweight component events listeners.
 *     Use the <code>addComponentListener</code> and <code>removeComponentListener</code> methods.
 *   </li>
 *   <li>
 *     Lightweight container events listeners.
 *     Use the <code>addContainerListener</code> and <code>removeContainerListener</code> methods.
 *   </li>
 *   <li>
 *     Lightweight mouse events listeners.
 *     Use the <code>addMouseListener</code> and <code>removeMouseListener</code> methods.
 *   </li>
 *   <li>
 *     Lightweight mouse motion events listeners.
 *     Use the <code>addMouseMotionListener</code> and <code>removeMouseMotionListener</code> methods.
 *   </li>
 *   <li>
 *     Lightweight key events listeners.
 *     Use the <code>addKeyListener</code> and <code>removeKeyListener</code> methods.
 *   </li>
 *   <li>
 *     Lightweight focus events listeners.
 *     Use the <code>addFocusListener</code> and <code>removeFocusListener</code> methods.
 *   </li>
 * </ul>
 * <p>
 * The manager provides universal methods to register and unregister light weight listeners,
 * the method checks if the object implements one of the events listeners and calls
 * appropriate method (see list above) to register or unregister appropriate listeners.
 * Use the <code>addXXXListener</code> and <code>removeXXXListener</code> for the purposes.
 */
public class LwEventManager
implements LwManager
{
  private Vector listeners = new Vector();
  private int[] indexes  = new int[6];
  private int[] counters = new int[6];

 /**
  * Adds the specified events listener. The method checks what lightweight listeners interfaces
  * are implemented by the listener and adds the events listeners as appropriate lightweight
  * events listeners.
  * @param l the specified events listener.
  */
  public void addXXXListener (EventListener l)
  {
    if (l instanceof LwMouseListener) 	   addMouseListener      ((LwMouseListener)l);
    if (l instanceof LwMouseMotionListener)addMouseMotionListener((LwMouseMotionListener)l);
    if (l instanceof LwKeyListener)        addKeyListener        ((LwKeyListener)l);
    if (l instanceof LwFocusListener)	   addFocusListener      ((LwFocusListener)l);
  }

 /**
  * Removes the specified events listener. The method checks what light weight listeners interfaces
  * are implemented with the listener and removes the events listeners from the appropriate listeners
  * lists.
  * @param l the specified events listener.
  */
  public void removeXXXListener (EventListener l)
  {
    if (l instanceof LwMouseListener)       removeMouseListener      ((LwMouseListener)l);
    if (l instanceof LwMouseMotionListener) removeMouseMotionListener((LwMouseMotionListener)l);
    if (l instanceof LwKeyListener)         removeKeyListener        ((LwKeyListener)l);
    if (l instanceof LwFocusListener)       removeFocusListener      ((LwFocusListener)l);
  }

 /**
  * Adds the specified mouse events listener.
  * @param l the specified events listener.
  */
  public synchronized void addMouseListener(LwMouseListener l) {
    a_(2, l);
  }

 /**
  * Removes the specified mouse events listener.
  * @param l the specified events listener.
  */
  public synchronized void removeMouseListener(LwMouseListener l) {
    r_(2, l);
  }

 /**
  * Adds the specified mouse motion events listener.
  * @param l the specified events listener.
  */
  public synchronized void addMouseMotionListener(LwMouseMotionListener l) {
    a_(3, l);
  }

 /**
  * Removes the specified mouse motion events listener.
  * @param l the specified events listener.
  */
  public synchronized void removeMouseMotionListener(LwMouseMotionListener l) {
    r_(3, l);
  }

 /**
  * Adds the specified focus events listener.
  * @param l the specified events listener.
  */
  public synchronized void addFocusListener(LwFocusListener l) {
    a_(4, l);
  }

 /**
  * Removes the specified focus events listener.
  * @param l the specified events listener.
  */
  public synchronized void removeFocusListener(LwFocusListener l) {
    r_(4, l);
  }

 /**
  * Adds the specified key events listener.
  * @param l the specified events listener.
  */
  public synchronized void addKeyListener(LwKeyListener l) {
    a_(5, l);
  }

 /**
  * Removes the specified key events listener.
  * @param l the specified events listener.
  */
  public synchronized void removeKeyListener(LwKeyListener l) {
    r_(5, l);
  }

  public void dispose() {
    listeners.removeAllElements();
    listeners = null;
  }

 /**
  * Performs the specified light weight event. The method is used to distribute
  * the event according the manager functionality and it is utilized with different
  * parts of the library.
  * @param e the specified light weight event.
  */
  public void perform(LwAWTEvent e)
  {
    LwComponent target = e.getLwComponent();
    switch (e.getUID())
    {
      case LwAWTEvent.MOUSE_UID:
      {
        if (e.getID() > 16)
        {
          performMMotion ((LwMouseEvent)e);
          if (target instanceof LwMouseMotionListener)
            process((LwMouseMotionListener)target, (LwMouseEvent)e);
          return;
        }
        else
        {
          performMouse ((LwMouseEvent)e);
          if (target instanceof LwMouseListener)
            process((LwMouseListener)target, (LwMouseEvent)e);
        }
      } break;
      case LwAWTEvent.KEY_UID:
      {
        performKey ((LwKeyEvent)e);
        if (target instanceof LwKeyListener)
          process((LwKeyListener)target, (LwKeyEvent)e);
      } break;
      case LwAWTEvent.FOCUS_UID:
      {
        performFocus ((LwFocusEvent)e);
        if (target instanceof LwFocusListener)
          process((LwFocusListener)target, (LwFocusEvent)e);
      } break;
    }

    for (target = target.getLwParent();target != null;target = target.getLwParent())
    {
      if (target instanceof LwChildrenListener)
        ((LwChildrenListener)target).childPerformed(e);
    }
  }

 /**
  * Gets the event destination. The method looks for composite parent of the specified target
  * component and defines if the parent should be the event destination
  * @param target the specified target component.
  * @return an event destination.
  */
  public /*C#virtual*/ LwComponent getEventDestination(LwComponent target) {
    LwComponent composite = findComposite(target);
    return composite == null?target:composite;
  }

  private void a_ (int index, Object o)
  {
    for (int i=indexes[index]; i < counters[index]; i++)
      if (listeners.elementAt(i).equals(o)) return;

    listeners.insertElementAt(o, indexes[index] + counters[index]);
    counters[index]++;
    for (int i=index + 1;i < indexes.length; i++) indexes[i]++;
  }

  private void r_ (int index, Object o)
  {
    int i=indexes[index];
    for (;i < counters[index];i++)
      if (listeners.elementAt(i).equals(o)) break;
    if (i == counters[index]) return;

    listeners.removeElementAt(i);
    counters[index]--;
    for (i=index+1;i<indexes.length;i++) indexes[i]--;
  }

  private void performMouse (LwMouseEvent e) {
    for (int i=indexes[2];i<indexes[2] + counters[2];i++)
      process((LwMouseListener)listeners.elementAt(i), e);
  }

  private void performMMotion (LwMouseEvent e) {
    for (int i=indexes[3];i<indexes[3] + counters[3];i++)
      process((LwMouseMotionListener)listeners.elementAt(i), e);
  }

  private void performKey (LwKeyEvent e) {
    for (int i=indexes[5];i<indexes[5] + counters[5];i++)
      process((LwKeyListener)listeners.elementAt(i), e);
  }

  private void performFocus (LwFocusEvent e) {
    for (int i=indexes[4];i<indexes[4] + counters[4];i++)
      process((LwFocusListener)listeners.elementAt(i), e);
  }

 /**
  * The method knows how the specified focus event has to be delivered to the given
  * focus listener.
  * @param l the specified focus listener.
  * @param e the specified focus event.
  */
  protected void process (LwFocusListener l, LwFocusEvent e) {
    if (e.getID() == LwFocusEvent.FOCUS_LOST) l.focusLost  (e);
    else l.focusGained(e);
  }

 /**
  * The method knows how the specified key event has to be delivered to the given
  * key listener.
  * @param l the specified key listener.
  * @param e the specified key event.
  */
  protected void process (LwKeyListener l, LwKeyEvent e)
  {
    switch (e.getID())
    {
      case LwKeyEvent.KEY_TYPED    : l.keyTyped   (e); break;
      case LwKeyEvent.KEY_RELEASED : l.keyReleased(e); break;
      case LwKeyEvent.KEY_PRESSED  : l.keyPressed (e); break;
    }
  }

 /**
  * The method knows how the specified mouse motion event has to be delivered to the given
  * mouse motion listener.
  * @param l the specified mouse motion listener.
  * @param e the specified mouse motion event.
  */
  protected void process(LwMouseMotionListener l, LwMouseEvent e)
  {
     switch (e.getID())
     {
       case LwMouseEvent.MOUSE_DRAGGED      : l.mouseDragged(e); break;
       case LwMouseEvent.MOUSE_STARTDRAGGED : l.startDragged(e); break;
       case LwMouseEvent.MOUSE_ENDDRAGGED   : l.endDragged  (e); break;
       case LwMouseEvent.MOUSE_MOVED        : l.mouseMoved  (e); break;
     }
  }

 /**
  * The method knows how the specified mouse event has to be delivered to the given
  * mouse listener.
  * @param l the specified mouse listener.
  * @param e the specified mouse event.
  */
  protected static void process(LwMouseListener l, LwMouseEvent e)
  {
     switch (e.getID())
     {
       case LwMouseEvent.MOUSE_CLICKED : l.mouseClicked (e); break;
       case LwMouseEvent.MOUSE_PRESSED : l.mousePressed (e); break;
       case LwMouseEvent.MOUSE_RELEASED: l.mouseReleased(e); break;
       case LwMouseEvent.MOUSE_ENTERED : l.mouseEntered (e); break;
       case LwMouseEvent.MOUSE_EXITED  : l.mouseExited  (e); break;
     }
  }

  /*C#private static LwComponent findComposite (LwComponent target) {*/
  private static final LwComponent findComposite (LwComponent target) { /*java*/
    return findComposite (target, target);
  }

  /*C#private static LwComponent findComposite (LwComponent target, LwComponent child)*/
  private static final LwComponent findComposite (LwComponent target, LwComponent child) /*java*/
  {
    if (target == null) return null;
    LwComponent parent = target.getLwParent();

    boolean b = (parent instanceof LwComposite);
    LwComponent res = findComposite (parent, b?parent:child);
    if (res != null) return res;

    return (b && ((LwComposite)parent).catchInput(child))?parent:null;
  }
}

