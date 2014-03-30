package com.hipoqih.plugin.s60_2nd.gps.utilities;


/**
 * Reference the codes of the datums used for the conversion between WGS84 datum (gps) and another datum.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @see Utilities
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */

public abstract class CoordinatesFormat {

    /**
     * Coordinates in decimal degrees, south and west values are negatives
     */
    public static final int DECIMALDEGREES = 0;

    /**
     * Latitude and longitude in the degrees, minutes, seconds format.
     * Ex: -156.5788 is converted into 156°34'44.00''W or 1.533336666 into 01°32'00.20''N
     */
    public static final int DEGREESMINSEC = 1;

    /**
     * CH1903 system (swiss coordinates)
     */
    public static final int CH1903 = 2;

    /**
     * NTF (french coordinates)
     * translation
     */
    public static final int NTF = 3;

    /**
     * Lambert 1 system (northern french coordinates)
     */
    public static final int LAMBERT1 = 4;

    /**
     * Lambert 2 system (center french coordinates)
     */
    public static final int LAMBERT2 = 5;

    /**
     * Lambert 2 Etendu system (french coordinates)
     */
    public static final int LAMBERT2ETENDU = 6;

    /**
     * Lambert 3 system (southern french coordinates)
     */
    public static final int LAMBERT3 = 7;

    /**
     * Lambert 4 system (Corsica)
     */
    public static final int LAMBERT4 = 8;

    /**
     * Lambert 93 system (french coordinates)
     */
    public static final int LAMBERT93 = 9;

    /**
     * BD72 (belgium)
     * translation
     */
    public static final int BD72 = 10;

    /**
     * Lambert72 (belgium)
     */
    public static final int LAMBERT72 = 11;

}
