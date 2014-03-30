package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.lw.event.*;
import org.zaval.misc.*;
import org.zaval.misc.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This is list component. The main features of the component are shown in the list below:
 * <ul>
 *   <li>
 *      The list component uses other light weight component as the list items. Use
 *      <code>add</code>, <code>remove</code>, <code>insert</code> methods to manipulate
 *      the list component content. For example, if it is necessary to add following three
 *      text items: "Item 1", "Item 2", "Item 3", you can use LwLabel component as it is shown
 *      below:
 *      <pre>
 *         ...
 *         LwList list = new LwList();
 *         list.add(new LwLabel("Item 1"));
 *         list.add(new LwLabel("Item 2"));
 *         list.add(new LwLabel("Item 3"));
 *         ...
 *      </pre>
 *      The class has method <code>add</code> that get string as input argument. The method
 *      creates LwLabel object with the string argument as a title. In this case the sample
 *      above can be simplify as follow:
 *      <pre>
 *         ...
 *         LwList list = new LwList();
 *         list.add("Item 1");
 *         list.add("Item 2");
 *         list.add("Item 3");
 *         ...
 *      </pre>
 *   </li>
 *   <li>
 *     The list component is a composite component, that allows you to use other composite component
 *     as the list items. It means the it is possible to add LwButton component to the
 *     list as an item and it is possible to work with the component after it has been
 *     selected (the button component can be pressed).
 *   </li>
 *   <li>
 *     The list component uses LwListLayout as default manager to layout the list items.
 *     It is possible to use any other layout manager for the purpose, but the layout
 *     manager should implement org.zaval.misc.PosInfo to have ability working together
 *     with the a pos manager of the list. The library provides two layout managers that
 *     can be used as the layout manager: LwListLayout and LwGridLayout.
 *   </li>
 *   <li>
 *     The list has PosController class that is used to control position. Draw attention that
 *     selection and position are two different things. The list implementation automatically
 *     reselects item when the position has been changed. But further version of the component
 *     can support multiselection. To control the list position use pos controller that can be
 *     got by <code>getPosController</code> method. To control selection use <code>select</code>,
 *     <code>getSelected</code>, <code>getSelectedIndex</code>, <code>isSelected</code> methods.
 *     To listen when an item component has been selected use <code>addSelectionListener</code>
 *     and <code>removeSelectionListener</code> methods.
 *   </li>
 *   <li>
 *     The list component can scroll its view if not all items are visible. The feature works
 *     correct if the component will be inserted in LwScrollPan.
 *   </li>
 *   <li>
 *     Use <code>addSelectionListener</code> and <code>removeSelectionListener</code> methods
 *     to listen whenever the selected item has been changed. The passed data argument of
 *     event is an item index or <code>null</code> if the item has been re-selected.
 *   </li>
 * </ul>
 */
