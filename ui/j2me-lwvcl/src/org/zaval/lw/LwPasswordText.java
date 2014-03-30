package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class inherits LwTextRender to represent a password text. For the purpose the
 * render uses an echo character.
 */
public class LwPasswordText
extends LwTextRender
{
  private char echo = '*';

 /**
  * Constructs the render with the specified target text model.
  * @param text the specified target text model.
  */
  public LwPasswordText(TextModel text) {
    super(text);
  }

 /**
  * Sets the specified echo character. The symbol will be used to render the text,
  * it means that every character of the text is render with the echo character.
  * @param ch the specified echo character.
  */
  public void setEchoChar (char ch)
  {
    if (echo != ch)
    {
      echo = ch;
      invalidate();
    }
  }

 /**
  * Gets the string presentation of the specified line. The method is overridden with
  * the class and it replaces every character of the given string with the echo character
  * and after that returns the string as a result.
  * @param r the specified line number.
  * @return a string line.
  */
  protected /*C#override*/ String getLine(int r)
  {
    String s = super.getLine(r);
    char[] buf = new char[s.length()];
    for (int i=0; i < buf.length; i++) buf[i] = echo;
    return new String(buf);
  }

  protected /*C#override*/ void paintSelection(Graphics g, int x, int y, int line, Layoutable d) {}
}



