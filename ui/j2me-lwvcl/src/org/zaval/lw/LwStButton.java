package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This is state button component. The component can have one of the following
 * states:
 * <ul>
 *   <li>
 *     LwStButton.ST_ONSURFACE - indicates that the mouse cursor is inside of the component and
 *     a mouse button has not been pressed.
 *   </li>
 *   <li>
 *     LwStButton.ST_PRESSED - indicates that a mouse button has been pressed.
 *   </li>
 *   <li>
 *     LwStButton.ST_OUTSURFACE - indicates that the mouse cursor is outside of the component and
 *     a mouse button has not been pressed.
 *   </li>
 *   <li>
 *     LwStButton.ST_DISABLED - indicates that the component is disabled.
 *   </li>
 * </ul>
 * Use the <code>getState</code> method to get the current state.
 * <p>
 * Whenever a state of the component has been changed the <code>stateChanged</code> method
 * is called. Use the <code>addStateListener</code> and  <code>removeStateListener</code>
 * methods to listen the component state changing.
 * <p>
 * The component applies a view (if it is available) for an appropriate state as follow:
 * <ul>
 *   <li>
 *     "st.over" view corresponds to LwStButton.ST_ONSURFACE state.
 *   </li>
 *   <li>
 *     "st.pressed" view corresponds to LwStButton.ST_PRESSED state.
 *   </li>
 *   <li>
 *     "st.out" view corresponds to LwStButton.ST_OUTSURFACE state.
 *   </li>
 *   <li>
 *     "st.disabled" view corresponds to LwStButton.ST_DISABLED state.
 *   </li>
 * </ul>
 * <p>
 * The component generates action events whenever the button has been pressed or released.
 * Use the <code>addActionListener</code> and <code>removeActionListener</code> methods
 * to handle the events. Use the <code>fireByPress</code> method to define what event should
 * perform the action event: mouse released or mouse pressed event. The component
 * allows setting action event repeatable time. In this case the action event will be performed
 * again if for example a mouse button is kept down the certain time.
 * <p>
 * The sample below illustrates the component usage for creating tool bar button:
 * <pre>
 *    ...
 *    LwStButton   b = new LwStButton();
 *    LwAdvViewMan m = new LwAdvViewMan();
 *    m.put ("st.over", new LwImgRender("over.gif", LwView.ORIGINAL));
 *    m.put ("st.out", new LwImgRender("out.gif", LwView.ORIGINAL));
 *    m.put ("st.pressed", new LwImgRender("out.gif", LwView.ORIGINAL));
 *    b.setViewMan (m);
 *    ...
 * </pre>
 */
