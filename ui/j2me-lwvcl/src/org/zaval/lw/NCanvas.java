package org.zaval.lw;

import java.util.*;
import org.zaval.util.*;
import org.zaval.lw.event.*;
import org.zaval.lw.*;
import org.zaval.port.j2me.*;
import javax.microedition.lcdui.*;

public class NCanvas
extends Canvas
{
  private LwDesktop desktop;
  private LwComponent draggOwner, pressOwner;
  private int pressX, pressY, pressedKeyCode, gameAction;
  private org.zaval.port.j2me.Graphics targetGr = new org.zaval.port.j2me.Graphics(null);

  boolean enableRepaint = true;

  public NCanvas(LwDesktop d) {
    LwInputController.controller = new LwInputController(1000);
    desktop = d;
  }

  public void paint (javax.microedition.lcdui.Graphics g)
  {
    synchronized (LwPaintManager.LOCKER)
    {
      //System.out.println ("1. NCanvas.paint () enableRepaint = " + enableRepaint + ", CLIP = [ " + g.getClipX() + "," + g.getClipY() + "," + g.getClipWidth() + "," + g.getClipHeight() + "]");

      targetGr.setTarget(g);
      LwToolkit.getPaintManager().startPaint(targetGr, desktop);

      //System.out.println ("2. NCanvas.paint () enableRepaint = " + enableRepaint);
      if (!enableRepaint) enableRepaint = true;
    }
  }

  protected void showNotify()
  {
    synchronized (LwPaintManager.LOCKER)
    {
      enableRepaint = false;
      desktop.setSize(getWidth(), getHeight());
      enableRepaint = true;
    }
  }

  protected void hideNotify() {
    desktop.getRootLayer().releaseFocus();
  }

  protected void sizeChanged(int w, int h) {
    desktop.setSize(w, h);
  }

  protected void pointerPressed(int x, int y)
  {
    pressX = x;
    pressY = y;

    desktop.getRootLayer().mousePressed(pressX, pressY, LwToolkit.BUTTON1_MASK);

    LwComponent d = desktop.getLwComponentAt(pressX, pressY);
    if (isEventable(d))
    {
      pressOwner = d;
      fme(d, LwMouseEvent.MOUSE_PRESSED, x, y, 0);
    }
  }

  protected void pointerReleased(int x, int y)
  {
    boolean drag = (draggOwner != null);

    if (drag)
    {
      fme(draggOwner, LwMouseEvent.MOUSE_ENDDRAGGED, x, y, 0);
      draggOwner = null;
    }

    if (pressOwner != null)
    {
      fme(pressOwner, LwMouseEvent.MOUSE_RELEASED, x, y, 0);
      if (!drag) fme(pressOwner, LwMouseEvent.MOUSE_CLICKED, x, y, 1);
      pressOwner = null;
    }
  }

 /**
  * Invoked when a key has been pressed.
  */
  protected void keyPressed(int keyCode)
  {
    LwComponent focusOwner = LwToolkit.getFocusManager().getFocusOwner();
    if (focusOwner != null)
    {
      gameAction     = getGameAction(keyCode);
      pressedKeyCode = LwInputController.controller.translate(keyCode, gameAction);

      //System.out.println ("gameAction = " + gameAction + ", keyCode = " + keyCode);

      fke (focusOwner, LwKeyEvent.KEY_PRESSED);
      if (pressedKeyCode == LwToolkit.VK_ENTER || (pressedKeyCode > 0 && gameAction == 0))
        fke (focusOwner, LwKeyEvent.KEY_TYPED);
    }
  }

 /**
  * Invoked when a key has been released.
  */
  protected void keyReleased(int keyCode) {
    LwComponent focusOwner = LwToolkit.getFocusManager().getFocusOwner();
    if (focusOwner != null) fke(focusOwner, LwKeyEvent.KEY_RELEASED);
  }

  protected void keyRepeated(int keyCode)
  {
    LwComponent focusOwner = LwToolkit.getFocusManager().getFocusOwner();
    if (focusOwner != null)
    {
      fke (focusOwner, LwKeyEvent.KEY_PRESSED);
      if (pressedKeyCode > 0 && gameAction == 0)
        fke (focusOwner, LwKeyEvent.KEY_TYPED);
    }
  }

 /**
  * Invoked when a mouse button is pressed on a component and then
  * dragged. Mouse drag events will continue to be delivered to
  * the component where the first originated until the mouse button is
  * released (regardless of whether the mouse position is within the
  * bounds of the component).
  */
  protected void pointerDragged(int x, int y)
  {
    if (draggOwner == null)
    {
      LwComponent d = desktop.getLwComponentAt(pressX, pressY);
      if (isEventable(d))
      {
        draggOwner = d;
        fme(draggOwner, LwMouseEvent.MOUSE_STARTDRAGGED, pressX, pressY, 0);
        if (pressX != x || pressY != y)
          fme(draggOwner, LwMouseEvent.MOUSE_DRAGGED, x, y, 0);
      }
    }
    else fme(draggOwner, LwMouseEvent.MOUSE_DRAGGED, x, y, 0);
  }

  /*[fire key event]*/
  private final void  fke(LwComponent target, int id) {
    KE_STUB.reset(target, id, pressedKeyCode, (char)pressedKeyCode, 0);
    LwToolkit.getEventManager().perform (KE_STUB);
  }

  /*[fire mouse event]*/
  private final void fme(LwComponent target, int id, int x, int y, int clicks) {
    ME_STUB.reset(target, id, x, y, clicks, LwToolkit.BUTTON1_MASK);
    LwToolkit.getEventManager().perform(ME_STUB);
  }

  private static final boolean isEventable(LwComponent c) {
    return c != null && c.isEnabled();
  }

  private LwKeyEvent     KE_STUB  = new LwKeyEvent   (desktop, LwKeyEvent.KEY_PRESSED, 0, 'x', 0);
  private LwMouseEvent   ME_STUB  = new LwMouseEvent (desktop, LwMouseEvent.MOUSE_PRESSED, 0, 0, 0, 1);
}

