package com.hipoqih.plugin.s60_2nd.gps.utilities;

/**
 * Offers various useful methods related to geopositionning.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */


public abstract class Utilities {
    // value found at: http://scienceworld.wolfram.com/astronomy/EarthRadius.html
    //http://www.iugg.org/

    /**
     * Used to precise that the value given for the <code>degreesToDegreesMinSec</code> method is a latitude.
     */
    public static final int LATITUDE = 0;
    /**
     * Used to precise that the value given for <code>degreesToDegreesMinSec</code> method is a longitude.
     */
    public static final int LONGITUDE = 1;
    // values were found on http://www.metric-conversions.org
    private static final double metersToFeet = 3.28084;
    private static final double KnotsToKm = 1.852;
    private static final double KnotsToMiles = 1.15078;
    private static final double KMToMiles = 0.62137;
    private static final double yardsToFeet = 3;


    /**
     * Convert WGS84 datum (gps) coordinates into topographics coordinates of another datum.
     * <p>Class to translate and transform latitude and longitude coordinates (WGS84) to Northing (Coordinates Y) and Easting (Coordinates X).
     * <p>The translation model is done by the Molodensky Model.
     * <ul><li>For the Belgium system, the translation used is called <code>BD72</code>.
     * <li>For the French System, the translation used is called <code>NTF</code>.</ul>
     * <p>Different projection System :  <ul><li><code>lambert 72</code> (Belgium System)
     *                                   <li><code>Lambert I</code> (North part of France)
     *                                   <li><code>Lambert II</code> (Middle part of France)
     *                                   <li><code>Lambert II</code> etendu (all part of france)
     *                                   <li><code>Lambert III</code> (South part of France)
     *                                   <li><code>Lambert IV</code>  (Corsica)
     *                                   <li><code>Lambert 93</code>  (New System in France)</ul>
     * @see CoordinatesFormat
     * @param latitude  The longitude value in decimal degrees.
     * @param longitude The latitude value in decimal degrees.
     * @param altitude  The altitude in meters.
     * @param datum   The code of the datum you want to convert the gps coordinates into.
     *                  Use CoordinatesFormat.code to acces the codes. ex: CoordinatesFormat.USA or CoordinatesFormat.CH
     * @return The result in a int array of size 3. The first value is the vertical coordinate, the second is the horizontal coordinate.
     *         coordinate and the third is the altitude.
     * @throws NumberFormatException is thrown when the CoordinatesFormat code is not valid (is not one of a country or don't exist).
     */
    public static double[] toDatum(double latitude, double longitude, double altitude, int datum) throws NumberFormatException {
        double[] result = new double[3];
        switch (datum) {
            case CoordinatesFormat.CH1903 :
                result = CH1903.WGS84ToCH1903(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.NTF :
                result = WGS84ToNTF(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT1 :
                result = WGS84ToLambert1(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT2 :
                result = WGS84ToLambert2(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT2ETENDU :
                result = WGS84ToLambert2E(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT3 :
                result = WGS84ToLambert3(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT4 :
                result = WGS84ToLambert4(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT93 :
                result = WGS84ToLambert93(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.BD72 :
                result = WGS84ToBD72(latitude, longitude, altitude);
                break;
            case CoordinatesFormat.LAMBERT72 :
                result = WGS84ToLambert72(latitude, longitude, altitude);
                break;
            default :
                throw new NumberFormatException("The CoordinatesFormat is not valid, must be a valid datum");
        }
        return result;
    }

    /**
     * Convert WGS84 datum (gps) coordinates into topographics coordinates of another datum.
     * <p>Class to translate and transform latitude and longitude coordinates (WGS84) to Northing (Coordinates Y) and Easting (Coordinates X).
     * <p>The translation model is done by the Molodensky Model.
     * <ul><li>For the Belgium system, the translation used is called <code>BD72</code>.
     * <li>For the French System, the translation used is called <code>NTF</code></ul>
     * <p>Different projection System :  <ul><li><code>lambert 72</code> (Belgium System)
     *                                <li><code>Lambert I</code> (North part of France)
     *                                <li><code>Lambert II</code> (Middle part of France)
     *                                <li><code>Lambert II</code> etendu (all part of france)
     *                                <li><code>Lambert III</code> (South part of France)
     *                                <li><code>Lambert IV</code>  (Corsica)
     *                                <li><code>Lambert 93</code>  (New System in France)</ul>
     * @see CoordinatesFormat
     * @param latitude  The longitude value in decimal degrees.
     * @param longitude The latitude value in decimal degrees.
     * @param datum   The code of the datum you want to convert the gps coordinates into.
     *                  Use CoordinatesFormat.code to acces the codes. ex: CoordinatesFormat.USA or CoordinatesFormat.CH
     * @return The result in a int array of size 2. The first value is the vertical coordinate, the second is the horizontal
     *         coordinate and the third is the altitude.
     * @throws NumberFormatException is thrown when the CoordinatesFormat code is not valid (is not one of a country or don't exist).
     */
    public static double[] toDatum(double latitude, double longitude, int datum) throws NumberFormatException {
        double[] result = new double[2];
        switch (datum) {
            case CoordinatesFormat.CH1903 :
                result = CH1903.WGS84ToCH1903(latitude, longitude);
                break;
            case CoordinatesFormat.NTF :
                result = WGS84ToNTF(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT1 :
                result = WGS84ToLambert1(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT2 :
                result = WGS84ToLambert2(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT2ETENDU :
                result = WGS84ToLambert2E(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT3 :
                result = WGS84ToLambert3(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT4 :
                result = WGS84ToLambert4(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT93 :
                result = WGS84ToLambert93(latitude, longitude);
                break;
            case CoordinatesFormat.BD72 :
                result = WGS84ToBD72(latitude, longitude);
                break;
            case CoordinatesFormat.LAMBERT72 :
                result = WGS84ToLambert72(latitude, longitude);
                break;
            default :
                throw new NumberFormatException("The CoordinatesFormat is not valid, must be a valid datum");
        }
        return result;
    }

    /**
     * Convert topographics coordinates of a country into WGS84 datum (gps) coordinates.
     * <p>Class to transform and translate Northing and Easting to latitude and longitude coordinates (WGS84).
     * <p>Different projection System :  <ul><li>lambert72 (Belgium System)
     *                                   <li><code>Lambert I</code> (North part of France)
     *                                   <li><code>Lambert II</code> (Middle part of France)
     *                                   <li><code>Lambert II</code> etendu (all part of france)
     *                                   <li><code>Lambert III</code> (South part of France)
     *                                   <li><code>Lambert IV</code>  (Corsica)
     *                                   <li><code>Lambert 93</code>  (New System in France)</ul>
     * <p>The translation model is done by the Molodensky Model.
     * <ul><li>For the Belgium system, the translation used is called <code>BD72</code>.
     * <li>For the French System, the translation used is called <code>NTF</code>.</ul>
     * @see CoordinatesFormat
     * @param x        The horizontal value.
     * @param y        The vertical value.
     * @param altitude The altitude.
     * @param datum  The code of the datum from wich you want to convert the coordinates into gps coordinates
     *                 Use CoordinatesFormat.code to acces the codes. ex: CoordinatesFormat.USA or CoordinatesFormat.CH
     * @return The result is a double array of size 3. The first value is the latitude coordinate, the second is the longitude
     *         coordinate and the third is the altitude. The longitude and latitude are in decimal degrees and the altitude is in meters.
     * @throws NumberFormatException is thrown when the CoordinatesFormat code is not valid (is not one of a country or don't exist).
     */
    public static double[] toWGS84(double y, double x, double altitude, int datum) throws NumberFormatException {
        double[] result = new double[3];
        switch (datum) {
            case CoordinatesFormat.CH1903 :
                result = CH1903.CH1903ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.NTF :
                result = NTFToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT1 :
                result = lambert1ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT2 :
                result = lambert2ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT2ETENDU :
                result = lambert2EToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT3 :
                result = lambert3ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT4 :
                result = lambert4ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT93 :
                result = lambert93ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.BD72 :
                result = BD72ToWGS84(y, x, altitude);
                break;
            case CoordinatesFormat.LAMBERT72 :
                result = lambert72ToWGS84(y, x, altitude);
                break;

            default :
                throw new NumberFormatException("The CoordinatesFormat is not valid, must be the code of a country");
        }
        return result;
    }

    /**
     * Convert topographics coordinates of a country into WGS84 datum (gps) coordinates.
     * <p>Class to translate and transform Northing and Easting to latitude and longitude coordinates (WGS84).
     * <p>Different projection System :  <ul><li><code>lambert 72</code> (Belgium System)
     *                                   <li><code>Lambert I</code> (North part of France)
     *                                   <li><code>Lambert II</code> (Middle part of France)
     *                                   <li><code>Lambert II</code> etendu (all part of france)
     *                                   <li><code>Lambert III</code> (South part of France)
     *                                   <li><code>Lambert IV</code>  (Corsica)
     *                                   <li><code>Lambert 93</code>  (New System in France)</ul>
     * <p>The translation model is done by the Molodensky Model.
     * <ul><li>For the Belgium system, the translation used is called <code>BD72</code>.
     * <li>For the French System, the translation used is called <code>NTF</code>.</ul>
     * @see CoordinatesFormat
     * @param x       The horizontal value (Coordinates X).
     * @param y       The vertical value (Coordinates Y).
     * @param datum The code of the country from wich you want to convert the coordinates into gps coordinates
     *                Use CoordinatesFormat.code to acces the codes. ex: CoordinatesFormat.Lambert72 or CoordinatesFormat.CH
     * @return The result in a int array of size 2. The first value is the latitude coordinate, the second is the longitude
     *         coordinate and the third is the altitude. The latitude and longitude are in decimal degrees.
     * @throws NumberFormatException is thrown when the CoordinatesFormat code is not valid (is not one of a country or don't exist).
     */
    public static double[] toWGS84(double y, double x, int datum) throws NumberFormatException {
        double[] result = new double[2];
        switch (datum) {
            case CoordinatesFormat.CH1903 :
                result = CH1903.CH1903ToWGS84(y, x);
                break;
            case CoordinatesFormat.NTF :
                result = NTFToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT1 :
                result = lambert1ToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT2 :
                result = lambert2ToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT2ETENDU :
                result = lambert2EToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT3 :
                result = lambert3ToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT4 :
                result = lambert4ToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT93 :
                result = lambert93ToWGS84(y, x);
                break;
            case CoordinatesFormat.BD72 :
                result = BD72ToWGS84(y, x);
                break;
            case CoordinatesFormat.LAMBERT72 :
                result = lambert72ToWGS84(y, x);
                break;
            default :
                throw new NumberFormatException("The CoordinatesFormat is not valid, must be the code of a country");
        }
        return result;
    }

    //NTF => translation to the French System
    //Method to translate latitude, longitude and altitude of the WGS84 system to the French System (NTF).
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the new longitude, latitude and altitude respectevely.
    private static double[] WGS84ToNTF(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toNTF(latitude, longitude, altitude);
        return result;
    }

    //NTF => translation to the French System
    //Method to translate latitude and longitude of the WGS84 system to the French System (NTF).
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the new longitude and latitude respectevely.
    private static double[] WGS84ToNTF(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toNTF(latitude, longitude);
        return result;
    }

    //NTF => translation to the French System (Reverse)
    //Reverse method to translate latitude, longitude and altitude of the French system (NTF) to the WGS84 system.
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the new longitude, latitude and altitude respectevely.
    private static double[] NTFToWGS84(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.NTFtoWGS84(latitude, longitude, altitude);
        return result;
    }

    //NTF => translation to the French System (Reverse)
    //Reverse method to translate latitude and longitude of the French system (NTF) to the WGS84 system.
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the new longitude and latitude respectevely.
    private static double[] NTFToWGS84(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.NTFtoWGS84(latitude, longitude);
        return result;
    }

    //LambertI => translation and transformation to the French System (North part)
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert I
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert1(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertI(latitude, longitude, altitude);
        return result;
    }

    //LambertI => translation and transformation to the French System (North part)
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert I
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert1(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertI(latitude, longitude);
        return result;
    }

    //LambertI => translation and transformation to the French System (North part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert I to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert1ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertItoWGS84(northing, easting, altitude);
        return result;
    }

    //LambertI => translation and transformation to the French System (North part) (Reverse)
    //everse method to convert northing and easting coordinates of the projection system of Lambert I to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert1ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertItoWGS84(northing, easting);
        return result;
    }

    //LambertII => translation and transformation to the French System (center part)
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert II
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert2(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertII(latitude, longitude, altitude);
        return result;
    }

    //LambertII => translation and transformation to the French System (center part)
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert II
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert2(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertII(latitude, longitude);
        return result;
    }

    //LambertII => translation and transformation to the French System (center part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert II to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert2ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIItoWGS84(northing, easting, altitude);
        return result;
    }

    //LambertII => translation and transformation to the French System (center part) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert II to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert2ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIItoWGS84(northing, easting);
        return result;
    }

    //LambertII etendu=> translation and transformation to the French System
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert II etendu
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert2E(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIIEtendu(latitude, longitude, altitude);
        return result;
    }

    //LambertII etendu=> translation and transformation to the French System
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert II etendu
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert2E(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIIEtendu(latitude, longitude);
        return result;
    }

    //LambertII etendu=> translation and transformation to the French System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert II etendu to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert2EToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIIEtendutoWGS84(northing, easting, altitude);
        return result;
    }

    //LambertII etendu=> translation and transformation to the French System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert II etendu to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert2EToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIIEtendutoWGS84(northing, easting);
        return result;
    }

    //LambertIII => translation and transformation to the French System (south part)
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert III
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert3(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIII(latitude, longitude, altitude);
        return result;
    }

    //LambertIII => translation and transformation to the French System (south part)
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert III
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert3(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIII(latitude, longitude);
        return result;
    }

    //LambertIII => translation and transformation to the French System (south part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert III to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert3ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIIItoWGS84(northing, easting, altitude);
        return result;
    }

    //LambertIII => translation and transformation to the French System (south part) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert III to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert3ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIIItoWGS84(northing, easting);
        return result;
    }

    //LambertIV => translation and transformation to the French System (Corsica)
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert IV
    //an the first time, the translation is done: WGS84 => NTF.
    //after that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert4(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIV(latitude, longitude, altitude);
        return result;
    }

    //LambertIV => translation and transformation to the French System (Corsica)
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert IV
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert4(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambertIV(latitude, longitude);
        return result;
    }

    //LambertIV => translation and transformation to the French System (Corsica) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert IV to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert4ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIVtoWGS84(northing, easting, altitude);
        return result;
    }


    //LambertIV => translation and transformation to the French System (Corsica) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert IV to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert4ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambertIVtoWGS84(northing, easting);
        return result;
    }

    //Lambert93 => translation and transformation to the French System
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert 93
    //In the first time, the translation is done.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert93(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambert93(latitude, longitude, altitude);
        return result;
    }

    //Lambert93 => translation and transformation to the French System
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert 93
    //In the first time, the translation is done.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert93(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambert93(latitude, longitude);
        return result;
    }

    //Lambert93 => translation and transformation to the French System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert 93 to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert93ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambert93toWGS84(northing, easting, altitude);
        return result;
    }

