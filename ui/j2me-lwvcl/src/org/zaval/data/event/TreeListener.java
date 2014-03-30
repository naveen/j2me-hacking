package org.zaval.data.event;

import org.zaval.data.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This listener interface is used to receive tree model originated events.
 */
public interface TreeListener
extends EventListener
{
 /**
  * Invoked when the item has been inserted as a member of the source tree.
  * @param target the specified tree model.
  * @param item the specified item that has been inserted into the tree model.
  */
  void itemInserted(Object target, Item item);

 /**
  * Invoked when the item has been removed from the source tree.
  * @param target the specified tree model.
  * @param item the specified item that has been removed from the tree model.
  */
  void itemRemoved (Object target, Item item);

 /**
  * Invoked when source tree item has been modified.
  * @param target the specified tree model.
  * @param item the specified item that has been modified.
  */
  void itemModified(Object target, Item item);
}
