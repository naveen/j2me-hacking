package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is listener interface to receive light weight mouse motion events.
 */
public interface LwMouseMotionListener
extends EventListener
{
 /**
  * Invoked when the mouse starts dragged.
  * @param e the specified mouse event.
  */
  void startDragged(LwMouseEvent e);

 /**
  * Invoked when the mouse ends dragged.
  * @param e the specified mouse event.
  */
  void endDragged  (LwMouseEvent e);

 /**
  * Invoked when a mouse button is pressed on a component and then dragged.
  * Mouse drag events will continue to be delivered to the component where
  * the first originated until the mouse button is released (regardless of
  * whether the mouse position is within the bounds of the component).
  * @param e the specified mouse event.
  */
  void mouseDragged(LwMouseEvent e);

 /**
  * Invoked when the mouse pointer has been moved on a light weight component
  * (with no buttons no down).
  * @param e the specified mouse event.
  */
  void mouseMoved  (LwMouseEvent e);
}
