package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is base implementation of the layer interface.
 */
public class LwBaseLayer
extends LwPanel
implements LwLayer
{
   private LwComponent prevFocusOwner;
   private Object id;

  /**
   * Constructs the layer with the specified id.
   * @param id the specified id.
   */
   public LwBaseLayer(Object id) {
     if (id  == null) throw new IllegalArgumentException();
     this.id = id;
   }

   public /*C#virtual*/ Object getID () {
     return id;
   }

   public /*C#virtual*/ void releaseFocus()  {
     prevFocusOwner = LwToolkit.getFocusManager().getFocusOwner();
     LwToolkit.getFocusManager().requestFocus(null);
   }

   public /*C#virtual*/ void setupFocus() {
     LwComponent cc = (prevFocusOwner == null)?LwToolkit.getFocusManager().findFocusable(getFocusRoot()):prevFocusOwner;
     LwToolkit.getFocusManager().requestFocus(cc);
   }

   public /*C#virtual*/ void mousePressed(int x, int y, int mask) {}

   public /*C#virtual*/ void keyPressed  (int keyCode, int mask) {}

   public /*C#virtual*/ boolean isActive() {
     return true;
   }

   public /*C#virtual*/ LwComponent getFocusRoot() {
     return this;
   }
}
