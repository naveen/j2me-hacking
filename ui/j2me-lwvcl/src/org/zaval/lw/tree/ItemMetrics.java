package org.zaval.lw.tree;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is tree view node descriptor class. The class provides set of metrical characteristics
 * for a node of the tree component. Mainly, the class is used for internal purposes.
 */
public class ItemMetrics
{
  protected int  x, y, width, height;
  protected int viewWidth = -1, viewHeight;
  protected boolean isOpenValue;

 /**
  * Constructs the item metrics instance with the specified open flag value.
  * @param b the specified open flag value.
  */
  public ItemMetrics(boolean b) {
   isOpenValue = b;
  }

 /**
  * Gets the node height.
  * @return a node height.
  */
  public int getHeight() {
    return height;
  }

 /**
  * Gets the node width.
  * @return a node width.
  */
 public int getWidth() {
    return width;
  }

 /**
  * Tests if the item is opened.
  * @return <code>true</code> if the item is opened; <code>false</code> otherwise.
  */
   public boolean isOpen() {
    return isOpenValue;
  }
}


