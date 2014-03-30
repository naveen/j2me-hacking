package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This interface provides ability for a container to control child input events
 * delivery. The input events are performed by mouse or keyboard. The interface has just one
 * method that is invoked by LwEventManager manager to test if the input event for the given
 * child component should be caught by the parent component.
 */
public interface LwComposite
extends LwContainer
{
 /**
  * Checks if the input event for the specified child component should be caught by the parent.
  * @param child the specified child component.
  * @return <code>true</code> if input events for the child component should be caught by this
  * component; <code>false</code> otherwise.
  */
  boolean catchInput(LwComponent child);
}



