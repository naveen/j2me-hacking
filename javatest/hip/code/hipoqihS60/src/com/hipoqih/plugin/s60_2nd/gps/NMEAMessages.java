package com.hipoqih.plugin.s60_2nd.gps;

/*
 * API for gps
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */

// Class to create NMEA message to send to configure the gps.
// By exemple, the SIRF message is used to switch the NMEA protocol to the SIRF protocol
class NMEAMessages {

    private BluetoothConnection bt = null;

    //Constructor.
    //param bluetoothManager => BluetoothConnection object
    public NMEAMessages(BluetoothConnection bluetoothManager){
        bt = bluetoothManager;
    }

    // set the gps device in SIRF protocol
    //param int baud. set 4800, 9600, 19200,38400
    public void setSIRFProtocol(int baud) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF100");
        //Number to switch to sirf protocol
        Command.setData("0", 1);
        Command.setData(Integer.toString(baud), 2);
        Command.setData("8", 3);
        Command.setData("1", 4);
        Command.setData("0", 5);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the DGPS Port.
    //param int baud. set 4800, 9600, 19200,38400
    public void setDGPSPort(int baud) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF102");
        Command.setData(Integer.toString(baud), 1);
        Command.setData("08", 2);
        Command.setData("01", 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    //set SBAS
    //param boolean on/off
    public void setSbas(boolean on){
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF108");
        if(on)
        Command.setData("01", 1);
        else
        Command.setData("00", 1);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of GGA messages send by the gps device.
    //if the rate is null, the GGA message are deactivated
    public void setGGARate(int rate) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable GGA message
        Command.setData("00", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of GLL messages send by the gps device.
    //if the rate is null, the GLL message are deactived
    public void setGLLRate(int rate) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable GLL message
        Command.setData("01", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of GSA messages send by the gps device.
    //if the rate is null, the GSA message are deactived
    public void setGSARate(int rate)  {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable GSA message
        Command.setData("02", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of GSV messages send by the gps device.
    //if the rate is null, the GSV message are deactived
    public void setGSVRate(int rate)  {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable GSV message
        Command.setData("03", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of RMC messages send by the gps device.
    //if the rate is null, the RMC message are deactived
    public void setRMCRate(int rate) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable RMC message
        Command.setData("04", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

    // Set the rate of VTG messages send by the gps device.
    //if the rate is null, the VTG message are deactived
    public void setVTGRate(int rate) {
        NMEAObject Command = new NMEAObject();
        Command.setType("PSRF103");
        //Number for enable or disable VTG message
        Command.setData("05", 1);
        Command.setData("00", 2);
        Command.setData(Integer.toString(rate), 3);
        Command.setData("00", 4);
        bt.sendNMEAMessage(Command.toNMEASentence() + "\r\n");
    }

}
