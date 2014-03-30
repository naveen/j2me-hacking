// Copyright 2005 Nokia Corporation.
//
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER,
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED
// BY THIRD PARTIES
//
// Furthermore, information provided in this source code is preliminary,
// and may be changed substantially prior to final release. Nokia Corporation
// retains the right to make changes to this source code at
// any time, without notice. This source code is provided for informational
// purposes only.
//
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
//
// A non-exclusive, non-transferable, worldwide, limited license is hereby
// granted to the Licensee to download, print, reproduce and modify the
// source code. The licensee has the right to market, sell, distribute and
// make available the source code in original or modified form only when
// incorporated into the programs developed by the Licensee. No other
// license, express or implied, by estoppel or otherwise, to any other
// intellectual property rights is granted herein.
package com.nokia.example.location.tourist;

/**
 * A container class for general purpose utility method(s).
 */
public class Utils
{
    /**
     * Creates a String presentation of double value that is formatted to have a
     * certain number of decimals. Formatted output value does not include any
     * rounding rules. Output value is just truncated on the place that is
     * defined by received <i>decimals</i> parameter.
     * 
     * @param value -
     *            double value to be converted.
     * @param decimals -
     *            number of decimals in the String presentation.
     * @return a string representation of the argument.
     */
    public static String formatDouble(double value, int decimals)
    {
        String doubleStr = "" + value;
        int index = doubleStr.indexOf(".") != -1 ? doubleStr.indexOf(".")
                : doubleStr.indexOf(",");
        // Decimal point can not be found...
        if (index == -1) return doubleStr;
        // Truncate all decimals
        if (decimals == 0)
        {
            return doubleStr.substring(0, index);
        }

        int len = index + decimals + 1;
        if (len >= doubleStr.length()) len = doubleStr.length();

        double d = Double.parseDouble(doubleStr.substring(0, len));
        return String.valueOf(d);
    }
}
