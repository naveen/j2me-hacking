package com.j2medeveloper.util;

/* -----------------------------------------------------------------------------
 * URL Utils - UrlUtils.java
 * Author: C. Enrique Ortiz
 * Copyright (c) 2004-2005 C. Enrique Ortiz 
 *
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 *
 * Usage & redistributions of source code must retain the above copyright notice.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should get a copy of the GNU Lesser General Public License from
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * -----------------------------------------------------------------------------
 */
public class UrlUtils {

    // Unreserved punctuation mark/symbols
    private static String mark = "-_.!~*'()\"";

    /**
     * Converts Hex digit to a UTF-8 "Hex" character
     * @param digitValue digit to convert to Hex
     * @return the converted Hex digit
     */
    static private char toHexChar(int digitValue) {
        if (digitValue < 10)
            // Convert value 0-9 to char 0-9 hex char
            return (char)('0' + digitValue);
        else
            // Convert value 10-15 to A-F hex char
            return (char)('A' + (digitValue - 10));
    }

    /**
     * Encodes a URL - This method assumes UTF-8
     * @param url URL to encode
     * @return the encoded URL
     */
    static public String encodeURL(String url) {
        StringBuffer encodedUrl = new StringBuffer(); // Encoded URL
        int len = url.length();
        // Encode each URL character
        for(int i = 0; i < len; i++) {
            char c = url.charAt(i); // Get next character
            if ((c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z'))
                // Alphanumeric characters require no encoding, append as is
                encodedUrl.append(c);
            else {
                int imark = mark.indexOf(c);
                if (imark >=0) {
                    // Unreserved punctuation marks and symbols require
                    //  no encoding, append as is
                    encodedUrl.append(c);
                } else {
                    // Encode all other characters to Hex, using the format "%XX",
                    //  where XX are the hex digits
                    encodedUrl.append('%'); // Add % character
                    // Encode the character's high-order nibble to Hex
                    encodedUrl.append(toHexChar((c & 0xF0) >> 4));
                    // Encode the character's low-order nibble to Hex
                    encodedUrl.append(toHexChar (c & 0x0F));
                }
            }
        }
        return encodedUrl.toString(); // Return encoded URL
    }
}
