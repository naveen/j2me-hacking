/*
 * Main.java
 *
 * Created on 10. èerven 2004, 15:11
 */

package mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author  iz148508
 * @version
 */
public class Request extends Form implements CommandListener, Runnable{
    private static final int MAX_LENGTH= 256;
    private ReturnableScreen parent;
    private Command backCommand;
    private Command againCommand;
    private Display display;
    private String url;
    private StringItem result;
    public Request(String url, ReturnableScreen parent, Display display) {
        
        super("HTTP Request");
        this.parent=parent;
        this.display=display;
        this.url=url;
        result=new StringItem(null,null);
        backCommand=new Command("BACK",Command.BACK,1);
        againCommand=new Command("RELOAD",Command.OK,1);
        this.addCommand(backCommand);
        this.addCommand(againCommand);
        this.setCommandListener(this);
        this.append(result);
        
    }
    
    public void commandAction(Command c, Displayable d)  {
        if (c == backCommand) {
            parent.goBack();
        } else if (c ==againCommand) {
           Request.connect(url, parent, display);
        }
    }
    
    public static void connect(String url, ReturnableScreen parent, Display display) {
        Request m= new Request(url, parent, display);
        display.setCurrent(m);
        Thread t = new Thread(m);
        t.start();
    }
    
    private void setStatus(String s) {
        this.setTitle(s);
    }
    
    
    public synchronized void run() {
        HttpConnection cn = null;
        InputStream str=null;
        StringBuffer sb=null;
        try {
            setStatus("Connecting ...");
            result.setText("Url: "+url);
            cn=(HttpConnection)Connector.open(url);
            str= cn.openInputStream();
            sb = new StringBuffer();
            InputStreamReader r= new InputStreamReader(str);
            int total=0;
            int read=0;
            setStatus("Receiving...");
            
            while ((read=r.read())>=0 && total<= MAX_LENGTH) {
                total++;
                result.setText("Recieved "+ total + " bytes");
                sb.append( (char) read);
            }
            
            setStatus("Done!");
            displayResult(sb.toString(), cn);
            
        }
        
        catch (Exception e) {
            setStatus("Error!");
            result.setText(e.toString());
            return;
            
        }
        finally {
            try {
                if (str!=null) str.close();
                if (cn!= null) cn.close();
            }
            catch(Exception e) {
            }
            
        }
        
        
    }
    protected void displayResult(String content, HttpConnection cn) throws IOException {
        StringBuffer b= new StringBuffer();
        b.append("Status:"); b.append(cn.getResponseCode());
        b.append("\n");
        int i=0;
        String header;
        
        while((header=cn.getHeaderField(i))!=null) {
            
            b.append(cn.getHeaderFieldKey(i));
            b.append(": ");
            b.append(header);
            b.append("\n");
            i++;
        }
        if (content!=null) {
            b.append("Content:\n");
            b.append(content);
        }
        result.setText(b.toString());
        
    }
}
