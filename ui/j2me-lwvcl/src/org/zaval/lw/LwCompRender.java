package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class implements lightweight component render interface that provides ability to render
 * a lightweight component (the component should be used as a target for the render).
 */
public class LwCompRender
extends LwRender
{
 /**
  * Constructs the render with the specified target light weight component.
  * @param c the target light weight component to be rendered.
  */
  public LwCompRender(LwComponent c) {
    super(c);
  }

 /**
  * Calculates and returns the view preferred size. The method has not to use
  * the view insets to compute the preferred size (this is named "pure" preferred size).
  * The method returns the target light weight component preferred size if it is defined.
  * @return a "pure" preferred size of the view.
  */
  protected /*C#override*/ Dimension calcPreferredSize() {
    LwComponent target = (LwComponent)getTarget();
    return target == null? super.calcPreferredSize():target.getPreferredSize();
  }

 /**
  * Invoked by the <code>validate</code> method if the view is invalid. The method
  * is used to calculate metrical characteristics of the view and the method
  * is called only if it is necessary, so there you don't need to care about
  * superfluous calling and computing.
  */
  protected /*C#override*/ void recalc (){
    LwComponent target = (LwComponent)getTarget();
    if (target != null) target.validate();
  }

 /**
  * Paints the view using the given width and height. The location where the
  * view has to be painted is determined with <code>x</code> and <code>y</code>
  * coordinates.
  * @param g the specified context to be used for painting.
  * @param x the x coordinate.
  * @param y the y coordinate.
  * @param w the width of the view.
  * @param h the height of the view.
  * @param d the owner component.
  */
  public /*C#override*/ void paint(Graphics g, int x, int y, int w, int h, Object d)
  {
    LwComponent c = (LwComponent)getTarget();
    if (c != null)
    {
      if (LwToolkit.getDesktop(c) == null) c.setSize(w, h);
      int clipX = g.getClipX(), clipY = g.getClipY(), clipW = g.getClipWidth(), clipH = g.getClipHeight();
      g.translate(x, y);
      LwToolkit.getPaintManager().paint (g, c);
      g.translate(-x, -y);
      if (clipW > 0 && clipH > 0) g.setClip(clipX, clipY, clipW, clipH);
    }
  }
}



