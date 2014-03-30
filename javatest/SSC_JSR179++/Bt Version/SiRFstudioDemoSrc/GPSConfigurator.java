


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import com.sirf.microedition.location.extensions.LocationProviderConfiguration;
import com.sirf.microedition.util.LoggerForm;


//#ifdef JSR82 
import com.sirf.microedition.location.extensions.BTGPSDiscovererListener;
import com.sirf.microedition.location.extensions.BTGPSDiscoverer;
public class GPSConfigurator extends Form implements CommandListener, BTGPSDiscovererListener {

	private static final int DEFAULT_SERVER_PORT = 59910;
    private static final String DEFAULT_SERVER_IP = "66.232.130.251";
    private static final String DEFAULT_CONNECTION_STRING = "COM1";
    private static final String RECORD_STORE_NAME = "SiRF_Config";
    /** Commands used in the UI elements */
    private Command cmdCancel = new Command("Cancel", Command.CANCEL, 1);
    private Command cmdSave = new Command("Save", Command.OK, 1);
    private Display display;
    private TextBox mainScreen;
    private TextField serverIPAddressTextField;
    private TextField serverPortTextField;
    private TextField connectionStringTextField;
    private TextField referenceLocationLatTextField;
    private TextField referenceLocationLonTextField;

//#ifdef JSR82    
    /** Bluetooth manager used for bt gps device discovery */
    private BTGPSDiscoverer bluetoothManager;

    private Command cmdStartBluetoothDiscovery = new Command(
            "Search for bluetooth devices", Command.SCREEN, 1);

    /** Thread doing the bluetooth search */
    private Thread bluetoothSearchThread;
    
    /** Alert shown while users waits for device discovery */
    private Alert bluetoothDiscoveryWaitAlert;

    /** Alert that shows discovery results */
    private Alert bluetoothDiscoveryResults;
    
    private String connectionStringCandidate;
//#endif

    /** the IP of the AGPS server */
    private String serverIP;

    /** the tcp/ip port of the AGPS server */
    private int serverPort;

    /** the connection string passed to locationing subsystem to be connected to */
    private String connectionString;

    private CommandListener listener;
    private double approxLatitude;
    private double approxLongitude;
    private boolean isApproxPositionSet;


    /* Default Constructor initializes the form */
    public GPSConfigurator(Display display) {
        super("Configuration");
        // this needs access to display
        this.display = display;

        serverIPAddressTextField = new TextField("Server IP", "", 15,
                TextField.ANY);
        serverIPAddressTextField.setInitialInputMode("IS_FULLWIDTH_DIGITS");
        serverPortTextField = new TextField("Server Port Number", "", 5,
                TextField.NUMERIC);
        connectionStringTextField = new TextField("GPS connectionstring", "",
                150, TextField.ANY);
        referenceLocationLatTextField = new TextField("Your approx latitude", "37",
                150, TextField.ANY);
        referenceLocationLonTextField = new TextField("Your approx longitude", "-121",
                150, TextField.ANY);
        append(serverIPAddressTextField);
        append(serverPortTextField);
        append(connectionStringTextField);
        append(referenceLocationLatTextField);
        append(referenceLocationLonTextField);
        setCommandListener(this);
        addCommand(cmdCancel);
        addCommand(cmdSave);

        addCommand(cmdStartBluetoothDiscovery);
        // get a Bluetooth manager instance this as the listener
        bluetoothManager = new BTGPSDiscoverer(this);
        
        readConfig();

        serverIPAddressTextField.setString(serverIP);
        serverPortTextField.setString("" + serverPort);
        connectionStringTextField.setString(connectionString);
        referenceLocationLatTextField.setString(approxLatitude+"");
        referenceLocationLonTextField.setString(approxLongitude+"");
    }

