package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is lightweight component that can be used to organize border panel.
 * The panel can use any other component as a title and the title
 * can be placed on the top or on the bottom line of the container. The border panel
 * supports following alignments for the title: "left", "center", "right". The border panel
 * implements and uses own layout manager, so to add a component to the container it is
 * necessary to use one of the following constraints:
 * <ul>
 *   <li>
 *     LwBorderPan.CENTER. The constraint is used to add a central component.
 *   </li>
 *   <li>
 *     LwBorderPan.TITLE. The constraint is used to add a title component.
 *   </li>
 * </ul>
 * <p>
 * The table below shows different samples of the border panel usage:
 * <table border="1" width="100%" cellspacing="1">
 *   <tr class="tb3">
 *     <td align="center"> <b>Source code </b> </td>
 *     <td align="center"> <b>Application image </b> </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       <pre>
 *         ...
 *         LwBorderPan bp = new LwBorderPan();
 *         bp.setXAlignment(LwToolkit.CENTER);
 *         bp.add(LwBorderPan.TITLE,  new LwLabel("Title"));
 *         bp.add(LwBorderPan.CENTER, new LwLabel("Center"));
 *         ...
 *       </pre>
 *     </td>
 *     <td align="center">
 *        <img src="images/BorderPanelApp1.gif">
 *     </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       <pre>
 *         ...
 *         LwBorderPan bp = new LwBorderPan();
 *         bp.setXAlignment(LwToolkit.LEFT);
 *         bp.add(LwBorderPan.TITLE,  new LwLabel("Title"));
 *         bp.add(LwBorderPan.CENTER, new LwLabel("Center"));
 *         ...
 *       </pre>
 *     </td>
 *     <td align="center">
 *        <img src="images/BorderPanelApp2.gif">
 *     </td>
 *   </tr>
 *   <tr class="tb2">
 *     <td>
 *       <pre>
 *         ...
 *         LwBorderPan bp = new LwBorderPan();
 *         bp.setXAlignment(LwToolkit.RIGHT);
 *         bp.setTitleAlignment(LwToolkit.BOTTOM);
 *         bp.add(LwBorderPan.TITLE,  new LwLabel("Title"));
 *         bp.add(LwBorderPan.CENTER, new LwLabel("Center"));
 *         ...
 *       </pre>
 *     </td>
 *     <td align="center">
 *        <img src="images/BorderPanelApp3.gif">
 *     </td>
 *   </tr>
 * </table>
 */
