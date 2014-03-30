import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.location.*;

public class LocationListenerTest extends MIDlet implements CommandListener, LocationListener {
	
  private Display display;	
  private Form mainForm;
  private Alert positionAlert;
  private boolean isTracking;
  
  public LocationListenerTest() {
  	
  	buildMainForm();
  }
  
  private void buildMainForm() {
  	
  	mainForm = new Form("LocationListenerTest");
  	StringItem welcomeMsg = new StringItem("welcome", "Press OK to get current location");
    mainForm.append(welcomeMsg);
    
    mainForm.addCommand(new Command("Exit", Command.EXIT, 0));
    mainForm.addCommand(new Command("OK", Command.OK, 1));
    mainForm.setCommandListener(this);
  }
  
  public void startApp() {
  	
  	display = Display.getDisplay(this);
    display.setCurrent(mainForm);
  }
  
  public void pauseApp() {}
  
  public void destroyApp(boolean unconditional) {}
  
  public void commandAction(Command c, Displayable s) {
  	
  	String label = c.getLabel();
    if (label.equals("Exit")) {
    	
    	notifyDestroyed();
    }
    else if (label.equals("OK")) {
    	
    	if (isTracking == false) {
    		
    		isTracking = true;
    		
			for (int i=0; i< mainForm.size(); i++) {

				mainForm.delete(i);
			}
			mainForm.append("Acquiring info ...");
    		try {
				
				Criteria cr = new Criteria();
				cr.setHorizontalAccuracy(500);
				
				LocationProvider lp = LocationProvider.getInstance(cr);	
				lp.setLocationListener(this, -1, -1, -1);

			}
			catch (Exception e) {
				
				positionAlert = new Alert("Error");
				positionAlert.setString(e.toString());
				positionAlert.setTimeout(Alert.FOREVER);
			}
	    }
    }
  }
  
  public void locationUpdated(LocationProvider provider, Location location){
  	
  	mainForm.append("\nrefreshing location ...");
  	LocationInfo locationInfo = new LocationInfo(location);
	Thread locationInfoThread = new Thread(locationInfo);
	locationInfoThread.start();	
  }
  
  public void providerStateChanged(LocationProvider provider, int newState){}
    
  class LocationInfo implements Runnable {
  	
  	Location location;
  	
  	LocationInfo(Location location) {
  		
  		this.location = location;
  	}
  	
  	public void run() {
  		
  		try {
	
			Coordinates c = location.getQualifiedCoordinates();
			if (c != null) {
		   
				String lat = c.convert(c.getLatitude(),c.DD_MM);
				if (c.getLatitude() > 0) {
				
					lat = "E "+lat;
				}
				else {
				
					lat = "W "+lat;
				}
				
				String lon = c.convert(c.getLongitude(),c.DD_MM);
				if (c.getLatitude() > 0) {
				
					lon = "N "+lon;
				}
				else {
				
					lon = "S "+lon;
				}
				
				for (int i=0; i< mainForm.size(); i++) {
				
					mainForm.delete(i);
				}
				mainForm.append("\n" + lat + "\n" + lon);
			}
			else {
				
				mainForm.append("\nnull coordinate");
				//positionAlert = new Alert("Location Not Found");
				//positionAlert.setString("Null Coordinate Received");
				//positionAlert.setTimeout(Alert.FOREVER);
			}
			
		}
		catch (Exception e) {
			
			mainForm.append("\nexception");
			//positionAlert = new Alert("Error");
			//positionAlert.setString(e.toString());
			//positionAlert.setTimeout(Alert.FOREVER);
		}
  	}
  }
}