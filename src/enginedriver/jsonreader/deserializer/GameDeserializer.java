package enginedriver.jsonreader.deserializer;

import static enginedriver.jsonreader.deserializer.DeserializerHelperUtils.getCaseInsensitive;
import static enginedriver.jsonreader.deserializer.DeserializerHelperUtils.parseProblem;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.GameController;
import enginedriver.model.GameWorld;
import enginedriver.model.entity.Fixture;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.IProblem;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Puzzle;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom deserializer for the GameController class.
 */
public class GameDeserializer extends JsonDeserializer<GameController> {

  /**
   * Deserialize a JSON representation of a GameController into a GameController object.

   * @param jsonParser the JsonParser to read the JSON
   * @param ctxt the DeserializationContext
   * @return the deserialized GameController object
   * @throws IOException if an error occurs during deserialization
   */
  @Override
  public GameController deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    GameWorld world;
    Player player;

    JsonNode worldNode = getCaseInsensitive(rootNode, "world");
    // if world node not found, that means it's a raw world file, use it as rootNode
    if (worldNode == null) {
      world = deserializeWorld(rootNode);

      player = new Player("DefaultPlayer", 100,
              20, 0, 1, new HashMap<>(), 0);
    } else {
      world = deserializeWorld(worldNode);

      JsonNode playerNode = getCaseInsensitive(rootNode, "player");
      player = deserializePlayer(playerNode);
    }

