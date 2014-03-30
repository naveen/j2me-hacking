package com.hipoqih.plugin.s60_2nd.gps;

/**
 * Represents a satellite in view of the gps device.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 * 
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */
public class Satellite {
    private int PRN;
    private int elev;
    private int azi;
    private int SNR;
    private float res;

    /**
     * Constructor
     */
    public Satellite() {

    }

    /**
     * Set the PRN of the satellite.
     * @param prn The PRN, this is the ID of the satellite.
     */
    void setPRN(int prn) {
        PRN = prn;
    }

    /**
     * Returns the PRN of the satellite.
     * @return The PRN, this is the ID of the satellite.
     */
    public int getPRN() {
        return PRN;
    }


    /**
     *  Set the elevation of the satellite.
     * @param elevation The elevation.
     */
    void setElevation(int elevation) {
        elev = elevation;
    }

    /**
     * Returns the elevation of the satellite.
     * @return The elevation.
     */
    public int getElevation() {
        return elev;
    }

    /**
     * Set the azimuth of the satellite.
     * @param azimuth  The azimuth in degrees.
     */
    void setAzimuth(int azimuth) {
        azi = azimuth;
    }

    /**
     * Returns the azimuth of the satellite.
     * @return The azimuth in degrees.
     */
    public int getAzimuth() {
        return azi;
    }

    /**
     * Set the Signal To Noise ratio of the satellite.
     * @param snr The Signal To Noise ratio (higher is better, from 0 to 99)
     */
    void setSNR(int snr) {
        SNR = snr;
    }

    /**
     * Returns the Signal To Noise ratio of the satellite .
     * @return The Signal To Noise ratio (higher is better, from 0 to 99).
     */
    public int getSNR() {
        return SNR;
    }

    /**
     * Returns the residual of the satellite.
     * @return The residual value in meters.
     */
    public float getResidual(){
        return res;
    }

    /**
     * Set the residual of the satellite.
     * @param residual in meters.
     */
    public void setResidual(float residual){
        res=residual;
    }

}