    public void setExternalCommandListener(CommandListener listener) {
        this.listener = listener;
    }

    
    /**
     * Commandaction is called whenever user select a command in UI elements
     */
    public void commandAction(Command c, Displayable d) {
        // ////////////////////////////////////////////////////
        // configurationForm
        // ////////////////////////////////////////////////////
        if (d == this) {
            if (c == cmdSave) {
                boolean error = false;
                // check the IP address and conn string
                serverIP = serverIPAddressTextField.getString().trim();
                connectionString = connectionStringTextField.getString().trim();
                if (serverIP.length() == 0 || connectionString.length() == 0) {
                    display.setCurrent(new Alert("",
                            "Invalid server IP or connection string!", null,
                            AlertType.ERROR), this);
                    error = true;
                }

                // check port
                try {
                    serverPort = Integer.parseInt(serverPortTextField.getString().trim());
                } catch (NumberFormatException e) {
                    display.setCurrent(new Alert("", "Invalid server port!",
                            null, AlertType.ERROR), this);
                    error = true;
                }
                
                // check approx latitude
                try {
                    String lat = referenceLocationLatTextField.getString().trim();
                    if(lat.length()>0) {
                        approxLatitude = Double.parseDouble(lat);
                        if(approxLatitude<-90.0 || approxLatitude>90.0)
                            throw new NumberFormatException();
                        isApproxPositionSet = true;
                    } else {
                        isApproxPositionSet = false;
                    }
                } catch (NumberFormatException e) {
                    display.setCurrent(new Alert("", "Invalid approx latitude!",
                            null, AlertType.ERROR), this);
                    error = true;
                }
                
                // check approx longitude
                try {
                    String lon = referenceLocationLonTextField.getString().trim();
                    if(lon.length()>0) {
                        approxLongitude = Double.parseDouble(lon);
                        if(approxLongitude<-180.0 || approxLongitude>180.0)
                            throw new NumberFormatException();
                        isApproxPositionSet = true;
                    } else isApproxPositionSet = false;
                } catch (NumberFormatException e) {
                    display.setCurrent(new Alert("", "Invalid approx longitude!",
                            null, AlertType.ERROR), this);
                    error = true;
                }

                if (!error) {
                    saveConfig();
					commandAction(cmdCancel, this);
                }
            }
            else if (c == cmdStartBluetoothDiscovery) {
                // cancel previous one, if any
                if (bluetoothSearchThread != null)
                    bluetoothManager.close();
                // create new Thread for discovery
                this.append("Starting bluetooth discovery.");
                bluetoothSearchThread = new Thread() {
                    public void run() {
                        // Listener is alrady set earlier.
                        bluetoothManager.searchBTGPSDevices();
                    }
                };
                bluetoothSearchThread.start();

                // show waiting screen to user
                bluetoothDiscoveryWaitAlert = new Alert("",
                        "Please wait while searching devices...", null,
                        AlertType.INFO);
                bluetoothDiscoveryWaitAlert.addCommand(cmdCancel);
                bluetoothDiscoveryWaitAlert.setTimeout(Alert.FOREVER);
                bluetoothDiscoveryWaitAlert.setCommandListener(this);
                display.setCurrent(bluetoothDiscoveryWaitAlert);
                display.setCurrent(this);
            }
            else if (c == cmdCancel) {
                if(listener != null) {
                    listener.commandAction(new Command("", Command.BACK, 1), this);
                }
            }
        }
        /****************************************************/
        /* bluetoothDiscoveryWaitAlert                      */
        /****************************************************/
        else if (d == bluetoothDiscoveryWaitAlert && c == cmdCancel) {
            if (bluetoothSearchThread != null)
                bluetoothManager.close();
            bluetoothSearchThread = null;
            display.setCurrent(this);
        }
        /****************************************************/
        /* bluetoothDiscoveryResults                        */
        /****************************************************/
        else if (d == bluetoothDiscoveryResults) {
            if (c == cmdCancel) {
                display.setCurrent(mainScreen);
            } else if (c == cmdSave) {
                connectionString = connectionStringCandidate;
                connectionStringTextField.setString(connectionString);
                display.setCurrent(this);
            }
        }
    }