    return new GameController(world, player);
  }

  /**
   * Deserialize a JSON representation of a GameWorld into a GameWorld object.

   * @return the deserialized GameWorld object
   * @throws IOException if an error occurs during deserialization
   */
  private GameWorld deserializeWorld(JsonNode worldNode) throws IOException {
    final String name = DeserializerHelperUtils.getNodeText(worldNode, "name");
    final String version = DeserializerHelperUtils.getNodeText(worldNode, "version");

    // parse items
    Map<String, Item> items = new HashMap<>();
    JsonNode itemsNode = getCaseInsensitive(worldNode, "items");
    DeserializerHelperUtils.parseItem(items, itemsNode);

    // parse fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    JsonNode fixturesNode = getCaseInsensitive(worldNode, "fixtures");
    if (fixturesNode != null) {
      for (JsonNode fixtureNode : fixturesNode) {
        String fixtureName = DeserializerHelperUtils.getNodeText(fixtureNode, "name");
        int weight = DeserializerHelperUtils.getNodeInt(fixtureNode, "weight");
        Puzzle<?> puzzle = null;

        // parse states
        JsonNode statesNode = getCaseInsensitive(fixtureNode, "states");
        int states; // for storing the number of states
        if (statesNode != null && statesNode.isInt()) {
          states = statesNode.asInt();
        } else {
          states = -1; // default value
        }

        String description = DeserializerHelperUtils.getNodeText(fixtureNode, "description");
        String picture = DeserializerHelperUtils.getNodeText(fixtureNode, "picture");

        Fixture fixture = new Fixture(fixtureName, description, weight, puzzle, states, picture);
        fixtures.put(fixture.getName(), fixture);
      }
    }

    // parse monsters
    JsonNode monstersNode = getCaseInsensitive(worldNode, "monsters");
    Map<String, Monster<?>> monsters = new HashMap<>();
    if (monstersNode != null) {
      Map<String, Object> raw = parseProblem(monstersNode, items, true);
      for (Map.Entry<String, Object> entry : raw.entrySet()) {
        monsters.put(entry.getKey().toUpperCase(), (Monster<?>) entry.getValue());
      }
    }

    // parse puzzles
    JsonNode puzzlesNode = getCaseInsensitive(worldNode, "puzzles");
    Map<String, Puzzle<?>> puzzles = new HashMap<>();
    if (puzzlesNode != null) {
      Map<String, Object> raw = parseProblem(puzzlesNode, items, false);
      for (Map.Entry<String, Object> entry : raw.entrySet()) {
        puzzles.put(entry.getKey().toUpperCase(), (Puzzle<?>) entry.getValue());
      }
    }

    // parse rooms
    Map<Integer, Room> rooms = new HashMap<>();
    JsonNode roomsNode = getCaseInsensitive(worldNode, "rooms");
    if (roomsNode != null) {
      for (JsonNode roomNode : roomsNode) {
        final String roomName = DeserializerHelperUtils.getNodeText(roomNode, "room_name");
        final int id = DeserializerHelperUtils.getNodeInt(roomNode, "room_number");
        final String description = DeserializerHelperUtils.getNodeText(roomNode, "description");

        Map<String, Integer> exits = new HashMap<>();
        exits.put("N", DeserializerHelperUtils.getNodeInt(roomNode, "N"));
        exits.put("S", DeserializerHelperUtils.getNodeInt(roomNode, "S"));
        exits.put("E", DeserializerHelperUtils.getNodeInt(roomNode, "E"));
        exits.put("W", DeserializerHelperUtils.getNodeInt(roomNode, "W"));

        // parse items and fixtures
        Map<String, IdentifiableEntity> itemEntities = new HashMap<>();
        String itemsText = DeserializerHelperUtils.getNodeText(roomNode, "items");
        if (!itemsText.isEmpty()) {
          for (String itemName : itemsText.split(", ")) {
            itemEntities.put(itemName, items.get(itemName));
          }
        }
        Map<String, IdentifiableEntity> fixtureEntities = new HashMap<>();
        String fixturesText = DeserializerHelperUtils.getNodeText(roomNode, "fixtures");
        if (!fixturesText.isEmpty()) {
          for (String fixtureName : fixturesText.split(", ")) {
            fixtureEntities.put(fixtureName, fixtures.get(fixtureName));
          }
        }
        // combine itemEntities and fixtureEntities into one map
        Map<String, IdentifiableEntity> entityNames = new HashMap<>();
        entityNames.putAll(itemEntities);
        entityNames.putAll(fixtureEntities);

        // parse problem (monster or puzzle)
        IProblem<?> problem = null;
        if (roomNode.has("puzzle") && !getCaseInsensitive(roomNode, "puzzle").isNull()) {
          String puzzleUppercase = DeserializerHelperUtils.getNodeText(roomNode, "puzzle").toUpperCase();
          problem = puzzles.get(puzzleUppercase);
        } else if (roomNode.has("monster") && !getCaseInsensitive(roomNode, "monster").isNull()) {
          String monsterUppercase = DeserializerHelperUtils.getNodeText(roomNode, "monster").toUpperCase();
          problem = monsters.get(monsterUppercase);
        }

        // parse picture
        String pictureName = DeserializerHelperUtils.getNodeText(roomNode, "picture");

        Room room = new Room(id, roomName, description, exits, entityNames, problem, pictureName);
        rooms.put(id, room);
      }
    }

    return new GameWorld(name, version, rooms);
  }

  /**
   * Deserialize a JSON representation of a Player into a Player object.
   */
  private Player deserializePlayer(JsonNode playerNode) {
    String name = DeserializerHelperUtils.getNodeText(playerNode, "name");
    int health = DeserializerHelperUtils.getNodeInt(playerNode, "health");
    int maxWeight = DeserializerHelperUtils.getNodeInt(playerNode, "max_weight");
    int currentWeight = DeserializerHelperUtils.getNodeInt(playerNode, "current_weight");
    int roomNumber = DeserializerHelperUtils.getNodeInt(playerNode, "room_number");
    int score = DeserializerHelperUtils.getNodeInt(playerNode, "score");

    // Parse inventory
    Map<String, Item> inventory = new HashMap<>();
    JsonNode inventoryNode = getCaseInsensitive(playerNode, "items");
    DeserializerHelperUtils.parseItem(inventory, inventoryNode);

    return new Player(name, health, maxWeight, currentWeight, roomNumber, inventory, score);
  }
}
