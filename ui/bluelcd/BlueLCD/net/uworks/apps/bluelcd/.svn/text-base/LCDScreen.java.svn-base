package net.uworks.apps.bluelcd;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import java.io.IOException;

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
public class LCDScreen extends Canvas implements CommandListener {
    private Image charImage;
    private Image[] customCharImages;

    private int charWidth;
    private int charHeight;
    private RFCommServer commServer;
    private String debugString = ".";
    private byte[][] screenInfo;
    private int currentX, currentY;

    private boolean readingCommand = false;
    private boolean readingArgument = false;
    private int currentCommand;
    private int[] commandArgument;
    private int argumentLeft;

    private Image imageBuffer;
    private Graphics imageBufferGraphics;
    private int boardOffsetX, boardOffsetY;
    private int boardWidth, boardHeight;
    private boolean autoWrap = false;
    private boolean autoScroll = false;

    private Command settingsCommand = new Command("Settings", Command.SCREEN, 1);
    private Command aboutCommand = new Command("About", Command.HELP, 2);
    private Command exitCommand = new Command("Exit", Command.EXIT, 3);
    private Command closeCommand = new Command("Close", Command.BACK, 4);

    private OnScreenMenu menu;
    private boolean showingMenu = false;

    private boolean underlineCursor = false;
    private boolean blockCursor = false;



    private CharTable charTable;
// Custom character location optimization (disabled for now)
//    private Vector[] customPoints;

    public LCDScreen() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Custom character location optimization (disabled for now)
        // Init custom points vectors
        //customPoints = new Vector[8];
        //for (int c=0; c<customPoints.length; c++) {
        //    customPoints[c] = new Vector();
        //}

        // Initialize full screen menu
        menu = new OnScreenMenu(this, this);
        menu.addOption(settingsCommand);
        menu.addOption(aboutCommand);
        menu.addOption(exitCommand);
        menu.addOption(closeCommand);

        this.setFullScreenMode(ConfigValues.useFullScreen);

        checkCommands();

        initializeScreen();

