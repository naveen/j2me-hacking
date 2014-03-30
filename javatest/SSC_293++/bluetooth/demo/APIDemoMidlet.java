import java.util.Enumeration;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sirf.microedition.location.AddressInfo;
import com.sirf.microedition.location.Coordinates;
import com.sirf.microedition.location.Landmark;
import com.sirf.microedition.location.extensions.POIServiceProvider;
import com.sirf.microedition.location.extensions.SiRFProviderManager;
import com.sirf.microedition.location.extensions.SystemConfiguration;
import com.sirf.microedition.location.services.CircleGeographicArea;
import com.sirf.microedition.location.services.GeocodingServiceProvider;
import com.sirf.microedition.location.services.GeographicArea;
import com.sirf.microedition.location.services.MapLayer;
import com.sirf.microedition.location.services.MapServiceListener;
import com.sirf.microedition.location.services.MapServiceProvider;
import com.sirf.microedition.location.services.NavigationServiceListener;
import com.sirf.microedition.location.services.NavigationServiceProvider;
import com.sirf.microedition.location.services.ProviderInfo;
import com.sirf.microedition.location.services.ProviderManager;
import com.sirf.microedition.location.services.Route;
import com.sirf.microedition.location.services.RouteSegment;
import com.sirf.microedition.location.services.ServiceException;
import com.sirf.microedition.location.services.ServiceProvider;

/**
 * MIDlet that demostrates how to use the SiRFstudio APIs and its service providers.
 */
public class APIDemoMidlet extends MIDlet implements CommandListener, MapServiceListener, NavigationServiceListener {

//	private static final String DEFAULT_LAT = "40.002722";
//	private static final String DEFAULT_LON = "116.365451";
	private static final String DEFAULT_LAT = "37.374898";
	private static final String DEFAULT_LON = "-121.913009";
	
	private Display display;
	private String appKey;

	private Command exitCmd = new Command("Exit", Command.EXIT, 0);
	private Command selectCmd = new Command("Select", Command.OK, 0);
	private Command okCmd = new Command("Ok", Command.OK, 0);
	private Command doGeocodingCmd = new Command("Geocode", Command.OK, 0);
	private Command doRevGeocodingCmd = new Command("Reverse geocode", Command.OK, 0);
	private Command useRouteCmd = new Command("Use hardcoded route(US)", Command.OK, 0);
	
	// main menu
	private Form mainMenu;
	private ChoiceGroup mainMenu_choices;
	
	private TextField latField;
	private TextField lonField;
	private Form resultsForm = new Form("Results");
	private String message;
	
	// POI
	private Form poi_searchForm;
	private TextField poi_keywordField;
	private TextField poi_radiusField;
	private Form poiProvidersForm;
	private ChoiceGroup poiProvidersForm_choices;
	private POIServiceProvider poiProvider;
	private ChoiceGroup poi_categoryChoiceGroup;
	// MAP
	private Form mapProvidersForm;
	private MapServiceProvider mapProvider;
	private ChoiceGroup mapProvidersForm_choices;
	private Form map_pointForm;
	// GEOCODING
	private Form geocodingProvidersForm;
	private GeocodingServiceProvider geocodingProvider;
	protected ChoiceGroup geocodingProvidersForm_choices;
	private Form geocodingForm;
	private TextField streetField;
	private TextField cityField;
	private TextField postalcodeField;
	private TextField countrycodeField;
	// NAVIGATION
	private Form navigationProvidersForm;
	protected NavigationServiceProvider navigationProvider;
	protected ChoiceGroup navigationProvidersForm_choices;
	private Form naviForm;
	private TextField navi_endLatField;
	private TextField navi_endLonField;
	
	// app id form
	private Form appKeyForm;
	private TextField appKeyForm_appid;


	

