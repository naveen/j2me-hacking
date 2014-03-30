package org.zaval.lw.demo;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;

public class NoteTestMidlet
extends MIDlet
{
  protected void startApp()
  {
    METoolkit.startVCL(null);

    Display display = Display.getDisplay(this);

    Form  mainForm = new Form("String Item Demo");

    MENotebook note = new MENotebook();
    note.addPage("Page1", new StringItem("", "1"));
    note.addPage("Page2", new StringItem("", "1"));
    note.addPage("Page3", new StringItem("", "1"));
    note.select(1);
    mainForm.append(note);
    display.setCurrent(mainForm);

  }

  protected void destroyApp(boolean unconditional) {
  }

  protected void pauseApp() {
  }
}
