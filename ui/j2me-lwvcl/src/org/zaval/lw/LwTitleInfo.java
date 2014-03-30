package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * The interface has to be implemented with a lightweight component that wants to use
 * LwTitledBorder view as a border view. The methods of the interface provide
 * information about the border title bounds and alignment.
 */
public interface LwTitleInfo
{
 /**
  * Gets the title size and location. The bounds usage depends on the title alignment:
  * <ul>
  *   <li>
  *     If the alignment is LwToolkit.BOTTOM or LwToolkit.TOP than the appropriate title
  *     border view will use <code>y</code> coordinate to locate the border and <code>x</code>
  *     coordinate is calculated depending on the title border alignment.
  *   </li>
  *   <li>
  *     If the alignment is LwToolkit.LEFT or LwToolkit.RIGHT than the appropriate title
  *     border view will use <code>x</code> coordinate to locate the border and <code>y</code>
  *     coordinate is calculated depending on the title border alignment.
  *   </li>
  * </ul>
  * @return a title size and location.
  */
  Rectangle getTitleBounds();

 /**
  * Gets the title alignment. The alignment can have one of following values:
  * LwToolkit.TOP, LwToolkit.BOTTOM, LwToolkit.LEFT, LwToolkit.RIGHT and it
  * defines how the title has to be placed on the border.
  * @return a title alignment.
  */
  int getTitleAlignment();
}
