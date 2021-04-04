import java.util.TreeSet;

public class Station {
	private String lineNumber;
	private String name;
	private TreeSet<Station> connectedStations;

	public Station(){
	}

	public Station (String lineNumber, String name) {
		this.lineNumber = lineNumber;
		this. name = name;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeSet<Station> getConnectedStations(){
		return connectedStations;
	}

	public void addConnectedStation(Station station) {
		connectedStations.add(station);
	}

	public void addAllConnectedStations(TreeSet<Station> stations) {
		connectedStations.addAll(stations);
	}
}
