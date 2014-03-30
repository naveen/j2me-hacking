package com.hipoqih.plugin.s60_2nd.gps.utilities;

//Class to translate and transform latitude and longitude to Northing and Easting coordinates.
//The translation model is done by the Molodensky Model.
//For the Belgium system, the translation used is called "BD72".
//For the French System, the translation used is called "NTF".
//The transformation of projection used in thos class is the Lambert Conformal Conic projection
//Different projection System :  -lambert72 (Belgium System)
//                               -Lambert I (North part of France)
//                               -Lambert II (Middle part of France)
//                               -Lambert II etendu (all part of france, parameters are the same of Lambert II)
//                               -Lambert III (South part of France)
//                               -Lambert IV  (Corse)
//                               -Lambert 93  (New System in France)
// The reverse formula can be used for each transformation and translation system.
// Northing and Easting coordinates are transforming to latitude and longitude.
// After, the translation is done.
// License: This library is under the GNU Lesser General Public License
// author Praplan Christophe
// author Velen Stephane
// version 1.0 <p>Geneva, the 23.03.2006
class LambertConformalConicProjection {

    private static double firstEllipsoidalSemiMajor;
    private static double firstEllipsoidalSemiMinor;
    private static double secondEllipsoidalSemiMajor;
    private static double secondEllipsoidalSemiMinor;
    private static double firstStandardParallel;
    private static double secondStandardParallel;
    private static double latitudeFalseOrigin;
    private static double longitudeFalseOrigin;
    private static double eastingFalseOrigin;
    private static double northingFalseOrigin;
    private static double latitude;
    private static double longitude;
    private static double easting;
    private static double northing;
    private static double height;
    private static double deltaX;
    private static double deltaY;
    private static double deltaZ;
    private static double deltaA;
    private static double deltaF;

