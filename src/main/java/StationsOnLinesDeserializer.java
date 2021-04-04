import com.google.gson.*;

import java.lang.reflect.Type;

public class StationsOnLinesDeserializer implements JsonDeserializer<String> {
	@Override
	public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonObject lines = jsonObject.get("stations").getAsJsonObject();
		StringBuilder result = new StringBuilder();
		for (String key : lines.keySet()) {
			JsonArray stations = lines.get(key).getAsJsonArray();
			result.append("Line ").append(key).append(": ").append(stations.size()).append(" stations\n");
		}
		return result.toString();
	}
}
