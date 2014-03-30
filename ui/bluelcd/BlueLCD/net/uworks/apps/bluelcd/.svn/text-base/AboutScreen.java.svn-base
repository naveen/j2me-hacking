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
public class AboutScreen extends Form implements CommandListener, ItemCommandListener {
    Displayable backDisplayable;
    StringItem urlItem;

    public AboutScreen(Displayable backDisplayable) {
        super("About BlueLCD");

        this.backDisplayable = backDisplayable;

        try {
            init();
        }
        catch(Exception e) {
            if (ConfigValues.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    private void init() throws Exception {
        StringItem authorItem = new StringItem("Author", "Josep del Río");
        authorItem.setLayout(Item.LAYOUT_NEWLINE_AFTER);
        this.append(authorItem);

        StringItem vendorItem = new StringItem("Vendor", "uWorks");
        vendorItem.setLayout(Item.LAYOUT_NEWLINE_AFTER);
        this.append(vendorItem);

        urlItem = new StringItem("URL", "http://uWorks.net", Item.HYPERLINK);
        urlItem.setItemCommandListener(this);
        urlItem.setDefaultCommand(new Command("Open", Command.OK, 1));
        urlItem.setLayout(Item.LAYOUT_NEWLINE_AFTER);

        this.append(urlItem);

        this.append(new StringItem(null, "Support the development of this application by making a small donation!"));

        // Set up this Displayable to listen to command events
        setCommandListener(this);
        // add the Exit command
        addCommand(new Command("Back", Command.BACK, 1));
    }

    public void commandAction(Command command, Displayable displayable) {
        Display.getDisplay(BlueLCD.instance).setCurrent(backDisplayable);
    }

    public void commandAction(Command command, Item item) {
        if (item == urlItem) {
            // Launch the browser
            try {
                BlueLCD.instance.platformRequest("http://www.uworks.net");
            } catch (Exception ex) {
                if (ConfigValues.DEBUG) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
