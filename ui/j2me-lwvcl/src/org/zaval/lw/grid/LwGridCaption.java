package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;

/**
 * This component is supposed to be used as the top or left grid caption component.
 * The list below describes basic features of the component:
 * <ul>
 *   <li>
 *     Use the <code>putTitle</code> method to set a caption title for an appropriate column or row.
 *     It is possible to specify a string or any view as the row or column title content. So it is easy
 *     to customize the caption view. For example if you need to have an image as the caption title
 *     content use the following code:
 *     <pre>
 *     ...
 *     LwGridCaption caption = new LwGridCaption(gridMetric);
 *     // set an image as the caption title content
 *     caption.putTitle (0, new LwImgRender("/imgs/captionImg.gif", LwView.ORIGINAL));
 *     ...
 *     </pre>
 *   </li>
 *   <li>
 *     Use the <code>setTitleProps</code> method to customize the caption title view alignments
 *     and the caption title background color.
 *   </li>
 *   <li>
 *     Use the <code>setBorderView</code> method to customize the caption border view.
 *   </li>
 *   <li>
 *     It is possible to change size of the grid columns or rows using
 *     mouse. Use the <code>setFlagState</code> method to allow or disable columns or rows
 *     resizing.
 *   </li>
 *   <li>
 *     It is possible to enable the grid columns or rows auto sizing according to its preferred sizes.
 *     The auto-sizing is initiated by the mouse double clicking inside the resize area. Use
 *     the <code>setFlagState</code> method for the purpose.
 *   </li>
 * </ul>
 */
