package org.zaval.lw.grid;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is an implementation of the <code>LwGridViewProvider</code> interface where the
 * <code>LwTextRender</code> view with the single line text model is used to render grid cells.
 */
public class LwDefViews
implements LwGridViewProvider
{
   private LwTextRender defView = new LwTextRender(new SingleLineTxt(""));

  /**
   * Gets the horizontal alignment for the specified grid cell.
   * The method should return one of the following values:
   * <ul>
   *   <li>LwToolkit.CENTER</li>
   *   <li>LwToolkit.LEFT</li>
   *   <li>LwToolkit.RIGHT</li>
   * </ul>
   * The implementation returns LwToolkit.CENTER as the horizontal alignment value.
   * @param row the specified cell row.
   * @param col the specified cell column.
   * @return a horizontal alignment.
   */
   public /*C#virtual*/ int getXAlignment(int row, int col) {
     return LwToolkit.CENTER;
   }

  /**
   * Gets the vertical alignment for the specified grid cell.
   * The method should return one of the following values:
   * <ul>
   *   <li>LwToolkit.CENTER</li>
   *   <li>LwToolkit.TOP</li>
   *   <li>LwToolkit.BOTTOM</li>
   * </ul>
   * The implementation returns LwToolkit.CENTER as the vertical alignment value.
   * @param row the specified cell row.
   * @param col the specified cell column.
   * @return a vertical alignment.
   */
   public /*C#virtual*/ int getYAlignment(int row, int col) {
     return LwToolkit.CENTER;
   }

 /**
  * Gets the specified grid cell color. The color is used to fill the
  * cell background. If the cell has not own background than the method
  * should return <code>null</code>. The implementation returns <code>null</code>
  * as the cell background color.
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @return a color.
  */
  public /*C#virtual*/ Color getCellColor(int row, int col) {
     return null;
   }

 /**
  * Gets the view of the specified cell and the given data model value.
  * The implementation returns <code>LwTextRender</code> as the view.
  * @param row the specified cell row.
  * @param col the specified cell column.
  * @param obj the specified data model value.
  * @return a view.
  */
   public /*C#virtual*/ LwView getView(int row, int col, Object obj)
   {
     if (obj == null) return null;
     else
     {
       defView.getTextModel().setText((String)obj);
       return defView;
     }
   }

  /**
   * Gets the text render that is used as the grid cells view.
   * @return a text render.
   */
   public /*C#virtual*/ LwTextRender getTextRender () {
     return defView;
   }
}

