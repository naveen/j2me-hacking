package org.zaval.lw;

import java.util.*;
import java.io.*;
import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.lw.rs.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

public class LwToolkit
{
 /**
  * The horizontal alignment constant.
  */
  public static final int HORIZONTAL = 1;

 /**
  * The vertical alignment constant.
  */
  public static final int VERTICAL = 2;

 /**
  * The none vertical and none horizontal alignment constant.
  */
  public static final int NONE = 0;

 /**
  * Specifies the left horizontal alignment type.
  */
  public static final int LEFT   = 1;

 /**
  * Specifies the right horizontal alignment type.
  */
  public static final int RIGHT = 2;

 /**
  * Specifies the top vertical alignment type.
  */
  public static final int TOP = 4;

 /**
  * Specifies the bottom vertical alignment type.
  */
  public static final int BOTTOM = 8;

 /**
  * Specifies the center horizontal or vertical alignment type.
  */
  public static final int CENTER = 16;

 /**
  * The dark blue color definition.
  */
  public static final Color darkBlue = new Color(0, 0, 140);

 /**
  * The default font metrics definition.
  */
  public static FontMetrics FONT_METRICS;

 /**
  * The action key type.
  */
  public static final int ACTION_KEY    = 1;

 /**
  * The cancel key type.
  */
  public static final int CANCEL_KEY    = 2;

 /**
  * The pass focus key type.
  */
  public static final int PASSFOCUS_KEY = 3;

  private static Hashtable  staticObjects;
  private static String     base;

  //
  //
  //  Start NONE-Abstract methods
  //
  //
  private static String         version;
  private static Vector         managers = new Vector();
  private static LwPaintManager paintManager;
  private static LwEventManager eventManager;

  private static Class SRC_CL;

 /**
  * Reads a properties file by the specified path and returns it as java.util.Properties instance.
  * The specified path points to the properties file relatively resources base directory.
  * If the path starts with '/' it is used as is.
  * @param name the specified path.
  */
  public static Properties getProperties(String name)
  {
    try {
      InputStream is = SRC_CL.getResourceAsStream(name.charAt(0)=='/'?name:(getResourcesBase().concat(name)));

      if (is != null)
      {
        Properties p = new Properties();
        p.load(is);
        return p;
      }
    }
    catch (Exception e) { e.printStackTrace (); }
    return null;
  }

