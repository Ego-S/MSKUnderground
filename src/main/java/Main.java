import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

public class Main {
	private static final String URL = "https://www.moscowmap.ru/metro.html#lines";
	private static final String DATA = "data/metrodata.json";

	public static void main(String[] args) {

		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(String.class, new StationsOnLinesDeserializer())
				.create();

		try {
			Document doc = Jsoup.connect(URL).maxBodySize(0).get();
			Element element = doc.getElementById("metrodata");
			MetroData metroData = createMetroDataFromElements(element);

			File file = new File(DATA);
			PrintWriter pw = new PrintWriter(file);
			pw.write(gson.toJson(metroData));
			pw.flush();
			pw.close();

			StringBuilder sb = new StringBuilder();
			Files.readAllLines(file.toPath()).forEach(sb::append);
			System.out.println(gson.fromJson(sb.toString(), String.class));


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static MetroData createMetroDataFromElements(Element element) {
		MetroData metroData = new MetroData();
		metroData.setLines(parseLinesFromElementsToMap(element));
		metroData.setStations(parseStationsFromElement(element));
		metroData.setConnections(parseConnectionsFromElement(element));
		return metroData;
	}

	private static HashSet<HashSet<Station>> parseConnectionsFromElement(Element metrodata) {
		HashSet<HashSet<Station>> connections = new HashSet<>();

		Elements lines = metrodata.select("div.t-metrostation-list-table");
		lines.forEach(line -> {
			String lineNumber = line.attr("data-line");
			line.select("p").forEach(station -> {
				HashSet<Station> connectedStations = new HashSet<>();
				if (!station.select("span.t-icon-metroln").isEmpty()) {
					Station from = new Station(lineNumber, station.select("span.name").text());
					Station to = new Station(station.select("span.t-icon-metroln").attr("class").substring(18), //18 - number of symbols before lineNumber in html
							getConnectedStationNameFromDescription(station));
					connectedStations.add(from);
					connectedStations.add(to);
					connections.add(connectedStations);
				}
			});
		});
		return connections;
	}

	private static LinkedHashMap<String, ArrayList<String>>  parseStationsFromElement (Element element) {
		LinkedHashMap<String, ArrayList<String>> stations = new LinkedHashMap<>();
		Elements elementsLines = element.select("div.t-metrostation-list-table");
		elementsLines.forEach(el -> {
			String lineNumber = el.attr("data-line");
			ArrayList<String> stationsNames = new ArrayList<>();
			el.select("p").forEach(e -> {
				String stationName = e.select("span.name").text();
				stationsNames.add(stationName);
			});
			stations.put(lineNumber, stationsNames);
		});
		return stations;
	}


	private static String getConnectedStationNameFromDescription(Element element) {
		String description = element.toString();
		description = description.substring(description.indexOf("\u00ab") + 1);
		description = description.substring(0, description.indexOf("\u00bb"));
		return description; //get substring from "<<" to ">>"
	}

	private static ArrayList<Lines> parseLinesFromElementsToMap(Element metrodata) {
		ArrayList<Lines> lines = new ArrayList<>();
		Elements elementsLines = metrodata.select("div.js-toggle-depend").select("span");
		elementsLines.forEach(element -> {
			String lineNumber = element.attr("data-line");
			String lineName = element.text();
			lines.add(new Lines(lineNumber, lineName));
		});
		return lines;
	}
}
