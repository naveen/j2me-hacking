package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This listener interface for receiving light weight focus events.
 */
public interface LwFocusListener
extends EventListener
{
 /**
  * Invoked when the light weight component gained focus.
  * @param e the specified focus event.
  */
  void focusGained(LwFocusEvent e);

 /**
  * Invoked when the light weight component lost focus.
  * @param e the specified focus event.
  */
  void focusLost(LwFocusEvent e);
}