        commServer = new RFCommServer(this);
        commServer.startServer();
    }

    private void jbInit() throws Exception {
        // Set up this Displayable to listen to command events
        setCommandListener(this);

    }

    public void stopCommServer() {
        commServer.stopServer();
    }

    public void initializeScreen() {
        charWidth = 5;
        charHeight = 8;

        // Init custom chars
        customCharImages = new Image[8];

        try {
            charImage = Image.createImage("/chars/" + ConfigValues.currentCharTable);
        } catch (IOException ioex) {}

        charWidth = charImage.getWidth() / 16;
        charHeight = charImage.getHeight() / 16;

        if (ConfigValues.screenTransformation == ConfigValues.SCREEN_LANDSCAPE) {
            // We're rotated
            Image tempCharImage = Image.createImage(charImage.getHeight(), charImage.getWidth());
            tempCharImage.getGraphics().drawRegion(charImage, 0, 0, charImage.getWidth(), charImage.getHeight(), Sprite.TRANS_ROT270, 0,0, 0);
            charImage = tempCharImage;
        }

        charTable = new CharTable();
/*
        for (int c = 0; c < 16; c++) {
            for (int d = 0; d < 16; d++) {
                charTable.drawChar(charImageGraphics, c * 16 + d, c * charWidth,
                                   d * charHeight);
            }
        }
*/
        screenInfo = new byte[ConfigValues.totalLines][ConfigValues.
                     charsPerLine];

        // Init array to spaces (char 32)
        for (int c=0; c<ConfigValues.totalLines; c++) {
            for (int d=0; d<ConfigValues.charsPerLine; d++) {
                screenInfo[c][d] = 0x20;
            }
        }


        currentX = 0;
        currentY = 0;

        boardWidth = ConfigValues.charsPerLine * (charWidth + 1) + 1;
        boardHeight = ConfigValues.totalLines * (charHeight + 1) + 1;

        createNewBoard(getWidth(), getHeight());
    }

    public void checkCommands() {
        if (ConfigValues.useFullScreen) {
            removeCommand(settingsCommand);
            removeCommand(aboutCommand);
            removeCommand(exitCommand);
        } else {
            addCommand(settingsCommand);
            addCommand(aboutCommand);
            addCommand(exitCommand);
        }
    }

    public void createNewBoard(int width, int height) {
        imageBuffer = Image.createImage(width, height);
        if (ConfigValues.screenTransformation == ConfigValues.SCREEN_NORMAL) {
            boardOffsetX = (width - boardWidth) / 2;
            boardOffsetY = (height - boardHeight) / 2;
        } else {
            // We're rotated...
            boardOffsetX = (width - boardHeight) / 2;
            boardOffsetY = (height - boardWidth) / 2 + boardWidth;
        }
        imageBufferGraphics = imageBuffer.getGraphics();
        drawEmpty();

        // Draw current info
        for (int c=0; c<screenInfo.length; c++) {
            for (int d=0; d<screenInfo[c].length; d++) {
                drawCharacter(c, d, screenInfo[c][d]);
            }
        }
    }

    public void sizeChanged(int width, int height) {
        createNewBoard(width, height);

        repaint();
    }

    public void drawEmpty() {
        imageBufferGraphics.setColor(ConfigValues.backgroundColor);
        imageBufferGraphics.fillRect(0, 0, imageBuffer.getWidth(),
                                         imageBuffer.getHeight());

        // Draw border...
        imageBufferGraphics.setColor(ConfigValues.foregroundColor);
        if (ConfigValues.screenTransformation == ConfigValues.SCREEN_NORMAL) {
            imageBufferGraphics.drawRect(boardOffsetX - 2, boardOffsetY - 2,
                                         boardWidth + 1,
                                         boardHeight + 1);
        } else {
            // We're rotated
            imageBufferGraphics.drawRect(boardOffsetX - 2, boardOffsetY - 2 - boardWidth,
                                         boardHeight + 1,
                                         boardWidth + 1);
        }
    }

    public void aboutToConnect() {
        repaint();
    }

    public void connected() {
        repaint();
    }

    public void readed(int readedByte) {
        if (readingArgument) {
            commandArgument[commandArgument.length - argumentLeft] = readedByte;
            argumentLeft--;
            if (argumentLeft == 0) {
                processCommand();
                readingArgument = false;
                readingCommand = false;
            }
        } else if (readingCommand) {
            currentCommand = readedByte;
            int argumentLength = 0;
            switch (currentCommand) {
            case 80:
            case 66:
            case 153:
                argumentLength = 1;
                break;
            case 71:
            case 61:
            case 35:
                argumentLength = 2;
                break;
            case 111:
                argumentLength = 3;
                break;
            case 124:
                argumentLength = 4;
                break;
            case 78:
                argumentLength = 9;
                break;
            }
            if (argumentLength == 0) {
                processCommand();
                readingCommand = false;
            } else {
                readingArgument = true;
                argumentLeft = argumentLength;
                commandArgument = new int[argumentLength];
            }
        } else if (readedByte == 254) {
            // New command
            readingCommand = true;
        } else {
            // Add character!
            setCharacter(currentY, currentX, readedByte);
            // Advance
            if (currentX < (ConfigValues.charsPerLine - 1)) {
                currentX++;
            } else if (autoWrap) {
                if (currentY < (ConfigValues.totalLines - 1)) {
                    currentX = 0;
                    currentY++;
                } else if (autoScroll) {
                    for (int c = 0; c < screenInfo.length; c++) {
                        for (int d = 0; d < screenInfo[c].length; d++) {
                            if (c < (screenInfo.length - 1)) {
                                setCharacter(c,d, screenInfo[c+1][d]);
                            } else {
                                setCharacter(c, d, 0);
                            }
                        }
                    }
                    currentX = 0;
                } else {
                    currentX = 0;
                    currentY = 0;
                }
            }
            repaint();
        }
    }

    public void processCommand() {
        switch (currentCommand) {
        case 67:

            // Auto line wrap on
            autoWrap = true;
            break;
        case 68:

            // Auto line wrap off
            autoWrap = false;
            break;
        case 81:

            // Auto scroll on
            autoScroll = true;
            break;
        case 82:

            // Auto scroll off
            autoScroll = false;
            break;
        case 71:

            // Set cursor position
            currentX = commandArgument[0] - 1;
            currentY = commandArgument[1] - 1;
            break;
        case 72:

            // Send cursor home
            currentX = 0;
            currentY = 0;
            break;
        case 74:

            // Turn on underline cursor
            underlineCursor = true;
            break;
        case 75:

            // Turn off underline cursor
            underlineCursor = false;
            break;
        case 83:

            // Turn on block (blinking) cursor
            blockCursor = true;
            break;
        case 84:

            // Turn off block (blinking) cursor
            blockCursor = false;
            break;
        case 76:

            // Cursor left
            if (currentX > 0) {
                currentX--;
            } else if (currentY > 0) {
                currentY--;
                currentX = ConfigValues.charsPerLine - 1;
            } else {
                currentY = ConfigValues.totalLines - 1;
                currentX = ConfigValues.charsPerLine - 1;
            }
            break;
        case 77:

            // Cursor right
            if (currentX < (ConfigValues.charsPerLine - 1)) {
                currentX++;
            } else if (currentY < (ConfigValues.totalLines - 1)) {
                currentX = 0;
                currentY++;
            } else {
                currentX = 0;
                currentY = 0;
            }
        case 118:

            // Initialize wide vertical bar graph
            loadCustomChars(CharTable.VERTICALBAR_CHARS);
            break;
        case 115:

            // Initialize narrow vertical bar graph
            loadCustomChars(CharTable.VERTICALNARROWBAR_CHARS);
            break;
        case 61:
        {
            // Draw vertical bar graph
            int targetColumn = commandArgument[0];
            int lengthPixels = commandArgument[1];
            for (int c = (screenInfo.length - 1); c >= 0; c++) {
                if (lengthPixels > 7) {
                    lengthPixels -= 9;
                    screenInfo[c][targetColumn] = 0x07;
                } else if (lengthPixels > 0) {
                    screenInfo[c][targetColumn] = (byte) (lengthPixels - 1);
                    lengthPixels = 0;
                } else {
                    screenInfo[c][targetColumn] = 0x20;
                }
            }
        }
            break;
        case 104:

            // Initialize horizontal bar graph
            loadCustomChars(CharTable.HORIZONTALBAR_CHARS);
            break;
        case 124:
        {
            // Draw horizontal bar graph
            int targetColumn = commandArgument[0];
            int targetRow = commandArgument[1];
            boolean goRight = (commandArgument[2] == 0);
            int lengthPixels = commandArgument[3];

            while ((lengthPixels > 0) && (targetColumn < screenInfo[targetRow].length) && (targetColumn >= 0)) {
                if (lengthPixels >= 5) {
                    lengthPixels -= 6;
                    screenInfo[targetRow][targetColumn] = 0x04;
                    if (goRight) {
                        targetColumn++;
                    } else {
                        targetColumn--;
                    }
                } else {
                    if (goRight) {
                        screenInfo[targetRow][targetColumn] = (byte)(lengthPixels - 1);
                    } else {
                        screenInfo[targetRow][targetColumn] = (byte)(lengthPixels + 4);
                        if (screenInfo[targetRow][targetColumn] > 0x07) {
                            screenInfo[targetRow][targetColumn] = 0x07;
                        }
                    }
                    lengthPixels = 0;
                }
            }
        }
            break;
            case 109:
                // Initialize medium digits
                // TODO
                break;
            case 110:

            // Initialize large digits
            // TODO
            break;
        case 35:

            // Place large digit
            // TODO
            break;
        case 111:

            // Place medium digit
            // TODO
            break;
        case 78:
            // Define custom character
            int targetChar = commandArgument[0];
            byte[] newChar = new byte[8];
            for (int c=0; c<8; c++) {
                newChar[c] = (byte)commandArgument[c+1];
            }

            processCustomChar(targetChar, newChar);

            // Custom character location optimization (disabled for now)
            //if (customPoints[targetChar].size() > 0) {
            //    Enumeration customPointList = customPoints[targetChar].elements();
            //    while (customPointList.hasMoreElements()) {
            //        LCDPoint currentPoint = (LCDPoint)customPointList.nextElement();
            //        drawCharacter(currentPoint.line,currentPoint.col, targetChar);
            //    }
            //    repaint();
            //}
          break;
        case 88:

            // Clear display
            for (int c = 0; c < screenInfo.length; c++) {
                for (int d = 0; d < screenInfo[c].length; d++) {
                    setCharacter(c, d, 32);
                }
            }
            currentX = 0;
            currentY = 0;
            repaint();
            break;
        case 80:

            // Set Contrast
            // TODO
            break;
        case 66:

            // Backlight on
            // TODO
            break;
        case 70:

            // Backlight off
            // TODO
            break;
        case 86:

            // General purpose output off
            // TODO
            break;
        case 153:

            // Set backlight brightness
            // TODO
            break;
        case 87:

            // General purpose output on
            // TODO:
            break;
            // case ???:
            // Read module type
            // TODO
            // break;

        }

    }

    public void loadCustomChars(byte[][] charArray) {
        for (int c=0; c<charArray.length; c++) {
            processCustomChar(c, charArray[c]);
        }
    }

    public void processCustomChar(int targetChar, byte[] newChar) {
        charTable.setCustomChar(targetChar, newChar);
//        int col = targetChar / 16;
//        int row = targetChar % 16;

        Image tempCharImage = Image.createImage(5,8);

        charTable.drawChar(tempCharImage.getGraphics(), targetChar, 0,
                           0);

        if ((charWidth != 5) || (charHeight != 8)) {
            customCharImages[targetChar] = scale(tempCharImage, charWidth, charHeight);
        } else {
            customCharImages[targetChar] = tempCharImage;
        }

        if (ConfigValues.screenTransformation == ConfigValues.SCREEN_LANDSCAPE) {
                // We're rotated
                Image tempRotatedImage = Image.createImage(customCharImages[targetChar].getHeight(), customCharImages[targetChar].getWidth());
                tempRotatedImage.getGraphics().drawRegion(customCharImages[targetChar], 0, 0, customCharImages[targetChar].getWidth(), customCharImages[targetChar].getHeight(), Sprite.TRANS_ROT270, 0,0, 0);
                customCharImages[targetChar] = tempRotatedImage;
        }

        // Refresh current drawn characters
        boolean foundExisting = false;
        for (int c=0; c<screenInfo.length; c++) {
            for (int d=0; d<screenInfo[c].length; d++) {
                if (screenInfo[c][d] == targetChar) {
                    drawCharacter(c,d, targetChar);
                    foundExisting = true;
                }
            }
        }
        if (foundExisting) {
            repaint();
        }
    }

    public void setCharacter(int line, int col, int character) {
        if (screenInfo[line][col] != character) {

            // Custom character location optimization (disabled for now)
            //if (screenInfo[line][col] < 8) {
                // Remove from custom points
            //    customPoints[screenInfo[line][col]].removeElement(new LCDPoint(line, col));
            //}

            // Update info
            screenInfo[line][col] = (byte) character;

            // Custom character location optimization (disabled for now)
            //if (character < 8) {
            //    customPoints[character].addElement(new LCDPoint(line, col));
            //}

            // Paint in buffer
            drawCharacter(line, col, character);
        }
/*
        imageBufferGraphics.drawRegion(charImage, (character / 16) * charWidth, (character % 16) * charHeight, charWidth, charHeight,
                     0, boardOffsetX + col * (charWidth + 1),
                     boardOffsetY + line * (charHeight + 1), 0);
*/
    }

    public void drawCharacter(int line, int col, int character) {
        if (ConfigValues.screenTransformation == ConfigValues.SCREEN_NORMAL) {
            if ((character >= 8) || (customCharImages[character] == null)) {
                imageBufferGraphics.drawRegion(charImage,
                                               (character / 16) * charWidth,
                                               (character % 16) * charHeight,
                                               charWidth, charHeight,
                                               0,
                                               boardOffsetX +
                                               col * (charWidth + 1),
                                               boardOffsetY +
                                               line * (charHeight + 1), 0);
            } else {
                // Draw custom char
                imageBufferGraphics.drawImage(customCharImages[character],
                                              boardOffsetX +
                                              col * (charWidth + 1),
                                              boardOffsetY +
                                              line * (charHeight + 1), 0);
            }
        } else {
            if ((character >= 8) || (customCharImages[character] == null)) {
                imageBufferGraphics.drawRegion(charImage,
                                               (character % 16) * charHeight,
                                               (15 - character / 16) * charWidth,
                                               charHeight, charWidth,
                                               0,
                                               boardOffsetX +
                                               line * (charHeight + 1),
                                               boardOffsetY +
                                               (ConfigValues.charsPerLine - col - 1) * (charWidth + 1) - boardWidth, 0);
            } else {
                // Draw custom char
                imageBufferGraphics.drawImage(customCharImages[character],
                                              boardOffsetX +
                                               line * (charHeight + 1),
                                               boardOffsetY +
                                               (ConfigValues.charsPerLine - col - 1) * (charWidth + 1) - boardWidth, 0);
            }
        }
    }

    public void keyPressed(int keyCode) {
        if (showingMenu) {
            menu.keyPressed(keyCode);
        } else {
            // TODO: See if we've pressed any key we should send back to the PC. If not, show the menu
            if (ConfigValues.useFullScreen) {
                showingMenu = true;
                menu.updateMenu = false;
                repaint();
            }
        }
    }

    public void keyReleased(int key) {

    }

    public void keyRepeated(int key) {

    }

    public void pointerPressed(int x, int y) {
        if (showingMenu) {
            menu.pointerPressed(x, y);
        } else {
            // TODO: See if we've pressed any virtual key. If not, we show the menu
            if (ConfigValues.useFullScreen) {
                showingMenu = true;
                menu.updateMenu = false;
                repaint();
            }
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        /** @todo Add command handling code */
        if (command.getCommandType() == Command.EXIT) {
            // stop the MIDlet
            BlueLCD.quitApp();
        } else if (command == settingsCommand) {
            // Hide menu
            showingMenu = false;
            // Show app settings
            AppSettings settingsScreen = new AppSettings(this);
            Display.getDisplay(BlueLCD.instance).setCurrent(settingsScreen);
        } else if (command == aboutCommand) {
            AboutScreen aboutScreen = new AboutScreen(this);
            Display.getDisplay(BlueLCD.instance).setCurrent(aboutScreen);
        } else if (command == closeCommand) {
            showingMenu = false;
            repaint();
        }
    }

    protected void paint(Graphics g) {
        if (showingMenu && menu.updateMenu) {
            menu.drawMenu(g);
            menu.updateMenu = false;
        } else {
            g.drawImage(imageBuffer, 0, 0, 0);

            if (showingMenu) {
                menu.drawMenu(g);
            }
        }
    }

    public static Image scale(Image src, int width, int height)
            {
            int scanline = src.getWidth();
            int srcw = src.getWidth();
            int srch = src.getHeight();
            int buf[] = new int[srcw * srch];
            src.getRGB(buf, 0, scanline, 0, 0, srcw, srch);
            int buf2[] = new int[width*height];
            for (int y=0;y<height;y++)
                    {
                    int c1 = y*width;
                    int c2 = (y*srch/height)*scanline;
                    for (int x=0;x<width;x++)
                            {
                            buf2[c1 + x] = buf[c2 + x*srcw/width];
                            }
                    }
            Image img = Image.createRGBImage(buf2, width, height, true);
            return img;
		}
}