	public APIDemoMidlet() {
		display = Display.getDisplay(this);
		
		appKeyForm = new Form("App ID");
		appKeyForm.setCommandListener(this);
		appKeyForm.addCommand(okCmd);
		appKeyForm_appid = new TextField("App ID", "", 100, TextField.ANY);
		appKeyForm.append(appKeyForm_appid);
		display.setCurrent(appKeyForm);

		// init the main menu
		mainMenu = new Form("Main");
		mainMenu.setCommandListener(this);
		mainMenu.addCommand(exitCmd);
		mainMenu.addCommand(selectCmd);
		mainMenu_choices = new ChoiceGroup("Services", Choice.EXCLUSIVE, new String[] {"Geocoding", "Mapping", "Navigation", "POI search"}, null);
		mainMenu.append(mainMenu_choices);

		SystemConfiguration.display = display;
		
		// init the results form
        resultsForm.addCommand(okCmd);
        resultsForm.setCommandListener(this);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
	}

	public void commandAction(Command c, Displayable d) {
		if(d == appKeyForm && c == okCmd) {
			appKey = appKeyForm_appid.getString();
			display.setCurrent(mainMenu);
		}
		else if(d == mainMenu && c == exitCmd) {
			notifyDestroyed();
		} else if(d == mainMenu && c == selectCmd) {
			switch (mainMenu_choices.getSelectedIndex()) {
				case 0:
					retrieveGeocodingProviders();
					break;
				case 1:
					retrieveMapProviders();
					break;
				case 2:
					retrieveNavigationProviders();
					break;
				case 3:
					retrievePOIProviders();
					break;
			}			
		} else if(d == poiProvidersForm && c == selectCmd) {
			String name = poiProvidersForm_choices.getString(poiProvidersForm_choices.getSelectedIndex());
			poiProvider = (POIServiceProvider)SiRFProviderManager.connectToServiceProvider(name, SiRFProviderManager.POI, appKey);
			showPOISearchForm();
		} else if(d == mapProvidersForm && c == selectCmd) {
			String name = mapProvidersForm_choices.getString(mapProvidersForm_choices.getSelectedIndex());
			mapProvider = (MapServiceProvider)ProviderManager.connectToServiceProvider(name, ProviderManager.MAP, appKey);
			showMapPointForm();
		} else if(d == geocodingProvidersForm && c == selectCmd) {
			String name = geocodingProvidersForm_choices.getString(geocodingProvidersForm_choices.getSelectedIndex());
			geocodingProvider = (GeocodingServiceProvider)ProviderManager.connectToServiceProvider(name, ProviderManager.GEOCODING, appKey);
			showGeocodingForm();
		} else if(d == navigationProvidersForm && c == selectCmd) {
			String name = navigationProvidersForm_choices.getString(navigationProvidersForm_choices.getSelectedIndex());
			navigationProvider = (NavigationServiceProvider)ProviderManager.connectToServiceProvider(name, ProviderManager.NAVIGATION, appKey);
			showNavigationForm();
		} else if(d == poi_searchForm && c == okCmd) {
			doSearch();
		} else if(d == map_pointForm && c == okCmd) {
			doShowMap();
		}  else if(d == naviForm && c == okCmd) {
			Coordinates[] waypoints = new Coordinates[] {
					new Coordinates(Double.valueOf(latField.getString()).doubleValue(), Double.valueOf(lonField.getString()).doubleValue(), 0.0f),
					new Coordinates(Double.valueOf(navi_endLatField.getString()).doubleValue(), Double.valueOf(navi_endLonField.getString()).doubleValue(), 0.0f),
			};
			try {
				navigationProvider.navigate(waypoints, null, this);
			} catch (ServiceException e) {
				Alert alert = new Alert("", "Sorry, error. ["+e+"]", null, AlertType.INFO );
	        	alert.setTimeout(Alert.FOREVER);
	        	display.setCurrent(alert, mainMenu);
			}
		} else if(d == naviForm && c == useRouteCmd) {
			try {
				navigationProvider.navigate(getTestRoute(), null, this);
			} catch (ServiceException e) {
				Alert alert = new Alert("", "Sorry, error. ["+e+"]", null, AlertType.INFO );
	        	alert.setTimeout(Alert.FOREVER);
	        	display.setCurrent(alert, mainMenu);
			}
		} else if(d == geocodingForm && c == doRevGeocodingCmd) {
			doReverseGeocoding();
		} else if(d == geocodingForm && c == doGeocodingCmd) {
			doGeocoding();
		} else if(d == resultsForm && c == okCmd) {
			display.setCurrent(mainMenu);
		}
	}


