package org.zaval.lw;

import java.util.*;
import org.zaval.util.*;
import org.zaval.lw.event.*;
import org.zaval.lw.*;
import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;

public class J2MEDesktop
extends LwPanel
implements LwDesktop, LwLayout
{
  private LwLayer   root;
  private NCanvas   ncanvas;
  private Rectangle da = new Rectangle (0,0,-1,-1);

 /**
  * Constructs the class instance.
  */
  public J2MEDesktop()
  {
    ncanvas = new NCanvas(this);
    //!!!
    ncanvas.enableRepaint = false;
    add (new LwBaseLayer("root"));
    root.setOpaque(true);
    //!!!
    ncanvas.enableRepaint = true;
  }

  public Rectangle getVisiblePart() {
    return isVisible()?new Rectangle(0, 0, getWidth(), getHeight()):null;
  }

  public LwComponent getLwComponentAt(int x, int y) {
    return LwToolkit.getEventManager().getEventDestination(root.getLwComponentAt(x, y));
  }

  public LwLayer getRootLayer () {
    return root;
  }

  public LwLayer getLayer (Object id) {
    return root;
  }

  public Object[] getLayersIDs () {
    return new String[] { "root" };
  }

  public Image createImage(int w, int h) {
    return Image.createImage(w, h);
  }

  public org.zaval.port.j2me.Graphics getGraphics () {
    return null;
  }

  public void componentAdded(Object id, Layoutable lw, int index)
  {
    if (root != null) throw new RuntimeException();
    root = (LwLayer)lw;
  }

  public void componentRemoved(Layoutable lw, int index) {
    root = null;
  }

  public Dimension calcPreferredSize(LayoutContainer target) {
    return new Dimension ();
  }

  public void layout(LayoutContainer target)
  {
    int w = getWidth(), h = getHeight();
    for (int i=0;i<target.count();i++)
      target.get(i).setSize(w, h);
  }

  public Object getProperty (int id) {
    return null;
  }

  public void setProperty (int id, Object value) {}

  public Rectangle getDA () {
    return da;
  }

  public Object getNCanvas () {
    return ncanvas;
  }

  public LwLayout getDefaultLayout() {
    return this;
  }

  public void repaint (int x, int y, int w, int h)
  {
    synchronized(LwPaintManager.LOCKER)
    {

      if (ncanvas.enableRepaint && w > 0 && h > 0) {
        ncanvas.enableRepaint = false;
        ncanvas.repaint(x, y, w, h);
//        System.out.println ("J2MEDesktop.repaint() : " + x + "," + y + "," + w + "," + h);
      }
    }
  }

  public void repaint ()
  {
    synchronized(LwPaintManager.LOCKER)
    {
      if (ncanvas.enableRepaint) {
        ncanvas.enableRepaint = false;
        ncanvas.repaint();

//        System.out.println ("J2MEDesktop.repaint() ALL " );

        //throw new RuntimeException("repaint()");
      }
    }
  }
}

