package net.uworks.apps.bluelcd;

import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.microedition.rms.RecordStore;
import java.io.DataInputStream;

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
public class ConfigValues {
    public final static boolean USE_NOKIA_EXT = false;
    public final static boolean DEBUG = false;

    public final static int SCREEN_NORMAL = 0;
    public final static int SCREEN_LANDSCAPE = 1;

    public static int charsPerLine = 20;
    public static int totalLines = 4;
    public static int backgroundColor = 0x000000;
    public static int foregroundColor = 0xffffff;
    public static int textBackgroundColor = 0x000000;
    public static int textForegroundColor = 0xffffff;
    //    public static String currentCharTable = null;
    public static String currentCharTable = "standard.png";
    public static int screenTransformation = SCREEN_LANDSCAPE;
    public static boolean useFullScreen = true;
    public static boolean useBacklight = true;
    /*
    public static boolean useChannel = false;
    public static int channelNumber = 1;
    */

    final static String RECORDSTORE_CONFIGURATION = "conf";

    // Read configuration from recordstore
    public static void readConfiguration() {
      DataInputStream inputStream = readRecordStore(RECORDSTORE_CONFIGURATION);

      if (inputStream != null) {
        try {
          charsPerLine = inputStream.readInt();
          totalLines = inputStream.readInt();
          backgroundColor = inputStream.readInt();
          foregroundColor = inputStream.readInt();
          textBackgroundColor = inputStream.readInt();
          textForegroundColor = inputStream.readInt();
          currentCharTable = inputStream.readUTF();
          screenTransformation = inputStream.readInt();
          useFullScreen = inputStream.readBoolean();
          useBacklight = inputStream.readBoolean();
          /*
          useChannel = inputStream.readBoolean();
          channelNumber = inputStream.readInt();
          */
        }
        catch (Exception eofe) {
          if (ConfigValues.DEBUG) {
            System.out.println("Exception at readConfiguration");
            eofe.printStackTrace();
          }
        }
      } else {
        writeConfiguration();
      }
    }

    public static void writeConfiguration() {
      // This means that we don't have a record defined.
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataOutputStream outputStream = new DataOutputStream(baos);

      try {
          outputStream.writeInt(charsPerLine);
          outputStream.writeInt(totalLines);
          outputStream.writeInt(backgroundColor);
          outputStream.writeInt(foregroundColor);
          outputStream.writeInt(textBackgroundColor);
          outputStream.writeInt(textForegroundColor);
          outputStream.writeUTF(currentCharTable);
          outputStream.writeInt(screenTransformation);
          outputStream.writeBoolean(useFullScreen);
          outputStream.writeBoolean(useBacklight);
          /*
          outputStream.writeBoolean(useChannel);
          outputStream.writeInt(channelNumber);
          */
     }
      catch (Exception ioe) {
        if (ConfigValues.DEBUG) {
          System.out.println("Exception at writeConfiguration");
          ioe.printStackTrace();
        }
      }

      writeRecordStore(RECORDSTORE_CONFIGURATION, baos);
    }

    //
    // Common functions
    //
    public static void deleteRecordStore(String targetStore) {
      try {
        RecordStore.deleteRecordStore(targetStore);
      }
      catch (Exception rsex) {
        if (ConfigValues.DEBUG) {
          rsex.printStackTrace();
        }
      }
    }

    public static void writeRecordStore(String targetStore, ByteArrayOutputStream baos) {
      RecordStore recordStore = null;

      // Extract the byte array
      byte[] b = baos.toByteArray();
      // Add it to the record store
      try {
        deleteRecordStore(targetStore);
        recordStore = RecordStore.openRecordStore(targetStore, true);
        recordStore.addRecord(b, 0, b.length);
        recordStore.closeRecordStore();
      }
      catch (Exception rsex) {
        if (ConfigValues.DEBUG) {
          rsex.printStackTrace();
        }
      }
    }

    public static DataInputStream readRecordStore(String targetStore) {
      RecordStore recordStore = null;
      ByteArrayInputStream bais = null;

      // Get configuration
      try {
        recordStore = RecordStore.openRecordStore(targetStore, true);
        RecordEnumeration storedConfiguration = recordStore.enumerateRecords(null, null, false);

        if (storedConfiguration.hasNextElement()) {
          int id = storedConfiguration.nextRecordId();
          bais = new ByteArrayInputStream(recordStore.getRecord(id));
        }
        recordStore.closeRecordStore();
      }
      catch (Exception rse) {
        if (ConfigValues.DEBUG) {
          rse.printStackTrace();
        }
      }
      if (bais != null) {
        return new DataInputStream(bais);
      } else {
        return null;
      }
    }

}
