package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This light weight component is used to show an image. To show the image the
 * component uses LwImgRender as the face view.
 */
public class LwImage
extends LwCanvas
{
  /**
   * Constructs the component with the specified image object.
   * LwView.STRETCH view type is set by the constructor.
   * @param img the specified image object.
   */
   public LwImage(Image img) {
     this(new LwImgRender(img, LwView.STRETCH));
   }

  /**
   * Constructs the component with the specified image name. The name is used to
   * read the image as resource. It means that the name should point to an image
   * file relatively LwVCL resources base (see the LwToolkit.getResourcesBase method) directory.
   * LwView.ORIGINAL view type is set by the constructor.
   * @param name the specified image name.
   */
   public LwImage(String name) {
     this(new LwImgRender(name, LwView.ORIGINAL));
   }

  /**
   * Constructs the component with the specified image render. The render is used as
   * the face view of the component.
   * @param r the specified image render.
   */
   public LwImage(LwImgRender r) {
     getViewMan(true).setView(r);
   }

  /**
   * Gets the image that is shown with the component.
   * @return an image that is shown.
   */
   public Image getImage() {
     return getImageRender().getImage();
   }

  /**
   * Gets the image render that is used as the face view for the component with the view manager.
   * @return an image render.
   */
   public LwImgRender getImageRender() {
     return (LwImgRender)getViewMan(true).getView();
   }
}
