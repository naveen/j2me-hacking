package org.zaval.lw;

import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This implementation of the switch manager class is used to control state for a set of switching
 * components where only one component can be "on". The library uses the manager to organize
 * radio groups. The example below shows the manager usage:
 * <pre>
 *    ...
 *    LwCheckbox ch1 = new LwCheckbox("Radio 1");
 *    LwCheckbox ch2 = new LwCheckbox("Radio 3");
 *    LwCheckbox ch3 = new LwCheckbox("Radio 3");
 *    LwGroup    group = new LwGroup();
 *    ch1.setSwitchManager(group);
 *    ch2.setSwitchManager(group);
 *    ch3.setSwitchManager(group);
 *    ...
 * </pre>
 */
public class LwGroup
extends LwSwitchManager
{
   private Switchable selected;

  /**
   * Gets the switching component that has "on" state for the group.
   * @return a selected switching component.
   */
   public Switchable getSelected() {
     return selected;
   }

  /**
   * Sets the specified switching component as selected component. In this case
   * the old selected component will be switched off. Use <code>null</code> if none of the components
   * need to be selected.
   * @param s the specified switching component.
   */
   public void setSelected(Switchable s) {
     if (s != null) setState(s, true);
     else           clearSelected();
   }

   public /*C#override*/ boolean getState(Switchable o) {
     return o == selected;
   }

   public /*C#override*/ void setState(Switchable o, boolean b)
   {
     if (b)
     {
       clearSelected();
       selected = o;
       if (selected != null)  selected.switched(true);
       perform (selected);
     }
   }

   private void clearSelected()
   {
     if (selected != null)
     {
       Switchable old = selected;
       selected = null;
       old.switched(false);
       perform (old);
     }
   }
}
