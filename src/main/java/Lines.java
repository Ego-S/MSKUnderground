import java.util.ArrayList;

public class Lines {
	private String number;
	private String name;
	private ArrayList<Station> stations;

	public Lines(String number, String name) {
		this.number = number;
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Station> getStations() {
		return stations;
	}

	public void addStation(Station station) {
		stations.add(station);
	}

	public void addAllStations(ArrayList<Station> stations) {
		this.stations.addAll(stations);
	}
}

