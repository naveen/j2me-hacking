package net.uworks.apps.bluelcd;

import javax.microedition.lcdui.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;

/**
 * <p>Title: BlueLCD</p>
 *
 * <p>Description: A LCD simulator for mobile phones</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: uWorks</p>
 *
 * @author Josep del Rio
 * @version 1.0
 */
public class RFCommServer implements Runnable {
       // Bluetooth singleton object
       LocalDevice device;
       DiscoveryAgent agent;

       // SPP_Server specific service UUID
       // note: this UUID must be a string of 32 char
       // do not use the 0x???? constructor because it won't
       // work. not sure if it is a N6600 bug or not
       public final static UUID uuid = new UUID("102030405060708090A0B0C0D0E0F010", false);

       //
       // major service class as SERVICE_TELEPHONY
       private final static int SERVICE_TELEPHONY = 0x400000;


       // control flag for run loop
       // set true to exit loop
       public boolean done = false;
       private DataInputStream in = null;
       private DataInputStream out = null;


       // our BT server connection
       public StreamConnectionNotifier server;

       private LCDScreen screen;

       public RFCommServer(LCDScreen screen)
       {
           this.screen = screen;
       }

       public void startServer()
       {
         try
         {
           //
           // initialize the JABWT stack
           device = LocalDevice.getLocalDevice(); // obtain reference to singleton
           device.setDiscoverable(DiscoveryAgent.GIAC); // set Discover mode to LIAC

           // start a thread to serve the server connection.
           // for simplicity of this demo, we only start one server thread
           // see run() for the task of this thread
           Thread t = new Thread( this );
           t.start();

         } catch ( BluetoothStateException e )
         {
           e.printStackTrace();
         }
       }

       public void stopServer() {
           done = true;
           if (in != null) {
               try {
                   in.close();
                   in = null;
               } catch (Exception ex) {
                   if (ConfigValues.DEBUG) {
                       ex.printStackTrace();
                   }
               }
           }
           if (out != null) {
               try {
                   out.close();
                   out = null;
               } catch (Exception ex) {
                   if (ConfigValues.DEBUG) {
                       ex.printStackTrace();
                   }
               }
           }

           if (server != null) {
               try {
                   server.close();
                   server = null;
               } catch (Exception ex) {
                   if (ConfigValues.DEBUG) {
                       ex.printStackTrace();
                   }
               }
           }
       }

       public void run()
       {
         // human friendly name of this service
         String appName = "BlueLCD";

         // connection to remote device
         StreamConnection c = null;
         try
         {
             String url = "btspp://localhost:" + uuid.toString() + ";name=" + appName;
             /*
             String url = "btspp://localhost:" + uuid.toString();
             if (ConfigValues.useChannel) {
                 url += ":" + ConfigValues.channelNumber;
             }
             url += ";name=" + appName;
             */

           // Create a server connection object, using a
           // Serial Port Profile URL syntax and our specific UUID
           // and set the service name to BlueChatApp
           server =  (StreamConnectionNotifier)Connector.open( url );

           // Retrieve the service record template
           ServiceRecord rec = device.getRecord( server );

           // set ServiceRecrod ServiceAvailability (0x0008) attribute to indicate our service is available
           // 0xFF indicate fully available status
           // This operation is optional
           DataElement serviceClassIDList = new DataElement( DataElement.DATSEQ);
           serviceClassIDList.addElement(new DataElement(DataElement.UUID, new UUID("0000110100001000800000805f9b34fb", false) ));
           rec.setAttributeValue( 0x0001, serviceClassIDList);

//           rec.setAttributeValue( 0x0002, new DataElement( DataElement.U_INT_4, 0x09000000) );
           DataElement browseGroupList = new DataElement( DataElement.DATSEQ);
           browseGroupList.addElement(new DataElement( DataElement.UUID, new UUID("0000100200001000800000805f9b34fb", false) ));
           rec.setAttributeValue( 0x0005, browseGroupList );
//           rec.setAttributeValue( 0x0007, new DataElement( DataElement.U_INT_4, 0xffffffb0040000l) );

           // This one must be present
           rec.setAttributeValue( 0x0008, new DataElement( DataElement.U_INT_1, 0xFF ) );

//           rec.setAttributeValue( 0x0009, new DataElement( DataElement.UUID, new UUID("0000110100001000800000805F9B34FB", false) ) );
           rec.setAttributeValue( 0x0100, new DataElement(DataElement.STRING, "BlueLCD" ));
           rec.setAttributeValue( 0x0101, new DataElement(DataElement.STRING, "LCD emulator" ));
           rec.setAttributeValue( 0x0102, new DataElement(DataElement.STRING, "uWorks.net" ));

           device.updateRecord(rec);

           // Print the service record, which already contains
           // some default values
//           Util.printServiceRecord( rec );

           // Set the Major Service Classes flag in Bluetooth stack.
           // We choose Telephony Service
           rec.setDeviceServiceClasses( SERVICE_TELEPHONY  );



         } catch (Exception e)
         {
           e.printStackTrace();
         }

         while( !done)
         {
           try {
           screen.aboutToConnect();
             ///////////////////////////////
             //
             // start accepting client connection.
             // This method will block until a client
             // connected
             c = server.acceptAndOpen();
             screen.connected();
             //
             // retrieve the remote device object
             RemoteDevice rdev = RemoteDevice.getRemoteDevice( c );

             // obtain an input stream to the remote service
             in = c.openDataInputStream();

             try {
                 while(!done) {
                     screen.readed(in.readUnsignedByte());
/*
                                      byte[] buffer = new byte[1024];

                                      while(true && !done) {
                                          int readed = in.read(buffer);
                                          for (int k=0; k<readed; k++) {
                                              screen.readed((buffer[k] >= 0) ? buffer[k] : buffer[k] + 256);
                                          }
                                      }
*/
                 }
             } catch (IOException ioex) {
                 // Problem with connection
             }

             // close current connection, wait for the next one
             c.close();


           } catch (Exception e)
           {

             e.printStackTrace();
           }

         } // while

         if (in != null) {
             try {
                 in.close();
             } catch (IOException ioex) {}
             in = null;
         }
         if (out != null) {
             try {
                 out.close();
             } catch (IOException ioex) {}
             out = null;
         }
         if (server != null) {
             try {
                 server.close();
                 server = null;
             } catch (Exception ex) {
                 if (ConfigValues.DEBUG) {
                     ex.printStackTrace();
                 }
             }
         }
       }
 }
