package org.zaval.util;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This interface can be implemented by the object that should control its validation state.
 */
public interface Validationable
{
 /**
  * Validates this object.
  */
  void validate();

 /**
  * Invalidates this object.
  */
  void invalidate();

 /**
  * Checks if this object is valid.
  * @return a valid state of the object. The object is valid if the method returns
  * <code>true</code>.
  */
  boolean isValid();
}
