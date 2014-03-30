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

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.location.Orientation;

import com.nokia.example.location.tourist.TouristMIDlet;
import com.nokia.example.location.tourist.model.ConfigurationProvider;

/**
 * Viewer class representing a compass.
 */
public class CompassUI extends Canvas implements CommandListener, Runnable
{
    /** Constant value for X coordinate. Used in arrays. */
    private final int X = 0;

    /** Constant value for Y coordinate. Used in arrays. */
    private final int Y = 1;

    /** Compass center X point */
    private int centerX;

    /** Compass center Y point */
    private int centerY;

    /** Constant value representing one degree. */
    private final float degree = (float) (2 * Math.PI / 360.0);

    /** Current compass azimuth. */
    private float azimuth = 0.0f;

    /**
     * Is orientation relative to the magnetic field of the Earth or true north
     * and gravity.
     */
    private boolean isMagnetic;

    /** Flag telling is the compass update thread active */
    private boolean threadActive = false;

    /** Sleep 1000ms during each compass update */
    private final long SLEEPTIME = 1000;

    /** A reference to Route UI */
    private Displayable route = null;

    /** A reference to Pitch and Roll UI */
    private Displayable pitchrollUI = null;

    /** Command that swithes current displayable to Route UI */
    private Command routeCmd = new Command("Route", Command.BACK, 1);

    /** Command that swithes current displayable to Pitch and Roll UI */
    private Command prCmd = new Command("Pitch and Roll", Command.OK, 1);

    /** Compss background image. */
    private Image compassImage = null;

    /**
     * Construct instance of this displayable.
     * 
     * @param route
     *            is a reference to Route UI.
     */
    public CompassUI(Displayable route)
    {
        this.route = route;
        pitchrollUI = new PitchRollUI(this);

        loadCompassImage();

        centerX = getWidth() / 2;
        centerY = getHeight() / 2;

        addCommand(routeCmd);
        addCommand(prCmd);

        setCommandListener(this);
    }

    /**
     * Load compass backgound image from JAR file.
     */
    private void loadCompassImage()
    {
        String imageName = "/compass_small.gif";

        if (getWidth() > 160)
        {
            imageName = "/compass_large.gif";
        }

        try
        {
            compassImage = Image.createImage(getClass().getResourceAsStream(
                    imageName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * VM calls this method immediately prior to this Canvas being made visible
     * on the display.
     */
    protected void showNotify()
    {
        // Actives compass update thread.
        threadActive = true;
        new Thread(this).start();
    }

    /**
     * VM calls this method shortly after the Canvas has been removed from the
     * display.
     */
    protected void hideNotify()
    {
        // Stops the thread.
        threadActive = false;
    }

    /**
     * Renders the canvas.
     * 
     * @param g -
     *            the Graphics object to be used for rendering the Canvas
     */
    protected void paint(Graphics g)
    {
        // clean up canvas
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        int spikeLen = 5;
        int len = (compassImage.getWidth() / 2) - spikeLen;

        // draw compass background
        g.drawImage(compassImage, (getWidth() - compassImage.getWidth()) / 2,
                centerY, Graphics.LEFT | Graphics.VCENTER);

        // draw compass arrow
        g.setColor(0, 0, 255);
        drawArrow(g, degree * azimuth, len, spikeLen);

        // draw orientation type
        g.setColor(0, 0, 255);
        String otext = "True North";
        if (!isMagnetic)
        {
            otext = "Magnetic field";
        }
        g.drawString("Orientation: " + otext, 0, getHeight(), Graphics.BOTTOM
                | Graphics.LEFT);
    }

    /**
     * Draw a compass arrow rotated to a certain angle.
     * 
     * @param g
     *            is a reference to Graphics object.
     * @param angle
     *            in degrees [0.0,360.0)
     * @param len
     *            is arrows length.
     * @param spikeLen
     *            is length of arrows spike.
     */
    private void drawArrow(Graphics g, float angle, int len, int spikeLen)
    {
        int a[] = rotate(angle, 0, -(len - spikeLen));
        int b[] = rotate(angle, -spikeLen, -(len - spikeLen));
        int c[] = rotate(angle, 0, -len);
        int d[] = rotate(angle, spikeLen, -(len - spikeLen));
        int e[] = rotate(angle + (degree * 180.0), 0, -len);

        // use red foreground color
        g.setColor(255, 0, 0);
        g.drawLine(centerX, centerY, centerX + a[X], centerY + a[Y]);
        g.drawLine(centerX + b[X], centerY + b[Y], centerX + d[X], centerY
                + d[Y]);
        g.drawLine(centerX + b[X], centerY + b[Y], centerX + c[X], centerY
                + c[Y]);
        g.drawLine(centerX + c[X], centerY + c[Y], centerX + d[X], centerY
                + d[Y]);

        // use black foreground color
        g.setColor(0, 0, 0);
        g.drawLine(centerX, centerY, centerX + e[X], centerY + e[Y]);
    }

    /**
     * Rotate point (x,y) by degrees that angle parameter defines. The new
     * coordinate calculation is performed with a 2x2 rotate matrix.
     * 
     * @param angle
     *            to be rotated
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     * @return new coordinate pair in int array format [x,y]
     */
    private int[] rotate(double angle, int x, int y)
    {
        int rotated[] = new int[2];
        rotated[X] = (int) (Math.cos(angle) * x + Math.sin(angle) * y);
        rotated[Y] = (int) (-Math.sin(angle) * x + Math.cos(angle) * y);
        return rotated;
    }

    /**
     * run method from Runnable interface. Updates azimuth, pitch and roll
     * values and repaints the canvas.
     * 
     * If Orientation is supported on the terminal, compass sensor is either 2D
     * or 3D. If the terminals compass sensor providers only compass azimuth,
     * pitch and roll values are Float.NaN.
     * 
     * @see HideNotify() method.
     */
    public void run()
    {
        // Keep the thread running until another displayable is set visible.
        // See also hideNotify() method.
        while (threadActive)
        {
            Orientation orientation = ConfigurationProvider.getInstance()
                    .getOrientation();

            if (orientation != null)
            {
                isMagnetic = orientation.isOrientationMagnetic();
                azimuth = orientation.getCompassAzimuth();
            }

            repaint();

            try
            {
                // Pause this thread for a secord before next update.
                Thread.sleep(SLEEPTIME);
            }
            catch (InterruptedException e)
            {
            }
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
        if (command == routeCmd)
        {
            TouristMIDlet.getDisplay().setCurrent(route);
        }
        else if (command == prCmd)
        {
            TouristMIDlet.getDisplay().setCurrent(pitchrollUI);
        }
    }

}
