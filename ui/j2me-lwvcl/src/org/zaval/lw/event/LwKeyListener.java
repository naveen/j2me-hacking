package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is listener interface to receive light weight key events.
 */
public interface LwKeyListener
extends EventListener
{
 /**
  * Invoked when a key has been pressed.
  * @param e the specified key event.
  */
  void keyPressed (LwKeyEvent e);

 /**
  * Invoked when a key has been released.
  * @param e the specified key event.
  */
  void keyReleased(LwKeyEvent e);

 /**
  * Invoked when a key has been typed.
  * @param e the specified key event.
  */
  void keyTyped(LwKeyEvent e);
}
