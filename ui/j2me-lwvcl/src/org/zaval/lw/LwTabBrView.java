package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is render that is used as a default tab border render for the LwNotebook component.
 * The border colors schema can be customized by the "lw.properties" file or during the view
 * initialization. To set the colors schema by the properties file use the following keys:
 * <ul>
 *   <li> "br.c1" - for the brightest color.
 *   <li> "br.c2" - for the midle color.
 *   <li> "br.c3" - for the darkest color.
 * </ul>
 */
public class LwTabBrView
extends LwView
{
   private int orient, vgap = 2, hgap = 5;
   private Color[] colors = new Color[3];

  /**
   * Constructs a tab border view with the given orientation.
   * @param o the specified orientation. Use one of the following constants as the
   * argument value:
   * <ul>
   *   <li>LwToolkit.TOP
   *   <li>LwToolkit.LEFT
   *   <li>LwToolkit.RIGHT
   *   <li>LwToolkit.BOTTOM
   * </ul>
   */
   public LwTabBrView(int o) {
     this(o, (Color)LwToolkit.getStaticObj("br.c1"),
             (Color)LwToolkit.getStaticObj("br.c2"),
             (Color)LwToolkit.getStaticObj("br.c3"));
   }

  /**
   * Constructs a tab border view with the given orientation and the specified colors schema.
   * @param o the specified orientation. Use one of the following constants as the
   * argument value:
   * <ul>
   *   <li>LwToolkit.TOP
   *   <li>LwToolkit.LEFT
   *   <li>LwToolkit.RIGHT
   *   <li>LwToolkit.BOTTOM
   * </ul>
   * @param brightest the brightest color of the colors schema.
   * @param middle the middle color of the colors schema.
   * @param darkest the darkest color of the colors schema.
   */
   public LwTabBrView(int o, Color brightest, Color middle, Color darkest)
   {
     super(STRETCH);
     if (o != LwToolkit.TOP &&
         o != LwToolkit.BOTTOM &&
         o != LwToolkit.LEFT &&
         o != LwToolkit.RIGHT)  throw new IllegalArgumentException();
     orient = o;
     colors[0] = brightest == null?Color.white:brightest;
     colors[1] = middle    == null?Color.gray :middle;
     colors[2] = darkest   == null?Color.black:darkest;
   }

  /**
   * Paints the view using the given width and height at the specified location.
   * @param g the specified context to be used for painting.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param w the width of the view.
   * @param h the height of the view.
   * @param d the owner component.
   */
   public /*C#override*/ void paint(Graphics g, int x, int y, int w, int h, Object d)
   {
     int xx = x + w - 1;
     int yy = y + h - 1;
     g.setColor (colors[0]);
     switch (orient)
     {
       case LwToolkit.TOP:
       {
         g.drawLine (x + 3, y, xx-1, y);
         g.drawLine (x, y + 3, x, yy);
         g.drawLine (x, y + 3,x + 3, y);

         g.setColor (colors[1]);
         g.drawLine (xx - 1, y + 1, xx - 1, yy);
         g.setColor (colors[2]);
         g.drawLine (xx, y + 2, xx, yy);
       }  break;
       case LwToolkit.BOTTOM:
       {
         g.drawLine (x, y, x, yy-3);

         g.setColor (colors[1]);
         g.drawLine (xx - 1, y, xx - 1, yy - 1);
         g.drawLine (x + 3, yy-1, xx-1, yy-1);
         g.drawLine (x, yy-4, x + 4, yy);

         g.setColor (colors[2]);
         g.drawLine (xx, y, xx, yy);
         g.drawLine (x + 3, yy, xx-1, yy);
         g.drawLine (x, yy-3, x + 3, yy);
       } break;
       case LwToolkit.LEFT:
       {
         g.drawLine (x + 3, y, xx, y);
         g.drawLine (x, y + 3, x, yy-3);
         g.drawLine (x, y + 3,x + 3, y);

         g.setColor (colors[1]);
         g.drawLine (x + 3, yy - 1, xx, yy - 1);
         g.drawLine (x, yy-4, x + 4, yy);

         g.setColor (colors[2]);
         g.drawLine (x + 3, yy, xx, yy);
         g.drawLine (x, yy-3, x + 3, yy);
       } break;
       case LwToolkit.RIGHT:
       {
         g.drawLine (x, y, xx - 3, y);

         g.setColor (colors[1]);
         g.drawLine (xx - 4, y, xx, y + 4);
         g.drawLine (xx - 1, y + 4, xx - 1, yy - 4);
         g.drawLine (xx, yy - 4, xx - 4, yy);
         g.drawLine (x, yy-1, xx - 4, yy-1);

         g.setColor (colors[2]);
         g.drawLine (xx - 3, y, xx, y + 3);
         g.drawLine (xx, y + 3, xx, yy - 3);
         g.drawLine (xx, yy - 3, xx - 3, yy);
         g.drawLine (x, yy, xx - 3, yy);
       } break;
     }
   }

   public /*C#override*/ int getTop() {
    return vgap;
   }

   public /*C#override*/ int getLeft() {
    return hgap;
   }

   public /*C#override*/ int getBottom() {
    return vgap;
   }

   public /*C#override*/ int getRight() {
    return hgap;
   }
}