	private void retrieveGeocodingProviders() {
		geocodingProvidersForm = new Form("");
		geocodingProvidersForm.append("Please wait, fetching providers...");
		geocodingProvidersForm.setCommandListener(this);
		display.setCurrent(geocodingProvidersForm);
		Thread poiProviderFetcher = new Thread() {

			public void run() {
				try {
					ProviderInfo[] infos = ProviderManager.findServiceProviders(ProviderManager.GEOCODING, appKey);
					if(infos.length == 1) {
						geocodingProvider = (GeocodingServiceProvider)ProviderManager.connectToServiceProvider(infos[0], appKey);
						showGeocodingForm();
					} else if(infos.length > 1) {
						geocodingProvidersForm.deleteAll();
						String[] providerNames = new String[infos.length];
						for(int i=0; i<infos.length; i++) {
							providerNames[i] = infos[i].getName();
						}
						geocodingProvidersForm_choices = new ChoiceGroup("Select provider to be used:", Choice.EXCLUSIVE, providerNames, null);
						geocodingProvidersForm.append(geocodingProvidersForm_choices);
						geocodingProvidersForm.addCommand(selectCmd);
						display.setCurrent(geocodingProvidersForm);
					} else {
						Alert alert = new Alert("", "Sorry, no geocoding providers configured.", null, AlertType.INFO);
						alert.setTimeout(Alert.FOREVER);
						display.setCurrent(alert, mainMenu);
					}
				} catch (ServiceException e) {
					Alert alert = new Alert("Error", "Sorry, could not fetch provider capabilities. ["+e+"]", null, AlertType.ERROR );
					alert.setTimeout(Alert.FOREVER);
					display.setCurrent(alert, mainMenu);
				}
			}
		};
		poiProviderFetcher.start();
	}


	private void retrievePOIProviders() {
		poiProvidersForm = new Form("");
		poiProvidersForm.append("Please wait, fetching providers...");
		poiProvidersForm.setCommandListener(this);
		display.setCurrent(poiProvidersForm);
		Thread poiProviderFetcher = new Thread() {
			public void run() {
				try {
					ProviderInfo[] infos = SiRFProviderManager.findServiceProviders(SiRFProviderManager.POI, appKey);
					if(infos.length == 1) {
						poiProvider = (POIServiceProvider)SiRFProviderManager.connectToServiceProvider(infos[0], appKey);
						showPOISearchForm();
					} else if(infos.length > 1) {
						poiProvidersForm.deleteAll();
						String[] providerNames = new String[infos.length];
						for(int i=0; i<infos.length; i++) {
							providerNames[i] = infos[i].getName();
						}
						poiProvidersForm_choices = new ChoiceGroup("Select provider to be used:", Choice.EXCLUSIVE, providerNames, null);
						poiProvidersForm.append(poiProvidersForm_choices);
						poiProvidersForm.addCommand(selectCmd);
						display.setCurrent(poiProvidersForm);
					} else {
						Alert alert = new Alert("", "Sorry, no POI providers configured.", null, AlertType.INFO);
						alert.setTimeout(Alert.FOREVER);
						display.setCurrent(alert, mainMenu);
					}
				} catch (ServiceException e) {
					Alert alert = new Alert("Error", "Sorry, could not fetch provider capabilities. ["+e+"]", null, AlertType.ERROR );
					alert.setTimeout(Alert.FOREVER);
					display.setCurrent(alert, mainMenu);
				}
			}
		};
		poiProviderFetcher.start();
	}
	
