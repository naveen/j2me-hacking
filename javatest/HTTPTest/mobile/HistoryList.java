/*
 * HistoryList.java
 *
 * Created on 28. èerven 2004, 15:55
 */

package mobile;

import java.util.Enumeration;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

/**
 *
 * @author  iz148508
 * @version
 */
public class HistoryList extends  List implements CommandListener, ReturnableScreen
{
    
    public Command backCommand;
    private Display display;
    private ReturnableScreen parent;
    public HistoryList(ReturnableScreen parent, Display display) {
        super("Select URL", List.IMPLICIT);
        this.display= display;
        this.parent=parent;
        backCommand= new Command("BACK",Command.BACK, 1);
        this.addCommand(backCommand);
        this.setCommandListener(this);
        fillList();
    }
    
    
    private UrlForm urlForm;
    public void commandAction(Command cmd, Displayable target) {
        if (cmd == backCommand) {
           parent.goBack();   
            
        }
        else if ( cmd == List.SELECT_COMMAND) {
        String url = this.getString(this.getSelectedIndex());
        if (urlForm==null) urlForm= new UrlForm(parent,display);
        urlForm.setUrl(url);
        display.setCurrent(urlForm);
        }
        
    }
    
    
    private void fillList() {
       if (History.getHistory().getSize()<1) {
           setTitle("No History Available");
           return;
       }
        Enumeration e = History.getHistory().elements();
        while (e.hasMoreElements()) 
            this.append((String) e.nextElement(), null);
    }
    public void goBack() {
        display.setCurrent(this);
    }
    
}
