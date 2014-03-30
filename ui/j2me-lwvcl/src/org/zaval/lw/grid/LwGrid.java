package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.data.event.*;
import org.zaval.lw.*;
import org.zaval.lw.event.*;
import org.zaval.misc.*;
import org.zaval.misc.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;

/**
 * This is lightweight grid component. The component is a composite component that is built basing
 * on MVC-model:
 * <ul>
 *   <li>
 *     <b>Model.</b> The component is bound with <code>MatrixModel</code>. Use
 *     the <code>setModel</code> and <code>getModel</code> methods to set and get the
 *     model.
 *   </li>
 *   <li>
 *     <b>View.</b>
 *     The cell rendering process is defined by the special view provider interface. Use
 *     the <code>setViewProvider</code> and <code>getViewProvider</code> methods to customize
 *     it. Using the interface it is possible to define a background color for the
 *     specified cell, a view to render the cell data and horizontal and vertical alignments.
 *   </li>
 *   <li>
 *     <b>Controller.</b>
 *     The grid component controls painting, event handling and validation processes.
 *   </li>
 * </ul>
 * <p>
 * The component supports cells editing and provides ability to customize editors set
 * by the editor provider interface. Use the <code>setEditorProvider</code> and
 * <code>getEditorProvider</code> methods to set and get the editor provider interface.
 * Actually the interface defines two things:
 * <ul>
 *  <li>A lightweight component that will be used to edit the specified cell data.</li>
 *  <li>How to fetch the edited data from the editor component to update the grid model.</li>
 * </ul>
 * <p>
 * The grid component supports two metric types:
 * <ul>
 *   <li>
 *     <b>Custom metric.</b> In this case the rows heights and columns widths can be defined
 *     by the <code>setRowHeight</code> and <code>setColWidth</code> methods, the painting
 *     process uses horizontal and vertical alignments (provided by the view provider interface)
 *     to align cells views.
 *   </li>
 *   <li>
 *     <b>Preferred size metric.</b> In this case the rows heights and columns widths are
 *     calculated basing on the cells views preferred sizes. The <code>setRowHeight</code> and
 *     <code>setColWidth</code> methods have no effect. The painting process doesn't use
 *     horizontal and vertical alignments (provided by view a provider interface) to align cells
 *     views.
 *   </li>
 * </ul>
 * To set appropriate metric type use the <code>usePsMetric</code> method.
 * <p>
 * The component sets special layout manager that should not be changed. The layout
 * manager defines TOP_CAPTION_EL constraint that can be used to add top grid caption
 * component as follow:
 * <pre>
 *   ...
 *   LwGrid        grid       = new LwGrid();
 *   LwGridCaption topCaption = new LwGridCaption(grid);
 *   grid.add (LwGrid.TOP_CAPTION_EL, topCaption);
 *   ...
 * </pre>
 * <p>
 * The layout manager defines LEFT_CAPTION_EL constraint that can be used to add left grid caption
 * component as follow:
 * <pre>
 *   ...
 *   LwGrid        grid        = new LwGrid();
 *   LwGridCaption leftCaption = new LwGridCaption(grid, LwToolkit.VERTICAL);
 *   grid.add (LwGrid.LEFT_CAPTION_EL, leftCaption);
 *   ...
 * </pre>
 * <p>
 * To control selection state use the PosController that can be got by the <code>getPosController</code>
 * method. The component supports single row selection. It is possible to disable rows selection
 * by the setting the pos controller to the <code>null</code>. Use the <code>setPosController</code> method
 * for this purpose.
 * <p>
 * Use the <code>setColor</code> method to customize the selection marker and net lines color.
 * <p>
 * The component implements the ScrollObj interface, so the grid component can be used inside
 * the LwScrollPan component.
 */
