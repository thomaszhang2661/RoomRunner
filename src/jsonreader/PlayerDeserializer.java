package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import enginedriver.Player;

import java.io.IOException;

public class PlayerDeserializer extends JsonDeserializer<Player> {

  @Override
  public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
    Player player = null;

    if (rootNode.has("player")) {
      String name = rootNode.get("name").asText();
      int health = rootNode.get("health").asInt();
      Map<String, T> items
      int maxWeight = rootNode.get("maxWeight").asInt();
      int currentWeight = rootNode.get("currentWeight").asInt();
      int roomNumber = rootNode.get("roomNumber").asInt();
      int score = rootNode.get("score").asInt();

      player = new Player(name, health, maxWeight, currentWeight, roomNumber, score);
    }

    return player;
  }
}