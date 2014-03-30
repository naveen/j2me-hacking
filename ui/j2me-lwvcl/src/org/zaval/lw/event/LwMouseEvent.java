package org.zaval.lw.event;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is used with light weight components to describe mouse events.
 */
public class LwMouseEvent
extends LwAWTEvent
{
 /**
  * The mouse clicked event type.
  */
  public static final int MOUSE_CLICKED = 1;

 /**
  * The mouse pressed event type.
  */
  public static final int MOUSE_PRESSED  = 2;

 /**
  * The mouse released event type.
  */
  public static final int MOUSE_RELEASED = 4;

 /**
  * The mouse entered event type.
  */
  public static final int MOUSE_ENTERED  = 8;

 /**
  * The mouse exited event type.
  */
  public static final int MOUSE_EXITED   = 16;

 /**
  * The mouse dragged event type.
  */
  public static final int MOUSE_DRAGGED = 32;

 /**
  * The mouse start dragged event type.
  */
  public static final int MOUSE_STARTDRAGGED = 64;

 /**
  * The mouse end dragged event type.
  */
  public static final int MOUSE_ENDDRAGGED = 128;

 /**
  * The mouse moved event type.
  */
  public static final int MOUSE_MOVED = 256;

  protected int x, y, ax, ay, mask, clickCount;

 /**
  * Constructs the event object with the specified source object, the event id,
  * the absolute mouse pointer location, mask and click count. The location defines x and
  * y coordinates relatively the desktop parent component of the light weight source component.
  * @param target the object where the event originated.
  * @param id the specified event id.
  * @param ax the x coordinate relatively the desktop parent component.
  * @param ay the y coordinate relatively the desktop parent component.
  * @param mask the specified mask. The mask can be used to check trigger keys
  * status.
  * @param clickCount the number of consecutive clicks.
  */
  public LwMouseEvent(LwComponent target, int id, int ax, int ay, int mask, int clickCount) {
    super(target, id, MOUSE_UID);
    reset(target, id, ax, ay, mask, clickCount);
  }

  public /*C#virtual*/ void reset(LwComponent target, int id, int ax, int ay, int mask, int clickCount)
  {
    source = target;
    this.id = id;
    this.ax = ax;
    this.ay = ay;
    this.mask = mask;
    this.clickCount = clickCount;
    Point p = LwToolkit.getRelLocation(ax, ay, target);
    this.x = p.x;
    this.y = p.y;
  }

 /**
  * Gets the mask. The mask can be used to test if the "Ctrl", "Alt"
  * and so on keys are pressed or not. Use org.zaval.port.j2me.event.InputEvent key modifiers constants
  * for the purpose.
  * @return a mask.
  */
  public int getMask(){
    return mask;
  }

 /**
  * Gets the x coordinate of the mouse pointer relatively the desktop parent component.
  * @return a x coordinate.
  */
  public int getAbsX() {
    return ax;
  }

 /**
  * Gets the y coordinate of the mouse pointer relatively the desktop parent component.
  * @return an y coordinate.
  */
  public int getAbsY() {
    return ay;
  }

 /**
  * Gets the x coordinate of the mouse pointer relatively the source component.
  * @return a x coordinate.
  */
  public int getX() {
    return x;
  }

 /**
  * Gets the y coordinate of the mouse pointer relatively the source component.
  * @return an y coordinate.
  */
  public int getY() {
    return y;
  }

 /**
  * Gets the number of consecutive mouse clicks.
  * @return a number of consecutive mouse clicks.
  */
  public int getClickCount() {
    return clickCount;
  }
}

