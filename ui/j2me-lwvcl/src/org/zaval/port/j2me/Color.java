package org.zaval.port.j2me;

public class Color
{
  public static final  Color gray      = new Color(128, 128, 128);
  public static final  Color darkGray  = new Color(64, 64, 64);
  public static final  Color lightGray = new Color(192, 192, 192);
  public static final  Color black     = new Color(0,0,0);
  public static final  Color white     = new Color(255, 255, 255);
  public static final  Color red       = new Color(255, 0, 0);
  public static final  Color blue      = new Color(0, 0, 255);
  public static final  Color yellow    = new Color(255, 255, 0);
  public static final  Color pink      = new Color(255, 175, 175);
  public static final  Color orange    = new Color(255, 200, 0);
  public static final  Color green     = new Color(0, 255, 0);
  public static final  Color magenta   = new Color(255, 0, 255);
  public static final  Color cyan      = new Color(0, 255, 255);

  private int color;

  public Color(int r, int g, int b) {
    this ((r<<16 | g << 8 | b));
  }

  public Color(int c) {
    color = c & 0xFFFFFF;
  }

  public int getRed() {
    return 0xFF & (color >> 16);
  }

  public int getGreen() {
    return 0xFF & (color >> 8);
  }

  public int getBlue() {
    return 0xFF & color;
  }

  public int getRGB() {
    return color;
  }

  public boolean equals(Object o) {
    if (o instanceof Color) return color == ((Color)o).getRGB();
    return super.equals(o);
  }
}

