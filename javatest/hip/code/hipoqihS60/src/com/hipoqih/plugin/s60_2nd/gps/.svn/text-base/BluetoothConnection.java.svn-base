package com.hipoqih.plugin.s60_2nd.gps;

import javax.microedition.lcdui.List;

/**
 *  Connect the mobile phone to a Bluetooth device
 *
 *   <p><code>Connection to a new device : </code></p>
 *   <ul><li><i>searchDevices</i> method must be called first to do a research of Bluetooth devices.
 *   <li>The programmer must only take and display the list of device with the <i>getDevicesList</i> method.
 *   <li><code>WARNING</code> : in the application of the programmer,
 *   the thread have to be synchronized until the device is choosed.
 *   <li>After the device selection, the <i>connectDevice</i> method must be called with the number of the selection.
 *   </ul><p>
 *   <code>Connection to the last device selected: </code>
 *   <ul>The programmer have to call only the <i>connectLastDevice</i> method
 *   </ul><p>
 *   The programmer can use boolean to know when device is found, when the connection is done, etc...
 *   <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @see MessageListener
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0  <p>Geneva, the 23.03.2006
 */
public class BluetoothConnection {
    private BluetoothConnectionToGPS btConnectionToGPS;

    /*
     * Listen when a NMEA message is received.
     * @param listener  The MessageListener
     */
     void setMessageListener(MessageListener listener) {
         btConnectionToGPS = new BluetoothConnectionToGPS(listener);
    }

    /**
     * Send NMEA message to configure the gps.
     *
     * @param NMEAMessage string
     */
    public void sendNMEAMessage(String NMEAMessage) {
        btConnectionToGPS.sendNMEAMessage(NMEAMessage);
        Debug.setDebug("The following NMEA message has been sent : "+NMEAMessage ,Debug.DETAIL);
    }

    /**
     * Send SIRF message to configure the gps.
     *
     * @param SIRFMessage int[]
     */
    public void sendSIRFMessage(int[] SIRFMessage) {
        btConnectionToGPS.sendSirfMessage(SIRFMessage);
       Debug.setDebug("The following SIRF message has been sent : "+SIRFMessage ,Debug.DETAIL);
    }

    /**
     * Connect directly with the last device connected.
     * Service automatically connected if the last device and service are found.
     */
    public void connectLastDevice() {
        btConnectionToGPS.connectLastDevice();
    }

    /**
     * Search fo Bluetooth devices.
     * When a device is found,
     * the method getDevicesList must be called to choose the device from a list.
     */
    public void searchDevices() {
        btConnectionToGPS.searchDevices();
    }

    /**
     * Only with the searchDevices method.
     * Called when the device is choosed from the list obtained with getDevicesList(). After, the service is connected automatically.
     *
     * @param i number of the index from the list of devices.
     */
    public void connectDevice(int i) {
        btConnectionToGPS.connectDevice(i);
    }

    /**
     * Stop all thread of the bluetooth package.
     * Must be called when the application is closed or when a service or device is not found.
     */

    public void stopGPS() {
        btConnectionToGPS.stopGPS();
    }

    /**
     * To know if there is or not a last bluetooth connection in the recordstore.
     * If it's true, this is possible to use the connectLastDevice method.
     *
     * @return boolean true if a last connection exist.
     */
    public boolean isLastDevice() {
        return btConnectionToGPS.isLastDevice();
    }

    /**
     * Change the time during the application is allowed to search Devices.
     * Default : 20 seconds.
     *
     * @param time time in seconds
     */
    public void setSearchDeviceTime(int time) {
        btConnectionToGPS.setSearchDeviceTime(time);
    }

    /**
     * Change the time during the application is allowed to search Services.
     * Default : 10 seconds.
     *
     * @param time time in seconds
     */
    public void setSearchServiceTime(int time) {
        btConnectionToGPS.setSearchServiceTime(time);
    }

    /**
     * To know if there is found one or more bluetooth device
     * Then, the list can be showing
     *
     * @return true if one or more devices are found.
     */
    public boolean isDeviceFound() {
        return btConnectionToGPS.isDeviceFound();
    }

    /**
     * To know when the service is connected.
     *
     * @return true if its connected.
     */
    public boolean isServiceConnected() {
        return btConnectionToGPS.isServiceConnected();
    }

    /**
     * After 20 seconds (or time set), if no bluetooth devices or services are found,
     * the research of device and service will be stopping.
     *
     * @return true if there is definitely no connection.
     */
    public boolean isServiceDefinitelyNotConnected() {
        return btConnectionToGPS.isServiceDefinitelyNotConnected();
    }


    /**
     * Get a displayable List of devices found from the searchDevices method.
     * Use the index of a device of the list to connect it with connectDevice().
     * @return the list of devices.
     */
    public List getDevicesList() {
        return btConnectionToGPS.getDevicesList();
    }

    /**
     * Get the URL of service of the bluetooth device. Must not necessary be called, only
     * if the user want to do another gps application.
     *
     * @return a String of the url of the service.
     */
    public String getDeviceURL() {
        return btConnectionToGPS.getDeviceUrl();
    }
}
