package org.zaval.lw.event;

import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is used with light weight components to describe keyboard events.
 */
public class LwKeyEvent
extends LwAWTEvent
{
 /**
  * The key typed event type.
  */
  public static final int KEY_TYPED     = 512;

 /**
  * The key released event type.
  */
  public static final int KEY_RELEASED  = 1024;

 /**
  * The key pressed event type.
  */
  public static final int KEY_PRESSED   = 2048;

  private int code, mask;
  private char ch;

 /**
  * Constructs the event object with the specified source object, the event id,
  * the key code associated with the key, the character that has been typed with
  * the key and the mask that describes state of a keyboard.
  * @param target the object where the event originated.
  * @param id the specified event id.
  * @param code the specified key code associated with the key.
  * @param ch the specified key character.
  * @param mask the specified key mask. Using the mask you can define for example
  * status of "Ctrl", "Alt", "Shift" and so on keys.
  */
  public LwKeyEvent(LwComponent target, int id, int code, char ch, int mask)
  {
    super(target, id, KEY_UID);
    reset (target, id, code, ch, mask);
  }

  public void reset (LwComponent target, int id, int code, char ch, int mask)
  {
    source    = target;
    this.id   = id;
    this.code = code;
    this.mask = mask;
    this.ch   = ch;
  }

 /**
  * Gets the key code associated with the key in the event.
  * @return a key code.
  */
  public int getKeyCode () {
    return code;
  }

 /**
  * Gets the key character that has been typed.
  * @return a key character.
  */
  public char getKeyChar () {
    return ch;
  }

 /**
  * Gets the keyboard mask. The mask can be used to test if the "Ctrl", "Alt"
  * and so on keys are pressed or not. Use org.zaval.port.j2me.event.InputEvent key modifiers constants
  * for the purpose.
  * @return a keyboard mask.
  */
  public int getMask(){
    return mask;
  }
}


