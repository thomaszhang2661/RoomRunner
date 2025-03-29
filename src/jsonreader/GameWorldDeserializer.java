package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.Fixture;
import enginedriver.GameWorld;
import enginedriver.IProblem;
import enginedriver.IdentifiableEntity;
import enginedriver.Item;
import enginedriver.Monster;
import enginedriver.Puzzle;
import enginedriver.Room;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom deserializer for the GameWorld class, which converts a JSON representation of
 * a game world into a GameWorld object.
 */
public class GameWorldDeserializer extends JsonDeserializer<GameWorld> {

  /**
   * Deserialize a JSON representation of a GameWorld into a GameWorld object.

   * @param jsonParser the JsonParser to read the JSON
   * @param deserializationContext the DeserializationContext
   * @return the deserialized GameWorld object
   * @throws IOException if an error occurs during deserialization
   * @throws JsonProcessingException if an error occurs during JSON processing
   */
  @Override
  public GameWorld deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
          throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    final String name = rootNode.get("name").asText();
    final String version = rootNode.get("version").asText();

    // parse items
    Map<String, Item> items = new HashMap<>();
    for (JsonNode itemNode : rootNode.get("items")) {
      String itemName = itemNode.get("name").asText();
      int weight = itemNode.get("weight").asInt();
      int maxUses = itemNode.get("max_uses").asInt();
      int remainingUses = itemNode.get("uses_remaining").asInt();
      int value = itemNode.get("value").asInt();
      String whenUsed = itemNode.get("when_used").asText();
      String description = itemNode.get("description").asText();
      String picture = itemNode.get("picture").asText(); // not used

      Item item = new Item(itemName, description, maxUses, remainingUses, value, weight, whenUsed);
      items.put(item.getName(), item);
    }

    // parse fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    for (JsonNode fixtureNode : rootNode.get("fixtures")) {
      String fixtureName = fixtureNode.get("name").asText();
      int weight = fixtureNode.get("weight").asInt();
      Puzzle puzzle = null;
      Object states = fixtureNode.get("states");
      String description = fixtureNode.get("description").asText();
      String picture = fixtureNode.get("picture").asText();

      Fixture fixture = new Fixture(fixtureName, description, weight, puzzle, states, picture);
      fixtures.put(fixture.getName(), fixture);
    }

    // parse rooms
    Map<Integer, Room> rooms = new HashMap<>();
    for (JsonNode roomNode : rootNode.get("rooms")) {
      final String roomName = roomNode.get("room_name").asText();
      final int id = roomNode.get("room_number").asInt();
      final String description = roomNode.get("description").asText();

      Map<String, Integer> exits = new HashMap<>();
      exits.put("N", roomNode.get("N").asInt());
      exits.put("S", roomNode.get("S").asInt());
      exits.put("E", roomNode.get("E").asInt());
      exits.put("W", roomNode.get("W").asInt());

      // parse problem (monster or puzzle)
      IProblem<?> problem = null;
      if (roomNode.has("monster") && !roomNode.get("monster").isNull()) {
        problem = mapper.treeToValue(roomNode.get("monster"), Monster.class);
      } else if (roomNode.has("puzzle") && !roomNode.get("puzzle").isNull()) {
        problem = mapper.treeToValue(roomNode.get("puzzle"), Puzzle.class);
      }

      // parse items and fixtures
      Map<String, IdentifiableEntity> itemEntities = new HashMap<>();
      for (String itemName : roomNode.get("items").asText().split(", ")) {
        itemEntities.put(itemName, items.get(itemName));
      }
      Map<String, IdentifiableEntity> fixtureEntities = new HashMap<>();
      for (String fixtureName : roomNode.get("fixtures").asText().split(", ")) {
        fixtureEntities.put(fixtureName, fixtures.get(fixtureName));
      }
      // combine itemEntities and fixtureEntities into one map
      Map<String, IdentifiableEntity> entityNames = new HashMap<>();
      entityNames.putAll(itemEntities);
      entityNames.putAll(fixtureEntities);

      // parse pricture (not used)
      String picture = roomNode.get("picture").asText();


      Room room = new Room(id, roomName, description, exits, entityNames, problem);
      rooms.put(id, room);
    }

    return new GameWorld(name, version, rooms);
  }
}