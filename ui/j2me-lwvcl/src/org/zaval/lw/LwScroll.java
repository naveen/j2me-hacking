package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.misc.*;
import org.zaval.misc.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is scrollbar component. The component is used as the vertical and horizontal scrolls by the scroll
 * bar panel (LwScrollPan). The component reads the following resources:
 * <ul>
 *   <li>"scroll.hbg" - this is a background view of the horizontal scrollbar</li>
 *   <li>"scroll.vbg" - this is a background view of the vertical scrollbar</li>
 *   <li>
 *      "sbt.bottom.out", "sbt.bottom.over", "sbt.bottom.disabled", "sbt.bottom.pressed" -
 *      these are the lower button face views of the vertical scrollbar
 *   </li>
 *   <li>
 *     "sbt.top.out", "sbt.top.over", "sbt.top.disabled", "sbt.top.pressed" -
 *     these are the upper button face views of the vertical scrollbar</li>
 *   <li>
 *     "sbt.left.out", "sbt.left.over", "sbt.left.disabled", "sbt.left.pressed" -
 *      these are the left button face views of the horizontal scrollbar
 *   </li>
 *   <li>
 *    "sbt.right.out", "sbt.right.over", "sbt.right.disabled", "sbt.right.pressed" -
 *     these are the right button face views of the horizontal scrollbar
 *   </li>
 * </ul>
 * <p>
 * The scrollbar component defines a value that should be less than certain maximal value and
 * cannot be less zero. The maximal possible value and the current value are defined by
 * a pos controller of the component. Use the <code>getMaxOffset</code>, <code>setMaxOffset</code>
 * methods of the component and the <code>getOffset</code>, <code>setOffset</code> methods of the
 * pos controller to control the scrollbar properties. The sample below illustrates the scrollbar
 * properties usage:
 * <pre>
 *   ...
 *   LwScroll scroll = new LwScroll(LwToolkit.VERTICAL);
 *   scroll.setMaxOffset(100); // Setting the maximal posiible value of the scrollbar
 *   scroll.getPosController().getOffset();    // Getting the current value of the scrollbar
 *   scroll.getPosController().setOffset(10);  // Setting the current value of the scrollbar
 *   ...
 * </pre>
 * <p>
 * A user can interact with the component using following actions:
 * <ul>
 *   <li>
 *     By pressing the scrollbar buttons. In this case, the component uses "unitIncrement" value to
 *     increase or decrease the value property.
 *   </li>
 *   <li>
 *     By pressing a mouse button inside the scrollbar area. In this case "pageIncrement" value is
 *     used to increase or decrease the scrollbar value property.
 *   </li>
 *   <li>
 *     By dragging the scrollbar bundle element. In this case the scrollbar value will be
 *     calculated basing on the current bundle location.
 *   </li>
 * </ul>
 * To control "unitIncrement" and "pageIncrement" properties use the <code>setPageIncremet</code>
 * and <code>setUnitIncremet</code> methods.
 * <p>
 * The component is a container that implements own layout manager. Use the defined by the component
 * constraints to customize the container elements.
 */
