/*
 * History.java
 *
 * Created on 28. èerven 2004, 11:42
 */

package mobile;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.microedition.midlet.*;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author  iz148508
 * @version
 */
public class History {
    
    private static final String RS_NAME="mobile.history";
    public static final int MAX_SIZE=5;
    private String[] history;
    
    private int size;
    private int position;
    private static  History instance;
    private static RecordStore rs;
    
    
    private History() {
        history=new String[MAX_SIZE];
        try {
            rs=RecordStore.openRecordStore(RS_NAME,  true);
        }
        catch (RecordStoreException e){
            e.printStackTrace();
        }
        
        size=0;
        position=0;
        restoreFromRS();
    }
    
    public static History getHistory() {
        if (instance == null) instance = new History();
        return instance;
    }
    
    public synchronized void storeToRS() {
        
        RecordEnumeration re=null;
        try {
            
            Enumeration e= this.elements();
            while (e.hasMoreElements()) {
                byte[] buf= ((String) e.nextElement()).getBytes();
                rs.addRecord(buf, 0, buf.length );
            
            }
        }
        catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }
    
    private void restoreFromRS() {
        
        try {
        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        while (re.hasNextElement()) {
            int id = re.nextRecordId();
            this.addItem(new String(rs.getRecord(id)));
            rs.deleteRecord(id);
        }
        }
        catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public int addItem(String item) {
        if (contains(item)) return -1;
        history[position]= item;
        int cp= position;
        position=(++position) % MAX_SIZE;
        if (size<MAX_SIZE) size++;
        return cp;
    }
    
    public int getSize() {
        return size;
    }
    
    public String getItem(int pos) {
        if (pos>= MAX_SIZE || pos<0) throw new IllegalArgumentException("Index out of range");
        return history[pos];
    }
    
    public boolean contains(String s) {
        if (s==null) return true;
        for (int i=0; i<size; i++) {
            if (s.equals(history[i])) return true;
        }
        return false;
    }
    public Enumeration elements() {
        return new HistoryEnum(position, size);
    }
    
    class HistoryEnum implements Enumeration {
        
        int count;
        int pos;
        public HistoryEnum(int pos, int size) {
            count = size;
            this.pos=pos;
            if (this.pos==0) this.pos=MAX_SIZE-1;
            else this.pos--;
        }
        
        public boolean hasMoreElements() {
            return (count>0);
            
        }
        
        public Object nextElement() {
            if (count>0) {
                String s =  history[pos];
                pos--;
                if (pos<0) pos = MAX_SIZE-1;
                count--;
                return s;
            }
            else
                throw new NoSuchElementException();
        }
        
    }
    
    
    
    
}
