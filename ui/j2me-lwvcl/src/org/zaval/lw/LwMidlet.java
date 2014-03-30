package org.zaval.lw;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;

public abstract class LwMidlet
extends MIDlet
implements Runnable
{
  private LwDesktop desktop = null;

  public LwContainer getRoot() {
    return desktop.getRootLayer();
  }

  public void run () {
    desktop.getRootLayer().setupFocus();
  }

  protected void startApp()
  {
    synchronized (LwPaintManager.LOCKER)
    {
       Display display = Display.getDisplay(this);
       if (desktop == null)
       {
         LwToolkit.startVCL(null);
         desktopCount++;
         desktop = new J2MEDesktop();

                //!!!
        ((NCanvas)desktop.getNCanvas()).enableRepaint = false;

         //System.out.println ("LwMidlet.startApp() >>>> ");


         display.setCurrent((Canvas)desktop.getNCanvas());
         init();
       }
       display.callSerially (this);

        //System.out.println ("LwMidlet.startApp() <<<< ");


       //!!!
       ((NCanvas)desktop.getNCanvas()).enableRepaint = true;
    }
  }

  protected void destroyApp(boolean b)
  {
    if (desktop != null)
    {
      desktopCount--;
      if (desktopCount == 0) LwToolkit.stopVCL();
      desktop = null;
    }
  }

  protected void pauseApp() {
    if (desktop != null) desktop.getRootLayer().releaseFocus();
  }

  protected abstract void init();

  private static int desktopCount;
}
