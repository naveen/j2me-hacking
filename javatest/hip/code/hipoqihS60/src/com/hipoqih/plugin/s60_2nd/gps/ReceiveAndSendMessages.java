package com.hipoqih.plugin.s60_2nd.gps;

import com.hipoqih.plugin.State;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.Connector;
import java.io.*;

import com.hipoqih.plugin.s60_2nd.gps.Debug;


//This class received the NMEA sentence from the gps device.
//The runnable take character after character to create a full sentence.
//NMEA and SIRF messages can be send to configure the gps device.
//Without stopping the connection after sending a message the runnable received again NMEA sentence.
//If the gps device turn off, it is possible to turn it on without a new connection.
//License: This library is under the GNU Lesser General Public License
//
//author Praplan Christophe
//author Velen Stephane
//version 1.0 <p>Geneva,the 23.03.2006
class ReceiveAndSendMessages implements Runnable {

    private Thread mClientThread = null;
    private boolean mEndnow = false;
    private boolean firstMessage = true;
    private StringBuffer content = new StringBuffer();
    public static String url = new String();
    private boolean isConnected = false;
    private boolean sendNMEAmessage = false;
    private String NMEAMessage;
    private int[] SIRFMessage;
    private boolean isNMEAMessage = true;
    private boolean firstConnection = true;
    private MessageListener listener;

    //constructor.
    //param con String of the URL
    //param listener to know when a message is received
    public ReceiveAndSendMessages(String con, MessageListener listener) {
        url = con;
        mEndnow = false;
        content = new StringBuffer();
        startClient();
        this.listener = listener;
       
    }

    //method to send NMEA messages to the gps.
    //param NMEAMessage string
    public void sendNMEAMessage(String NMEAmessage) {
        isNMEAMessage = true;
        NMEAMessage = new String(NMEAmessage);
        sendNMEAmessage = true;
    }

    //method to send SIRF message.
    //In this API, only the SIRF message to switch to nmea protocole is possible to send
    //param SIRFmessage array of byte of the message
    public void sendSirfMessage(int[] SIRFmessage) {
        isNMEAMessage = false;
        SIRFMessage = new int[SIRFmessage.length];
        SIRFMessage = SIRFmessage;
        sendNMEAmessage = true;
    }

    //gps message content
    //return a String of the NMEA message from the gps device
    public String getContent() {
        return content.toString();
    }

    //method called to know if the service is connected.
    //The service is connected when the connection is done
    //return true if its connected
    public boolean isConnected() {
        return isConnected;
    }

    //start the thread
    private void startClient() {
        if (mClientThread != null)
            return;
        mClientThread = new Thread(this);
        mClientThread.start();
    }

    //stop the thread
    public void stop() {
        mEndnow = true;
        isConnected = false;
    }

    //thread to read NMEA messages from the gps message
    //close the connection and open a new connection to send NMEA messages to the gps
    //after sending NMEA messages, the thread open a new InputStreamReader to read again the messages.
    //the connection is not close to send NMEA message to the gps device
    //When the gps device is off, the connection will restart automatically when the gps device is again on.
    public void run() {
        StreamConnection conn = null;
        //endnow is a boolean to stop the API
        while (!mEndnow) {
            try {
                // InputStreamReader to read NMEA message from the gps device
                InputStreamReader is = null;
                //URL have to exist
                if (url != null) {
                    if (!sendNMEAmessage || firstConnection)
                    {
                        //connector to the gps device
                    	try
                    	{
                    		conn = (StreamConnection) Connector.open(url, Connector.READ);
                    	}
                    	catch(Exception ex)
                    	{
                    		//State.addToLog(ex.getMessage());
                    		State.addToLog("Exception");
                    	}
                    	
                    }
                    if (conn != null) 
                    {
                    	State.addToLog("Dentro del if conn");
                        firstConnection = false;
                        sendNMEAmessage = false;
                        is = new InputStreamReader(conn.openInputStream());
                        int i = 0;
                        char c;
                        StringBuffer s = new StringBuffer();
                        isConnected = true;
                        do {
                            isConnected = true;
                            //take character after character
                            i = is.read();
                            c = (char) i;
                            State.addToLog(String.valueOf(c));
                            //i = 36 => $. $ is the start of the NMEA sentence
                            if (i == 36) {
                                firstMessage = false;
                                content = s;
                                //listener to know when a sentence is received
                                listener.messageReceived(content.toString());
                                s = new StringBuffer();
                            }
                            if (!firstMessage) {
                                s.append(c);
                            }
                            Debug.setDebug("The following NMEA message has been received : " + content, Debug.DETAIL);
                        } while ((i != -1) && (!mEndnow) && (!sendNMEAmessage));
                        //close InputStreamReader
                        is.close();
                        State.addToLog(s.toString());
                        // to send A NMEA or SIRF sentence
                        if (!mEndnow && sendNMEAmessage) {
                            try {
                                // OutputStreamWriter open
                                OutputStreamWriter is2 = null;
                                if (url != null) {
                                    if (conn != null) {
                                        isConnected = true;
                                        is2 = new OutputStreamWriter(conn.openOutputStream());
                                        //send NMEA sentence
                                        if (isNMEAMessage) {
                                            is2.write(NMEAMessage);
                                        }
                                        // send SIRF sentence
                                        else {
                                            for (int index = 0; index < SIRFMessage.length; index++) {
                                                is2.write(SIRFMessage[index]);
                                            }
                                        }
                                        is2.close();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                    //if no first connector
                    else
                    {
                    	State.addToLog("conn == null");
                        isConnected = false;
                    }
                }
                //if no URL
                else
                    isConnected = false;
            } catch (Exception e) {
            }
            //close the connector no message must be sending
            if (!sendNMEAmessage) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //test code for our simulator
    //thread to read NMEA messages from the gps simulator message
    //close the connection and open a new connection to send NMEA messages to the gps simulator
    //after sending NMEA messages, the thread open a new connection to read again the messages simulator.
    /*
  public void run() {
      StreamConnection conn = null;
      while (!mEndnow) {
          try {
              DataInputStream is = null;
              if (url != null) {
                  if (!sendNMEAmessage)
                  conn = (StreamConnection) Connector.open(url, Connector.READ_WRITE);
                  if (conn != null) {
                      isConnected = true;
                      is = new DataInputStream(conn.openInputStream());
                      StringBuffer s = new StringBuffer();
                      while ((!mEndnow) && (!sendNMEAmessage)){
                          s=new StringBuffer(is.readUTF());
                          content = s;
                          listener.messageReceived(content.toString());
                      }
                      is.close();
                      if (!mEndnow && sendNMEAmessage) {
                          try {
                              DataOutputStream is2 = null;
                              if (url != null) {
                                  if (conn != null) {
                                      isConnected = true;
                                      is2 = new DataOutputStream(conn.openOutputStream());
                                      if (isNMEAMessage) {
                                          System.out.print(NMEAMessage);
                                      } else
                                          for (int index = 0; index < SIRFMessage.length; index++) {
                                              System.out.print(SIRFMessage[index]);
                                          }
                                      is2.close();
                                      sendNMEAmessage = false;
                                  }
                              }
                          } catch (Exception e) {
                          }
                      }
                  } else
                      isConnected = false;
              } else
                  isConnected = false;
          } catch (Exception e) {
          }
          try {
              conn.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  } */   
}



