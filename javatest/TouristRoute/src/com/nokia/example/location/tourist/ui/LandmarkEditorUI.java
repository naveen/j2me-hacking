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
package com.nokia.example.location.tourist.ui;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.location.AddressInfo;
import javax.microedition.location.Landmark;
import javax.microedition.location.LandmarkException;
import javax.microedition.location.QualifiedCoordinates;

import com.nokia.example.location.tourist.TouristMIDlet;
import com.nokia.example.location.tourist.Utils;
import com.nokia.example.location.tourist.model.ControlPoints;
import com.nokia.example.location.tourist.model.TouristData;

/**
 * Viewer class enabling landmarks address info monifications.
 */
class LandmarkEditorUI extends Form implements CommandListener
{
    // Landmark fields, (*) = required field
    private TextField nameField = new TextField("Name (*):", "", 20,
            TextField.ANY);

    private TextField descField = new TextField("Description (*):", "", 40,
            TextField.ANY);

    // AddressInfo fields
    private TextField countryField = new TextField("Country:", "", 20,
            TextField.ANY);

    private TextField stateField = new TextField("State:", "", 20,
            TextField.ANY);

    private TextField cityField = new TextField("City:", "", 20, TextField.ANY);

    private TextField streetField = new TextField("Street:", "", 20,
            TextField.ANY);

    private TextField buildingNameField = new TextField("Building name (*):",
            "", 20, TextField.ANY);

    // Coordinate information
    private StringItem lat = new StringItem("Lat:", "");

    private StringItem lon = new StringItem("Lon:", "");

    private StringItem alt = new StringItem("Alt:", "");

    /** Landmark selector list */
    private List list = new List("Landmarks:", List.EXCLUSIVE);

    private Command saveNewCmd = new Command("Save", Command.OK, 1);

    private Command saveUpdatedCmd = new Command("Save", Command.OK, 1);

    private Command updateCmd = new Command("Update", Command.OK, 1);

    private Command removeCmd = new Command("Remove", Command.OK, 1);

    private Command listCmd = new Command("List", Command.OK, 1);

    private Command routeCmd = new Command("Route", Command.BACK, 1);

    public static final int MODE_UPDATE = 0;

    public static final int MODE_ADDNEW = 1;

    private QualifiedCoordinates coord = null;

    private Displayable route = null;

    private TouristData data = null;

    /**
     * Construct Landmark Editor UI Form.
     * 
     * @param route -
     *            a reference of Route UI.
     * @param data -
     *            a reference to TouristData model layer class.
     */
    public LandmarkEditorUI(Displayable route, TouristData data)
    {
        super("Landmark Editor");

        this.route = route;
        this.data = data;

        // route and list commands always available on landmark editor
        addCommand(routeCmd);
        addCommand(listCmd);
        setCommandListener(this);

        // route command always available on landmarks list
        list.addCommand(routeCmd);
        list.setCommandListener(this);
    }

    /**
     * Initialize UI components and show the landmark editor.
     */
    public void showEditor(QualifiedCoordinates newCoord, int mode)
    {
        this.coord = newCoord;

        // initialize coordinate values
        lat.setText(Utils.formatDouble(newCoord.getLatitude(), 3));
        lon.setText(Utils.formatDouble(newCoord.getLongitude(), 3));
        alt.setText(Utils.formatDouble(newCoord.getAltitude(), 1));

        // initialize editable test field values
        nameField.setString("");
        descField.setString("");
        countryField.setString("");
        stateField.setString("");
        cityField.setString("");
        streetField.setString("");
        buildingNameField.setString("");

        deleteAll();
        append(nameField);
        append(descField);
        append(countryField);
        append(stateField);
        append(cityField);
        append(streetField);
        append(buildingNameField);

        append(lat);
        append(lon);
        append(alt);

        // Update existing landmark.
        if (mode == MODE_UPDATE)
        {
            Landmark lm = ControlPoints.getInstance().findNearestLandMark(
                    newCoord);

            if (lm != null)
            {
                nameField.setString(lm.getName());
                descField.setString(lm.getDescription());

                AddressInfo info = lm.getAddressInfo();
                if (info != null)
                {
                    countryField.setString(info.getField(AddressInfo.COUNTRY));
                    stateField.setString(info.getField(AddressInfo.STATE));
                    cityField.setString(info.getField(AddressInfo.CITY));
                    streetField.setString(info.getField(AddressInfo.STREET));
                    buildingNameField.setString(info
                            .getField(AddressInfo.BUILDING_NAME));
                }
            }

            removeCommand(updateCmd);
            removeCommand(saveNewCmd);
            addCommand(saveUpdatedCmd);
            addCommand(listCmd);
        }
        // Add new landmark to the landmark store.
        else if (mode == MODE_ADDNEW)
        {
            removeCommand(updateCmd);
            removeCommand(saveUpdatedCmd);
            addCommand(saveNewCmd);
            addCommand(listCmd);
        }

        TouristMIDlet.getDisplay().setCurrent(this);
    }

