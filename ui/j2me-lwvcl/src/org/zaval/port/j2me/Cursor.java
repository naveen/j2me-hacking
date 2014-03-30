package org.zaval.port.j2me;

public class Cursor
{
  public final static int HAND_CURSOR      = 1;
  public final static int W_RESIZE_CURSOR  = 2;
  public final static int N_RESIZE_CURSOR  = 3;
  public final static int S_RESIZE_CURSOR  = 4;
  public final static int E_RESIZE_CURSOR  = 5;
  public final static int SE_RESIZE_CURSOR = 6;
  public final static int SW_RESIZE_CURSOR = 7;
  public final static int NE_RESIZE_CURSOR = 8;
  public final static int NW_RESIZE_CURSOR = 9;
  public final static int TEXT_CURSOR      = 10;
  public final static int WAIT_CURSOR      = 11;
  public final static int MOVE_CURSOR      = 12;
  public final static int CROSSHAIR_CURSOR = 13;
  public final static int DEFAULT_CURSOR   = 14;

  private int cursor;

  public Cursor() {
    this(DEFAULT_CURSOR);
  }

  public Cursor(int c) {
    cursor = c;
  }

  public int getType () {
    return cursor;
  }

  public static Cursor getDefaultCursor () {
    return new Cursor (Cursor.DEFAULT_CURSOR);
  }
}

