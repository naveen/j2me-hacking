package org.zaval.port.j2me;

public class Insets
{
  public int top, left, right, bottom;

  public Insets() {
    this(0, 0, 0, 0);
  }

  public Insets(int t, int l, int b, int r)
  {
    top    = t;
    left   = l;
    right  = r;
    bottom = b;
  }
}


