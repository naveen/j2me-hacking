package org.zaval.lw;

import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This abstract class can be used to control state of switching components.
 * The switching component is a component that can have one of the following two states: "off" or "on".
 * The class provides ability to control state of the component by the <code>getState</code>
 * and <code>setState</code> methods.
 * <p>
 * It is possible to organize combined action for a set of switching components. For example,
 * if we have group of switching components where only one component can be "on" at the given
 * moment than we can use the class to implement appropriate switch manager. First of all we
 * should have ability to register and un-register the components as a members of the set.
 * For the purpose the class provides
 * <code>install</code> and <code>uninstall</code> methods. Than we should implement
 * <code>getState</code> and <code>setState</code> abstract methods. The input argument
 * for the <code>install</code>, <code>uninstall</code> and <code>setState</code> methods is Switchable
 * interface. The interface has to be used as a callback interface to notify a switching
 * component by calling the <code>switched</code> method about
 * changing state of the switching component.
 * <p>
 * The switch manager has to perform action events when a state has been changed. Use the
 * <code>addActionListener</code> and <code>removeActionListener</code> to register and
 * un-register listeners of the events.
 */
public abstract class LwSwitchManager
{
  private LwActionSupport support;

 /**
  * Registers the specified switching component that is controlled by the manager.
  * @param o the specified switching component.
  */
  public /*C#virtual*/ void install (Switchable o) {
    o.switched(getState(o));
  }

 /**
  * Un-registers the specified switching component is controlled by the manager.
  * @param o the specified switching component.
  */
  public /*C#virtual*/ void uninstall(Switchable o) {}

 /**
  * Adds the specified action listener to receive action events from this manager.
  * @param a the specified action listener.
  */
  public void addActionListener(LwActionListener a) {
    if (support == null) support = new LwActionSupport();
    support.addListener(a);
  }

 /**
  * Removes the specified action listener so it no longer receives action events
  * from this manager.
  * @param a the specified action listener.
  */
  public void removeActionListener(LwActionListener a) {
    if (support != null) support.removeListener(a);
  }

 /**
  * Fires the action event for the list of LwActionListener with the specified switch
  * manager as the event source and the switchable component as the data.
  * @param s the specified switching component.
  */
  protected /*C#virtual*/ void perform(Switchable s) {
    if (support != null) support.perform(this, s);
  }

 /**
  * Gets state for the specified switching component.
  * @param o the specified switching component.
  * @return <code>true</code> if the switching component has "on" state, <code>false</code>
  * if it has "off" state.
  */
  public abstract boolean getState(Switchable o);

 /**
  * Sets the specified state for the switching component.
  * @param o the specified switching component.
  * @param b the specified state. The <code>true</code> value corresponds to
  * "on" state and <code>false</code> value corresponds to "off" state.
  */
  public abstract void setState(Switchable o, boolean b);
}