 /**
  * Reads an image by the specified path. The path is relative base directory.
  * If the path starts with '/' it is used as is.
  * @param name the specified path.
  * @return the image object or <code>null</code> if the image cannot be fetched.
  */
  public static Image getImage(String name)
  {
    try {
      return Image.createImage(SRC_CL.getResourceAsStream(name.charAt(0)=='/'?name:(getResourcesBase().concat(name))));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

 /**
  * Gets the font metrics for the specified font.
  * @param f the specified font.
  * @return a font metrics for the specified font.
  */
  public static FontMetrics getFontMetrics(Font f) {
    return f == null?FONT_METRICS:new FontMetrics(f);
  }

 /**
  * Draws the horizontal dotted line between the (x1,y1) and (x2,y1) points.
  * @param gr the specified context to be used for drawing.
  * @param x1 the x coordinate of the start of the line.
  * @param x2 the x coordinate of the end of the line.
  * @param y1 the y coordinate of the start and end of the line.
  */
  public static void drawDotHLine(Graphics g, int x1, int x2, int y1)
  {
    int prev = g.target.getStrokeStyle();
    g.target.setStrokeStyle(javax.microedition.lcdui.Graphics.DOTTED);
    g.drawLine (x1, y1, x2, y1);
    g.target.setStrokeStyle(prev);
  }

 /**
  * Draws the vertical dotted line between the (x1,y1) and (x1,y2) points.
  * @param gr the specified context to be used for drawing.
  * @param y1 the y coordinate of the start of the line.
  * @param y2 the y coordinate of the end of the line.
  * @param x1 the x coordinate of the start and end of the line.
  */
  public static void drawDotVLine(Graphics g, int y1, int y2, int x1)
  {
    int prev = g.target.getStrokeStyle();
    g.target.setStrokeStyle(javax.microedition.lcdui.Graphics.DOTTED);
    g.drawLine (x1, y1, x1, y2);
    g.target.setStrokeStyle(prev);
  }

 /**
  * Draws the dotted outline of the specified rectangle using the current color.
  * The resulting rectangle will cover an area (w + 1) pixels wide by (h + 1) pixels tall.
  * @param gr the specified context to be used for drawing.
  * @param x the x coordinate of the rectangle to be drawn (left top corner).
  * @param y the y coordinate of the rectangle to be drawn (left top corner).
  * @param w the width of the rectangle to be drawn.
  * @param h the height of the rectangle to be drawn.
  */
  public static void drawDotRect(Graphics g, int x, int y, int w, int h)
  {
    int prev = g.target.getStrokeStyle();
    g.target.setStrokeStyle(javax.microedition.lcdui.Graphics.DOTTED);
    g.drawRect(x, y, w - 1, h-1);
    g.target.setStrokeStyle(prev);
  }

 /**
  * Gets the size of the screen.
  * @return a size of the screen.
  */
  public static final Dimension getScreenSize () {
    //!!!
    return new Dimension(300, 300);
  }

 /**
  * Draws marker for the specified rectangular area basing on the given background and foreground
  * colors.
  * @param g the specified graphics context.
  * @param x the x coordinate of the top-left corner of the rectangular area.
  * @param y the y coordinate of the top-left corner of the rectangular area.
  * @param w the width of the rectangular area.
  * @param h the height of the rectangular area.
  * @param bg the background color.
  * @param fc the foreground color.
  */
  public static void drawMarker(Graphics g, int x, int y, int w, int h, Color bg, Color fc)
  {
    //!!!
    g.setColor(fc);
    g.drawRect(x, y, w - 1, h - 1);
  }

 /**
  * Draws line using XOR mode.
  * @param target the specified component.
  * @param x1 the first x coordinate of the line.
  * @param y1 the first y coordinate of the line.
  * @param x2 the second x coordinate of the line.
  * @param y2 the second y coordinate of the line.
  */
  public static void drawXORLine (LwComponent target, int x1, int y1, int x2, int y2)
  {
    throw new RuntimeException();
  }

 /**
  * Draws rectangle using XOR mode.
  * @param target the specified component.
  * @param x the top-left corner x coordinate of the rectangle.
  * @param y the top-left corner y coordinate of the rectangle.
  * @param w the rectangle width.
  * @param h the rectangle height.
  */
  public static void drawXORRect (LwComponent target, int x, int y, int w, int h)
  {
    throw new RuntimeException();
  }

 /**
  * Fills the rectangle using XOR mode.
  * @param target the specified component.
  * @param x the top-left corner x coordinate of the rectangle.
  * @param y the top-left corner y coordinate of the rectangle.
  * @param w the rectangle width.
  * @param h the rectangle height.
  */
  public static void fillXORRect (LwComponent target, int x, int y, int w, int h)
  {
    throw new RuntimeException();
  }

 /**
  * Don't touch the method it will be redesigned in the further version.
  */
  public static boolean isActionMask(int mask) {
    return mask == 0 || ((mask & LwToolkit.BUTTON1_MASK) >  0&&
                         (mask & LwToolkit.BUTTON3_MASK) == 0  );
  }

 /**
  * Gets the list of available fonts names.
  * @return a list of available fonts names.
  */
  public static String[] getFontList()  {
    return new String[] { "PROPORTIONAL", "MONOSPACE", "SYSTEM" };
  }

 /**
  * Gets the key type for the specified key code and key mask.
  * The method should return one of the following constant : ACTION_KEY, CANCEL_KEY, PASSFOCUS_KEY or
  * -1 in all other cases.
  * @param keyCode the specified key code.
  * @param mask the specified key mask.
  * @return a key type.
  */
  public static int getKeyType(int keyCode, int mask)
  {
    if (keyCode == LwToolkit.VK_ENTER || (char)keyCode == ' ') return ACTION_KEY;
    else
    if (keyCode == LwToolkit.VK_ESCAPE) return CANCEL_KEY;
    else
    if (keyCode == LwToolkit.VK_TAB && mask == 0) return PASSFOCUS_KEY;
    return -1;
  }

  //
  //
  //  End NONE-Abstract
  //
  //

 /**
  * Gets the static object by the specified key.
  * @param key the specified key.
  * @return a static object.
  */
  public static Object getStaticObj(String key) {
    return staticObjects.get(key);
  }

 /**
  * Puts the static object with the specified key. The method cannot be used to redefine a manager.
  * @param key the specified key.
  * @param obj the specified static object. Use <code>null</code> as the static object
  * to remove it.
  * @return a previous static object stored with the specified key.
  */
  public static Object putStaticObj(String key, Object obj) {
    return (obj == null)?staticObjects.remove(key):staticObjects.put(key, obj);
  }

 /**
  * Gets a static object by the specified key as LwView class instance.
  * @param key the specified key.
  * @return a static object.
  */
  public static LwView getView(String key) {
    return (LwView)staticObjects.get(key);
  }

 /**
  * Gets the library version.
  * @return the library version.
  */
  public static final String getVersion () {
    return version;
  }

 /**
  * Gets the base directory to look up LwVCL resources (properties, images etc).
  * @return a base directory.
  */
  public static final String getResourcesBase() {
    return base;
  }

 /**
  * Gets the desktop container for the specified component. Desktop container is top-level
  * container where the specified component is resided.
  * @param c the specified component.
  * @return a desktop.
  */
  public static LwDesktop getDesktop(LwComponent c) {
    for (;c != null && !(c instanceof LwDesktop);c = c.getLwParent());
    return (LwDesktop)c;
  }

 /**
  * Returns the immediate child component for the parent and the given child.
  * @param parent the parent component.
  * @param child  the child component.
  * @return an immediate child component.
  */
  public static LwComponent getDirectChild(LwComponent parent, LwComponent child) {
    for (;child != null && child.getLwParent() != parent; child = child.getLwParent());
    return child;
  }

 /**
  * Returns the immediate child component at the specified location of the parent component.
  * @param x the x coordinate relatively the parent component.
  * @param y the y coordinate relatively the parent component.
  * @param p the parent component.
  * @return an immediate child component.
  */
  public static int getDirectCompAt(int x, int y, LwContainer p)
  {
    for (int i=0; i<p.count(); i++)
    {
      LwComponent c = (LwComponent)p.get(i);
      if (c.isVisible() && c.getX() <= x && c.getY() <= y &&
          c.getX() + c.getWidth() > x && c.getY() + c.getHeight() > y)
        return i;
    }
    return -1;
  }

 /**
  * Checks if the component is ancestor for the component.
  * @param p the specified parent component.
  * @param c the specified child component.
  * @return <code>true</code> if the component is ancestor;
  * otherwise <code>false</code>.
  */
  public static boolean isAncestorOf(LwComponent p, LwComponent c) {
    while (c != null && c != p) c = c.getLwParent();
    return c != null;
  }

 /**
  * Returns the absolute location for the given relative location of the component.
  * The absolute location is calculated relatively a native component where the
  * light weight component is resided.
  * @param x the x coordinate relatively the component.
  * @param y the y coordinate relatively the component.
  * @param c the lightweight component.
  * @return an absolute location.
  */
  public static Point getAbsLocation(int x, int y, LwComponent c)
  {
    do {
      x += c.getX();
      y += c.getY();
      c = c.getLwParent();
    } while (c != null);
    return new Point (x, y);
  }

 /**
  * Returns the absolute location of the component.
  * The absolute location is calculated relatively a native component where the
  * light weight component is resided.
  * @param c the lightweight component.
  * @return an absolute location of the component.
  */
  public static Point getAbsLocation(LwComponent c) {
    return getAbsLocation(0, 0, c);
  }

 /**
  * Returns the relative location for the specified absolute location relatively the light weight component.
  * @param x the x coordinate relatively a native where the component is resided.
  * @param y the y coordinate relatively a native where the component is resided.
  * @param c the lightweight component.
  * @return a relative location.
  */
  public static Point getRelLocation(int x, int y, LwComponent c) {
    return getRelLocation(x, y, getDesktop(c), c);
  }

 /**
  * Returns the relative location for the specified target location relatively the light weight component.
  * @param x the x coordinate relatively the target component where the component
  * is resided.
  * @param y the y coordinate relatively the target component where the component
  * is resided.
  * @param target the target lightweight component.
  * @param c the lightweight component.
  * @return a relative location.
  */
  public static Point getRelLocation(int x, int y, LwComponent target, LwComponent c)
  {
    while (c != target)
    {
      x -= c.getX();
      y -= c.getY();
      c = c.getLwParent();
    }
    return new Point(x, y);
  }

 /**
  * Calculates and gets the maximal preferred size among visible children of the specified
  * container.
  * @param target the container.
  * @return a maximal preferred size.
  */
  public static Dimension getMaxPreferredSize(LayoutContainer target)
  {
     int maxWidth = 0, maxHeight = 0;
     for (int i=0; i<target.count(); i++)
     {
       Layoutable l = target.get(i);
       if (l.isVisible())
       {
         Dimension ps = l.getPreferredSize();
         if (ps.width  > maxWidth ) maxWidth = ps.width;
         if (ps.height > maxHeight) maxHeight = ps.height;
       }
     }
     return new Dimension(maxWidth, maxHeight);
  }

 /**
  * Calculates and gets the origin for the specified area of the component. The origin is
  * calculated as a location of the component view to have the specified area inside
  * a visible part of the component.
  * @param x the x coordinate of the component area.
  * @param y the y coordinate of the component area.
  * @param w the width of the component area.
  * @param h the height of the component area.
  * @param target the component.
  * @return an origin of the component.
  */
  public static Point calcOrigin (int x, int y, int w, int h, LwComponent target) {
    Point origin = target.getOrigin();
    return calcOrigin (x, y, w, h, origin==null?0:origin.x, origin==null?0:origin.y, target);
  }

 /**
  * Calculates and gets the origin for the specified area of the component relatively the specified
  * previous origin. The origin is calculated as a location of the component view to have the
  * specified area inside a visible part of the component.
  * @param x the x coordinate of the component area.
  * @param y the y coordinate of the component area.
  * @param w the width of the component area.
  * @param h the height of the component area.
  * @param px the x coordinate of the previous origin.
  * @param py the y coordinate of the previous origin.
  * @param target the component.
  * @return an origin of the component.
  */
  public static Point calcOrigin (int x, int y, int w, int h, int px, int py, LwComponent target) {
    return calcOrigin (x, y, w, h, px, py, target, target.getInsets());
  }

 /**
  * Calculates and gets the origin for the specified area of the component relatively the specified
  * previous origin. The origin is calculated as a location of the component view to have the
  * specified area inside a visible part of the component.
  * @param x the x coordinate of the component area.
  * @param y the y coordinate of the component area.
  * @param w the width of the component area.
  * @param h the height of the component area.
  * @param px the x coordinate of the previous origin.
  * @param py the y coordinate of the previous origin.
  * @param target the component.
  * @param i the insets.
  * @return Fan origin of the component.
  */
  public static Point calcOrigin (int x, int y, int w, int h, int px, int py, LwComponent target, Insets i)
  {
     int dw = target.getWidth(), dh = target.getHeight();
     if (dw > 0 && dh > 0)
     {
       if (dw - i.left - i.right >= w)
       {
         int xx = x + px;
         if (xx < i.left) px += (i.left - xx);
         else
         {
           xx += w;
           if (xx > dw - i.right) px -= (xx - dw + i.right);
         }
       }

       if (dh - i.top - i.bottom >= h)
       {
         int yy = y + py;
         if (yy < i.top) py += (i.top - yy);
         else
         {
           yy += h;
           if (yy > dh - i.bottom) py -= (yy - dh + i.bottom);
         }
       }
       return new Point(px, py);
     }
     else return new Point(0, 0);
  }

 /**
  * Gets the graphics context for the specified lightweight component.
  * @param c the lightweight component.
  * @return a graphics context.
  */
  public static Graphics getGraphics(LwComponent c) {
    return getGraphics(c, 0, 0, c.getWidth(), c.getHeight());
  }

 /**
  * Gets the graphics context for the specified area of the lightweight component.
  * The method calculates clip area as intersecation the area bounds and a visible
  * part of the component.
  * @param c the lightweight component.
  * @param x the x coordinate of the component area.
  * @param y the y coordinate of the component area.
  * @param w the width of the component area.
  * @param h the height of the component area.
  * @return a graphics context.
  */
  public static Graphics getGraphics(LwComponent c, int x, int y, int w, int h)
  {
    LwDesktop nc = getDesktop(c);
    if (nc != null)
    {
       Rectangle vp = c.getVisiblePart();
       if (vp != null)
       {
         Graphics gr = nc.getGraphics();
         if (gr != null)
         {
           Point l = getAbsLocation(c);
           gr.clipRect(vp.x + l.x + x, vp.y + l.y + y, vp.width , vp.height);
           gr.translate(l.x, l.y);
           return gr;
         }
       }
    }
    return null;
  }

 /**
  * Gets the current paint manager.
  * @return a paint manager.
  */
  public final static LwPaintManager getPaintManager() {
    return paintManager;
  }

 /**
  * Gets the current focus manager.
  * @return a focus manager.
  */
  public final static LwFocusManager getFocusManager() {
    return (LwFocusManager)getStaticObj("focus");
  }

 /**
  * Gets the current event manager.
  * @return an event manager.
  */
  public final static LwEventManager getEventManager() {
    return eventManager;
  }

 /**
  * Starts LwToolkit. The method performs all necessary operations to prepare data, resources, properties and
  * so on to be ready for working with the library. The given base directory defines the root path
  * relative to that the LwVCL resources are stored.
  * @param base the base directory.
  */
  public synchronized static void startVCL (String sbase)
  {
    if (staticObjects == null)
    {
      try {
        if (SRC_CL == null) SRC_CL = Class.forName("org.zaval.lw.LwLabel");

        LwToolkit.base = (sbase == null)?"rs/":sbase;
        Properties props = getProperties("lw.properties");

        version = props.getProperty("lwvcl.ver");
        LwRsLoader rsLoader = (LwRsLoader)Class.forName(props.getProperty("rs.loader")).newInstance();
        staticObjects = new Hashtable();
        rsLoader.loadResources(staticObjects);
        managers = rsLoader.getManagers();
        FONT_METRICS = new FontMetrics((Font)getStaticObj("def.font"));

        eventManager = (LwEventManager)managers.elementAt(0);
        for (int i=0; i < managers.size(); i++)
        {
           LwManager m = (LwManager)managers.elementAt(i);
           if (m instanceof EventListener)
             eventManager.addXXXListener((EventListener)m);
        }
        paintManager = (LwPaintManager)getStaticObj("paint");
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

 /**
  * Stops LwToolkit. The method performs all necessary operations to destroy data and free system resources,
  * since the LwVCL is stopped.
  */
  public synchronized static void stopVCL()
  {
    if (staticObjects != null)
    {
      for (int i=0; i<managers.size(); i++)
        ((LwManager)managers.elementAt(i)).dispose();
      managers.removeAllElements();

      staticObjects.clear();
      staticObjects = null;
    }
  }

 /**
  * Calculates and returns location of the specified rectangular area that
  * is adjusted inside the given rectangular area with the specified horizontal
  * and vertical alignments.
  * @param alignObj the specified rectangular area to be adjusted.
  * @param alignX the specified horizontal alignment.
  * @param alignY the specified vertical alignment.
  * @param aw the specified align area width.
  * @param ah the specified align area height.
  * @return a location. The method throws <code>IllegalArgumentException</code> if
  * the horizontal or vertical alignment is undefined.
  */
  public static Point getLocation (Dimension alignObj, int alignX, int alignY, int aw, int ah)
  {
     Point r = new Point(0, 0);

     if (alignX == LEFT ) r.x = 0;
     else
     if (alignX == RIGHT) r.x = aw - alignObj.width;
     else
     if (alignX == CENTER) r.x = (aw - alignObj.width)/2;
     else
     if (alignX != NONE) throw new IllegalArgumentException();

     if (alignY == TOP ) r.y = 0;
     else
     if (alignY == BOTTOM) r.y = ah - alignObj.height;
     else
     if (alignY == CENTER) r.y = (ah - alignObj.height)/2;
     else
     if (alignY != NONE) throw new IllegalArgumentException();
     return r;
  }

  public static final int SHIFT_MASK = 1;
  public static final int CTRL_MASK  = 2;
  public static final int ALT_MASK   = 4;

  public static final int BUTTON1_MASK = 8;
  public static final int BUTTON3_MASK = 32;

  public static final int VK_ENTER       = -500000;
  public static final int VK_TAB         = -500000;
  public static final int VK_LEFT        = -500002;
  public static final int VK_UP          = -500003;
  public static final int VK_RIGHT       = -500004;
  public static final int VK_DOWN        = -500005;

  public static final int VK_BACK_SPACE  = -2000;
  public static final int VK_CANCEL      = -3000;
  public static final int VK_CLEAR       = -8;
  public static final int VK_SHIFT       = -5000;
  public static final int VK_CONTROL     = -6000;
  public static final int VK_ALT         = -7000;
  public static final int VK_PAUSE       = -8000;
  public static final int VK_CAPS_LOCK   = -9000;
  public static final int VK_ESCAPE      = -10000;
  public static final int VK_SPACE       = -11000;
  public static final int VK_PAGE_UP     = -12000;
  public static final int VK_PAGE_DOWN   = -13000;
  public static final int VK_END         = -14000;
  public static final int VK_HOME        = -15000;

  public static final int VK_DELETE      = -16000;
  public static final int VK_INSERT      = -17000;
}




