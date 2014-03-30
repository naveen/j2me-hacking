package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is the same LwImgRender render but it provides ability to render the specified part
 * of the target image object.
 */
public class LwImgSetRender
extends LwImgRender
{
   private int x, y, w, h;

  /**
   * Constructs the render with the specified target image name, the view type and
   * the specified rectangular image part that should be rendered.
   * @param name the specified target image name.
   * @param x the x coordinate of the top left corner the rendered part of the image.
   * @param y the y coordinate of the top left corner the rendered part of the image.
   * @param w the width the rendered part of the image.
   * @param h the height the rendered part of the image.
   * @param type the specified view type.
   */
   public LwImgSetRender (String name, int x, int y, int w, int h, int type) {
     this (LwToolkit.getImage(name), x, y, w, h, type);
   }

  /**
   * Constructs the render with the specified target image, the view type and
   * the specified rectangular image part that should be rendered.
   * @param img the specified target image to be rendered.
   * @param x the x coordinate of the top left corner the rendered part of the image.
   * @param y the y coordinate of the top left corner the rendered part of the image.
   * @param w the width the rendered part of the image.
   * @param h the height the rendered part of the image.
   * @param type the specified view type.
   */
   public LwImgSetRender (Image img, int x, int y, int w, int h, int type)
   {
     super(img, type);
     this.x = x;
     this.y = y;
     this.w = w;
     this.h = h;
   }

   public /*C#override*/ void paint (Graphics g, int x, int y, int w, int h, Object d) {
     g.drawImage (getImage(), x, y, x + w, y + h,
                  this.x, this.y, this.x + this.w, this.y + this.h, null);
   }

  /**
   * Calculates and returns the view preferred size. The render returns the target image
   * size as the "pure" preferred size.
   * @return a "pure" preferred size of the view.
   */
   protected /*C#override*/ Dimension calcPreferredSize()  {
     return getImage() == null?super.calcPreferredSize():new Dimension (w, h);
   }
}