public class LwGrid
extends LwPanel
implements MatrixListener, ScrollObj, LwMouseListener,
           LwKeyListener, PosInfo, PosListener,
           LwLayout, LwGridMetrics
{
   /**
    * Top caption component layout constraint.
    */
    public static final Integer TOP_CAPTION_EL  = new Integer(1);

   /**
    * Left caption component layout constraint.
    */
    public static final Integer LEFT_CAPTION_EL = new Integer(3);

   /**
    * The stub component constraint.
    */
    public static final Integer STUB_EL = new Integer(4);


   /**
    * Draw horizontal lines bit mask.
    */
    public static final int DRAW_HLINES = 128;

   /**
    * Draw vertical lines bit mask.
    */
    public static final int DRAW_VLINES = 256;

   /**
    * The default column width.
    */
    public static final int DEF_COLWIDTH  = 35;

   /**
    * The default row height.
    */
    public static final int DEF_ROWHEIGHT = 15;

   /**
    * Defines the selection marker color type for the grid component that has the focus.
    */
    public static final int SEL1_COLOR = 0;

   /**
    * Defines the selection marker color type for the grid component that doesn't have the focus.
    */
    public static final int SEL2_COLOR = 1;

   /**
    * Defines the grid net lines color type.
    */
    public static final int NET_COLOR  = 2;

    private MatrixModel        data;
    protected int[]            colWidths, rowHeights;
    protected int              dx, dy, psWidth_, psHeight_;

    private int                netSize = 1, colOffset, rowOffset;
    private int                cellInsetsTop = 2, cellInsetsLeft = 2, cellInsetsBottom = 2, cellInsetsRight = 2;
    private LwGridViewProvider provider;
    private Color[]            colors = new Color[3];
    private CellsVisibility    visibility = new CellsVisibility();
    private ScrollMan          man;
    private PosController      controller;
    private LwComponent        leftCaption, topCaption, stub;
    private Rectangle          visibleArea;

   /**
    * Constructs the component with the default data model.
    */
    public LwGrid() {
      this(new Matrix(5, 5));
    }

   /**
    * Constructs the component with the specified data model.
    * @param data the specified data model
    */
    public LwGrid(MatrixModel data)
    {
      setColor(SEL1_COLOR, (Color)LwToolkit.getStaticObj("sel1.color"));
      setColor(SEL2_COLOR, (Color)LwToolkit.getStaticObj("sel2.color"));
      setColor(NET_COLOR, Color.gray);
      setModel(data);
      setViewProvider(new LwDefViews());
      setPosController (new PosController());
      setNetMask (DRAW_HLINES | DRAW_VLINES);
      add (STUB_EL, new LwCanvas());
    }

    public /*C#override*/ boolean canHaveFocus() {
      return true;
    }

   /**
    * Gets the net mask.
    * @return a net mask.
    */
    public int getNetMask () {
      return (bits & (DRAW_HLINES | DRAW_VLINES));
    }

   /**
    * Sets the specified net mask. The net mask is a bit mask that defines
    * what grid lines should be painted. There are four ways to paint grid lines:
    * <ul>
    *   <li>To paint only horizontal lines. Use DRAW_HLINES bit mask.</li>
    *   <li>To paint only vertical lines. Use DRAW_VLINES bit mask.</li>
    *   <li>To paint vertical and horizontal lines. Use DRAW_VLINES | DRAW_HLINES bit mask.</li>
    *   <li>To paint no lines. Use zero bits mask.</li>
    * </ul>
    * The default net mask is DRAW_VLINES | DRAW_HLINES.
    * @param mask the specified net mask.
    */
    public void setNetMask (int mask)
    {
      if (mask != getNetMask())
      {
        bits = MathBox.getBits(bits, DRAW_HLINES, (mask & DRAW_HLINES) > 0);
        bits = MathBox.getBits(bits, DRAW_VLINES, (mask & DRAW_VLINES) > 0);
        repaint();
      }
    }

   /**
    * Sets the specified grid metric type. There are two metric types:
    * <ul>
    *  <li>
    *    Preferred size metric type.
    *  </li>
    *  <li>
    *    Custom metric type.
    *  </li>
    * </ul>
    * The default metric type is custom metric type.
    * @param b use <code>true</code> to set preferred size metric type;
    *  <code>false</code> to set custom metric type.
    */
    public void usePsMetric (boolean b)
    {
      if (isUsePsMetric() != b)
      {
        bits = MathBox.getBits(bits, USE_PSMETRIC, b);
        vrp();
      }
    }

   /**
    * Tests if the grid uses preferred size to set columns widths
    * and rows heights.
    * @return <code>true</code> if the grid uses preferred size to set columns widths
    * and rows heights.
    */
    public boolean isUsePsMetric() {
      return MathBox.checkBit(bits, USE_PSMETRIC);
    }

   /**
    * Gets the position controller.
    * @return a position controller.
    */
    public PosController getPosController() {
      return controller;
    }

   /**
    * Sets the position controller. The controller can be set to <code>null</code>, in this
    * case it will be impossible to navigate over the grid component rows.
    * @param p the specified position controller.
    */
    public void setPosController(PosController p)
    {
      if (controller != p)
      {
        if (controller != null) controller.removePosListener(this);
        controller = p;
        if (controller != null)
        {
          controller.addPosListener(this);
          controller.setPosInfo(this);
        }
        repaint();
      }
    }

   /**
    * Gets the data model.
    * @return a data model.
    */
    public MatrixModel getModel () {
      return data;
    }

   /**
    * Gets the view provider.
    * @return a view provider.
    */
    public /*C#virtual*/ LwGridViewProvider getViewProvider() {
      return provider;
    }

   /**
    * Sets the view provider.
    * @param p the view provider.
    */
    public /*C#virtual*/ void setViewProvider(LwGridViewProvider p)
    {
      if (provider != p)
      {
        provider = p;
        vrp();
      }
    }

   /**
    * Sets the data model.
    * @param d the data model.
    */
    public /*C#virtual*/ void setModel (MatrixModel d)
    {
      if (d != data)
      {
        if (data != null) data.removeMatrixListener(this);
        data = d;
        if (data != null) data.addMatrixListener(this);
        vrp();
      }
    }

  /**
   * Sets the specified color to render the given element. Use the SEL1_COLOR, SEL2_COLOR
   * or NET_COLOR constant as the type value.
   * @param type the specified element type.
   * @param c the specified color.
   */
    public void setColor (int type, Color c)  {
      colors[type] = c;
      repaint();
    }

   /**
    * Gets the color for the specified element.
    * @param type the specified element type.
    * @return a color.
    */
    public Color getColor (int type) {
      return colors[type];
    }

   /**
    * Gets the grid cells insets. The insets specifies the top, left, bottom and
    * right indents.
    * @return a grid cells insets.
    */
    public Insets getCellInsets () {
      return new Insets (cellInsetsTop, cellInsetsLeft, cellInsetsBottom, cellInsetsRight);
    }

   /**
    * Sets the grid cells insets.
    * @param t the top cell indent.
    * @param l the left cell indent.
    * @param b the bottom cell indent.
    * @param r the right cell indent.
    */
    public void setCellInsets (int t, int l, int b, int r)
    {
      int nt = (t<0)?cellInsetsTop:t, nl = (l<0)?cellInsetsLeft:l;
      int nb = (b<0)?cellInsetsBottom:b, nr = (r<0)?cellInsetsRight:r;

      if (nt != cellInsetsTop    || nl != cellInsetsLeft ||
          nb != cellInsetsBottom || nr != cellInsetsRight    )
      {
        cellInsetsTop    = nt;
        cellInsetsLeft   = nl;
        cellInsetsBottom = nb;
        cellInsetsRight  = nr;
        vrp();
      }
    }

    public void matrixResized(Object target, int prevRows, int prevCols)
    {
      vrp();
      if (controller != null && controller.getCurrentLine() >= getGridRows())
        controller.clearPos();
    }

    public void cellModified (Object target, int row, int col, Object prevValue) {
      if (isUsePsMetric()) invalidate();
    }

    public /*C#override*/ void paint(Graphics g)
    {
      vVisibility();
      if (visibility.hasVisibleCells())
      {
        int clipX = 0, clipY = 0, clipW = 0, clipH = 0;
        if (leftCaption != null ||  topCaption != null)
        {
          clipX = g.getClipX();
          clipY = g.getClipY();
          clipW = g.getClipWidth();
          clipH = g.getClipHeight();
          g.clipRect(leftCaption!=null && leftCaption.isVisible()?leftCaption.getX() + leftCaption.getWidth() - dx:clipX,
                     topCaption !=null &&  topCaption.isVisible()?topCaption.getY()  + topCaption.getHeight() - dy:clipY,
                     width, height);
        }
        paintData  (g);
        paintNet (g);
        if (clipW > 0 && clipH > 0) g.setClip(clipX, clipY, clipW, clipH);
      }
    }

    public /*C#override*/ void paintOnTop(Graphics g) {
      if (visibility.hasVisibleCells()) paintMarker(g);
    }

    protected /*C#override*/ void laidout()  {
      vVisibility ();
    }

    protected /*C#override*/ void recalc ()
    {
      if (isUsePsMetric()) rPsMetric();
      else                 rCustomMetric();
      rPs();
    }

    public /*C#override*/ void invalidate()
    {
      super.invalidate();
      iColVisibility(0);
      iRowVisibility(0);
    }

    public /*C#virtual*/ void setRowHeight (int row, int h)
    {
      if (!isUsePsMetric())
      {
        validate();
        if (rowHeight(row) != h)
        {
          psHeight_ += (h - rowHeight(row));
          rowHeights[row] = h;

          updateCashedPs(-1, psHeight_ + ((topCaption != null && topCaption.isVisible())?topCaption.getPreferredSize().height:0));
          if (parent != null) parent.invalidate();
          iRowVisibility(0);

          // Invalidate layout
          isValidValue = false;
          repaint();
        }
      }
    }

    public /*C#override*/ boolean isInvalidatedByChild(LwComponent c) {
      return isUsePsMetric();
    }

    public /*C#virtual*/ void setColWidth (int col, int w)
    {
      if (!isUsePsMetric())
      {
        validate();
        if (colWidth(col) != w)
        {
          psWidth_ += (w - colWidth(col));
          colWidths[col] = w;
          updateCashedPs(psWidth_ + ((leftCaption != null && leftCaption.isVisible())?leftCaption.getPreferredSize().width:0), -1);
          if (parent != null) parent.invalidate();
          iColVisibility(0);

          // Invalidate layout
          isValidValue = false;
          repaint();
        }
      }
    }

    public /*C#override*/ Point getOrigin () {
      return new Point (dx, dy);
    }

    public /*C#virtual*/ Point getSOLocation() {
      return getOrigin ();
    }

    public /*C#virtual*/ void setSOLocation(int x, int y)
    {
      if (x != dx || y != dy)
      {
        int offx = x - dx, offy = y - dy;

        if (offx != 0) iColVisibility(offx > 0?1:-1);
        if (offy != 0) iRowVisibility(offy > 0?1:-1);

        dx = x;
        dy = y;
        repaint();
      }
    }

    public /*C#virtual*/ Dimension getSOSize() {
      return getPreferredSize();
    }

    public boolean moveContent() {
      return true;
    }

    public void setScrollMan (ScrollMan m) {
      man = m;
    }

    public /*C#virtual*/ void mouseClicked (LwMouseEvent e) { }
    public /*C#virtual*/ void mouseReleased(LwMouseEvent e) {}
    public /*C#virtual*/ void mouseEntered (LwMouseEvent e) {}
    public /*C#virtual*/ void mouseExited  (LwMouseEvent e) {}

    public /*C#virtual*/ void mousePressed (LwMouseEvent e)
    {
      if (visibility.hasVisibleCells())
      {
        if (LwToolkit.isActionMask(e.getMask()))
        {
          Point p = cellByLocation(e.getX(), e.getY());
          if (p != null)
          {
            if (controller != null)
            {
              int off = controller.getCurrentLine();
              if (off == p.x) calcOrigin(off, getRowY(off));
              else controller.setOffset (p.x);
            }
          }
        }
      }
    }

    public int getLines() {
      return getGridRows();
    }

    public int getLineSize(int line) {
      return 1;
    }

    public int getMaxOffset() {
      return getGridRows()-1;
    }

    public /*C#virtual*/ void posChanged(Object target, int prevOffset, int prevLine, int prevCol)
    {
      int off = controller.getCurrentLine();
      if (off >= 0)
      {
        int y = getRowY(off);
        calcOrigin(off, y);
        if (prevOffset >= 0)
        {
          int yy = getRowY(prevOffset);
          repaint (0, Math.min(yy, y) + dy, width, Math.abs(yy - y) + rowHeight(Math.max(off, prevOffset)));
        }
        else repaint ();
      }
    }

    public /*C#virtual*/ void keyPressed(LwKeyEvent e)
    {
      if (controller != null)
      {
        switch (e.getKeyCode())
        {
          case LwToolkit.VK_LEFT     : controller.seek(-1); break;
          case LwToolkit.VK_UP       : controller.seekLineTo(PosController.UP); break;
          case LwToolkit.VK_RIGHT    : controller.seek(1); break;
          case LwToolkit.VK_DOWN     : controller.seekLineTo(PosController.DOWN); break;
          case LwToolkit.VK_PAGE_UP  : controller.seekLineTo(PosController.UP,   pageSize(-1)); break;
          case LwToolkit.VK_PAGE_DOWN: controller.seekLineTo(PosController.DOWN, pageSize(1)); break;
          case LwToolkit.VK_END      : controller.setOffset(getLines()-1); break;
          case LwToolkit.VK_HOME     : controller.setOffset(0); break;
        }
      }
    }

    public /*C#virtual*/ void keyReleased(LwKeyEvent e) {}
    public /*C#virtual*/ void keyTyped   (LwKeyEvent e) {}

    public /*C#virtual*/ void layout(LayoutContainer target)
    {
      int topHeight  = (topCaption != null && topCaption.isVisible())?topCaption.getPreferredSize().height:0;
      int leftWidth  = (leftCaption != null && leftCaption.isVisible())?leftCaption.getPreferredSize().width:0;

      if (stub != null && stub.isVisible())
      {
        Dimension stubPs = stub.getPreferredSize();
        leftWidth = Math.max(leftWidth, stubPs.width);
        topHeight = Math.max(topHeight, stubPs.height);
        stub.setSize(leftWidth, topHeight);
        stub.setLocation(getLeft(), getTop());
      }

      if (topHeight > 0)
      {
        topCaption.setLocation (getLeft() + leftWidth, getTop());
        topCaption.setSize(Math.min(target.getWidth() - getLeft() - getRight() - leftWidth, psWidth_), topHeight);
      }

      if (leftWidth > 0)
      {
        leftCaption.setLocation (getLeft(), getTop() + topHeight);
        leftCaption.setSize(leftWidth, Math.min(target.getHeight() - getTop() - getBottom() - topHeight, psHeight_));
      }
    }

    public /*C#virtual*/ void componentAdded(Object id, Layoutable lw, int index)
    {
      if (id != null)
      {
        if (id.equals(TOP_CAPTION_EL)) topCaption = (LwComponent)lw;
        else
        if (id.equals(LEFT_CAPTION_EL)) leftCaption = (LwComponent)lw;
        else
        if (id.equals(STUB_EL)) stub = (LwComponent)lw;
      }
    }

    public /*C#virtual*/ void componentRemoved(Layoutable lw, int index)
    {
      if (lw == topCaption) topCaption = null;
      else
      if (lw == leftCaption) leftCaption = null;
      else
      if (lw == stub) stub = null;
    }

    public /*C#virtual*/ Dimension calcPreferredSize(LayoutContainer target) {
      return new Dimension (psWidth_  + ((leftCaption != null && leftCaption.isVisible())?leftCaption.getPreferredSize().width:0),
                            psHeight_ + ((topCaption != null && topCaption.isVisible())?topCaption.getPreferredSize().height:0));
    }

    public /*C#virtual*/ int getGridRows() {
      return data.getRows();
    }

    public /*C#virtual*/ int getGridCols() {
      return data.getCols();
    }

    public /*C#virtual*/ int getRowHeight(int row) {
      validate();
      return rowHeight(row);
    }

    public /*C#virtual*/ int getColWidth (int col) {
      validate();
      return colWidth(col);
    }

    public CellsVisibility getCellsVisibility() {
      validate();
      return visibility;
    }

    public int getNetGap() {
      return netSize;
    }

    public /*C#virtual*/ int getColX (int col) {
      validate();
      return getColX_(col);
    }

    private int getColX_ (int col)
    {
      int start = 0, d = 1;
      int x = getLeft() + getLeftCaptionWidth() + netSize;
      if (visibility.hasVisibleCells())
      {
        start = visibility.fc.x;
        x     = visibility.fc.y;
        d     = (col > visibility.fc.x)?1:-1;
      }
      for (int i=start; i != col; x += ((colWidth(i) + netSize)*d), i+=d);
      return x;
    }

    public /*C#virtual*/ int getRowY (int row) {
      validate();
      return getRowY_(row);
    }

    private int getRowY_(int row)
    {
      int start = 0, d = 1;
      int y = getTop() + getTopCaptionHeight() + netSize;
      if (visibility.hasVisibleCells())
      {
        start = visibility.fr.x;
        y     = visibility.fr.y;
        d     = (row > visibility.fr.x)?1:-1;
      }
      for (int i=start; i != row; y += ((rowHeight(i) + netSize)*d), i+=d);
      return y;
    }

   /**
    * Gets the grid top caption component.
    * @return a grid top caption component.
    */
    public LwComponent getTopCaption() {
      return topCaption;
    }

   /**
    * Gets the grid left caption component.
    * @return a grid left caption component.
    */
    public LwComponent getLeftCaption() {
      return leftCaption;
    }

    public int getXOrigin () {
      return dx;
    }

    public int getYOrigin () {
      return dy;
    }

    public /*C#virtual*/ int getColPSWidth(int col) {
      return getPSSize(col, false);
    }

    public /*C#virtual*/ int getRowPSHeight(int row) {
      return getPSSize(row, true);
    }

   /**
    * Returns the specified column width. The method is used by all other methods
    * (except recalculation) to get the actual column width.
    * @param col the specified column.
    * @return the specified column width.
    */
    protected /*C#virtual*/ int colWidth (int col) {
      return colWidths[col];
    }

   /**
    * Returns the specified row height. The method is used by all other methods
    * (except recalculation) to get the actual row height.
    * @param row the specified row.
    * @return the specified row height.
    */
    protected /*C#virtual*/ int rowHeight (int row) {
      return rowHeights[row];
    }

   /**
    * Invoked whenever the component paints the specified cell to fetch data from the
    * grid data model.
    * @param row the specified row.
    * @param col the specified column.
    * @return data to be painted.
    */
    protected /*C#virtual*/ Object dataToPaint (int row, int col) {
      return data.get(row, col);
    }

   /**
    * Calculates the preferred size of the grid component. The method calls
    * <code>colWidth</code> and <code>rowHeight</code> to get actual columns
    * widths and rows heights. The method is called by <code>vMetric</code>
    * method.
    * @return a calculated preferred size;
    */
    private void rPs()
    {
      int cols = getGridCols();
      int rows = getGridRows();
      psWidth_  = netSize * (cols + 1);
      psHeight_ = netSize * (rows + 1);
      for (int i=0; i<cols; i++) psWidth_  += colWidth(i);
      for (int i=0; i<rows; i++) psHeight_ += rowHeight(i);
    }

   /**
    * Paints the grid lines.
    * @param g the specified graphics context.
    */
    protected /*C#virtual*/ void paintNet (Graphics g)
    {
      int topX = visibility.fc.y - netSize;
      int topY = visibility.fr.y - netSize;
      int botX = visibility.lc.y + colWidth (visibility.lc.x);
      int botY = visibility.lr.y + rowHeight(visibility.lr.x);

      g.setColor(colors[NET_COLOR]);
      if (MathBox.checkBit(bits, DRAW_HLINES))
      {
        int y = topY, i = visibility.fr.x;
        for (;i <= visibility.lr.x; i++)
        {
          g.drawLine(topX, y, botX, y);
          y += rowHeight(i) + netSize;
        }
        g.drawLine(topX, y, botX, y);
      }

      if (MathBox.checkBit(bits, DRAW_VLINES))
      {
        int x = topX, i = visibility.fc.x;
        for (;i <= visibility.lc.x; i++)
        {
          g.drawLine(x, topY, x, botY);
          x += colWidth(i) + netSize;
        }
        g.drawLine(x, topY, x, botY);
      }
    }

   /**
    * Paints the grid cells.
    * @param g the specified graphics context.
    */
    protected /*C#virtual*/ void paintData (Graphics g)
    {
      int y    = visibility.fr.y + cellInsetsTop;
      int addW = cellInsetsLeft + cellInsetsRight;
      int addH = cellInsetsTop  + cellInsetsBottom;
      int       cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
      Rectangle res = new Rectangle();

      for (int i=visibility.fr.x; i <= visibility.lr.x && y < cy + ch; i++)
      {
        if (y + rowHeight(i) > cy)
        {
          int x = visibility.fc.y + cellInsetsLeft;
          for (int j=visibility.fc.x; j<=visibility.lc.x; j++)
          {
             LwView v = provider.getView(i, j, dataToPaint(i, j));
             if (v != null)
             {
               Color bg = provider.getCellColor(i, j);
               if (bg != null)
               {
                 g.setColor(bg);
                 g.fillRect(x - cellInsetsLeft, y - cellInsetsTop, colWidth(j), rowHeight(i));
               }

               int w = colWidth (j) - addW;
               int h = rowHeight(i) - addH;

               MathBox.intersection(x, y, w, h, cx, cy, cw, ch, res);
               if (res.width > 0 && res.height > 0)
               {
                 g.setClip(res);
                 if (isUsePsMetric() || v.getType() == LwView.STRETCH) v.paint(g, x, y, w, h, this);
                 else
                 {
                   Dimension ps = v.getPreferredSize();
                   Point p = LwToolkit.getLocation (ps,
                                                    provider.getXAlignment(i, j),
                                                    provider.getYAlignment(i, j),
                                                    w, h);
                   v.paint(g, x + p.x, y + p.y, ps.width, ps.height, this);
                 }
                 g.setClip (cx, cy, cw, ch);
               }
             }
             x += (colWidth(j) + netSize);
          }
        }
        y += (rowHeight(i) + netSize);
      }
    }

   /**
    * Paints the grid marker.
    * @param g the specified graphics context.
    */
    protected /*C#virtual*/ void paintMarker (Graphics g)
    {
      if (controller != null && controller.getOffset() >= 0)
      {
        int offset = controller.getOffset();
        if (offset >= visibility.fr.x && offset <= visibility.lr.x)
        {
          //???
          g.clipRect(getLeftCaptionWidth()-dx, getTopCaptionHeight() - dy, width, height);

          int y = getRowY(offset), h = rowHeight(offset);
          int x = visibility.fc.y;

          for (int i=visibility.fc.x; i<=visibility.lc.x; i++)
          {
            Color bg = provider.getCellColor(offset, i);
            if (bg == null) bg = getBackground();
            LwToolkit.drawMarker(g, x, y, colWidth(i), h, bg, colors[hasFocus()?SEL1_COLOR:SEL2_COLOR]);
            x += colWidth(i) + netSize;
          }
        }
      }
    }

    protected /*C#override*/ LwLayout getDefaultLayout() {
      return this;
    }

   /**
    * Finds and returns grid cell row and column at the specified location.
    * The result is presented with org.zaval.port.j2me.Point class where <code>x</code>
    * field corresponds to row and <code>y</code> field corresponds to column.
    * @param x the specified x coordinate.
    * @param y the specified y coordinate.
    * @return a cell at the specified location.
    */
    public /*C#virtual*/ Point cellByLocation(int x, int y)
    {
      validate();
      int ry1 = visibility.fr.y + dy;
      int ry2 = visibility.lr.y + rowHeight(visibility.lr.x) + dy;
      int rx1 = visibility.fc.y + dx;
      int rx2 = visibility.lc.y + colWidth(visibility.lc.x) + dx;

      int row = -1, col = -1;

      if (y > ry1 && y < ry2)
      {
         for (int i = visibility.fr.x; i<=visibility.lr.x; ry1 += rowHeight(i) + netSize, i++)
           if (y > ry1 && y < ry1 + rowHeight(i))
           {
             row = i;
             break;
           }
      }

      if (x > rx1 && x < rx2)
      {
         for (int i = visibility.fc.x; i<=visibility.lc.x; rx1 += colWidth(i) + netSize, i++)
           if (x > rx1 && x < rx1 + colWidth(i))
           {
             col = i;
             break;
           }
      }
      return (col >= 0 && row >=0)?new Point(row, col):null;
    }

   /**
    * Invoked by <code>vMetric</code> method to calculate preferred size metric type.
    */
    protected /*C#virtual*/ void rPsMetric()
    {
      int cols = getGridCols(), rows = getGridRows();
      if (colWidths  == null || colWidths.length  != cols) colWidths  = new int[cols];
      if (rowHeights == null || rowHeights.length != rows) rowHeights = new int[rows];

      int addW = cellInsetsLeft + cellInsetsRight;
      int addH = cellInsetsTop  + cellInsetsBottom;

      for (int i=0; i<cols ;i++) colWidths [i] = 0;
      for (int i=0; i<rows ;i++) rowHeights[i] = 0;

      for (int i=0; i<cols ;i++)
      {
        for (int j=0; j<rows; j++)
        {
          LwView v = provider.getView(j, i, data.get(j, i));
          if (v != null)
          {
            Dimension ps = v.getPreferredSize();
            ps.width  += addW;
            ps.height += addH;
            if (ps.width  > colWidths[i] ) colWidths [i] = ps.width;
            if (ps.height > rowHeights[j]) rowHeights[j] = ps.height;
          }
          else
          {
            if (DEF_COLWIDTH  > colWidths [i]) colWidths [i] = DEF_COLWIDTH;
            if (DEF_ROWHEIGHT > rowHeights[j]) rowHeights[j] = DEF_ROWHEIGHT;
          }
        }
      }
    }

    protected /*C#virtual*/ int getPSSize(int rowcol, boolean b)
    {
      if (isUsePsMetric()) return b?getRowHeight(rowcol):getColWidth(rowcol);
      else
      {
        int cols = getGridCols(), rows = getGridRows();
        int max = 0, count = b?cols:rows;
        for (int j=0; j<count; j++)
        {
          int r = b?rowcol:j, c = b?j:rowcol;
          LwView v = provider.getView(r, c, data.get(r, c));
          if (v != null)
          {
            Dimension ps = v.getPreferredSize();
            if (b)
            {
              if (ps.height > max) max = ps.height;
            }
            else if (ps.width > max) max = ps.width;
          }
        }
        return max + netSize*2 + (b?cellInsetsTop + cellInsetsBottom:cellInsetsLeft + cellInsetsRight);
      }
    }

   /**
    * Invoked by <code>vMetric</code> method to calculate custom metric type.
    */
    protected /*C#virtual*/ void rCustomMetric()
    {
      int start = 0;
      if (colWidths != null)
      {
        start = colWidths.length;
        if (colWidths.length != getGridCols())
        {
          int[] na = new int[getGridCols()];
          System.arraycopy(colWidths, 0, na, 0, Math.min(colWidths.length, na.length));
          colWidths = na;
        }
      }
      else colWidths = new int[getGridCols()];
      for (; start<colWidths.length; start++) colWidths[start] = DEF_COLWIDTH;

      start = 0;
      if (rowHeights != null)
      {
        start = rowHeights.length;
        if (rowHeights.length != getGridRows())
        {
          int[] na = new int[getGridRows()];
          System.arraycopy(rowHeights, 0, na, 0, Math.min(rowHeights.length, na.length));
          rowHeights = na;
        }
      }
      else rowHeights = new int[getGridRows()];
      for (;start<rowHeights.length; start++) rowHeights[start] = DEF_ROWHEIGHT;
    }

   /**
    * Returns the page size for the specified direction.
    * @param d the specified direction. Use <code>-1</code> value to specify bottom-up direction and
    * <code>1</code> value to specify up-bottom direction.
    * @return a page size.
    */
    protected /*C#virtual*/ int pageSize(int d)
    {
      validate();
      if (visibility.hasVisibleCells())
      {
        int off = controller.getOffset();
        if (off >= 0)
        {
           int hh = visibleArea.height - getTopCaptionHeight();
           int sum = 0, poff = off;
           for (;off >=0 && off < getGridRows() && sum < hh; sum += rowHeight(off) + netSize, off+=d);
           return Math.abs(poff - off);
        }
      }
      return 0;
    }

   /**
    * Invalidates columns visibility properties.
    */
    protected /*C#virtual*/ void iColVisibility(int off) {
      if (colOffset == 100) colOffset = off;
      else colOffset = ((off + colOffset) == 0)?0:colOffset;
    }

   /**
    * Invalidates rows visibility properties.
    */
    protected /*C#virtual*/ void iRowVisibility(int off) {
      if (rowOffset == 100) rowOffset = off;
      else rowOffset = ((off + rowOffset) == 0)?0:rowOffset;
    }

    private Point colVisibility(int col, int x, int d, boolean b)
    {
      int cols = getGridCols();
      if (cols == 0) return null;

      int left = getLeft(), right = getRight();
      int xx1  = Math.min(visibleArea.x + visibleArea.width, getWidth()- right);
      int xx2 =  Math.max(left, visibleArea.x + getLeftCaptionWidth());
      for (;col < cols && col >=0; col+=d)
      {
        if (x + dx < xx1 && (x + colWidth(col) + dx) > xx2)
        {
          if (b) return new Point (col, x);
        }
        else if (!b) return colVisibility(col, x, (d > 0?-1:1), true);

        if (d < 0) {
          if (col > 0) x -= (colWidth(col - 1) + netSize);
        }
        else if (col < cols - 1) x += (colWidth(col) + netSize);
      }

      if (b) return null;

      return (d > 0)?new Point(col - 1, x):
                     new Point(0, getLeft() + getLeftCaptionWidth() + netSize);

    }

    private Point rowVisibility(int row, int y, int d, boolean b)
    {
      int rows = getGridRows();
      if (rows == 0) return null;

      int top = getTop(), bottom = getBottom();
      int yy1 = Math.min(visibleArea.y + visibleArea.height, getHeight() - bottom);
      int yy2 = Math.max(visibleArea.y, top + getTopCaptionHeight());
      for (;row < rows && row >= 0; row+=d)
      {
        if (y + dy < yy1 && (y + rowHeight(row) + dy) > yy2)
        {
          if (b) return new Point (row, y);
        }
        else if (!b) return rowVisibility(row, y, (d > 0?-1:1), true);

        if (d < 0)  {
          if (row > 0) y -= (rowHeight(row - 1) + netSize);
        }
        else if (row < rows - 1) y += (rowHeight(row) + netSize);
      }

      if (b) return null;

      return (d > 0)?new Point(row - 1, y):
                     new Point(0, getTop() + getTopCaptionHeight() + netSize);
    }

    private void vVisibility ()
    {
      Rectangle va = getVisiblePart();
      if (va == null)
      {
        visibleArea = null;
        visibility.cancelVisibleCells();
        return;
      }
      else
      {
        if (!va.equals(visibleArea))
        {
          iColVisibility(0);
          iRowVisibility(0);
          visibleArea = va;
        }
      }

      boolean b = visibility.hasVisibleCells();
      if (colOffset != 100)
      {
        if (colOffset > 0 && b)
        {
          visibility.lc = colVisibility(visibility.lc.x, visibility.lc.y, -1, true);
          visibility.fc = colVisibility(visibility.lc.x, visibility.lc.y, -1, false);
        }
        else
        if (colOffset < 0 && b)
        {
          visibility.fc = colVisibility(visibility.fc.x, visibility.fc.y, 1, true);
          visibility.lc = colVisibility(visibility.fc.x, visibility.fc.y, 1, false);
        }
        else
        {
          visibility.fc = colVisibility(0, getLeft() + netSize + getLeftCaptionWidth(), 1, true);
          visibility.lc = (visibility.fc!=null)?colVisibility(visibility.fc.x, visibility.fc.y, 1, false):null;
        }
        colOffset = 100;
      }

      if (rowOffset != 100)
      {
        if (rowOffset > 0 && b)
        {
          visibility.lr = rowVisibility(visibility.lr.x, visibility.lr.y, -1, true);
          visibility.fr = rowVisibility(visibility.lr.x, visibility.lr.y, -1, false);
        }
        else
        if (rowOffset < 0 && b)
        {
          visibility.fr = rowVisibility(visibility.fr.x, visibility.fr.y, 1, true);
          visibility.lr = rowVisibility(visibility.fr.x, visibility.fr.y, 1, false);
        }
        else
        {
          visibility.fr = rowVisibility(0, getTop() + getTopCaptionHeight() + netSize, 1, true);
          visibility.lr = (visibility.fr!=null)?rowVisibility(visibility.fr.x, visibility.fr.y, 1, false):null;
        }
        rowOffset = 100;
      }
    }

    private void calcOrigin(int off, int y)
    {
      Insets i = getInsets();
      i.top += getTopCaptionHeight();
      i.left += getLeftCaptionWidth();
      Point o = LwToolkit.calcOrigin(getColX(0) - netSize, y - netSize, psWidth_, rowHeight(off) + 2*netSize, dx, dy, this, i);
      if (man != null) man.moveScrolledObj(o.x, o.y);
      else             setSOLocation(o.x, o.y);
    }

   /**
    * Gets the grid top caption component height.
    * @return a grid top caption component height.
    */
    protected int getTopCaptionHeight() {
      return (topCaption != null && topCaption.isVisible())?topCaption.getHeight():0;
    }

   /**
    * Gets the grid left caption component width.
    * @return a grid left caption component width.
    */
    protected int getLeftCaptionWidth() {
      return (leftCaption != null && leftCaption.isVisible())?leftCaption.getWidth():0;
    }

   /**
    * Use preferred size metric bit mask.
    */
    private static final int USE_PSMETRIC = 64;
}


