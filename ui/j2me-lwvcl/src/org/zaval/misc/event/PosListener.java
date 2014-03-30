package org.zaval.misc.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is listener interface for receiving pos events.
 */
public interface PosListener
extends EventListener
{
 /**
  * Invoked when a virtual position has been changed.
  * @param target the specified pos controller.
  * @param prevOffset the previous offset.
  * @param prevLine the previous line.
  * @param prevColumn the previous column.
  */
  void posChanged(Object target, int prevOffset, int prevLine, int prevColumn);
}