public class LwStButton
extends LwCanvas
implements LwMouseListener, LwMouseMotionListener
{
  /**
   * The ONSURFACE state.
   */
   public static final int ST_ONSURFACE = 0;

  /**
   * The PRESSED state.
   */
   public static final int ST_PRESSED = 1;

  /**
   * The OUTSURFACE state.
   */
   public static final int ST_OUTSURFACE = 2;

  /**
   * The DISABLED state.
   */
   public static final int ST_DISABLED = 3;

   private int state, time = 20;
   private LwActionSupport support, ssupport;

  /**
   * Constructs the class instance.
   */
   public LwStButton() {
     setState(ST_OUTSURFACE);
   }

  /**
   * Adds the specified state listener to receive action events from this component.
   * The data argument of the originated event is a previous state of the
   * button.
   * @param l the specified action listener.
   */
   public void addStateListener(LwActionListener l) {
     if (ssupport == null) ssupport = new LwActionSupport();
     ssupport.addListener(l);
   }

  /**
   * Removes the specified state listener.
   * @param l the specified action listener.
   */
   public void removeStateListener(LwActionListener l) {
     if (ssupport != null) ssupport.removeListener(l);
   }

  /**
   * Tests if the action event will be perfomed by mouse pressed event.
   * @return <code>true</code> if the action event is performed by mouse pressed event;
   * <code>false</code> if the action event is performed by mouse released event;
   */
   public boolean isFireByPress() {
     return MathBox.checkBit(bits, EVENT_BY_PRESS);
   }

  /**
   * Sets the trigger event to generate action event by the component and the given
   * repeatable time.
   * @param b <code>true</code> to set mouse pressed event as the trigger,
   * <code>false</code> to set mouse released event as the trigger.
   * @param time the repeatable time. The time defines an interval after
   * the action event will be performed again if the trigger event is not switched off.
   * Use <code>-1</code> as the property value to disable the event repeating.
   */
   public void fireByPress(boolean b, int time) {
     bits      = MathBox.getBits(bits, EVENT_BY_PRESS, b);
     this.time = time;
   }

   public /*C#virtual*/ void mouseClicked (LwMouseEvent e) {}

   public /*C#virtual*/ void mouseEntered (LwMouseEvent e) {
     setState(ST_ONSURFACE);
   }

   public /*C#virtual*/ void mouseExited  (LwMouseEvent e) {
     if (isEnabled()) setState(ST_OUTSURFACE);
   }

   public /*C#virtual*/ void mousePressed (LwMouseEvent e)
   {
     if (LwToolkit.isActionMask(e.getMask()))
     {
       setState(ST_PRESSED);
       if (isFireByPress()) perform ();
     }
   }

   public /*C#virtual*/ void mouseReleased(LwMouseEvent e)
   {
     if (LwToolkit.isActionMask(e.getMask()) && getState() != ST_OUTSURFACE)
     {
       setState(ST_ONSURFACE);
       if (!isFireByPress()) perform ();
     }
   }

   public /*C#virtual*/ void startDragged(LwMouseEvent e) {}
   public /*C#virtual*/ void endDragged  (LwMouseEvent e) {}
   public /*C#virtual*/ void mouseMoved  (LwMouseEvent e) {}

   public /*C#virtual*/ void mouseDragged(LwMouseEvent e)
   {
     if (LwToolkit.isActionMask(e.getMask()))
     {
       int x = e.getX();
       int y = e.getY();
       setState(x>=0 && x<width && y>=0 && y<=height?ST_PRESSED:ST_OUTSURFACE);
     }
   }


  /**
   * Gets the current button state.
   * @return a current button state.
   */
   public int getState () {
     return state;
   }

  /**
   * Adds the specified action listener to receive action events from this component.
   * @param a the specified action listener.
   */
   public void addActionListener(LwActionListener a) {
     if (support == null) support = new LwActionSupport();
     support.addListener(a);
   }

  /**
   * Removes the specified action listener so it no longer receives action events
   * from this component.
   * @param a the specified action listener.
   */
   public void removeActionListener(LwActionListener a) {
     if (support != null) support.removeListener(a);
   }

   public /*C#override*/ void setEnabled (boolean b) {
     super.setEnabled(b);
     setState(b?ST_OUTSURFACE:ST_DISABLED);
   }

  /**
   * Invoked whenever it is necessary to set an appropriate state view for the current state.
   */
   public /*C#virtual*/ void sync() {
     stateChanged(getState(), getState());
   }

   protected /*C#override*/ void viewManChanged() {
     sync();
   }

  /**
   * Sets the specified button state.
   * @param s the specified button state.
   */
   protected /*C#virtual*/ void setState (int s)
   {
     if (s != state)
     {
       int p = state;
       state = s;
       stateChanged(s, p);
       if (ssupport != null) ssupport.perform (this, new Integer(p));
     }
   }

  /**
   * Invoked when the state of the component has been changed. Use the method to
   * listen state changing.
   * @param state the new state of the component.
   * @param prevState the previous state of the component.
   */
   protected /*C#virtual*/ void stateChanged(int state, int prevState)
   {
     if (skins != null)
     {
       LwView n = skins.getView(ST[state]);
       if (skins.getView() != n)
       {
         skins.setView(n);
         repaint();
       }
     }
   }

  /**
   * Fires the specified action event to the registered listeners.
   */
   protected /*C#virtual*/ void perform () {
     if (support != null) support.perform (this, null);
   }

   private static final int EVENT_BY_PRESS = 64;

   private static String[] ST = { "st.over", "st.pressed", "st.out", "st.disabled" };
}