public class LwList
extends LwPanel
implements LwKeyListener, LwMouseListener,
           PosListener, ScrollObj
{
   /**
    * Defines the selection marker color type.
    */
    public static final int SEL_COLOR = 0;

   /**
    * Defines the pos marker color type.
    */
    public static final int POS_COLOR = 1;

    private int selectedIndex = -1;
    private PosController controller;
    private PosInfo       posInfo;
    private LwActionSupport support;
    private int  dx, dy;
    private Color[] colors = new Color[2];
    private ScrollMan man;

   /**
    * Constructs a list component with no items.
    */
    public LwList()
    {
      setColor(SEL_COLOR, (Color)LwToolkit.getStaticObj("sel1.color"));
      setColor(POS_COLOR, Color.gray);
      setPosController(new PosController());
    }

    public /*C#override*/ boolean canHaveFocus() {
      return true;
    }

   /**
    * Sets the specified position controller. The controller manages the virtual cursor
    * location.
    * @param c the specified position controller.
    */
    public void setPosController(PosController c)
    {
      if(c != controller)
      {
        if (controller != null) controller.removePosListener(this);
        controller = c;
        controller.addPosListener(this);
        if (posInfo != null) controller.setPosInfo(posInfo);
        repaint();
      }
    }

   /**
    * Sets the layout manager for this container. The layout manager has to implement
    * org.zaval.misc.PosInfo interface, the interface allows you to control virtual position
    * of the list component with the position controller of the component.
    * @param l the specified layout manager.
    */
    public /*C#override*/ void setLwLayout(LwLayout l)
    {
      if (l != getLwLayout())
      {
        super.setLwLayout(l);
        if (l instanceof PosInfo) posInfo = (PosInfo)l;
        if (controller != null) controller.setPosInfo(posInfo);
      }
    }

   /**
    * Gets the position controller.
    * @return a position controller.
    */
    public PosController getPosController() {
      return controller;
    }

   /**
    * Gets the specified element color.
    * @param type the specified element type. Use the SEL_COLOR or POS_COLOR constant as the
    * argument value.
    * @return a color.
    */
    public Color getColor(int type) {
      return colors[type];
    }

   /**
    * Sets the specified color for the given element.
    * @param type the specified element type. Use the SEL_COLOR or POS_COLOR constant as the
    * argument value.
    * @param c the color.
    */
    public void setColor(int type, Color c)  {
      colors[type] = c;
      repaint();
    }

   /**
    * Adds an item to the list. The method creates LwLabel component with the specified
    * label and adds it to the list.
    * @param s the specified label.
    */
    public void add (String s)  {
      add (new LwLabel(s));
    }

    /**
    * Selects the item by the specified index.
    * @param index the specified item index.
    */
    public /*C#virtual*/ void select(int index)
    {
      LwComponent prev = (selectedIndex < count())?getSelected():null;
      if (selectedIndex != index)
      {
        if (index >= 0)
        {
          selectedIndex = index;
          notifyScrollMan(index);
        }
        else clearSelection();

        repaint();
        perform(this, new Integer(selectedIndex));
      }
      else perform(this, null);
    }

   /**
    * Gets the selected item index.
    * @return a selected item index.
    */
    public int getSelectedIndex() {
      return selectedIndex;
    }

   /**
    * Tests if the item with the specified index is selected or not.
    * @return <code>true</code> if the item with the specified index is selected; otherwise
    * <code>false</code>.
    */
    public boolean isSelected(int i)  {
      return i == selectedIndex;
    }

   /**
    * Adds the specified selection listener to receive selection events from this list component.
    * The event data argument is selected item index that is represented as the Integer object.
    * The data is <code>null</code> if the item has been re-selected.
    * @param l the specified listener.
    */
    public void addSelectionListener(LwActionListener l)  {
      if (support == null) support = new LwActionSupport();
      support.addListener(l);
    }

   /**
    * Removes the specified selection listener so it no longer receives selection events
    * from this list component.
    * @param l the specified listener.
    */
    public void removeSelectionListener(LwActionListener l)  {
      if (support != null) support.removeListener(l);
    }

   /**
    * Paints this component. The method is overridden to paint the list item selection rectangle.
    * @param g the graphics context to be used for painting.
    */
    public /*C#override*/ void paintOnTop(Graphics g) {
      drawSelMarker(g);
      if (hasFocus()) drawPosMarker(g);
    }

   /**
    * Invoked to paint selection marker.
    * @param g the graphics context to be used for painting.
    */
    protected /*C#virtual*/ void drawSelMarker(Graphics g)
    {
       if (selectedIndex >= 0 && colors[SEL_COLOR] != null)
       {
         LwComponent c   = getSelected();
         int         gap = getMarkerGap();
         LwToolkit.drawMarker(g, c.getX() - gap, c.getY() - gap,
                                 c.getWidth() + 2*gap, c.getHeight() + 2*gap,
                                 getBackground(), colors[SEL_COLOR]);
       }
    }

   /**
    * Invoked to paint position marker.
    * @param g the graphics context to be used for painting.
    */
    protected /*C#virtual*/ void drawPosMarker(Graphics g)
    {
       int offset = controller.getOffset();
       if (offset >= 0 && colors[POS_COLOR] != null)
       {
         LwComponent c = (LwComponent)get(offset);
         g.setColor(colors[POS_COLOR]);
         g.drawRect(c.getX(), c.getY(), c.getWidth()-1, c.getHeight()-1);
       }
    }

    public /*C#virtual*/ void mouseClicked(LwMouseEvent e) {}
    public /*C#virtual*/ void mouseEntered(LwMouseEvent e) {}
    public /*C#virtual*/ void mouseExited(LwMouseEvent e)  {}

    public /*C#virtual*/ void mousePressed(LwMouseEvent e)
    {
      if (LwToolkit.isActionMask(e.getMask()))
      {
        int index = LwToolkit.getDirectCompAt(e.getX(), e.getY(), this);
        if (index >= 0)
        {
          controller.setOffset(index);
          select(index);
        }
      }
    }

    public /*C#virtual*/ void mouseReleased(LwMouseEvent e) {}

    public /*C#virtual*/ void keyPressed(LwKeyEvent e)
    {
      if (posInfo.getMaxOffset() > 0)
      {
        switch(e.getKeyCode())
        {
          case LwToolkit.VK_END  :  controller.setOffset(controller.getMaxOffset()); break;
          case LwToolkit.VK_HOME :  controller.setOffset(0); break;
          case LwToolkit.VK_RIGHT:  controller.seek(1); break;
          case LwToolkit.VK_DOWN :  controller.seekLineTo(PosController.DOWN); break;
          case LwToolkit.VK_LEFT :  controller.seek(-1); break;
          case LwToolkit.VK_UP   :  controller.seekLineTo(PosController.UP); break;
        }
      }
    }

    public /*C#virtual*/ void keyReleased(LwKeyEvent e){}
    public /*C#virtual*/ void keyTyped(LwKeyEvent e)   {}

    public /*C#virtual*/ void posChanged(Object target, int prevOffset, int prevLine, int prevCol)
    {
      int off = controller.getOffset();
      select(off);
      LwComponent input = (off >= 0)?(LwComponent)get(off):null;
      repaint(input, (prevOffset >= 0 && prevOffset < count())?get(prevOffset):null);
    }

    public /*C#override*/ void insert(int i, Object s, LwComponent d)
    {
      int offset = controller.getOffset();
      super.insert(i, s, d);

      if (offset >= 0 && offset >= i)
      {
        clearSelection();
        controller.clearPos();
        offset = offset == i?offset:offset+1;
        controller.setOffset(offset);
      }
    }

    public /*C#override*/ void remove(int i)
    {
      int offset = controller.getOffset();

      super.remove(i);
      if (selectedIndex == i || count() == 0) clearSelection();
      else
      if (selectedIndex > i ) selectedIndex--;

      if (offset == i || count() == 0) controller.clearPos();
      else
      if (offset > i) controller.setOffset(offset-1);
    }

    public /*C#override*/ void removeAll()
    {
      super.removeAll();
      clearSelection();
      controller.clearPos();
    }

    public Point getSOLocation () {
      return getLayoutOffset();
    }

    public void setSOLocation (int x, int y)
    {
      if (x != dx || y != dy)
      {
        dx = x;
        dy = y;
        vrp();
      }
    }

    public Dimension getSOSize() {
      return getPreferredSize();
    }

    public void setScrollMan (ScrollMan m) {
      man = m;
    }

    public boolean moveContent() {
      return true;
    }

    public /*C#override*/ Point getLayoutOffset()  {
      return new Point(dx, dy);
    }

   /**
    * Gets the selected item component.
    * @return a selected item component.
    */
    public LwComponent getSelected() {
      return selectedIndex < 0?null:(LwComponent)get(selectedIndex);
    }

   /**
    * Creates and fires the selection event.
    * @param src the specified source of the event.
    * @param data the specified event related data.
    */
    protected /*C#virtual*/ void perform(Object src, Object data) {
      if (support != null) support.perform(src, data);
    }

   /**
    * Notifies the scroll manager that an item component at the specified index should be fully
    * visible. The scroll manager should scrolls the list view to make the component fully
    * visible if it is necessary.
    * @param index the specified index.
    * otherwise.
    */
    protected /*C#virtual*/ void notifyScrollMan(int index)
    {
      if (index >= 0)
      {
        validate();
        LwComponent c = (LwComponent)get(index);

        Point p = LwToolkit.calcOrigin (c.getX() - dx, c.getY()- dy, c.getWidth(), c.getHeight(), dx, dy, this);
        if (p.x != dx || p.y != dy)
        {
          //!!!
          if (man != null) man.moveScrolledObj(p.x, p.y);
          else             setSOLocation(p.x, p.y);
        }
      }
    }

    protected /*C#override*/ LwLayout getDefaultLayout() {
      return new LwListLayout();
    }

    protected /*C#virtual*/ int getMarkerGap() {
      return 0;
    }

    protected /*C#virtual*/ void repaint(Layoutable i1, Layoutable i2)
    {
      int mgap = getMarkerGap();
      if (i1 != null)
        repaint (i1.getX() - mgap, i1.getY() - mgap,
                 i1.getWidth() + mgap*2, i1.getHeight() + mgap*2);

      if (i2 != null)
        repaint (i2.getX() - mgap,       i2.getY() - mgap,
                 i2.getWidth() + mgap*2, i2.getHeight() + mgap*2);
    }

    private void clearSelection() {
      if (selectedIndex >= 0) selectedIndex = -1;
    }
}
