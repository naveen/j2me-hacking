
import javax.microedition.midlet.*;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.DateField;
import java.util.*;

//Jsr179
import com.sirf.microedition.location.*;
//jsr179++
import com.sirf.microedition.location.extensions.*;
import com.sirf.microedition.util.LoggerForm;

/**
 * SiRFstudioDemo
 * 
 * This application demonstrates SiRFstudio JSR179 compliance and additional functionalities provided by it.
 * The application connects to SiRF GPS receiver over bluetooth.
 * 
 * Application Usage :-
 * 
 *  1) Use "Config" command for the configuration of following parameters :
 *      a) AGPS server IP and port 
 *      b) GPS receiver, which is a BT  device. Use the "Search for bluetooth devices" 
 *         command for starting a search for the BT device
 *      c)Approximate latitude and longitude values.
 * 
 * 2) After setting required configuration use one of the following commands 
 *   a) "Start Location Demo" - It uses JSR179 APIs to get asynchronous position.
 *   b) "Start NMEA Demo" - It uses additional API provided by SiRFstudio to display
 *      NMEA mesgs 
 *   c) Display log entries.
 * 
 * Copyright(C) SiRF Technologies, Inc.
 * 
 * 
 */

public class SiRFstudioDemo extends MIDlet implements CommandListener,LocationListener, NMEALocationListener{
   
    // Constant indicating current demo
    private static final int TYPE_LOCATION_DEMO = 0;

    // Constant indicating current demo
    private static final int TYPE_NMEA_DEMO = 2;

    /** Commands used in the UI elements */
    private Command cmdExit = new Command("Exit", Command.EXIT, 1);

    private Command cmdStartLocDemo = new Command("Display Location ", Command.SCREEN, 1);

    private Command cmdStartNMEADemo = new Command("Display NMEA ", Command.SCREEN, 1);

    private Command cmdConfig = new Command("Provider Configuration", Command.SCREEN, 1);

    private Command cmdDebugLog = new Command("Display Logs", Command.SCREEN, 1);

    private Command cmdCancel = new Command("Cancel", Command.CANCEL, 1);

    private Command cmdStop = new Command("Stop", Command.STOP, 1);

    private Command cmdBack = new Command("Back", Command.BACK, 1);

    private Display display;

    /** main screen of the app */
    private TextBox mainScreen;

    /**
     * A TextBox showing the positioning results (for both NMEA and Location demos)
     */
    private TextBox resultsTextBox;

    /** reference to Location Provider instance */
    private LocationProvider lp = null;

    /** Criteria passed to Location Provider */
    private Criteria cr;

    /** Total number of Locations received */
    private int locationCount;

    /** Active Demo */
    private int activeUseCase = 0;

    Displayable currentDisplayable = null;

	/** reference to GPSConfigurator instance */
	private GPSConfigurator configurator;

	/** reference to Thread instance*/
	
	Thread demoRunnerThread;

    /* Default Constructor initializes the form and starts the main thread. */
    public SiRFstudioDemo() {

        display = Display.getDisplay(this);

        mainScreen = new TextBox("SiRFstudioDemo", "Welcome to SiRFStudio demo!\n", 200, TextField.ANY | TextField.UNEDITABLE);
        mainScreen.addCommand(cmdConfig);
        mainScreen.addCommand(cmdStartLocDemo);
        mainScreen.addCommand(cmdStartNMEADemo);
        mainScreen.addCommand(cmdDebugLog);
        mainScreen.addCommand(cmdExit);
        mainScreen.setCommandListener(this);

        resultsTextBox = new TextBox("Location Info", "Waiting For GPS Fix.", 300, TextField.ANY | TextField.UNEDITABLE);
		resultsTextBox.addCommand(cmdStop);
        resultsTextBox.addCommand(cmdDebugLog);
        resultsTextBox.setCommandListener(this);
    }

    public void startApp() {

        // Creating Criteria
        cr = new Criteria();
        cr.setHorizontalAccuracy(Integer.MAX_VALUE);
        cr.setVerticalAccuracy(Integer.MAX_VALUE);
        cr.setPreferredResponseTime(90000);
		/*Setting CommandListener on LoggForm */
        LoggerForm.getInstance().setExternalCommandListener(this);
        display.setCurrent(mainScreen);
    }

    public void pauseApp() {}

    public void destroyApp(boolean unconditional) {}

