package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;


/**
 * This is text render.
 * The render is used to paint a text that is represented by the org.zaval.data.TextModel
 * target object. The data class has not any connections with painting or light weight functionality,
 * it just provides handy interface to manipulate text data. The render defines set of properties that
 * are bound with the view:
 * <ul>
 *   <li>Font to show the text</li>
 *   <li>Foreground color to define the text color</li>
 *   <li>Text line indent</li>
 * </ul>
 * <p>
 * The render listens when the text has been modified to control its validation state, so it is not
 * necessary to synchronize the render validation state with the modifying the text data.
 * <p>
 * The render overrides the <code>ownerChanged</code> method to have reference to a light weight
 * component that uses the render. The reference is used to synchronize validation status of
 * the light weight component with the render. It means if the render is invalidated than
 * the component (owner) will be invalidated too.
 * <p>
 * The render has ability to paint selection basing on the information provided by the owner
 * component. In this case the owner component should implement the <code>TxtSelectionInfo</code>
 * interface.
 */
public class LwTextRender
extends LwRender
implements TextListener
{
  private FontMetrics fontMetrics;
  private int         textWidth, textHeight;
  private Color       fore;
  private int         startLine, lines;
  private Validationable owner;

 /**
  * Constructs the render with the specified target string.
  * The constructor creates org.zaval.data.Text basing on the target string.
  * @param text the specified target string.
  */
  public LwTextRender(String text) {
    this(new Text(text));
  }

 /**
  * Constructs the render with the specified target text model.
  * @param text the specified target text model.
  */
  public LwTextRender(TextModel text) {
    super(text);
  }

 /**
  * Gets line height.
  * @return a line height.
  */
  public int getLineHeight() {
    return getFontMetrics().getHeight();
  }

 /**
  * Sets the font for the text render. The font is used to render the text.
  * @param f the font.
  */
  public void setFont(Font f)
  {
    Font old = getFont();
    if (f != old && (f == null || !f.equals(old)))
    {
      fontMetrics = LwToolkit.getFontMetrics(f);
      invalidate(0, getTextModel().getSize());
    }
  }

 /**
  * Gets the font of the text render.
  * @return a font.
  */
  public Font getFont() {
    return fontMetrics == null?(Font)LwToolkit.getStaticObj("def.font"):fontMetrics.getFont();
  }

 /**
  * Gets the font metrics of the text render.
  * @return a font metrics.
  */
  protected FontMetrics getFontMetrics() {
    return fontMetrics == null?LwToolkit.FONT_METRICS:fontMetrics;
  }

 /**
  * Gets the foreground color. The color is used as the text color.
  * @return a foreground color.
  */
  public Color getForeground() {
    return fore == null?(Color)LwToolkit.getStaticObj("def.fgcolor"):fore;
  }

 /**
  * Sets the foreground color that is used as the text color to render the target text.
  * @param c the foreground color. Use <code>null</code> to set the default
  * foreground color.
  */
  public void setForeground(Color c) {
    if (c != fore && (c == null || !c.equals(fore)))
      fore = c;
  }

 /**
  * Gets the line indent.
  * @return a line indent.
  */
  public /*C#virtual*/ int getLineIndent() {
    return 1;
  }

 /**
  * Invoked whenever the target object has been changed.
  * The method is overridden to register and un-register the render as a text events listener
  * for the target object.
  * @param o the old target object.
  * @param n the new target object.
  */
  protected /*C#override*/ void targetWasChanged(Object o, Object n)
  {
    if (o != null) ((TextModel)o).removeTextListener(this);
    if (n != null)
    {
      ((TextModel)n).addTextListener(this);
      invalidate(0, getTextModel().getSize());
    }
  }

 /**
  * Calculates and returns the render preferred size. The method doesn't use
  * the view insets to compute the preferred size.
  * @return a "pure" preferred size of the view.
  */
  protected /*C#override*/ Dimension calcPreferredSize() {
    return new Dimension(textWidth, textHeight);
  }

 /**
  * Gets the target object as the text model.
  * @return a text model.
  */
  public TextModel getTextModel() {
    return (TextModel)getTarget();
  }

 /**
  * Gets the target text as a string.
  * @return a string presentation of the target text.
  */
  public String getText() {
    TextModel text = getTextModel();
    return text == null?null:text.getText();
  }

 /**
  * Invalidates the render. If the render has been registered as a view of a lightweight
  * component than the owner component will be invalidated too.
  */
  public /*C#override*/ void invalidate() {
    if (owner != null) owner.invalidate();
    super.invalidate();
  }

 /**
  * Invoked with <code>validate</code> method if the render is invalid. The method
  * is overridden to calculate size of the target text according to the font and indent.
  */
  protected /*C#override*/ void recalc ()
  {
    TextModel text = getTextModel();
    if (text != null)
    {
      if (lines > 0)
      {
        for (int i=startLine; i<startLine + lines; i++)
          text.setExtraChar(i, stringWidth(text.getLine(i)));
        lines = 0;
      }

      int lineHeight = getLineHeight();
      int max = 0, size = text.getSize();
      for (int i=0; i<size; i++)
      {
        int len = text.getExtraChar(i);
        if (len > max) max = len;
      }
      textWidth  = max;
      textHeight = lineHeight * size + (size - 1) * getLineIndent();
    }
  }

 /**
  * Invoked when a part of the target text has been removed.
  * @param e the text event.
  */
  public void textRemoved(TextEvent e)
  {
    int sl = e.getFirstUpdatedLine(), ul = e.getUpdatedLines();
    int rl = e.getPrevLines() - getTextModel().getSize();
    if (rl > 0 && lines > 0 && sl + 1 < startLine + lines)
    {
       int sd = sl + 1;
       int nl = Math.max(sd, startLine);
       int ns = Math.min(sd + rl, startLine + lines) - nl;

       // Check if there is no any intersection
       if (ns <= 0)
       {
         if (sd < startLine) startLine -= rl;
       }
       if (ns >= lines)  lines = 0;
       else
       {
         lines -= ns;
         if (sd <= startLine) startLine = sd;
       }
    }
    invalidate(sl, ul);
  }

 /**
  * Invoked when a part of the target text has been updated.
  * @param e the text event.
  */
  public void textUpdated(TextEvent e) {
    invalidate(e.getFirstUpdatedLine(), e.getUpdatedLines());
  }

 /**
  * Invoked when a new text has been inserted in the target text.
  * @param e the text event.
  */
  public void textInserted (TextEvent e)
  {
    int ln = e.getUpdatedLines(), first = e.getFirstUpdatedLine();
    if (ln > 1 && lines > 0)
    {
      if (first <= startLine) startLine += (ln - 1);
      else
      if (first < startLine + lines) lines += (ln - 1);
    }
    invalidate(first, ln);
  }

 /**
  * Invalidates the given lines number starting form the specified line.
  * @param start the starting line number.
  * @param size the number of lines to be invalidated.
  */
  protected /*C#virtual*/ void invalidate(int start, int size)
  {
    if (size > 0 && (startLine != start || size != lines))
    {
      if (lines == 0)
      {
        startLine = start;
        lines     = size;
      }
      else
      {
        int e1 = start     + size;
        int e2 = startLine + lines;
        startLine = Math.min(start, startLine);
        lines     = Math.max(e1, e2) - startLine;
      }
    }
    invalidate();
  }

 /**
  * Invoked to render a line with the specified index, at the given location, graphics context and
  * owner component. The method can be overridden to provide another line painting algorithm.
  * @param g the graphics context.
  * @param x the x coordinate of the text line location.
  * @param y the y coordinate of the text line location.
  * @param line the specified line index.
  * @param d the specified owner component that uses the render.
  */
  protected /*C#virtual*/ void paintLine(Graphics g, int x, int y, int line, Object d) {
    g.drawString(getLine(line), x, y + getAscent());
  }

 /**
  * Renders the text model using the specified graphics context, location, size and owner
  * component.
  * @param g the graphics context.
  * @param x the x coordinate of the text location.
  * @param y the y coordinate of the text location.
  * @param w the specified width.
  * @param h the specified height.
  * @param d the specified owner component that uses the render.
  */
  public /*C#override*/ void paint(Graphics g, int x, int y, int w, int h, Object d)
  {
     int clipY = g.getClipY(), clipW = g.getClipWidth(), clipH = g.getClipHeight();
     if (clipW > 0 && clipH > 0)
     {
       int lineIndent = getLineIndent();
       int lineHeight = getLineHeight();

       w = Math.min(clipW, w);
       h = Math.min(clipH, h);

       int startLine = -1;
       if (y < clipY)
       {
         startLine = (lineIndent + clipY - y)/(lineHeight + lineIndent);
         h += (clipY - startLine * lineHeight - startLine * lineIndent);
       }
       else
       if (y > (clipY + clipH)) return;
       else startLine = 0;

       int size = getTextModel().getSize();
       if (startLine < size)
       {
         int lines = (h + lineIndent)/(lineHeight + lineIndent) +
                    (((h + lineIndent)%(lineHeight + lineIndent)>lineIndent)?1:0);
         if (startLine + lines > size) lines = size - startLine;
         y += startLine*(lineHeight + lineIndent);

         g.setFont (getFont());
         for (int i=0; i<lines; i++)
         {
           g.setColor(getForeground());
           paintLine(g, x, y, i + startLine, d);
           y += (lineIndent + lineHeight);
         }
       }
     }
  }

 /**
  * Gets the text font ascent. The font ascent is the distance from the base line
  * to the top of most Alphanumeric characters. Note, however, that some characters
  * in the font may extend above this height.
  * @return a text font ascent.
  */
  public int getAscent() {
    return getFontMetrics().getAscent();
  }

 /**
  * Returns the total advance width for showing the specified line number.
  * The advance width is the amount by which the current point is moved from one
  * character to the next in a line of the target text model.
  * @param line the specified line number.
  * @return a string width.
  */
  public /*C#virtual*/ int lineWidth (int line) {
    validate();
    return getTextModel().getExtraChar(line);
  }

 /**
  * Returns the total advance width for showing the specified string.
  * The advance width is the amount by which the current point is moved from one
  * character to the next in a line of the target text model.
  * @param s the specified string.
  * @return a string width.
  */
  public /*C#virtual*/ int stringWidth (String s) {
    return getFontMetrics().stringWidth(s);
  }

 /**
  * Returns the total advance width for showing the specified substring.
  * @param s the specified string.
  * @param off the specified starting offset of the substring.
  * @param len the specified length of the substring.
  * @return a string width.
  */
  public /*C#virtual*/ int substrWidth (String s, int off, int len) {
    return getFontMetrics().charsWidth(s.toCharArray(), off, len);
  }

 /**
  * Gets the string presentation of the specified line.
  * @param r the specified line number.
  * @return a string line.
  */
  protected /*C#virtual*/ String getLine(int r) {
    return getTextModel().getLine(r);
  }

 /**
  * The method is called whenever the view owner has been changed.
  * The render overrides the method to store owner component reference. The reference
  * is necessary to synchronize validation status of the render with the owner.
  * @param v the new owner component.
  */
  protected /*C#override*/ void ownerChanged(Validationable v) {
    owner = v;
  }
}


