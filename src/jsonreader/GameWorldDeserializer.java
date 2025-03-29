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

    final String name = getNodeText(rootNode, "name");
    final String version = getNodeText(rootNode, "version");

    // parse items
    Map<String, Item> items = new HashMap<>();
    JsonNode itemsNode = rootNode.get("items");
    if (itemsNode != null) {
      for (JsonNode itemNode : itemsNode) {
        String itemName = getNodeText(itemNode, "name");
        int weight = getNodeInt(itemNode, "weight");
        int maxUses = getNodeInt(itemNode, "max_uses");
        int remainingUses = getNodeInt(itemNode, "uses_remaining");
        int value = getNodeInt(itemNode, "value");
        String whenUsed = getNodeText(itemNode, "when_used");
        String description = getNodeText(itemNode, "description");
        String picture = getNodeText(itemNode, "picture"); // not used

        Item item = new Item(itemName, description, maxUses, remainingUses, value, weight, whenUsed);
        items.put(item.getName(), item);
      }
    }

    // parse fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    JsonNode fixturesNode = rootNode.get("fixtures");
    if (fixturesNode != null) {
      for (JsonNode fixtureNode : fixturesNode) {
        String fixtureName = getNodeText(fixtureNode, "name");
        int weight = getNodeInt(fixtureNode, "weight");
        Puzzle puzzle = null;
        Object states = fixtureNode.get("states");
        String description = getNodeText(fixtureNode, "description");
        String picture = getNodeText(fixtureNode, "picture");

        Fixture fixture = new Fixture(fixtureName, description, weight, puzzle, states, picture);
        fixtures.put(fixture.getName(), fixture);
      }
    }

    // parse monsters
    Map<String, Monster<?>> monsters = new HashMap<>();
    JsonNode monstersNode = rootNode.get("monsters");
    if (monstersNode != null) {
      for (JsonNode monsterNode : monstersNode) {
        String monsterName = getNodeText(monsterNode, "name");
        Boolean active = monsterNode.get("active").asBoolean();
        Boolean affectsTarget = monsterNode.get("affects_target").asBoolean();
        Boolean affectsPlayer = monsterNode.get("affects_player").asBoolean();
        String solutionText = getNodeText(monsterNode, "solution");
        Object solution;
        if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
          solution = solutionText.substring(1, solutionText.length() - 1);
        } else {
          solution = items.getOrDefault(solutionText, null);
        }
        int value = getNodeInt(monsterNode, "value");
        String description = getNodeText(monsterNode, "description");
        String effects = getNodeText(monsterNode, "effects");
        int damage = getNodeInt(monsterNode, "damage");
        String target = getNodeText(monsterNode, "target");
        Boolean canAttack = monsterNode.get("can_attack").asBoolean(); // not used
        String attack = getNodeText(monsterNode, "attack");
        String pictureName = getNodeText(monsterNode, "picture");

        Monster<?> monster = new Monster<>(monsterName, description, active, affectsTarget,
                affectsPlayer, solution, value, damage, effects, target, pictureName, attack);
      }
    }

    // parse puzzles
    Map<String, Puzzle<?>> puzzles = new HashMap<>();
    JsonNode puzzlesNode = rootNode.get("puzzles");
    if (puzzlesNode != null) {
      for (JsonNode puzzleNode : puzzlesNode) {
        String puzzleName = getNodeText(puzzleNode, "name");
        Boolean active = puzzleNode.get("active").asBoolean();
        Boolean affectsTarget = puzzleNode.get("affects_target").asBoolean();
        Boolean affectsPlayer = puzzleNode.get("affects_player").asBoolean();
        String solutionText = getNodeText(puzzleNode, "solution");
        Object solution;
        if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
          solution = solutionText.substring(1, solutionText.length() - 1);
        } else {
          solution = items.getOrDefault(solutionText, null);
        }
        int value = getNodeInt(puzzleNode, "value");
        String description = getNodeText(puzzleNode, "description");
        String effects = getNodeText(puzzleNode, "effects");
        String target = getNodeText(puzzleNode, "target");
        String pictureName = getNodeText(puzzleNode, "picture");

        Puzzle<?> puzzle = new Puzzle<>(puzzleName, description, active, affectsTarget,
                affectsPlayer, solution, value, effects, target, pictureName);
      }
    }

    // parse rooms
    Map<Integer, Room> rooms = new HashMap<>();
    JsonNode roomsNode = rootNode.get("rooms");
    if (roomsNode != null) {
      for (JsonNode roomNode : roomsNode) {
        final String roomName = getNodeText(roomNode, "room_name");
        final int id = getNodeInt(roomNode, "room_number");
        final String description = getNodeText(roomNode, "description");

        Map<String, Integer> exits = new HashMap<>();
        exits.put("N", getNodeInt(roomNode, "N"));
        exits.put("S", getNodeInt(roomNode, "S"));
        exits.put("E", getNodeInt(roomNode, "E"));
        exits.put("W", getNodeInt(roomNode, "W"));

        // parse items and fixtures
        Map<String, IdentifiableEntity> itemEntities = new HashMap<>();
        String itemsText = getNodeText(roomNode, "items");
        if (!itemsText.isEmpty()) {
          for (String itemName : itemsText.split(", ")) {
            itemEntities.put(itemName, items.get(itemName));
          }
        }
        Map<String, IdentifiableEntity> fixtureEntities = new HashMap<>();
        String fixturesText = getNodeText(roomNode, "fixtures");
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
        if (roomNode.has("puzzle")) {
          problem = puzzles.get(getNodeText(roomNode, "puzzle"));
        } else if (roomNode.has("monster")) {
          problem = monsters.get(getNodeText(roomNode, "monster"));
        }

        // parse picture (not used)
        String picture = getNodeText(roomNode, "picture");

        Room room = new Room(id, roomName, description, exits, entityNames, problem);
        rooms.put(id, room);
      }
    }

    return new GameWorld(name, version, rooms);
  }

  private String getNodeText(JsonNode node, String fieldName) {
    JsonNode fieldNode = node.get(fieldName);
    return fieldNode != null ? fieldNode.asText() : "";
  }

  private int getNodeInt(JsonNode node, String fieldName) {
    JsonNode fieldNode = node.get(fieldName);
    return fieldNode != null ? fieldNode.asInt() : 0;
  }
}