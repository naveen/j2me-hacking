package com.hipoqih.plugin.s60_2nd.gps;

//*********************************************************************
//@(#)$RCSfile: GPSSirfDataProcessor.java,v $   $Revision: 1.1 $ $Date: 2003/04/17 15:00:50 $
//
//Copyright (c) 2001 IICM, Graz University of Technology
//Inffeldgasse 16c, A-8010 Graz, Austria.
//
//This program is free software; you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License (LGPL)
//as published by the Free Software Foundation; either version 2.1 of
//the License, or (at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the
//Free Software Foundation, Inc.,
//59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
//*********************************************************************/

//----------------------------------------------------------------------
//This data processor switches a sirf device from sirf mode to nmea
//mode. The rest is normal nmea behaviour. So no real sirf protocol
//is used (only the change from sirf to nmea).  As a reference the
//sirf manual from http://www.falcom.de/pub/sirf/SiRFmessages.pdf is
//used.
//
//@author Christof Dallermassl
//@version $Revision: 1.1 $

class SIRFMessages {

    private com.hipoqih.plugin.s60_2nd.gps.BluetoothConnection bt = null;

    private final int CHECKSUM_15BIT_LIMIT = (int) 32767;

    //SIRF message by default to switch to NMEA protocol.
    //The rate is defined for each sort of NMEA message
    private int[] SWITCH_SIRF_TO_NMEA_PAYLOAD = new int[]
            {0x81,  // message id
                    0x02,  // mode
                    0x01,  // gga message. rate = 1.
                    0x01,  // checksum
                    0x00,  // gll message.  rate = 0 (deactived).
                    0x01,  // checksum
                    0x05,  // gsa message.  rate = 5.
                    0x01,  // checksum
                    0x05,  // gsv message.  rate = 5.
                    0x01,  // checksum
                    0x00,  // mss message.  rate = 0 (deactived).
                    0x01,  // checksum
                    0x00,  // rmc message.  rate = 0 (deactived).
                    0x01,  // checksum
                    0x00,  // vtg message.   rate = 0 (deactived).
                    0x01,  // checksum
                    0x00,  // unused field
                    0x01,  // unused field
                    0x00,  // unused field
                    0x01,  // unused field
                    0x00,  // unused field
                    0x01,  // unused field
                    0x12, 0xc0  // baud rate (38400,19200,9600,4800,2400)
            };

    // Default constructor.
    public SIRFMessages(BluetoothConnection bluetoothManager) {
        bt = bluetoothManager;
    }

    //Method to send the default SIRF message
    public void sendSIRFMessageToNMEAProtocol() {
        int[] sirfMessage = SWITCH_SIRF_TO_NMEA_PAYLOAD;
        bt.sendSIRFMessage(createSIRFMessage(sirfMessage));
    }

    //Method to set parameters of the SIRF message
    public void sendSIRFMessageToNMEAProtocol(int GGARate, int GLLRate, int GSARate, int GSVRate, int MSSRate, int RMCRate, int VTGRate, int baud) {
        String temp=Integer.toHexString(baud);
        if (temp.length()==3) temp='0'+temp;
        int[] sirfMessage = new int[]
                {0x81,  // message id
                        0x02,  // mode
                        0x0 + Integer.parseInt(Integer.toHexString(GGARate), 16),// gga message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(GLLRate), 16),// gll message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(GSARate), 16),// gsa message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(GSVRate), 16),// gsv message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(MSSRate), 16),// mss message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(RMCRate), 16),// rmc message
                        0x01,  // checksum
                        0x0 + Integer.parseInt(Integer.toHexString(VTGRate), 16),// vtg messag
                        0x01,  // checksum
                        0x00,  // unused field
                        0x01,  // unused field
                        0x00,  // unused field
                        0x01,  // unused field
                        0x00,  // unused field
                        0x01,  // unused field
                        0x0 + temp.charAt(0) + temp.charAt(1),
                        0x0 + temp.charAt(2) + temp.charAt(3)  // baud rate (38400,19200,9600,4800,2400)
                };
        bt.sendSIRFMessage(createSIRFMessage(sirfMessage));
    }

    //Calculate the checksum of a sirf message
    //param message the sirf message
    //return the checksum
     private int calculateSIRFChecksum(int[] message) {
        int index = 0;
        int checksum = 0;
        while (index < message.length) {
            checksum += message[index];
            index++;
        }
        checksum = checksum & CHECKSUM_15BIT_LIMIT;
        return (checksum);
    }

    //Create a sirf message (sequence start, payload, checksum, sequence
    //end) from a given payload.
    //param payload the payload
    //return the sirf message (int array)
    private int[] createSIRFMessage(int [] payload) {
        // start sequence = 2 bytes, end sequence = 2 bytes,
        // payload length = 2 bytes, message checksum = 2 bytes
        int message_length = payload.length + 8;
        int[] message = new int[message_length];
        message[0] = 0xa0;
        message[1] = 0xa2;
        message[2] = (payload.length & 0xff00) >> 8;
        message[3] = payload.length & 0x00ff;
        System.arraycopy(payload, 0, message, 4, payload.length);
        int checksum = calculateSIRFChecksum(payload);
        message[message_length - 4] = (checksum & 0xff00) >> 8;
        message[message_length - 3] = checksum & 0x00ff;
        message[message_length - 2] = 0xb0;
        message[message_length - 1] = 0xb3;
        return (message);
    }
}