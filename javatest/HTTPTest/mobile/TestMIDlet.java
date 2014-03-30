/*
 * TestMIDlet.java
 *
 * Created on 9. èerven 2004, 12:35
 */

package mobile;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * An example MIDlet with simple "Hello" text and an Exit command.
 * Refer to the startApp, pauseApp, and destroyApp
 * methods so see how each handles the requested transition.
 *
 * @author  iz148508
 * @version
 */
public class TestMIDlet extends MIDlet implements CommandListener, ReturnableScreen {
    
    private Command exitCommand; // The exit command
   
   
    private Display display;    // The display for this MIDlet
    
    public TestMIDlet() {
        
        
    }
    
   
  
    
    /**
     * Start up the Hello MIDlet by creating the TextBox and associating
     * the exit command and listener.
     */
    public void startApp() {
       if (display == null ) initApp();
       display.setCurrent(l);
    }
    
    List l;
    private void initApp() {
         
         display = Display.getDisplay(this);
        exitCommand = new Command("EXIT", Command.BACK, 1);
       
       
        l = new List("HTTP Test",List.IMPLICIT, new String[] {"New HTTP request", "Url History", "Exit"},null);
        l.addCommand(exitCommand);
        l.setCommandListener(this);
       
        
    }
    
    
    
    /**
     * Pause is a no-op since there are no background activities or
     * record stores that need to be closed.
     */
    public void pauseApp() {
    }
    
    /**
     * Destroy must cleanup everything not handled by the garbage collector.
     * In this case there is nothing to cleanup.
     */
    public void destroyApp(boolean unconditional) {
        History.getHistory().storeToRS();
    }
    
    /*
     * Respond to commands, including exit
     * On the exit command, cleanup and notify that the MIDlet has been destroyed.
     */
    private Form urlForm;
    
    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
        else  if (c == l.SELECT_COMMAND)
            
            if (l.getSelectedIndex()==0) {
            if (urlForm== null) urlForm = new UrlForm(this,display);
            display.setCurrent(urlForm);
            }
            else if (l.getSelectedIndex()==1) {
               HistoryList hl= new HistoryList(this,display);
               display.setCurrent(hl);
            }
            else
            {
            destroyApp(false);
            notifyDestroyed();
            }
            
        }
        
    
    
    public void goBack() {
        display.setCurrent(l);
    }    
   
    
}
