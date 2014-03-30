package com.hipoqih.plugin.s60_2nd.gps;

//import com.hipoqih.plugin.LogScreen;
import javax.bluetooth.*;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStoreException;

import com.hipoqih.plugin.State;

// - Connection to a new device :
//   searchDevices method must be called.
//   After that, a research of Bluetooth devices is automatically done.
//   The programmer must only take and display the list of device with the getDevicesList method.
//   WARNING : in the application of the programmer, the thread have to be synchcronized until
//             the device is not choosed.
//   After the device selection, the connectDevice method must be called with the number of the selection.
//
// - Connection to the last device selected:
//   The programmer have to call only the connectLastDevice method
//
// - The programmer can use boolean to know when device is found, when the connection is done, etc...
//License: This library is under the GNU Lesser General Public License
//author Praplan Christophe
//author Velen Stephane
//version 1.0 <p>Geneva,the 23.03.2006
class BluetoothConnectionToGPS implements DiscoveryListener, Runnable {

    private ReceiveAndSendMessages bluetoothToGPS;
    private SaveAndLoad saveLoad = new SaveAndLoad("bluetoothToGPS");
    private DiscoveryAgent mDiscoveryAgent = null;
    private RemoteDevice remoteDevice;
    private Vector devices = new Vector();
    private List lastDevice = new List("last Device", List.EXCLUSIVE);
    private List listFindDevice = new List("Devices find", List.EXCLUSIVE);
    private static String url = "";
    private String lastdeviceAdress = new String();
    private int idService = 0;
    private int timeSearchDevice = 20;
    private int timeSearchService = 10;
    private UUID[] uuidSet = {new UUID(0x1101)};
    private boolean searchDevice = false;
    private boolean searchService = false;
    private boolean isConnected = false;
    private boolean connectLastDevice = false;
    private boolean deviceFind = false;
    private boolean serviceNotConnected = false;
    private boolean endNow = false;
    private MessageListener listener;


    //constructor
    public BluetoothConnectionToGPS(MessageListener listener) {
        this.listener = listener;
    }

    //use to have a bluetooth connection with the last device connected.
    //Service directly connected if the last device and service are found.
    public void connectLastDevice() {
        url = null;
        idService = 0;
        serviceNotConnected = false;
        isConnected = false;
        deviceFind = false;
        endNow = false;
        // A last connection exist.
        if (isLastDevice()) {
            //to know if its a connection to the last device
            connectLastDevice = true;
            Thread thread = new Thread(this);
            thread.start();
        } else {
            serviceNotConnected = true;
        }
    }

    //Bluetooth connection to a new device.
    //When a device is found,
    //the method getDeviceList must be called to choose the device from a list.
    public void searchDevices() {
        isConnected = false;
        deviceFind = false;
        connectLastDevice = false;
        serviceNotConnected = false;
        endNow = false;
        url = null;
        devices.removeAllElements();
        listFindDevice.deleteAll();
        listFindDevice = new List("Devices found", List.EXCLUSIVE);
        lastDevice.deleteAll();
        idService = 0;
        Thread thread = new Thread(this);
        thread.start();
    }

    //called when the device is choosed from the list. After, the service is connected automatically.
    // param i number of the index from the list of devices.
    public void connectDevice(int i) 
    {
        if (searchDevice)
            //cancel research of device
            mDiscoveryAgent.cancelInquiry(this);
        remoteDevice = (RemoteDevice) devices.elementAt(i);
        synchronized (this) {
            this.notify();
        }
    }

    // while the API is not close, the boolean isConnected is true
    private void readContent() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // stop all thread of the bluetooth package.
    //it must be called when the application is closed or when a service or device is not found.
    public void stopGPS() {
        endNow = true;
        url = null;
        if (searchDevice)
            mDiscoveryAgent.cancelInquiry(this);
        if (searchService)
            mDiscoveryAgent.cancelServiceSearch(idService);
        isConnected = false;
        synchronized (this) {
            this.notify();
        }
        if (bluetoothToGPS != null) {
            bluetoothToGPS.stop();
        }
    }

