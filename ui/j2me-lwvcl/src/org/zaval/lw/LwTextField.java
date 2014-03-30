package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.lw.event.*;
import org.zaval.misc.*;
import org.zaval.misc.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is a text field component. The library provides several text renders implementations
 * that can be used as a face view of the component:
 * <ul>
 *   <li>
 *     LwTextRender. This text render is used as default text render. It allows rendering text and
 *     selected text part
 *   </li>
 *   <li>
 *     LwPasswordText. This text render is used for password text fields.
 *     The sample below shows the render usage:
 *     <pre>
 *        ...
 *        LwTextFiled tf = new LwTextFiled("Password text");
 *        tf.getViewMan(true).setView(new LwPasswordText(tf.getText()));
 *        ...
 *     </pre>
 *    </li>
 * </ul>
 * <p>
 * The list below describes some features of the component:
 * <ul>
 *   <li>
 *     Use the following code to organize single line text field component with a fixed number
 *     of characters
 *     <pre>
 *        ...
 *        // Creates single line text field component, where it is possible to
 *        // enter at most 10 characters.
 *        LwTextFiled tf = new LwTextFiled("", 10);
 *        ...
 *     </pre>
 *   </li>
 *   <li>
 *     Use <code>setPSByRowsCols</code> method to define preferred size of the component
 *     basing on the number of rows and columns. The method calculates and sets preferred size of
 *     the text field component according to its row size and column size. Draw attention that
 *     if the font has been redefined for the text render, than it is necessary to call
 *     the method again to recalculate his preferred size.
 *   </li>
 *   <li>
 *     It is possible to redefined text cursor view by <code>setCursorView</code> method.
 *   </li>
 *   <li>
 *     It is possible to define another pos controller by <code>setPosController</code>
 *     method to control text cursor position.
 *   </li>
 *   <li>
 *     Use <code>setEditable</code> method to define if a text can be edited or not.
 *   </li>
 *   <li>
 *     Use <code>getSelectedText</code> method to get selected text.
 *   </li>
 * </ul>
 * <p>
 * The component overrides the <code>getOrigin</code> method to organize scrolling of the
 * content. The scrolling mechanism works right if the component will be inserted
 * into a LwScrollPan component.
 * <p>
 * To listen when the cursor position has been changed use the pos controller as follow:
 * <pre>
 *   public class Sample
 *   implements PosListener
 *   {
 *     ...
 *     public void init()
 *     {
 *       ...
 *       LwTextFiled tf = new LwTextFiled("Text");
 *       tf.getPosController().addPosListener(this);
 *       ...
 *     }
 *     public void posChanged(Object target, int po, int pl, int pc) {
 *       System.out.println("The old cursor location is - " + po);
 *     }
 *   }
 * </pre>
 */
public class LwTextField
extends LwLabel
implements LwKeyListener, LwMouseListener,
           LwFocusListener, PosInfo, PosListener, ScrollObj

