package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface is top-level container for all other lightweight components. The main
 * purpose of the interface to provide abstraction level that binds the library with a
 * concrete GUI implementation. Every lightweight component is bound with a desktop
 * container. Use the <code>LwToolkit.getDesktop</code> method to get a desktop for the
 * specified lightweight component. The desktop consists of layers. Layer is a special
 * light weight container that is a child component of the desktop. It is impossible
 * to add a none-layer component to the desktop. Use the <code>getRootLayer</code> method
 * to get the root layer. The layer should be used if you want to add a light weight
 * component on the desktop surface. The other available layer is the windows layer. The
 * layer provides functionality to work with internal frames. The top-level layer is a popup
 * menu layer.
 */
public interface LwDesktop
extends LwContainer
{
 /**
  * Gets the native component where the desktop layer "lives". For example, in a case the
  * J2SE LwVCL version the method returns a org.zaval.port.j2me.Panel instance as the native component.
  * @return a native component.
  */
  Object getNCanvas();

 /**
  * Gets the root layer that should be used to add a light weight component on the desktop
  * surface.
  * @return a root layer.
  */
  LwLayer getRootLayer ();

 /**
  * Gets the layer by the specified id. There are two layers available by the "root" and "win" IDs.
  * @param id the specified id.
  * @return a layer.
  */
  LwLayer getLayer (Object id);

 /**
  * Gets the list of available layers' IDs.
  * @return a list of the layers' IDs.
  */
  Object[] getLayersIDs();

 /**
  * Gets a graphics context for this desktop. This is adaptive method and it is used to bind
  * native GUI implementation with the components of the library.
  * @return a graphics context.
  */
  Graphics getGraphics();

 /**
  * Creates an off-screen image to be used for double buffering with the specified width
  * and height. This is adaptive method and it is used to bind native GUI implementation with the
  * components of the library.
  * @param w the specified image width.
  * @param h the specified image height.
  * @return an off-screen image.
  */
  Image createImage(int w, int h);

 /**
  * Gets the value for the specified property.
  * @param id the specified property id.
  * @return a value of the property.
  */
  Object getProperty (int id);

 /**
  * Sets the value for the specified property.
  * @param id the specified property id.
  * @param value the specified property value.
  */
  void setProperty (int id, Object value);

 /**
  * Gets the dirty area of the component. The method is used to store and update
  * the desktop dirty area (this is the area that should be updated).
  * @return a dirty area.
  */
  Rectangle getDA();
}


