package org.zaval.lw.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * The interface should be implemented by the components that are interested in handling child
 * components events. As soon as you implement the interface the library event manager will
 * pass child events into <code>childPerformed</code> method.
 * Pay attention that some events types cannot be caught using the interface, for
 * example mouse motion event. The reason is the performance improvement.
 */
public interface LwChildrenListener {

 /**
  * Invoked whenever the child event has been performed.
  * @param e the child event.
  */
  void childPerformed(LwAWTEvent e);
}