{
  private PosController cur;
  private ScrollMan     man;
  private int           curX, curY, curH, curW, dx = 0, dy = 0;
  private boolean       isEditableVal = true;

 /**
  * Constructs a text filed component with no text.
  */
  public LwTextField() {
    this("");
  }

 /**
  * Constructs a text field component with the specified text. The methods sets LwTextRender
  * as the face view and sets org.zaval.data.Text as the render target.
  * @param s the specified text.
  */
  public LwTextField(String s) {
    this(new Text(s));
  }

 /**
  * Constructs a single line text field component with the specified text and
  * the maximal number of columns.
  * @param s the specified text.
  * @param maxCol the specified maximal number of columns.
  */
  public LwTextField(String s, int maxCol) {
    this (new SingleLineTxt(s, maxCol));
  }

 /**
  * Constructs a text field component with the specified text model.
  * @param model the specified text model.
  */
  public LwTextField(TextModel model)
  {
    super (model);
    setPosController(new PosController(this));
    getViewMan(true).setBorder(LwToolkit.getView("br.sunken"));
  }

  public /*C#override*/ boolean canHaveFocus() {
    return true;
  }

 /**
  * Gets the pos controller that manages the text cursor position.
  * @return a pos controller.
  */
  public PosController getPosController() {
    return cur;
  }

 /**
  * Sets the specified pos controller to manage the text cursor position.
  * @param p the specified pos controller.
  */
  public void setPosController(PosController p)
  {
    if (cur != p)
    {
      if (cur != null) cur.removePosListener(this);
      cur = p;
      cur.setPosInfo(this);
      cur.addPosListener(this);
      invalidate();
    }
  }

 /**
  * Paints this component. The method initiates painting of the cursor caret by calling
  * <code>drawCursor</code> method.
  * @param g the graphics context to be used for painting.
  */
  public /*C#override*/ void paint(Graphics g) {
    super.paint(g);
    drawCursor(g);
  }

 /**
  * Sets the specified mode for the text field component. The component is editable if
  * it is possible to edit a text.
  * @param b the specified mode. If the mode is <code>true</code> than the component
  * is editable; otherwise not-editable.
  */
  public void setEditable (boolean b)
  {
    if (b != isEditableVal)
    {
      isEditableVal = b;
      vrp();
    }
  }

 /**
  * Checks if the text field component is editable.
  * @return <code>true</code> if the text field is editable; <code>false</code>
  * otherwise.
  */
  public boolean isEditable() {
    return isEditableVal;
  }

  public void keyPressed(LwKeyEvent e) {
    if (!isFiltered(e)) handleKey(e);
  }

  public void keyTyped(LwKeyEvent e)
  {
    if ((e.getMask()&LwToolkit.CTRL_MASK) == 0)
    {
      if (e.getKeyChar() == '\n' && getTextModel() instanceof SingleLineTxt) return;
      write(cur.getOffset(), String.valueOf(e.getKeyChar()));
    }
  }

  public void focusGained  (LwFocusEvent e) {
    if (cur.getOffset() < 0) cur.setOffset(0);
  }

  public void focusLost    (LwFocusEvent e) {}
  public void keyReleased  (LwKeyEvent e)   {}

  public void mouseClicked (LwMouseEvent e) {}
  public void mouseEntered (LwMouseEvent e) {}
  public void mouseExited  (LwMouseEvent e) {}
  public void mouseReleased(LwMouseEvent e) {}

  public void mousePressed (LwMouseEvent e)
  {
    if (LwToolkit.isActionMask(e.getMask()))
    {
      Point p = getTextRowColAt(getTextRender(), e.getX() - dx - getLeft(), e.getY() - dy - getTop());
      if (p != null) cur.setRowCol(p.x, p.y);
    }
  }

  public int getLineSize(int i) {
    return getTextModel().getLine(i).length() + 1;
  }

  public int getLines() {
    return getTextModel().getSize();
  }

  public int getMaxOffset() {
    return getTextModel().getTextLength();
  }

  public /*C#override*/ void setText(String s)
  {
    cur.setOffset(0);
    if (man != null) man.moveScrolledObj(0, 0);
    else             setSOLocation(0, 0);
    super.setText(s);
  }

  public /*C#virtual*/ void posChanged(Object target, int po, int pl, int pc)
  {
    rCursor();
    if (cur.getOffset() >=0)
    {
      int lineHeight = getTextRender().getLineHeight(), top = getTop();
      if (man != null) man.makeVisible(curX + getLeft(), curY + top, curW, lineHeight);
      else
      {
        Point o = LwToolkit.calcOrigin(curX + getLeft(), curY + top, curW, lineHeight, this);
        setSOLocation(o.x, o.y);
      }

      if (pl >= 0)
      {
        int minUpdatedLine = Math.min(pl, cur.getCurrentLine());
        int maxUpdatedLine = Math.max(pl, cur.getCurrentLine());

        int li = getTextRender().getLineIndent();
        int y1 = lineHeight * minUpdatedLine + minUpdatedLine * li + top + dy;

        if (y1 < top) y1 = top;
        if (y1 < height - bottom)
        {
          int h = (maxUpdatedLine - minUpdatedLine + 1)*(lineHeight + li);
          if (y1 + h > height - bottom) h = height - bottom - y1;
          repaint (left, y1, width - left - right, h);
        }
      }
      else {
        repaint();
      }
    }
  }

 /**
  * Returns an origin of the component. The method is overridden with the component
  * to offset a content of the text field depending on the cursor position.
  * @return an origin of the component.
  */
  public /*C#override*/ Point getOrigin() {
    return (dx != 0 || dy !=0)?new Point(dx, dy):null;
  }

  public /*C#override*/ Dimension getPreferredSize()
  {
    Dimension d = super.getPreferredSize();
    if (psWidth < 0) d.width += curW;
    return d;
  }

  public Point getSOLocation () {
    return new Point(dx, dy);
  }

  public void setSOLocation (int x, int y)
  {
    if (x != dx || y != dy)
    {
      dx = x;
      dy = y;
      repaint();
    }
  }

  public Dimension getSOSize() {
    return getPreferredSize();
  }

  public void setScrollMan (ScrollMan m) {
    man = m;
  }

 /**
  * Tests if the scroll component performs scrolling by changing it location or view.
  * The method is overridden with the component to point move a content of the text field,
  * the method always returns <code>true</code>.
  * @return <code>true</code> if the scroll component organizes scrolling by moving
  * its view; otherwise <code>false</code>.
  */
  public boolean moveContent   () {
    return true;
  }

 /**
  * The method is used to paint the text cursor using the cursor view.
  * @param g the graphics context to be used for painting.
  */
  protected /*C#virtual*/ void drawCursor(Graphics g)
  {
    if (isEditableVal && hasFocus() && cur.getOffset() >= 0)
    {
      g.setColor(getForeground());
      g.fillRect(curX + getLeft(), curY + getTop(), curW, curH);
    }
  }

  protected /*C#override*/ void recalc()
  {
    if (cur.getOffset() >= 0)
    {
      int row = cur.getCurrentLine();
      int col = cur.getCurrentCol ();
      TextModel text = getTextModel();
      if (row >= text.getSize() || (row > 0 && col > text.getLine(row).length())) cur.setOffset(0);
    }
    rCursor();
  }

 /**
  * Handles the specified key event.
  * @param e the specified key event to be handle.
  * @return <code>true</code> if the key event has been handled with the method; otherwise
  * <code>false</code>.
  */
  protected /*C#virtual*/ void handleKey(LwKeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case LwToolkit.VK_DOWN      : cur.seekLineTo(PosController.DOWN); break;
      case LwToolkit.VK_UP        : cur.seekLineTo(PosController.UP); break;
      case LwToolkit.VK_RIGHT     : cur.seek(1);  break;
      case LwToolkit.VK_LEFT      : cur.seek(-1); break;
      case LwToolkit.VK_END       : cur.seekLineTo(PosController.END); break;
      case LwToolkit.VK_HOME      : cur.seekLineTo(PosController.BEG); break;
      case LwToolkit.VK_PAGE_DOWN : cur.seekLineTo(PosController.DOWN, pageSize()); break;
      case LwToolkit.VK_PAGE_UP   : cur.seekLineTo(PosController.UP, pageSize()); break;
      case LwToolkit.VK_DELETE    : remove(cur.getOffset(), 1); break;
      case LwToolkit.VK_BACK_SPACE: remove(cur.getOffset()-1, 1); break;
    }
  }

 /**
  * Returns <code>true</code> if the key event should be handle next, returns
  * <code>false</code> if the handling process has to be terminated. The method
  * is called before any other event handler will be executed.
  * @param e the specified key event.
  * @return <code>true</code> if the key event should be handled.
  */
  protected /*C#virtual*/ boolean isFiltered(LwKeyEvent e)
  {
    int code = e.getKeyCode();
    return code == LwToolkit.VK_SHIFT || code == LwToolkit.VK_CONTROL ||
           code == LwToolkit.VK_TAB   || code == LwToolkit.VK_ALT ||
           (e.getMask() & LwToolkit.ALT_MASK) > 0;
  }

  protected /*C#override*/ LwTextRender makeTextRender(TextModel t) {
    return new LwTextRender(t);
  }

 /**
  * Removes a part of the text starting from the given position and with the specified size.
  * @param pos the given position.
  * @param size the specified removed part size.
  */
  public /*C#virtual*/ void remove(int pos, int size)
  {
    if (isEditableVal)
    {
      int max = cur.getMaxOffset();
      int pl  = getLines();
      if (pos >= 0 && (pos + size) <= max)
      {
        int old = cur.getOffset();
        cur.setOffset(pos);
        getTextModel().remove(pos, size);
        if (getLines() != pl || old == pos) repaint();
      }
    }
  }

 /**
  * Inserts the specified text at the given position.
  * @param pos the given position.
  * @param s the specified text to be inserted.
  */
  public /*C#virtual*/ void write(int pos, String s)
  {
    if (isEditableVal)
    {



      int old = cur.getOffset();
      int pl  = getLines();

      //!!!
      if (!LwInputController.controller.getInsertMode()) remove (pos, 1);

      getTextModel().write(s, pos);


      //!!!
      if (LwInputController.controller.getInsertMode()) cur.seek (s.length());

      if (getLines() != pl || cur.getOffset() == old) repaint();
    }
  }

 /**
  * Gets the page size.
  * @return a page size.
  */
  protected /*C#virtual*/ int pageSize()
  {
    int height = this.height - getTop() - getBottom();
    LwTextRender render = getTextRender();
    int indent     = render.getLineIndent();
    int textHeight = render.getLineHeight();
    return (height + indent)/(textHeight + indent) +
           (((height + indent)%(textHeight + indent)>indent)?1:0);
  }

  private void rCursor()
  {
    if (cur.getOffset() >=0)
    {
      Point  p = getTextLocationAt(getTextRender(), cur);
      curX = p.x;
      curY = p.y;
    }

    if (isEditable())
    {
      curW = 2;
      curH = getTextRender().getLineHeight() - 1;
    }
    else
    {
      curW = 0;
      curH = 0;
    }
  }

  /**
   * Calculates and gets a pixel location for the specified text render and the text
   * cursor position.
   * @param render the specified text render.
   * @param pos the pos controller that defines the text position.
   * @return a pixel location.
   */
   public static Point getTextLocationAt(LwTextRender render, PosController pos)
   {
     if (pos.getOffset() < 0) return null;
     int cl = pos.getCurrentLine();
     return new Point(render.substrWidth(render.getLine(cl), 0, pos.getCurrentCol()),
                      cl * (render.getLineHeight() + render.getLineIndent()));
   }

  /**
   * Calculates and gets a text position for the specified text render and the location.
   * The result is represented with org.zaval.port.j2me.Point class where <code>x</code> field defines
   * a row and <code>y</code> field defines a column.
   * @param render the specified text render.
   * @param x the x coordinate of the location.
   * @param y the y coordinate of the location.
   * @return a text position.
   */
   public static Point getTextRowColAt(LwTextRender render, int x, int y)
   {
     int size = render.getTextModel().getSize();
     if (size == 0) return null;

     int lineHeight = render.getLineHeight();
     int lineIndent = render.getLineIndent();
     int lineNumber = (y<0)?0:(y  + lineIndent)/(lineHeight + lineIndent) +
                      ((y  + lineIndent)%(lineHeight + lineIndent)>lineIndent?1:0) - 1;

     if (lineNumber >= size) return new Point (size - 1, render.getLine(size - 1).length());
     else
     if (lineNumber < 0) return new Point();

     if (x < 0) return new Point(lineNumber, 0);

     int x1 = 0, x2 = 0;
     String s = render.getLine(lineNumber);
     for(int c = 0; c < s.length(); c++)
     {
       x1 = x2;
       x2 = render.substrWidth(s, 0, c + 1);
       if (x >= x1 && x < x2) return new Point(lineNumber, c);
     }
     return new Point (lineNumber, s.length());
   }
}

