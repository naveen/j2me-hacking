package org.zaval.lw;

import javax.microedition.lcdui.*;
import org.zaval.data.*;
import org.zaval.port.j2me.*;
import org.zaval.port.j2me.Graphics;


/**
 * This light weight component is used to show a text by using LwTextRender as the face view.
 */
public class LwLabel
extends LwCanvas
{
 /**
  * Constructs the component. The text string is set to "".
  */
  public LwLabel() {
    this("");
  }

 /**
  * Constructs the component with the specified string.
  * @param t the specified string.
  */
  public LwLabel(String t) {
    this (new SingleLineTxt(t));
  }

 /**
  * Constructs the component with the specified text model.
  * @param t the specified text model.
  */
  public LwLabel(TextModel t)  {
    getViewMan(true).setView(makeTextRender(t));
    setInsets(1,1,1,1);
  }

 /**
  * Constructs the component with the specified text render.
  * @param r the specified text render.
  */
  public LwLabel(LwTextRender r)  {
    getViewMan(true).setView(r);
    setInsets(1,1,1,1);
  }

 /**
  * Gets the face view as a LwTextReneder instance.
  * @return a text render instance.
  */
  public LwTextRender getTextRender() {
    return (LwTextRender)getViewMan(false).getView();
  }

 /**
  * Gets the text that is shown with the class as a string.
  * @return a string.
  */
  public String getText () {
    return getTextRender().getText();
  }

 /**
  * Gets the text model that is shown with the class. The text model is represented with
  * org.zaval.data.TextModel interface.
  * @return a text model.
  */
  public TextModel getTextModel () {
    return getTextRender().getTextModel();
  }

 /**
  * Sets the specified string to be shown with the class.
  * @param s the specified string.
  */
  public /*C#virtual*/ void setText (String s) {
    getTextRender().getTextModel().setText(s);
    repaint();
  }

 /**
  * Sets the text color. The method uses appropriate method of the text render
  * that is used as the face view.
  * @param c the specified text color.
  */
  public void setForeground(Color c)
  {
    if (!getForeground().equals(c))
    {
      getTextRender().setForeground(c);
      repaint();
    }
  }

 /**
  * Gets the text color.
  * @return a text color.
  */
  public Color getForeground() {
    return getTextRender().getForeground();
  }

 /**
  * The method gets default text render that will be used to render the
  * specified text model. The render is set as the face view for the component
  * during the component initialization.
  * @param t the specified text model.
  * @return a text render that is going to be used as the face view of the component.
  */
  protected /*C#virtual*/ LwTextRender makeTextRender(TextModel t) {
    return new LwTextRender(t);
  }
}
