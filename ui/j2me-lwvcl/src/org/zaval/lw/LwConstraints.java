package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The class is used to specify constrains that are used with the LwGridLayout layout manager.
 */
public class LwConstraints
{
 /**
  * Specifies number of columns that has to be used for the cell.
  * The field is not used with the LwGridLayout layout manager.
  * It is going to be utilized in the future version of the library
  */
  public int colSpan = 1;

 /**
  * Specifies number of rows that has to be used for the cell.
  * The field is not used with the LwGridLayout layout manager.
  * It is going to be utilized in the future version of the library
  */
  public int rowSpan = 1;

 /**
  * This field is used when the component display area is larger
  * than the component requested size. It determines whether to
  * align the component, and if so, how.
  * <p>
  * The following constants can be used as the value:
  * <p>
  * <ul>
  *   <li>
  *     <b>LwToolkit.NONE</b> - do not align the component and use its preferred size.
  *   </li>
  *   <li>
  *     <b>LwToolkit.HORIZONTAL</b> - make the component wide enough to fill
  *     its display area horizontally, but do not change its height.
  *   </li>
  *   <li>
  *     <b>LwToolkit.VERTICAL</b> - make the component tall enough to fill its
  *          display area vertically, but do not change its width.
  *   </li>
  *   <li>
  *     <b>LwToolkit.HORIZONTAL | LwToolkit.VERTICAL</b> - make the component fill its display area entirely.
  *   </li>
  * </ul>
  * <p>
  *  <b>LwToolkit.HORIZONTAL|LwToolkit.VERTICAL</b> is used as default value for the field.
  */
  public int fill = LwToolkit.HORIZONTAL | LwToolkit.VERTICAL;

 /**
  * This field defines the component horizontal alignment inside the area.
  * Possible values for the alignment are: <b>LwToolkit.CENTER</b>,
  * <b>LwToolkit.LEFT</b>, <b>LwToolkit.RIGHT</b>, <b>LwToolkit.NONE</b>.
  */
  public int ax = LwToolkit.CENTER;


 /**
  * This field defines the component vertical alignment inside the area.
  * Possible values for the alignment are: <b>LwToolkit.TOP</b>,
  * <b>LwToolkit.BOTTOM</b>, <b>LwToolkit.CENTER</b>, <b>LwToolkit.NONE</b>.
  */
  public int ay = LwToolkit.CENTER;

 /**
  * This field specifies the external padding of the component, the
  * minimum amount of space between the component and the edges of its
  * display area.
  * <p>
  * The default value is <code>null</code>. This is equivalent of <code>(0, 0, 0, 0)</code>
  * insets.
  */
  public Insets insets = null;
}