public class LwGridCaption
extends LwCanvas
{
  private int                orient, psW, psH;
  private LwGridMetrics      metrics;
  private Object[]           titles;
  private LwView             borderView;
  private LwTextRender       render;

 /**
  * Constructs the object with the specified grid metrics.
  * @param m the specified grid metrics.
  */
  public LwGridCaption(LwGridMetrics m) {
    this(m, LwToolkit.HORIZONTAL);
  }

 /**
  * Constructs the object with the specified grid metrics and the given orientation.
  * @param m the specified grid metrics.
  * @param o the specified caption orientation. It is possible to use one of the
  * following constants as the argument value:
  * <ul>
  *  <li><b>LwToolkit.HORIZONTAL</b></li>
  *  <li><b>LwToolkit.VERTICAL</b></li>
  * </ul>
  */
  public LwGridCaption(LwGridMetrics m, int o)
  {
    if (o != LwToolkit.HORIZONTAL && o != LwToolkit.VERTICAL) throw new IllegalArgumentException();
    render   = new LwTextRender("");
    render.setFont((Font)LwToolkit.getStaticObj("def.bfont"));
    metrics  = m;
    orient   = o;
    setBorderView (LwToolkit.getView("br.raised"));
  }

 /**
  * Sets the specified view to render the caption border.
  * @param v the specified view .
  */
  public void setBorderView (LwView v)
  {
    if (v != borderView) {
      borderView = v;
      vrp();
    }
  }

 /**
  * Gets the title value for the specified column or row (depending on the orientation property).
  * @param rowcol the specified column or row.
  * @return a title value.
  */
  public Object getTitle(int rowcol) {
    return titles == null || titles.length/2 <= rowcol?null:titles[rowcol*2];
  }

 /**
  * Sets the given title value for the specified column or row (depending on the caption orientation).
  * It is possible to use <code>null</code> value as the title value to clear the title.
  * You can use a string or any view as the title content.
  * @param rowcol the specified column or row.
  * @param title the specified title value. Use a string value or any preferred view as the
  * title content.
  */
  public void putTitle (int rowcol, Object title)
  {
    Object old = getTitle (rowcol);
    if (old != title || (title != null && !title.equals(old)))
    {
      if (titles == null) titles = new Object[(rowcol + 1)*2];
      else
      {
        if (titles.length/2 <= rowcol)
        {
          Object[] nt = new Object[(rowcol + 1)*2];
          System.arraycopy(titles, 0, nt, 0, titles.length);
          titles = nt;
        }
      }
      int index = rowcol*2;
      titles[index] = title;
      if (title == null && index + 2 == titles.length)
      {
        if (index + 2 == titles.length)
        {
          Object[] nt = new Object[titles.length - 2];
          System.arraycopy(titles, 0, nt, 0, index);
          titles = nt;
        }
      }
      vrp();
    }
  }

 /**
  * Sets the specified title properties. The method allows setting the title vertical and horizontal
  * alignments and the background color.
  * @param rowcol the specified column or row.
  * @param ax the specified horizontal alignment.
  * @param ay the specified vertical alignment.
  * @param bg the specified background color.
  */
  public void setTitleProps (int rowcol, int ax, int ay, Color bg)
  {
    int[] p = getTitleProps(rowcol);
    if (p == null) p = new int[3];
    p[0] = ax;
    p[1] = ay;
    p[2] = bg == null?0:bg.getRGB();
    titles[rowcol*2 + 1] = p;
    repaint();
  }

  public /*C#override*/ void paint (Graphics g)
  {
     CellsVisibility cv = metrics.getCellsVisibility();

     boolean   isHor = (orient == LwToolkit.HORIZONTAL);
     int       gap   = metrics.getNetGap();
     int       top = 0, left = 0, bottom = 0, right = 0;

     if (borderView != null)
     {
       top    = borderView.getTop();
       left   = borderView.getLeft();
       bottom = borderView.getBottom();
       right  = borderView.getRight();
     }

     int       x     = isHor?cv.fc.y - this.x + metrics.getXOrigin() - gap:getLeft();
     int       y     = isHor?getTop():cv.fr.y - this.y + metrics.getYOrigin() - gap;
     int       size  = isHor?metrics.getGridCols():metrics.getGridRows();
     int       clipX  = g.getClipX(), clipY = g.getClipY(), clipW  = g.getClipWidth(), clipH  = g.getClipHeight();

     for (int i=(isHor?cv.fc.x:cv.fr.x); i <= (isHor?cv.lc.x:cv.lr.x); i++)
     {
       int    wh1 = isHor?metrics.getColWidth(i) + gap + (((size - 1) == i)?gap:0):psW;
       int    wh2 = isHor?psH:metrics.getRowHeight(i) + gap + (((size - 1) == i)?gap:0);
       int    row = isHor?0:i, col = isHor?i:0;

       LwView v = getTitleView(i);
       if (v != null)
       {
          int[] props = getTitleProps(i);
          if (props != null && props[2] != 0)
          {
            g.setColor (new Color(props[2]));
            g.fillRect(x, y, wh1 - 1, wh2 - 1);
          }

          Dimension ps = v.getPreferredSize();
          Point pp = LwToolkit.getLocation(ps, props != null?props[0]:LwToolkit.CENTER,
                                               props != null?props[1]:LwToolkit.CENTER,
                                               wh1 - left - right, wh2 - top - bottom);
          g.clipRect(x, y, wh1, wh2);
          v.paint (g, x + pp.x + left, y + pp.y + top, ps.width, ps.height, this);
          g.setClip(clipX, clipY, clipW, clipH);
       }

       if (borderView != null) borderView.paint (g, x, y, wh1, wh2, this);
       if (isHor) x += wh1;
       else       y += wh2;
     }
  }

  protected /*C#override*/ void recalc()
  {
    psW = 0;
    psH = 0;

    boolean isHor = (orient == LwToolkit.HORIZONTAL);
    int size = isHor?metrics.getGridCols():metrics.getGridRows();
    for (int i = 0; i < size; i++)
    {
      LwView v = getTitleView(i);
      if (v != null)
      {
        Dimension ps = v.getPreferredSize();
        if (isHor)
        {
          if (ps.height > psH) psH = ps.height;
          psW += ps.width;
        }
        else
        {
          if (ps.width > psW) psW = ps.width;
          psH += ps.height;
        }
      }
    }

    if (psH == 0) psH = DEF_ROWHEIGHT;
    if (psW == 0) psW = DEF_COLWIDTH;

    if (borderView != null)
    {
      psW += (borderView.getLeft() + borderView.getRight()) * (isHor?size:1);
      psH += (borderView.getTop() + borderView.getBottom()) * (isHor?1:size);
    }
  }

  protected /*C#override*/ Dimension calcPreferredSize() {
    return new Dimension (psW, psH);
  }

  private LwView getTitleView(int i)
  {
    Object data = getTitle (i);
    if (data != null)
    {
      if (data instanceof LwView) return (LwView)data;
      else
      {
        render.getTextModel().setText(data.toString());
        return render;
      }
    }
    return null;
  }

  private int[] getTitleProps(int i) {
    return titles != null && i < titles.length/2?(int[])titles[i*2 + 1]:null;
  }

 /**
  * Enable col resizing bit mask.
  */
  private static final int DEF_ROWHEIGHT = 10;
  private static final int DEF_COLWIDTH  = 10;
}