public class LwBorderPan
extends LwPanel
implements LwLayout, LwTitleInfo
{
 /**
  * The center layout constraint (it is used to add a central component).
  */
  public static final Object CENTER = new Integer(1);

 /**
  * The title layout constraint (it is used to add a title component).
  */
  public static final Object TITLE  = new Integer(2);

  private Layoutable label;
  private Layoutable center;
  private int titleAlignment, xAlignment, vGap, hGap;

 /**
  * Constructs a new border panel.
  */
  public LwBorderPan() {
    this(null, null);
  }

 /**
  * Constructs a new border panel with the specified title component and center component.
  * In this case the title and the center components will be added to the border panel
  * automatically
  * @param title the specified title component.
  * @param center the specified central component.
  */
  public LwBorderPan(LwComponent title, LwComponent center)
  {
    getViewMan(true).setBorder(LwToolkit.getView("bpan.br"));
    setTitleAlignment(LwToolkit.TOP);
    setXAlignment(LwToolkit.LEFT);
    if (title  != null) add (TITLE, title);
    if (center != null) add (CENTER, center);
  }

 /**
  * Sets the border panel layout gaps. The gaps are used as the vertical and horizontal indents to
  * place a center component.
  * @param vg the vertical layout gap.
  * @param hg the horizontal layout gap.
  */
  public void setGaps (int vg, int hg)
  {
    if (vGap != vg || hg != hGap)
    {
      vGap = vg;
      hGap = hg;
      vrp();
    }
  }

 /**
  * The method is called if a component has been added to the owner
  * layoutable container. The implementation performs IllegalArgumentException
  * if the constraint is not defined or doesn't equal LwBorderPan.TITLE or
  * LwBorderPan.CENTER constant.
  * @param id the layoutable component constraints.
  * @param lw the layoutable component.
  * @param index the child index.
  */
  public void componentAdded (Object id, Layoutable lw, int index)
  {
    if (id.equals(TITLE)) label = lw;
    else
    if (id.equals(CENTER)) center = lw;
    else throw new IllegalArgumentException();
  }

 /**
  * The method is called if a component has been removed from the owner
  * layoutable container.
  * @param lw the layoutable component.
  * @param index the child index.
  */
  public void  componentRemoved (Layoutable lw, int index) {
    if (lw == label) label = null;
    else if (lw == center) center = null;
  }

 /**
  * The method computes a preferred size for the specified target component.
  * @param target the specified layoutable container.
  */
  public Dimension calcPreferredSize(LayoutContainer target)
  {
     Dimension ps = center != null && center.isVisible()?center.getPreferredSize():new Dimension();

     if (label != null && label.isVisible())
     {
       Dimension lps = label.getPreferredSize();
       lps.width += INDENT;
       ps.height += lps.height;
       ps.width   = Math.max(ps.width, lps.width);
     }
     ps.width  += (hGap*2);
     ps.height += (vGap*2);
     return ps;
  }

 /**
  * The method is an implementation of appropriate layout manager method.
  * The method performs layouting of the child layoutable components for the specified
  * target component.
  * @param target the specified layoutable container.
  */
  public void layout(LayoutContainer target)
  {
    Insets targetInsets = getInsets();

    int y = 0, x = 0, w = 0, h = 0;
    if (label != null && label.isVisible())
    {
      Dimension ps = label.getPreferredSize();
      y = (getTitleAlignment() == LwToolkit.BOTTOM)?(height - targetInsets.bottom - ps.height):targetInsets.top;
      x = (xAlignment == LwToolkit.LEFT)?targetInsets.left + INDENT
                                        :((xAlignment == LwToolkit.RIGHT)?width - targetInsets.right - ps.width - INDENT
                                                                         :(width - ps.width)/2);
      w = ps.width;
      h = ps.height;
      label.setSize(w, h);
      label.setLocation(x, y);
    }

    if (center != null && center.isVisible())
    {
      center.setLocation(targetInsets.left + hGap, (getTitleAlignment() == LwToolkit.BOTTOM?targetInsets.top
                                                                                           :targetInsets.top + h) + vGap);
      center.setSize(width  - targetInsets.right - targetInsets.left - 2*hGap,
                     height - targetInsets.top - targetInsets.bottom - h - 2*vGap);
    }
  }

 /**
  * Overrides parent method to define default layout. The component returns itself
  * as the default layout manager.
  * @return the default layout manager.
  */
  protected /*C#override*/ LwLayout getDefaultLayout() {
    return this;
  }

 /**
  * Gets the rectangle where the title component has been placed with the border panel.
  * @return a rectangle where the the title component has been placed.
  */
  public Rectangle getTitleBounds() {
    return (label != null)?label.getBounds():null;
  }

 /**
  * Sets the specified vertical alignment for the title of the border panel. The border panel
  * supports LwToolkit.TOP and LwToolkit.BOTTOM values for the alignment, otherwise
  * IllegalArgumentException will be thrown.
  * @param a the vertical alignment.
  */
  public void setTitleAlignment(int a)
  {
    if (a != LwToolkit.TOP && a != LwToolkit.BOTTOM) throw new IllegalArgumentException();
    if (a != titleAlignment)
    {
      titleAlignment = a;
      vrp();
    }
  }

 /**
  * Sets the specified horizontal alignment for the title of the border panel. The border panel
  * supports LwToolkit.LEFT, LwToolkit.RIGHT and LwToolkit.CENTER values for the alignment,
  * otherwise IllegalArgumentException will be thrown.
  * @param a the horizontal alignment.
  */
  public void setXAlignment(int a)
  {
    if (a != LwToolkit.LEFT && a != LwToolkit.RIGHT && a != LwToolkit.CENTER) throw new IllegalArgumentException();
    if (a != xAlignment)
    {
      xAlignment = a;
      vrp();
    }
  }

 /**
  * Gets the vertical alignment of the title component.
  * @return a vertical alignment.
  */
  public int getTitleAlignment() {
    return titleAlignment;
  }

  protected /*C#override*/ int calcInsets(int type, int ci, int bi) {
    return type == titleAlignment && label != null?0:bi;
  }

  private static final int INDENT = 4;
}


