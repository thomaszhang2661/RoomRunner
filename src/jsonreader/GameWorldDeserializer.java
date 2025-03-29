package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enginedriver.Fixture;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Monster;
import enginedriver.Player;
import enginedriver.Puzzle;
import enginedriver.Room;

public class GameWorldDeserializer extends JsonDeserializer<GameWorld> {

  @Override
  public GameWorld deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    String name = rootNode.get("name").asText();
    String version = rootNode.get("version").asText();

//    parse rooms
    Map<Integer, Room> rooms = new HashMap<>();
    for (JsonNode roomNode : rootNode.get("rooms")) {
      int id = roomNode.get("room_number").asInt();
      String roomName = roomNode.get("room_name").asText();
      String description = roomNode.get("description").asText();

      Map<String, Integer> exits = new HashMap<>();
      exits.put("N", roomNode.get("N").asInt());
      exits.put("S", roomNode.get("S").asInt());
      exits.put("E", roomNode.get("E").asInt());
      exits.put("W", roomNode.get("W").asInt());

      List<String> itemNames = List.of(roomNode.get("items").asText().split(", "));
      List<String> fixtureNames = List.of(roomNode.get("fixtures").asText().split(", "));
      String problem = roomNode.get("puzzle").asText(null);
      // Room constructor do not accept List.
      // Need to change it to Map<elementName, element>
      Room room = new Room(id, roomName, description, exits, itemNames, fixtureNames, problem);
      rooms.put(Integer.parseInt(String.valueOf(id)), room);
    }

//    parse items
    Map<String, Item> items = new HashMap<>();
    for (JsonNode itemNode : rootNode.get("items")) {
      Item item = mapper.treeToValue(itemNode, Item.class);
      items.put(item.getName(), item);
    }

//    parse fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    for (JsonNode fixtureNode : rootNode.get("fixtures")) {
      Fixture fixture = mapper.treeToValue(fixtureNode, Fixture.class);
      fixtures.put(fixture.getName(), fixture);
    }

//    parse monsters
    Map<String, Monster> monsters = new HashMap<>();
    for (JsonNode monsterNode : rootNode.get("monsters")) {
      Monster monster = mapper.treeToValue(monsterNode, Monster.class);
      monsters.put(monster.getName(), monster);
    }

//    parse puzzles
    Map<String, Puzzle> puzzles = new HashMap<>();
    for (JsonNode puzzleNode : rootNode.get("puzzles")) {
      Puzzle puzzle = mapper.treeToValue(puzzleNode, Puzzle.class);
      puzzles.put(puzzle.getName(), puzzle);
    }

//    parse player
    Player player = mapper.treeToValue(rootNode.get("player"), Player.class);

    return new GameWorld(name, version, rooms, items, fixtures, monsters, puzzles, player);
  }
}