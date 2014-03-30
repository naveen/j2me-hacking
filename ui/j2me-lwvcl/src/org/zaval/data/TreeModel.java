package org.zaval.data;

import org.zaval.data.event.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This interface describes tree-like data models and helps controlling its content. Actually
 * you will get the following abilities:
 * <ul>
 *   <li>Support tree-hierarchy</li>
 *   <li>Modify tree-hierarchy</li>
 *   <li>Listen when the tree-hierarchy has been changed</li>
 * </ul>
 */
public interface TreeModel
{
 /**
  * Gets the root item of the tree model.
  * @return a root item of the tree model.
  */
  Item getRoot();

 /**
  * Gets the total items number in the tree model.
  * @return a total items number.
  */
  int getItemsCount();

 /**
  * Returns a parent for the given item.
  * @param item the specified item.
  * @return a parent item of the child item.
  */
  Item getParent (Item item);

 /**
  * Returns a child item for the given parent item at the specified index.
  * @param item the item that is used as a parent to get a child item by the index.
  * @param index the index into this parent child items vector.
  * @return the item at the specified index.
  */
  Item getChildAt(Item item, int index);

 /**
  * Searches for the first occurence of the given item in the parent items vector,
  * testing for equality using the <code>equals</code> method and returns the child index.
  * @param item the specified item.
  * @return the index of the first occurrence of the item in this parent items
  *         vector. Returns <code>-1</code> if the item is not member of the model.
  */
  int getChildIndex(Item item);

 /**
  * Returns a number of child items for the given parent item.
  * @param item the parent item.
  * @return a number of child items. Actually the method gets a size of the item
  * child vector.
  */
  int getChildrenCount(Item item);

 /**
  * Tests if the item has one or more child items.
  * @param item the item to test.
  * @return <code>true</code> if the item has one or more child items; <code>false</code>
  * otherwise
  */
  boolean hasChildren(Item item);

 /**
  * Adds the item into the tree as a child of the parent item.
  * The parent item has to belong to the tree.
  * @param to  the parent item.
  * @param item the child item that has to be added.
  */
  void add(Item to, Item item);

 /**
  * Inserts the specified item into the tree as a child of the parent item,
  * at the specified index. The parent item has to belong to the tree.
  * @param to  the specified parent item.
  * @param item the specified child item that has to be added.
  * @param index the specified index where the child has to be inserted.
  */
  void insert(Item to, Item item, int index);

 /**
  * Sets the specified value for the given tree item.
  * @param item the item.
  * @param value the specified value.
  */
  void set(Item item, Object value);

 /**
  * Removes the specified item from the tree. If the item has child items than the children
  * will be removed too. The method is recursive to remove all hierarchy that is bound with
  * this item.
  * @param item the item that has to be removed.
  */
  void remove(Item item);

 /**
  * Adds the specified tree listener to receive the tree events.
  * @param l the tree listener.
  */
  void addTreeListener (TreeListener l);

 /**
  * Removes the specified tree listener so that it no longer
  * receives tree events from this tree model.
  * @param l the tree listener.
  */
  void removeTreeListener(TreeListener l);
}

