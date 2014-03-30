package org.zaval.lw;

import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This abstract class is used as base for lightweight renders implementations.
 * The main purpose to provide face for an object (text, image and
 * so on). So, to create own render it is necessary to inherit the class and
 * implement <code> paint </code> method that knows how to paint the target object.
 */
public abstract class LwRender
extends LwView
{
   private Object target;

  /**
   * Constructs the render with the specified target component that should be rendered.
   * The constructor sets ORIGINAL view type.
   * @param target the target component.
   */
   public LwRender(Object target) {
     this(target, ORIGINAL);
   }

  /**
   * Constructs the render with the specified target component that should be rendered
   * and the specified view type.
   * @param target the target component to be rendered.
   * @param type the specified view type.
   */
   public LwRender(Object target, int type) {
     super(type);
     setTarget (target);
   }

  /**
   * Gets the target object.
   * @return a target object.
   */
   public Object getTarget () {
     return target;
   }

  /**
   * Sets the specified target object.
   * @param o the target object.
   */
   public void setTarget (Object o)
   {
     if (target != o)
     {
       Object old = target;
       target = o;
       targetWasChanged(old, target);
       invalidate();
     }
   }

  /**
   * Invoked whenever the target object has been set.
   * The method can be overridden if the render wants to be notified about
   * re-setting the target object.
   * @param o the old target object.
   * @param n the new target object.
   */
   protected /*C#virtual*/ void targetWasChanged(Object o, Object n) {}
}

