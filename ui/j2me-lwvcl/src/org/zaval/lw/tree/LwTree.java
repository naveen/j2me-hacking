package org.zaval.lw.tree;

import java.util.*;
import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.data.event.*;
import org.zaval.data.Item;
import org.zaval.lw.*;
import org.zaval.lw.event.*;
import org.zaval.misc.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;
import org.zaval.util.*;



/**
 * This is tree view component. The component renders the specified  <code>TreeModel</code>
 * and organizes navigation through the tree. The sample below illustrates
 * the component usage:
 * <pre>
 *   ...
 *   Item root = new Item("root");
 *   TreeModel data = new Tree(root);
 *   data.add(data.getRoot(), new Item("First Child root"));
 *   data.add(data.getRoot(), new Item("Second Child root"));
 *   LwTree tree = new LwTree(data);
 *   ...
 * </pre>
 * <p>
 * The class provides ability to customize the tree node rendering by defining the
 * <code>LwViewProvider</code> provider. It allows you to specify the view for the tree item.
 * The default tree item view is LwTextRender.
 * <p>
 * The class provides ability to edit and customize editing process for the tree view items.
 * Use the <code>setEditorProvider</code> method to customize items editing.
 * <p>
 * Use the <code>addSelectionListener</code> and <code>removeSelectionListener</code> methods to
 * handle selection events. The events are performed by the component whenever the tree node
 * has been selected, deselected.
 * <p>
 * Use the <code>addActionListener</code> and <code>removeActionListener</code> methods to
 * be notified whenever the node has been expanded or collapsed. The data argument of the event contains
 * the tree expanded or collapsed item.
 * <p>
 * It is possible to disable node selection by the <code>enableSelection</code> method.
 * <p>
 * It is possible to customize views for some elements of the tree by the <code>setView</code>
 * method. The default views of the elements are read as static objects by the following keys:
 * <ul>
 *   <li>The toggle off view is fetched by "toggle.off" key.</li>
 *   <li>The toggle on view is fetched by "toggle.on" key.</li>
 *   <li>The list node view is fetched by the "tree.least" key.</li>
 *   <li>The opened node view is fetched by the "tree.open" key.</li>
 *   <li>The closed node view is fetched by the "tree.close" key.</li>
 * </ul>
 * <p>
 * The tree nodes height and width can be extended by the <code>setGaps</code> method that
 * defines horizontal and vertical gaps between the nodes.
 * <p>
 * Use the <code>setColor</code> method to customize the selection marker and tree lines color.
 * <p>
 * It is possible disables tree view line rendering by the <code>setColor</code> method.
 * Set the tree lines color to <code>null</code> value to disable the lines rendering.
 */
