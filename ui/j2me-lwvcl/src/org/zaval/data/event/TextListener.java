package org.zaval.data.event;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
/**
 * This is listener interface for receiving text events.
 */
public interface TextListener
extends EventListener
{
 /**
  * Invoked when a part of the text has been removed.
  * @param e the text event.
  */
  void textRemoved  (TextEvent e);

 /**
  * Invoked when a new text has been inserted in the text.
  * @param e the text event.
  */
  void textInserted (TextEvent e);

 /**
  * Invoked when the text has been updated.
  * @param e the text event.
  */
  void textUpdated (TextEvent e);

}