	private void retrieveMapProviders() {
		mapProvidersForm = new Form("");
		mapProvidersForm.append("Please wait, fetching providers...");
		mapProvidersForm.setCommandListener(this);
		display.setCurrent(mapProvidersForm);
		Thread poiProviderFetcher = new Thread() {
			public void run() {
				try {
					ProviderInfo[] infos = ProviderManager.findServiceProviders(ProviderManager.MAP, appKey);
					if(infos.length == 1) {
						mapProvider = (MapServiceProvider)ProviderManager.connectToServiceProvider(infos[0], appKey);
						showMapPointForm();
					} else if(infos.length > 1) {
						mapProvidersForm.deleteAll();
						String[] providerNames = new String[infos.length];
						for(int i=0; i<infos.length; i++) {
							providerNames[i] = infos[i].getName();
						}
						mapProvidersForm_choices = new ChoiceGroup("Select provider to be used:", Choice.EXCLUSIVE, providerNames, null);
						mapProvidersForm.append(mapProvidersForm_choices);
						mapProvidersForm.addCommand(selectCmd);
						display.setCurrent(mapProvidersForm);
					} else {
						Alert alert = new Alert("", "Sorry, no map providers configured.", null, AlertType.INFO);
						alert.setTimeout(Alert.FOREVER);
						display.setCurrent(alert, mainMenu);
					}
				} catch (ServiceException e) {
					Alert alert = new Alert("Error", "Sorry, could not fetch provider capabilities. ["+e+"]", null, AlertType.ERROR );
					alert.setTimeout(Alert.FOREVER);
					display.setCurrent(alert, mainMenu);
				}
			}
		};
		poiProviderFetcher.start();
	}
	
	private void retrieveNavigationProviders() {
		navigationProvidersForm = new Form("");
		navigationProvidersForm.append("Please wait, fetching providers...");
		navigationProvidersForm.setCommandListener(this);
		display.setCurrent(navigationProvidersForm);
		Thread providerFetcher = new Thread() {
			public void run() {
				try {
					ProviderInfo[] infos = ProviderManager.findServiceProviders(ProviderManager.NAVIGATION, appKey);
					if(infos.length == 1) {
						navigationProvider = (NavigationServiceProvider)ProviderManager.connectToServiceProvider(infos[0], appKey);
						showNavigationForm();
					} else if(infos.length > 1) {
						navigationProvidersForm.deleteAll();
						String[] providerNames = new String[infos.length];
						for(int i=0; i<infos.length; i++) {
							providerNames[i] = infos[i].getName();
						}
						navigationProvidersForm_choices = new ChoiceGroup("Select provider to be used:", Choice.EXCLUSIVE, providerNames, null);
						navigationProvidersForm.append(navigationProvidersForm_choices);
						navigationProvidersForm.addCommand(selectCmd);
						display.setCurrent(navigationProvidersForm);
					} else {
						Alert alert = new Alert("", "Sorry, no providers configured.", null, AlertType.INFO);
						alert.setTimeout(Alert.FOREVER);
						display.setCurrent(alert, mainMenu);
					}
				} catch (ServiceException e) {
					Alert alert = new Alert("Error", "Sorry, could not fetch provider capabilities. ["+e+"]", null, AlertType.ERROR );
					alert.setTimeout(Alert.FOREVER);
					display.setCurrent(alert, mainMenu);
				}
			}
		};
		providerFetcher.start();
	}


	private void showPOISearchForm() {
        poi_searchForm = new Form("Search POI");
        poi_keywordField = new TextField("Keyword", "", 100, TextField.ANY);
        latField = new TextField("Your latitude", DEFAULT_LAT, 11, TextField.DECIMAL);
        lonField = new TextField("Your longitude", DEFAULT_LON, 11, TextField.DECIMAL);
        poi_radiusField = new TextField("Search radius (m)", "10000", 6, TextField.NUMERIC);
        poi_searchForm.append(poi_keywordField);
        poi_searchForm.append(latField);
        poi_searchForm.append(lonField);
        poi_searchForm.append(poi_radiusField);
        poi_searchForm.addCommand(okCmd);
        poi_searchForm.setCommandListener(this);
        try {
			poi_categoryChoiceGroup = new ChoiceGroup("Category", Choice.POPUP, ((POIServiceProvider)poiProvider).getCategories(), null);
		} catch (ServiceException e) {
			poi_searchForm.append("Error with categories! ["+e+"]");
			poi_categoryChoiceGroup = null;
		}
        poi_searchForm.append(poi_categoryChoiceGroup);
        display.setCurrent(poi_searchForm);
	}
	
