package net.uworks.apps.bluelcd;

import javax.microedition.lcdui.*;

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
public class CharTable {
    final private static byte[] NULL_CHAR = { 0,0,0,0,0,0,0,0 };

    public static byte[][] HORIZONTALBAR_CHARS = {
                                           { 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10 },
                                           { 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18, 0x18 },
                                           { 0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x1C, 0x1C },
                                           { 0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x1E, 0x1E },
                                           { 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F },
                                           { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 },
                                           { 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03, 0x03 },
                                           { 0x07, 0x07, 0x07, 0x07, 0x07, 0x07, 0x07, 0x07 }
    };

    public static byte[][] VERTICALBAR_CHARS = {
                                           { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1F },
                                           { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x1F },
                                           { 0x00, 0x00, 0x00, 0x00, 0x00, 0x1F, 0x1F, 0x1F },
                                           { 0x00, 0x00, 0x00, 0x00, 0x1F, 0x1F, 0x1F, 0x1F },
                                           { 0x00, 0x00, 0x00, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F },
                                           { 0x00, 0x00, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F },
                                           { 0x00, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F },
                                           { 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F }
    };

    public static byte[][] VERTICALNARROWBAR_CHARS = {
                                                     { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x06 },
                                                     { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x06 },
                                                     { 0x00, 0x00, 0x00, 0x00, 0x00, 0x06, 0x06, 0x06 },
                                                     { 0x00, 0x00, 0x00, 0x00, 0x06, 0x06, 0x06, 0x06 },
                                                     { 0x00, 0x00, 0x00, 0x06, 0x06, 0x06, 0x06, 0x06 },
                                                     { 0x00, 0x00, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06 },
                                                     { 0x00, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06 },
                                                     { 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06, 0x06 }
    };


    private byte[][] customChars = { NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
                                 NULL_CHAR,
    };

    public CharTable() {
    }

    public void setCustomChar(int number, byte[] data) {
        customChars[number] = data;
    }

    public void drawChar(Graphics targetGraphics, int charNumber, int offsetX, int offsetY) {
        byte[] targetChar = NULL_CHAR;

        if ((charNumber >= 0) && (charNumber <= 7)) {
            targetChar = customChars[charNumber];
        }

        targetGraphics.setColor(ConfigValues.textBackgroundColor);
        targetGraphics.fillRect(offsetX, offsetY, 5, targetChar.length);
            for (int c = 0; c < targetChar.length; c++) {
                byte currentLine = targetChar[c];
                int currentPos = 4;
                for (int d = 1; d < 32; d <<= 1) {
                    if ((currentLine & d) > 0) {
                        targetGraphics.setColor(ConfigValues.textForegroundColor);
                        targetGraphics.fillRect(offsetX + currentPos, offsetY + c, 1, 1);
                    }
                    currentPos--;
                }
            }
    }
}

