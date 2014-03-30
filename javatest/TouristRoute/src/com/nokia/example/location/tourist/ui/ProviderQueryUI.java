// Copyright 2005 Nokia Corporation.
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
package com.nokia.example.location.tourist.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import com.nokia.example.location.tourist.TouristMIDlet;

/**
 * Viewer class that is responsible for all the UI actions when the application
 * is seaching for the location provider.
 */
public class ProviderQueryUI
{
    /** Status information Form */
    private Form searchForm = new Form("Seaching location provider...");

    /** StringItem showing the current status. */
    private StringItem infoItem = new StringItem("Status:", "");

    /** Provider cost selection command - Yes. */
    private Command yesCmd = new Command("Yes", Command.OK, 1);

    /** Provider cost selection command - No. */
    private Command noCmd = new Command("No", Command.STOP, 1);

    /** A boolean indicating may user allow location provider cost. */
    private boolean result = false;

    private static final String COST_QUERY_MESSAGE = "Cost free location providers can not be found. Do you with continue "
            + "search with providers that cost?";

    private static final String OUT_OF_SERVICE_MESSAGE = "All Location providers are currently out of service. Please unsure "
            + "that location-providing module is properly connected.";

    private static final String SEACHING_FREE_PROVIDERS = "Seaching for free location providers.";

    private static final String SEACHING_COST_PROVIDERS = "Seaching for providers that may cost.";

    private static final String NOT_FOUND_MESSAGE = "Try again after location-providing module is properly connected.";

    private static final String NO_FREE_SERVICE_SERVICE_MESSAGE = "Cost free location providers can not be found. Please ensure "
            + "that location-providing module is properly connected.";

    /**
     * Construct the UI with default values.
     */
    public ProviderQueryUI()
    {
        infoItem.setText(SEACHING_FREE_PROVIDERS);
        searchForm.append(infoItem);
    }

    /**
     * Show out of service error message.
     */
    public void showOutOfService()
    {
        Alert alert = new Alert("Error", OUT_OF_SERVICE_MESSAGE, null,
                AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        TouristMIDlet.getDisplay().setCurrent(alert, searchForm);
        infoItem.setText(NOT_FOUND_MESSAGE);
    }

    /**
     * Show no cost free location provider found error message.
     */
    public void showNoFreeServiceFound()
    {
        Alert alert = new Alert("Error", NO_FREE_SERVICE_SERVICE_MESSAGE, null,
                AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        TouristMIDlet.getDisplay().setCurrent(alert, searchForm);
        infoItem.setText(NOT_FOUND_MESSAGE);
    }

    /**
     * Query the user whether the use of location provider may cost. The use of
     * this method is locked with synchronized keyword, so only one thread can
     * access this method at once.
     * 
     * @return a boolean indicating may user allow location provider cost.
     */
    public synchronized boolean confirmCostProvider()
    {
        Alert alert = new Alert("Confimnation", COST_QUERY_MESSAGE, null,
                AlertType.CONFIRMATION);
        alert.addCommand(yesCmd);
        alert.addCommand(noCmd);
        alert.setTimeout(Alert.FOREVER);

        // Set the monitoring object to be this instance.
        final ProviderQueryUI hinstance = this;

        // Add a CommandLister as anomynous inner class
        alert.setCommandListener(new CommandListener()
        {
            /*
             * Event indicating when a command button is pressed.
             * 
             * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
             *      javax.microedition.lcdui.Displayable)
             */
            public void commandAction(Command command, Displayable d)
            {
                if (command == yesCmd)
                {
                    infoItem.setText(SEACHING_COST_PROVIDERS);
                    result = true;
                    synchronized (hinstance)
                    {
                        // Wake up the monitoring object
                        hinstance.notifyAll();
                    }
                }
                else if (command == noCmd)
                {
                    result = false;
                    infoItem.setText(NOT_FOUND_MESSAGE);
                    synchronized (hinstance)
                    {
                        // Wake up the monitoring object
                        hinstance.notifyAll();
                    }
                }

            }
        });

        TouristMIDlet.getDisplay().setCurrent(alert, searchForm);

        // Wait indefinitely for notification.
        try
        {
            wait();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        TouristMIDlet.getDisplay().setCurrent(searchForm);

        return result;
    }

}
