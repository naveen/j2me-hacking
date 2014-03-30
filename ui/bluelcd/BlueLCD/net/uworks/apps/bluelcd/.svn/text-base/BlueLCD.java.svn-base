package net.uworks.apps.bluelcd;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * <p>Title: BlueLCD</p>
 *
 * <p>Description: A LCD simulator for mobile phones</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: uWorks</p>
 *
 * @author Josep del Rio
 * @version 1.0
 */
public class BlueLCD extends MIDlet {
    static BlueLCD instance;
    LCDScreen displayable;

    public BlueLCD() {
        instance = this;

        String firstCharTable = getAppProperty("Chars-1");
        if (firstCharTable != null) {
            ConfigValues.currentCharTable = firstCharTable.substring(0, firstCharTable.indexOf(","));
        }
        ConfigValues.readConfiguration();

        // Create displayable
        displayable = new LCDScreen();
    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(displayable);

        if (ConfigValues.USE_NOKIA_EXT && ConfigValues.useBacklight) {
            com.nokia.mid.ui.DeviceControl.setLights(0,100);
        }
    }

    public void pauseApp() {
        if (ConfigValues.USE_NOKIA_EXT) {
            com.nokia.mid.ui.DeviceControl.setLights(0,0);
        }
    }

    public void destroyApp(boolean unconditional) {
            // Turn off lights...
            if (ConfigValues.USE_NOKIA_EXT) {
                com.nokia.mid.ui.DeviceControl.setLights(0,0);
            }

            // Stop thread
            displayable.stopCommServer();
    }

    public static void quitApp() {
        instance.destroyApp(true);
        instance.notifyDestroyed();
        instance = null;
    }

}
