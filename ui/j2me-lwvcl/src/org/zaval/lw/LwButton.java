package org.zaval.lw;

import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is button component, that can use
 * any other component as the button label. The component has the following
 * features:
 * <ul>
 *   <li>
 *     The class performs action events when the button has been pressed. Use
 *     the <code>addActionListener</code> and <code>removeActionListener</code> methods to catch
 *     the event.
 *   </li>
 *   <li>
 *     The component uses "button.off" view to show unpressed state and
 *     "button.on" view to show pressed state. The two keys are used to set appropriate
 *     view, the default view manager reads the view as a static object by the <code>getView</code>
 *     static method of LwToolkit.class. It is possible to redefine the view using
 *     LwAdvViewMan as follow:
 *     <pre>
 *        ...
 *        LwButton     button = new LwButton("Button");
 *        LwAdvViewMan man    = new LwAdvViewMan();
 *        man.put("button.off", new LwImgRender("off.gif", LwView.ORIGINAL));
 *        man.put("button.on",  new LwImgRender("on.gif", LwView.ORIGINAL));
 *        button.setViewMan(man);
 *        ...
 *     </pre>
 *   </li>
 *   <li>
 *     As it has been described above the component is a composite. The sample below illustrates
 *     how an image with a label can be used as the button title:
 *     <pre>
 *       ...
 *       LwContainer buttonLabel = new LwPanel();
 *       buttonLabel.setLwLayout(new LwFlowLayout());
 *       buttonLabel.add(new LwImage("label.gif"));
 *       buttonLabel.add(new LwLabel("Button text"));
 *       LwButton button = new LwButton(buttonLabel);
 *       ...
 *     </pre>
 *   </li>
 * </ul>
 */
public class LwButton
extends LwActContainer
implements LwMouseListener, LwKeyListener, LwMouseMotionListener
{
  private boolean pressed;
  private LwActionSupport support;

 /**
  * Constructs a button component with no label.
  */
  public LwButton() {
    this((LwComponent)null);
  }

 /**
  * Constructs a button component with the specified label text.
  * In this case the component creates LwLabel component with the text and
  * uses it as the button label.
  * @param lab the specified label text.
  */
  public LwButton(String lab) {
    this(new LwLabel(lab));
  }

 /**
  * Constructs a button component with the specified component as a label.
  * @param t the specified component to be used as the button label.
  */
  public LwButton(LwComponent t) {
    super(t);
    setPressed(false);
  }

 /**
  * Adds the specified action listener to receive action events from this button.
  * @param a the specified action listener.
  */
  public void addActionListener(LwActionListener a) {
    if (support == null) support = new LwActionSupport();
    support.addListener(a);
  }

 /**
  * Removes the specified action listener so it no longer receives action events
  * from this button.
  * @param a the specified action listener.
  */
  public void removeActionListener(LwActionListener a) {
    if (support != null) support.removeListener(a);
  }

 /**
  * Fires the action event for list of LwActionListener.
  */
  protected /*C#virtual*/ void perform() {
    if (support != null) support.perform(this, null);
  }

  public void mouseClicked(LwMouseEvent e) {}

  public void mousePressed(LwMouseEvent e)  {
    if (LwToolkit.isActionMask(e.getMask())) setPressed(true);
  }

  public void mouseReleased(LwMouseEvent e) {
    if (isPressed()) perform();
    setPressed(false);
  }

  public void keyPressed (LwKeyEvent e) {
    if (!isPressed() && LwToolkit.ACTION_KEY == LwToolkit.getKeyType(e.getKeyCode(),e.getMask())) setPressed(true);
  }

  public void keyReleased(LwKeyEvent e)
  {
    if (isPressed())
    {
      setPressed(false);
      perform();
    }
  }

  public void focusLost(LwFocusEvent e) {
    setPressed(false);
  }

  public void mouseEntered(LwMouseEvent e) {}

  public void mouseExited(LwMouseEvent e) {
    setPressed(false);
  }

  public void startDragged(LwMouseEvent e) {}
  public void endDragged  (LwMouseEvent e) {}
  public void mouseMoved  (LwMouseEvent e) {}

  public void mouseDragged(LwMouseEvent e)
  {
    if (LwToolkit.isActionMask(e.getMask()))
    {
      int x = e.getX();
      int y = e.getY();
      setPressed(x>=0&&x<width&&y>=0&&y<=height);
    }
  }

  public void keyTyped(LwKeyEvent e) {}

 /**
  * Invoked whenever the view manager has been set. The method is overridden with the class
  * to set appropriate ("button.off" or "button.on") view by the view manager.
  */
  protected /*C#override*/ void viewManChanged() {
    getViewMan(true).setView(isPressed ()?"button.on":"button.off");
  }

 /**
  * Sets the specified state of the button. The button can have "pressed" or "un-pressed" state.
  * @param b the specified state. The <code>true</code> value is used to
  * set the "pressed" state, the <code>false</code> value to set the "un-pressed" state.
  */
  protected /*C#virtual*/ void setPressed (boolean b)
  {
    if (pressed != b || getViewMan(true).getView() == null)
    {
      pressed = b;
      getViewMan(true).setView(b?"button.on":"button.off");
      repaint();
    }
  }

 /**
  * Gets the state of the button.
  * @return <code>true</code> if the button is "pressed", <code>false</code> if the button
  * is "un-pressed".
  */
  public boolean isPressed () {
    return pressed;
  }
}
