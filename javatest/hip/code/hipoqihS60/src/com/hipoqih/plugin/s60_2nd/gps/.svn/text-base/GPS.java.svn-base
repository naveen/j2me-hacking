package com.hipoqih.plugin.s60_2nd.gps;

/**
 * This is the main class of the API. It keeps the position refreshed constantly. You can acces the
 * current coordinate with the getCoordinates method, and the others data given by the gps can be accessed
 * with the appropriates methods. If no fix has been found yet, data returned is generally 0 or "". If the fix is lost,
 * the gps device stopped or Bluetooth connection lost, the data is equal to the last data received from the gps.
 * When the phone goes into sleep mode, it can sometimes interrupt the Bluetooth connection. To avoid this, you must prevent
 * your phone to goes into sleep mode, by using in example display.flashBacklight(1) every x secondes in you program.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */
public class GPS implements MessageListener {

    private static final double KnotsToMiles = 1.15078;

    private NMEAObject message = null;
    private BluetoothConnection bt = null;
    private NMEAObject GSVSentence1, GSVSentence2, GSVSentence3;
    private String time = "";
    private float speedKmh = 0;
    private float speedKnot = 0;
    private String fixQuality = "";
    private int satellitesNumber = 0;
    private float HDOP = 0;
    private float DOP = 0;
    private float VDOP = 0;
    private float altitudeDifference = 0;
    private String fixType = "";
    private char fixSelectionType = 'N';
    private int[] satellitesForFix = new int[12];
    private static Satellite[] satellitesInView = new Satellite[12];
    private static Satellite[] satellitesTemp = new Satellite[12];
    private char status = 'N';
    private float azimuth = 0;
    private float magneticAzimuth = 0;
    private String date = "";
    private String magneticVariation = "";
    private int numberOfSatellitesInView = 0;
    private char validityOfData = 'N';
    private String waypointLatitude = "";
    private String waypointLongitude = "";
    private String waypointName = "";
    private int residualsMode = -1;            //0 : residuals used in GGA 1: residuals calculated after GGA
    private boolean hasFix = false;
    private Coordinates point = new Coordinates(0, 0);
    private SIRFMessages sirfMessages = null;
    private NMEAMessages nmeaMessages = null;


    /**
     * Constructor
     *
     * @param bluetoothManager The <code>BluetoothConnection</code> wich will get the NMEA messages for this class.
     */
    public GPS(BluetoothConnection bluetoothManager) {
        bt = bluetoothManager;
        bt.setMessageListener(this);
        sirfMessages = new SIRFMessages(bt);
        nmeaMessages = new NMEAMessages(bt);
    }

    /**
     * Set a new <code>BluetoothConnection</code> wich will get the NMEA messages for this class.
     *
     * @param bluetoothConnection
     */
    public void setBluetooth(BluetoothConnection bluetoothConnection) {
        bt = bluetoothConnection;
        bt.setMessageListener(this);
    }

    /**
     * Get the current <code>BluetoothConnection</code> wich get the NMEA messages for this class.
     *
     * @return bluetooth manager.
     */
    public BluetoothConnection getBluetooth() {
        return bt;
    }


    /**
     * Return a <code>Coordinates</code> object wich contains the current position.
     * Coordinates and altitude are equal to 0 if no coordinates have been received yet, and time equals to "00:00:00"
     * Time is the Greenwich Meridian Time.
     *
     * @return The coordinates in a <code>Coordinates</code> object.
     * @see Coordinates
     */
    public Coordinates getCoordinates() {
        return point;
    }


    /**
     * Returns the altitude difference between the mean sea level and WGS84. Returns 0 if cannot be determined yet.
     * ellipsoid.
     *
     * @return The altitude difference in meters.
     */
    public float getAltitudeDifference() {
        return altitudeDifference;
    }

    /**
     * Returns the current time given by the gps. Time equals to "00:00:00" if no time has been received yet.
     *
     * @return The time in the format HH:MM:SS (Greenwich Meridian Time).
     */
    public String getTime() {
        return makeTimeAndDate(time);
    }

    /**
     * Returns the current speed in kilometers/hour. Returns 0 if cannot be determined yet.
     *
     * @return The speed in kilometers/hour.
     */
    public float getSpeedKmh() {
        return speedKmh;
    }

