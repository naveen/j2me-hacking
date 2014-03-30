package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This class is implementation of the LwPaintManager for the library.
 * The implementation supports:
 * <ul>
 *   <li>Light weight component views painting.</li>
 * </ul>
 */
public class LwPaintManImpl
extends LwPaintManager
{
 /**
  * The method is called to update the specified component. The updating process
  * can be occurred with the following two ways:
  * <ul>
  *   <li>
  *     If the component has a view manager and the manager defines a background
  *     view, than the view will be used to fill the component background.
  *   </li>
  *   <li>
  *     If the component has not any view manager or the manager doesn't define a
  *     background view, than the method fills the component with its background
  *     color.
  *   </li>
  * </ul>
  * After that the method calls <code>update</code> method of the component.
  * @param g the specified graphics.
  * @param c the specified lightweight component to be updated.
  */
  protected void updateComponent (Graphics g, LwComponent c)
  {
     LwViewMan skins = c.getViewMan(false);
     if (skins != null && skins.getBg() != null) paintSkin(g, skins.getBg(), c);
     else
     {
       Color bg = c.getBackground();
       LwComponent p = c.getLwParent();

       if ( p == null || !p.isOpaque() ||
           ((skins=p.getViewMan(false)) != null && skins.getBg() != null) ||
           !p.getBackground().equals(bg))
       {
         g.setColor(bg);
         g.fillRect(0, 0, c.getWidth(), c.getHeight());
       }
     }
     c.update(g);
  }

 /**
  * The method is called to paint the specified component. The method uses
  * the border and face views that can be provided with a view manager of
  * the specified component to paint the component.
  * @param g the specified graphics.
  * @param c the specified lightweight component to be painted.
  */
  protected void paintComponent(Graphics g, LwComponent c)
  {
    LwViewMan skins = c.getViewMan(false);
    LwView    view  = null;
    if (skins != null)
    {
      view = skins.getView();
      if (view != null && view.getType() != LwView.ORIGINAL)
      {
        paintSkin(g, view, c);
        view = null;
      }
      paintSkin(g, skins.getBorder(), c);
    }

    Insets insets = c.getInsets();
    if (insets.left + insets.right + insets.top + insets.bottom > 0)
    {
      //
      //!!! This is MSIE clipRect() method equivalent.
      //
      int cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
      if (cw == 0 || ch == 0) return;
      int x1 = Math.max(cx, insets.left);
      int y1 = Math.max(cy, insets.top);
      g.setClip(x1, y1, Math.min(cx + cw, insets.left + c.getWidth() - insets.left - insets.right) - x1,
                        Math.min(cy + ch, insets.top + c.getHeight() - insets.top - insets.bottom) - y1);
    }

    Point p = c.getOrigin();
    if (p != null) g.translate(p.x, p.y);
    if (view != null) paintSkin(g, view, c);
    c.paint(g);
    if (p != null) g.translate(-p.x, -p.y);
  }

  protected void paintOnTop (Graphics g, LwContainer c)
  {
    Point p = c.getOrigin();
    if (p != null) g.translate(p.x, p.y);
    c.paintOnTop(g);
    if (p != null) g.translate(-p.x, -p.y);
  }

  private void paintSkin(Graphics g, LwView s, Layoutable c)
  {
    if (s != null)
    {
      switch (s.getType())
      {
        case LwView.STRETCH :
        {
          s.paint(g, 0, 0, c.getWidth(), c.getHeight(), c);
        } break;
        case LwView.MOSAIC  :
        {
          Dimension ps = s.getPreferredSize();
          if (ps.width > 0 && ps.height > 0)
          {
            int dx = c.getWidth ()/ps.width +  (c.getWidth()%ps.width>0?1:0);
            int dy = c.getHeight()/ps.height + (c.getHeight()%ps.height>0?1:0);
            int xx = 0;
            for (int i=0; i<dx; i++)
            {
               int yy = 0;
               for (int j=0; j<dy; j++)
               {
                 s.paint(g, xx, yy, ps.width, ps.height, c);
                 yy += ps.height;
               }
               xx += ps.width;
            }
          }
        } break;
        case LwView.ORIGINAL:
        {
          s.paint(g, ((LwCanvas)c).getLeft(), ((LwCanvas)c).getTop(), c);
        } break;
      }
    }
  }
}




