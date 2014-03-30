package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is implementation of the abstract switch manager class.
 * The class can be used to store two state - "on" and "off" for a switching component.
 */
public class LwSwitchManImpl
extends LwSwitchManager
{
  private boolean state;

  public /*C#override*/ boolean getState(Switchable o) {
    return state;
  }

  public /*C#override*/ void setState(Switchable o, boolean b)
  {
    if (state != b)
    {
      state = b;
      o.switched(b);
      perform (o);
    }
  }
}
