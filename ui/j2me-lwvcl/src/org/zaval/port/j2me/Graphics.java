package org.zaval.port.j2me;

import javax.microedition.lcdui.*;

public class Graphics
{
  public javax.microedition.lcdui.Graphics target;

  public Graphics(javax.microedition.lcdui.Graphics g)  {
    target = g;
  }

  public void setTarget(javax.microedition.lcdui.Graphics g) {
    target = g;
  }

  public final Font getFont () {
    return target.getFont();
  }

  public final FontMetrics getFontMetrics () {
    return new FontMetrics(target.getFont());
  }

  public final FontMetrics getFontMetrics (Font f) {
    return new FontMetrics(f);
  }

  public final Color getColor () {
    return new Color (target.getColor());
  }

  public final void setColor (Color c) {
    target.setColor(c.getRGB());
  }

  public final void setFont (Font f) {
    target.setFont(f);
  }

  public final void drawLine (int x1, int y1, int x2, int y2) {
    target.drawLine(x1, y1, x2, y2);
  }

  public final void drawRect (int x, int y, int w, int h) {
    target.drawRect(x, y, w, h);
  }

  public final void drawImage (Image img, int x, int y, Object observer) {
    target.drawImage(img, x, y, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
  }

  public final void drawImage (Image img, int x, int y, int w, int h, Object observer) {
    //!!! Stretching is not supported
    target.drawImage(img, x, y, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
  }

  public final void drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
                                     int sx1, int sy1, int sx2, int sy2, Object observer)
  {
    //!!! Stretching is not supported
    target.drawRegion(img, sx1, sy1, sx2 - sx1, sy2 - sy1, javax.microedition.lcdui.game.Sprite.TRANS_NONE,
                      dx1, dy1, javax.microedition.lcdui.Graphics.TOP | javax.microedition.lcdui.Graphics.LEFT);
  }

  public final void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
    target.drawArc(x, y, w, h, startAngle, arcAngle);
  }

  public final void drawString(String str, int x, int y) {
    target.drawString(str, x, y, javax.microedition.lcdui.Graphics.BASELINE | javax.microedition.lcdui.Graphics.LEFT);
  }

  public final void fillRect(int x, int y, int w, int h) {
    target.fillRect(x, y, w-1, h-1);
  }

  public final void clearRect(int x, int y, int w, int h) {
    //???
     target.fillRect(x, y, w, h);
  }

  public final void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
    target.fillArc(x, y, w, h, startAngle, arcAngle);
  }

  public final void setClip(int x, int y, int w, int h) {
    target.setClip(x, y, w, h);
  }

  public final void setClip(Rectangle r) {
    target.setClip(r.x, r.y, r.width, r.height);
  }

  public final void clipRect(int x, int y, int w, int h) {
    target.clipRect(x, y, w, h);
  }

  public final Rectangle getClipBounds ()  {
    return new Rectangle(target.getClipX(), target.getClipY(),
                         target.getClipWidth(), target.getClipHeight());
  }

  public final void translate (int dx, int dy) {
    target.translate (dx, dy);
  }

  public final int getClipX() {
    return target.getClipX();
  }

  public final int getClipY() {
    return target.getClipY();
  }

  public final int getClipWidth() {
    return target.getClipWidth();
  }

  public final int getClipHeight() {
    return target.getClipHeight();
  }
}

