package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class implements set of borders views. The library registers the views as static objects
 * that can be used by the border view key. It is possible to use the following keys to get a border
 * view as a static object:
 * <ul>
 *   <li>"br.etched"</li>
 *   <li>"br.raised"</li>
 *   <li>"br.raised2"</li>
 *   <li>"br.sunken"</li>
 *   <li>"br.sunken2"</li>
 *   <li>"br.plain"</li>
 *   <li>"br.dot"</li>
 * </ul>
 * It is possible to specify a colors set that should be used to render the border. The colors set
 * is represented with the three colors: the brightest color, middle color and the darkest color.
 * Use on of the following ways to set the colors:
 * <ul>
 *   <li>By the border view class constructor.</li>
 *   <li>By default. In this case the "white", "gray" and "gray" colors will be used.</li>
 *   <li>
 *       By the "lw.properties" file. For this purpose add the colors into the properties file
 *       with the following keys: "br.c1", "br.c2", "br.c3".
 *   </li>
 * </ul>
 * <p>
 * The sample below illustrates how the borders set can be used for light weight components:
 * <pre>
 *    ...
 *    LwLabel label = new LwLabel("Label Text");
 *    label.getViewMan(true).setBorder("br.dot");
 *    ...
 * </pre>
 */
public class LwBorder
extends LwView
{
   /**
    * The RAISED border type.
    */
    public static final int RAISED = 1;

   /**
    * The SUNKEN border type.
    */
    public static final int SUNKEN = 2;

   /**
    * The ETCHED border type.
    */
    public static final int ETCHED = 3;

   /**
    * The PLAIN border type.
    */
    public static final int PLAIN = 4;

   /**
    * The DOT border type.
    */
    public static final int DOT = 5;

   /**
    * The SUNKEN2 border type.
    */
    public static final int SUNKEN2 = 6;

   /**
    * The RAISED2 border type.
    */
    public static final int RAISED2 = 7;

    private int borderType;
    private Color[] colors;

   /**
    * Constructs the new border view with the specified border type.
    * The view uses STRETCH view type.
    * @param type the specified border type.
    */
    public LwBorder(int type) {
      this(type, (Color)LwToolkit.getStaticObj("br.c1"),
                 (Color)LwToolkit.getStaticObj("br.c2"),
                 (Color)LwToolkit.getStaticObj("br.c3"));
    }

   /**
    * Constructs the new border view with the specified border type and the given colors set.
    * The view uses STRETCH view type.
    * @param type the specified border type.
    * @param brightest the given brightest color.
    * @param middle the given middle color.
    * @param darkest the given darkest color.
    */
    public LwBorder(int type, Color brightest, Color middle, Color darkest)
    {
      super(STRETCH);
      setBorderType(type);
      colors = new Color[3];
      colors[0] = brightest == null?Color.white:brightest;
      colors[1] = middle    == null?Color.gray :middle;
      colors[2] = darkest   == null?Color.black:darkest;
    }

   /**
    * Gets the border type.
    * @return a border type.
    */
    public int getBorderType() {
      return borderType;
    }

   /**
    * Draws the left vertical border line between the coordinates (x1,y1) and (x2,y2).
    * @param g the specified context to be used for drawing.
    * @param x1 the x coordinate of the start of the line.
    * @param y1 the y coordinate of the start of the line.
    * @param x2 the x coordinate of the end of the line.
    * @param y2 the y coordinate of the end of the line.
    */
    protected /*C#virtual*/ void leftLine(Graphics g, int x1, int y1, int x2, int y2)
    {
      g.setColor(colors[1]);
      switch(borderType)
      {
        case DOT:
        {
          LwToolkit.drawDotVLine(g, y1, y2, x1);
        } break;
        case PLAIN:
        {
          g.setColor(colors[2]);
          g.drawLine(x1, y1, x1, y2);
        } break;
        case ETCHED:
        {
          g.drawLine(x1, y1, x1, y2-1);
          g.setColor(colors[0]);
          g.drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 2);
        } break;
        case RAISED2:
        {
          g.drawLine(x1, y1, x1, y2 - 1);
          x1++;
        }
        case RAISED:
        {
          g.setColor(colors[0]);
          g.drawLine(x1, y1, x1, y2 - 1);
        } break;
        case SUNKEN2:
        case SUNKEN:
        {
          g.drawLine(x1, y1, x1, y2 - 1);
          if (borderType == SUNKEN)
          {
            g.setColor(colors[2]);
            g.drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 1);
          }
        } break;
      }
    }

   /**
    * Draws the right vertical border line between the coordinates (x1,y1) and (x2,y2).
    * @param g the specified context to be used for drawing.
    * @param x1 the x coordinate of the start of the line.
    * @param y1 the y coordinate of the start of the line.
    * @param x2 the x coordinate of the end of the line.
    * @param y2 the y coordinate of the end of the line.
    */
    protected /*C#virtual*/ void rightLine(Graphics g, int x1, int y1, int x2, int y2)
    {
      g.setColor(colors[1]);
      switch(borderType)
      {
        case DOT: LwToolkit.drawDotVLine(g, y1, y2, x2); break;
        case PLAIN:
        {
          g.setColor(colors[2]);
          g.drawLine(x2, y1, x2, y2);
        } break;
        case ETCHED:
        {
          g.drawLine(x2 - 1, y1, x2 - 1, y2 - 1);
          g.setColor(colors[0]);
          g.drawLine(x2, y1 + 1, x2, y2);
        } break;
        case RAISED2:
        {
          g.drawLine(x2 - 1, y1 + 1, x2 - 1, y2 - 1);
          g.setColor(colors[2]);
          g.drawLine(x2, y1, x2, y2);
        } break;
        case RAISED:
        {
          g.drawLine(x2, y1, x2, y2);
        } break;
        case SUNKEN2:
        case SUNKEN:
        {
          g.setColor(colors[0]);
          g.drawLine(x2, y1, x2, y2);
        } break;
      }
    }

   /**
    * Draws the top horizontal border line between the coordinates (x1,y1) and (x2,y2).
    * @param g the specified context to be used for drawing.
    * @param x1 the x coordinate of the start of the line.
    * @param y1 the y coordinate of the start of the line.
    * @param x2 the x coordinate of the end of the line.
    * @param y2 the y coordinate of the end of the line.
    */
    protected /*C#virtual*/ void topLine(Graphics g, int x1, int y1, int x2, int y2)
    {
      g.setColor(colors[1]);
      switch(borderType)
      {
        case DOT:
        {
          LwToolkit.drawDotHLine(g, x1, x2, y1);
        } break;
        case PLAIN:
        {
          g.setColor(colors[2]);
          g.drawLine(x1, y1, x2, y1);
        } break;
        case ETCHED:
        {
          g.drawLine(x1, y1, x2 - 1, y1);
          g.setColor(colors[0]);
          g.drawLine(x1 + 1, y1 + 1, x2 - 2, y1 + 1);
        } break;
        case RAISED2:
        {
          g.drawLine(x1, y1, x2 - 1, y1);
          y1++;
          x1++;
          x2--;
        }
        case RAISED:
        {
          g.setColor(colors[0]);
          g.drawLine(x1, y1, x2 - 1, y1);
        } break;
        case SUNKEN2:
        case SUNKEN:
        {
          g.drawLine(x1, y1, x2 - 1, y1);
          if (borderType == SUNKEN)
          {
            g.setColor(colors[2]);
            g.drawLine(x1 + 1, y1 + 1, x2 - 1, y1 + 1);
          }
        } break;
      }
    }

   /**
    * Draws the bottom horizontal border line between the coordinates (x1,y1) and (x2,y2).
    * @param g the specified context to be used for drawing.
    * @param x1 the x coordinate of the start of the line.
    * @param y1 the y coordinate of the start of the line.
    * @param x2 the x coordinate of the end of the line.
    * @param y2 the y coordinate of the end of the line.
    */
    protected /*C#virtual*/ void bottomLine(Graphics g, int x1, int y1, int x2, int y2)
    {
      g.setColor(colors[1]);
      switch(borderType)
      {
        case DOT:
        {
          LwToolkit.drawDotHLine(g, x1, x2, y2);
        } break;
        case PLAIN:
        {
          g.setColor(colors[2]);
          g.drawLine(x1, y2, x2, y2);
        } break;
        case ETCHED:
        {
          g.drawLine(x1, y2 - 1, x2 - 1, y2 - 1);
          g.setColor(colors[0]);
          g.drawLine(x1 + 1, y2, x2, y2);
        } break;
        case RAISED2:
        {
          g.drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
          g.setColor(colors[2]);
          g.drawLine(x1, y2, x2, y2);
        } break;
        case RAISED:
        {
           g.drawLine(x1, y2, x2, y2);
        } break;
        case SUNKEN2:
        case SUNKEN:
        {
          g.setColor(colors[0]);
          g.drawLine(x1, y2, x2, y2);
        } break;
      }
    }

   /**
    * Paints the view using the given width and height. The location where the
    * view has to be painted is determined with <code>x</code> and <code>y</code>
    * coordinates. This method is implemented to define the
    * face of the border view.
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
      leftLine  (g, x, y, xx, yy);
      rightLine (g, x, y, xx, yy);
      topLine   (g, x, y, xx, yy);
      bottomLine(g, x, y, xx, yy);
    }

    public /*C#override*/ int getTop() {
      return 2;
    }

    public /*C#override*/ int getLeft() {
      return 2;
    }

    public /*C#override*/ int getBottom() {
      return 2;
    }

    public /*C#override*/ int getRight() {
      return 2;
    }

    protected /*C#override*/ Dimension calcPreferredSize() {
      return new Dimension (2, 2);
    }

    private void setBorderType(int t) {
      if(t < RAISED || t > RAISED2) throw new IllegalArgumentException();
      borderType = t;
    }
}
