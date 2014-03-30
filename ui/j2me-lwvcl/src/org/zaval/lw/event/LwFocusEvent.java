package org.zaval.lw.event;

import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is used with lightweight components to notify when a component
 * gets or leaves the focus.
 */
public class LwFocusEvent
extends LwAWTEvent
{
 /**
  * The focus lost event type.
  */
  public static final int FOCUS_LOST = 4096;

 /**
  * The focus gained event type.
  */
  public static final int FOCUS_GAINED = 8192;

 /**
  * Constructs the event object with the specified source object and the event id.
  * @param target the object where the event originated.
  * @param id the specified event id.
  */
  public LwFocusEvent(LwComponent target, int id) {
    super(target, id, FOCUS_UID);
  }
}


