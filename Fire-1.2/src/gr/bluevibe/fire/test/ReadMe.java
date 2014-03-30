/*
 * Fire (Flexible Interface Rendering Engine) is a set of graphics widgets for creating GUIs for j2me applications. 
 * Copyright (C) 2006  Bluebird co (www.bluebird.gr)
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */

/*
 * Created on Nov 26, 2006
 */
package gr.bluevibe.fire.test;

import java.util.Calendar;
import java.util.Hashtable;

import gr.bluevibe.fire.components.Component;
import gr.bluevibe.fire.components.DateTimeRow;
import gr.bluevibe.fire.components.FTicker;
import gr.bluevibe.fire.components.ListBox;
import gr.bluevibe.fire.components.ListElement;
import gr.bluevibe.fire.components.Panel;
import gr.bluevibe.fire.components.Popup;
import gr.bluevibe.fire.components.Row;
import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.displayables.SplashScreen;
import gr.bluevibe.fire.util.CommandListener;
import gr.bluevibe.fire.util.FireIO;
import gr.bluevibe.fire.util.Lang;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * 
 * This Midlet is a demonstration and a walk-through on developing a GUI using the Fire Engine. 
 * 
 * Read the comments in the source for details.
 * 
 * On the following walk through I assume you've gone throught the documentation and got a general idea of what each Fire class does.
 * 
 * In order for the FireScreen singleton to initialize correctly it must have a theme file. 
 * So make sure you have the default theme provided with the source.  
 * 
 * 
 * @author padeler
 *
 */
public class ReadMe extends MIDlet implements CommandListener
{
	/* These vars are used in the examples below */
	private Command exit,cancel, update,selection,show2,show3,alertCmd,menuCmd,reset,more,less,orientation;
	private Command show1 = new Command("Back",Command.BACK,1);
	private Row nameRow,textField;
	private FireScreen screen;
	
	/* ****************************************** */

	/**
	 * A midlet that will demostrate the capabilities of the Fire Components and 
	 * how to use them. 
	 *
	 */
	public ReadMe()
	{
	}

	protected void startApp() throws MIDletStateChangeException
	{
		// The first step is to initialize the FireIO. 
		// The FireScreen class uses the FireIO in order to load the 
		// theme image so we really need to do this first.
		// Most of the lines here could be ommited, if the default values are ok.
		
		Hashtable mediaMap = new Hashtable(); // The hashtable maps key string to file 
																		  // name that are located either in the jar or at a web location.
		
		mediaMap.put(FireScreen.THEME_FILE,"theme.png"); // THEME_FILE is the default key for the theme image
		mediaMap.put("logo","cafe.png"); // some other media we are going to use later on the tutorial.
		mediaMap.put("box","box.png");  
		mediaMap.put("checkedbox","checkedbox.png");
		mediaMap.put("fire","fire.jpg");  
		mediaMap.put("water","water.jpg");  
		
		// we setup the FireIO with the mediaMap we just created, and a cache size=10.
		// This means that up to 10 images will be cached in the FireIO class. 
		// The downloadLocation is null because all our images will be local.   
		FireIO.setup(mediaMap,10,null); 
		
		
		// I want to have a nice splash screen while my application initializes 
		// so i will use the SplashScreen class before doing anything else. 
        SplashScreen loading = new SplashScreen(); 
        loading.setTitle("Fire Demo"); // set the title of the splash screen
        loading.setLogo(FireIO.getLocalImage("logo")); // show a nice logo.
        // Note that i used the FireIO.getLocalImage in order to load the logo image. 
        // The getLocalImage method will first look in the cache for the image, then in the record store, 
        // thirdly in the jar and finally (if a url was provided on setup()) it will try to download the image and 
        // store it locally. If the image is found on a location it is cached, in case of a later request.
        
        Display.getDisplay(this).setCurrent(loading); // show the load screen.
        
        // now we continue with the start up proccess:
        
        // First we load the default language bundle:
        Lang.loadBundle(); // this is optional if you dont want to use the Lang class for i18n
        
        
        // Secondly set the location of the theme logo, on the top border of each panel. 
        // This is optional, the default value is FireScreen.RIGHT, other possible values are FireScreen.CENTER and FireScreen.LEFT.
        FireScreen.setLogoPossition(FireScreen.LEFT);
        
        
        // Thirdly we initialize the FireScreen.
        screen = FireScreen.getScreen(Display.getDisplay(this));
        // A request to the FireScreen.getSctreen() with null parameter, returns the singleton if initialized
        // ie : screen = FireScreen.getScreen(null); returns the same screen object, thus there is no need for keeping the pointer.
        // For the scope of this demo, we will use the screen variable instead of calling the FireScreen.getScreen(null) each time.
        screen.setFullScreenMode(true); // set the FireScreen to full screen mode. 
        
        // You can set the orientation of the screen, normal, or landscape (right handed or left handed)
        screen.setOrientation(FireScreen.NORMAL); // normal is the default orientation
        screen.setOrientationChangeKey(new Integer(Canvas.KEY_STAR));  // no preset key is the default. You can reset that by setting null key.
        
        // set the screen to a non interractive busy mode. That means that no user action is allowed while the screen is in busy mode.
        screen.setInteractiveBusyMode(false); // false is the default value.
        
        // and thats with the initialization proccess ... in a real application there would be more stuff to do here
        // now we will create out first panel
        Panel p = createPanel(); // look in the method comments for details.
        
        screen.setCurrent(p); // set the current panel on the FireScreen.
  
	}
	
