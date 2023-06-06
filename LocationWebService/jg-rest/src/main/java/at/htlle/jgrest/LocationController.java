package at.htlle.jgrest;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
public class LocationController {
	
	public ArrayList<Location> knownLocations = new ArrayList<Location>();
	
	private static String localUrl = "C:\\Users\\janni\\Downloads\\srtm_40_03\\srtm_40_03.asc";
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@PostConstruct
	public void init() {
		System.out.println("Ich wurde erzeugt");
		knownLocations.add(new Location("Leoben", 47.383333, 15.1));
		knownLocations.add(new Location("Bruck", 47.416667, 15.266667));
		knownLocations.add(new Location("Kapfenberg", 47.433333, 15.316667));
		knownLocations.add(new Location("Mariazell", 47.769722, 15.316667));
		knownLocations.add(new Location("Graz", 47.066667, 15.45));
		knownLocations.add(new Location("Vienna", 48.2082, 16.3738));
		knownLocations.add(new Location("Linz", 48.3064, 14.2858));
		knownLocations.add(new Location("Graz", 47.0707, 15.4395));
		knownLocations.add(new Location("Salzburg", 47.8095, 13.0550));
		knownLocations.add(new Location("Innsbruck", 47.2682, 11.3923));
		knownLocations.add(new Location("Klagenfurt", 46.6249, 14.3050));
		knownLocations.add(new Location("Villach", 46.6111, 13.8558));
		knownLocations.add(new Location("Wels", 48.1575, 14.0289));
		knownLocations.add(new Location("St. Pölten", 48.2047, 15.6256));
		knownLocations.add(new Location("Dornbirn", 47.4125, 9.7417));
		knownLocations.add(new Location("Wiener Neustadt", 47.8151, 16.2465));
		knownLocations.add(new Location("Bregenz", 47.5031, 9.7471));
		knownLocations.add(new Location("Eisenstadt", 47.8450, 16.5336));
		knownLocations.add(new Location("Leonding", 48.2606, 14.2406));
		knownLocations.add(new Location("Traun", 48.2203, 14.2333));
		knownLocations.add(new Location("Amstetten", 48.1219, 14.8747));
		knownLocations.add(new Location("Klosterneuburg", 48.3053, 16.3256));
		knownLocations.add(new Location("Schwechat", 48.1381, 16.4708));
		knownLocations.add(new Location("Ternitz", 47.7275, 16.0361));
		knownLocations.add(new Location("Baden bei Wien", 48.0069, 16.2308));
	}

	@GetMapping("/location")
	public Location location(@RequestParam(value = "name", defaultValue = "World") String locationName) {
		for(int i = 0; i < knownLocations.size(); i++) {
			if(knownLocations.get(i).getName().equalsIgnoreCase(locationName)) {
				return knownLocations.get(i);
			}
		}
		return null;
	}
	
	@GetMapping("/nearestLocation")
	public Location nearestLocation(@RequestParam(value = "Lat", defaultValue = "0") Double Lat, @RequestParam(value = "Long", defaultValue = "0") Double Long) {
		Location ui = new Location("user Location", Lat, Long);
		
		Double nearestLat;
		Double nearestLong;
		
		int locIndex = 0;
		Double oldDistance = 0.0;
		
		for(int i = 0; i < knownLocations.size(); i++) {
			if(i == 0) {
				oldDistance = ui.distanceTo(knownLocations.get(i));
				locIndex = i;
			} else if(i > 0) {
				if(ui.distanceTo(knownLocations.get(i)) < oldDistance) {
					oldDistance = ui.distanceTo(knownLocations.get(i));
					locIndex = i;
				}
			}	
		}
		return knownLocations.get(locIndex);
	}


	@GetMapping("/altitudeLocation")
	public Optional<Double> altitudeLocation(@RequestParam(value = "name", defaultValue = "World") String locationName) {
		String fileLoc = "C:\\Users\\janni\\Downloads\\srtm_40_03\\srtm_40_03.asc";
		SrtmFile file = new SrtmFile(new File(fileLoc));
		
		for(int i = 0; i < knownLocations.size(); i++) {
			if(knownLocations.get(i).getName().equalsIgnoreCase(locationName)) {
				return file.getAltitudeForLocation(knownLocations.get(i));
			}
		}
		return null;
	}
}
