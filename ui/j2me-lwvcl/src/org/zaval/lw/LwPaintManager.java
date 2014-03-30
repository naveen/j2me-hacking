package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This manager is abstract class that is used as base for creating own paint managers
 * for the light weight library. To create own paint manager it is necessary to
 * implement the <code>updateComponent</code> and <code>paintComponent</code> methods that
 * define update and paint algorithms for a light weight component.
 * <p>
 * Any light weight component implementation has to use the <code>paint</code> and
 * the <code>repaint</code> methods of the current paint manager to paint and repaint
 * itself.
 */
public abstract class LwPaintManager
implements LwManager
{
  public static Object LOCKER = "111";

 /**
  * Repaints the specified light weight component.
  * @param c the specified light weight component.
  */
  public /*C#virtual*/ void repaint(LwComponent c) {
    if (c.isVisible())
      repaint(c, 0, 0, c.getWidth(), c.getHeight());
  }

 /**
  * Repaints a part of the specified light weight component. The part is specified
  * with the bound (location and size).
  * @param c the specified light weight component.
  * @param x the x coordinate of the repainting part.
  * @param y the y coordinate of the repainting part.
  * @param w the width of the repainting part.
  * @param h the height of the repainting part.
  */
  public /*C#virtual*/ void repaint(LwComponent c, int x, int y, int w, int h)
  {
    if (c.isVisible() && w > 0 && h > 0)
    {
      LwDesktop desktop = LwToolkit.getDesktop(c);
      if (desktop != null)
      {
        Point p = LwToolkit.getAbsLocation(x, y, c);
        x = p.x;
        y = p.y;

       // System.out.println ("____ LwPaintManager.repaint() loc = " + p + ", DA = " + desktop.getDA () + ", w = " + w + ", h = " + h);

        synchronized (LOCKER)
        {
          //
          // The code below normalizes repainted area according to
          // the desktop size
          //
          int x1 = desktop.getX(), x2 = x1 + desktop.getWidth ();
          int y1 = desktop.getY(), y2 = y1 + desktop.getHeight();
          if (x < x1)
          {
            w -= (x1 - x);
            x  = x1;
          }
          if (y < y1)
          {
            h -= (y1 - y);
            y  = y1;
          }
          if (w + x > x2) w = x2 - x;
          if (h + y > y2) h = y2 - y;

         // System.out.println ("...... LwPaintManager.repaint() x = " + x + ", y = " + y + ",w = " + w + ", h = " + h);



          if (w > 0 && h > 0)
          {
            Rectangle da = desktop.getDA ();
            if (da.width > 0)
            {
              //!!!
              // test if the dirty area includes the repainted area
              //!!!
              if (x >= da.x && y >= da.y  &&
                  x + w <= da.x + da.width &&
                  y + h <= da.y + da.height  )
              {
               // System.out.println ("...... LwPaintManager.repaint() HALTED HALTED HALTED ........... ");


                return;
              }
              MathBox.unite(da.x, da.y, da.width, da.height, x, y, w, h, da);

              //System.out.println ("!!!!!!!! LwPaintManager.repaint() NEW DA = " + da);

            }
            else MathBox.intersection(0, 0, desktop.getWidth(), desktop.getHeight(), x, y, w, h, da);

            if (da.width > 0) desktop.repaint (da.x, da.y, da.width, da.height);
          }
        }
      }
    }
  }

 /**
  * The method initiates painting process for the specified root light weight component
  * using the graphics. The method has to be used with a light weight root component
  * implementation to start painting process.
  * @param g the specified graphics.
  * @param d the specified root lightweight component.
  */

  private int ccc;


  protected /*C#virtual*/ void startPaint(Graphics g, LwDesktop d)
  {

   // System.out.println (ccc + " = LwPaintManager.startPaint() ");

    d.validate();

    synchronized (LOCKER)
    {
      Rectangle r = d.getDA();

      if (r.width > 0)
      {
        int cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
        if (r.x < cx || r.y < cy ||
            r.x + r.width > cx + cw ||
            r.y + r.height > cy + ch  )
        {
          MathBox.unite(r.x, r.y, r.width, r.height, cx, cy, cw, ch, r);
          ((Canvas)d.getNCanvas ()).repaint(r.x, r.y, r.width, r.height);
          r.width = -1;
          return;
        }
      }
//      System.out.println (ccc + " = LwPaintManager.startPaint() CLIP = " + g.getClipBounds());

      paintDesktop(g, d);

//      System.out.println (ccc + " = LwPaintManager.startPaint() da = " + r);
      ccc++;

      r.width = -1;
    }
  }

  protected /*C#virtual*/ void paintDesktop(Graphics g, LwDesktop d) {
    paint(g, d);
  }

 /**
  * The method initiates painting process for the specified light weight component
  * using the graphics. The method has to be used with a light weight component implementation
  * to paint itself.
  * @param g the specified graphics.
  * @param c the specified lightweight component.
  */
  public /*C#virtual*/ void paint(Graphics g, LwComponent c)
  {
    //!!! This path for IE.
    int dw = c.getWidth(), dh = c.getHeight();
    if (dw != 0 && dh != 0)
    {
      c.validate();

      if (c.isOpaque()) updateComponent (g, c);
      paintComponent (g, c);
      if (c instanceof LwContainer)
      {
        LwContainer container = (LwContainer)c;
        g.clipRect(0, 0, dw, dh);
        int cx = g.getClipX(), cw = g.getClipWidth();
        int cy = g.getClipY(), ch = g.getClipHeight();

        if (cw > 0 && ch > 0)
        {
          int count = container.count();
          for (int i = 0; i<count ; i++)
          {
            LwComponent kid = (LwComponent)container.get(i);
            if (kid.isVisible())
            {
              int kidX = kid.getX(), kidY = kid.getY();
              int ix   = Math.max(kidX, cx);
              int iw   = Math.min(kidX + kid.getWidth(), cx + cw) - ix;
              int iy   = Math.max(kidY, cy);
              int ih   = Math.min(kidY + kid.getHeight(), cy + ch) - iy;

              if (iw > 0 && ih > 0)
              {
                g.setClip(ix, iy, iw, ih);
                g.translate(kidX, kidY);
                paint(g, kid);
                g.translate(-kidX, -kidY);
              }
            }
          }
          g.setClip(cx, cy, cw, ch);
          paintOnTop(g, container);
        }
      }
    }
  }

  public /*C#virtual*/ void dispose() {}

 /**
  * The method is called to update the specified component. The method has to just update
  * the component, it doesn't care about updating any child components.
  * @param g the specified graphics.
  * @param c the specified lightweight component to be updated.
  */
  abstract protected void updateComponent (Graphics g, LwComponent c);

 /**
  * The method is called to paint the specified component. The method has to just paint
  * the component, it doesn't care about painting any child components.
  * @param g the specified graphics.
  * @param c the specified lightweight component to be painted.
  */
  abstract protected void paintComponent(Graphics g, LwComponent c);

 /**
  * Probably the method will be redesigned
  */
  abstract protected void paintOnTop (Graphics g, LwContainer c);
}
