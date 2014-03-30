package org.zaval.lw.demo;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import org.zaval.lw.tree.*;
import org.zaval.data.*;
import org.zaval.data.Item;

public class TreeTestMidlet
extends MIDlet
{
  protected void startApp()
  {
    METoolkit.startVCL(null);

    Display display = Display.getDisplay(this);

    Form  mainForm = new Form("String Item Demo");

    Item root      = new Item("base");
    Tree treeModel = new Tree(root);
    treeModel.add (root, new Item("Item 1"));
    treeModel.add (root, new Item("Item 2"));
    treeModel.add (root, new Item("Item 3"));
    METree tree = new METree(treeModel);
    tree.select(root);

    mainForm.append(tree);
    display.setCurrent(mainForm);

  }

  protected void destroyApp(boolean unconditional) {
  }

  protected void pauseApp() {
  }
}