    //method to send NMEA messages to the gps.
    //param NMEAMessage string
    public void sendNMEAMessage(String NMEAMessage) {
        bluetoothToGPS.sendNMEAMessage(NMEAMessage);
    }

    //method to send SIRF messages to the gps.
    //param SIRFmessage string
    public void sendSirfMessage(int[] SIRFmessage) {
        bluetoothToGPS.sendSirfMessage(SIRFmessage);
    }

    //use to know if there is or not a last bluetooth connection in the recordstore.
    //If its true, this is possible to use the connectLastDevice method.
    //return a true boolean if a last connection exist.
    public boolean isLastDevice() {
        try {
            lastDevice = saveLoad.showListRecordStore();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        boolean lastDevice1;
        if (lastDevice.size() != 0)
            lastDevice1 = true;
        else
            lastDevice1 = false;
        return lastDevice1;
    }

    //change the time during the application is allowed to search Devices.
    //Default : 20 seconds.
    //param time time in seconds
    public void setSearchDeviceTime(int time) {
        timeSearchDevice = time;
    }

    //change the time during the application is allowed to search Services.
    //Default : 10 seconds.
    //param time time in seconds
    public void setSearchServiceTime(int time) {
        timeSearchService = time;
    }

    //use to know if there is found, at least, a bluetooth device.
    //Then, the list can be showing
    //return true if a device is found.
    public boolean isDeviceFound() {
        return deviceFind;
    }

    //use to know when the service is connected.
    //return true if its connected.
    public boolean isServiceConnected() {
        return isConnected;
    }

    //after 20 seconds (or time set), if there is found no bluetooth device or service,
    //the research of device and service will be stopping.
    //return true if there is definitely no connection.
    public boolean isServiceDefinitelyNotConnected() {
        return serviceNotConnected;
    }


    //list of device found from the searchDevices method.
    //return the list of the device found
    public List getDevicesList() {
        return listFindDevice;
    }

    //url service of the bluetooth device. must not necessary be called, only
    //if the user want to do another bluetoothToGPS application.
    //return a String of the url of the service.
    public String getDeviceUrl() {
        return url;
    }

    //find the device for a new or a last connection,
    //add the list or find directly the service.
    //param remoteDevice remotedevice found
    //param deviceClass  deviceClass found
    public void deviceDiscovered(RemoteDevice rd, DeviceClass deviceClass) {
        //For a last connection
        if (connectLastDevice) {
            try {
                //load from the recordStore the Bluetooth address of the last device connected
                lastdeviceAdress = saveLoad.loadRecordStore(0).toString();
            } catch (RecordStoreException e) {
                e.printStackTrace();
            }
            //Compare the Bluetooth address of the device found with the last device connected.
            if (rd.getBluetoothAddress().toString().equals(lastdeviceAdress)) {
                try {
                    deviceFind = true;
                    searchService = true;
                    remoteDevice = rd;
                    //if its equal the service is founding
                    idService = mDiscoveryAgent.searchServices(null, uuidSet, rd, this);
                } catch (BluetoothStateException e) {
                    e.printStackTrace();
                }
            }
            //Connect to a new device
        } else if (!connectLastDevice) {
            try {
                //Every devices name found is put in the list
                listFindDevice.append(rd.getFriendlyName(false).toString(), null);
                //Every remote devices is put in the vecor
                devices.addElement(rd);
                deviceFind = true;
                Debug.setDebug("Device found : " + rd.getFriendlyName(false).toString(), Debug.DETAIL);
            } catch (Exception e) {
            }
        }
    }

    //method when the device search is finished.
    //param i to know if the inquiry is completed, terminated or if there is an error.
    public void inquiryCompleted(int i) {
        searchDevice = false;
        switch (i) {
            case DiscoveryListener.INQUIRY_COMPLETED: {
                Debug.setDebug("Device search completed", Debug.NORMAL);
                break;
            }
            case DiscoveryListener.INQUIRY_ERROR: {
                Debug.setDebug("Error: Device search stopped", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
            case DiscoveryListener.INQUIRY_TERMINATED: {
                Debug.setDebug("Device search aborted", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
        }
    }

    //Method to take the first service offered of the device.
    //param i
    //param serviceRecords services of the device
    public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
        Debug.setDebug("Service discovered : " + serviceRecords[0], Debug.DETAIL);
        //take the URL when the service is discovered
        url = serviceRecords[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
        url = url.substring(0, 22);
        State.addToLog("url: " + url);
    }

    //the service search is terminated.
    //param i
    //param i1 to know the state of the service search.
    public void serviceSearchCompleted(int i, int i1) {
        searchService = false;
        //URL have not to be empty
        if (url != null) {
            Debug.setDebug("Connection to : " + url, Debug.NORMAL);
            //start to read sentence from the gps device
            bluetoothToGPS = new ReceiveAndSendMessages(url, listener);
            long time = System.currentTimeMillis() / 1000;
            boolean stop = false;
            while (!isConnected && !stop) {
                isConnected = bluetoothToGPS.isConnected();
                //When the time by default or setting is done, the service search is stopping
                if ((System.currentTimeMillis() / 1000 - time) > timeSearchService) {
                    serviceNotConnected = true;
                    stop = true;
                }
            }
        } else {
            serviceNotConnected = true;
        }
        switch (i1) {
            case DiscoveryListener.SERVICE_SEARCH_COMPLETED: {
                Debug.setDebug("Service search completed", Debug.NORMAL);
                try {
                    //save the Bluetooth Address in a record store
                    saveLoad.deleteAndSaveRecordStore(remoteDevice.getBluetoothAddress().toString(), "bluetoothToGPS device");
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                }
                readContent();
                break;
            }
            case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE: {
                Debug.setDebug("Service not reachable", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
            case DiscoveryListener.SERVICE_SEARCH_ERROR: {
                Debug.setDebug("Error in the device search", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
            case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS: {
                Debug.setDebug("Service search: no records", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
            case DiscoveryListener.SERVICE_SEARCH_TERMINATED: {
                Debug.setDebug("Service search aborted", Debug.NORMAL);
                serviceNotConnected = true;
                break;
            }
        }
    }

    //thread to find device and service.
    public void run() {
        while (searchDevice || searchService) {
            Thread.yield();
        }
        if (url == null) {
            try {
                LocalDevice localDevice = LocalDevice.getLocalDevice();
                //discover Bluetooth device
                mDiscoveryAgent = localDevice.getDiscoveryAgent();
                searchDevice = true;
                //start discover
                mDiscoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
                Debug.setDebug("Devices search started", Debug.NORMAL);
                long time = System.currentTimeMillis() / 1000;
                boolean stop = false;
                while (!deviceFind && !stop) {
                	//State.addToLog("timeSearchDevice:" + Integer.toString(timeSearchDevice));
                    //When the time by default or setting is done, the device search is stopping
                    if ((System.currentTimeMillis() / 1000 - time) > timeSearchDevice) {
                        serviceNotConnected = true;
                        stop = true;
                    }
                }
                //Thhread must be synchronized when the Bluetooth device is selected
                if (stop) {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                    if (!endNow) {
                        searchService = true;
                        Debug.setDebug("Services search started", Debug.NORMAL);
                        //search service of the Bluetooth device selected
                        idService = mDiscoveryAgent.searchServices(null, uuidSet, remoteDevice, this);
                    }
                }
            } catch (Exception e) {
            }
        } else if (!endNow) {
            while (isConnected) {
                isConnected = bluetoothToGPS.isConnected();
            }
        } else
            bluetoothToGPS.stop();
    }
}