    /**
     * Returns the current speed in miles per hour. Returns 0 if cannot be determined yet.
     *
     * @return The speed in miles per hour.
     */
    public float getSpeedMph() {
        return (float) (speedKnot * KnotsToMiles);
    }

    /**
     * Returns the current speed in mters/second. Returns 0 if cannot be determined yet.
     *
     * @return The speed in meters/second.
     */
    public float getSpeedMS() {
        return (float) (speedKmh / 3.6);
    }

    /**
     * Return the current speed in nautical miles per hour. Returns 0 if cannot be determined yet.
     *
     * @return The speed in nautilcal miles per hour.
     */
    public float getSpeedKnot() {
        return speedKnot;
    }


    /**
     * Returns the last message receveid from the gps in the NMEA format.
     *
     * @return The last NMEA String received.
     */
    public String getNMEAMessage() {
        return message.toNMEASentence();
    }

    /**
     * Returns the residuals mode. Returns -1 if cannot be determined yet.
     *
     * @return 0: residuals used in GGA, 1: residuals calculated after GGA
     */
    public int getResidualsMode() {
        return residualsMode;
    }

    /**
     * Returns if the gps device has currently a fix or not. If there is no Bluetooth connection with a gps device, it return false.
     * This method use GGA,VTG,RMC, and GSA sentences to determine if the gps has a fix or not, so if all those
     * sentences are deactivated, the fix can't be determined.
     *
     * @return <code>true</code> if the gps has a fix, else return <code>false</code>.
     */
    public boolean hasFix() {
        return hasFix;
    }

    /**
     * Returns the magnetic variation. Returns "" if cannot be determined yet.
     *
     * @return The magnetic variation.
     */
    public String getMagneticVariation() {
        return magneticVariation;
    }

    /**
     * Returns the validity of data. If the validity cannot be determined yet, returns 'N'.
     *
     * @return A=Active (data valid), V=Void (data not valid).
     */
    public char getValidityOfData() {
        return validityOfData;
    }

     /**
     * Returns the type of the fix. Returns "" if cannot be determined yet.
     *
     * @return Values can be : <code>no fix, 2D fix, 3D fix</code>.
     */
    public String getFixType() {
        return fixType;
    }

    /**
     * Returns the fix selection type. Returns 'N' if cannot be determined yet.
     *
     * @return 'A'= Automatic, 'M'=Manual
     */
    public char getFixSelectionType() {
        return fixSelectionType;
    }

    /**
     * Returns the number of satellites being tracked. Returns 0 if cannot be determined yet.
     *
     * @return The number of satellites being tracked.
     */
    public int getSatellitesNumber() {
        return satellitesNumber;
    }

    /**
     * Returns the PRN of satellites being tracked.
     * @return An array wich contains the PRN of satellites being tracked.
     */
    public int[] getSatellitesForFix(){
        return satellitesForFix;
    }

    /**
     * Returns the horizontal dilution of precision. Returns 0 if cannot be determined yet.
     *
     * @return The horizontal dilution of precision in meters.
     */
    public float getHorizontalDilution() {
        return HDOP;
    }

    /**
     * Returns the vertical dilution of precision. Returns 0 if cannot be determined yet.
     *
     * @return The vertical dilution of precision in meters.
     */
    public float getVerticalDilution() {
        return VDOP;
    }

    /**
     * Returns the dilution of precision. Returns 0 if cannot be determined yet.
     *
     * @return The dilution of precision in meters.
     */
    public float getDilution() {
        return DOP;
    }



    /**
     * Returns the fix quality. Returns "" if cannot be determined yet.
     *
     * @return Values can be : <code>invalid ,gps fix, DGPS fix, PPS fix, Real Time Kinematic,
     * Float Real Time Kinematic, estimated, Manual input mode, Simulation mode<code>.
     */
    public String getFixQuality() {
        return fixQuality;
    }


    /**
     * Returns the azimuth of the current direction of the movement. So if the gps device is still,
     * the value will most likely random. Returns 0 if cannot be determined yet.
     *
     * @return The azimuth in degrees.
     */
    public float getAzimuth() {
        return azimuth;
    }

    /**
     * Returns the magnetic azimuth of the current direction of the movement. So if the gps device is still,
     * the value will most likely random. Returns 0 if cannot be determined yet.
     *
     * @return The azimuth in degrees.
     */
    public float getMagneticAzimuth() {
        return magneticAzimuth;
    }

