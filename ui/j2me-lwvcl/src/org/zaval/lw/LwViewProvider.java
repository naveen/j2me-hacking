package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This interface is used to provide a view for the specified object. For example,
 * tree view component uses the interface to define a view for its nodes.
 */
public interface LwViewProvider
{
 /**
  * Gets the view for the specified object of the specified component.
  * @param src the specified component.
  * @param obj the specified object.
  * @return a view.
  */
  LwView getView(Object src, Object obj);
}


