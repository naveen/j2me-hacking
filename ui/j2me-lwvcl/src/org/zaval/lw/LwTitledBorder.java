package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This	 class inherits LwBorder class to support a title area for the border view. The view using
 * the title bounds and alignment that are provided with an owner component, paints the border
 * with a break (title area). To use the title border view it is necessary to do the following steps:
 * <ul>
 *   <li>
 *     The owner component has to implement LwTitleInfo interface to provide the title
 *     parameters: the title bounds - to determine location and size of the title and
 *     the title alignment - to determine where the title has to be placed. The alignment
 *     can have one of the following values :  LwToolkit.TOP - to use top border line as
 *     the title place, LwToolkit.BOTTOM - to use bottom border line as the title place,
 *     LwToolkit.LEFT - to use left border line as the title place and LwToolkit.RIGHT -
 *     to use right border line as the title place.
 *   </li>
 *   <li>
 *     Sets the title border view with appropriate line alignment for the owner component:
 *     <pre>
 *       ...
 *       LwComponent c = LwTitleInfoComponent();
 *       c.getViewMan(true).setBorder(new LwTitledBorder(LwBorder.ETCHED));
 *       ...
 *     </pre>
 *   </li>
 * </ul>
 * <p>
 * The view has line alignment property that defines border line alignment relatively the
 * title area and can have following values: LwToolkit.TOP, LwToolkit.BOTTOM, LwToolkit.CENTER.
 * The next samples illustrate the property usage:
 * <br><br>
 * <table border="1" width="100%" cellspacing="1">
 *   <tr class="tb3">
 *     <td align="center">
 *        <b>Property value</b>
 *     </td>
 *     <td align="center">
 *        <b>Result application</b>
 *     </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">
 *        LwToolkit.BOTTOM
 *     </td>
 *     <td align="center">
 *       <img src="images/TitledBorderBottom.gif">
 *     </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">
 *       LwToolkit.CENTER
 *     </td>
 *     <td align="center">
 *       <img src="images/TitledBorderCenter.gif">
 *     </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td align="center">
 *       LwToolkit.TOP
 *     </td>
 *     <td align="center">
 *       <img src="images/TitledBorderTop.gif">
 *     </td>
 *   </tr>
 * </table>
 */
public class LwTitledBorder
extends LwBorder
{
    private int lineAlignment;
   /**
    * Constructs the border view with the specified border type. The default line alignment is
    * LwToolkit.BOTTOM.
    * @param type the border type.
    */
    public LwTitledBorder(int type) {
      this(type, LwToolkit.BOTTOM);
    }

   /**
    * Constructs the border view with the specified border type and the line alignment.
    * @param type the border type.
    * @param a the line alignment.
    */
    public LwTitledBorder(int type, int a)
    {
      super(type);
      if (a != LwToolkit.BOTTOM && a != LwToolkit.TOP && a != LwToolkit.CENTER)
        throw new IllegalArgumentException();
      lineAlignment = a;
    }

   /**
    * Paints the view using a given width and height. The location where the
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
      if (d instanceof LwTitleInfo)
      {
        Rectangle r = ((LwTitleInfo)d).getTitleBounds();
        if (r != null)
        {
           int o = ((LwTitleInfo)d).getTitleAlignment();
           int borderType = getBorderType();

           int xx = w - 1;
           int yy = h - 1;
           switch (o)
           {
             case LwToolkit.TOP:
             {
               if (lineAlignment == LwToolkit.TOP)    y = r.y;
               else
               if (lineAlignment == LwToolkit.BOTTOM) y = r.y + r.height - 1;
               else
               if (lineAlignment == LwToolkit.CENTER) y = r.y + r.height/2;

               leftLine(g, x, y, x, yy);
               rightLine(g, xx, y, xx, yy);
               topLine(g, x, y, r.x, y);
               topLine(g, r.x + r.width, y, xx, y);
               bottomLine(g, x, yy, xx, yy);
             } break;
             case LwToolkit.LEFT:
             {
               if (lineAlignment == LwToolkit.TOP)    x = r.x;
               else
               if (lineAlignment == LwToolkit.BOTTOM) x = r.x + r.width - 1;
               else
               if (lineAlignment == LwToolkit.CENTER) x = r.x + r.width/2;

               leftLine(g, x, y, x, r.y);
               leftLine(g, x, r.y + r.height, x, yy);
               rightLine(g, xx, y, xx, yy);
               topLine(g, x, y, xx, y);
               bottomLine(g, x, yy, xx, yy);
             } break;
             case LwToolkit.BOTTOM:
             {
               if (lineAlignment == LwToolkit.TOP)    yy = r.y + r.height - 1;
               else
               if (lineAlignment == LwToolkit.BOTTOM) yy = r.y;
               else
               if (lineAlignment == LwToolkit.CENTER) yy = r.y + r.height/2;

               leftLine(g, x, y, x, yy);
               rightLine(g, xx, y, xx, yy);
               topLine(g, x, y, xx, y);
               bottomLine(g, x, yy, r.x, yy);
               bottomLine(g, r.x + r.width, yy, xx, yy);
             } break;
             case LwToolkit.RIGHT :
             {
               if (lineAlignment == LwToolkit.TOP)    xx = r.x + r.width - 1;
               else
               if (lineAlignment == LwToolkit.BOTTOM) xx = r.x;
               else
               if (lineAlignment == LwToolkit.CENTER) xx = r.x + r.width/2;

               leftLine(g, x, y, x, yy);
               rightLine(g, xx, y, xx, r.y);
               rightLine(g, xx, r.y + r.height, xx, yy);
               topLine(g, x, y, r.x, y);
               bottomLine(g, x, yy, xx, yy);
             } break;
           }
           return;
        }
      }
      super.paint(g, x, y, w, h, d);
    }
}
