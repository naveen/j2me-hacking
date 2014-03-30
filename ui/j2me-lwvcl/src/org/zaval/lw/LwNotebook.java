package org.zaval.lw;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This is notebook container. The main features of the container are listed below:
 * <ul>
 *   <li>
 *     The page tabs can be laid out using  "right", "left", "top" or "bottom" alignments.
 *     The default tabs alignment is "top".
 *   </li>
 *   <li>
 *     It is possible to use any views or string title as tabs views. The appropriate tab view
 *     can be passed as an argument of the <code>addPage</code> method.
 *   </li>
 *   <li>
 *     The <code>add</code>, <code>insert</code>, <code>remove</code> methods can be used to change
 *     the notebook pages content.
 *   </li>
 *   <li>
 *     The <code>enablePage</code>, <code>isPageEnabled</code> methods can be used to change
 *     enabled state of the specified page.
 *   </li>
 *   <li>
 *     The container implements and uses own layout manager, so it is undesirable
 *     to use any other layout manager for the notebook container.
 *   </li>
 *   <li>
 *     To listen when a page of the container has been selected use <code>addSelectionListener</code>
 *     and <code>removeSelectionListener</code> methods. The passed data argument is selected tab
 *     index.
 *   </li>
 *   <li>
 *     It is possible to customize the tabs borders views for the selected and unselected tab states.
 *     Use the <code>setView</code> method for this purpose.
 *   </li>
 *   <li>
 *     It is possible to customize the tabs borders marker view.
 *     Use the <code>setView</code> method for this purpose.
 *   </li>
 * </ul>
 * <p>
 * The sample below illustrates the notebook container usage:
 * <pre>
 *    ...
 *    LwNotebook n = new LwNotebook();
 *    n.addPage("Tab 1", new LwPanel());
 *    n.addPage("Tab 2", new LwPanel());
 *    n.addPage("Tab 3", new LwPanel());
 *
 *    // Add the image as the tab view
 *    n.addPage(new LwImgRender("/imgs/tab.gif", LwView.ORIGINAL), new LwPanel());
 *
 *    ...
 * </pre>
 */