	private void showMapPointForm() {
        map_pointForm = new Form("Select point to be shown.");
        latField = new TextField("Your latitude", DEFAULT_LAT, 11, TextField.DECIMAL);
        lonField = new TextField("Your longitude", DEFAULT_LON, 11, TextField.DECIMAL);
        map_pointForm.append(latField);
        map_pointForm.append(lonField);
        map_pointForm.addCommand(okCmd);
        map_pointForm.setCommandListener(this);
        display.setCurrent(map_pointForm);
	}
	

	private void showNavigationForm() {
		// route in US
        naviForm = new Form("Select points to be navigated.");
        latField = new TextField("Start latitude", "37.374898", 12, TextField.DECIMAL);
        lonField = new TextField("Start longitude", "-121.913009", 12, TextField.DECIMAL);
        navi_endLatField = new TextField("End latitude", "37.366917", 12, TextField.DECIMAL);
        navi_endLonField = new TextField("End longitude", "-121.926012", 12, TextField.DECIMAL);
//        Route in China
//        latField = new TextField("Start latitude", "31.305894", 12, TextField.DECIMAL);
//        lonField = new TextField("Start longitude", "121.411656", 12, TextField.DECIMAL);
//        navi_endLatField = new TextField("End latitude", "31.336831", 12, TextField.DECIMAL);
//        navi_endLonField = new TextField("End longitude", "121.410658", 12, TextField.DECIMAL);
        
        naviForm.append(latField);
        naviForm.append(lonField);
        naviForm.append(navi_endLatField);
        naviForm.append(navi_endLonField);
        naviForm.addCommand(okCmd);
        naviForm.addCommand(useRouteCmd);
        naviForm.setCommandListener(this);
        display.setCurrent(naviForm);

	}
	

	private void showGeocodingForm() {
        geocodingForm = new Form("");
        latField = new TextField("Your latitude", DEFAULT_LAT, 11, TextField.DECIMAL);
        lonField = new TextField("Your longitude", DEFAULT_LON, 11, TextField.DECIMAL);
        streetField = new TextField("Street", "", 50, TextField.ANY);
        cityField = new TextField("City", "", 50, TextField.ANY);
        postalcodeField = new TextField("Postal code", "", 50, TextField.NUMERIC);
        countrycodeField = new TextField("Country code", "", 50, TextField.ANY);
        geocodingForm.append(latField);
        geocodingForm.append(lonField);
        geocodingForm.append(streetField);
        geocodingForm.append(cityField);
        geocodingForm.append(postalcodeField);
        geocodingForm.append(countrycodeField);
        geocodingForm.addCommand(doGeocodingCmd);
        geocodingForm.addCommand(doRevGeocodingCmd);
        geocodingForm.setCommandListener(this);
        display.setCurrent(geocodingForm);
	}

	
    public void doSearch() {
        Display.getDisplay(this).setCurrent(resultsForm);
        resultsForm.deleteAll();
        resultsForm.append("Please wait while searching...");
        display.setCurrent(resultsForm);
        
        Thread runner = new Thread() {
            public void run() {
                try {
                    String keyword = poi_keywordField.getString();
                    if(keyword.length() == 0)
                        keyword = null;
                    String category = null;
                    if(poi_categoryChoiceGroup != null) {
                    	category = poi_categoryChoiceGroup.getString(poi_categoryChoiceGroup.getSelectedIndex());
                    }

                    double lat = Double.valueOf(latField.getString()).doubleValue();
                    double lon = Double.valueOf(lonField.getString()).doubleValue();
                    int radius = Integer.valueOf(poi_radiusField.getString()).intValue();

                    Landmark[] results = poiProvider.getPOIs(category, keyword, new CircleGeographicArea(new Coordinates(lat, lon, 0.0f), radius, 0.0f), 20);
                    resultsForm.deleteAll();
                    if(results != null && results.length>0) {
                    	for(int i=0; i<results.length; i++) {
                    		resultsForm.append(results[i].toString());
                    	}
                    } else {
                    	Alert alert = new Alert("", "Sorry, no results.", null, AlertType.INFO );
                    	alert.setTimeout(Alert.FOREVER);
                    	display.setCurrent(alert, mainMenu);
                    }
                } catch (Exception e) {
                	Alert alert = new Alert("", "Sorry, error. ["+e+"]", null, AlertType.INFO );
                	alert.setTimeout(Alert.FOREVER);
                	display.setCurrent(alert, mainMenu);
                }
            }
        };
        runner.start();
    }

