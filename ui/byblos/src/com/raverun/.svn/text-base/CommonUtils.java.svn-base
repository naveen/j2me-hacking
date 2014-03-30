/*
   Copyright 2006-2007 Gavin Bong

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.raverun;

import javax.microedition.lcdui.Font;

/**
 * Central place where utilities common to all components reside
 *
 * @author Gavin Bong gavin.emploi@gmail.com
 */
public class CommonUtils {

    /**
     * <ul>
     * <li>small &lt; 130
     * <li>medium &lt; 200
     * <li>large &gt; 200
     * </ul> 
     *
     * @param dimension either height or width
     * @return value to represent "small", "medium" or "large" screens
     */
    public static int sizing( int dimension )
    {        
        if ( dimension >= 60 && dimension <= 130 )
        {
            return 2; // small
        }
        else if ( dimension >= 131 && dimension <= 200 )
        {
            return 3; // medium
        }
        else if ( dimension >= 201 )
        {
            return 4; // large
        }
        else
            return 0; // undefined
    }

    /**
     * @param percentage should be <= 100
     * @return percentage of dimension 
     */
    public static int normalize( int dimension, int percentage )
    {
        return (percentage * dimension)/100;        
    }    
    
    public final static Font  BOLD_MEDIUM = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    public final static Font PLAIN_MEDIUM = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    
    private CommonUtils() 
    {
    }
}
