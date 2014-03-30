package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is listener interface to receive light weight mouse events.
 */
public interface LwMouseListener
extends EventListener
{
 /**
  * Invoked when the mouse button has been clicked on a light weight component.
  * @param e the specified mouse event.
  */
  void mouseClicked (LwMouseEvent e);

 /**
  * Invoked when the mouse enters a light weight component.
  * @param e the specified mouse event.
  */
  void mouseEntered (LwMouseEvent e);

 /**
  * Invoked when the mouse exits a light weight component.
  * @param e the specified mouse event.
  */
  void mouseExited  (LwMouseEvent e);

 /**
  * Invoked when the mouse button has been pressed on a light weight component.
  * @param e the specified mouse event.
  */
  void mousePressed (LwMouseEvent e);

 /**
  * Invoked when the mouse button has been released on a light weight component.
  * @param e the specified mouse event.
  */
  void mouseReleased(LwMouseEvent e);
}