	private void doShowMap() {
		double lat = Double.valueOf(latField.getString()).doubleValue();
        double lon = Double.valueOf(lonField.getString()).doubleValue();
		try {
			mapProvider.displayMap(
					null,
					null,
					new Coordinates(lat, lon, 0.0f),
					null,
					MapServiceProvider.MAP_TYPE_REGULAR,
					MapServiceProvider.SELECT_CIRCLE|MapServiceProvider.SELECT_LANDMARKS|MapServiceProvider.SELECT_LOCATION|MapServiceProvider.SELECT_POLYGON|MapServiceProvider.SELECT_RECTANGLE,
					true,
					true,
					this);
		} catch (ServiceException e) {
			Alert alert = new Alert("", "Sorry, error. ["+e+"]", null, AlertType.INFO );
        	alert.setTimeout(Alert.FOREVER);
        	display.setCurrent(alert, mainMenu);
		}
		
	}



	private void doGeocoding() {
        final AddressInfo address = new AddressInfo();
        address.setField(AddressInfo.STREET, streetField.getString());
        address.setField(AddressInfo.CITY, cityField.getString());
        address.setField(AddressInfo.POSTAL_CODE, postalcodeField.getString());
        address.setField(AddressInfo.COUNTRY_CODE, countrycodeField.getString());
        Thread runner = new Thread() {
            public void run() {
                try {
                    Enumeration enumeration = geocodingProvider.geocode(address, true, null);
                    resultsForm.deleteAll();
                    int count = 0;
                    while (enumeration.hasMoreElements()){
                        Landmark landmark = (Landmark)enumeration.nextElement();
                        count++;
                        AddressInfo address = landmark.getAddressInfo();
                        if (address != null) {
                        	resultsForm.append("["+count+"]"+address.toString());
                        }
                    }
                    if (count == 0)
                    	resultsForm.append("Sorry, no results.");
                } catch (ServiceException e) {
                	resultsForm.deleteAll();
                	resultsForm.append("Sorry, error in service.");
                } catch (InterruptedException e) {
                	resultsForm.deleteAll();
                	resultsForm.append("Cancelled by user.");
                }
                display.setCurrent(resultsForm);
            }
        };
        runner.start();
	}
	