    /**
     * Show landmark selector List. Content of the list is refreshed each time
     * this method is called.
     */
    public void showList()
    {
        list.deleteAll();

        Landmark lm = null;
        Enumeration landmarks = ControlPoints.getInstance().getLandMarks();

        // Check whether landmarks can be founds from the Landmark store.
        if (landmarks != null)
        {
            while (landmarks.hasMoreElements())
            {
                lm = ((Landmark) landmarks.nextElement());
                list.append(lm.getName(), null);
            }

            list.addCommand(updateCmd);
            list.addCommand(removeCmd);
        }
        // No landmarks found (list is empty)
        else
        {
            list.removeCommand(updateCmd);
            list.removeCommand(removeCmd);
        }

        TouristMIDlet.getDisplay().setCurrent(list);
    }

    /**
     * Check wheter any required fields are missing. (*) on the TextFields label
     * indicates that field is mandatory.
     * 
     * @return Name of the missing field name or null indicating no required
     *         fields are missing.
     */
    private String checkRequiredFields()
    {
        if (nameField.getString().equals(""))
        {
            return "Name";
        }
        else if (nameField.getString().equals(""))
        {
            return "Description";
        }
        else if (buildingNameField.getString().equals(""))
        {
            return "Building name";
        }

        return null;
    }

    /**
     * Generate landmark from UI component values.
     * 
     * @return Genereated Landmark.
     */
    private Landmark generateLandmark()
    {
        String field = checkRequiredFields();
        if (field != null)
        {
            Alert alert = new Alert("Error", "Value for required field "
                    + field + " is missing.", null, AlertType.ERROR);
            alert.setTimeout(3000); // 3 secs

            TouristMIDlet.getDisplay().setCurrent(alert, this);
            return null;
        }

        AddressInfo info = new AddressInfo();
        info.setField(AddressInfo.COUNTRY, countryField.getString());
        info.setField(AddressInfo.STATE, stateField.getString());
        info.setField(AddressInfo.CITY, cityField.getString());
        info.setField(AddressInfo.STREET, streetField.getString());
        info.setField(AddressInfo.BUILDING_NAME, buildingNameField.getString());

        Landmark lm = new Landmark(nameField.getString(),
                descField.getString(), coord, info);

        return lm;
    }

    /**
     * Event indicating when a command button is pressed.
     * 
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     *      javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable)
    {
        Landmark landmark = null;

        // Add new Landmark to the LandmarkStore
        if (command == saveNewCmd)
        {
            landmark = generateLandmark();
            if (landmark != null)
            {
                try
                {
                    ControlPoints.getInstance().addLandmark(landmark);
                    data.createProximityListener(coord);
                }
                catch (IOException e)
                {
                    Alert alert = new Alert("Error",
                            "I/O Error during add operation", null,
                            AlertType.ERROR);
                    alert.setTimeout(3000); // 3 secs
                    TouristMIDlet.getDisplay().setCurrent(alert);
                }

                // New landmark is available on the list
                showList();
            }
        }
        // Update existing landmark
        else if (command == saveUpdatedCmd)
        {
            landmark = generateLandmark();
            if (landmark != null)
            {
                try
                {
                    ControlPoints.getInstance().updateLandmark(landmark);
                    data.createProximityListener(coord);
                }
                catch (IOException e)
                {
                    Alert alert = new Alert("Error",
                            "I/O Error during update operation", null,
                            AlertType.ERROR);
                    alert.setTimeout(3000); // 3 secs
                    TouristMIDlet.getDisplay().setCurrent(alert);
                }
                catch (LandmarkException e)
                {
                    // Landmark instance passed as the parameter does not
                    // belong to this LandmarkStore or does not exist in the
                    // store any more.
                    Alert alert = new Alert("Error",
                            "Unexpected error: can not find landmark from "
                            + "the landmark store.", null,
                            AlertType.ERROR);
                    alert.setTimeout(3000); // 3 secs
                    TouristMIDlet.getDisplay().setCurrent(alert);
                }

                // Updated landmark is available on the list
                showList();
            }
        }
        // Go back to the previous screen
        else if (command == routeCmd)
        {
            TouristMIDlet.getDisplay().setCurrent(route);
        }
        // Show landmark editor for the selected landmark.
        else if (command == updateCmd)
        {
            int index = list.getSelectedIndex();
            landmark = ControlPoints.getInstance().findLandmark(index);

            showEditor(landmark.getQualifiedCoordinates(), MODE_UPDATE);
        }
        // Remove selected Landmark from LandmarkStore
        else if (command == removeCmd)
        {
            try
            {
                int index = list.getSelectedIndex();
                landmark = ControlPoints.getInstance().findLandmark(index);
                ControlPoints.getInstance().removeLandmark(landmark);

                Alert alert = new Alert("Information",
                        "Landmark removed successfully.", null, AlertType.INFO);
                alert.setTimeout(3000); // 3 secs
                TouristMIDlet.getDisplay().setCurrent(alert);
            }
            catch (IOException e)
            {
                Alert alert = new Alert("Error", "I/O Error during operation",
                        null, AlertType.ERROR);
                alert.setTimeout(3000); // 3 secs
                TouristMIDlet.getDisplay().setCurrent(alert);
            }
            catch (LandmarkException le)
            {
                Alert alert = new Alert("Error",
                        "landmark can not be found from the landmark store.",
                        null, AlertType.ERROR);
                alert.setTimeout(3000); // 3 secs
                TouristMIDlet.getDisplay().setCurrent(alert);
            }

            showList();
        }
        // Show the list of Landmarks
        else if (command == listCmd)
        {
            showList();
        }
    }
}
