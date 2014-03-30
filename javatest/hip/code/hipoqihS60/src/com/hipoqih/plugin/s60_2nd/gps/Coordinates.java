package com.hipoqih.plugin.s60_2nd.gps;
/**
 *  Represent a location. <code>Coordinates</code> is a pair of <code>latitude</code> and <code>longitude</code>, with eventually a <code>name</code>,
 *  the <code>time</code> and an <code>altitude</code>.
 *  Returned by <code>gps</code> when <code>getCoordinates</code> is called
 *  <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @see GPS
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0  <p>Geneva, the 23.03.2006
 */
public class Coordinates {
    private double latitude;
    private double longitude;
    private float altitude;
    private String time;
    private String name;

    /**
     * Constructor.
     *
     * @param name      The name of the point.
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     */
    public Coordinates(String name,double latitude,double longitude) {
        this(name,latitude,longitude,0,"");
    }

     /**
     * Constructor
     * @param name      The name of the point.
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     * @param altitude The altitude in meters.
     */
    public Coordinates(String name,double latitude,double longitude,float altitude) {
        this(name,latitude,longitude,altitude,"");
    }

    /**
     * Constructor
     *
     * @param longitude The longitude in decimal degrees.
     * @param latitude The latitude in decimal degrees.
     * @param altitude The altitude in meters.
     * @param time The timestamp of the coordinate.
     */
    public Coordinates(double longitude,double latitude,float altitude,String time) {
        this("",latitude,longitude,altitude,time);
    }

     /**
     * Constructor
      *
     * @param name      The name of the point.
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     * @param time The timestamp of the coordinate.
     */
    public Coordinates(String name,double latitude,double longitude,String time) {
        this(name,latitude,longitude,0,time);
    }

     /**
     * Constructor
     *
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     */
    public Coordinates(double latitude,float longitude) {
        this("",latitude,longitude);
    }

     /**
     * Constructor
     *
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     * @param time The timestamp of the coordinate.
     */
    public Coordinates(double latitude,float longitude,String time) {
        this("",latitude,longitude,0,time);
    }

    /**
     * Constructor
     *
     * @param name      The name of the point.
     * @param latitude The latitude in decimal degrees.
     * @param longitude The longitude in decimal degrees.
     * @param altitude The altitude in meters.
     * @param time The timestamp of the coordinate.
     */
    public Coordinates(String name, double latitude, double longitude,float altitude,String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.altitude = altitude;
        this.time = time;
    }


    /**
     * Set the longitude of the coordinate.
     * @param longitude in decimal degrees.
     */
   public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Set the latitude of the coordinate.
     * @param latitude in decimal degrees.
     */
   public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Set the altitude of the coordinate.
     * @param altitude in meters.
     */
   public void setAltitude(float altitude) {
      this.altitude = altitude;
    }

    /**
     * Set the timestamp of the coordinate.
     * @param time Set the timestamp.
     */
   public void setTime(String time) {
       this.time = time;
    }

    /**
     * Set the name of the coordinate.
     * @param name Set the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the latitude of the coordinate.
     * @return Return the latitude in decimal degrees.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the latitude of the coordinate.
     * @return Return the longitude in decimal degrees.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Get the altitude.
     * @return Return the altitude in meters.
     */
    public float getAltitude() {
        return altitude;
    }

    /**
     * Get the timestamp of the coordinate.
     * @return Return the timestamp of the coordinate in the format hh:mm:ss.
     */
    public String getTime() {
        return time;
    }

    /**
     * Get the name of the coordinate.
     * @return Return the name of the point.
     */
    public String getName() {
        return name;
    }
}