public class LwTree
extends LwPanel
implements TreeListener, LwViewProvider, LwMouseListener,
           LwKeyListener, ScrollObj
{
  /**
   * Defines list view element type.
   */
   public static final int LEAST_VIEW  = 0;

  /**
   * Defines opened view element type.
   */
   public static final int OPENED_VIEW = 1;

  /**
   * Defines closed view element type.
   */
   public static final int CLOSED_VIEW = 2;

  /**
   * Defines toggle off view element type.
   */
   public static final int TOGGLE_OFF_VIEW = 3;

  /**
   * Defines toggle on view element type.
   */
   public static final int TOGGLE_ON_VIEW  = 4;

  /**
   * Defines the selection marker color type for the tree component that has the focus.
   */
   public static final int SEL1_COLOR  = 0;

  /**
   * Defines the selection marker color type for the tree component that doesn't have the focus.
   */
   public static final int SEL2_COLOR  = 1;

  /**
   * Defines the lines color type.
   */
   public static final int LN_COLOR    = 2;

   private TreeModel          data;
   private LwActionSupport    actions;
   private LwActionSupport    selection;
   private boolean isOpenVal;

   private LwViewProvider  provider;
   private LwView[]        views = new LwView[5];
   private Hashtable       nodes;
   private int             gapx = 2, gapy = 2, dx, dy, maxw, maxh, itemGapX = 3, itemGapY = 0;
   private Item            selected, firstVisible;
   private Rectangle       visibleArea;
   private Dimension[]     viewSizes = new Dimension[5];
   private ScrollMan       man;
   private LwTextRender    defaultRender = new LwTextRender("");
   private Color[]         colors = new Color[3];

  /**
   * Constructs a tree view component with the specified tree model.
   * @param d the specified tree model.
   */
   public LwTree(TreeModel d) {
     this(d, true);
   }

  /**
   * Constructs a tree view component with the specified tree model and tree node state.
   * The state defines if all nodes that have children should be opened or closed.
   * @param d the specified tree model.
   * @param b the specified tree node state.
   */
   public LwTree(TreeModel d, boolean b)
   {
     setColor(SEL1_COLOR, (Color)LwToolkit.getStaticObj("sel1.color"));
     setColor(SEL2_COLOR, (Color)LwToolkit.getStaticObj("sel2.color"));
     setColor(LN_COLOR,   (Color)LwToolkit.getStaticObj("sel2.color"));

     isOpenVal = b;
     setModel(d);
     setViewProvider(this);
     setView(TOGGLE_ON_VIEW,  LwToolkit.getView("toggle.on"));
     setView(TOGGLE_OFF_VIEW, LwToolkit.getView("toggle.off"));
     setView(LEAST_VIEW,      LwToolkit.getView("tree.least"));
     setView(OPENED_VIEW,     LwToolkit.getView("tree.open"));
     setView(CLOSED_VIEW,     LwToolkit.getView("tree.close"));
     enableSelection(true);
   }

   public /*C#override*/ boolean canHaveFocus() {
     return true;
   }

  /**
   * Enables the tree node selecting.
   * @param b use <code>true</code> to enable the tree node selecting;
   * <code>false</code> otherwise.
   */
   public void enableSelection(boolean b)
   {
     if (isSelectionEnabled() != b)
     {
       if (!b && selected != null) select(null);
       bits = MathBox.getBits(bits, ENSEL_BIT, b);
       repaint();
     }
   }

  /**
   * Tests if the tree items selection is enabled.
   * @return <code>true</code> if the tree items selection is enabled;
   * <code>false</code> otherwise.
   */
   public boolean isSelectionEnabled() {
     return MathBox.checkBit(bits, ENSEL_BIT);
   }

  /**
   * Gets the color for the specified element.
   * @param type the specified element type.
   * @return a color.
   */
   public Color getColor(int type) {
     return colors[type];
   }

  /**
   * Sets the specified color to render the given element. Use the SEL1_COLOR, SEL2_COLOR
   * or LN_COLOR constant as the type value. To disable the tree lines rendering, set the LN_COLOR
   * to the <code>null</code> value.
   * @param type the specified element type.
   * @param c the specified color.
   */
   public void setColor(int type, Color c) {
     colors[type] = c;
     repaint();
   }

  /**
   * Sets the specified vertical and horizontal gaps. The method doesn't touch
   * a gap in case if it is less zero, so it is possible to use the method to
   * set only vertical or only horizontal gap.
   * @param gx the specified horizontal gap.
   * @param gy the specified vertical gap.
   */
   public void setGaps (int gx, int gy)
   {
     if ((gx >= 0 && gx != gapx)||
         (gy >= 0 && gy != gapy)  )
     {
       gapx = gx<0?gapx:gx;
       gapy = gy<0?gapy:gy;
       vrp();
     }
   }

  /**
   * Gets the tree model that is rendered with the tree view component.
   * @return a tree model.
   */
   public TreeModel getModel() {
     return data;
   }

  /**
   * Sets the specified view provider. The provider is used to define a view that
   * will be used to paint the node. It is possible to use <code>null</code> value
   * as the input argument, in this case the LwTextRender view is used (the method
   * sets itself as the view provider) to paint the node.
   * @param p the specified view provider.
   */
   public void setViewProvider (LwViewProvider p)
   {
     if (p == null) p = this;
     if (provider != p)
     {
       provider = p;

       // force nodes re-creation and re-calculating
       if (nodes != null) nodes.clear();

       repaint();
     }
   }

  /**
   * Gets the view provider.
   * @return a view provider.
   */
   public LwViewProvider getViewProvider () {
     return provider;
   }

  /**
   * Sets the specified view for the specified tree view element. Use one of the following
   * constants as the view element id:
   * <ul>
   *   <li>
   *      LEAST_VIEW - to define least node element.
   *   </li>
   *   <li>
   *      OPENED_VIEW - to define opened node element.
   *   </li>
   *   <li>
   *      CLOSED_VIEW - to define closed node element.
   *   </li>
   *   <li>
   *      TOGGLE_OFF_VIEW - to define toggle off node element.
   *   </li>
   *   <li>
   *      TOGGLE_ON_VIEW - to define toggle on node element.
   *   </li>
   * </ul>
   * @param id the specified element id.
   * @param v the specified view.
   */
   public void setView(int id, LwView v)
   {
     if (views[id] != v)
     {
       views[id] = v;
       viewSizes[id] = viewPS(v);
       vrp();
     }
   }

  /**
   * Sets the specified tree model to be rendered with the component.
   * @param d the specified tree model.
   */
   public void setModel(TreeModel d)
   {
     if (data != d)
     {
       select(null);
       if (data != null) data.removeTreeListener(this);
       data = d;
       if (data != null) data.addTreeListener(this);
       firstVisible = null;
       vrp();
     }
   }

  /**
   * Paints this component.
   * @param g the graphics context to be used for painting.
   */
   public /*C#override*/ void paint (Graphics g)
   {
     if (data != null)
     {
       vVisibility();
       if (firstVisible != null) paintTree (g, firstVisible);
     }
   }

  /**
   * Adds the specified selection listener to be notified whenever the tree item has been
   * selected, deselected.
   * @param l the specified selection listener.
   */
   public void addSelectionListener(LwActionListener l) {
     if (selection == null) selection = new LwActionSupport();
     selection.addListener(l);
   }

  /**
   * Removes the specified selection listener.
   * @param l the specified selection listener.
   */
   public void removeSelectionListener(LwActionListener l) {
     if (selection != null) selection.removeListener(l);
   }

   public LwView getView(Object d, Object obj) {
     defaultRender.getTextModel().setText(obj.toString());
     return defaultRender;
   }

   public void mouseClicked (LwMouseEvent e)  {}

   public void mouseReleased(LwMouseEvent e) {}

   public void mouseEntered (LwMouseEvent e) {}
   public void mouseExited  (LwMouseEvent e) {}
   public void keyReleased  (LwKeyEvent e) {}

   public void keyTyped (LwKeyEvent e)
   {
     if (selected != null)
     {
       switch (e.getKeyChar())
       {
         case '+' : if (!isOpen(selected)) toggle(selected); break;
         case '-' : if (isOpen(selected)) toggle(selected); break;
       }
     }
   }

   public void keyPressed (LwKeyEvent e)
   {
     Item newSelection = null;
     switch (e.getKeyCode())
     {
       case LwToolkit.VK_DOWN :
       case LwToolkit.VK_RIGHT: newSelection = findNext (selected); break;

       case LwToolkit.VK_UP   :
       case LwToolkit.VK_LEFT : newSelection = findPrev (selected); break;

       case LwToolkit.VK_HOME : select(data.getRoot()); break;
       case LwToolkit.VK_END  : select(findLast(data.getRoot())); break;
       case LwToolkit.VK_PAGE_DOWN : if (selected != null) select(nextPage(selected, 1));
                                 break;
       case LwToolkit.VK_PAGE_UP   : if (selected != null) select(nextPage(selected, -1));
                                 break;
     }
     if (newSelection != null) select(newSelection);
   }

   public void mousePressed (LwMouseEvent e)
   {
     if (firstVisible != null && LwToolkit.isActionMask(e.getMask()))
     {
       int x = e.getX(), y = e.getY();
       Item root = getItemAt(firstVisible, x, y);
       if (root != null)
       {
         x -= dx;
         y -= dy;
         Rectangle r = getToggleBounds(root);
         if (r.contains(x, y)) toggle(root);
         else
         {
           if (x > r.x + r.width) select(root);
         }
       }
     }
   }

  /**
   * Switches a toggle state for the specified tree item. If the toggle state
   * is "on" than the method switches it to "off" and the "off" state will be
   * switched to "on" state.
   * @param item the specified tree item.
   */
   public void toggle(Item item)
   {
     validate();

     ItemMetrics node = getIM (item);
     node.isOpenValue = !node.isOpenValue;
     invalidate();
     if (actions != null)  actions.perform (this, item);
     if (!node.isOpenValue && selected != null)
     {
       Item parent = selected;
       do {
         parent = data.getParent(parent);
       } while (parent != item && parent != null);
       if (parent == item) select(item);
     }
     repaint();
   }

  /**
   * Selects the specified node of the tree.
   * @param item the specified node.
   */
   public void select(Item item)
   {
     if (isSelectionEnabled())
     {
       if (item != selected)
       {
         Item old = selected;
         selected = item;
         if (selected != null)
         {
           validate();
           Rectangle r = getViewBounds(selected);
           if (man != null) man.makeVisible(r.x, r.y, r.width, r.height);
           else
           {
             Point o = LwToolkit.calcOrigin(r.x, r.y, r.width, r.height, this);
             setSOLocation(o.x, o.y);
           }
         }
         if (selection != null) selection.perform (this, selected);

         ItemMetrics m = null;
         if (old != null && isVerVisible(old))
         {
           m = getItemMetrics(old);
           repaint(m.x + dx, m.y + dy, m.width, m.height);
         }

         if (selected != null && isVerVisible(selected))
         {
           m = getItemMetrics(selected);
           repaint(m.x + dx, m.y + dy, m.width, m.height);
         }
       }
     }
   }

  /**
   * Gets the item that is selected at the moment.
   * @return a selected item.
   */
   public Item getSelectedItem() {
     return selected;
   }

  /**
   * Adds the specified action listener to receive action events from this tree.
   * The event is generated whenever the tree node has been expanded or collapsed.
   * The tree item that has been expanded or collapsed can be got as the event data
   * argument.
   * @param l the specified action listener.
   */
   public void addActionListener(LwActionListener l) {
     if (actions == null) actions = new LwActionSupport();
     actions.addListener(l);
   }

  /**
   * Removes the specified action listener.
   * @param l the specified action listener.
   */
   public void removeActionListener(LwActionListener l) {
     if (actions != null) actions.removeListener(l);
   }

   public void itemInserted(Object target, Item item) {
     vrp();
   }

   public void itemRemoved (Object target, Item item)
   {
     if (nodes != null)
     {
       if (item == firstVisible) firstVisible = null;
       if (item == selected) select(null);
       nodes.remove(item);
       vrp();
     }
   }

   public void itemModified(Object target, Item item)
   {
     if (nodes != null)
     {
       ItemMetrics node = getIM(item);
       if (node != null) node.viewWidth = -1;
       vrp();
     }
   }

  /**
   * Looks up and returns an item that is placed at the specified location.
   * The method searches the item only inside the visible component area.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @return an item that is placed at the specified location of the visible area.
   */
   public Item getItemAt (int x, int y) {
     validate();
     return firstVisible == null?null:getItemAt(firstVisible, x, y);
   }

   public /*C#override*/ void invalidate ()
   {
     if (isValid())
     {
       bits = MathBox.getBits(bits, VALVIS_BIT, false);
       super.invalidate();
     }
   }

   public /*C#override*/ boolean isInvalidatedByChild(LwComponent c) {
     return false;
   }

   public /*C#override*/ Point getOrigin() {
     return new Point(dx, dy);
   }

   public Point getSOLocation () {
     return getOrigin();
   }

   public void setSOLocation (int x, int y)
   {
     if (x != dx || y != dy)
     {
       dx = x;
       int prevDy = dy;
       dy = y;

       // Can be improved
       if (firstVisible == null) firstVisible = data.getRoot();
       firstVisible = (y < prevDy)?nextVisible(firstVisible):prevVisible(firstVisible);

       repaint();
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

  /**
   * Checks if the tree item is expanded. The item is expanded if it is expanded and
   * all parent items are expanded too.
   * @param i the specified tree item.
   * @return <code>true</code> if the item and its parent item is expanded too;
   * <code>false</code> otherwise.
   */
   public boolean isOpen(Item i) {
     validate();
     return isOpen_(i);
   }

  /**
   * Gets the item metric for the specified item.
   * @param i the specified item.
   * @return an item metric.
   */
   public ItemMetrics getItemMetrics (Item i) {
     validate();
     return getIM (i);
   }

   protected /*C#override*/ Dimension calcPreferredSize() {
     return data == null?super.calcPreferredSize():new Dimension (maxw, maxh);
   }

   protected /*C#override*/ void laidout() {
     vVisibility();
   }

  /**
   * Validates the tree component visibility.
   */
   protected /*C#virtual*/ void vVisibility()
   {
     if (data == null) firstVisible = null;
     else
     {
       Rectangle nva = getVisiblePart();
       if (nva == null)
       {
         firstVisible = null;
       }
       else
       {
         if (!MathBox.checkBit(bits, VALVIS_BIT) ||
             !nva.equals(visibleArea))
         {
           visibleArea = nva;
           if (firstVisible != null)
           {
             firstVisible = findOpened(firstVisible);
             firstVisible = isAbove(firstVisible)?nextVisible(firstVisible):prevVisible(firstVisible);
           }
           else
             firstVisible = (-dy > maxh/2)?prevVisible(findLast(data.getRoot())):nextVisible(data.getRoot());
         }
       }
     }
     bits = MathBox.getBits(bits, VALVIS_BIT, true);
   }

   protected /*C#override*/ void recalc()
   {
     maxw = 0;
     maxh = 0;
     if (data != null && data.getRoot() != null)
     {
       recalc (getLeft(), getTop(), null, data.getRoot(), true);

       // correct the width according to the left insets
       maxw -= getLeft();

       // correct height since it the gapy for the last item is not necessary
       maxh -= gapy;
     }
   }

   private int recalc(int x, int y, Item parent, Item root, boolean isVis)
   {
     ItemMetrics node = getIM(root);
     if (node == null)
     {
       node = new ItemMetrics(isOpenVal);
       if (nodes == null) nodes = new Hashtable();
       nodes.put(root, node);
     }

     if (isVis)
     {
        if (node.viewWidth < 0)
        {
          LwView nodeView = provider.getView(this, root);
          Dimension viewSize = nodeView.getPreferredSize();
          node.viewWidth  = viewSize.width == 0?5:viewSize.width + itemGapX*2;
          node.viewHeight = viewSize.height + itemGapY*2;
        }

        Dimension imageSize  = getImageSize (root);
        Dimension toggleSize = getToggleSize(root);
        if (parent != null)
        {
          Rectangle pImg = getImageBounds(parent);
          x = pImg.x + (pImg.width - toggleSize.width)/2;
        }

        node.x      = x;
        node.y      = y;
        node.width  = toggleSize.width + imageSize.width + node.viewWidth +
                      (toggleSize.width>0?gapx:0) + (imageSize.width>0?gapx:0);
        node.height = Math.max(Math.max (toggleSize.height, imageSize.height), node.viewHeight);

        if (node.x + node.width > maxw) maxw = node.x + node.width;
        maxh += (node.height + gapy);

        x = node.x + toggleSize.width + (toggleSize.width>0?gapx:0);
        y += (node.height + gapy);
     }

     //!!!
     // Take care that it is necessary to use method isOpen(Item), but the method
     // implementation works correctly if the field isOpenVal is used. The feature allows
     // speed up performance for the method.
     //!!!
     int count = data.getChildrenCount(root);
     boolean b = node.isOpenValue && isVis;
     for (int i=0; i<count; i++) y = recalc(x, y, root, data.getChildAt(root, i), b);

     return y;
   }

   private boolean isOpen_(Item i) {
     return (i == null || (data.hasChildren(i) && getIM(i).isOpenValue && isOpen_(data.getParent(i))));
   }

   private ItemMetrics getIM(Item i) {
     return (nodes == null)?null:(ItemMetrics)nodes.get(i);
   }

   private Item getItemAt (Item root, int x, int y)
   {
     if (y >= visibleArea.y && y < visibleArea.y + visibleArea.height)
     {
       Item found = getItemAtInBranch(root, x - dx, y - dy);
       if (found != null) return found;

       Item parent = data.getParent(root);
       while(parent != null)
       {
         int count = data.getChildrenCount(parent);
         for (int i=data.getChildIndex(root) + 1; i<count; i++)
         {
           found = getItemAtInBranch (data.getChildAt(parent, i), x - dx, y - dy);
           if (found != null) return found;
         }
         root   = parent;
         parent = data.getParent(root);
       }
     }
     return null;
   }

   private Item getItemAtInBranch (Item root, int x, int y)
   {
     if (root != null)
     {
       ItemMetrics node = getIM(root);
       if (x >= node.x && y >= node.y && x < node.x + node.width && y < node.y + node.height + gapy)
         return root;

       if (isOpen_(root))
       {
         for (int i=0; i<data.getChildrenCount(root); i++)
         {
           Item res = getItemAtInBranch(data.getChildAt(root, i), x, y);
           if (res != null) return res;
         }
       }
     }
     return null;
   }

   private LwView getImageView (Item i) {
     return data.hasChildren(i)?(getIM(i).isOpenValue?views[OPENED_VIEW]:views[CLOSED_VIEW]):views[LEAST_VIEW];
   }

   private Dimension getImageSize (Item i) {
     return data.hasChildren(i)?(getIM(i).isOpenValue?viewSizes[OPENED_VIEW]:viewSizes[CLOSED_VIEW]):viewSizes[LEAST_VIEW];
   }

   private Rectangle getImageBounds (Item root)
   {
     ItemMetrics node = getIM(root);
     Dimension id = getImageSize(root);
     Dimension td = getToggleSize(root);
     return new Rectangle(node.x + td.width + (td.width>0?gapx:0), node.y + (node.height - id.height)/2, id.width, id.height);
   }

   private Rectangle getToggleBounds (Item root)
   {
     ItemMetrics node = getIM(root);
     Dimension d = getToggleSize(root);
     return new Rectangle(node.x, node.y + (node.height - d.height)/2, d.width, d.height);
   }

   private Rectangle getViewBounds(Item root)
   {
     ItemMetrics metrics = getIM(root);
     Rectangle toggle = getToggleBounds(root);
     Rectangle image  = getImageBounds(root);
     return new Rectangle(image.x + image.width + (image.width > 0 || toggle.width > 0 ?gapx:0),
                          metrics.y + (metrics.height - metrics.viewHeight)/2,
                          metrics.viewWidth, metrics.viewHeight);
   }

   private LwView getToggleView (Item i) {
     return data.hasChildren(i)?(getIM(i).isOpenValue?views[TOGGLE_ON_VIEW]:views[TOGGLE_OFF_VIEW]):null;
   }

   private Dimension getToggleSize (Item i)  {
     return isOpen_(i)?viewSizes[TOGGLE_ON_VIEW]:viewSizes[TOGGLE_OFF_VIEW];
   }

   private boolean isAbove (Item i){
     ItemMetrics node = getIM(i);
     return node.y + node.height + dy < visibleArea.y;
   }

   private Item findOpened (Item item) {
     Item parent = data.getParent(item);
     return (parent == null || isOpen_(parent))?item:findOpened(parent);
   }

   private Item findNext(Item item)
   {
     if (item != null)
     {
       if (data.hasChildren(item) && isOpen_(item)) return data.getChildAt(item, 0);

       Item parent = null;
       while ((parent = data.getParent(item)) != null)
       {
         int index = data.getChildIndex(item);
         if (index + 1 < data.getChildrenCount(parent)) return  data.getChildAt(parent, index + 1);
         item = parent;
       }
     }
     return null;
   }

   private Item findPrev(Item item)
   {
     if (item != null)
     {
       Item parent = data.getParent(item);
       if (parent != null)
       {
         int index = data.getChildIndex(item);
         return (index - 1 >= 0)?findLast(data.getChildAt(parent, index - 1)):parent;
       }
     }
     return null;
   }

   private Item findLast(Item item) {
     int count = data.getChildrenCount(item);
     return (count == 0 || !isOpen_(item))?item:findLast(data.getChildAt(item, count - 1));
   }

   private Item prevVisible(Item item)
   {
     if (item == null || isAbove(item)) return nextVisible(item);

     Item parent = null;
     while ((parent = data.getParent(item)) != null)
     {
       for (int i=data.getChildIndex(item)-1; i>=0; i--)
       {
         Item child = data.getChildAt(parent, i);
         if (isAbove(child)) return nextVisible(child);
       }
       item = parent;
     }
     return item;
   }

   private boolean isVerVisible(Item item)
   {
     if (visibleArea == null) return false;
     ItemMetrics node = getIM(item);
     int yy1 = node.y + dy, yy2 = yy1 + node.height - 1, by = visibleArea.y + visibleArea.height;
     return  ((visibleArea.y <= yy1 && yy1 < by)||
              (visibleArea.y <= yy2 && yy2 < by)||
              (visibleArea.y > yy1  && yy2 >= by)  );
   }

   private Item nextVisible(Item item)
   {
     if (item == null || isVerVisible(item)) return item;

     Item res = nextVisibleInBranch(item);
     if (res != null) return res;

     Item parent = null;
     while ((parent = data.getParent(item)) != null)
     {
       int count = data.getChildrenCount(parent);
       for (int i=data.getChildIndex(item) + 1; i<count; i++)
       {
         res = nextVisibleInBranch(data.getChildAt(parent, i));
         if (res != null) return res;
       }
       item = parent;
     }
     return null;
   }

  /*
   * [checked]
   **/
   private Item nextVisibleInBranch (Item item)
   {
     if (isVerVisible(item)) return item;
     if (isOpen_(item))
     {
        for (int i=0; i < data.getChildrenCount(item); i++)
        {
          Item res = nextVisibleInBranch(data.getChildAt(item, i));
          if (res != null) return res;
        }
     }
     return null;
   }

   private void paintTree (Graphics g, Item item)
   {
     paintBranch(g, item);
     Item parent = null;
     while ((parent = data.getParent(item)) != null)
     {
       paintChild (g, parent, data.getChildIndex(item)+1);
       item = parent;
     }
   }

   private boolean paintBranch(Graphics g, Item root)
   {
     if (root == null) return false;

     ItemMetrics node = getIM(root);
     if (visibleArea.intersects(new Rectangle(node.x + dx, node.y + dy , node.width, node.height)))
     {
       Rectangle toggle     = getToggleBounds(root);
       LwView    toggleView = getToggleView(root);
       if (toggleView != null) toggleView.paint(g, toggle.x, toggle.y, this);

       Rectangle image = getImageBounds(root);
       if (image.width > 0) getImageView (root).paint(g, image.x, image.y, this);

       int vx = image.x + image.width + (image.width > 0 || toggle.width > 0 ?gapx:0);
       int vy = node.y + (node.height - node.viewHeight)/2;
       provider.getView(this, root).paint(g, vx + itemGapX, vy + itemGapY, this);

       if (colors[LN_COLOR] != null)
       {
         g.setColor (colors[LN_COLOR]);
         LwToolkit.drawDotHLine(g, toggle.x + (toggleView==null?toggle.width/2 + 1:toggle.width) + 1, image.x - 1, toggle.y + toggle.height/2);
       }

       if (selected == root)
       {
         Color selectColor = colors[hasFocus()?SEL1_COLOR:SEL2_COLOR];
         if (selectColor != null)
           LwToolkit.drawMarker(g, vx, vy, node.viewWidth, node.viewHeight, getBackground(), selectColor);
       }
     }
     else
     {
       if (node.y + dy > visibleArea.y + visibleArea.height||
           node.x + dx > visibleArea.x + visibleArea.width   ) return false;
     }
     return paintChild (g, root, 0);
   }

   private int y_(Item item, boolean isStart)
   {
     Rectangle r = getToggleBounds(item);
     int       y = (data.hasChildren(item))?(isStart?r.y + r.height:r.y - 1):r.y + r.height/2;

     if (y + dy > height) y = height - dy;
     else
     {
       if (y + dy < 0) y = -dy;
     }
     return y;
   }

   private boolean paintChild (Graphics g, Item root, int index)
   {
     if (isOpen_(root))
     {
       Rectangle tb = getImageBounds(root);
       Item firstChild = data.getChildAt(root, 0);
       int x = getIM(firstChild).x + (isOpen_(firstChild)?viewSizes[TOGGLE_ON_VIEW].width:viewSizes[TOGGLE_OFF_VIEW].width)/2;
       int count = data.getChildrenCount(root);
       if (index < count)
       {
         int y = (index > 0)?y_(data.getChildAt(root, index-1), true):tb.y + tb.height;
         for (int i=index; i<count; i++)
         {
           Item child = data.getChildAt(root, i);
           if (colors[LN_COLOR] != null)
           {
             g.setColor(colors[LN_COLOR]);
             LwToolkit.drawDotVLine(g, y, y_(child, false), x);
             y = y_(child, true);
           }

           if (!paintBranch(g, child))
           {
             if (colors[LN_COLOR] != null && i + 1 != count)
             {
               g.setColor(colors[LN_COLOR]);
               LwToolkit.drawDotVLine(g, y, height - dy, x);
             }
             return false;
           }
         }
       }

       if (colors[LN_COLOR] != null && index == count && count > 0)
       {
         g.setColor(colors[LN_COLOR]);
         LwToolkit.drawDotVLine(g, y_(root, true), y_(data.getChildAt(root, index-1), false), x);
       }
     }
     return true;
   }

   private Item nextPage(Item item, int dir)
   {
     int sum = 0;
     Item prev = item;
     while (item != null && sum < visibleArea.height)
     {
       sum += (getIM(item).height + gapy);
       prev = item;
       item = dir < 0?findPrev(item):findNext(item);
     }
     return prev;
   }

   private Dimension viewPS(LwView v) {
     return (v == null)?ZERO:v.getPreferredSize();
   }

   private static final int ENSEL_BIT  = 32;
   private static final int VALVIS_BIT = 128;

   /* To speed up */
   private static final Dimension ZERO = new Dimension();
}


