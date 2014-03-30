package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This listener interface for receiving the action events.
 */
public interface LwActionListener
extends EventListener
{
 /**
  * Invoked when an action event occurred.
  * @param src the specified source where the event has been originated.
  * @param data the event data.
  */
  void actionPerformed(Object src, Object data);
}