    /**
     * Saves current values of serverIP, serverPort and connection string into
     * recordstore to be used later.If values have not been saved before, this
     * will create the RecordStore. Also initializes Location engine component
	 * with these values.
     */
    private void saveConfig() {
        this.append("Saving configuration.");
        // set retrieved values into GeoManager
        LocationProviderConfiguration.setAgpsServer(serverIP, serverPort);
        LocationProviderConfiguration.setSerialConnectionString(connectionString);
        if(isApproxPositionSet) {
            LocationProviderConfiguration.setApproxPosition(isApproxPositionSet, approxLatitude, approxLongitude);
        } else this.append("(Did not set the approx position.)");

        RecordStore rs = null;
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            rs = RecordStore.openRecordStore(RECORD_STORE_NAME, true); // create if not found!
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeUTF(serverIP);
            dos.writeInt(serverPort);
            dos.writeUTF(connectionString);
            dos.writeBoolean(isApproxPositionSet);
            dos.writeDouble(approxLatitude);
            dos.writeDouble(approxLongitude);
            byte[] record = baos.toByteArray();
            // update old one, or add a new one
            if (rs.getNumRecords() == 0) {
                // empty store, use addRecord()
                LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------saving add " + connectionString);
                rs.addRecord(record, 0, record.length);
                LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------saving add done "
                        + connectionString);
            } else {
                LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------saving set " + connectionString);
                rs.setRecord(1, record, 0, record.length);
                LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------saving set done "
                        + connectionString);
            }
        } catch (Exception e) {
            display.setCurrent(new Alert("Error saving values."), mainScreen);
        } finally {
            try {
                if (dos != null)
                    dos.close();
                if (baos != null)
                    baos.close();
                if (rs != null)
                    rs.closeRecordStore();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Reads the previously saved configuration (server IP address, port and GPS
     * connection string) from RecordStore. If the read fails for some reason,
     * eg. the app has not been used before and thus values not yet saved, it
     * will set default values.
     */
    public void readConfig() {
        ByteArrayInputStream bais = null;
        DataInputStream din = null;
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(RECORD_STORE_NAME, false); // dont create if not found!

            byte[] config = rs.getRecord(1);
            bais = new ByteArrayInputStream(config);
            din = new DataInputStream(bais);

            // read the data from record.
            serverIP = din.readUTF();
            serverPort = din.readInt();
            connectionString = din.readUTF();
            isApproxPositionSet = din.readBoolean();
            approxLatitude = din.readDouble();
            approxLongitude = din.readDouble();

            // set retrieved values into GeoManager
            LocationProviderConfiguration.setAgpsServer(serverIP, serverPort);
            LocationProviderConfiguration.setSerialConnectionString(connectionString);
            if(isApproxPositionSet)
                LocationProviderConfiguration.setApproxPosition(isApproxPositionSet, approxLatitude, approxLongitude);
            else this.append("(Did not set the approx position.)");
            LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------read " + connectionString);
        } catch (Exception e) {
            // something caused an exception; use default values.
            LoggerForm.getInstance().log(LoggerForm.LOGLEVEL_LOW, "---------read exception " + e);
            serverIP = DEFAULT_SERVER_IP;
            serverPort = DEFAULT_SERVER_PORT;
            connectionString = DEFAULT_CONNECTION_STRING;
            // set retrieved values into GeoManager
            LocationProviderConfiguration.setAgpsServer(serverIP, serverPort);
            LocationProviderConfiguration.setSerialConnectionString(connectionString);
        } finally {
            try {
                if (din != null)
                    din.close();
                if (bais != null)
                    bais.close();
                if (rs != null)
                    rs.closeRecordStore();
            } catch (Exception e) {
            }
        }
    }

//#ifdef JSR82
    /**
     * Implements BTListener interface This gets called when the Bluetooth
     * discovery process is done.
     * 
     */
    public void notifyDevicesDiscovered(String deviceConnectionStrings,String deviceFriendlyNames) {
        if (deviceConnectionStrings != null) {
            /* we found a bluetooth device! */
            connectionStringCandidate = deviceConnectionStrings;
            bluetoothDiscoveryResults = new Alert("Found:",
                    connectionStringCandidate + " \n\n Use this device?", null,
                    AlertType.CONFIRMATION);
            bluetoothDiscoveryResults.addCommand(cmdSave);
            bluetoothDiscoveryResults.addCommand(cmdCancel);
            bluetoothDiscoveryResults.setTimeout(Alert.FOREVER);
            bluetoothDiscoveryResults.setCommandListener(this);
            display.setCurrent(bluetoothDiscoveryResults);
        } else {
            display.setCurrent(new Alert("", "No Bluetooth devices found! ["
                    + deviceConnectionStrings + "]", null, AlertType.ERROR),
                    this);
        }
    }

    public void notifyDiscoveryError(int errorCode, String errorMessage) {
        /* No bluetooth devices found, show an error to user. */
        display.setCurrent(new Alert("", "No Bluetooth devices found! ["
                + errorMessage + "]", null, AlertType.ERROR),
                this);
    }

}
