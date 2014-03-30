package net.uworks.apps.bluelcd;

/**
 * <p>Title: Labyrinth</p>
 * <p>Description: Labyrinth generator.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: uWorks</p>
 * @author Josep del Río
 * @version 1.0
 */
import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Enumeration;

public class OnScreenMenu {
  // Constants for the menu
  final int menuColor = 0x3e31a2;
  final int menuFontColor = 0x00ffff;
  final Font menuFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);

  final int selectedMenuColor = 0x00ffff;
  final int selectedMenuFontColor = 0x3e31a2;
  final Font selectedMenuFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

  final int menuAnchor = Graphics.HCENTER|Graphics.VCENTER;
  // Options have only horizontal anchor (HCENTER, LEFT, RIGHT)
  final int optionsAnchor = Graphics.HCENTER;

  // Menu options size. If below zero, it will autodetect them
  int sizeX = -1;
  int sizeY = -1;
  // Menu offset size. If below zero, it will autodetect them
  int offsetX = -1;
  int offsetY = -1;

  // If we want the keypad numbers to activate the options, we should set this to true
  final boolean numbersAreShortcuts = false;

  // Var definition
  Vector menuOptions = new Vector();
  Canvas targetCanvas;
  CommandListener targetCommandListener;
  int currentOption = 0;
  int lastOption = 0;
  boolean updateMenu = false;

  public void addOption(Command optionCommand) {
    menuOptions.addElement(optionCommand);
  }

  public OnScreenMenu(Canvas targetCanvas, CommandListener targetCommandListener) {
    // If sizeY is less than zero, get the value (it doesn't depend from the string itself)
    if (sizeY < 0) {
      if (menuFont.getHeight() > selectedMenuFont.getHeight()) {
        sizeY = menuFont.getHeight();
      } else {
        sizeY = selectedMenuFont.getHeight();
      }
      // Put some margin
      sizeY += 2;
    }

    // Set targets
    this.targetCanvas = targetCanvas;
    this.targetCommandListener = targetCommandListener;
  }

  void selectOption(int newOption) {
    // Save last option
    lastOption = currentOption;
    // Set option number
    currentOption = newOption;
    // Validate it
    if (currentOption < 0) {
      currentOption = menuOptions.size() - 1;
    } else if (currentOption >= menuOptions.size()) {
      currentOption = 0;
    }
    // Show it
    updateMenu = true;
    targetCanvas.repaint();
  }

  void executeCurrentOption() {
    executeOption(currentOption);
  }

  void executeOption(int optionNumber) {
    // Select it
    selectOption(optionNumber);
    // Get the target command
    Command targetCommand = (Command)menuOptions.elementAt(optionNumber);
    // Send to our commandListener
    targetCommandListener.commandAction(targetCommand, targetCanvas);
  }

  public void drawMenu(Graphics g) {
    // If sizeX is less than zero, get the value
    if (sizeX < 0) {
      Enumeration finalOptions = menuOptions.elements();
      while (finalOptions.hasMoreElements()) {
        Command targetCommand = (Command)finalOptions.nextElement();
        if (menuFont.stringWidth(targetCommand.getLabel()) > sizeX) {
          sizeX = menuFont.stringWidth(targetCommand.getLabel());
        }
        if (selectedMenuFont.stringWidth(targetCommand.getLabel()) > sizeX) {
          sizeX = selectedMenuFont.stringWidth(targetCommand.getLabel());
        }
      }
      // Put a bit of margin
      sizeX += 2;
    }

    if (offsetX < 0) {
      if ((this.menuAnchor & Graphics.HCENTER) > 0) {
        offsetX = (targetCanvas.getWidth() - sizeX - 4) / 2;
      }
      else if ((this.menuAnchor & Graphics.LEFT) > 0) {
        offsetX = 0;
      } else { //if ((this.menuAnchor & Graphics.RIGHT > 0) {
        offsetX = targetCanvas.getWidth()- sizeX - 4;
      }

      if (offsetX < 0) {
        offsetX = 0;
      }
    }

    if (offsetY < 0) {
      if ((this.menuAnchor & Graphics.VCENTER) > 0) {
        offsetY = (targetCanvas.getHeight() - (sizeY * menuOptions.size()) - 4) / 2;
      } else if ((this.menuAnchor & Graphics.TOP) > 0) {
        offsetY = 0;
      } else { //if ((this.menuAnchor & Graphics.BOTTOM) > 0) {
        offsetY = targetCanvas.getHeight() - (sizeY * menuOptions.size()) - 4;
      }

      if (offsetY < 0) {
        offsetY = 0;
      }
    }

    if (updateMenu) {
      // Update last option and current option
      Command lastSelectedCommand = (Command)menuOptions.elementAt(lastOption);
      drawOption(g, lastSelectedCommand.getLabel(), lastOption, false);
      Command actualSelectedCommand = (Command)menuOptions.elementAt(currentOption);
      drawOption(g, actualSelectedCommand.getLabel(), currentOption, true);
      updateMenu = false;
    } else {
      // Draw external rectangle with foreground color
      g.setColor(menuFontColor);
      g.drawRect(offsetX, offsetY, sizeX + 3, sizeY * menuOptions.size() + 3);
      g.setColor(menuColor);
      g.fillRect(offsetX + 1, offsetY + 1, sizeX + 2, sizeY * menuOptions.size() + 2);
      g.setColor(menuFontColor);
      g.setFont(menuFont);
      Enumeration optionsList = menuOptions.elements();
      int optionNumber = 0;
      while (optionsList.hasMoreElements()) {
        Command targetCommand = (Command)optionsList.nextElement();
        drawOption(g, targetCommand.getLabel(), optionNumber, optionNumber == currentOption);
        optionNumber++;
      }
    }

  }

  void drawOption(Graphics g, String option, int number, boolean selected) {
    int currentOffsetX;
    int currentOffsetY;

    if (selected) {
      g.setColor(selectedMenuColor);
    } else {
      g.setColor(menuColor);
    }
    g.fillRect(offsetX + 2, offsetY + 2 + sizeY * number, sizeX, sizeY);
    if (selected) {
      g.setColor(selectedMenuFontColor);
      g.setFont(selectedMenuFont);
      // Calculate currentOffsetY
      currentOffsetY = (sizeY - selectedMenuFont.getHeight()) / 2;
    } else {
      g.setColor(menuFontColor);
      g.setFont(menuFont);
      // Calculate currentOffsetY
      currentOffsetY = (sizeY - menuFont.getHeight()) / 2;
    }
    if ((optionsAnchor & Graphics.HCENTER) > 0) {
      currentOffsetX = (sizeX - g.getFont().stringWidth(option)) / 2;
    } else if ((optionsAnchor & Graphics.LEFT) > 0) {
      currentOffsetX = 0;
    } else { //if ((optionsAnchor & Graphics.RIGHT) > 0) {
      currentOffsetX = sizeX - g.getFont().stringWidth(option);
    }

    g.drawString(option, offsetX + 2 + currentOffsetX, offsetY + 2 + currentOffsetY + sizeY * number, Graphics.TOP|Graphics.LEFT);
  }

  /**
   * When the menu is activated, the canvas keypressed should be redirected here
   * @param keyCode the key that was pressed
   */
  public void keyPressed (int keyCode) {
    if (keyCode > 0) {
      if (numbersAreShortcuts) {
        switch (keyCode) {
          case Canvas.KEY_NUM1:
            executeOption(1);
            break;
          case Canvas.KEY_NUM2:
            executeOption(2);
            break;
          case Canvas.KEY_NUM3:
            executeOption(3);
            break;
          case Canvas.KEY_NUM4:
            executeOption(4);
            break;
          case Canvas.KEY_NUM5:
            executeOption(5);
            break;
          case Canvas.KEY_NUM6:
            executeOption(6);
            break;
          case Canvas.KEY_NUM7:
            executeOption(7);
            break;
          case Canvas.KEY_NUM8:
            executeOption(8);
            break;
          case Canvas.KEY_NUM9:
            executeOption(9);
            break;
          case Canvas.KEY_NUM0:
            executeOption(10);
            break;
        }
      } else {
        switch (keyCode) {
          case Canvas.KEY_NUM2:
            selectOption(currentOption - 1);
            break;
          case Canvas.KEY_NUM8:
            selectOption(currentOption + 1);
            break;
          default:
// Any other key executes the option...
//          case Canvas.KEY_NUM5:
            executeCurrentOption();
            break;
        }
      }
    }
    else {
      int gameAction = targetCanvas.getGameAction(keyCode);
      switch (gameAction) {
        case Canvas.UP:
          selectOption(currentOption - 1);
          break;
        case Canvas.DOWN:
          selectOption(currentOption + 1);
          break;
        default:
// Any other key executes the option...
//        case Canvas.FIRE:
//        case Canvas.GAME_A:
//        case Canvas.GAME_B:
//        case Canvas.GAME_C:
//        case Canvas.GAME_D:
          executeCurrentOption();
          break;
      }
    }
  }

  void pointerPressed(int x, int y) {
    // Check what option number was selected
    if ((x > (offsetX + 2)) && (x < (offsetX + 2 + sizeX))) {
      // The "X" coordinate is well placed
      int pointerSelectedY = (y - offsetY - 2) / sizeY;
      if ((pointerSelectedY >= 0) && (pointerSelectedY < menuOptions.size())) {
          executeOption(pointerSelectedY);
      }
    }
  }
}
