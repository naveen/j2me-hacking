package org.zaval.lw;

import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is callback interface is used by a switch manager to notify when the state
 * of the switching component has been changed. The switching component can have one of the following
 * states : "on" or "off".
 */
public interface Switchable
{
 /**
  * Invoked when the state of a switching component has been changed.
  * @param state the component state. <code>true</code>
  * if the component has "on" state and <code>false</code> if the
  * component has "off" state.
  */
  void switched(boolean state);
}
