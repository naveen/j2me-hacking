package com.hipoqih.plugin.s60_2nd.gps;

import java.util.*;
import javax.microedition.rms.*;
import javax.microedition.lcdui.List;

//Save String in a Record Store
//List of String from the record Store
//Load String from a Record Store
//Delete Record Store
//License: This library is under the GNU Lesser General Public License
//
//author Praplan Christophe
//author Velen Stephane
 //version 1.0 <p>Geneva,the 23.03.2006
class SaveAndLoad {

    private Vector vector1;
    private Vector vector2;
    private List list = new List("SaveAndLoad", List.EXCLUSIVE);
    private String recordStoreName;

    //Constructor
    //param name give the name of the record store.
    public SaveAndLoad(String name) {
        recordStoreName = new String(name);
        vector1 = new Vector();
        vector2 = new Vector();
    }

    //method to create a list of name of record from the recordStore specified
    //return the list of the record name
    //throws RecordStoreException
    public List showListRecordStore() throws RecordStoreException {
        list.deleteAll();
        vector1.removeAllElements();
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            //for each record
            while (re.hasNextElement()) {
                String save = null;
                //identify
                int id = re.nextRecordId();
                //take record
                String load = new String(rs.getRecord(id));
                int ind = load.indexOf(".");
                //nom fichier
                String nomfichier = (load.substring(0, ind).toString());
                list.append(nomfichier, null);
                //string contenu
                save = load.substring(ind + 1, load.length());
                load = save;
                vector1.addElement(load);
            }
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
        return list;
    }

    //load the content of the record.
    //param i user has to specify the index of the list
    //return a String of the record
    //throws RecordStoreException
    public String loadRecordStore(int i) throws RecordStoreException {
        return vector1.elementAt(i).toString();

    }

    //save the record.
    //param content content to save
    //param fileName name of the content to save
    //throws RecordStoreException
    public void saveRecordStore(String content, String fileName) throws RecordStoreException {
        //file name and content in the same string
        content = fileName + "." + content;
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            byte[] raw = content.getBytes();
            //save
            rs.addRecord(raw, 0, raw.length);
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }

    //delete before all record store and save the record.
    //param content content to save
    //param fileName name of the content to save
    //throws RecordStoreException
    public void deleteAndSaveRecordStore(String content, String fileName) throws RecordStoreException {
        content = fileName + "." + content;
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            //delete all record store
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }
            byte[] raw = content.getBytes();
            //save new record store
            rs.addRecord(raw, 0, raw.length);
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }

    //create a vector to contain the id of each record
    //use to load or to delete a specific record
    //return the id vector
    //throws RecordStoreException
    public Vector idRecordStore() throws RecordStoreException {
        vector2.removeAllElements();
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            // add the vector
            while (re.hasNextElement()) {
                vector2.addElement(Integer.toString(re.nextRecordId()));
            }
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
        return vector2;
    }

    //delete a record
    //param i number of index from the list of name
    //throws RecordStoreException
    public void deleteRecordStore(int i) throws RecordStoreException {
        idRecordStore();
        //take id from the vector of idRecordStore method
        String save = vector2.elementAt(i).toString();
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            //delete record
            rs.deleteRecord(Integer.parseInt(save));
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }

    //delete all record from the record store
    //throws RecordStoreException
    public void deleteallRecordStore() throws RecordStoreException {
        RecordStore rs = null;
        RecordEnumeration re = null;
        try {
            rs = RecordStore.openRecordStore(recordStoreName, true);
            re = rs.enumerateRecords(null, null, false);
            //delete all record of the specific record store
            while (re.hasNextElement()) {
                int id = re.nextRecordId();
                rs.deleteRecord(id);
            }
        } finally {
            if (re != null) re.destroy();
            if (rs != null) rs.closeRecordStore();
        }
    }
}
