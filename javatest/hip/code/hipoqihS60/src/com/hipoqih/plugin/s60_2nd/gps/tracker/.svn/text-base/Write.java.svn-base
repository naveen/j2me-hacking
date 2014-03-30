package com.hipoqih.plugin.s60_2nd.gps.tracker;


/*
 * API for gps
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */

import com.hipoqih.plugin.s60_2nd.gps.Debug;

import java.io.*;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.io.file.*;

//class used to write the coordinates in a file.
class Write implements Runnable {
    private String filePath;
    private static FileConnection fc = null;
    private static PrintStream dos = null;
    private Vector coordVector = new Vector();
    private FileIOListener listener;



    /*
     * Constructor
     *
     * @param filePath The complete path of the file in wich we want to write the coordinates.
     */
    Write(String filePath, FileIOListener listener) {
        this.listener = listener;
        this.filePath = filePath;
    }


    /*
     * Save a vector of coordinates into a file.
     *
     * @param coord The vector of coordinates.
     */
    void save(Vector coord) {
        coordVector = coord;
        Thread t = new Thread(this);
        t.start();
    }


    //the methode wich writes the data into the file
    private void saveData() throws IOException {
        try {
            // create the file if it doesn't exist.
            Debug.setDebug("tracker saving in " + filePath, Debug.NORMAL);
            fc = (FileConnection) Connector.open("file://" + filePath, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
                Debug.setDebug("file created", Debug.DETAIL);
            } else {
                // if the file exist, it is deleted and then a new file is created
                Debug.setDebug(filePath + " overwritten", Debug.NORMAL);
                fc.delete();
                fc.create();
            }
            dos = new PrintStream(fc.openOutputStream());
            //write the header of the file
            dos.print("site,long,lat,time,altitude\r\n");

            //for each coordinate in the vector, it writes it in the file

            for (int i = 0; i <= (coordVector.size() - 1); i++) {
                dos.print(coordVector.elementAt(i).toString());
                //the end of line character
                dos.print("\r\n");
            }
        } catch (Exception e) {
            throw new IOException("Error, can't record in "+filePath);

        } finally {

            if (dos != null) {
                dos.flush();
                dos.close();
            }
            if (fc != null) fc.close();
            Debug.setDebug("File has finished to be saved", Debug.NORMAL);
            //when the save is succesfuly done, the listener is called and STATE_OK is returned
            listener.fileWritten(FileIOListener.STATE_OK);
        }
    }

    public void run() {
        try {
            saveData();
        } catch (IOException ex) {
            //if an error has occured, call the listener and return STATE_PROBLEM
            listener.fileWritten(FileIOListener.STATE_PROBLEM);
        }
    }
}