	/**
	 * Demonstrates how to create a simple panel on Fire engine. 
	 * It shows the use of Row as a means to display images, text, create textfields, links (or buttons) etc. 
	 * It also demonstrates the  use of a ListBox, and the Busy mode of the FireScreen.
	 * Finally it shows how to create a popup menu.
	 */
	public Panel createPanel()
	{
		Panel helloPanel = new Panel(); // create instance
		helloPanel.setLabel(Lang.get("Fire Demo")); // set label (displayed on the top of the screen)
		// ok we have a panel now, lets add some functionallity
		exit = new Command(Lang.get("Exit"),Command.EXIT,1);
		helloPanel.addCommand(exit); // add the command to the panel, 
		// it will be displayed on the bottom bar on the right, and assigned to the righ softkey.
		// The Panel checks the type of the command added. If it is Command.BACK 
		// it is assigned to the left softkey, otherwise it goes to the right. 
		
		// now set the cancel command (it is displayed only if the FireScreen is on busy mode
		cancel = new Command(Lang.get("Cancel"),Command.CANCEL,1);
		helloPanel.addCommand(cancel);
		
		
		// now set the CommandListener of the panel.
		helloPanel.setCommandListener(this);
		// this class implements a CommandListener.
		// for notes on the listener go to the commandAction method.
		
		// ok, up to now we have a very simple application. 
		// next step is to add a couple of rows to our panel.
		
		Row simpleText = new Row(Lang.get("This is a text only row. The string can have any size, and the alligment can be set both horizontally and verytically"));
		simpleText.setTextHpos(FireScreen.CENTRE); // the default V/H pos is TOP/LEFT, you dont have to set these every time.
		simpleText.setTextVpos(FireScreen.TOP);
		helloPanel.add(simpleText);
		
		
		Row imageText = new Row(Lang.get("This is a row that has both text and an image. The image can be before or after the text."),FireIO.getLocalImage("fire"));
		helloPanel.add(imageText);
		
		
		// in order to make the UI clearer we can add an empty row.
		helloPanel.add(new Row()); // you can comment this out to see the difference.
		
		// another type of seperator or header can be a line of color, with or without a text label
		Row seperator = new Row();
		seperator.setFilled(new Integer(FireScreen.defaultFilledRowColor)); // use the default color of the theme.
		seperator.setBorder(true); // borders look nice :)
		seperator.setText(Lang.get("TextFields & Commands")); // a string can be usefull on a header.
		seperator.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_SMALL)); // change the font
		seperator.setAlignment(FireScreen.CENTRE); // allign the text to the centre.
		helloPanel.add(seperator);
		
		// now lets add a text area
		textField = new Row();
		textField.setEditable(true); // when a row is set as editable 
													 // it gets a min height equal to the Fonts height.
		
		// you can even add a label on a row, so lets add one to our textField example
		textField.setLabel(Lang.get("What is your name?"),
									  FireScreen.defaultLabelFont,
									  new Integer(screen.getWidth()/2), // set the width of the label to be half the screen width.
									  																						   // this helps with the alligment of multiple textfields.
									  																						   // If you dont care about the label width, you can leave this null
									  FireScreen.CENTRE); // The alligment of the label in the row (applicable if row.height>labeltext.heigh)
		
		textField.setTextBoxConstrains(TextField.INITIAL_CAPS_WORD); // if a row is editable it gets the same constratints as a TextBox
		textField.setTextBoxSize(20); // max name size=20
		// when a user enters text on a text field you can get it back using the getText() method. 
		helloPanel.add(textField); // finally add it to the panel.
		
		// now lets add some components with commands
		nameRow = new Row(); 
		nameRow.setLabel(Lang.get("Your name is"),FireScreen.defaultLabelFont,new Integer(screen.getWidth()/2),FireScreen.CENTRE);
		helloPanel.add(nameRow);
		
		// Create a "button" that will update the text in the nameRow, with the one set on the textField.
		update = new Command("",Command.OK,1);
		Row updateButton = new Row(Lang.get("Update"));
		updateButton.setAlignment(FireScreen.RIGHT); // set it on the right
		updateButton.addCommand(update); // set the command
		updateButton.setCommandListener(this); 
		helloPanel.add(updateButton);
		
		
		// add another seperator. Before we continue to the next component
		seperator = new Row();
		seperator.setFilled(new Integer(FireScreen.defaultSecondaryFilledRowColor)); // use the default color of the theme.
		seperator.setBorder(true);
		seperator.setImage(FireIO.getLocalImage("logo")); // add an image
		seperator.setText(Lang.get("ListBox & Gauge")); // and a text
		seperator.setImageHpos(FireScreen.LEFT); // set image horizontal possition on the left
		seperator.setTextHpos(FireScreen.RIGHT); // set text pos to the right
		/* Note:
		 * Setting the horizontal location of images and text on a row can be done in a single line 
		 * by using the "new Row(Image, String)" or the "new Row(String, Image)" constructors.
		 */
		seperator.setFont(Font.getFont(Font.FACE_MONOSPACE,Font.STYLE_BOLD,Font.SIZE_MEDIUM)); // change the font
		seperator.setAlignment(FireScreen.CENTRE); // allign the text to the centre.
		helloPanel.add(seperator);
		
		
		// Now we will make a list of items, and demonstrate the use of the busy indicator.
		
		ListBox choice = new ListBox(); // a listbox is an area that shown a list of choices.
														  // it can function as a set of checkboxes (multiple selection)
														  // or are a set radio buttons ( single selection, default)
		choice.setMultiple(false); // radio buttons mode (default)
		
		choice.setBullet(FireIO.getLocalImage("box"));  // if not set, a filled circle is drawn as a bullet..
		choice.setSelectedBullet(FireIO.getLocalImage("checkedbox"));  // comment these two lines to see the difference.
		
		
		// For this demo we will use the single selection, to turn busy mode on and off.
		choice.add(new ListElement(Lang.get("Normal mode"),"N",true)); // the ListBox contains list elemens.
		choice.add(new ListElement(Lang.get("Busy mode"),"B",false)); 
		// each ListElement, has a text description, an object identifier (or payload) and a state (selected or not).
		selection = new Command("",Command.OK,1);
		choice.addCommand(selection); // go to the corresponding location in the commandAction method. 
		choice.setCommandListener(this);
		helloPanel.add(choice);
		
		// The last part of the demo, on this panel will be to create a popup menu. 
		// Throught this menu we will move to other Panels demonstrating more of the engines capabilities.
		Popup menu = new Popup(); // create a popup instance.
		
		// instantiate some commands
		
		show2 = new Command("",Command.OK,1);
		show3 = new Command("",Command.OK,1);
		alertCmd = new Command("",Command.OK,1);

		// in a popup panel you can add everything you add on a normal panel.
		// it even shown a scrollbar if you put too much elements in it.
		Row panel2= new Row("to Panel2");
		panel2.addCommand(show2);
		panel2.setCommandListener(this);
		Row panel3= new Row("to Panel3");		
		panel3.addCommand(show3);
		panel3.setCommandListener(this);
