// Updates: 2006.01.13


/**
 * The listener interface for receiving FileIO events.
 *  
 * @author Michel Deriaz
 */

package com.hipoqih.plugin.s60_2nd.gps.tracker;

public interface FileIOListener {

     /**
     * The operation finished successfully.
     */
    public static final int STATE_OK = 0;

    /**
     * A problem occured during the operation.
     */
    public static final int STATE_PROBLEM = 1;
    
    /**
     * Called when the operation launched by the <code>tracker.saveCoordinates</code> method
     * ends up.
     *
     * @param state describes how the operation ended up. Can be
     *              <code>STATE_OK</code> or  <code>STATE_PROBLEM</code>.
     */
    public void fileWritten(int state);

}