public class LwScroll
extends LwPanel
implements LwActionListener, LwMouseListener, LwMouseMotionListener,
           PosInfo, PosListener, LwLayout, LwComposite
{
  /**
   * The bundle element constraint.
   */
   public static final Integer BUNDLE_EL    = new Integer(0);

  /**
   * The increment button element constraint.
   */
   public static final Integer INCBUTTON_EL = new Integer(1);

  /**
   * The decrement button element constraint.
   */
   public static final Integer DECBUTTON_EL = new Integer(2);

   private int           type, max = 100, pageIncrement = 20, unitIncrement = 5, extra = 100;
   private int           startDragLoc = Integer.MAX_VALUE, bundleLoc;
   private LwComponent   bundle;
   private LwStButton    incBt, decBt;
   private PosController pos;

  /**
   * Constructs a scrollbar component with the specified orientation.
   * The orientation value can be LwToolkit.VERTICAL or LwToolkit.HORIZONTAL,
   * otherwise the IllegalArgumentException will be thrown.
   * @param t the specified orientation.
   */
   public LwScroll(int t)
   {
     LwCanvas bundle = new LwCanvas();
     bundle.getViewMan(true).setBorder(new LwBorder(LwBorder.RAISED2));
     add (BUNDLE_EL, bundle);

     LwAdvViewMan man1 = new LwAdvViewMan();
     LwAdvViewMan man2 = new LwAdvViewMan();
     LwStButton incBt = new LwStButton();
     LwStButton decBt = new LwStButton();
     incBt.fireByPress(true, 20);
     decBt.fireByPress(true, 20);
     if (t == LwToolkit.VERTICAL)
     {
       getViewMan(true).setBg(LwToolkit.getView("scroll.vbg"));
       man1.put ("st.out", LwToolkit.getView("sbt.bottom.out"));
       man1.put ("st.over", LwToolkit.getView("sbt.bottom.over"));
       man1.put ("st.pressed", LwToolkit.getView("sbt.bottom.pressed"));
       man1.put ("st.disabled", LwToolkit.getView("sbt.bottom.disabled"));

       man2.put ("st.out", LwToolkit.getView("sbt.top.out"));
       man2.put ("st.over", LwToolkit.getView("sbt.top.over"));
       man2.put ("st.pressed", LwToolkit.getView("sbt.top.pressed"));
       man2.put ("st.disabled", LwToolkit.getView("sbt.top.disabled"));
     }
     else
     if (t == LwToolkit.HORIZONTAL)
     {
       getViewMan(true).setBg(LwToolkit.getView("scroll.hbg"));
       man1.put ("st.out", LwToolkit.getView("sbt.right.out"));
       man1.put ("st.over", LwToolkit.getView("sbt.right.over"));
       man1.put ("st.pressed", LwToolkit.getView("sbt.right.pressed"));
       man1.put ("st.disabled", LwToolkit.getView("sbt.right.disabled"));

       man2.put ("st.out", LwToolkit.getView("sbt.left.out"));
       man2.put ("st.over", LwToolkit.getView("sbt.left.over"));
       man2.put ("st.pressed", LwToolkit.getView("sbt.left.pressed"));
       man2.put ("st.disabled", LwToolkit.getView("sbt.left.disabled"));
     }
     else throw new IllegalArgumentException();

     add (DECBUTTON_EL, decBt);
     add (INCBUTTON_EL, incBt);
     incBt.setViewMan(man1);
     decBt.setViewMan(man2);

     type = t;
     setPosController(new PosController(this));
   }

  /**
   * Sets the specified maximal value for the scrollbar component.
   * The scrollbar value cannot be more than the maximal value, so the method
   * sets the current value to maximal value if the current value is more than
   * the new maximal value.
   * @param m the specified maximal value.
   */
   public void setMaximum (int m)
   {
     if (m != max)
     {
       max = m;
       if (pos.getOffset() > max) pos.setOffset(max);
       vrp();
     }
   }

  /**
   * Sets the page increment value. The value is used to decrease or increase the scrollbar value
   * whenever a mouse button has been pressed inside the component area.
   * @param pi the specified page increment.
   */
   public void setPageIncrement (int pi) {
     if (pageIncrement != pi) pageIncrement = pi;
   }

  /**
   * Gets the page increment value.
   * @return a page increment value.
   */
   public int getPageIncrement() {
     return pageIncrement;
   }

  /**
   * Sets the unit increment value. The value is used to decrease or increase the scrollbar value
   * whenever one of the scrollbar button has been pressed.
   * @param li the specified unit increment.
   */
   public void setUnitIncrement (int li) {
     if (unitIncrement != li) unitIncrement = li;
   }

  /**
   * Gets the unit increment value.
   * @return a unit increment value.
   */
   public int getUnitIncrement() {
     return unitIncrement;
   }

  /**
   * Sets the specified position controller. The controller is used to control the
   * scrollbar value.
   * @param p the specified position controller.
   */
   public void setPosController (PosController p)
   {
     if (p != pos)
     {
       if (pos != null) pos.removePosListener(this);
       pos = p;
       if (pos != null)
       {
         pos.addPosListener(this);
         pos.setPosInfo (this);
         pos.setOffset(0);
       }
     }
   }

  /**
   * Gets the position controller.
   * @return a position controller.
   */
   public PosController getPosController () {
     return pos;
   }

   public void actionPerformed(Object src, Object data) {
     pos.setOffset(pos.getOffset() + ((src == incBt)?unitIncrement:-unitIncrement));
   }

   public void mouseClicked (LwMouseEvent e) {}
   public void mouseEntered (LwMouseEvent e) {}
   public void mouseExited  (LwMouseEvent e) {}
   public void mouseReleased(LwMouseEvent e) {}
   public void mouseMoved   (LwMouseEvent e) {}

   public void startDragged (LwMouseEvent e)
   {
     if (isInsideBundle(e.getX(), e.getY()))
     {
       startDragLoc = type == LwToolkit.HORIZONTAL?e.getX():e.getY();
       bundleLoc    = type == LwToolkit.HORIZONTAL?bundle.getX():bundle.getY();
     }
   }

   public void endDragged (LwMouseEvent e) {
     startDragLoc  = Integer.MAX_VALUE;
   }

   public void mouseDragged (LwMouseEvent e) {
     if (Integer.MAX_VALUE != startDragLoc)
       pos.setOffset (pixel2value(bundleLoc - startDragLoc + ((type == LwToolkit.HORIZONTAL)?e.getX():e.getY())));
   }

   public void mousePressed (LwMouseEvent e)
   {
     if (!isInsideBundle(e.getX(), e.getY()) && LwToolkit.isActionMask(e.getMask()))
     {
       int d = pageIncrement;
       if (type == LwToolkit.VERTICAL) {
         if (e.getY() < bundle.getY()) d = -pageIncrement;
       }
       else
         if (e.getX() < bundle.getX()) d = -pageIncrement;
       pos.setOffset(pos.getOffset() + d);
     }
   }

   public void posChanged(Object target, int po, int pl, int pc)
   {
     if (type == LwToolkit.HORIZONTAL)
       bundle.setLocation(value2pixel(), getTop());
     else
       bundle.setLocation(getLeft(), value2pixel());
   }

   public int getLines   () {
     return max;
   }

   public int getLineSize(int line) {
     return 1;
   }

   public int getMaxOffset() {
     return max;
   }

  /**
   * Get the extra size.
   * @return an extra size.
   */
   public int getExtraSize() {
     return extra;
   }

  /**
   * Sets the extra size. By the size it is possible to control bundle size depending on the maximal possible
   * size.
   * @param e the given extra size.
   */
   public void setExtraSize(int e)
   {
     if (e != extra)
     {
       extra = e;
       vrp();
     }
   }

   public void componentAdded(Object id, Layoutable lw, int index)
   {
     if (id.equals(BUNDLE_EL)) bundle = (LwComponent)lw;
     else
     if (id.equals(INCBUTTON_EL))
     {
       incBt = (LwStButton)lw;
       incBt.addActionListener(this);
     }
     else
     if (id.equals(DECBUTTON_EL))
     {
       decBt = (LwStButton)lw;
       decBt.addActionListener(this);
     }
   }

   public void componentRemoved(Layoutable lw, int index)
   {
     if (lw == bundle) bundle = null;
     else
     if (lw == incBt)
     {
       incBt.removeActionListener(this);
       incBt = null;
     }
     else
     if (lw == decBt)
     {
       decBt.removeActionListener(this);
       decBt = null;
     }
   }

   public Dimension calcPreferredSize(LayoutContainer target)
   {
     Dimension ps1 = ps(incBt);
     Dimension ps2 = ps(decBt);
     Dimension ps3 = ps(bundle);

     if (type == LwToolkit.HORIZONTAL)
     {
       ps1.width   += (ps2.width + ps3.width);
       ps1.height  = Math.max(Math.max(ps1.height, ps2.height), ps3.height);
     }
     else
     {
       ps1.height += (ps2.height + ps3.height);
       ps1.width  = Math.max(Math.max(ps1.width, ps2.width), ps3.width);
     }
     return ps1;
   }

   public void layout(LayoutContainer  target)
   {
     Insets  i  = getInsets();
     int     ew = width - i.left - i.right, eh = height - i.top - i.bottom;
     boolean b  = (type == LwToolkit.HORIZONTAL);

     Dimension ps1 = ps(decBt);
     if (ps1.width > 0)
     {
       decBt.setSize(b?ps1.width:ew, b?eh:ps1.height);
       decBt.setLocation (i.left, i.top);
     }

     Dimension ps2 = ps(incBt);
     if (ps2.width > 0)
     {
       incBt.setSize     (b?ps2.width:ew, b?eh:ps2.height);
       incBt.setLocation (b?width - i.right - ps2.width:i.left, b?i.top:height - i.bottom - ps2.height);
     }

     if (bundle != null && bundle.isVisible())
     {
       int am = amount();
       if (am > MIN_BUNDLE_SIZE)
       {
         int bsize = Math.max (Math.min ((extra * am)/max, am - MIN_BUNDLE_SIZE), MIN_BUNDLE_SIZE);
         bundle.setSize(b?bsize:ew, b?eh:bsize);
         bundle.setLocation(b?value2pixel():i.left, b?i.top:value2pixel());
       }
       else bundle.setSize(0, 0);
     }
   }

   public boolean catchInput(LwComponent child) {
     return child == bundle || (bundle instanceof LwContainer && LwToolkit.isAncestorOf ((LwContainer)bundle, child));
   }

  /**
   * Gets the element of the scroll bar by the given constraint. Using the method it is possible
   * to get bundle, increment or decrement buttons components.
   * @param key the specified constraint. Use the BUNDLE_EL, INCBUTTON_EL, DECBUTTON_EL
   * constants as the argument value.
   * @return an element.
   */
   public LwComponent getElement (Object key)
   {
     if (key.equals(BUNDLE_EL))    return bundle;
     else
     if (key.equals(INCBUTTON_EL)) return incBt;
     else
     if (key.equals(DECBUTTON_EL)) return decBt;
     else                          throw new IllegalArgumentException();
   }

   protected /*C#override*/ LwLayout getDefaultLayout() {
     return this;
   }

   private boolean isInsideBundle (int x, int y) {
     return  (bundle != null && bundle.isVisible() && bundle.getX() <= x && bundle.getY() <= y &&
              bundle.getX() + bundle.getWidth() > x  && bundle.getY() + bundle.getHeight() > y);
   }

   private int amount() {
     return (type == LwToolkit.VERTICAL)?incBt.y - decBt.y - decBt.height:
                                         incBt.x - decBt.x - decBt.width;
   }

   private int pixel2value(int p) {
     return (type == LwToolkit.VERTICAL)? (max * (p - decBt.y - decBt.height))/(amount() - bundle.getHeight())
                                        : (max * (p - decBt.x - decBt.width))/(amount() - bundle.getWidth());
   }

   private int value2pixel() {
     return (type == LwToolkit.VERTICAL)? decBt.y + decBt.height + ((amount() - bundle.getHeight()) * pos.getOffset())/max
                                        : decBt.x + decBt.width  + ((amount() - bundle.getWidth()) * pos.getOffset())/max;
   }

   private Dimension ps (Layoutable l) {
     return l != null && l.isVisible()?l.getPreferredSize():new Dimension();
   }

   private static final int MIN_BUNDLE_SIZE = 16;
}



