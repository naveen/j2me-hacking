package org.zaval.data;

import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is an implementation of the <code>TextModel</code> interface. It is used to
 * represent single line text, it means that in any case the specified input string is
 * parsed to single line (you can use '\n' delimiter, but it will not have effect)
 * text.
 */
public class SingleLineTxt
implements TextModel
{
  private StringBuffer buf;
  private int          maxLen, extra;

  private TextListenerSupport support;

 /**
  * Constructs a new text with the given text.
  * @param s the initial value of the text.
  */
  public SingleLineTxt(String s) {
    this(s, -1);
  }

 /**
  * Constructs a new text with the given text and the specified maximal length.
  * @param s the initial value of the text.
  * @param max the specified maximal length.
  */
  public SingleLineTxt(String s, int max) {
    maxLen = max;
    setText(s==null?"":s);
  }

 /**
  *
  * Returns the line at the specified line number.
  * @param line the specified line number. The implementation doesn't use
  * the argument, since this is single line implementation.
  * @return a line at the specified line number.
  */
  public String getLine (int line) {
    return buf==null?"":buf.toString();
  }

 /**
  * Sets the specified text data. The method performs the text data parsing process.
  * The process decides how the text data have to be divided into lines. The implementation
  * uses the input text as is.
  * @param text the text data.
  */
  public void setText (String text)
  {
    String old = getText();
    if (old == null || !old.equals(text))
    {
      if (old != null)
      {
        buf = null;
        perform (TextEvent.REMOVED, 0, getTextLength());
      }

      buf = (maxLen > 0 && text.length() > maxLen)? new StringBuffer(text.substring(0, maxLen))
                                                  : new StringBuffer(text);
      perform(TextEvent.INSERTED, 0, getTextLength());
    }
  }

 /**
  * Returns the original text data that have been set with <code>setText</code> method.
  * @return an original text data.
  */
  public String getText () {
    return (buf == null)?null:buf.toString();
  }

 /**
  * Returns the line number of the text.
  * @return a line number.
  */
  public int getSize() {
    return (buf==null)?0:1;
  }

 /**
  * Inserts the specified text at the given offset. The offset has to be less than the text
  * length. The method performs re-parsing of the text.
  * @param s the text to be inserted.
  * @param offset the offset where the text will be inserted.
  */
  public void write (String s, int offset)
  {
    if (maxLen <= 0 || getTextLength() + s.length() <= maxLen)
    {
      buf.insert(offset, s);
      perform(TextEvent.INSERTED, offset, s.length());
    }
  }

 /**
  * Inserts the specified character at the given offset. The offset has to be less than the text
  * length. The method performs re-parsing of the text.
  * @param ch the character to be inserted.
  * @param offset the offset where the character will be inserted.
  */
  public void write (char ch, int offset) {
    write (String.valueOf(ch), offset);
  }

 /**
  * Removes a text at the specified offset with the size. The offset and the offset plus the
  * size have to be less than the text length.
  * @param offset the offset where the text will be removed.
  * @param size the size of the part that is going to be removed.
  */
  public void remove (int offset, int size)
  {
    String s = buf.toString();
    buf = new StringBuffer(s.substring(0, offset).concat(s.substring(offset + size)));
    perform(TextEvent.REMOVED, offset, size);
  }

 /**
  * Fires the specified text event to all text listeners. The text event is created
  * with the method using the specified <code>id</code>,<code>offset</code> and
  * <code>size</code>.
  * @param id the specified event id.
  * @param offset the specified offset.
  * @param size the specified size.
  */
  void perform (int id, int offset, int size)
  {
    if (support != null)
    {
      TextEvent e = new TextEvent(this, id, offset, size, 1);
      e.setUpdatedLines(0, 1);
      support.perform(e);
    }
  }

 /**
  * Adds the specified text listener.
  * @param l the text listener.
  */
  public void addTextListener(TextListener l) {
    if (support == null) support = new TextListenerSupport();
    support.addListener(l);
  }

 /**
  * Removes the specified text listener.
  * @param l the text listener.
  * @see      org.zaval.data.event.TextListener
  * @see      org.zaval.data.event.TextEvent
  */
  public void removeTextListener(TextListener l) {
    if (support != null) support.removeListener(l);
  }

 /**
  * Returns the text length.
  * @return a text length.
  */
  public int getTextLength() {
    return (buf == null)?-1:buf.length();
  }

  public int getExtraChar (int i) {
    return extra;
  }

  public void setExtraChar (int i, int ch) {
    extra = ch;
  }
}