    /**
     * Commandaction is called whenever user select a command in UI elements
     */
    public void commandAction(Command c, Displayable d) {
        if (d == mainScreen)
		{
            if (c == cmdExit) {
                // clean up code for the demo
                if (lp != null) {
                    // Reseting Location Provider
                    lp.reset();
                    lp = null;
                }
                notifyDestroyed();
            } else if (c == cmdConfig) {
				configurator = new GPSConfigurator(display);
				configurator.setExternalCommandListener(this);
				display.setCurrent(configurator);
            } else if (c == cmdStartLocDemo) {
                display.setCurrent(resultsTextBox);
                resultsTextBox.setTitle("Location Info...");
                resultsTextBox.setString("Location Demo Session.");
              	startSession(TYPE_LOCATION_DEMO);
            } else if (c == cmdStartNMEADemo) {
                display.setCurrent(resultsTextBox);
                resultsTextBox.setString("NMEA Demo Session.");
                resultsTextBox.setTitle("NMEA Info...");
              	startSession(TYPE_NMEA_DEMO);
            } else if (c == cmdDebugLog) {
                currentDisplayable = display.getCurrent();
                display.setCurrent(LoggerForm.getInstance());
            }
        } else if (d == LoggerForm.getInstance())
		{
            display.setCurrent(currentDisplayable);
        } else if (d == resultsTextBox)
		{
            if (c == cmdStop)
			{
                if(activeUseCase == TYPE_LOCATION_DEMO)
				{
					if (lp != null)
					lp.setLocationListener(null, -1, -1, -1);
		        }else
					NMEALocationProvider.removeNMEAListener(this);
      			System.gc();
                display.setCurrent(mainScreen);
            } else if (c == cmdBack)
			{
                display.setCurrent(mainScreen);
                
            } else if (c == cmdDebugLog)
			{
                currentDisplayable = display.getCurrent();
                display.setCurrent(LoggerForm.getInstance());
            }
		}
		else if (d == configurator)
		{
			display.setCurrent(mainScreen);
		}

    }

    /**
     * Start the session in a new Thread.
     * 
     * @param type The type of demo (TYPE_NMEA_DEMO or TYPE_LOCATION_DEMO) that is started.
     *  Essentially this selects what listener is set in (either NMEAlistener or LocationListener )
     */
    private void startSession(final int type) {
        final LocationListener listener = this;
        final NMEALocationListener nmeaListener = this;

        demoRunnerThread = new Thread() {

            public void run() {
                try {
					if (type == TYPE_NMEA_DEMO) {
                        activeUseCase = TYPE_NMEA_DEMO;
                        NMEALocationDemo(nmeaListener);
					}
					else if (type == TYPE_LOCATION_DEMO)
					{
						activeUseCase = TYPE_LOCATION_DEMO;
						startLocationDemo(listener);
					}

                } catch (Exception e) {
                    display.setCurrent(new Alert("", "Start Session failed,Please retry", null, AlertType.ERROR));
                    lp = null;
                    activeUseCase = 0;
                }
            }
        };
        demoRunnerThread.start();
    }

    void startLocationDemo(LocationListener listener) {

        try {
            if (lp == null)
                lp = LocationProvider.getInstance(cr);

            if (lp != null)
                lp.setLocationListener(listener, 2, 2, 2);
        } catch (Exception e) {
            display.setCurrent(new Alert("", "Failed to instantiate the LocationProvider object", null, AlertType.ERROR));
        }

        resultsTextBox.setString("");
    }

    void NMEALocationDemo(NMEALocationListener nmeaListener) {
        try {
            NMEALocationProvider.addNMEAListener(nmeaListener);
            resultsTextBox.setString("Waiting For NMEA String.");
        } catch (Exception e) {
            display.setCurrent(new Alert("", "Failed to add NMEALocationListener object", null, AlertType.ERROR));
        }

    }

    /**
     * Implements NMEAListener interface method.
     * 
     */
    public void updateNMEA(String str) {
        if (str != null)
            resultsTextBox.setString("NMEA String: \n\r " + str);
    }

    /**
     * Implements LocationListener interface method.
     */
    public void locationUpdated(LocationProvider provider, Location location) {
        try {
            display.setCurrent(resultsTextBox);
            if (location == null || !location.isValid())
                resultsTextBox.setString("Trying to get fix from the receiver....!");
            else {
                DateField today = new DateField("", DateField.DATE_TIME);
                Date time = new Date(location.getTimestamp());
                today.setDate(time);
                displayLocation(location);
                display.setCurrent(resultsTextBox);
            }
        } catch (Exception e) {
            resultsTextBox.setString("POSITION RETURNED EXCEPTION: " + e.toString());
        }
    }

	void displayLocation(Location location)
	{
		DateField today = new DateField("", DateField.DATE_TIME);
		Date time = new Date(location.getTimestamp());
		today.setDate(time);

		resultsTextBox.setString("GPS Location: " + ++locationCount + "\r\n\nLatitude     " + location.getQualifiedCoordinates().getLatitude()
				+ "\nLongitude    " + location.getQualifiedCoordinates().getLongitude()
				+ "\nFix Time      "
				+ today.getDate()//location.getTimestamp()
				+ "\nAccuracy     " + location.getQualifiedCoordinates().getHorizontalAccuracy() + " m\nSpeed        " + (location.getSpeed())
				+ " m/s\nHeading       " + location.getCourse()  + "degrees\nExtra Info(NMEA) "
				+ location.getExtraInfo("application/X-jsr179-location-nmea"));

	}
    /**
     * Implements LocationListener interface method.
     */
    public void providerStateChanged(LocationProvider provider, int newState) {

        String StateStr;
        if (newState == LocationProvider.AVAILABLE)
            StateStr = "AVAILABLE";
        else if (newState == LocationProvider.TEMPORARILY_UNAVAILABLE)
            StateStr = "TEMPORARILY_UNAVAILABLE";
        else
            StateStr = "OUT_OF_SERVICE";

        (display.getCurrent()).setTitle("Provider State : " + StateStr);
    }

    
}