    // BD72 => translation to the belgium System
    // Method to translate latitude and longitude of the WGS84 system to the belgium System (BD72).
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // return an array with the new longitude and latitude respectevely.
    public static double[] WGS84toBD72(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toBD72(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // BD72 => translation to the belgium System
    // Method to translate latitude, longitude and altitude of the WGS84 system to the belgium System (BD72).
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // param h : double of the height.
    // return an array with the new longitude, latitude and altitude respectevely.
    public static double[] WGS84toBD72(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param  used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378388;
        secondEllipsoidalSemiMinor = 6356911.946128;
        firstStandardParallel = Math.toRadians(51.166666667);
        secondStandardParallel = Math.toRadians(49.83333333);
        eastingFalseOrigin = 150000.0;
        northingFalseOrigin = 165373;
        latitudeFalseOrigin = Math.toRadians(50.79936);
        longitudeFalseOrigin = Math.toRadians(4.36749);
        //Param used only for the translation
        deltaX = Math.toRadians(-125.8);
        deltaY = Math.toRadians(79.9);
        deltaZ = Math.toRadians(-100.5);
        deltaA = Math.toRadians(-251);
        deltaF = Math.toRadians(-0.000014192702);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // BD72 => translation to the belgium System (Reverse)
    // Reverse method to translate latitude and longitude of the belgium system (BD72) to the WGS84 system.
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // return an array with the new longitude and latitude respectevely.
    public static double[] BD72toWGS84(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = BD72toWGS84(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // BD72 => translation to the belgium System (Reverse)
    // Reverse method to translate latitude, longitude and altitude of the belgium system (BD72) to the WGS84 system.
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // param h : double of the height.
    // return an array with the new longitude, latitude and altitude respectevely.
    public static double[] BD72toWGS84(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378388;
        secondEllipsoidalSemiMinor = 6356911.946128;
        firstStandardParallel = Math.toRadians(51.166666667);
        secondStandardParallel = Math.toRadians(49.83333333);
        eastingFalseOrigin = 150000.0;
        northingFalseOrigin = 165373;
        latitudeFalseOrigin = Math.toRadians(50.79936);
        longitudeFalseOrigin = Math.toRadians(4.36749);
        //Param used only for the translation
        deltaX = Math.toRadians(+125.8);
        deltaY = Math.toRadians(-79.9);
        deltaZ = Math.toRadians(+100.5);
        deltaA = Math.toRadians(+251);
        deltaF = Math.toRadians(+0.000014192702);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // Lambert72 => translation and transformation to the belgium System
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert 72
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambert72(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambert72(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // Lambert72 => translation and transformation to the belgium System
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert 72
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambert72(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378388;
        secondEllipsoidalSemiMinor = 6356911.946128;
        firstStandardParallel = Math.toRadians(51.166666667);
        secondStandardParallel = Math.toRadians(49.83333333);
        eastingFalseOrigin = 150000.0;
        northingFalseOrigin = 165373;
        latitudeFalseOrigin = Math.toRadians(50.79936);
        longitudeFalseOrigin = Math.toRadians(4.36749);
        //Param used only for the translation
        deltaX = Math.toRadians(-125.8);
        deltaY = Math.toRadians(79.9);
        deltaZ = Math.toRadians(-100.5);
        deltaA = Math.toRadians(-251);
        deltaF = Math.toRadians(-0.000014192702);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // Lambert72 => translation and transformation to the belgium System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert 72 to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: BD72 => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambert72toWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambert72toWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // Lambert72 => translation and transformation to the belgium System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert 72 to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: BD72 => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambert72toWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378388;
        secondEllipsoidalSemiMinor = 6356911.946128;
        firstStandardParallel = Math.toRadians(51.166666667);
        secondStandardParallel = Math.toRadians(49.83333333);
        eastingFalseOrigin = 150000.0;
        northingFalseOrigin = 165373;
        latitudeFalseOrigin = Math.toRadians(50.79936);
        longitudeFalseOrigin = Math.toRadians(4.36749);
        //Param used only for the translation
        deltaX = Math.toRadians(+125.8);
        deltaY = Math.toRadians(-79.9);
        deltaZ = Math.toRadians(+100.5);
        deltaA = Math.toRadians(+251);
        deltaF = Math.toRadians(+0.000014192702);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // NTF => translation to the French System
    // Method to translate latitude and longitude of the WGS84 system to the French System (NTF).
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // return an array with the new longitude and latitude respectevely.
    public static double[] WGS84toNTF(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toNTF(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // NTF => translation to the French System
    // Method to translate latitude, longitude and altitude of the WGS84 system to the French System (NTF).
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // param h : double of the height.
    // return an array with the new longitude, latitude and altitude respectevely
    public static double[] WGS84toNTF(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param  used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(48.59852);
        secondStandardParallel = Math.toRadians(50.39591);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 1200000;
        latitudeFalseOrigin = Math.toRadians(49.5);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // NTF => translation to the French System (Reverse)
    // Reverse method to translate latitude and longitude of the French system (NTF) to the WGS84 system.
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // return an array with the new longitude and latitude respectevely.
    public static double[] NTFtoWGS84(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = NTFtoWGS84(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // NTF => translation to the French System (Reverse)
    // Reverse method to translate latitude, longitude and altitude of the French system (NTF) to the WGS84 system.
    // Only the translation in the new system is done, not a transformation.
    // param lat : double of the latitude.
    // param lon : double of the longitude.
    // param h : double of the height.
    // return an array with the new longitude, latitude and altitude respectevely.
    public static double[] NTFtoWGS84(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(48.59852);
        secondStandardParallel = Math.toRadians(50.39591);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 1200000;
        latitudeFalseOrigin = Math.toRadians(49.5);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // LambertI => translation and transformation to the French System (North part)
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert I
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambertI(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambertI(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertI => translation and transformation to the French System (North part)
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert I
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambertI(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(48.59852);
        secondStandardParallel = Math.toRadians(50.39591);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 1200000;
        latitudeFalseOrigin = Math.toRadians(49.5);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // LambertI => translation and transformation to the French System (North part) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert I to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambertItoWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambertItoWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertI => translation and transformation to the French System (North part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert I to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambertItoWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(48.59852);
        secondStandardParallel = Math.toRadians(50.39591);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 1200000;
        latitudeFalseOrigin = Math.toRadians(49.5);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // LambertII => translation and transformation to the French System (center part)
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert II
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambertII(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambertII(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertII => translation and transformation to the French System (center part)
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert II
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambertII(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(45.89892);
        secondStandardParallel = Math.toRadians(47.69601);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 2200000;
        latitudeFalseOrigin = Math.toRadians(46.8);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // LambertII => translation and transformation to the French System (center part) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert II to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambertIItoWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambertIItoWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertII => translation and transformation to the French System (center part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert II to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambertIItoWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(45.89892);
        secondStandardParallel = Math.toRadians(47.69601);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 2200000;
        latitudeFalseOrigin = Math.toRadians(46.8);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // LambertII etendu=> translation and transformation to the French System
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert II etendu
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambertIIEtendu(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambertIIEtendu(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertII etendu=> translation and transformation to the French System
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert II etendu
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambertIIEtendu(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(45.89892);
        secondStandardParallel = Math.toRadians(47.69601);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 2200000;
        latitudeFalseOrigin = Math.toRadians(46.8);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // LambertII etendu=> translation and transformation to the French System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert II etendu to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambertIIEtendutoWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambertIIEtendutoWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertII etendu=> translation and transformation to the French System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert II etendu to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambertIIEtendutoWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(45.89892);
        secondStandardParallel = Math.toRadians(47.69601);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 2200000;
        latitudeFalseOrigin = Math.toRadians(46.8);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // LambertIII => translation and transformation to the French System (south part)
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert III
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambertIII(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambertIII(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertIII => translation and transformation to the French System (south part)
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert III
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambertIII(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(43.19929);
        secondStandardParallel = Math.toRadians(44.99609);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 3200000;
        latitudeFalseOrigin = Math.toRadians(44.1);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // LambertIII => translation and transformation to the French System (south part) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert III to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambertIIItoWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambertIIItoWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertIII => translation and transformation to the French System (south part) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert III to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambertIIItoWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(43.19929);
        secondStandardParallel = Math.toRadians(44.99609);
        eastingFalseOrigin = 600000;
        northingFalseOrigin = 3200000;
        latitudeFalseOrigin = Math.toRadians(44.1);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // LambertIV => translation and transformation to the French System (Corse)
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert IV
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude (Coordinates Y).
    //param lon : double of the longitude (Coordinates X).
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambertIV(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambertIV(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertIV => translation and transformation to the French System (Corse)
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert IV
    //In the first time, the translation is done: WGS84 => NTF.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambertIV(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(41.56039);
        secondStandardParallel = Math.toRadians(42.76766);
        eastingFalseOrigin = 234.358;
        northingFalseOrigin = 4185861.369;
        latitudeFalseOrigin = Math.toRadians(42.165);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(-168);
        deltaY = Math.toRadians(-60);
        deltaZ = Math.toRadians(+320);
        deltaA = Math.toRadians(-112.14);
        deltaF = Math.toRadians(-0.000054750714);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // LambertIV => translation and transformation to the French System (Corse) (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert IV to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting  (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambertIVtoWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambertIVtoWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // LambertIV => translation and transformation to the French System (Corse) (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert IV to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambertIVtoWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378249.2;
        secondEllipsoidalSemiMinor = 6356515;
        firstStandardParallel = Math.toRadians(41.56039);
        secondStandardParallel = Math.toRadians(42.76766);
        eastingFalseOrigin = 234.358;
        northingFalseOrigin = 4185861.369;
        latitudeFalseOrigin = Math.toRadians(42.165);
        longitudeFalseOrigin = Math.toRadians(2.33723);
        //Param used only for the translation
        deltaX = Math.toRadians(+168);
        deltaY = Math.toRadians(+60);
        deltaZ = Math.toRadians(-320);
        deltaA = Math.toRadians(+112.14);
        deltaF = Math.toRadians(+0.000054750714);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    // Lambert93 => translation and transformation to the French System
    // Method to convert latitude and longitude of the WGS84 system to the projection system of Lambert 93
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param lat : double of the latitude
    //param lon : double of the longitude
    //return an array with the northing and easting respectevely
    public static double[] WGS84toLambert93(double lat, double lon) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = WGS84toLambert93(lat, lon, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // Lambert93 => translation and transformation to the French System
    // Method to convert latitude, longitude and altitude of the WGS84 system to the projection system of Lambert 93
    //In the first time, the translation is done: WGS84 => BD72.
    //After that, the transformation is done.
    //param lat : double of the latitude.
    //param lon : double of the longitude.
    //param h : double of the height.
    //return an array with the northing, easting and height respectevely.
    public static double[] WGS84toLambert93(double lat, double lon, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378137;
        secondEllipsoidalSemiMinor = 6356752.31414;
        firstStandardParallel = Math.toRadians(44);
        secondStandardParallel = Math.toRadians(49);
        eastingFalseOrigin = 700000;
        northingFalseOrigin = 6600000;
        latitudeFalseOrigin = Math.toRadians(46.5);
        longitudeFalseOrigin = Math.toRadians(3);
        //Param used only for the translation
        deltaX = Math.toRadians(0);
        deltaY = Math.toRadians(0);
        deltaZ = Math.toRadians(0);
        deltaA = Math.toRadians(0);
        deltaF = Math.toRadians(-0.000000000016);
        //Param received
        latitude = Math.toRadians(lat);
        longitude = Math.toRadians(lon);
        height = h;
        //Method of the translation
        Molodensky();
        //Method of the transformation
        result[0] = Northing();
        result[1] = Easting();
        result[2] = height;
        return result;
    }

    // Lambert93 => translation and transformation to the French System (Reverse)
    //Reverse method to convert northing and easting coordinates of the projection system of Lambert 93 to the WGS84 system.
    //In the first time, the transformation is done.
    //After that, the translation is done: NTF => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //return an array with the latitude and longitude respectevely
    public static double[] lambert93toWGS84(double north, double east) {
        //temp array for the second method
        double[]temp1 = new double[3];
        //temp array to keep only latitude and longitude without altitude
        double[]temp2 = new double[2];
        //array returned by this method
        double[] result = new double[2];
        //Second method is called with a null altitude
        temp1 = lambert93toWGS84(north, east, 0);
        temp2[0] = temp1[0];
        temp2[1] = temp1[1];
        result = temp2;
        return result;
    }

    // Lambert93 => translation and transformation to the French System (Reverse)
    //Reverse method to convert northing, easting and altitude coordinates of the projection system of Lambert 93 to the WGS84 system.
    //In the first, the transformation is done.
    //After that, the translation is done: BD72 => WGS84.
    //param lat : double of the northing (Coordinates Y).
    //param lon : double of the easting (Coordinates X).
    //param h : double of the height
    //return an array with the latitude, longitude and height respectevely
    public static double[] lambert93toWGS84(double north, double east, double h) {
        double[] result = new double[3];
        //Param used for the translation and the transformation
        firstEllipsoidalSemiMajor = 6378137;
        firstEllipsoidalSemiMinor = 6356752.31414;
        secondEllipsoidalSemiMajor = 6378137;
        secondEllipsoidalSemiMinor = 6356752.31414;
        firstStandardParallel = Math.toRadians(44);
        secondStandardParallel = Math.toRadians(49);
        eastingFalseOrigin = 700000;
        northingFalseOrigin = 6600000;
        latitudeFalseOrigin = Math.toRadians(46.5);
        longitudeFalseOrigin = Math.toRadians(3);
        //Param used only for the translation
        deltaX = Math.toRadians(0);
        deltaY = Math.toRadians(0);
        deltaZ = Math.toRadians(0);
        deltaA = Math.toRadians(0);
        deltaF = Math.toRadians(+0.000000000016);
        //Param received
        northing = north;
        easting = east;
        height = h;
        //Method of the transformation
        latitude = latitude();
        longitude = longitude();
        //Method of the translation
        Molodensky();
        latitude = Math.toDegrees(latitude);
        longitude = Math.toDegrees(longitude);
        result[0] = latitude;
        result[1] = longitude;
        result[2] = height;
        return result;
    }

    //Method to Molodensky
    private static double M() {
        return (firstEllipsoidalSemiMajor * (1 - e2())) / Math.sqrt(Float11.pow(1 - e2() * Float11.pow(Math.sin(latitude), 2), 3));
    }

    // Method to Molodensky
    private static double N() {
        return firstEllipsoidalSemiMajor / Math.sqrt(1 - e2() * Float11.pow(Math.sin(latitude), 2));
    }

    //Method to do the translation between two different System.
    //This algorithyme doesnt need to change the coordinate system to the cartesien system to do the translation
    public static void Molodensky() {
        //delta Latitude
        double deltaLatitude = ((-1) * Math.sin(latitude) * Math.cos(longitude) * deltaX
                - Math.sin(latitude) * Math.sin(longitude) * deltaY
                + Math.cos(latitude) * deltaZ
                + (N() * e2() * Math.sin(latitude) * Math.cos(latitude)) / firstEllipsoidalSemiMajor * deltaA
                + (M() * (firstEllipsoidalSemiMajor / firstEllipsoidalSemiMinor) + N() * (firstEllipsoidalSemiMinor / firstEllipsoidalSemiMajor)) * deltaF * Math.sin(latitude) * Math.cos(latitude))
                / M();
        //delta longitude
        double deltaLongitude = ((-1) * Math.sin(longitude) * deltaX
                + Math.cos(longitude) * deltaY) / (N() * Math.cos(latitude));
        //delta altitude
        double deltaHeight = Math.cos(latitude) * Math.cos(longitude) * deltaX
                + Math.cos(latitude) * Math.sin(longitude) * deltaY
                + Math.sin(latitude) * deltaZ
                - (firstEllipsoidalSemiMajor / N()) * deltaA
                + (firstEllipsoidalSemiMinor / firstEllipsoidalSemiMajor) * deltaF * N() * Float11.pow(Math.sin(latitude), 2);
        //translation
        latitude -= Math.toDegrees(deltaLatitude);
        longitude -= Math.toDegrees(deltaLongitude);
        height -= deltaHeight;
    }

    //Method to calcul the flattening of the ellipsoid
    private static double fInverse() {
        return secondEllipsoidalSemiMajor / (secondEllipsoidalSemiMajor - secondEllipsoidalSemiMinor);
    }

    //Method to calcul the eccentricity of the ellipsoid
    private static double e2() {
        return 2 * (1 / fInverse()) - Float11.pow(1 / fInverse(), 2);
    }

    // Method use to calcul Northing and Easting
    private static double e() {
        return Math.sqrt(e2());
    }

    // Method use to calcul Northing and Easting
    private static double m(double x, double y) {
        return Math.cos(x) / Float11.pow(1 - (y * Float11.pow(Math.sin(x), 2)), 0.5);
    }

    // Method use to calcul Northing and Easting
    private static double t(double x, double y) {
        return Math.tan(0.25 * Math.PI - 0.5 * x) / Float11.pow((1 - y * Math.sin(x)) / (1 + y * Math.sin(x)), 0.5 * y);
    }

    // Method use to calcul Northing and Easting
    private static double n() {
        return (Float11.log(m(firstStandardParallel, e2())) - Float11.log(m(secondStandardParallel, e2()))) / (Float11.log(t(firstStandardParallel, e())) - Float11.log(t(secondStandardParallel, e())));
    }

    // Method use to calcul Northing and Easting
    private static double f() {
        return m(firstStandardParallel, e2()) / (n() * Float11.pow(t(firstStandardParallel, e()), n()));
    }

    // Method use to calcul Northing and Easting
    private static double r(double x) {
        return secondEllipsoidalSemiMajor * f() * Float11.pow(t(x, e()), n());

    }

    // Method use to calcul Northing and Easting
    private static double phi() {
        return n() * (longitude - longitudeFalseOrigin);
    }

    //Method to calcul the Easting of the lambert projection
    private static double Easting() {
        return eastingFalseOrigin + r(latitude) * Math.sin(phi());
    }

    //Method to calcul the Northing of the lambert projection
    private static double Northing() {
        return northingFalseOrigin + r(latitudeFalseOrigin) - r(latitude) * Math.cos(phi());
    }

    //Reverse method to calcul the latitude and longitude
    private static double rInverse() {
        if (n() >= 0)
            return Math.sqrt(Float11.pow((easting - eastingFalseOrigin), 2) + Float11.pow(r(latitudeFalseOrigin) - (northing - northingFalseOrigin), 2));
        else
            return (-1) * Math.sqrt(Float11.pow((easting - eastingFalseOrigin), 2) + Float11.pow(r(latitudeFalseOrigin) - (northing - northingFalseOrigin), 2));
    }

    //Reverse method to calcul the latitude and longitude
    private static double tInverse() {
        return Float11.pow((rInverse() / (secondEllipsoidalSemiMajor * f())), 1 / n());
    }

    //Reverse method to calcul the latitude and longitude
    private static double phiInverse() {
        return Float11.atan((easting - eastingFalseOrigin) / (r(latitudeFalseOrigin) - (northing - northingFalseOrigin)));
    }

    //Reverse method to have the latitude
    //return double latitude
    private static double latitude() {
        double ecart = 1;
        latitude = 0;
        double latitude0 = latitude;
        //Iteration
        while (ecart > 0.0000000001) {
            latitude = latitude0;
            latitude0 = Math.PI / 2 - 2 * Float11.atan(tInverse() * Float11.pow((1 - e() * Math.sin(latitude)) / (1 + e() * Math.sin(latitude)), e() / 2));
            ecart = Math.abs(latitude - latitude0);
        }
        return latitude;
    }

    //Reverse method to have the longitude
    //return double longitude
    private static double longitude() {
        return phiInverse() / n() + longitudeFalseOrigin;
    }
}