//		 to get the best optical results its good to add image icons with text inside popup menus
		Row alert= new Row(FireIO.getLocalImage("logo"),Lang.get("Show Alert"));  		
		alert.addCommand(alertCmd); // for a demonstration of panel alerts go to the alertCmd 
														  // handling code in the commandAction() method
		alert.setCommandListener(this);
		
		menu.add(panel2);
		menu.add(panel3);
		menu.add(alert);

		// There are two ways to display a popup. 
		// The first is by directly calling the FireScreen.getScreen(null).showPopup(myPopup);
		// The second is indirectly by associating it with a softkey command on a panel using 
		// the panel.addCommand(popup,command) method like below:
		menuCmd = new Command(Lang.get("Menu"),Command.BACK,1); // set the Command  as BACK in order to be assigned on the left softkey
		helloPanel.addCommand(menuCmd,menu);
		
		// In every case you are responsible for calling the FireScreen.getScreen(null).closePopup() method,
		// when you want the popup to close (see menu handling code in commandAction() method) 
		
		// thats it for this panel. Go to createPanel2() and createPanel(3) methods to continue with the demonstration.
		return helloPanel;
	}
	
	
	/**
	 * This method demonstrates the Ticker, and the DateTimeRow
	 *  
	 * @return A panel
	 */
	public Panel createPanel2()
	{
		Panel panel = new Panel();
		panel.setLabel(Lang.get("Second Panel"));
		panel.setCommandListener(this);
		// A ticket is a very simple component it shows a string that scrolls on the top of the Panel.
		// A panel can have only one ticker.
		FTicker ticker = new FTicker(Lang.get("This is a ticker that does not inform you about anything usefull"));
		panel.setTicker(ticker);
		
		
		// now lets add a button with an image, that changes when selected.
		Row button = new Row("Fire & Water",FireIO.getLocalImage("fire"));
		button.setSelectedImage(FireIO.getLocalImage("water"));
		button.addCommand(new Command("",Command.OK,1)); // we dont want any action, just add the command and listener 
		button.setCommandListener(this);									  // to make the component traversable
		button.setAlignment(FireScreen.CENTRE);
		panel.add(button);
		
		
		// now lets add a DateTimeRow. This components allows , as the name implies, the user to input date and time.
		// The DateTimeRow has two modes, it can display only the date, or the date and the time (if isShowTime()==true)
		// The user can make the date/time selection either by tapping on the appropriate field or by pressing fire on it,
		// using the joystick of the device.
		DateTimeRow dtr = new DateTimeRow(Calendar.getInstance(),true);
		dtr.setLabel(Lang.get("Enter your age"));// The DateTimeRow can optionally have a Label. 
		panel.add(dtr);
		
		// If we want we can allow the user to change the orientation of the screen, at runtime.
		// We will add a listbox with the three available orientation options: Normal, landscape left, landscape right.
		panel.add(new Row(Lang.get("You can select the screen mode you prefer:")));
		ListBox orientationChoice = new ListBox(); 
		
		orientationChoice.setBullet(FireIO.getLocalImage("box"));  
		orientationChoice.setSelectedBullet(FireIO.getLocalImage("checkedbox"));  
		
		ListElement normal = new ListElement(Lang.get("Normal mode"),"N",false);
		ListElement left = new ListElement(Lang.get("Left-handed Landscape"),"L",false);
		ListElement right = new ListElement(Lang.get("Right-handed Landscape"),"R",false);
		
		// set the correct element as selected.
		int or = screen.getOrientation();
		switch(or)
		{
			case FireScreen.NORMAL:
				normal.setChecked(true);
				break;
			case FireScreen.LANDSCAPELEFT:
				left.setChecked(true);
				break;
			default:
				right.setChecked(true);
		}
		
		orientationChoice.add(normal); // the ListBox contains list elemens.
		orientationChoice.add(left);
		orientationChoice.add(right);
		
		// each ListElement, has a text description, an object identifier (or payload) and a state (selected or not).
		orientation  = new Command("",Command.OK,1);
		orientationChoice.addCommand(orientation); // go to the corresponding location in the commandAction method. 
		orientationChoice.setCommandListener(this);
		panel.add(orientationChoice);

		
		
		
		// finally we add a command to go back to the previous panel.
		// we will add this command both on a Softkey and on a row item on the panel.
		Row back = new Row("Back",FireIO.getLocalImage("logo"));
		back.addCommand(show1);
		back.setCommandListener(this);
		panel.add(back);
		
		panel.addCommand(show1);
		
		
		
		
		
		return panel;
	}

	/**
	 * This method demostrates how to dynamically add and remove components from a Panel.
	 * @return
	 */
	public Panel createPanel3()
	{
		Panel panel = new Panel();
		panel.setLabel(Lang.get("Adding components."));
		panel.setCommandListener(this);
		panel.addCommand(show1);
		reset = new Command(Lang.get("Reset"),Command.OK,1); // resets the panel.
		panel.addCommand(reset);
		
		more = new Command("",Command.OK,1);
		Row moreRow = new Row(Lang.get("More"));
		moreRow.setAlignment(FireScreen.CENTRE);
		moreRow.addCommand(more);
		moreRow.setCommandListener(this);
		less = new Command("",Command.OK,1);
		Row lessRow = new Row(Lang.get("Less"));
		lessRow.setAlignment(FireScreen.CENTRE);
		lessRow.addCommand(less);
		lessRow.setCommandListener(this);
		
		
		panel.add(new Row("Pressing more will add a new row to the panel. Pressing less will remove one row from the panel."));
		panel.add(moreRow);
		panel.add(lessRow);
		panel.add(new Row()); // empty line.
		// Check the commandAction method for the code and comments on dynamic adding/removing components.
		
		return panel;
	}
	
	protected void pauseApp()
	{
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
		// In order to stop the clock thread, and clean up you must 
		// call the destroy method on the FireScreen singleton, 
		// when you exit. 
		screen.destroy();

		//... thanks for reading :)
	}

	/**
	 * All the actions in this demo use this CommandListener's commandAction. 
	 * Check the individual comments for each example.
	 * 
	 * @see gr.bluevibe.fire.util.CommandListener#commandAction(javax.microedition.lcdui.Command, gr.bluevibe.fire.components.Component)
	 */
	public void commandAction(Command cmd, Component c)
	{	
		if(cmd==exit)
		{ // exit requested , check the comments on the destroyApp method.
			notifyDestroyed();
			return;
		}
		
		if(cmd==update)
		{
			// get the text entered on the textField
			String name = textField.getText();
			// set it on the nameRow:
			nameRow.setText(name);
			// Validate nameRow.
			nameRow.validate();
			screen.repaint();
			return;
		}
		if(cmd==selection)
		{
			// get the selected ListElement.
			ListElement selected = (ListElement)((ListBox)c).getCheckedElements().elementAt(0); // single selection means always only one checked
			if(((String)selected.getId()).equals("N")) // check the id Object of the ListElement. We set "N" to normal and "B" to Busy.
			{ // Normal Mode 
				screen.setBusyMode(false); // disable busy mode.
			}
			else
			{ // set busy mode, busy mode is usefull for notifing the user that a task is underway in the background. 
				// If interractive mode is true, the GUI continues to function normally, and the small animation is displayed on the bottom of the screen.
				// otherwise only the cancel command is allowed.
				
				screen.setBusyMode(true); // disable busy mode.
			}
			return;
		}
		
		if(cmd==show1) // return to main menu
		{
			// if returning to the main menu is going-back , then lets make the transition animation move from left to right
			screen.setCurrent(createPanel(),FireScreen.RIGHT);
			return;
		}
		
		if(cmd==more) // on panel3 , add more components to the panel
		{
			Panel panel3 = screen.getCurrentPanel();
			panel3.add(new Row("One more row, panel now has "+(panel3.countRows()+1)));
			panel3.validate();
			return;
		}
		if(cmd==less) // on panel3 , remove components from the panel
		{
			Panel panel3 = screen.getCurrentPanel();
			panel3.remove(panel3.countRows()-1); // remove last component.
			panel3.resetPointer(); // reset the pointer position.
			panel3.validate();
			return;
		}
		if(cmd==reset) //on panel3, reset.
		{
			screen.setCurrent(createPanel3());
		}
		
		/* Menu Commands Handling Code */
		if(cmd==show2)
		{
			screen.closePopup(); // first close the popup.
			screen.setCurrent(createPanel2());
			return;
		}

		if(cmd==show3)
		{
			screen.closePopup(); // first close the popup.
			screen.setCurrent(createPanel3());
			return;
		}

		if(cmd==alertCmd)
		{
			screen.closePopup(); // first close the popup.
			// The panel has an alertUser method, witch displays an alert-like message to the user. 
			// This can be very usefull for reporting errors,warnings etc in order to display an alert 
			// you can call the showAlert method on the current panel.
			
			screen.getCurrentPanel().showAlert(Lang.get("An alert is a popup box containing a message and/or an image. Any action from the user dismisses the alert"),null);
			
			return;
		}
		if(cmd==orientation)
		{
			ListElement selected = (ListElement)((ListBox)c).getCheckedElements().elementAt(0); // single selection means always only one checked
			String id = ((String)selected.getId());
			if(id.equals("N"))
			{ // Normal Mode 
				screen.setOrientation(FireScreen.NORMAL);
			}
			else if(id.equals("L"))
			{ 
				screen.setOrientation(FireScreen.LANDSCAPELEFT);
			}
			else
			{
				screen.setOrientation(FireScreen.LANDSCAPERIGHT);
			}
			return;
		}
		if(cmd==cancel)
		{
			screen.setBusyMode(false);
		}
		/* End or Menu Commands */

		
	}
}
