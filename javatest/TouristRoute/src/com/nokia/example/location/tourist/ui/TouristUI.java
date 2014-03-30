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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import javax.microedition.location.AddressInfo;
import javax.microedition.location.QualifiedCoordinates;

import com.nokia.example.location.tourist.TouristMIDlet;
import com.nokia.example.location.tourist.Utils;
import com.nokia.example.location.tourist.model.ConfigurationProvider;
import com.nokia.example.location.tourist.model.TouristData;

/**
 * Viewer class that renders current location updates.
 */
public class TouristUI extends Canvas implements CommandListener
{
    /** The current state of the location provider as a String */
    private String providerState = "Unknown";

    /** Proximity monitoring state. */
    private String proximityState = "Waiting";

    private AddressInfo info;

    private QualifiedCoordinates coord;

    private float speed;

    /** Command that shows compass canvas */
    private Command compassCmd = new Command("Compass", Command.OK, 1);

    /** Command that shows Landmark editor UI */
    private Command editorCmd = new Command("Editor", Command.STOP, 1);

    /** Rerefence to the Landmark editor UI */
    private LandmarkEditorUI editorUI = null;

    /** Rerefence to the Compass UI */
    private CompassUI compassUI = null;

    public TouristUI(TouristData data)
    {
        editorUI = new LandmarkEditorUI(this, data);
        compassUI = new CompassUI(this);

        checkSupportedFeatures();

        addCommand(editorCmd);
        setCommandListener(this);
    }

    /**
     * Enable supported Location API features on the UI and disable unsupported
     * features.
     */
    protected void checkSupportedFeatures()
    {
        if (ConfigurationProvider.getInstance().isOrientationSupported())
        {
            addCommand(compassCmd);
        }
        else
        {
            removeCommand(compassCmd);
        }
    }

    public void setProviderState(String state)
    {
        providerState = state;
    }

    public void setProximityState(String state)
    {
        proximityState = state;
    }

    public void setInfo(AddressInfo info, QualifiedCoordinates coord,
            float speed)
    {
        this.info = info;
        this.coord = coord;
        this.speed = speed;
    }

    /**
     * Renders the canvas.
     * 
     * @param g -
     *            the Graphics object to be used for rendering the Canvas
     */
    protected void paint(Graphics g)
    {
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                Font.SIZE_SMALL);
        g.setFont(f);

        // use font height as a line height
        int lineHeight = f.getHeight();
        // current line counter
        int line = 0;

        // clean the backround
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(0x0000ff);
        g.drawString("Provider state: " + providerState, 0, lineHeight
                * (line++), Graphics.LEFT | Graphics.TOP);
        g.drawString("Proximity monitoring: " + proximityState, 0, lineHeight
                * (line++), Graphics.LEFT | Graphics.TOP);

        if (coord != null)
        {
            double lat = coord.getLatitude();
            double lon = coord.getLongitude();

            g.drawString("Lat, Lon (" + Utils.formatDouble(lat, 3) + ", "
                    + Utils.formatDouble(lon, 3) + ")", 0, lineHeight
                    * (line++), Graphics.TOP | Graphics.LEFT);
            g.drawString("Speed: " + Utils.formatDouble(speed, 2) + " m/s", 0,
                    lineHeight * (line++), Graphics.TOP | Graphics.LEFT);
        }

        // Check if AddressInfo is available
        if (info != null)
        {
            String country = info.getField(AddressInfo.COUNTRY);
            String state = info.getField(AddressInfo.STATE);
            String city = info.getField(AddressInfo.CITY);
            String street = info.getField(AddressInfo.STREET);
            String buildingName = info.getField(AddressInfo.BUILDING_NAME);

            g.setColor(0x000000);

            if (country != null)
                g.drawString("Country: " + country, 0, lineHeight * (line++),
                        Graphics.TOP | Graphics.LEFT);
            if (state != null)
                g.drawString("State: " + state, 0, lineHeight * (line++),
                        Graphics.TOP | Graphics.LEFT);
            if (city != null)
                g.drawString("City: " + city, 0, lineHeight * (line++),
                        Graphics.TOP | Graphics.LEFT);
            if (street != null)
                g.drawString("Street: " + street, 0, lineHeight * (line++),
                        Graphics.TOP | Graphics.LEFT);
            if (buildingName != null)
                g.drawString("Building name: " + buildingName, 0, lineHeight
                        * (line++), Graphics.TOP | Graphics.LEFT);
        }
    }

    /**
     * Event indicating when a command button is pressed.
     * 
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     *      javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable d)
    {
        if (command == editorCmd)
        {
            if (coord != null)
            {
                editorUI.showEditor(coord, LandmarkEditorUI.MODE_ADDNEW);
            }
            else
            {
                MessageUI.showLocationDataNotAvailable();
            }
        }
        else if (command == compassCmd)
        {
            TouristMIDlet.getDisplay().setCurrent(compassUI);
        }
    }

}