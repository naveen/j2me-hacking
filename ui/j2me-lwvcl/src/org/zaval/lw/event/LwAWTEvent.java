package org.zaval.lw.event;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is base class for most light weight events to provide:
 * <ul>
 *   <li>The event id by the <code>getID</code> method.</li>
 *   <li>
 *     The event class UID by the <code>getUID</code> method. The event class UID defines type of the
 *     event, for example LwMouseEvent has LwAWTEvent.MOUSE_UID UID.
 *   </li>
 * </ul>
 */
public class LwAWTEvent
extends EventObject
{
 /**
  * The mouse event UID.
  */
  public static final int MOUSE_UID  = 2;

 /**
  * The key event UID.
  */
  public static final int KEY_UID    = 4;

 /**
  * The focus event UID.
  */
  public static final int FOCUS_UID  = 8;

 /**
  * The event id.
  */
  protected int id, uid;

 /**
  * Constructs an event object with the specified source object, the event id and the given UID.
  * @param target the object where the event originated.
  * @param id the specified event id.
  * @param uid the specified event UID.
  */
  protected LwAWTEvent(LwComponent target, int id, int uid)
  {
    super(target);
    this.id = id;
    this.uid = uid;
  }

 /**
  * Gets the event id.
  * @return an event id.
  */
  public int getID() {
    return id;
  }

 /**
  * Gets the source object of the event as LwComponent instance.
  * @return a source object as LwComponent instance.
  */
  public LwComponent getLwComponent() {
    return (LwComponent)getSource();
  }

 /**
  * Gets the UID of the event. The UID defines the event class.
  * @return an UID.
  */
  public /*C#virtual*/ int getUID () {
    return uid;
  }
}
