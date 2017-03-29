package module3;

//Java utilities libraries
import java.awt.*;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Jonathan Grim
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
//	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.HybridProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);

			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int red = color(255, 0, 0);
	    int green = color(0, 255, 0);
	    
	    //TODO: Add code here as appropriate
//		Location loc = new Location(-38.14f, -73.03f);
//		Feature valEQ = new PointFeature(loc);
//		valEQ.addProperty("title", "Chile");
//		valEQ.addProperty("magnitude", "9.5");
//		Marker valMk = new SimplePointMarker(loc, valEQ.getProperties());
//		map.addMarker(valMk);

//		for each in - for (Type var: list_name) { }

		for (PointFeature earthquake: earthquakes) {
			SimplePointMarker eq = new SimplePointMarker(earthquake.getLocation(), earthquake.getProperties());
			Object mag = eq.getProperty("magnitude");
			Float magnitude = Float.parseFloat(mag.toString());
			if (magnitude > THRESHOLD_MODERATE) {
				eq.setRadius(16f);
			} else if (magnitude > THRESHOLD_LIGHT) {
				eq.setRadius(8f);
			} else {
				eq.setRadius(4f);
			}
//			eq.setRadius(magnitude);
			markers.add(eq);
		}

		for (Marker marker: markers) {

			Object mag = marker.getProperty("magnitude");
			float magnitude = Float.parseFloat(mag.toString());

			if (magnitude > THRESHOLD_MODERATE) {
				marker.setColor(red);
			} else if (magnitude > THRESHOLD_LIGHT) {
				marker.setColor(yellow);
			} else {
				marker.setColor(green);
			}
			map.addMarker(marker);
		}

	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		return new SimplePointMarker(feature.getLocation());
	}
	
	public void draw() {
	    background(200);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		int yellow = color(255, 255, 0);
		int red = color(255, 0, 0);
		int green = color(0, 255, 0);
		int black = color(0, 0, 0);
		int white = color(255, 255, 255);

		fill(white);
		rect(25, 100, 150, 300);
		textSize(16);
		fill(black);
		text("Legend:", 60, 125);

		// Moderate magnitude
		fill(red);
		ellipse(45, 160, 10, 10);
		fill(black);
		textSize(12);
		text("Moderate", 65, 165);
		text("Magnitude > 5.0", 40, 185);

		// Light magnitude
		fill(yellow);
		ellipse(45, 260, 10, 10);
		fill(black);
		textSize(12);
		text("Light", 65, 265);
		text("Magnitude > 4.0", 40, 285);

		// Minor magnitude
		fill(green);
		ellipse(45, 360, 10, 10);
		fill(black);
		textSize(12);
		text("Minor", 65, 365);
		text("Magnitude > 2.0", 40, 385);
	}
}
