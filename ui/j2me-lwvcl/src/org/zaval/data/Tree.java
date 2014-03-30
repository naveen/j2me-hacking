package org.zaval.data;

import java.util.*;
import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This class is an implementation of the <code>TreeModel</code> interface to organize
 * tree-like structures. The class supports listener interface that allows you to listen when
 * the structure has been modified.
 */
public class Tree
implements TreeModel
{
  private Item      root;
  private Hashtable elements = new Hashtable();
  private Vector    support;

 /**
  * Constructs a new tree class. The constructor defines a root item as default.
  */
  public Tree() {
    this(new Item());
  }

 /**
  * Constructs a new tree with the given item as a root of the hierarchy.
  * @param r the initial value of the container root. The root item
  * will be used as root of the hierarchy. Use <code>null</code> value to postpone
  * the root item definition.
  */
  public Tree(Item r) {
    setRoot(r);
  }

 /**
  * Sets the specified item as the root of the tree model.
  * @param r the new root item.
  */
  public void setRoot (Item r)
  {
    if (root != null || r == null) throw new IllegalArgumentException();
    root = r;
    regItem(r, null, null);
  }

  public Item getRoot() {
    return root;
  }

  public int getItemsCount() {
    return elements.size();
  }

 /**
  * Gets all child items for the root item of the tree.
  * @return an array of all child items for the root item.
  */
  public Item[] getChildren() {
    return getChildren(getRoot());
  }

 /**
  * Gets all child items for the specified item.
  * @param item the item that is used as a root to get a child items array.
  * @return an array of all child items for the given root item.
  */
  public Item[] getChildren(Item item)
  {
    ItemDesc desc = getItemDesc(item);
    if (desc.children == null) return new Item[0];

    Item[] items = new Item[desc.children.size()];
    for (int i=0; i<items.length; i++) items[i] = (Item)desc.children.elementAt(i);
    return items;
  }

  public Item getChildAt(Item item, int index) {
    return (Item)getItemDesc(item).children.elementAt(index);
  }

  public int getChildIndex (Item item)
  {
    if (contains(item))
    {
      Item parent = getParent(item);
      return parent != null?getItemDesc(parent).children.indexOf(item):0;
    }
    return -1;
  }

  public Item getParent (Item item) {
    return getItemDesc(item).parent;
  }

  public int getChildrenCount(Item item) {
    ItemDesc desc = getItemDesc(item);
    return (desc.children == null?0:desc.children.size());
  }

  public boolean hasChildren(Item item) {
    return getChildrenCount(item) > 0;
  }

 /**
  * Tests if the tree model contains the specified item.
  * @param item  the specified item to be tested.
  * @return  <code>true</code> if the tree model contains the given item;
  *          <code>false</code> otherwise.
  */
  public boolean contains(Item item) {
    return elements.get(item) != null;
  }

  public void add (Item to, Item item) {
    insert(to, item, getChildrenCount(to));
  }

  public void insert (Item to, Item item, int index)
  {
    if (index < 0) throw new IllegalArgumentException ();
    ItemDesc desc = getItemDesc(to);
    if (desc.children == null) desc.children = new Vector(5);
    desc.children.insertElementAt(item, index);
    regItem(item, to, null);
  }

  public void remove(Item item)
  {
    ItemDesc desc = getItemDesc(item);
    if (desc.children != null)
      while (desc.children.size() != 0)
        remove((Item)desc.children.elementAt(0));

    unregItem(item);
  }

 /**
  * Deletes a child item from the specified parent item at the specified index.
  * @param parent the parent item for what the child item will be removed.
  * @param index the index of the child item to be removed.
  */
  public void removeChild(Item parent, int index) {
    remove((Item)getItemDesc(parent).children.elementAt(index));
  }

 /**
  * Deletes all child items for the specified parent item.
  * @param item the parent item for what all child items will be removed.
  */
  public void removeKids(Item item) {
    Item[] items = getChildren(item);
    for (int i=0; i<items.length; i++) remove(items[i]);
  }

 /**
  * Creates a sub-tree for the specified item of this tree. The specified item
  * is set as the root item for the sub-tree.
  * @param item the specified item.
  * @return a clone of the item hierarchy.
  */
  public TreeModel clone(Item item)
  {
    Item  root = new Item(item);
    Tree  res  = new Tree(root);
    clone(res, root, getItemDesc(item));
    return res;
  }

  public void addTreeListener (TreeListener l) {
    if (support == null) support = new Vector(1);
    if (!support.contains(l)) support.addElement(l);
  }

  public void removeTreeListener(TreeListener l) {
    if (support != null) support.removeElement(l);
  }

 /**
  * Sets the specified value for the given tree item.
  * @param item the item.
  * @param o the specified value.
  */
  public void set (Item item, Object o)
  {
    if (elements.get(item) == null) throw new IllegalArgumentException();
    item.setValue(o);
    perform (MODIFIED, item);
  }

 /**
  * The method is used to register the specified item as a member of the tree model.
  * The new item is bound with the given parent item.
  * @param item the item that has to be registered as a member of the tree.
  * @param parent the parent of the registered item.
  * @param v the vector of child items for the given parent item.
  */
  protected void regItem(Item item, Item parent, Vector v) {
    elements.put(item, new ItemDesc(v, parent));
    perform (INSERTED, item);
  }

 /**
  * The method is used to un-register the specified item as a member of the tree model.
  * @param item the item that has to be unregistered as a member of this tree.
  */
  protected void unregItem(Item item)
  {
    ItemDesc desc = getItemDesc(item);
    if (desc.parent != null) getItemDesc(desc.parent).children.removeElement(item);
    elements.remove(item);
    if (item == root) root = null;
    perform (REMOVED, item);
  }

 /**
  * Fires the specified tree event to the tree model listeners.
  * @param id the tree event id.
  * @param item the tree item.
  */
  protected void perform(int id, Item item)
  {
    if (support != null)
    {
      for (int i=0; i<support.size(); i++)
      {
        TreeListener l = (TreeListener)support.elementAt(i);
        switch (id)
        {
          case INSERTED: l.itemInserted(this, item); break;
          case REMOVED : l.itemRemoved (this, item); break;
          case MODIFIED: l.itemModified(this, item); break;
        }
      }
    }
  }

 /**
  * Returns an internal description of the given item. The description provides information
  * about a parent item and child items.
  * @return an internal description of the item.
  */
  ItemDesc getItemDesc(Item item) {
    return (ItemDesc)elements.get(item);
  }

  private static void clone (Tree res, Item root, ItemDesc desc)
  {
    if (desc.children != null)
    {
      for (int i=0; i < desc.children.size(); i++)
      {
        Item originalItem = (Item)desc.children.elementAt(i);
        Item item         = new Item(originalItem);
        res.add(root, item);
        clone(res, item, res.getItemDesc(originalItem));
      }
    }
  }

  private static final int INSERTED = 1;
  private static final int REMOVED  = 2;
  private static final int MODIFIED = 3;
}

class ItemDesc
{
  protected Vector children;
  protected Item   parent;

  protected ItemDesc(Vector c, Item p) {
    children = c;
    parent = p;
  }
}





