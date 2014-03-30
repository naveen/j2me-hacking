package org.zaval.port.j2me;

import javax.microedition.lcdui.*;

public class FontMetrics
{
  private Font relFont;

  public FontMetrics(Font f) {
    relFont = f;
  }

  public int getHeight() {
    return relFont.getHeight();
  }

  public Font getFont() {
    return relFont;
  }

  public int getAscent() {
    return relFont.getBaselinePosition();
  }

  public final int stringWidth(String s) {
    return relFont.stringWidth(s);
  }

  public final int charsWidth(char[] ch, int off, int len) {
    return relFont.charsWidth(ch, off, len);
  }
}


