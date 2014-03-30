package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This layer interface is a special container that is used as the LwDesktop children.
 * The desktop consists of the layers. Every layer is laid out by the desktop to place all
 * desktop room. When an event is performed the desktop "asks" the layers starting from the
 * tail layer if it is ready to get the event and delivers the event to the first-found
 * layer. The layered structure of the desktop allows you to implement additional features
 * (for example, internal frames, popup menu) basing on the LwVCL level.
 */
public interface LwLayer
extends LwContainer
{
  /**
   * Gets the layer id.
   * @return the layer id
   */
   Object getID ();

  /**
   * Invoked whenever the desktop lost focus.
   */
   void releaseFocus();

  /**
   * Invoked whenever the desktop got focus.
   */
   void setupFocus();

  /**
   * Invoked whenever a mouse button has been pressed.
   * @param x the x coordinate of the mouse pointer.
   * @param y the y coordinate of the mouse pointer.
   * @param mask the mask that specifies mouse buttons states.
   */
   void mousePressed(int x, int y, int mask);

  /**
   * Invoked whenever a key has been pressed.
   * @param keyCode the key code.
   * @param mask the keyboard trigger keys states.
   */
   void keyPressed  (int keyCode, int mask);

  /**
   * Tests if the layer is active.
   * @return <code>true</code> if the layer is active.
   */
   boolean isActive();

  /**
   * Gets if the focus root component.
   * @return a focus root component.
   */
   LwComponent getFocusRoot();
}
