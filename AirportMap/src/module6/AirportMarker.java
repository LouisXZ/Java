package module6;

import java.util.HashMap;
import java.util.Set;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 * @author Xuhao Zhou
 *
 */
public class AirportMarker extends CommonMarker {
//	public static List<SimpleLinesMarker> routes;
	
	// TODO: Defining some variables 
	private final AirportMap am = new AirportMap();
	private final UnfoldingMap map = am.getMap();
	private final float offSetX = am.getMapX();
	private final float offSetY = am.getMapY();


	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		
		// draw circle marker
		pg.pushStyle();
		pg.fill(this.color);
		pg.stroke(this.color);
		pg.ellipse(x, y, this.radius, this.radius);
		pg.popStyle();
		

		// TODO: draw curves to represent routes

		Set<Location> cityList = this.getDests();
		
		if (cityList.size() != 0) {			
			for (Location cityLoc: cityList) {					
				ScreenPosition citySp = map.getScreenPosition(cityLoc);	
				float trueX = citySp.x - offSetX;
				float trueY = citySp.y - offSetY;
				double diffX = (double) trueX - (double) x;
				double diffY = (double) trueY - (double) y;
				float dist = (float) Math.sqrt(Math.pow(diffX, 2.0d) + Math.pow(diffY, 2.0d));
				
				if (getClicked() == true) {
					pg.pushStyle();
					pg.stroke(this.strokeColor);
					pg.noFill();
					pg.curve(trueX, trueY + dist, trueX, trueY, x, y, x, x + dist);
					pg.popStyle();
				} 
			}
		}
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // TODO: show rectangle with information
		int routes = 0;
		float maxWidth;

		PGraphics textRectBuffer = am.getTextRectBuffer();		
		routes = this.getRoutes();	
		String country = getCountry().replaceAll("^\"|\"$", "");
		String city = "City: " + getCity().replaceAll("^\"|\"$", "");
		String code = "Code: " + getCode().replaceAll("^\"|\"$", "");
		String name = "Name: " + getName().replaceAll("^\"|\"$", "");
		String routesNum = "Routes: " + routes;
		String cityCountry = city + ", " + country;
		maxWidth = Math.max(pg.textWidth(name), pg.textWidth(cityCountry));
		
		textRectBuffer.beginDraw();
		textRectBuffer.pushStyle();
		textRectBuffer.stroke(100);
		textRectBuffer.fill(255);
		textRectBuffer.rect(x + 10 + offSetX, y + offSetY, maxWidth + 15, 65, 10);
		textRectBuffer.fill(0);
		textRectBuffer.text(name, x + 16 + offSetX, y + 15 + offSetY);
		textRectBuffer.text(cityCountry, x + 16 + offSetX, y + 30 + offSetY);
		textRectBuffer.text(code, x + 16 + offSetX, y + 45 + offSetY);
		textRectBuffer.text(routesNum, x + 16 + offSetX, y + 60 + offSetY);
		textRectBuffer.popStyle();
		textRectBuffer.endDraw();
	}
		
		
	public String getCountry() {
		return (String) getProperty("country");	
	}
	
	public String getCode() {
		return (String) getProperty("code");
	}
	
	public String getCity() {
		return (String) getProperty("city");
	}
	
	public String getName() {
		return (String) getProperty("name");
	}
	
	public int getRoutes() {
		return (int) getProperty("routes");
	}
	
	public Set<Location> getDests() {
		return (Set<Location>) getProperty("dests");
	}
}
