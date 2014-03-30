package org.zaval.lw.demo;

import javax.microedition.lcdui.*;
import org.zaval.lw.*;
import org.zaval.misc.*;
import org.zaval.port.j2me.*;
import org.zaval.lw.grid.*;
import org.zaval.lw.tree.*;
import org.zaval.data.*;
import org.zaval.data.Item;

public class DemoMidlet
extends LwMidlet
{
  protected void init()
  {
    LwContainer root = getRoot();
    LwNotebook  note = new LwNotebook(LwToolkit.LEFT);

    note.addPage("Label", getLabelPanel());
    note.addPage("Button", getButtonPanel());
    note.addPage("Box", getCheckboxPanel());
    note.addPage("Text", getTextFieldPanel());
    note.addPage("List", getListPanel());
    note.addPage("Tree", getTreePanel());
    note.addPage("Grid", getGridPanel());

    root.setInsets(2,2,2,2);
    root.setLwLayout(new LwBorderLayout());
    root.add (LwBorderLayout.CENTER, note);
  }

  private static LwComponent getLabelPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));
    panel.add(new LwLabel("Single line label"));
    panel.add(new LwLabel(new Text("Multilines\nlabel")));
    return panel;
  }

  private static LwComponent getButtonPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));
    panel.add(new LwButton("Standard"));

    LwAdvViewMan man = new LwAdvViewMan();
    man.put("button.on", LwToolkit.getView("br.sunken"));
    man.put("button.off", LwToolkit.getView("br.raised"));
    LwButton winButton = new LwButton("WinButton");
    winButton.setViewMan(man);
    panel.add(winButton);

    return panel;
  }

  private static LwComponent getTextFieldPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));
    LwTextField tf1 = new LwTextField("123", 10);
    tf1.getViewMan(true).setBorder("br.sunken");
    panel.add(tf1);

    LwTextField tf2 = new LwTextField(new Text("Multiline\n textfiled \n ......."));
    tf2.getViewMan(true).setBorder("br.sunken");
    Dimension ps = tf2.getPreferredSize();
    tf2.setPSSize(ps.width, ps.height);
    panel.add(tf2);
    return panel;
  }

  private static LwComponent getListPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));
    LwList list = new LwList();
    list.setInsets(2,2,2,2);
    list.getViewMan(true).setBorder("br.sunken");
    for (int i=0; i<4; i++) list.add ("Item " + i);
    panel.add(list);
    return panel;
  }

  private static LwComponent getCheckboxPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));

    LwPanel center = new LwPanel();
    center.setLwLayout(new LwFlowLayout(LwToolkit.LEFT, LwToolkit.CENTER, LwToolkit.VERTICAL, 2));
    LwCheckbox ch1 = new LwCheckbox("Radio 1");
    ch1.setBoxType(LwCheckbox.RADIO);
    LwCheckbox ch2 = new LwCheckbox("Radio 2");
    ch2.setBoxType(LwCheckbox.RADIO);
    LwCheckbox ch3 = new LwCheckbox("Radio 3");
    ch3.setBoxType(LwCheckbox.RADIO);
    LwGroup gr = new LwGroup();
    ch1.setSwitchManager(gr);
    ch2.setSwitchManager(gr);
    ch3.setSwitchManager(gr);
    center.add (ch1);
    center.add (ch2);
    center.add (ch3);
    panel.add(new LwBorderPan(new LwLabel("Radio"), center));
    panel.add(new LwCheckbox("Checkbox"));
    return panel;
  }

  private static LwComponent getTreePanel()
  {
    LwPanel panel = new LwPanel();
    panel.setInsets(2,2,2,2);
    panel.setLwLayout(new LwBorderLayout());
    Item root      = new Item("base");
    Tree treeModel = new Tree(root);
    treeModel.add (root, new Item("Item 1"));
    treeModel.add (root, new Item("Item 2"));
    treeModel.add (root, new Item("Item 3"));
    LwTree tree = new LwTree(treeModel);
    panel.add(LwBorderLayout.CENTER, tree);
    tree.select(root);
    return panel;
  }

  private static LwComponent getGridPanel()
  {
    LwPanel panel = new LwPanel();
    panel.setInsets(4,4,4,4);
    panel.setLwLayout(new LwFlowLayout(LwToolkit.CENTER, LwToolkit.CENTER, LwToolkit.VERTICAL, 5));
    Matrix model = new Matrix(3, 3);
    for (int i=0; i<model.getRows(); i++)
      for (int j=0; j<model.getCols(); j++)
        model.put(i, j, "i [" + i + "," + j + "]");

    LwGrid        grid = new LwGrid(model);
    LwGridCaption cap  = new LwGridCaption(grid);
    for (int i=0; i<model.getCols(); i++)
      cap.putTitle (i, "Cap " + i);
    grid.add (LwGrid.TOP_CAPTION_EL, cap);

    panel.add(grid);
    return panel;
  }


}
