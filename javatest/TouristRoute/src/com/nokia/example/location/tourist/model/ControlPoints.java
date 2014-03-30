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

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Landmark;
import javax.microedition.location.LandmarkException;
import javax.microedition.location.LandmarkStore;

/**
 * Model class that handles landmark store actions.
 */
public class ControlPoints
{
    private LandmarkStore store = null;

    private static final String STORENAME = "TOURIST_DEMO";

    private static final String DEFAULT_CATEGORY = null;

    private static ControlPoints INSTANCE = null;

    /**
     * Constructs instance of this class. Makes sure that landmark store
     * instance exists.
     */
    private ControlPoints()
    {
        String name = null;
        // Try to find landmark store called "TOURIST_DEMO".
        try
        {
            store = LandmarkStore.getInstance(STORENAME);
        }
        catch (NullPointerException npe)
        {
            // This should never occur.
        }

        // Check whether landmark store exists.
        if (store == null)
        {
            // Landmark store does not exist.

            try
            {
                // Try to create landmark store named "TOURIST_DEMO".
                LandmarkStore.createLandmarkStore(STORENAME);
                name = STORENAME;
            }
            catch (IllegalArgumentException iae)
            {
                // Name is too long or landmark store with the specified
                // name already exists. This Exception should not occur,
                // because we have earlier tryed to created instance of
                // this landmark store.
            }
            catch (IOException e)
            {
                // Landmark store couldn't be created due to an I/O error
            }
            catch (LandmarkException e)
            {
                // Implementation does not support creating new landmark
                // stores.
            }

            store = LandmarkStore.getInstance(name);
        }
    }

    /**
     * Singleton patterns getInstance method. Makes sure that only one instance
     * of this class is alive at once.
     * 
     * @return instance of this class.
     */
    public static ControlPoints getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ControlPoints();
        }

        return INSTANCE;
    }

    /**
     * Find a Landmark from the landmark store using a index.
     * 
     * @param index -
     *            the landmark in the store.
     * @return Landmark from landmark store.
     */
    public Landmark findLandmark(int index)
    {
        Landmark lm = null;

        Enumeration cps = ControlPoints.getInstance().getLandMarks();
        int counter = 0;

        while (cps.hasMoreElements())
        {
            lm = (Landmark) cps.nextElement();

            if (counter == index)
            {
                break;
            }
            counter++;
        }

        return lm;
    }

    /**
     * Find nearest landmark to coordinate parameter from the landmarkstore.
     */
    public Landmark findNearestLandMark(Coordinates coord)
    {
        Landmark landmark = null;

        double latRadius = 0.1;
        double lonRadius = 0.1;

        float dist = Float.MAX_VALUE;

        try
        {
            // Generate enumeration of Landmarks that are close to coord.
            Enumeration enu = store.getLandmarks(null, coord.getLatitude()
                    - latRadius, coord.getLatitude() + latRadius, coord
                    .getLongitude() - lonRadius, coord.getLongitude() 
                    + lonRadius);
            float tmpDist;
            Landmark tmpLandmark = null;

            while (enu.hasMoreElements())
            {
                tmpLandmark = (Landmark) enu.nextElement();
                tmpDist = tmpLandmark.getQualifiedCoordinates().distance(coord);

                if (tmpDist < dist)
                {
                    landmark = tmpLandmark;
                }
            }
        }
        catch (IOException ioe)
        {
            // I/O error happened when accessing the landmark store.
            return null;
        }

        return landmark;
    }

    public Enumeration getLandMarks()
    {
        Enumeration enu = null;

        try
        {
            enu = store.getLandmarks();
        }
        catch (IOException ioe)
        {
            // I/O error happened when accessing the landmark store.
        }

        return enu;
    }

    public void addLandmark(Landmark landmark) throws IOException
    {
        store.addLandmark(landmark, DEFAULT_CATEGORY);
    }

    public void updateLandmark(Landmark landmark) throws IOException,
            LandmarkException
    {
        store.updateLandmark(landmark);
    }

    public void removeLandmark(Landmark landmark) throws IOException,
            LandmarkException
    {
        store.deleteLandmark(landmark);
    }

}