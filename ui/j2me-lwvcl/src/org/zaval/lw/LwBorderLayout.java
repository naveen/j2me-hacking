package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class implements the layout manager interface. The layout divides container area
 * into five parts: <code>North</code>, <code>South</code>, <code>East</code>,
 * <code>West</code>, and <code>Center</code>.  To add a component to a container
 * with the border layout, use one of the constraint constants of this layout manager
 * as follow:
 * <pre>
 *    ...
 *    LwContainer c = new LwPanel();
 *    c.setLwLayout(new LwBorderLayout());
 *    c.add(LwBorderLayout.SOUTH, new LwButton("South"));
 *    ...
 * </pre>
 * The image below shows how the five buttons have been laid out with the border layout
 * manager inside the container: <br>
 * <img src="images/BorderLayoutApp.gif">
 * <br><br>
 * Actually the lightweight border layout manager is the same org.zaval.port.j2me.BorderLayout.
 */
public class LwBorderLayout
implements LwLayout
{
   /**
    * The north layout constraint (top of container).
    */
    public static final Object NORTH  = new Integer(1);

   /**
    * The south layout constraint (bottom of container).
    */
    public static final Object SOUTH  = new Integer(2);

   /**
    * The east layout constraint (left side of container).
    */
    public static final Object EAST   = new Integer(3);

   /**
    * The west layout constraint (right side of container).
    */
    public static final Object WEST   = new Integer(4);

   /**
    * The center layout constraint (middle of container).
    */
    public static final Object CENTER = new Integer(5);

    private int        hgap, vgap;
    private Layoutable north, west, east, south, center;

   /**
    * Constructs a new border layout with no gaps between components.
    */
    public LwBorderLayout() {
      this(0, 0);
    }

   /**
    * Constructs a border layout with the specified gaps between components.
    * The horizontal gap is specified by <code>hgap</code> and the vertical gap is
    * specified by <code>vgap</code>.
    * @param hgap the horizontal gap.
    * @param vgap the vertical gap.
    */
    public LwBorderLayout(int hgap, int vgap) {
      this.hgap = hgap;
      this.vgap = vgap;
    }

   /**
    * Returns the horizontal.
    * @return a horizontal gap.
    */
    public int getHgap() {
      return hgap;
    }

   /**
    * Sets the horizontal gap.
    * @param hgap the horizontal gap.
    */
    public void setHgap(int hgap) {
      this.hgap = hgap;
    }

   /**
    * Returns the vertical gap.
    * @return a vertical gap.
    */
    public int getVgap() {
      return vgap;
    }

   /**
    * Sets the vertical gap.
    * @param vgap the vertical gap.
    */
    public void setVgap(int vgap) {
      this.vgap = vgap;
    }

   /**
    * Invoked when the specified layoutable component is added to the layout container, that
    * uses the layout manager. The specified constraints, layoutable component and child index
    * are passed as arguments into the method. For the border layout manager, the constraints must
    * be equal one of the manager constants, otherwise IllegalArgumentException will be performed.
    * @param id the layoutable component constraints.
    * @param comp the layoutable component.
    * @param index the child index.
    */
    public void componentAdded(Object id, Layoutable comp, int index)
    {
      if (CENTER.equals(id)) center = comp;
      else
      if (NORTH.equals(id)) north = comp;
      else
      if (SOUTH.equals(id)) south = comp;
      else
      if (EAST.equals(id)) east = comp;
      else
      if (WEST.equals(id)) west = comp;
      else                 throw new IllegalArgumentException();
    }

   /**
    * Invoked when the specified layoutable component is removed from the layout
    * container, that uses the layout manager.
    * @param lw the layoutable component to be removed
    * @param index the child index.
    */
    public void componentRemoved (Layoutable lw, int index)
    {
      if (lw == center) center = null;
      else
      if (lw == north)  north = null;
      else
      if (lw == south) south = null;
      else
      if (lw == east)  east = null;
      else
      if (lw == west)  west = null;
    }

   /**
    * Calculates the preferred size dimension for the layout container.
    * The method calculates "pure" preferred size, it means that an insets
    * of the container is not considered as a part of the preferred size.
    * @param target the layout container.
    */
    public Dimension calcPreferredSize(LayoutContainer target)
    {
      Dimension dim = new Dimension();

      if (isVisible(east))
      {
        Dimension d = east.getPreferredSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }

      if (isVisible(west))
      {
        Dimension d = west.getPreferredSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }

      if (isVisible(center))
      {
        Dimension d = center.getPreferredSize();
        dim.width += d.width;
        dim.height = Math.max(d.height, dim.height);
      }

      if (isVisible(north))
      {
        Dimension d = north.getPreferredSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }

      if (isVisible(south))
      {
        Dimension d = south.getPreferredSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }
      return dim;
    }

   /**
    * Lays out the child layoutable components inside the specified layout container.
    * @param target the layout container that needs to be laid out.
    */
    public void layout(LayoutContainer target)
    {
      Insets  insets = target.getInsets();
      Point   offs   = target.getLayoutOffset();
      int top = insets.top + offs.y;
      int bottom = target.getHeight() - insets.bottom;
      int left = insets.left + offs.x;
      int right = target.getWidth() - insets.right;

      if (isVisible(north))
      {
          Dimension d = north.getPreferredSize();
          north.setLocation(left, top);
          north.setSize(right - left, d.height);
          top += d.height + vgap;
      }

      if (isVisible(south))
      {
          Dimension d = south.getPreferredSize();
          south.setLocation(left, bottom - d.height);
          south.setSize(right - left, d.height);
          bottom -= d.height + vgap;
      }

      if (isVisible(east))
      {
          Dimension d = east.getPreferredSize();
          east.setLocation(right - d.width, top);
          east.setSize(d.width, bottom - top);
          right -= d.width + hgap;
      }

      if (isVisible(west))
      {
          Dimension d = west.getPreferredSize();
          west.setLocation(left, top);
          west.setSize(d.width, bottom - top);
          left += d.width + hgap;
      }

      if (isVisible(center))
      {
          center.setLocation(left, top);
          center.setSize    (right - left, bottom - top);
      }
    }

   /**
    * Tests if the specified layout container is not <code>null</code> and visible.
    * @param l the specified layout container.
    * @return <code>true</code> if the layoutable component is not null and visible;
    * otherwise <code>false</code>
    */
    protected static boolean isVisible (Layoutable l) {
      return l != null && l.isVisible();
    }
}




