package org.zaval.data;

import java.util.*;
import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is an implementation of the <code>TextModel</code> interface to support multi line text.
 * The lines are supposed to be terminated by the line feed ('\n').
 */
public class Text
implements TextModel
{
  private static final int EXTRA_SIZE = 1;

  private Vector lines;
  private int    textLength;
  private TextListenerSupport support;

 /**
  * Constructs a new text with the given text.
  * @param s the initial value of the text.
  */
  public Text(String s) {
    setText(s==null?"":s);
  }

 /**
  * Returns the line at the specified line number.
  * @param line the specified line number.
  * @return a line at the specified line number.
  */
  public String getLine (int line) {
    char[] buf = (char[])lines.elementAt(line);
    return new String(buf, 0, buf.length - EXTRA_SIZE);
  }

 /**
  * Sets the specified text data. The method performs the text data parsing process.
  * The process decides how the text data have to be divided into lines, the implementation
  * uses "\n" character as the text line separator.
  * @param text the text data.
  */
  public void setText (String text)
  {
    String old = getText();
    if (old == null || !old.equals(text))
    {
      if (old != null)
      {
        TextEvent e = new TextEvent(this, TextEvent.REMOVED, 0, getTextLength(), getSize());
        e.setUpdatedLines(0, 0);
        lines.removeAllElements();
        perform (e);
      }
      lines = parse(text);
      textLength = text.length();
      TextEvent ee = new TextEvent(this, TextEvent.INSERTED, 0, getTextLength(), 0);
      ee.setUpdatedLines(0, getSize());
      perform(ee);
    }
  }

 /**
  * Returns the original text data that have been set with <code>setText</code> method.
  * @return an original text data.
  */
  public String getText ()
  {
    if (lines == null || lines.size() == 0) return null;
    StringBuffer buf = new StringBuffer();
    for (int i=0; i<lines.size(); i++)
    {
      if (i > 0) buf.append ('\n');
      char[] ch = (char[])lines.elementAt(i);
      buf.append (ch, 0, ch.length - EXTRA_SIZE);
    }
    return buf.toString();
  }

 /**
  * Returns the line number of the text.
  * @return a line number.
  */
  public int getSize() {
    return (lines == null)?0:lines.size();
  }

 /**
  * Inserts the specified text at the given offset. The offset has to be less than the text
  * length. The method performs re-parsing of the text.
  * @param s the text to be inserted.
  * @param offset the offset where the text will be inserted.
  */
  public void write (String s, int offset)
  {
    int    len     = s.length();
    int[]  info    = getLnInfo(0, 0, offset);

    char[] line    = (char[])lines.elementAt(info[0]);
    int    length  =  line.length - EXTRA_SIZE;

    char[] tmp      = new char[length + s.length() + EXTRA_SIZE];
    int    lnOffset = offset - info[1];
    System.arraycopy(line, 0, tmp, 0, lnOffset);
    System.arraycopy(s.toCharArray(), 0, tmp, lnOffset, len);
    System.arraycopy(line, lnOffset, tmp, lnOffset + len, length - lnOffset);

    TextEvent e = new TextEvent(this, TextEvent.INSERTED, offset, s.length(), getSize());
    if (s.indexOf ('\n') < 0)
    {
      lines.setElementAt(tmp, info[0]);
      e.setUpdatedLines(info[0], 1);
    }
    else
    {
      lines.removeElementAt(info[0]);
      Vector v = parse(new String(tmp, 0, tmp.length - EXTRA_SIZE));
      for (int i=0; i<v.size(); i++)
        lines.insertElementAt(v.elementAt(i), info[0] + i);
      e.setUpdatedLines(info[0], v.size());
    }

    textLength += len;
    perform(e);
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
    int    lns = getSize();
    int[]  i1 = getLnInfo (0, 0, offset);
    int[]  i2 = getLnInfo (i1[0], i1[1], offset + size);

    char[] line2 = (char[])lines.elementAt(i2[0]);
    int    l1 = offset - i1[1];
    int    l2 = line2.length - size - offset + i2[1];
    char[] buf = new char[l1 + l2];

    System.arraycopy((char[])lines.elementAt(i1[0]), 0, buf, 0, l1);
    System.arraycopy(line2, offset + size - i2[1], buf, l1, l2);

    for (int i=i1[0]; i<i2[0]+1; i++) lines.removeElementAt(i1[0]);
    lines.insertElementAt(buf, i1[0]);

    textLength -= size;
    TextEvent e = new TextEvent(this, TextEvent.REMOVED, offset, size, lns);
    e.setUpdatedLines(i1[0], 1);
    perform(e);
  }

 /**
  * Parses the specified string and returns a vector of the text lines.
  * The method determines how the text has to be divided to the string lines.
  * @param buffer the specified string.
  * @return a vector of the strings that are represented as character arrays.
  */
  protected /*C#virtual*/ Vector parse(String buffer)
  {
    int    size = buffer.length(), offset = 0;
    Vector v = new Vector(3);
    for (int index=0; index >=0; offset = index + 1)
    {
      index = buffer.indexOf ('\n', offset);
      char[] line = new char[(index>=0?index:size) - offset + EXTRA_SIZE];
      buffer.getChars(offset, (index >= 0?index:size), line, 0);
      v.addElement(line);
    }
    return v;
  }

 /**
  * Fires the specified text event to all text listeners.
  * @param e the text event that has to be fired.
  */
  void perform (TextEvent e) {
    if (support != null) support.perform(e);
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
    return textLength;
  }

  public int getExtraChar (int i) {
    char[] line = (char[])lines.elementAt(i);
    return (int) line[line.length-1];
  }

  public void setExtraChar (int i, int ch) {
    char[] line = (char[])lines.elementAt(i);
    line[line.length-1] = (char)ch;
  }

  private int[] getLnInfo (int start, int startOffset, int o)
  {
    for (; start<lines.size(); start++)
    {
      char[] buf = (char[])lines.elementAt(start);
      if (o >= startOffset && o <= startOffset + buf.length - EXTRA_SIZE) return new int[] { start, startOffset };
      startOffset += (buf.length + 1 - EXTRA_SIZE);
    }
    return null;
  }
}

