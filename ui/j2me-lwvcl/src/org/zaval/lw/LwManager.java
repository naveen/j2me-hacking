package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This interface should be implemented by any light weight manager class to provide set of methods
 * that is necessary to control its behaviour.
 */
public interface LwManager
{
  /**
   * Disposes the manager. The method is supposed to be used for disposing different manager related structures
   * to free system resources. The method is invoked by LwVCL core when the manager ends its "live".
   */
   void dispose();
}
