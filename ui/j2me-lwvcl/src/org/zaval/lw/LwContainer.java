package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface defines light weight container that inherits the LwComponent interface and
 * can contain other light weight components as child components.
 * <p>
 * The interface inherits LayoutContainer interface so it is possible to use a layout manager
 * to layout its child components.
 */
public interface LwContainer
extends LwComponent, LayoutContainer
{
 /**
  * Adds the specified lightweight component as a child of this container.
  * The method should call the <code> componentAdded </code> method its layout manager to inform
  * the layout manager that the new child has been added.
  * @param c the lightweight component to be added.
  */
  void add(LwComponent c);

 /**
  * Adds the specified lightweight component with the specified constraints as a child
  * of this container. The method should call the <code> componentAdded </code> method its
  * layout manager to inform the layout manager that the new child has been added.
  * @param s the object expressing layout constraints for this.
  * @param c the lightweight component to be added.
  */
  void add(Object s, LwComponent c);

 /**
  * Inserts the specified lightweight component with the specified constraints as a child
  * of this container at the specified position in the container list. The method should call
  * the <code>componentAdded</code> method its layout manager to inform the layout manager
  * that the new child has been added with the given constraints.
  * @param i the position in the container list at which to insert
  * the component.
  * @param s the object expressing layout constraints for this.
  * @param c the lightweight component to be added.
  */
  void insert(int i, Object s, LwComponent c);

 /**
  * Removes the component, specified by the index, from this container.
  * The layout manager of this container should be informed by calling
  * the <code>componentRemoved</code> method of the manager.
  * @param i the index of the component to be removed.
  */
  void remove(int i);

 /**
  * Removes all child components from this container.
  * The layout manager of this container should be informed by calling
  * the <code>componentRemoved</code> method of the manager for every child component
  * that has been removed.
  */
  void removeAll();

 /**
  * Searches the specified component among this container children and returns
  * an index of the component in the child list. If the component has not been found
  * than the method returns <code>-1</code>.
  * @param c the component to get index.
  * @return a child component index inside the container children list.
  */
  int indexOf(LwComponent c);

 /**
  * Sets the layout manager for this container.
  * @param m the specified layout manager.
  */
  void setLwLayout(LwLayout m);

 /**
  * Gets a layout manager for this container.
  * @return a layout manager.
  */
  LwLayout getLwLayout();

 /**
  * Paints additional elements (for example marker) after the container and its child components
  * have been rendered.
  * @param g the graphics context.
  */
  void paintOnTop(Graphics g);

 /**
  * Invoked by a child component whenever the child invalidates itself. Usually a child component
  * invalidate its parent. The method provides ability to control container invalidation by the
  * specified child component. The child asks the container if it should be invalidated by the
  * calling the method.
  * @param c the child component that wants to invalidate the container.
  * @return <code>true</code> if the container should be invalidated by the child component;
  * <code>false</code> otherwise.
  */
  boolean isInvalidatedByChild(LwComponent c);
}

