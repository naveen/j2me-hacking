package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class implements image render that knows how to draw the target org.zaval.port.j2me.Image object.
 */
public class LwImgRender
extends LwRender
{
  /**
   * Constructs the render with the specified target image and the view type.
   * @param img the specified target image to be rendered.
   * @param type the specified view type.
   */
   public LwImgRender (Image img, int type) {
     super(img, type);
   }

  /**
   * Constructs the render with the specified target image name. The name is
   * a path to the image file relatively LwVCL resources base (see the LwToolkit.getResourcesBase method)
   * directory. The constructor sets MOSAIC view type as default.
   * @param name the specified image name.
   */
   public LwImgRender(String name) {
     this(name, MOSAIC);
   }

  /**
   * Constructs the render with the specified target image name and the view type.
   * The name is a path to the image file relatively LwVCL resources base
   * (see the LwToolkit.getResourcesBase method) directory.
   * @param name the specified image name.
   * @param type the specified view type.
   */
   public LwImgRender (String name, int type) {
     this(LwToolkit.getImage(name), type);
   }

  /**
   * Gets the target image object that is painted with the render.
   * @return a target image object.
   */
   public Image getImage () {
     return (Image)getTarget();
   }

  /**
   * Paints the view using the given width and height. The location where the
   * view has to be painted is determined with <code>x</code> and <code>y</code>
   * coordinates.
   * @param g the specified context to be used for painting.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param w the width of the view.
   * @param h the height of the view.
   * @param d the owner component.
   */
   public /*C#override*/ void paint (Graphics g, int x, int y, int w, int h, Object d) {
     g.drawImage (getImage(), x, y, w, h, null);
   }

  /**
   * Calculates and returns the view preferred size. The render returns the target image
   * size as the "pure" preferred size.
   * @return a "pure" preferred size of the view.
   */
   protected /*C#override*/ Dimension calcPreferredSize()  {
     Image img = getImage();
     return img == null?super.calcPreferredSize():new Dimension (img.getWidth(), img.getHeight());
   }
}
