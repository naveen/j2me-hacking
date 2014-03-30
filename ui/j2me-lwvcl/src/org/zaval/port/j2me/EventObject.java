package org.zaval.port.j2me;

/**
 * This abstract class represents an event that has unique integer identifier.
 * You can inherit the class to create own event classes that may be identified by an
 * integer id.
 */
public class EventObject
{
  protected Object source;

 /**
  * Constructs the event object with the specified source object and the event id.
  * @param <code>src</code> the object where the event originated.
  * @param <code>id</code> the specified event id.
  */
  public EventObject(Object src) {
    source = src;
  }

  public Object getSource() {
    return source;
  }
}
