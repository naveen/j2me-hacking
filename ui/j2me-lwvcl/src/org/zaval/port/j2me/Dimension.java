package org.zaval.port.j2me;

public class Dimension
{
  public int width, height;

  public Dimension() {
    this(0, 0);
  }

  public Dimension(int w, int h) {
    width  = w;
    height = h;
  }

  public Dimension(Dimension d) {
    this(d.width, d.height);
  }

  public String toString () {
    return "[w = " + width + ",h = " + height + "]";
  }
}


