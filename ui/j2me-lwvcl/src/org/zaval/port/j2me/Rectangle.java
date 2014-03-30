package org.zaval.port.j2me;

public class Rectangle
{
  public int x, y, width, height;

  public Rectangle() {
    this(0, 0, 0, 0);
  }

  public Rectangle(int x, int y, int w, int h)
  {
    this.x = x;
    this.y = y;
    width = w;
    height = h;
  }

  public boolean contains(int xx, int yy) {
    return (xx >= x) && ((xx - x) < width) && (yy >= y) && ((yy-y) < height);
  }

  public Rectangle intersection(Rectangle r)
  {
    int x1 = Math.max(x, r.x);
    int x2 = Math.min(x + width, r.x + r.width);
    int y1 = Math.max(y, r.y);
    int y2 = Math.min(y + height, r.y + r.height);
    return new Rectangle(x1, y1, x2 - x1, y2 - y1);
  }

  public boolean intersects(Rectangle r) {
    return !((r.x + r.width <= x) ||
             (r.y + r.height <= y) ||
             (r.x >= x + width) ||
             (r.y >= y + height));
  }

  public String toString () {
    return "[x = " + x + ",y = " + y + ",w = " + width + ",h = " + height + "]";
  }
}