    /**
     * Returns the current date. Returns "" if cannot be determined yet.
     *
     * @return The date in the format DD:MM:YY.
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the status. If no fix has been found yet, return 'N'.
     *
     * @return A=Active, M=Manual.
     */
    public char getStatus() {
        return status;
    }

    /**
     * Returns the number of satellites in view. Returns 0 if cannot be determined yet.
     *
     * @return The number of satellites in view
     */
    public int getNumberOfSatellitesInView() {
        return numberOfSatellitesInView;
    }

    /**
     * Returns the satellites in view.
     *
     * @return an Array of size 12 of <code>Satellite</code> in view. Use <code>getNumberOfSatellitesInView()</code>
     * to know how many <code>Satellite</code> there is in the array.
     */
    public Satellite[] getSatellitesInView() {
        return satellitesInView;
    }

    /**
     * Switch the gps device into NMEA protocol.
     * <p>
     * The settings will be a baud rate of 4800 and
     * <li>GGA rate : 1
     * <li>GLL rate : 0
     * <li>GSA rate : 5
     * <li>GSV rate : 5
     * <li>MSS rate : 0
     * <li>RMC rate : 0
     * <li>VTG rate : 0
     */
    public void switchToNMEAProtocol() {
        sirfMessages.sendSIRFMessageToNMEAProtocol();
    }

