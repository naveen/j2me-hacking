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
package com.hipoqih.plugin.s60_3rd.gps;

import com.hipoqih.plugin.State;
import javax.microedition.location.*;
import javax.microedition.lcdui.*;

public class ConfigurationProvider 
{
    private static ConfigurationProvider INSTANCE = null;
    private LocationProvider provider = null;

	public static ConfigurationProvider getInstance()
	{
	    if (INSTANCE == null)
	    {
	        // Enable use of this class when Location API is supported.
	        if (isLocationApiSupported())
	        {
	            INSTANCE = new ConfigurationProvider();
	        }
	        else
	        {
	            INSTANCE = null;
	        }
	    }
	
	    return INSTANCE;
	}
	
    public void autoSearch(ProviderStatusListener listener)
    {
    	try
    	{
            Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);

            provider = LocationProvider.getInstance(criteria);
            if (provider != null)
            {
                // Location provider found, send a selection event.
                listener.providerSelectedEvent();
                return;
            }
            else
            {
                Alert alert = new Alert("Error", "GPS unavailable", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                State.display.setCurrent(alert);
            }
        }
        catch (LocationException le)
        {
            Alert alert = new Alert("Error", "Error looking for providers", null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            State.display.setCurrent(alert);
        }
    }

    public LocationProvider getSelectedProvider()
    {
        return provider;
    }

	public static boolean isLocationApiSupported()
    {
        String version = System.getProperty("microedition.location.version");
        return (version != null && !version.equals("")) ? true : false;
    }
}
