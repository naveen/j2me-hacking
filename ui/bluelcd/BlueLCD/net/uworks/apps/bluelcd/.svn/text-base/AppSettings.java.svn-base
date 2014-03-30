package net.uworks.apps.bluelcd;

import javax.microedition.lcdui.*;
import java.util.Vector;

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

public class AppSettings extends Form implements CommandListener {
  private LCDScreen mainScreen;
  private ChoiceGroup binarySettings = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);
  //private TextField channelField = new TextField("Columns", "", 3, TextField.NUMERIC);
  private TextField columnsField = new TextField("Columns", "", 3, TextField.NUMERIC);
  private TextField rowsField = new TextField("Rows", "", 3, TextField.NUMERIC);
  private TextField foregroundColorField = new TextField("Foreground Color", "", 6, TextField.ANY);
  private TextField backgroundColorField = new TextField("Background Color", "", 6, TextField.ANY);
  private TextField textForegroundColorField = new TextField("Text Foreground Color", "", 6, TextField.ANY);
  private TextField textBackgroundColorField = new TextField("Text Background Color", "", 6, TextField.ANY);
  private ChoiceGroup screenTransformationField = new ChoiceGroup("Screen Type", ChoiceGroup.EXCLUSIVE);
  private ChoiceGroup charsetField = new ChoiceGroup("Charset", ChoiceGroup.POPUP);

  private final int INDEX_WANTFULLSCREEN = 0;
  //private final int INDEX_FORCECHANNEL = 1;
  private final int INDEX_WANTBACKLIGHT = 1;

  private final int NORMAL = 0;
  private final int LANDSCAPE = 1;
  private Vector charsets = new Vector();

  /**Construct the displayable*/
  public AppSettings(LCDScreen mainScreen) {
    super("Settings");

    // Store parent
    this.mainScreen = mainScreen;

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**Component initialization*/
  private void jbInit() throws Exception {
      binarySettings.append("Full screen", null);
      //binarySettings.append("Force channel", null);
    if (ConfigValues.USE_NOKIA_EXT) {
        binarySettings.append("Backlight", null);
    }

    binarySettings.setSelectedIndex(INDEX_WANTFULLSCREEN, ConfigValues.useFullScreen);
    //binarySettings.setSelectedIndex(INDEX_FORCECHANNEL, ConfigValues.useChannel);
    if (ConfigValues.USE_NOKIA_EXT) {
      binarySettings.setSelectedIndex(INDEX_WANTBACKLIGHT, ConfigValues.useBacklight);
    }

    screenTransformationField.append("Normal", null);
    screenTransformationField.append("Landscape", null);
    // Set default
    switch (ConfigValues.screenTransformation) {
    case ConfigValues.SCREEN_NORMAL:
        screenTransformationField.setSelectedIndex(NORMAL, true);
        break;
    default:
        screenTransformationField.setSelectedIndex(LANDSCAPE, true);
        break;
    }

    int currentCharset = 1;
    String currentCharsetProperty = null;
    int targetIndex = -1;
    while ((currentCharsetProperty = BlueLCD.instance.getAppProperty("Chars-" + currentCharset)) != null) {
        String charset = currentCharsetProperty.substring(0, currentCharsetProperty.indexOf(","));
        String description = currentCharsetProperty.substring(currentCharsetProperty.indexOf(",") + 1).trim();
        charsets.addElement(charset);
        charsetField.append(description, null);
        if (charset.equals(ConfigValues.currentCharTable)) {
            targetIndex = currentCharset - 1;
        }

        currentCharset++;
    }

    if (targetIndex > -1) {
        charsetField.setSelectedIndex(targetIndex, true);
    }

    //channelField.setString("" + ConfigValues.channelNumber);

    columnsField.setString("" + ConfigValues.charsPerLine);
    rowsField.setString("" + ConfigValues.totalLines);

    String foregroundColorValue = Integer.toHexString(ConfigValues.foregroundColor);
    while (foregroundColorValue.length() < 6) {
        foregroundColorValue = "0" + foregroundColorValue;
    }
    foregroundColorField.setString(foregroundColorValue);

    String backgroundColorValue = Integer.toHexString(ConfigValues.backgroundColor);
    while (backgroundColorValue.length() < 6) {
        backgroundColorValue = "0" + backgroundColorValue;
    }
    backgroundColorField.setString(backgroundColorValue);

    String textForegroundColorValue = Integer.toHexString(ConfigValues.textForegroundColor);
    while (textForegroundColorValue.length() < 6) {
        textForegroundColorValue = "0" + textForegroundColorValue;
    }
    textForegroundColorField.setString(textForegroundColorValue);

    String textBackgroundColorValue = Integer.toHexString(ConfigValues.textBackgroundColor);
    while (textBackgroundColorValue.length() < 6) {
        textBackgroundColorValue = "0" + textBackgroundColorValue;
    }
    textBackgroundColorField.setString(textBackgroundColorValue);


    // Add options
    this.append(binarySettings);
    //this.append(channelField);
    this.append(charsetField);
    this.append(columnsField);
    this.append(rowsField);
    this.append(foregroundColorField);
    this.append(backgroundColorField);
    this.append(textForegroundColorField);
    this.append(textBackgroundColorField);
    this.append(screenTransformationField);

    // set up this Displayable to listen to command events
    setCommandListener(this);
    // add the Exit command
    addCommand(new Command("Ok", Command.OK, 1));
    addCommand(new Command("Cancel", Command.CANCEL, 2));
  }

  /**Handle command events*/
  public void commandAction(Command command, Displayable displayable) {
    /** @todo Add command handling code */
    switch (command.getCommandType()) {
      case Command.OK:
        // Set binary settings
        boolean useFullScreen = binarySettings.isSelected(INDEX_WANTFULLSCREEN);
        if (useFullScreen != ConfigValues.useFullScreen) {
            ConfigValues.useFullScreen = useFullScreen;

            if (useFullScreen) {
                mainScreen.checkCommands();
            }

            mainScreen.setFullScreenMode(useFullScreen);

            if (!useFullScreen) {
                mainScreen.checkCommands();
            }
        }

        //ConfigValues.useChannel = binarySettings.isSelected(INDEX_FORCECHANNEL);
        //ConfigValues.channelNumber = Integer.parseInt(channelField.getString());

        if (ConfigValues.USE_NOKIA_EXT) {
            boolean useBacklight = binarySettings.isSelected(INDEX_WANTBACKLIGHT);
            if (ConfigValues.USE_NOKIA_EXT &&
                (useBacklight != ConfigValues.useBacklight)) {
                if (useBacklight) {
                    com.nokia.mid.ui.DeviceControl.setLights(0, 100);
                } else {
                    com.nokia.mid.ui.DeviceControl.setLights(0, 0);
                }
                ConfigValues.useBacklight = useBacklight;
            }
        }

        boolean initializeScreen = false;
        boolean createNewBoard = false;

        // Set screen transformation
        int screenTransformation;
        switch (screenTransformationField.getSelectedIndex()) {
          case NORMAL:
              screenTransformation = ConfigValues.SCREEN_NORMAL;
            break;
          default:
              screenTransformation = ConfigValues.SCREEN_LANDSCAPE;
            break;
        }

        if (screenTransformation != ConfigValues.screenTransformation) {
            initializeScreen = true;
            ConfigValues.screenTransformation = screenTransformation;
        }

        String charset = (String)charsets.elementAt(charsetField.getSelectedIndex());
        if (!ConfigValues.currentCharTable.equals(charset)) {
            initializeScreen = true;
            ConfigValues.currentCharTable = charset;
        }

        int charsPerLine = Integer.parseInt(columnsField.getString());
        if (charsPerLine != ConfigValues.charsPerLine) {
            initializeScreen = true;
            ConfigValues.charsPerLine = charsPerLine;
        }

        int totalLines = Integer.parseInt(rowsField.getString());
        if (totalLines != ConfigValues.totalLines) {
            initializeScreen = true;
            ConfigValues.totalLines = totalLines;
        }

        try {
            int foregroundColor = parseHexString(foregroundColorField.getString());
            if (foregroundColor != ConfigValues.foregroundColor) {
                createNewBoard = true;
                ConfigValues.foregroundColor = foregroundColor;
            }
        } catch (NumberFormatException nfex) {
            if (ConfigValues.DEBUG) {
                nfex.printStackTrace();
            }
        }

        try {
            int backgroundColor = parseHexString(backgroundColorField.getString());
            if (backgroundColor != ConfigValues.backgroundColor) {
                createNewBoard = true;
                ConfigValues.backgroundColor = backgroundColor;
            }
        } catch (NumberFormatException nfex) {
            if (ConfigValues.DEBUG) {
                nfex.printStackTrace();
            }
        }

        try {
            int textForegroundColor = parseHexString(textForegroundColorField.getString());
            if (textForegroundColor != ConfigValues.textForegroundColor) {
                createNewBoard = true;
                ConfigValues.textForegroundColor = textForegroundColor;
            }
        } catch (NumberFormatException nfex) {
            if (ConfigValues.DEBUG) {
                nfex.printStackTrace();
            }
        }

        try {
            int textBackgroundColor = parseHexString(textBackgroundColorField.getString());
            if (textBackgroundColor != ConfigValues.textBackgroundColor) {
                createNewBoard = true;
                ConfigValues.textBackgroundColor = textBackgroundColor;
            }
        } catch (NumberFormatException nfex) {
            if (ConfigValues.DEBUG) {
                nfex.printStackTrace();
            }
        }


        // Store configuration
        ConfigValues.writeConfiguration();

        if (initializeScreen) {
            mainScreen.initializeScreen();
        } else if (createNewBoard) {
            mainScreen.createNewBoard(mainScreen.getWidth(), mainScreen.getHeight());
        }
      case Command.CANCEL:
        // Show main menu
        Display.getDisplay(BlueLCD.instance).setCurrent(mainScreen);
        break;
    }
  }

  public int parseHexString(String targetString) throws NumberFormatException {
      String lowString = targetString.toLowerCase();

      int resultNumber = 0;

      int currentMultiplier = 1;
      for (int c=lowString.length() - 1; c>=0; c--) {
          char currentChar = lowString.charAt(c);

          int currentCharValue;

          switch (currentChar) {
          case '0':
              currentCharValue = 0;
              break;
          case '1':
              currentCharValue = 1;
              break;
          case '2':
              currentCharValue = 2;
              break;
          case '3':
              currentCharValue = 3;
              break;
          case '4':
              currentCharValue = 4;
              break;
          case '5':
              currentCharValue = 5;
              break;
          case '6':
              currentCharValue = 6;
              break;
          case '7':
              currentCharValue = 7;
              break;
          case '8':
              currentCharValue = 8;
              break;
          case '9':
              currentCharValue = 9;
              break;
          case 'a':
              currentCharValue = 10;
              break;
          case 'b':
              currentCharValue = 11;
              break;
          case 'c':
              currentCharValue = 12;
              break;
          case 'd':
              currentCharValue = 13;
              break;
          case 'e':
              currentCharValue = 14;
              break;
          case 'f':
              currentCharValue = 15;
              break;
          default:
              throw new NumberFormatException("Incorrect number");
          }

          resultNumber += currentCharValue * currentMultiplier;

          currentMultiplier *= 16;
      }

      return resultNumber;
  }

}
