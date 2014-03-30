/*
 * UrlForm.java
 *
 * Created on 28. èerven 2004, 10:23
 */

package mobile;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/**
 *
 * @author  iz148508
 * @version
 */
public class UrlForm extends Form implements CommandListener, ReturnableScreen{
    
    private Command goCommand; 
    private Command backCommand;
    private TextField urlField;
    private ReturnableScreen parent;
    private Display display;
  
    public UrlForm(ReturnableScreen parent, Display display) {
        super("Enter URL");
        this.parent= parent;
        this.display= display;
        goCommand= new Command("CONNECT",Command.OK, 1);
        backCommand = new Command("BACK", Command.BACK, 2);
        this.addCommand(goCommand);
        this.addCommand(backCommand);
        this.setCommandListener(this);
        urlField=new TextField("Url: ","http://", 128, TextField.URL);
        this.append(urlField);
        
    }
    
    public void setUrl(String newUrl) {
        urlField.setString(newUrl);
    }
    
    public String getUrl() {
        return urlField.getString();
    }
    
    Alert a;
    
    public void commandAction(Command cmd, Displayable target) {
       if (cmd == backCommand) 
          parent.goBack();
       else if (cmd== goCommand) {
           String url= getUrl();
           if (checkUrl(url)) {
               History.getHistory().addItem(url);
               Request.connect(url,this,display);
           }
           else {
               if (a==null) { 
                   a=new Alert("Invalid URL", "URL is not valid!", null, AlertType.ERROR);
                   a.setTimeout(2000);
               }
               display.setCurrent(a);
           
           }
       }
        
   }
    
    private boolean checkUrl(String url) {
    if (url== null || "".equals(url)) return false; 
    if (!url.startsWith("http://")) return false;
    if (url.length()<8) return false;
    return true;
    }
    
    public void goBack() {
        display.setCurrent(this);
    }
    
}
