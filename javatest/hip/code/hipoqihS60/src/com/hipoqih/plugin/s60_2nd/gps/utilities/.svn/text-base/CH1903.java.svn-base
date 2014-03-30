package com.hipoqih.plugin.s60_2nd.gps.utilities;

 class CH1903 {

    //Convert coordinates in WGS84 to CH1903
    //param latitude in decimal degrees
    //param longitude in decimal degrees
    //param altitude in meters (ellipsoidal height, not above sea level. If you have already altitude
    //above sea level, it's not necessary to transform it, as the difference with the result of the formula
    //is about one meter.
    //return an array of double wich contains the Y coordinate at position 0, the X coordinate at position 1
    //and the altitude at position 2
    //License: This library is under the GNU Lesser General Public License
    //@author Praplan Christophe
    //author Velen Stephane
    //version 1.0 <p>Geneva,the 23.03.2006
    public static double[] WGS84ToCH1903(double latitude, double longitude, double altitude) {
        double[] result = new double[3];
        double longitude2 = (longitude * 3600 - 26782.5) / 10000;
        double latitude2 = (latitude * 3600 - 169028.66) / 10000;

        //the vertical position
        result[0] = 200147.07
                + 308807.95 * latitude2
                + 3745.25 * longitude2 * longitude2
                + 76.63 * latitude2 * latitude2
                - 194.56 * longitude2 *longitude2 * latitude2
                + 119.79 * latitude2 * latitude2 * latitude2;

        //the horizontal position
        result[1] = 600072.37
                + 211455.93 * longitude2
                - 10938.51 * longitude2 * latitude2
                - 0.36 * longitude2 * latitude2 * latitude2
                - 44.54 * longitude2 * longitude2 * longitude2;

        //the altitude
        result[2] = altitude - 49.55
                + 2.73 * longitude2
                + 6.94 * latitude2;

        return result;
    }


    //Convert coordinates in WGS84 to CH1903
    //param latitude in decimal degrees
    //param longitude in decimal degrees
    //return an array of double wich contains the Y coordinate at position 0 and the X coordinate at position 1
    public static double[] WGS84ToCH1903(double latitude, double longitude) {
        double[] result = new double[2];
        double[] temp = new double[3];
        temp=WGS84ToCH1903(latitude, longitude,0);
        result[0]=temp[0];
        result[1]=temp[1];
        return result;

    }

    //Convert coordinates in CH1903 to WGS84
    //param y vertical values in meters
    //param x horizontal value in meters
    //param altitude in meters (give ellipsoidal height, not above sea level. If you want the altitude
    //above sea level, it's not necessary to transform it, as the difference with the result of the formula
    //is about one meter.)
    //return an array of double wich contains the latitude at position 0, the longitude at position 1
    //and the altitude at position 2
    public static double[] CH1903ToWGS84(double y, double x, double altitude) {
        double[] result = new double[3];
        double y2 = (y - 200000) / 1000000;

        double x2 = (x - 600000) / 1000000;

        result[0] = (16.9023892
                + 3.238272 * y2
                - 0.270978 * x2 * x2
                - 0.002528 * y2 * y2
                - 0.0447 * x2 * x2 * y2
                - 0.014 * y2 * y2 * y2)
               * 100 / 36;


        result[1] = (2.6779094
                + 4.728982 * x2
                + 0.791484 * x2 * y2
                + 0.1306 * x2 * y2 * y2
                - 0.0436 * x2 * x2 * x2)
                * 100 / 36;

        result[2] = altitude + 49.55
                - 12.6 * x2
                - 22.64 * y2;
        return result;
    }

   //Convert coordinates in CH1903 to WGS84
   //param y vertical values in meters
   //param x horizontal value in meters
   //return an array of double wich contains the latitude at position 0 and the longitude at position 1.
    public static double[] CH1903ToWGS84(double y, double x) {
         double[] result = new double[2];
        double[] temp = new double[3];
        temp=CH1903ToWGS84(y, x,0);
        result[0]=temp[0];
        result[1]=temp[1];
        return result;
    }
}
