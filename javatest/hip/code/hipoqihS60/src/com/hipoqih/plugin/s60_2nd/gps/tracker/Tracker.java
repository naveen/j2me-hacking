package com.hipoqih.plugin.s60_2nd.gps.tracker;

import com.hipoqih.plugin.s60_2nd.gps.utilities.*;
import com.hipoqih.plugin.s60_2nd.gps.*;
import java.io.IOException;
import java.util.Vector;
/**
 * Save the coordinates into a <code>Vector</code> every t seconds. Can also save the coordinates recorded in the vector in a file.
 * If the gps device is not connected or has no fix, no points are recorded during this time.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */

public class Tracker implements Runnable {

    private int time = 5;
    private int counterSave = 1;
    private Thread t = new Thread(this);
    private GPS gp;
    private boolean mEndnow = false;
    private Vector coordVector = new Vector();
    private Vector coordVector2 = new Vector();
    private boolean pause = false;
    private boolean meters;
    private int coordinatesFormat;
    private FileIOListener listener;


    /**
     *Constructor
     *
     * @param gps               The gps object we use to get the coordinates.
     * @param time              The delay in seconds between the record of two coordinates. Must be between 1 and 1000.
     * @param coordinatesFormat The format in wich the coordinates will be saved. To save in a particular format, use the classe <code>CoordinateFormat</code>.
     *                          Example: to save in Lambert 2, use <code>CoordinatesFormat.LAMBERT2</code>
     * @param meters            If true the altitude is saved in meters, is false the altitude is saved in feet.
     * @param listener          A file listener must be given to know when the file has finished to be saved.
     * @throws NumberFormatException This exception is thrown if the time parameter is not between 1 and 1000, or if the coordinatesFormat is not positive.
     * @see FileIOListener
     */
    public Tracker(GPS gps, int time, int coordinatesFormat, boolean meters, FileIOListener listener) throws NumberFormatException {
        this.meters = meters;
        this.listener = listener;
        if (coordinatesFormat < 0)
            throw new NumberFormatException("The CoordinatesFormat code is not valid");
        else
            this.coordinatesFormat = coordinatesFormat;
        if (time < 1 || time > 1000)
            throw new NumberFormatException("time must be between 1 and 1000");
        else
            this.time = time;
        mEndnow = false;
        gp = gps;
        Debug.setDebug("tracker created", Debug.DETAIL);
    }

    /**
     * Constructor
     *
     * The longitude/latitude will be recorded in decimal degrees
     * and the altitude in meters. The delay between 2 records is 5 seconds.
     *
     * @param gps      The gps object we use to get the coordinates.
     * @param listener A file listener must be given to know when the file has finished to be saved.
     * @see FileIOListener
     */
    public Tracker(GPS gps, FileIOListener listener) {
        this(gps, CoordinatesFormat.DECIMALDEGREES, 5, true, listener);
    }

    /**
     * Constructor
     *
     * The altitude will be saved altitude in meters.
     *
     * @param gps               The gps object we use to get the coordinates.
     * @param time              The delay in seconds between the record of two coordinates. Must be between 1 and 1000.
     * @param coordinatesFormat The format in wich the coordinates will be saved. To save in a particular format, use the classe <code>CoordinateFormat</code>.
     *                          Example: to save in Lambert 2, use <code>CoordinatesFormat.LAMBERT2</code>
     * @param listener          A file listener must be given to know when the file has finished to be saved.
     * @throws NumberFormatException This exception is thrown if the time parameter is not between 1 and 1000, or if the coordinatesFormat is not positive.
     * @see FileIOListener
     */
    public Tracker(GPS gps, int time, int coordinatesFormat, FileIOListener listener) throws NumberFormatException {
        this(gps, time, coordinatesFormat, true, listener);
    }

    /**
     * Resets the record of coordinates, removing all coordinates in the vector.
     */
    public void reset() {
        counterSave = 1;
        coordVector.removeAllElements();
        Debug.setDebug("tracker reseted", Debug.DETAIL);
    }

    /**
     * Changes the delay between two records.
     *
     * @param time The delay in seconds between the record of two coordinates. Must be between 1 and 1000.
     * @throws NumberFormatException This exception is thrown if the time parameter is not between 1 and 1000.
     */
    public void setTime(int time) throws NumberFormatException {
        if (time < 1 || time > 1000)
            throw new NumberFormatException("time must be between 1 and 1000");
        else
            this.time = time;
    }

