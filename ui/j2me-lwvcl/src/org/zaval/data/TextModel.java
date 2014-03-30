package org.zaval.data;

import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The interface is used to represent text data. Actually the interface
 * determines following three abilities to work with a text:
 * <ul>
 *   <li>
 *     Get the text properties. It is possible to get text lines, text size and so on
 *     using appropriate methods of the interface.
 *   </li>
 *   <li>
 *     Modify text data. It is possible to write and remove text data using appropriate
 *     methods of the interface.
 *   </li>
 *   <li>
 *     Listen text events. The interface provides the <code>addTextListener</code> and
 *     <code>removeTextListener</code> methods to listen the text events.
 *   </li>
 * </ul>
 */
public interface TextModel
{
 /**
  * Returns the line at the specified line number.
  * @param line the specified line number.
  * @return a line at the specified line number.
  */
  String getLine (int line);

 /**
  * Sets the specified text data. As rule the method should perform parsing process.
  * The process decides how the text data have to be divided into lines.
  * @param text the specified text data.
  */
  void setText (String text);

 /**
  * Returns the original text data that have been set with <code>setText</code> method.
  * @return an original text data.
  */
  String getText ();

 /**
  * Returns the number of lines for the text model.
  * @return a number of lines.
  */
  int getSize ();

 /**
  * Inserts the specified text at the given offset. The offset has to be less than the text
  * length. Actually the method performs re-parsing of the text.
  * @param s the text to be inserted.
  * @param offset the offset where the text will be inserted.
  */
  void write  (String s, int offset);

 /**
  * Inserts the specified character at the given offset. The offset has to be less than the text
  * length. Actually the method performs re-parsing of the text.
  * @param ch the character to be inserted.
  * @param offset the offset where the character will be inserted.
  */
  void write  (char ch, int offset);

 /**
  * Removes a text at the specified offset with the size. The offset and the offset plus the
  * size have to be less than the text length.
  * @param offset the offset where the text will be removed.
  * @param size the size of the part that is going to be removed.
  */
  void remove (int offset, int size);

 /**
  * Returns the text length (number of the text characters).
  * @return a text length.
  */
  int getTextLength();

 /**
  * Adds the specified text listener to receive text events.
  * @param l the text listener.
  * @see      org.zaval.data.event.TextListener
  * @see      org.zaval.data.event.TextEvent
  */
  void addTextListener(TextListener l);

 /**
  * Removes the specified text listener.
  * @param l the text listener.
  * @see      org.zaval.data.event.TextListener
  * @see      org.zaval.data.event.TextEvent
  */
  void removeTextListener(TextListener l);


 /**
  * Gets special extra char that is used to store extra information by the specified index.
  * The method is deprecated to be used, because it will be probably re-designed in the future.
  * @param i the specified extra char index.
  * @return an extra char value.
  */
  int getExtraChar (int i);

 /**
  * Sets special extra char that is used to store extra information by the specified index.
  * The method is deprecated, since probably it will  be re-designed in the future.
  * @param i the specified extra char index.
  * @param val the specified extra char value.
  */
  void setExtraChar (int i, int val);
}
