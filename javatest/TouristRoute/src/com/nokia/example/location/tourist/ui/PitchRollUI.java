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
import javax.microedition.lcdui.Graphics;
import javax.microedition.location.Orientation;

import com.nokia.example.location.tourist.TouristMIDlet;
import com.nokia.example.location.tourist.model.ConfigurationProvider;

/**
 * Viewer class that renders orientations pitch and roll values to meters.
 */
public class PitchRollUI extends Canvas implements CommandListener, Runnable
{
    private CompassUI compass;

    private float pitch = Float.NaN;

    private float roll = Float.NaN;

    /** Flag telling is the compass update thread active */
    private boolean threadActive = false;

    /** Sleep 1000ms during each compass update */
    private final long SLEEPTIME = 1000;

    private Command compassCmd = new Command("Compass", Command.BACK, 1);

    public PitchRollUI(CompassUI compass)
    {
        this.compass = compass;

        addCommand(compassCmd);
        setCommandListener(this);
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
     * run method from Runnable interface. Updates azimuth, pitch and roll
     * values and repaints the canvas.
     * 
     * If Orientation is supported on the terminal, support level may be either
     * 2D or 3D depending on the compass sensor. If the sensor providers only
     * compass azimuth, pitch and roll values are Float.NaN.
     */
    public void run()
    {
        // Run this thread until this displayable is not visiable.
        // See also hideNotify() method.
        while (threadActive)
        {
            Orientation orientation = ConfigurationProvider.getInstance()
                    .getOrientation();

            if (orientation != null)
            {
                pitch = orientation.getPitch();
                roll = orientation.getRoll();
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

        paintPitch(g);
        paintRoll(g);
    }

    /**
     * Draws a customized meter with a scale.
     */
    protected void drawMeter(Graphics g, int min, int max, int smallStep,
            int largeStep, int baseline, int margin)
    {
        double position;
        double scale = 100.0; // scale value to minimize rounding error
        int x;

        g.setColor(0x0000ff);
        g.drawLine(margin, baseline, getWidth(), baseline);

        for (int i = min; i <= max; i += smallStep)
        {
            position = (((i + max) * scale * (getWidth() - margin)) / (2 * max))
                    / scale;
            x = margin + (int) position;
            g.drawLine(x, baseline - 3, x, baseline + 3);
        }

        for (int i = min; i <= max; i += largeStep)
        {
            position = (((i + max) * scale * (getWidth() - margin)) / (2 * max))
                    / scale;
            x = margin + (int) position;
            g.drawLine(x, baseline - 5, x, baseline + 5);
        }
    }

    /**
     * Paint pitch meter and place the current pitch value to the meter.
     * 
     * @param g -
     *            the Graphics object to be used for rendering the Canvas
     */
    protected void paintPitch(Graphics g)
    {
        int baseline = 10;
        int textAreaWidth = 50;
        double position;

        g.setColor(0x000000);
        g.drawString("Pitch", 0, baseline - 5, Graphics.TOP | Graphics.LEFT);
        g.drawString("-90", textAreaWidth + 1, baseline, Graphics.TOP
                | Graphics.LEFT);
        g.drawString("0", textAreaWidth + (getWidth() - textAreaWidth) / 2,
                baseline, Graphics.TOP | Graphics.HCENTER);
        g.drawString("+90", getWidth(), baseline, Graphics.TOP
                | Graphics.RIGHT);

        drawMeter(g, -90, 90, 10, 30, baseline, textAreaWidth);

        // Draw the marker only if terminals pitch is available.
        if (pitch != Float.NaN)
        {
            position = (((pitch + 90.0) * 100 * (getWidth() - textAreaWidth)) / (2 * 90)) / 100;
            g.setColor(0x000000);
            g.fillRect(textAreaWidth + (int) position - 3, baseline - 2, 5, 5);
        }
    }

    /**
     * Paint roll meter and place the current pitch value to the meter.
     * 
     * @param g -
     *            the Graphics object to be used for rendering the Canvas
     */
    protected void paintRoll(Graphics g)
    {
        int baseline = 40;
        int textAreaWidth = 50;
        double position;

        g.setColor(0x000000);
        g.drawString("Roll", 0, baseline - 5, Graphics.TOP | Graphics.LEFT);
        g.drawString("-180", textAreaWidth + 1, baseline, Graphics.TOP
                | Graphics.LEFT);
        g.drawString("0", textAreaWidth + (getWidth() - textAreaWidth) / 2,
                baseline, Graphics.TOP | Graphics.HCENTER);
        g.drawString("+180", getWidth(), baseline, Graphics.TOP
                | Graphics.RIGHT);

        drawMeter(g, -180, 180, 15, 60, baseline, textAreaWidth);

        // Draw the marker only if terminals roll is available.
        if (roll != Float.NaN)
        {
            position = (((roll + 180.0) * 100 * (getWidth() - textAreaWidth)) / (2 * 180)) / 100;
            g.setColor(0x000000);
            g.fillRect(textAreaWidth + (int) position - 3, baseline - 2, 5, 5);
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
        if (command == compassCmd)
        {
            TouristMIDlet.getDisplay().setCurrent(compass);
        }
    }

}
