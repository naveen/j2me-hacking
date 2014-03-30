package com.hipoqih.plugin.s60_2nd.gps;

/**
 * This class records some events that happend in the API (connections found and lost, file written, fix found and lost)
 * so you can have an idea of what is happening inside the API. A debug string can always be obtained so it is possible
 * to have basic informations when the API is running on a cell phone.
 * Use setLevel to set the debug level wich will be printed in the console. Default level for this is NONE.
 * NORMAL level construc a debug string with basic informations wich can be obtained by the getDebug method.
 * DETAIL level will construct the debug string and print more informations in the console.
 * <p>License: This library is under the GNU Lesser General Public License</p>
 *
 * @author Praplan Christophe
 * @author Velen Stephane
 * @version 1.0 <p>Geneva, the 23.03.2006
 */
public abstract class Debug {
    private static String debug= new String();
    private static int level=0;
    /**
     * No debug.
     */
    public static final int NONE=0;
    /**
     * A debug string with basic informations is be created.
     */
    public static final int NORMAL=1;
    /**
     * More informations will be printed in the console when using the DETAIL level.
     */
    public static final int DETAIL=2;

    /**
     * Used by the API to construct the debug string. Users should not use this.
     * @param s The debug string
     */
    public static void setDebug(String s,int lvl){
        if (lvl==NORMAL)debug+=s;
        //print the appropriates debug
        if (level==NORMAL && lvl==NORMAL) System.err.println("Normal Debug : " +s);
        else if (level==DETAIL && lvl==NORMAL) System.err.println("Normal Debug : " +s);
        else if (level==DETAIL && lvl==DETAIL) System.err.println("Detailed Debug : " +s);

    }

    /**
     * Set the level of debug. Debug will be printed in the console.
     * @param lvl Use <code>Debug.NONE</code>, <code>Debug.NORMAL</code> or <code>Debug.DETAIL</code> to set the level of debug.
     */
    public static void setLevel(int lvl){
      level=lvl;
    }

    /**
     * This method returns the debug string. Since it is only a <code>String</code>, it can be used to debug when running the API
     * on a cell phone.
     * @return The debug string.
     */
     public static String getDebug(){
        return debug;
    }
}
