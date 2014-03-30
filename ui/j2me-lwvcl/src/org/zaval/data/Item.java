package org.zaval.data;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This class is used to represent tree model items.
 */

public class Item
{
  private Object value;

 /**
  * Constructs a new item. The item value is set to <code>null</code>.
  */
  public Item() {
    this((Object)null);
  }

 /**
  * Constructs a new item with the given initial value.
  * @param value the initial value of the item value.
  */
  public Item(Object value) {
    setValue(value);
  }

 /**
  * Constructs a new item as a copy of the given item.
  * @param item the given item.
  */
  public Item(Item item) {
    this(item.value);
  }

 /**
  * Gets the value of this item.
  * @return an item value. The value can be <code>null</code>.
  */
  public Object getValue() {
    return value;
  }

  public /*C#override*/ String toString () {
    return "" + value;
  }

 /**
  * Sets the specified value for this item.
  * @param v the specified value.
  */
  protected void setValue (Object v) {
    value = v;
  }
}