    /**
     * Gets the delay between two records.
     *
     * @return The delay in seconds.
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the format of coordinates.
     * @param format Use codes in <code>CoordinatesFormat</code>
     * @throws NumberFormatException
     * @see CoordinatesFormat
     */
      public void setCoordinatesFormat(int format) throws NumberFormatException {
        if (time < 0)
            throw new NumberFormatException("Format is not valid");
        else
            coordinatesFormat=format;
    }

    /**
     * Returns the format of coordinate used by the tracker.
     * @return The format. Use codes in <code>CoordinatesFormat</code> to know wich format it is.
     */
          public int getCoordinatesFormat() {
           return coordinatesFormat;
    }

    /**
     * Pauses the recording of coordinates.
     */
    public void pause() {
        pause = true;
        Debug.setDebug("tracker paused", Debug.DETAIL);
    }

    /**
     * Resumes the recording of coordinates.
     */
    public void resume() {
        pause = false;
        Debug.setDebug("tracker Resumed", Debug.DETAIL);
    }

    /**
     * Check if the tracker is paused.
     *
     * @return <code>true</code> if the tracker is paused, and <code>false</code> if not.
     */
    public boolean isPaused() {
        return pause;
    }

     /**
     * Returns a <code>Vector</code> of <code>Coordinates</code> wich contains all the coordinates recorded.
     * @return <code>true</code> if the tracker is paused, and <code>false</code> if not.
     * @see Coordinates
     */
    public Vector getRecords() {
        return coordVector2;
    }

    /**
     * Starts the tracker, reseting all preview records if any.
     */
    public void start() {
        if (t != null) {
            mEndnow = false;
            coordVector.removeAllElements();
            coordVector2.removeAllElements();
            counterSave = 1;
            t = new Thread(this);
            t.start();
            Debug.setDebug("tracker started", Debug.DETAIL);
        }
    }

    /**
     * Stops the tracker.
     */
    public void stop() {
        mEndnow = true;
        Debug.setDebug("tracker stopped", Debug.DETAIL);
    }

    /**
     * Saves the recorded coordinates into a file.
     * The format is NumberOfThePoint, Vertical Coordinate, Horizontal Coordinate, Time, Altitude.
     * A file in this format can be open by others programs such as ArcExplorer to draw the recorded points on a map.
     * The file can be saved even if the <code>tracker</code> is running or stopped.
     *
     * @param filePath The path of the file in wich we want to save the coordinates.
     *                 Ex: c:/other/coord.txt
     * @throws IOException This exception is thrown if a problem has occured during the save.
     */
    public void saveCoordinates(String filePath) throws IOException {
        mEndnow = true;
        Write rw = new Write(filePath, listener);
        rw.save(coordVector);
    }

    public void run() {
        String lastTime = "";
        while (!mEndnow) {
            String timeNow = gp.getTime();
            if (!pause) {
                Coordinates point = gp.getCoordinates();
                //Check if the gps is turned on and has a fix
                if (gp.hasFix() && !(timeNow.equals(lastTime))) {

                    String save = new String();
                    switch (coordinatesFormat) {
                        case CoordinatesFormat.DECIMALDEGREES:
                            {
                                save = counterSave + "," + point.getLongitude() + "," + point.getLatitude() + "," + timeNow + ",";
                                if (meters)
                                    save += point.getAltitude();
                                else
                                    save += Utilities.metersToFeet(point.getAltitude());
                                break;
                            }

                        case CoordinatesFormat.DEGREESMINSEC:
                            {
                                save = counterSave + "," + Utilities.degreesToDegreesMinSec(point.getLongitude(), Utilities.LONGITUDE) + "," + Utilities.degreesToDegreesMinSec(point.getLatitude(), Utilities.LATITUDE) + "," + timeNow + ",";
                                if (meters)
                                    save += point.getAltitude();
                                else
                                    save += Utilities.metersToFeet(point.getAltitude());
                                break;
                            }
                        default:
                            {
                                double[] temp = Utilities.toDatum(point.getLatitude(), point.getLongitude(), coordinatesFormat);
                                save = counterSave + "," + temp[1] + "," + temp[0] + "," + timeNow + ",";
                                if (meters)
                                    save += point.getAltitude();
                                else
                                    save += Utilities.metersToFeet(point.getAltitude());
                            }
                    }
                    Debug.setDebug("tracker recording " + save, Debug.DETAIL);
                    coordVector.addElement(save);
                    coordVector2.addElement(point);
                    counterSave++;
                    lastTime = timeNow;
                }
            }
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
