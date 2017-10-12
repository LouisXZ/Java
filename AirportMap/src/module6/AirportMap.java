package module6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PGraphics;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 * @author Xuhao Zhou
 *
 */
public class AirportMap extends PApplet {
	
	private static UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	
	private AirportMarker lastSelected;
	private AirportMarker lastClicked;
	
	private static HashMap<Location, Set<Location>> routeMap;
	private Set<Location> destList;

	// PGraphics layers
	private PGraphics keyBuffer;
	private static PGraphics textRectBuffer;
	
	// For initialize a map
	private static final float mapX = 210;
	private static final float mapY = 50;
	private final float mapWidth = 750;
	private final float mapHeight = 575;
	
	public void setup() {
		// setting up PAppler
		size(1000, 650, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, mapX, mapY, mapWidth, mapHeight, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		
		}
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeMap = new HashMap<Location, Set<Location>>();		
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));

			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));

				// TODO: build a hash map to record pairs of a source and its destinations
				if (routeMap.get(airports.get(source)) == null) {	
					destList = new HashSet<Location>();
					destList.add(airports.get(dest));
					routeMap.put(airports.get(source), destList); 
				} else {
					destList = routeMap.get(airports.get(source));
					destList.add(airports.get(dest));
					routeMap.put(airports.get(source), destList); 
				}
			}

			//SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());

			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			//routeList.add(sl);
		}
		
		// TODO: Only display airports that have at least one route.
		for (Iterator<Marker> it = airportList.iterator(); it.hasNext();) {
			Marker marker = it.next();
			if (routeMap.get(marker.getLocation()) == null) {
				it.remove();
			}
		}	
	
		// TODO:	
		// Add the number of routes and dests properties and then set the properties
		// Set colors of markers and routes based on the number of routes
		// Set radius of markers based on the number of routes
		for (Marker m : airportList) {
			int routeNum = 0;
			destList = routeMap.get(m.getLocation());
			
			if (destList != null && destList.size() > 0) {
				routeNum = destList.size();
			}
			HashMap<String, Object> properties = m.getProperties();		
			properties.put("routes", routeNum);
			
			// add destinations to marker's property list
			if (destList != null) {
				properties.put("dests", destList);
			} else {
				properties.put("dests", Collections.emptySet());
			}
			
			m.setProperties(properties);
			((AirportMarker) m).setRadius(map(routeNum, 1, 237, 3, 9));
			int colorLevel = (int) map(routeNum, 1, 237, 10, 255);
			m.setColor(color(colorLevel, 100, 255 - colorLevel));
			int lineColorLevel = (int) map(routeNum, 1, 237, 66, 177);
			((AirportMarker) m).setStrokeColor(color(66, lineColorLevel, 244));	
		}
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
//		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
		// TODO: Initialize PGraphics layers
		textRectBuffer = createGraphics(width, height);
		keyBuffer = createGraphics(width, height);
	}
	
	public void draw() {
		background(0);
		addKey();
		map.draw();
		image(textRectBuffer, 0, 0);
		image(keyBuffer, 0, 0);
	}
	
	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	public void addKey() {	
		
		float xBase = 25;
		float yBase = 50;
		keyBuffer.beginDraw();
		keyBuffer.pushStyle();
		keyBuffer.fill(255, 250, 240);
		keyBuffer.rect(xBase, yBase, 160, 180);
		
		keyBuffer.fill(0);
		keyBuffer.textSize(12);
		keyBuffer.text("Airport Key", xBase + 40, yBase + 25);
			
		keyBuffer.fill(25, 100, 230);
		keyBuffer.ellipse(xBase + 30, yBase + 50, 14, 14);
		keyBuffer.fill(128, 100, 127);
		keyBuffer.ellipse(xBase + 30, yBase + 75, 14, 14);
		keyBuffer.fill(180, 100, 75);
		keyBuffer.ellipse( xBase + 30, yBase + 100, 14, 14);
		keyBuffer.fill(255, 100, 0);
		keyBuffer.ellipse( xBase + 30, yBase + 125, 14, 14);
				
		keyBuffer.fill(0);		
		keyBuffer.text("< 60 Routes", xBase + 45, yBase + 55);
		keyBuffer.text("60 - 120 Routes", xBase + 45, yBase + 80);
		keyBuffer.text("120 - 180 Routes", xBase + 45, yBase + 105);
		keyBuffer.text("> 180 Routes", xBase + 45, yBase + 130);
		keyBuffer.text("Circle Size  ~  Routes", xBase + 20, yBase + 155);
		keyBuffer.popStyle();
		keyBuffer.endDraw();
	}

	// TODO		
	@Override
	public void keyPressed(){
		textRectBuffer.beginDraw();
		textRectBuffer.clear();
		textRectBuffer.endDraw();
	}
	
	// TODO
	@Override
	public void mouseDragged(){
		textRectBuffer.beginDraw();
		textRectBuffer.clear();
		textRectBuffer.endDraw();
	}
	
	// TODO
	@Override
	public void mouseReleased(){
		textRectBuffer.beginDraw();
		textRectBuffer.clear();
		textRectBuffer.endDraw();
	}
	
	// TODO
	@Override
	public void mouseMoved()
	{	
		if (mouseX > mapX && mouseX < mapX + mapWidth && 
			mouseY > mapY && mouseY < mapY + mapHeight) {
		// clear the last selection
			if (lastSelected != null) {
				lastSelected.setSelected(false);
				lastSelected = null;
				textRectBuffer.beginDraw();
				textRectBuffer.clear();
				textRectBuffer.endDraw();
			}
			selectMarkerIfHover(airportList);	
		}
	}
	
	// If there is a marker under the cursor, and lastSelected is null 
	// set the lastSelected to be the first marker found under the cursor
	// Make sure you do not select two markers.
	// 
	private void selectMarkerIfHover(List<Marker> markers) {
		// TODO: Implement this method
		for (Marker m : markers) {
			if (m.isInside(map, (float)mouseX, (float)mouseY)) {
				if (lastSelected == null) {
					lastSelected =  (AirportMarker) m;
					lastSelected.setSelected(true);
					break;
				} 
			}	
		}	
	}
	
	// If a airport is clicked, it will display the routes out of that airport.
	@Override
	public void mouseClicked()
	{
		// TODO: Implement this method	
		if (lastClicked != null) {
			lastClicked.setClicked(false);
			lastClicked = null;
			unhideMarkers();
		} else if (lastClicked == null && lastSelected != null) {
			lastClicked = lastSelected;
			lastClicked.setClicked(true);
			hideMarkers(lastClicked);
		}
	}
	
	// TODO: loop over and hide all city markers except one source and its destinations.
	private void hideMarkers(AirportMarker am) {
		destList = new HashSet<Location>();
		destList = am.getDests();
		
		if (destList.size() != 0) {
			for (Marker m : airportList) {
				m.setHidden(true);
			}
			for (Location loc : destList) {
				for (Marker m : airportList) {
					if (m.getLocation() == loc) {
						m.setHidden(false);
					}
				}
			}
		} else {
			System.out.println("There is no route out of the " + am.getName());
		}
		am.setHidden(false);
	}

	// TODO: loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}
	
	// TODO
	protected UnfoldingMap getMap() {
		return map;
	}
	
	// TODO
	protected PGraphics getTextRectBuffer() {
		return textRectBuffer;
	}
	
	// TODO
	protected float getMapX() {
		return mapX;
	}
	
	// TODO
	protected float getMapY() {
		return mapY;
	}
	
}
