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
package com.nokia.example.location.tourist.model;

import java.util.Enumeration;

import javax.microedition.location.AddressInfo;
import javax.microedition.location.Coordinates;
import javax.microedition.location.Landmark;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.ProximityListener;
import javax.microedition.location.QualifiedCoordinates;

import com.nokia.example.location.tourist.ui.TouristUI;

/**
 * Model class that handles LocationListeners and ProximityListeners events.
 */
public class TouristData implements LocationListener, ProximityListener
{
    /** A Reference to Tourist UI Canvas */
    private TouristUI touristUI = null;

    /** Coordinate detection threshold radius in meters */
    public static final float PROXIMITY_RADIUS = 100.0f;

    /** Previous coordinates outside the distance threshold area (20m) */
    private Coordinates prevCoordinates = null;

    /** The first location update done. */
    private boolean firstLocationUpdate = false;

    private ProviderStatusListener statusListener = null;

    /**
     * Construct instance of this model class.
     */
    public TouristData(ProviderStatusListener listener)
    {
        statusListener = listener;

        ConfigurationProvider config = ConfigurationProvider.getInstance();

        // 1. Register LocationListener
        LocationProvider provider = config.getSelectedProvider();
        if (provider != null)
        {
            int interval = -1; // default interval of this provider
            int timeout = 0; // parameter has no effect.
            int maxage = 0; // parameter has no effect.

            provider.setLocationListener(this, interval, timeout, maxage);
        }

        // 2. Register ProxymityListener to each Landmark found from the
        // Landmark store.
        ControlPoints cp = ControlPoints.getInstance();

        Enumeration enumeration = cp.getLandMarks();
        if (enumeration != null)
        {
            while (enumeration.hasMoreElements())
            {
                Landmark lm = (Landmark) enumeration.nextElement();
                createProximityListener(lm.getQualifiedCoordinates());
            }
        }
    }

    /**
     * Setter method to TouristUI reference.
     * 
     * @param ui -
     *            Reference to TouristUI.
     */
    public void setTouristUI(TouristUI ui)
    {
        touristUI = ui;
    }

    /**
     * Adds new ProximityListener to the location provider. This method is
     * called when constructing instance of this calss and when a new landmark
     * is saved by using LandmarkEditorUI.
     * 
     * @param coordinates
     */
    public void createProximityListener(Coordinates coordinates)
    {
        try
        {
            LocationProvider.addProximityListener(this, coordinates,
                    PROXIMITY_RADIUS);
        }
        catch (LocationException e)
        {
            // Platform does not have resources to add a new listener
            // and coordinates to be monitored or does not support
            // proximity monitoring at all
        }
    }

    /**
     * locationUpdated event from LocationListener interface. This method starts
     * a new thread to prevent blocking, because listener method MUST return
     * quickly and should not perform any extensive processing.
     * 
     * Location parameter is set final, so that the anonymous Thread class can
     * access the value.
     */
    public void locationUpdated(LocationProvider provider,
            final Location location)
    {
        // First location update arrived, so we may show the UI (TouristUI)
        if (!firstLocationUpdate)
        {
            firstLocationUpdate = true;
            statusListener.firstLocationUpdateEvent();
        }

        if (touristUI != null)
        {
            new Thread()
            {
                public void run()
                {
                    if (location != null && location.isValid())
                    {
                        AddressInfo address = location.getAddressInfo();
                        QualifiedCoordinates coord = location.getQualifiedCoordinates();
                        float speed = location.getSpeed();
                        
                        touristUI.setInfo(address, coord, speed);
                        touristUI.setProviderState("Available");
                        touristUI.repaint();
                    }
                    else
                    {
                        touristUI.setProviderState("Not valid location data");
                        touristUI.repaint();
                    }
                }
            }.start();
        }
    }

    /**
     * providerStateChanged event from LocationListener interface. This method
     * starts a new thread to prevent blocking, because listener method MUST
     * return quickly and should not perform any extensive processing.
     * 
     * newState parameter is set final, so that the anonymous Thread class can
     * access the value.
     */
    public void providerStateChanged(LocationProvider provider,
            final int newState)
    {
        if (touristUI != null)
        {
            new Thread()
            {
                public void run()
                {
                    switch (newState) {
                        case LocationProvider.AVAILABLE:
                            touristUI.setProviderState("Available");
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            touristUI.setProviderState("Out of service");
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            touristUI
                                    .setProviderState("Temporarily unavailable");
                            break;
                        default:
                            touristUI.setProviderState("Unknown");
                            break;
                    }

                    touristUI.repaint();
                }
            }.start();
        }
    }

    /**
     * proximity event from ProximityListener interface. The listener is called
     * only once when the terminal enters the proximity of the registered
     * coordinates. That's why no this method should not need to start a new
     * thread.
     */
    public void proximityEvent(Coordinates coordinates, Location location)
    {
        if (touristUI != null)
        {
            touristUI.setProviderState("Control point found!");

            Landmark lm = ControlPoints.getInstance().findNearestLandMark(
                    coordinates);

            // landmark found from landmark store
            if (lm != null)
            {
                touristUI.setInfo(lm.getAddressInfo(), lm
                        .getQualifiedCoordinates(), location.getSpeed());
            }
            // landmark not found from the landmark store, this should not never
            // happen!
            else
            {
                touristUI.setInfo(location.getAddressInfo(), location
                        .getQualifiedCoordinates(), location.getSpeed());
            }

            touristUI.repaint();
        }
    }

    /**
     * monitoringStateChanged event from ProximityListener interface. Notifies
     * that the state of the proximity monitoring has changed. That's why this
     * method should not need to start a new thread.
     */
    public void monitoringStateChanged(boolean isMonitoringActive)
    {
        if (touristUI != null)
        {
            if (isMonitoringActive)
            {
                // proximity monitoring is active
                touristUI.setProximityState("Active");
            }
            else
            {
                // proximity monitoring can't be done currently.
                touristUI.setProximityState("Off");
            }

            touristUI.repaint();
        }
    }
}
