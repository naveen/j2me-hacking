/*
 * Created by Javier Cancela
 * Copyright (C) 2007 hipoqih.com, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, If not, see <http://www.gnu.org/licenses/>.*/

package com.hipoqih.plugin.s60_3rd.gps;


import javax.microedition.location.*;

import com.hipoqih.plugin.UI.*;

public class HipoqihData implements LocationListener, ProximityListener 
{
    private ProviderStatusListener statusListener = null;
    private boolean firstLocationUpdate = false;
    private MainFormUI mainForm = null;

    public HipoqihData(ProviderStatusListener listener)
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
    }
    
    public void setMainForm(MainFormUI mf)
    {
        mainForm = mf;
    }

    public void locationUpdated(LocationProvider provider,
            final Location location)
    {
        // First location update arrived, so we may show the UI (TouristUI)
        if (!firstLocationUpdate)
        {
            firstLocationUpdate = true;
            statusListener.firstLocationUpdateEvent();
        }

        if (mainForm != null)
        {
            new Thread()
            {
                public void run()
                {
                    if (location != null && location.isValid())
                    {
                        QualifiedCoordinates coord = location.getQualifiedCoordinates();
                        mainForm.setLocation(coord.getLatitude(), coord.getLongitude(), true);
                    }
                    else
                    {
                        mainForm.setLocation(0, 0, false);
                    }
                }
            }.start();
        }
    }

    public void providerStateChanged(LocationProvider provider,
            final int newState)
    {
        if (mainForm != null)
        {
            new Thread()
            {
                public void run()
                {
                    switch (newState) {
                        case LocationProvider.AVAILABLE:
                        	mainForm.setLocation(0, 0, false);
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                        	mainForm.setLocation(0, 0, false);
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        	mainForm.setLocation(0, 0, false);
                            break;
                        default:
                        	mainForm.setLocation(0, 0, false);
                            break;
                    }
                }
            }.start();
        }
    }
    
    public void proximityEvent(Coordinates coordinates, Location location)
    {
        /*if (touristUI != null)
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
        }*/
    }
    
    public void monitoringStateChanged(boolean isMonitoringActive)
    {
/*       if (touristUI != null)
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
        }*/
    }
}