public class LwNotebook
extends LwPanel
implements LwMouseListener, LwKeyListener,
           LwLayout, LwTitleInfo, LwFocusListener
{
   /**
    * The tab border view id.
    */
    public static final int TABBR_VIEW  = 0;

   /**
    * The selected tab border view id.
    */
    public static final int STABBR_VIEW = 1;

    private Vector          pages  = new Vector();
    private LwView[]        views = new LwView[2];
    private LwTextRender    textRender;
    private int             selectedIndex = -1, selectedTabIndent = 1, tabBorderIndent = 1, orient, vgap, hgap;
    private int             tabAreaX, tabAreaY, tabAreaWidth, tabAreaHeight, hTabGap = 1, vTabGap = 1;

   /**
    * Constructs a notebook container using default (LwToolkit.TOP) tabs alignment.
    */
    public LwNotebook() {
      this(LwToolkit.TOP);
    }

   /**
    * Constructs a notebook container using the specified tabs alignment.
    * @param o the specified tabs alignement.
    */
    public LwNotebook(int o)
    {
      if (o != LwToolkit.TOP &&
          o != LwToolkit.BOTTOM &&
          o != LwToolkit.LEFT &&
          o != LwToolkit.RIGHT)  throw new IllegalArgumentException();

      orient      = o;
      textRender  = new LwTextRender("");
      setView(TABBR_VIEW, new LwTabBrView(o));
      getViewMan(true).setBorder(LwToolkit.getView("nb.br"));
    }

   /**
    * Sets the specified view for the given element type.
    * @param type the given element type. Use the TABBR_VIEW (tab border view),
    * STABBR_VIEW (selected tab border) constants as the argument value.
    * @param t the specified view.
    */
    public void setView (int type, LwView t)
    {
      if (views[type] != t)
      {
        views[type] = t;
        vrp();
      }
    }

   /**
    * Returns the view of the given element type.
    * @param type the given element type. Use the TABBR_VIEW (tab border view),
    * STABBR_VIEW (selected tab border) constants as the argument value.
    * @return an element view.
    */
    public LwView getView (int type) {
      return views[type];
    }

   /**
    * Sets the tab metrics.
    * @param vg the vertical gap between the tab border and the tab view.
    * @param hg the horizontal gap between the tab border and the tab view.
    * @param ea the selected tab extra indent. The indent is used to extend selected tab size.
    * @param bi the border indent.
    */
    public void setTabIndents (int vg, int hg, int ea, int bi)
    {
      if (vTabGap != vg || hg !=  hTabGap || selectedTabIndent != ea || tabBorderIndent != bi)
      {
        vTabGap = vg;
        hTabGap = hg;
        selectedTabIndent = ea;
        tabBorderIndent   = bi;
        vrp();
      }
    }

   /**
    * Returns the selected tab extra indent.
    * @return a selected tab extra indent.
    */
    public int getSelTabIndent() {
      return selectedTabIndent;
    }

   /**
    * Returns the tab border indent.
    * @return a tab border indent.
    */
    public int getTabBorderIndent() {
      return tabBorderIndent;
    }

   /**
    * Returns the tab vertical gap.
    * @return a vertical gap.
    */
    public int getTabVGap () {
      return vTabGap;
    }

   /**
    * Returns the tab horizontal gap.
    * @return a horizontal gap.
    */
    public int getTabHGap () {
      return hTabGap;
    }

   /**
    * Sets the notebook layout gaps. The gaps are used as the vertical and horizontal indents to
    * place content components.
    * @param vg the vertical layout gap.
    * @param hg the horizontal layout gap.
    */
    public void setGaps (int vg, int hg)
    {
      if (vgap != vg || hg != hgap)
      {
        vgap = vg;
        hgap = hg;
        vrp();
      }
    }

    public /*C#override*/ boolean canHaveFocus() {
      return true;
    }

   /**
    * Gets the tabs alignment.
    * @return a tabs alignment.
    */
    public int getTitleAlignment() {
      return orient;
    }

   /**
    * Tests if the specified page is enabled or not.
    * @param index the specified page index.
    * @return <code>true</code> if the page is enaled;otherwise <code>false</code>.
    */
    public boolean isPageEnabled (int index) {
      return ((LwComponent)get(index)).isEnabled();
    }

   /**
    * Sets the given enabled state for the specified page.
    * @param index the specified page index.
    * @param b the given enabled state.
    */
    public void enablePage (int index, boolean b)
    {
      LwComponent c = (LwComponent)get(index);
      if (c.isEnabled() != b)
      {
        c.setEnabled(b);
        if (!b && selectedIndex == index) select(-1);
        repaint();
      }
    }

   /**
    * Adds the page with the specified title.
    * @param title the specified tab title. It is possible to use a string or a view
    * as the title value.
    * @param c the specified page component.
    */
    public void addPage (Object title, LwComponent c)
    {
      pages.addElement(title);
      pages.addElement(new Rectangle());
      add (c);
      if (selectedIndex < 0) select(next(0, 1));
    }

   /**
    * Sets the specified page title.
    * @param pageIndex the specified page index.
    * @param data the specified tab title. It is possible to use a string or a view
    * as the title value.
    */
    public void setTitle (int pageIndex, Object data)
    {
      if (!pages.elementAt(2*pageIndex).equals(data))
      {
        pages.setElementAt(data, pageIndex*2);
        vrp();
      }
    }

    public /*C#override*/ void remove(int i)
    {
      if (selectedIndex == i) select(-1);
      pages.removeElementAt(i*2 + 1);
      pages.removeElementAt(i*2);
      super.remove(i);
    }

    public /*C#override*/ void removeAll()
    {
      if (selectedIndex >= 0) select(-1);
      pages.removeAllElements();
      super.removeAll();
    }

   /**
    * The method is overridden by the component for internal usage. Don't tuch the method.
    */
    protected /*C#override*/ void recalc ()
    {
      int count = pages.size()/2;
      if (count > 0)
      {
        tabAreaHeight = 0;
        tabAreaWidth  = 0;

        Insets  ti   = getInsets();
        boolean b    = (orient == LwToolkit.LEFT || orient == LwToolkit.RIGHT);
        int     hadd = views[TABBR_VIEW].getLeft() + views[TABBR_VIEW].getRight() +  2*hTabGap;
        int     vadd = views[TABBR_VIEW].getTop()  + views[TABBR_VIEW].getBottom() + 2*vTabGap;
        int     x    = ti.left + ((orient == LwToolkit.RIGHT)?0:selectedTabIndent);
        int     y    = ti.top  + ((orient == LwToolkit.BOTTOM)?0:selectedTabIndent);
        int     max = 0;

        for (int i=0; i<count; i++)
        {
          Dimension ps = getTabView(i).getPreferredSize();
          Rectangle r  = getTabBounds(i);
          if (b)
          {
            r.height = ps.height + vadd;
            r.y = y;
            y += r.height;
            if (ps.width + hadd > max) max = ps.width + hadd;
            tabAreaHeight += r.height;
          }
          else
          {
            r.width = ps.width + hadd;
            r.x = x;
            x += r.width;
            if (ps.height + vadd > max) max = ps.height + vadd;
            tabAreaWidth += r.width;
          }
        }

        for (int i=0; i < count; i++)
        {
          Rectangle r = getTabBounds(i);
          if (b) {
            r.width  = max;
          }
          else {
            r.height = max;
          }
        }

        if (b) {
          tabAreaWidth   = (max + selectedTabIndent + tabBorderIndent);
          tabAreaHeight += (2*selectedTabIndent);
        }
        else {
          tabAreaWidth  += (2*selectedTabIndent);
          tabAreaHeight  = (max + selectedTabIndent + tabBorderIndent);
        }

        if (selectedIndex >= 0)
        {
          Rectangle r = getTabBounds(selectedIndex);
          r.x -= (orient == LwToolkit.RIGHT)?tabBorderIndent:selectedTabIndent;
          r.y -= (orient == LwToolkit.BOTTOM)?tabBorderIndent:selectedTabIndent;
          r.width  += b?tabBorderIndent + selectedTabIndent:2*selectedTabIndent;
          r.height += b?2*selectedTabIndent:tabBorderIndent + selectedTabIndent;
        }
        tabAreaX = -1;
      }
    }

   /**
    * Gets the tab index of the selected tab.
    * @return a tab index.
    */
    public int getSelectedIndex() {
      return selectedIndex;
    }

   /**
    * Paints this component.
    * @param g the graphics context to be used for painting.
    */
    public /*C#override*/ void paint(Graphics g)
    {
      int cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
      if (selectedIndex >= 0)
      {
        Rectangle r = getTabBounds(selectedIndex);
        if (orient == LwToolkit.LEFT || orient == LwToolkit.RIGHT)
          g.clipRect(r.x, tabAreaY, r.width, r.y - tabAreaY);
        else
          g.clipRect(tabAreaX, r.y, r.x - tabAreaX, r.height);
      }

      for (int i=0; i<selectedIndex; i++)
        paintTab(g, i);

      if (selectedIndex >= 0)
      {
        g.setClip(cx, cy, cw, ch);
        Rectangle r = getTabBounds(selectedIndex);
        if (orient == LwToolkit.LEFT || orient == LwToolkit.RIGHT) g.clipRect(r.x, r.y + r.height, r.width, height - r.y - r.height);
        else                                                       g.clipRect(r.x + r.width, r.y, width - r.x - r.width, r.height);
      }

      for (int i=selectedIndex+1; i<pages.size()/2; i++)
        paintTab(g, i);

      if (cw > 0 && ch > 0) g.setClip(cx, cy, cw, ch);

      if (selectedIndex >= 0)
      {
        paintTab(g, selectedIndex);
        if (hasFocus()) drawMarker(g, getTabBounds(selectedIndex));
      }
   }

   public void keyPressed (LwKeyEvent e)
   {
     if (selectedIndex != -1 && pages.size() > 0)
     {
       switch (e.getKeyCode())
       {
         case LwToolkit.VK_UP   :
         case LwToolkit.VK_LEFT :
         {
           int nxt = next (selectedIndex-1, -1);
           if (nxt >= 0) select(nxt);
         }  break;
         case LwToolkit.VK_DOWN :
         case LwToolkit.VK_RIGHT:
         {
           int nxt = next (selectedIndex+1, 1);
           if (nxt >= 0) select(nxt);
         } break;
       }
     }
   }

   public void mousePressed (LwMouseEvent e)
   {
     if (LwToolkit.isActionMask(e.getMask()))
     {
       int index = getTabAt(e.getX(), e.getY());
       if (index >= 0 && isPageEnabled(index)) select(index);
     }
   }

   public void keyReleased  (LwKeyEvent e)   {}
   public void keyTyped     (LwKeyEvent e)   {}
   public void mouseEntered (LwMouseEvent e) {}
   public void mouseExited  (LwMouseEvent e) {}
   public void mouseClicked (LwMouseEvent e) {}
   public void mouseReleased(LwMouseEvent e) {}

   public void  componentAdded   (Object id, Layoutable lw, int index) {}
   public void  componentRemoved (Layoutable lw, int index) {}

   public Dimension calcPreferredSize(LayoutContainer target)
   {
     Dimension max = LwToolkit.getMaxPreferredSize(target);
     if (orient == LwToolkit.BOTTOM || orient == LwToolkit.TOP)
     {
       max.width  = Math.max (2*selectedTabIndent + max.width, tabAreaWidth);
       max.height += tabAreaHeight;
     }
     else
     {
       max.width  += tabAreaWidth;
       max.height = Math.max (2*selectedTabIndent + max.height, tabAreaHeight);
     }
     max.width  += (hgap*2);
     max.height += (vgap*2);
     return max;
   }

   public void layout(LayoutContainer target)
   {
     Insets ti = getInsets();
     boolean b = (orient == LwToolkit.TOP || orient == LwToolkit.BOTTOM);

     if (tabAreaX == -1)
     {
       if (b) {
         tabAreaX = ti.left;
         tabAreaY = (orient == LwToolkit.TOP)?ti.top:height - ti.bottom - tabAreaHeight;
       }
       else {
         tabAreaX = (orient == LwToolkit.LEFT)?ti.left:width - ti.right - tabAreaWidth;
         tabAreaY = ti.top;
       }

       for (int i=0; i < pages.size()/2; i++)
       {
         Rectangle r = getTabBounds(i);
         if (b) r.y = tabAreaY + ((orient == LwToolkit.BOTTOM)?tabBorderIndent:selectedTabIndent);
         else   r.x = tabAreaX + ((orient == LwToolkit.RIGHT)?tabBorderIndent:selectedTabIndent);
       }
     }

     for (int i=0; i<count(); i++)
     {
       Layoutable l = get(i);
       if (isSelected(i))
       {
         if (b)
           l.setSize(width  - ti.left - ti.right - 2*hgap,
                     height - tabAreaHeight - ti.top - ti.bottom - 2*vgap);
         else
           l.setSize(width  - tabAreaWidth - ti.left - ti.right - 2*hgap,
                     height - ti.top - ti.bottom - 2*vgap);

         l.setLocation (hgap + ((b || orient == LwToolkit.RIGHT)?ti.left:tabAreaWidth + ti.left),
                        vgap + ((orient == LwToolkit.RIGHT || orient == LwToolkit.BOTTOM || orient == LwToolkit.LEFT)?ti.top:tabAreaHeight + ti.top));
       }
       else l.setSize(0, 0);
     }
   }


  /**
   * Selects the tab by the specified index. Use <code>-1</code> index value to de-select
   * current selected tab, in this case no one tab will be selected.
   * @param index the specified item index.
   */
   public void select(int index)
   {
     if (selectedIndex != index)
     {
       selectedIndex = index;
       vrp();
     }
   }

  /**
   * Tests if the tab with the specified index is selected or not.
   * @return <code>true</code> if the tab with the specified index is selected; otherwise
   * <code>false</code>.
   */
   public boolean isSelected(int i) {
     return i == selectedIndex;
   }

   public Rectangle getTitleBounds()
   {
     boolean   b   = (orient == LwToolkit.LEFT || orient == LwToolkit.RIGHT);
     Rectangle res = b?new Rectangle (tabAreaX, 0, tabAreaWidth, 0)
                      :new Rectangle (0, tabAreaY, 0, tabAreaHeight);

     if (selectedIndex >= 0)
     {
       Rectangle r = getTabBounds(selectedIndex);
       if (b)
       {
         res.y      = r.y;
         res.height = r.height;
       }
       else
       {
         res.x     = r.x;
         res.width = r.width;
       }
     }
     return res;
   }

   public void focusGained(LwFocusEvent e) {
     if (selectedIndex < 0) select(next(0, 1));
   }

   public void focusLost(LwFocusEvent e) {}

  /**
   * Gets the tab index that is located at the given location.
   * @param x the specified x coordinate.
   * @param y the specified y coordinate.
   * @return a tab index. The method returns <code>-1</code> if no one tabs can be found at the specified
   * location.
   */
   public int getTabAt(int x, int y)
   {
     validate();
     if (x >= tabAreaX &&
         y >= tabAreaY &&
         x < tabAreaX + tabAreaWidth &&
         y < tabAreaY + tabAreaHeight)
     {
       for (int i=0; i<pages.size()/2; i++)
         if (getTabBounds(i).contains(x, y)) return i;
     }
     return -1;
   }

    public /*C#override*/ void setSize (int w, int h)
    {
      if (width != w || height != h)
      {
        if (orient == LwToolkit.RIGHT || orient == LwToolkit.BOTTOM) tabAreaX = -1;
        super.setSize(w, h);
      }
    }

   /**
    * Gets the default layout manager that is set with the container during initialization.
    * The component defines itself as the default layout. Don't use any other layout for
    * the component, in this case the working of the component can be wrong.
    * @return a layout manager.
    */
    protected /*C#override*/ LwLayout getDefaultLayout() {
      return this;
    }

   /**
    * Gets the view for the specified tab.
    * @param index the specified tab index.
    * @return a tab view.
    */
    protected /*C#virtual*/ LwView getTabView (int index)
    {
      Object data = pages.elementAt(2*index);
      if (data instanceof LwView) return (LwView)data;
      else
      {
        textRender.getTextModel().setText((String)data);
        return textRender;
      }
    }

   /**
    * Renders the marker for the specified tab bounds using the given graphical context.
    * @param g the specified context to be used for painting.
    * @param r the specified tab bounds.
    */
    protected /*C#virtual*/ void drawMarker(Graphics g, Rectangle r)
    {
      LwToolkit.drawDotRect (g, r.x + views[TABBR_VIEW].getLeft(),
                                r.y + views[TABBR_VIEW].getTop(),
                                r.width  - views[TABBR_VIEW].getLeft() - views[TABBR_VIEW].getRight(),
                                r.height - views[TABBR_VIEW].getTop()  - views[TABBR_VIEW].getBottom());
    }

    protected /*C#override*/ int calcInsets(int type, int ci, int bi) {
      return type == orient?0:bi;
    }

   /**
    * Renders the given tab using the given graphical context.
    * @param g the specified context to be used for painting.
    * @param pageIndex the specified tab index.
    */
    protected /*C#virtual*/ void paintTab (Graphics g, int pageIndex)
    {
      Rectangle  b    = getTabBounds(pageIndex);
      Layoutable page = get(pageIndex);
      if (getSelectedIndex() == pageIndex && views[STABBR_VIEW] != null)
        views[STABBR_VIEW].paint (g, b.x, b.y, b.width, b.height, page);
      else
        views[TABBR_VIEW].paint (g, b.x, b.y, b.width, b.height, page);

      LwView v = getTabView (pageIndex);
      Dimension ps = v.getPreferredSize();
      Point     p  = LwToolkit.getLocation (ps, LwToolkit.CENTER, LwToolkit.CENTER,
                                            b.width, b.height);
      v.paint (g, b.x + p.x, b.y + p.y, ps.width, ps.height, page);
      if (getSelectedIndex() == pageIndex) v.paint (g, b.x + p.x + 1, b.y + p.y, ps.width, ps.height, page);
    }

   /**
    * Gets the tab bounds. The returned bounds is not a copy, it means you should not
    * change its fields since it can bring to fault.
    * @param index the specified tab index.
    * @return a tab bounds.
    */
    protected /*C#virtual*/ Rectangle getTabBounds(int index) {
      return (Rectangle)pages.elementAt(2*index + 1);
    }

    private int next (int page, int d)
    {
      for (;page>=0 && page < pages.size()/2; page+=d)
        if (isPageEnabled(page)) return page;
      return -1;
    }
}