	private void doReverseGeocoding() {
		final double lat = Double.valueOf(latField.getString()).doubleValue();
		final double lon = Double.valueOf(lonField.getString()).doubleValue();
        Thread runner = new Thread() {
            public void run() {
                try {
                    Enumeration enumeration = geocodingProvider.reverseGeocode(new Coordinates(lat, lon, 0.0f), true);
                    resultsForm.deleteAll();
                    int count = 0;
                    while (enumeration.hasMoreElements()){
                        Landmark landmark = (Landmark)enumeration.nextElement();
                        count++;
                        AddressInfo address = landmark.getAddressInfo();
                        if (address != null) {
                        	resultsForm.append("["+count+"]"+address.toString());
                        }
                    }
                    if (count == 0)
                    	resultsForm.append("Sorry, no results.");
                } catch (ServiceException e) {
                	resultsForm.deleteAll();
                	resultsForm.append("Sorry, error in service."+e);
                } catch (InterruptedException e) {
                	resultsForm.deleteAll();
                	resultsForm.append("Cancelled by user.");
                }
                display.setCurrent(resultsForm);
            }
        };
        runner.start();
	}

	
	private Route getTestRoute() {
		// create a route
		Coordinates[] geometry = new Coordinates[] {
				new Coordinates(37.374898, -121.913009, 0.0f),
				new Coordinates(37.374250, -121.913953, 0.0f),
				new Coordinates(37.375716, -121.915669, 0.0f),
				new Coordinates(37.367156, -121.925325, 0.0f),
				new Coordinates(37.368963, -121.927128, 0.0f),
				new Coordinates(37.370942, -121.929059, 0.0f),
				new Coordinates(37.371385, -121.930432, 0.0f),
				new Coordinates(37.371112, -121.930604, 0.0f),
				new Coordinates(37.366917, -121.926012, 0.0f)
		};
		RouteSegment segment = new RouteSegment(geometry);
		segment.setDescription("This is description.");
		segment.setTravelTime(2000);
		segment.addInstruction("Start", new Coordinates(37.374898, -121.913009, 0.0f));
		segment.addInstruction("Turn left on Devcon Dr", new Coordinates(37.374250, -121.913953, 0.0f));
		segment.addInstruction("Turn right on E Brokaw Rd", new Coordinates(37.375716, -121.915669, 0.0f));
		segment.addInstruction("Turn right on Airport Blvd", new Coordinates(37.367156, -121.925325, 0.0f));
		segment.addInstruction("Keep tight left", new Coordinates(37.371385, -121.930432, 0.0f));
		segment.addInstruction("Arrive at terminal B", new Coordinates(37.366917, -121.926012, 0.0f));
		return new Route(new RouteSegment[] {segment});
	}
	
	// MAPSERVISELISTENER methods
	////////////////////////////////////////////////////////////
	public void mapChanged(MapLayer map) {
		message += "mapChanged()!";
	}

	public void selectionUpdated(GeographicArea[] area, Landmark[] landmarks, Coordinates[] coordinates) {
		StringBuffer selected = new StringBuffer();
		selected.append("\nSelected areas:");
		for(int i=0; i<area.length; i++) {
			selected.append("\n["+i+"]:"+area[i].toString());
		}
		selected.append("\nSelected landmarks:");
		for(int i=0; i<landmarks.length; i++) {
			selected.append("\n["+i+"]:"+landmarks[i].toString());
		}
		selected.append("\nSelected coordinates:");
		for(int i=0; i<coordinates.length; i++) {
			selected.append("\n["+i+"]:"+coordinates[i].toString());
		}
		message = selected.toString();
	}

	public void requestCanceled(ServiceProvider provider) {
		Alert alert = new Alert("", "\nrequestCanceled()! " + message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert, mainMenu);
	}

	public void requestCompleted(ServiceProvider provider) {
		Alert alert = new Alert("", "\nrequestCompleted()! " + message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert, mainMenu);
		message = null;
	}

	public void requestError(ServiceProvider provider) {
		Alert alert = new Alert("", "\nrequestError()! " + message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert, mainMenu);
	}

	public void requestReset(ServiceProvider provider) {
		Alert alert = new Alert("", "\nrequestReset()! " + message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert, mainMenu);
	}

	public void requestTimeout(ServiceProvider provider) {
		Alert alert = new Alert("", "\nrequestTimeout()! " + message, null, AlertType.INFO);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert, mainMenu);
	}

	public void destinationReached() {
		// TODO Auto-generated method stub
		
	}

	public void routeCalculated() {
		// TODO Auto-generated method stub
		
	}

	public void routeChanged() {
		// TODO Auto-generated method stub
		
	}

	public void waypointReached(Coordinates arg0) {
		// TODO Auto-generated method stub
		
	}
}