   /**
     * Switch the gps device to the NMEA protocol.
     * The rate for the GGARate, GLLRate, GSARate, GSVRate, MSSRate, RMCRate and VTGRate must be between 0 and 255.
     * A rate of 5 means the output of messages from the gps device will be every 5 seconds.
     * A rate of 0 turn the output of this type fo messages off.
     *
     * @param GGARate The rate of GGA messages.
     * @param GLLRate The rate of GLL messages.
     * @param GSARate The rate of GSA messages.
     * @param GSVRate The rate of GSV messages.
     * @param MSSRate The rate of MSS messages.
     * @param RMCRate The rate of RMC messages.
     * @param VTGRate The rate of VTG messages.
     * @param baud The rate in bauds, must be either 1200, 2400, 4800, 9600, 19200, 38400 or 57600.
     * @throws NumberFormatException Thrown if the rate is not one of the correct values.
     */
    public void switchToNMEAProtocol(int GGARate, int GLLRate, int GSARate, int GSVRate, int MSSRate, int RMCRate, int VTGRate, int baud) throws NumberFormatException {
        if (GGARate < 0 || GGARate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (GLLRate < 0 || GLLRate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (GSARate < 0 || GSARate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (GSVRate < 0 || GSVRate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (MSSRate < 0 || MSSRate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (RMCRate < 0 || RMCRate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (VTGRate < 0 || VTGRate > 255) throw new NumberFormatException("Rate must be between 0 and 255.");
        if (!(baud == 1200 || baud == 2400 || baud == 4800 || baud == 9600 || baud == 19200 || baud == 38400 || baud == 57600))
            throw new NumberFormatException("Invalid Baud number");
        sirfMessages.sendSIRFMessageToNMEAProtocol(GGARate, GLLRate, GSARate, GSVRate, MSSRate, RMCRate, VTGRate, baud);
    }

    /**
     * Switch the gps device to the SIRF protocol, with a default baud rate of 9600.
     */
    public void switchToSIRFProtocol() {
        nmeaMessages.setSIRFProtocol(9600);
    }

    /**
     * Switch the gps to the SIRF protocol.
     *
     * @param baud The rate in bauds, must be either 4800, 9600, 19200 or 38400.
     * @throws NumberFormatException Thrown if the rate is not one of the correct values.
     */
    public void switchToSIRFProtocol(int baud) throws NumberFormatException {
        if (!(baud == 4800 || baud == 9600 || baud == 19200 || baud == 38400))
            throw new NumberFormatException("Invalid Baud number");
        nmeaMessages.setSIRFProtocol(baud);
    }

    /**
     * Set the DGPS Port with a rate of 9600 bauds.
     * This method has not been tested but should work.
     */
    public void setDGPSPort() {
        nmeaMessages.setDGPSPort(9600);
    }

    /**
     * Set the DGPS Port.
     * This method has not been tested but should work.
     *
     * @param baud The rate of transmission in bauds. Must be either 4800, 9600, 19200 or 38400.
     * @throws NumberFormatException Thrown if the rate is not one of the correct values.
     */
    public void setDGPSPort(int baud) throws NumberFormatException {
        if (!(baud == 4800 || baud == 9600 || baud == 19200 || baud == 38400))
            throw new NumberFormatException("Invalid Baud number");
        nmeaMessages.setDGPSPort(baud);
    }

    /**
     * Set the rate of GGA messages send by the gps device.
     * GGA => Global positioning system fixed data, contains informations about latitude, longitude, altitude, time, date and the type of the fix.
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setGGARate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setGGARate(rate);
    }

    /**
     * Set the rate of GLL messages send by the gps device.
     * GLL => Geographic position – latitude/longitude.
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setGLLRate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setGLLRate(rate);
    }

    /**
     * Set the rate of GSA messages send by the gps device.
     * GSA => GNSS DOP, active satellites and fix quality.
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setGSARate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setGSARate(rate);
    }

    /**
     * Set the rate of GSV messages send by the gps device.
     * GSV => GNSS satellites in view.
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setGSVRate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setGSVRate(rate);
    }

    /**
     * Set the rate of RMC messages send by the gps device.
     * RMC => Recommended minimum specific GNSS data : longitde, latitude, speed, direction, status and date
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setRMCRate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setRMCRate(rate);
    }

    /**
     * Set the rate of VTG messages send by the gps device.
     * VTG => Course over ground and ground speed
     *
     * @param rate The rate must be between 0 and 255. A rate of 5 mean the output of messages will be every 5 seconds.
     *             A rate of 0 turn the output off.
     * @throws NumberFormatException Thrown if the rate is not between 0 and 255.
     */
    public void setVTGRate(int rate) throws NumberFormatException {
        if (rate > 255 || rate < 0) throw new NumberFormatException(" Rate value must be between 0 and 255");
        nmeaMessages.setVTGRate(rate);
    }

    /**
     * Set the SBAS on or off.
     *
     * @param on <code>true</code> to activate SBAS, <code>false</code> to turn it off.
     */
    public void setSBAS(boolean on) {
        nmeaMessages.setSbas(on);
    }

    //Check the type of the message and then put the data into the corresponding variables
    private void process() {
        if (message.getType().equals("GPGGA")) {
            time = message.getData(1);
            point.setTime(makeTimeAndDate(time));
            try {
                //if there is no latitude, the gps has no fix
                if (message.getData(2).equals("") || !bt.isServiceConnected())
                    hasFix = false;
                else
                    hasFix = true;
                point.setLatitude(degreesMinToDegrees(message.getData(2) + message.getData(3)));
                point.setLongitude(degreesMinToDegrees(message.getData(4) + message.getData(5)));
            } catch (NumberFormatException e) {
            }
            if (message.getData(6) != "") {
                try {
                    switch (Integer.parseInt(message.getData(6))) {
                        case 0:
                            fixQuality = "invalid";
                            break;
                        case 1:
                            fixQuality = "gps fix";
                            break;
                        case 2:
                            fixQuality = "DGPS fix";
                            break;
                        case 3:
                            fixQuality = "PPS fix";
                            break;
                        case 4:
                            fixQuality = "Real Time Kinematic";
                            break;
                        case 5:
                            fixQuality = "Float Real Time Kinematic";
                            break;
                        case 6:
                            fixQuality = "estimated";
                            break;
                        case 7:
                            fixQuality = "Manual input mode";
                            break;
                        case 8:
                            fixQuality = "Simulation mode";
                            break;
                    }
                } catch (Exception e) {
                }
            }
            try {
                satellitesNumber = Integer.parseInt(message.getData(7));
            } catch (Exception e) {
            }
            try {
                HDOP = Float.parseFloat(message.getData(8));
            } catch (Exception e) {
            }
            try {
                point.setAltitude(Float.parseFloat(message.getData(9)));
            } catch (Exception e) {
            }
            try {
                altitudeDifference = Float.parseFloat((message.getData(11)));
            } catch (Exception e) {
            }

        } else if (message.getType().equals("GPGSA")) {
            if (message.getData(1) == "A")
                fixSelectionType = 'A';
            else if (message.getData(1) == "M") fixSelectionType = 'M';
            try {
                switch (Integer.parseInt(message.getData(2))) {
                    case 1:
                        fixType = "no fix";
                        hasFix = false;
                        break;
                    case 2:
                        fixType = "2D fix";
                        if (bt.isServiceConnected())
                            hasFix = true;
                        else
                            hasFix = false;
                        break;
                    case 3:
                        fixType = "3D fix";
                        if (bt.isServiceConnected())
                            hasFix = true;
                        else
                            hasFix = false;
                        break;
                }
            } catch (Exception e) {
            }
            for (int i = 0; i <= 11; i++) {
                try {
                    satellitesForFix[i] = Integer.parseInt(message.getData(i + 3));
                } catch (Exception e) {
                }
            }
            try {
                DOP = Float.parseFloat(message.getData(15));
                HDOP = Float.parseFloat(message.getData(16));
                VDOP = Float.parseFloat(message.getData(17));
            } catch (Exception e) {
            }

        } else if (message.getType().equals("GPGST")) {

        } else if (message.getType().equals("GPGSV")) {
            switch (Integer.parseInt(message.getData(2))) {
                case 1:
                    GSVSentence1 = message;
                    break;
                case 2:
                    GSVSentence2 = message;
                    break;
                case 3:
                    {
                        GSVSentence3 = message;
                        processGSV(GSVSentence1);
                        processGSV(GSVSentence2);
                        processGSV(GSVSentence3);
                        break;
                    }
            }
        } else if (message.getType().equals("GPRMC")) {
            time = message.getData(1);
            point.setTime(makeTimeAndDate(time));
            if (message.getData(2) == "A")
                status = 'A';
            else if (message.getData(2) == "M") status = 'M';
            try {
                //if there is no latitude, the gps has no fix
                if (message.getData(3).equals("") || !bt.isServiceConnected())
                    hasFix = false;
                else
                    hasFix = true;
                point.setLatitude(degreesMinToDegrees(message.getData(3) + message.getData(4)));
                point.setLongitude(degreesMinToDegrees(message.getData(5) + message.getData(6)));
            } catch (NumberFormatException e) {
            }
            try {
                speedKnot = Float.parseFloat(message.getData(7));
            } catch (Exception e) {

            }
            try {
                azimuth = Float.parseFloat(message.getData(8));
            } catch (Exception e) {
            }
            date = makeTimeAndDate(message.getData(9));
            magneticVariation = message.getData(10) + message.getData(11);

        } else if (message.getType().equals("GPGLL")) {
            try {
                //if there is no latitude, the gps has no fix
                if (message.getData(1) == "") hasFix = false;
                point.setLatitude(degreesMinToDegrees(message.getData(1) + message.getData(2)));
                point.setLongitude(degreesMinToDegrees(message.getData(3) + message.getData(4)));
            } catch (NumberFormatException e) {
            }
            time = message.getData(5);
            if (message.getData(6) == "A") validityOfData = 'A';


        } else if (message.getType().equals("GPVTG")) {
            try {
                azimuth = Float.parseFloat(message.getData(1));
            } catch (Exception e) {
            }
            try {
                magneticAzimuth = Float.parseFloat(message.getData(3));
            } catch (Exception e) {
            }
            try {
                speedKnot = Float.parseFloat(message.getData(5));
            } catch (Exception e) {
            }
            try {
                //if there is no speed, the gps has no fix
                if (message.getData(7).equals("") || !bt.isServiceConnected())
                    hasFix = false;
                else
                    hasFix = true;
                speedKmh = Float.parseFloat(message.getData(7));
            } catch (Exception e) {
            }

        } else if (message.getType().equals("GPWPL")) {
            waypointLatitude = message.getData(1) + message.getData(2);
            waypointLongitude = message.getData(3) + message.getData(4);
            waypointName = message.getData(5);

        } else if (message.getType().equals("GPAAM")) {

        } else if (message.getType().equals("GPAPB")) {

        } else if (message.getType().equals("GPBOD")) {

        } else if (message.getType().equals("GPBWC")) {

        } else if (message.getType().equals("GPRTE")) {

        } else if (message.getType().equals("GPXTE")) {

        } else if (message.getType().equals("GPALM")) {

        } else if (message.getType().equals("GPZDA")) {

        } else if (message.getType().equals("GPMSK")) {

        } else if (message.getType().equals("GPMSS")) {

        } else if (message.getType().equals("GPGRS")) {
            try {
                residualsMode = Integer.parseInt(message.getData(2));
            } catch (Exception e) {
            }
            for (int i = 1; i <= 12; i++) {
                try {
                    satellitesInView[i].setResidual(Float.parseFloat(message.getData(i + 2)));
                } catch (Exception e) {
                }
            }

        } else if (message.getType().equals("GPROO")) {

        } else if (message.getType().equals("GPRMA")) {

        } else if (message.getType().equals("GPRMB")) {

        } else if (message.getType().equals("GPTRF")) {

        } else if (message.getType().equals("GPSTN")) {

        } else if (message.getType().equals("GPVBW")) {

        }

    }

    //make the time and date into a more readable format
    private String makeTimeAndDate(String time) {
        StringBuffer result = new StringBuffer();
        try {
            result.append(time.charAt(0));
            result.append(time.charAt(1));
            result.append(':');
            result.append(time.charAt(2));
            result.append(time.charAt(3));
            result.append(':');
            result.append(time.charAt(4));
            result.append(time.charAt(5));
        } catch (StringIndexOutOfBoundsException e) {
            return "00:00:00";
        }
        return result.toString();
    }


    /*
     * Convert latitude or longitude with degrees, minutes, seconds into degrees
     * ex: 15634.4400W is converted into -156.5788888 or 0132.0020N into 1.533336666
     * (west and south values are negatives)
     */
    private double degreesMinToDegrees(String position) throws NumberFormatException {
        double result;
        try {
            if (position.charAt(position.length() - 1) == 'W' || position.charAt(position.length() - 1) == 'E') {
                result = Double.parseDouble(position.substring(6, 10)) / 10000;
                result = (Double.parseDouble(position.substring(3, 5)) + result) / 60;
                result += Double.parseDouble(position.substring(0, 3));
            } else {
                result = Double.parseDouble(position.substring(5, 9)) / 10000;
                result = (Double.parseDouble(position.substring(2, 4)) + result) / 60;
                result += Double.parseDouble(position.substring(0, 2));
            }
            if (position.charAt(position.length() - 1) == 'W' || position.charAt(position.length() - 1) == 'S')
                return -result;
            else
                return result;
        } catch (StringIndexOutOfBoundsException e) {
            throw new NumberFormatException("The format is not valid");
        }
    }

    public void messageReceived(String NMEAMessage) {
        message = new NMEAObject();
        message.setData(NMEAMessage);
        process();
    }

    //process GSV messages
    private void processGSV(NMEAObject message) {
        int sentenceNumber = 0;
        int sentenceTotal = 0;
        int i = 0;
         numberOfSatellitesInView = Integer.parseInt(message.getData(3));
        try {

            sentenceNumber = Integer.parseInt(message.getData(2));
            sentenceTotal = Integer.parseInt(message.getData(1));
        } catch (Exception e) {
        }
        //if we process the first sentence and if the number of satellites has changed
        if (sentenceNumber == 1 && Integer.parseInt(message.getData(3)) != numberOfSatellitesInView) {
            satellitesInView = new Satellite[numberOfSatellitesInView];
            satellitesTemp = new Satellite[numberOfSatellitesInView];
        }
        Satellite sat = new Satellite();
        try {

            for (i = 1; i <= 4; i++) {
                sat = new Satellite();
                sat.setPRN(Integer.parseInt(message.getData(i * 4)));
                sat.setElevation(Integer.parseInt(message.getData(i * 4 + 1)));
                sat.setAzimuth(Integer.parseInt(message.getData(i * 4 + 2)));
                sat.setSNR(Integer.parseInt(message.getData(i * 4 + 3)));
                satellitesTemp[i - 1 + ((sentenceNumber - 1) * 4)] = sat;
            }
        } catch (Exception e) {
            satellitesTemp[i - 1 + ((sentenceNumber - 1) * 4)] = sat;
            //if (sentenceNumber == sentenceTotal) satellitesInView = satellitesTemp;
        }
        if (sentenceNumber == sentenceTotal) satellitesInView = satellitesTemp;
    }
}


