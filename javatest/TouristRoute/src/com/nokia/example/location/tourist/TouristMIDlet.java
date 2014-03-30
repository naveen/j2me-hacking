//
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER,
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED
// BY THIRD PARTIES
//
// Furthermore, information provided in this source code is preliminary,
// and may be changed substantially prior to final release. Nokia Corporation
// retains the right to make changes to this source code at
// any time, without notice. This source code is provided for informational
// purposes only.
//
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
//
// A non-exclusive, non-transferable, worldwide, limited license is hereby
// granted to the Licensee to download, print, reproduce and modify the
// source code. The licensee has the right to market, sell, distribute and
// make available the source code in original or modified form only when
// incorporated into the programs developed by the Licensee. No other
// license, express or implied, by estoppel or otherwise, to any other
// intellectual property rights is granted herein.
package com.nokia.example.location.tourist;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.nokia.example.location.tourist.model.ConfigurationProvider;
import com.nokia.example.location.tourist.model.ProviderStatusListener;
import com.nokia.example.location.tourist.model.TouristData;
import com.nokia.example.location.tourist.ui.MessageUI;
import com.nokia.example.location.tourist.ui.TouristUI;

/**
 * Tourist Route MIDlet class.
 */
public class TouristMIDlet extends MIDlet implements ProviderStatusListener
{
    /** A static reference to Display object. */
    private static Display display = null;

    /** A Reference to TouristData. */
    private TouristData data = null;

    /** Lock object */
    private Object mutex = new Object();

    public TouristMIDlet()
    {
        super();
    }

    protected void startApp() throws MIDletStateChangeException
    {
        display = Display.getDisplay(this);

        if (ConfigurationProvider.isLocationApiSupported())
        {
            ConfigurationProvider.getInstance().autoSearch(this);
        }
        else
        {
            MessageUI.showApiNotSupported();
        }
    }

    protected void pauseApp()
    {
    }

    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException
    {
    }

    /**
     * Getter method for Display reference.
     * 
     * @return reference to Display object.
     */
    public static Display getDisplay()
    {
        return display;
    }

    /**
     * Event indicating location provider is selected. MIDlet use may therefore
     * begin.
     * 
     * @see com.nokia.example.location.tourist.model.ProviderSelectedListener#providerSelectedEvent()
     */
    public void providerSelectedEvent()
    {
        // Attempt to acquire the mutex
        synchronized (mutex)
        {
            // Start scanning location updates. Also set the TouristData
            // reference data.
            MessageUI.showLocationProviderState();

            // Inform the user that MIDlet is looking for location data.
            data = new TouristData((ProviderStatusListener) this);
        }
    }

    /**
     * Event indication about the first location update. This method sets
     * Tourist UI visible. By using mutex object, we ensure TouristaData (data)
     * is created on providerSelectedEvent.
     * 
     * @see com.nokia.example.location.tourist.model.ProviderStatusListener#firstLocationUpdateEvent()
     */
    public void firstLocationUpdateEvent()
    {
        // Attempt to acquire the mutex
        synchronized (mutex)
        {
            TouristUI ui = new TouristUI(data);

            data.setTouristUI(ui);
            display.setCurrent(ui);
        }
    }
}