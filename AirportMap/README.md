### Screenshots:

![Alt text](screenshots/airport.jpg?raw=true "airport")
![Alt text](screenshots/airport2.jpg?raw=true "airport2")
![Alt text](screenshots/airport3.jpg?raw=true "airport3")


### Functionalities this program includes:

* Display airports that have at least one route out of them rather than all airports.
* When the user clicks on one airport, display the routes out of that airport.
* Display additional information about airports when the user hovers over them.
* The pop-up info boxes about the airports are always drawn on top of the other graphical information (markers).


### Technical details:

* Display airports that have at least one route out of them rather than all airports.
	1. First, create a HashMap\<Location, Set\<Location\>\>, namely routeMap, to record pairs of the location of source airport and the locations of its destination airport. Then, iterate airportList that is an ArrayList<Marker>, for each marker in airportList, if the location of this marker as source airport location does not exist in routeMap, then remove this marker from airportList so that each airport marker in airportList has at least one destination airport.
	2. The colors of the airport markers are different based on the routes out of them using map() function from the Processing library.
	3. The sizes of the airport markers are different based on the routes out of them using map() function from the Processing library.
* When the user clicks on one airport, display the routes out of that airport.
	1. For each airport marker, I put the routes out of it to its marker properties using the key "dests". In the AirportMarker class, for each airport marker, use getProperty("dests") to get a set of destination locations. Then, draw curves between this marker and its destination locations.
	2. Using the curve() function from the Processing library to draw routes.
	3. The colors of the curves (routes) are different based on the number of routes using map() function from the Processing library.
* Display additional information (name, city, code, the number of routes out of the airport) about airports when the user hovers over them.
	1. The pop-up info boxes about the airports are always drawn on top of the screen. First, I draw these infoboxes (rectangles with text) to an additional variable textRectBuffer with PGraphics type. Then, call textRectBuffer with image(textRectBuffer, 0, 0) after map.draw() in the draw() function so that infoboxes are drawn on the top of the screen.


### Credit

* This airport map is based on starter code for the Object Oriented Programming in Java course offered by UC San Diego through Coursera.
* Unfolding - Map Library
* Airports/Routes data - OpenFlights.org