    //Lambert93 => translation and transformation to the French System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert 93 to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert93ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambert93toWGS84(northing, easting);
        return result;
    }


    //BD72 => translation to the belgium System
    //Method to translate latitude, longitude and altitude of the WGS84 system to the belgium System (BD72).
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the new longitude, latitude and altitude respectevely.
    private static double[] WGS84ToBD72(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toBD72(latitude, longitude, altitude);
        return result;
    }

    //BD72 => translation to the belgium System
    //Method to translate latitude, longitude and altitude of the WGS84 system to the belgium System (BD72).
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the new longitude and latitude respectevely
    private static double[] WGS84ToBD72(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toBD72(latitude, longitude);
        return result;
    }

    //BD72 => translation to the belgium System (Reverse)
    //Reverse method to translate latitude, longitude and altitude of the belgium system (BD72) to the WGS84 system.
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the new longitude, latitude and altitude respectevely.
    private static double[] BD72ToWGS84(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.BD72toWGS84(latitude, longitude, altitude);
        return result;
    }

    //BD72 => translation to the belgium System (Reverse)
    //Reverse method to translate latitude and longitude of the belgium system (BD72) to the WGS84 system.
    //Only the translation in the new system is done, not a transformation.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return  an array with the new longitude and latitude respectevely.
    private static double[] BD72ToWGS84(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.BD72toWGS84(latitude, longitude);
        return result;
    }

    //Lambert72 => translation and transformation to the belgium System
    //Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert 72
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //param altitude double of the altitude.
    //return an array with the northing, easting and height respectevely
    private static double[] WGS84ToLambert72(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambert72(latitude, longitude, altitude);
        return result;
    }

    //Lambert72 => translation and transformation to the belgium System
    //Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert 72
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param latitude double of the latitude.
    //param longitude double of the longitude.
    //return an array with the northing and easting respectevely
    private static double[] WGS84ToLambert72(double latitude, double longitude) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.WGS84toLambert72(latitude, longitude);
        return result;
    }

    //Lambert72 => translation and transformation to the belgium System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert 72 to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: BD72 => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //param altitude double of the altitude.
    //return an array with the longitude, latitude and altitude respectevely.
    private static double[] lambert72ToWGS84(double northing, double easting, double altitude) {
        double[] result = new double[3];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambert72toWGS84(northing, easting, altitude);
        return result;
    }

    //Lambert72 => translation and transformation to the belgium System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert 72 to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: BD72 => WGS84.
    //param northing double of the northing (Coordinates Y).
    //param easting double of the easting (Coordinates X).
    //return an array with the longitude and latitude respectevely.
    private static double[] lambert72ToWGS84(double northing, double easting) {
        double[] result = new double[2];
        //class LambertConformalConicProjection called
        result = LambertConformalConicProjection.lambert72toWGS84(northing, easting);
        return result;
    }

    /**
     * Convert latitude or longitude in degrees, minutes, seconds into degrees
     * ex: 156°34'44.10''W is converted into -156.5788888 or 01°32'00.20''N into 1.533336666.
     *
     * @param position The position to convert. Must be in the format : 23°45'34.5021''N or 123°09'10.00''W or 002°34'52.61''E
     *                 The first two numbers for the latitude and the first three for the longitude are the degrees.
     *                 The next two are the minutes and the last four are the seconds.
     * @return          The position in decimal degrees, west and south values are negatives.
     * @throws StringIndexOutOfBoundsException May happen if the format is not respected.
     */
    public static double degreesMinSecToDegrees(String position) throws StringIndexOutOfBoundsException {
        double result;
        try {
            if (position.charAt(position.length() - 1) == 'W' || position.charAt(position.length() - 1) == 'E') {
                result = Double.parseDouble(position.substring(7, 12)) / 60;
                result = (Double.parseDouble(position.substring(4, 6)) + result) / 60;
                result += Double.parseDouble(position.substring(0, 3));
            } else {
                result = Double.parseDouble(position.substring(6, 11)) / 60;
                result = (Double.parseDouble(position.substring(3, 5)) + result) / 60;
                result += Double.parseDouble(position.substring(0, 2));
            }
            if (position.charAt(position.length() - 1) == 'W' || position.charAt(position.length() - 1) == 'S')
                return -result;
            else
                return result;
        } catch (StringIndexOutOfBoundsException e) {
            throw new StringIndexOutOfBoundsException("Bad entry format");
        }
    }

    /**
     * Convert latitude or longitude in degrees into a latitude or longitude in the degrees, minutes, seconds format.
     * ex: -156.5788 is converted into 156°34'44.00''W or 1.533336666 into 01°32'00.20''N
     * (west and south values are negatives).
     *
     * @param position The position to convert. Negative values means south or west.
     * @param i        Precise if the value given is a latitude or a longitude (0 means a latitude and 1 a latitude, or use <code>utilities.LATITUDE</code>U or <code>utilities.LONGITUDE</code>U).
     * @return The string returned is in the format: 23°34'23.21''N or 123°09'10.90''W or 002°34'52.61''E.
     *         The first two numbers for the latitude and the first three for the longitude are the degrees.
     *         The next two are the minutes and the last four are the seconds.
     * @throws NumberFormatException Thrown if the argument i is not 1 or 2.
     */
    public static String degreesToDegreesMinSec(double position, int i) throws NumberFormatException {
        String result = "";
        boolean isNegative;
        if (position < 0) isNegative = true;
        else isNegative = false;
        position = Math.abs(position);
        if (i == Utilities.LATITUDE) {
            if (position < 10) result += '0';
            result += Integer.toString((int) Math.floor(position)) + '°';
            position = (position - Math.floor(position)) * 60;
            if (position < 10) result += '0';
            result += Integer.toString((int) Math.floor(position)) + "'";
            position = (position - Math.floor(position)) * 60;
            position = Math.floor(position * 100) / 100;
            if (position < 10) result += '0';
            result += Double.toString(position);
            if (result.length() != 11) result += '0';
            result += "''";
            if (isNegative)
                result += 'S';
            else
                result += 'N';
        } else if (i == Utilities.LONGITUDE) {

            if (position < 10) result += "00";
            else if (position < 100) result += '0';
            result += Integer.toString((int) Math.floor(position)) + '°';
            position = (position - Math.floor(position)) * 60;
            if (position < 10) result += '0';
            result += Integer.toString((int) Math.floor(position)) + "'";
            position = (position - Math.floor(position)) * 60;
            position = Math.floor(position * 100) / 100;
            if (position < 10) result += '0';
            result += Double.toString(position);
            if (result.length() != 12) result += '0';
            result += "''";
            if (isNegative)
                result += 'W';
            else
                result += 'E';
        } else
            throw new NumberFormatException("Bad argument i");
        return result;
    }


    /**
     * Computes the distance between two points on Earth using the haversine formula
     * found at http://www.cs.nyu.edu/visual/home/proj/tiger/gisfaq.html.
     *
     * @param lat1 latitude of the first point in decimal degrees.
     * @param lon1 longitude of the first point in decimal degrees.
     * @param lat2 latitude of the second point in decimal degrees.
     * @param lon2 longitude of the second point in decimal degrees.
     * @return The distance, in meters, between the two points, or <code>NEGATIVE_INFINITY</code> if an
     *         error occurs.
     */
    public static double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        try {
            double lat1Rad = Math.toRadians(lat1);
            double lon1Rad = Math.toRadians(lon1);
            double lat2Rad = Math.toRadians(lat2);
            double lon2Rad = Math.toRadians(lon2);
            double dlat = lat2Rad - lat1Rad;
            double dlon = lon2Rad - lon1Rad;
            double a = Math.sin(dlat / 2.0) * Math.sin(dlat / 2.0) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dlon / 2.0) * Math.sin(dlon / 2.0);
            double c = 2.0 * Float11.asin(Math.min(1.0, Math.sqrt(a)));
            return 6367000.0 * c; // 6367000 = radius of Earth at 45° of latitude
        }
        catch (Exception e) {
            return Double.NEGATIVE_INFINITY;
        }
    }

    /**
     * Convert kilometers into statute miles (or kilometers/hour into miles/hour).
     *
     * @param kilometers The value to convert.
     * @return in statute miles.
     */
    public static float kilometersToMiles(int kilometers) {
        return (float) (kilometers * KMToMiles);
    }

    /**
     * Convert kilometers into statute miles (or kilometers/hour into miles/hour).
     *
     * @param kilometers The value to convert.
     * @return in statute miles.
     */
    public static float kilometersToMiles(float kilometers) {
        return (float) (kilometers * KMToMiles);
    }

    /**
     * Convert kilometers into statute miles (or kilometers/hour into miles/hour).
     *
     * @param kilometers The value to convert.
     * @return in statute miles
     */
    public static long kilometersToMiles(long kilometers) {
        return (long) (kilometers * KMToMiles);
    }

    /**
     * Convert kilometers into statute miles (or kilometers/hour into miles/hour).
     *
     * @param kilometers The value to convert.
     * @return in statute miles
     */
    public static double kilometersToMiles(double kilometers) {
        return kilometers * KMToMiles;
    }


    /**
     * Convert statute miles into kilometers (or miles/hour into kilometers/hour).
     *
     * @param miles The value to convert.
     * @return in kilometers.
     */
    public static float milesToKilometers(int miles) {
        return (float) (miles / KMToMiles);
    }

    /**
     * Convert statute miles into kilometers (or miles/hour into kilometers/hour).
     *
     * @param miles The value to convert.
     * @return in kilometers.
     */
    public static float milesToKilometers(float miles) {
        return (float) (miles / KMToMiles);
    }

    /**
     * Convert statute miles into kilometers (or miles/hour into kilometers/hour).
     *
     * @param miles The value to convert.
     * @return in kilometers.
     */
    public static long milesToKilometers(long miles) {
        return (long) (miles / KMToMiles);
    }

    /**
     * Convert statute miles into kilometers (or miles/hour into kilometers/hour).
     *
     * @param miles miles The value to convert.
     * @return in kilometers.
     */
    public static double milesToKilometers(double miles) {
        return miles / KMToMiles;
    }


    /**
     * Convert meters/second into kilometers/hour.
     *
     * @param ms The value to convert.
     * @return in kilometers/hour.
     */
    public static float msToKmh(int ms) {
        return (float) (ms * 3.6);
    }

    /**
     * Convert meters/second into kilometers/hour.
     *
     * @param ms The value to convert.
     * @return in kilometers/hour.
     */
    public static float msToKmh(float ms) {
        return (float) (ms * 3.6);
    }

    /**
     * Convert meters/second into kilometers/hour.
     *
     * @param ms The value to convert.
     * @return in kilometers/hour.
     */
    public static long msToKmh(long ms) {
        return (long) (ms * 3.6);
    }

    /**
     * Convert meters/second into kilometers/hour.
     *
     * @param ms The value to convert.
     * @return in kilometers/hour.
     */
    public static double msToKmh(double ms) {
        return ms * 3.6;
    }


    /**
     * Convert kilometers/hour into meters/second.
     *
     * @param kmh The value to convert.
     * @return in meters/second.
     */
    public static float kmhToMs(int kmh) {
        return (float) (kmh / 3.6);
    }

    /**
     * Convert kilometers/hour into meters/second.
     *
     * @param kmh The value to convert.
     * @return in meters/second.
     */
    public float kmhToMs(float kmh) {
        return (float) (kmh / 3.6);
    }

    /**
     * Convert kilometers/hour into meters/second.
     *
     * @param kmh The value to convert.
     * @return in meters/second.
     */
    public static long kmhToMs(long kmh) {
        return (long) (kmh / 3.6);
    }

    /**
     * Convert kilometers/hour into meters/second.
     *
     * @param kmh The value to convert.
     * @return in meters/second.
     */
    public static double kmhToMs(double kmh) {
        return kmh / 3.6;
    }


    /**
     * Convert meters into yard.
     *
     * @param meters The value to convert.
     * @return in yards.
     */
    public static float metersToYards(int meters) {
        return (float) (meters * metersToFeet / yardsToFeet);
    }

    /**
     * Convert meters into yards.
     *
     * @param meters The value to convert.
     * @return in yards.
     */
    public static float metersToYards(float meters) {
        return (float) (meters * metersToFeet / yardsToFeet);
    }

    /**
     * Convert meters into yards.
     *
     * @param meters The value to convert.
     * @return in yards.
     */
    public static long metersToYards(long meters) {
        return (long) (meters * metersToFeet / yardsToFeet);
    }

    /**
     * Convert meters into yards.
     *
     * @param meters The value to convert.
     * @return in yards.
     */
    public static double metersToYards(double meters) {
        return meters * metersToFeet / yardsToFeet;
    }


    /**
     * Convert feet into meters.
     *
     * @param yards The value to convert.
     * @return in meters.
     */
    public static float yardsToMeters(int yards) {
        return (float) (yards * yardsToFeet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param yards The value to convert.
     * @return in meters.
     */
    public static float yardsToMeters(float yards) {
        return (float) (yards * yardsToFeet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param yards The value to convert.
     * @return in meters.
     */
    public static long yardsToMeters(long yards) {
        return (long) (yards * yardsToFeet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param yards The value to convert.
     * @return in meters.
     */
    public static double yardsToMeters(double yards) {
        return yards * yardsToFeet / metersToFeet;
    }


    /**
     * Convert meters into feet.
     *
     * @param meters The value to convert.
     * @return in feet.
     */
    public static float metersToFeet(int meters) {
        return (float) (meters * metersToFeet);
    }

    /**
     * Convert meters into feet.
     *
     * @param meters The value to convert.
     * @return in feet.
     */
    public static float metersToFeet(float meters) {
        return (float) (meters * metersToFeet);
    }

    /**
     * Convert meters into feet.
     *
     * @param meters The value to convert.
     * @return in feet.
     */
    public static long metersToFeet(long meters) {
        return (long) (meters * metersToFeet);
    }

    /**
     * Convert meters into feet.
     *
     * @param meters The value to convert.
     * @return in feet.
     */
    public static double metersToFeet(double meters) {
        return meters * metersToFeet;
    }


    /**
     * Convert feet into meters.
     *
     * @param feet The value to convert.
     * @return in meters.
     */
    public static float feetToMeters(int feet) {
        return (float) (feet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param feet The value to convert.
     * @return in meters.
     */
    public static float feetToMeters(float feet) {
        return (float) (feet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param feet The value to convert.
     * @return in meters.
     */
    public static long feetToMeters(long feet) {
        return (long) (feet / metersToFeet);
    }

    /**
     * Convert feet into meters.
     *
     * @param feet The value to convert.
     * @return in meters.
     */
    public static double feetToMeters(double feet) {
        return feet / metersToFeet;
    }


    /**
     * Convert international nautical miles into kilometers.
     *
     * @param knots The value to convert.
     * @return in kilometers.
     */
    public static float knotsToKm(int knots) {
        return (float) (knots * KnotsToKm);
    }

    /**
     * Convert international nautical miles into kilometers.
     *
     * @param knots The value to convert.
     * @return in kilometers.
     */
    public static float knotsToKm(float knots) {
        return (float) (knots * KnotsToKm);
    }

    /**
     * Convert international nautical miles into kilometers.
     *
     * @param knots The value to convert.
     * @return in kilometers.
     */
    public static long knotsToKm(long knots) {
        return (long) (knots * KnotsToKm);
    }

    /**
     * Convert international nautical miles into kilometers.
     *
     * @param knots The value to convert.
     * @return in kilometers.
     */
    public static double knotsToKm(double knots) {
        return (knots * KnotsToKm);
    }


    /**
     * Convert kilometers into international nautical miles .
     *
     * @param km The value to convert.
     * @return in international nautical miles.
     */
    public static float kmToKnots(int km) {
        return (float) (km / KnotsToKm);
    }

    /**
     * Convert kilometers into international nautical miles .
     *
     * @param km The value to convert.
     * @return in international nautical miles.
     */
    public static float kmToKnots(float km) {
        return (float) (km / KnotsToKm);
    }

    /**
     * Convert kilometers into international nautical miles .
     *
     * @param km The value to convert.
     * @return in international nautical miles.
     */
    public static long kmToKnots(long km) {
        return (long) (km / KnotsToKm);
    }

    /**
     * Convert kilometers into international nautical miles .
     *
     * @param km The value to convert.
     * @return in international nautical miles.
     */
    public static double kmToKnots(double km) {
        return km / KnotsToKm;
    }

    /**
     * Convert statute miles into international nautical miles .
     *
     * @param miles The value to convert.
     * @return in international nautical miles.
     */
    public static float milesToKnots(int miles) {
        return (float) (miles / KnotsToMiles);
    }

    /**
     * Convert statute miles into international nautical miles .
     *
     * @param miles The value to convert.
     * @return in international nautical miles.
     */
    public static float milesToKnots(float miles) {
        return (float) (miles / KnotsToMiles);
    }

    /**
     * Convert statute miles into international nautical miles .
     *
     * @param miles The value to convert.
     * @return in international nautical miles.
     */
    public static long milesToKnots(long miles) {
        return (long) (miles / KnotsToMiles);
    }

    /**
     * Convert statute miles into international nautical miles .
     *
     * @param miles The value to convert.
     * @return in international nautical miles.
     */
    public static double milesToKnots(double miles) {
        return miles / KnotsToMiles;
    }


    /**
     * Convert international nautical miles into statute miles .
     *
     * @param knots The value to convert.
     * @return in statute miles.
     */
    public static float knotsToMiles(int knots) {
        return (float) (knots * KnotsToMiles);
    }

    /**
     * Convert international nautical miles into statute miles .
     *
     * @param knots The value to convert.
     * @return in statute miles.
     */
    public static float knotsToMiles(float knots) {
        return (float) (knots * KnotsToMiles);
    }

    /**
     * Convert international nautical miles into statute miles .
     *
     * @param knots The value to convert.
     * @return in statute miles.
     */
    public static long knotsToMiles(long knots) {
        return (long) (knots * KnotsToMiles);
    }

    /**
     * Convert international nautical miles into statute miles .
     *
     * @param knots The value to convert.
     * @return in statute miles.
     */
    public static double knotsToMiles(double knots) {
        return knots * KnotsToMiles;
    }
}