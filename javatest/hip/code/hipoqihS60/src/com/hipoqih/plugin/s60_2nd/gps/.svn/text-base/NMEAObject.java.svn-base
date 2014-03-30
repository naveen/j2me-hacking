package com.hipoqih.plugin.s60_2nd.gps;

/**
 * NMEAObject is an array of <code>String</code> where all the data of a NMEA sentence is stored.
 * The order is kept, so the first field of the array is the type of the sentence.
 * The array has a size of 20.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */
 public class NMEAObject {

    private String[] NMEA = new String[20];
    String t = new String();

    /**
     * Constructor
     * Parse the data of a NMEA message into a <code>NMEAObject</code> wich is an array of Strings.
     * Example of sentence : "$GGA,02111.2332,04604.4412,,650.6,,E*D3". Note that the Checksum (*XX) is not necessarely required.
     * @param NMEASentence The NMEA message to parse.
     */
    public NMEAObject(String NMEASentence) {
        NMEA[0] = new String("Empty");

        if (NMEASentence.length() > 2) {
           //the '$' at the start of the message is ignored
            NMEASentence = NMEASentence.substring(1, NMEASentence.length());
            int iTemp = 0;
            String sTemp = new String();
            //while there is data fields in the sentence
            while (NMEASentence.indexOf(',') != -1) {
                //if the data field is empty
                if (NMEASentence.charAt(0) == ',') {
                    sTemp = "";
                    NMEASentence = NMEASentence.substring(1, NMEASentence.length());
                } else {
                    sTemp = NMEASentence.substring(0, NMEASentence.indexOf(','));
                    NMEASentence = NMEASentence.substring(NMEASentence.indexOf(',') + 1, NMEASentence.length());
                }

                NMEA[iTemp] = sTemp;
                iTemp++;
            }
            //if we get the '*' we ignore it and put the checksum at the end of the array
            if (NMEASentence.indexOf('*') != -1) {
                NMEA[iTemp] = NMEASentence.substring(0, NMEASentence.indexOf('*'));
                NMEA[19] = NMEASentence.substring(NMEASentence.indexOf('*') + 1, NMEASentence.length());
                //if there is no checksum
            } else
                NMEA[iTemp] = NMEASentence;
        }
    }

    /**
     * Constructor
     * Create a new, empty NMEAObject.
     */
    public NMEAObject() {

    }


    /**
     * Get the type of the NMEA message, without the "$" before.
     *
     * @return The type of the NMEA message without the "$". Ex: GPGSA (and not $GPGSA).
     */
    public String getType() {
        return NMEA[0];
    }

    /**
     * Set the type of the NMEAObject
     *
     * @param type The type. Ex: GPGSA (and not $GPGSA).
     */
    public void setType(String type) {
        NMEA[0] = type;
    }

    /**
     * Set the data of the NMEAObject.
     *
     * @param data     The data.
     * @param position The position where the data will be inserted.
     */
    public void setData(String data, int position) {

        NMEA[position] = data;
    }

    /**
     * Parse the data of a NMEA message into a NMEAObject wich is an array on String.
     * Do the same thing than the constructor.
     * Example of sentence : $GPGGA,02111.2332,04604.4412,,650.6,,E*D3". Note that the Checksum (*XX) is not necessarely required.
     * @param NMEASentence The sentence to parse.
     */
    public void setData(String NMEASentence) {
        NMEA[0] = new String("Empty");

        if (NMEASentence.length() > 2) {
            //the '$' at the start of the message is ignored
            NMEASentence = NMEASentence.substring(1, NMEASentence.length());
            int iTemp = 0;
            String sTemp = new String();
            //while there is data fields in the sentence
            while (NMEASentence.indexOf(',') != -1) {
               //if the data field is empty
                if (NMEASentence.charAt(0) == ',') {
                    sTemp = "";
                    NMEASentence = NMEASentence.substring(1, NMEASentence.length());
                } else {
                    sTemp = NMEASentence.substring(0, NMEASentence.indexOf(','));
                    NMEASentence = NMEASentence.substring(NMEASentence.indexOf(',') + 1, NMEASentence.length());
                }

                NMEA[iTemp] = sTemp;
                iTemp++;
            }
            //if we get the '*' we ignore it and put the checksum at the end of the array
            if (NMEASentence.indexOf('*') != -1) {
                NMEA[iTemp] = NMEASentence.substring(0, NMEASentence.indexOf('*'));
                NMEA[19] = NMEASentence.substring(NMEASentence.indexOf('*') + 1, NMEASentence.length());
                //if there is no checksum
            } else
                NMEA[iTemp] = NMEASentence;
        }
    }

    /**
     * Construct a NMEA sentence from the data contained in this object that can be send to a gps device.
     * Example of sentence : "$GPGGA,02111.2332,04604.4412,,650.6,,E*D3". Note that the Checksum (*XX) is not necessarely required.
     * @return String The NMEA sentence.
     */
    public String toNMEASentence() {
        StringBuffer result = new StringBuffer("$" + getType());
        //for the entire array
        for (int i = 1; i < NMEA.length-1; i++) {
            //if there is data
            if (NMEA[i]!=null) {
                result.append(",").append(NMEA[i]);
            }
        }
        //append the checksum at the end of the sentence
        result.append('*');
        result.append(getChecksum(result.toString()));
         Debug.setDebug("NMEAObject has build the sentence "+result,Debug.DETAIL);
        return result.toString();
    }


    /**
     * Get a field in a NMEAObject.
     *
     * @param field The index of the field wanted (the array size is 20).
     * @return The data. Return an empy <code>String</code> if there is no data, and <code>null</code> if the field doesn't exist.
     * @throws ArrayIndexOutOfBoundsException is thrown if the param <code>field</code> is not right.
     */
    String getData(int field) throws ArrayIndexOutOfBoundsException {
        String result ="null";
        try {result = NMEA[field];
        } catch (NullPointerException e) {}
        catch (ArrayIndexOutOfBoundsException e) {throw new ArrayIndexOutOfBoundsException("");}
       return result;

    }

    /**
     * Calculate the checksum for a given NMEA sentence.
     * Note that you can enter a sentence with a checksum.
     * @param sentence Example of sentence : "$GPGGA,02111.2332,04604.4412,,650.6,,E*D3".
     * @return The checksum value.
     */
    public static String getChecksum(String sentence) {

        char chara;
        int checksum = 0;
        for (int i = 0; i < sentence.length(); i++) {
            chara = sentence.charAt(i);
            switch (chara) {
                //ignore the '$'
                case '$':
                    break;
                    //if we arrive at the '*' then stop
                case '*':
                    i = sentence.length();
                    break;
                default:
                    if (checksum == 0) {
                        // Yes. Set the checksum to the value
                        checksum = (byte) chara;
                    } else {
                        // No. XOR the checksum with this character's value
                        checksum = checksum ^ (byte) chara;
                    }
                    break;
            }
        }
        // Return the checksum formatted as a two-character hexadecimal
        String result = Integer.toHexString(checksum);
        result = result.toUpperCase();
        if (result.length() == 1) result = '0' + result;
        return result;
    }
}
