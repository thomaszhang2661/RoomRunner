package enginedriver.jsonreader.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.model.entity.Fixture;
import enginedriver.model.GameWorld;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.IProblem;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Puzzle;
import enginedriver.model.problems.validator.ItemSolutionValidator;
import enginedriver.model.problems.validator.SolutionValidator;
import enginedriver.model.problems.validator.StringSolutionValidator;
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
   */
  @Override
  public GameWorld deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
          throws IOException {
    ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
    JsonNode rootNode = mapper.readTree(jsonParser);

    final String name = DeserializerHelperUtils.getNodeText(rootNode, "name");
    final String version = DeserializerHelperUtils.getNodeText(rootNode, "version");

    // parse items
    Map<String, Item> items = new HashMap<>();
    JsonNode itemsNode = rootNode.get("items");
    DeserializerHelperUtils.parseItem(items, itemsNode);

    // parse fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    JsonNode fixturesNode = rootNode.get("fixtures");
    if (fixturesNode != null) {
      for (JsonNode fixtureNode : fixturesNode) {
        String fixtureName = DeserializerHelperUtils.getNodeText(fixtureNode, "name");
        int weight = DeserializerHelperUtils.getNodeInt(fixtureNode, "weight");
        Puzzle<?> puzzle = null;

        // parse states
        JsonNode statesNode = fixtureNode.get("states");
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
    JsonNode monstersNode = rootNode.get("monsters");
    Map<String, Monster<?>> monsters = new HashMap<>();
    if (monstersNode != null) {
      Map<String, Object> raw = parseProblem(monstersNode, items, true);
      for (Map.Entry<String, Object> entry : raw.entrySet()) {
        monsters.put(entry.getKey(), (Monster<?>) entry.getValue());
      }
    }

    // parse puzzles
    JsonNode puzzlesNode = rootNode.get("puzzles");
    Map<String, Puzzle<?>> puzzles = new HashMap<>();
    if (puzzlesNode != null) {
      Map<String, Object> raw = parseProblem(puzzlesNode, items, false);
      for (Map.Entry<String, Object> entry : raw.entrySet()) {
        puzzles.put(entry.getKey(), (Puzzle<?>) entry.getValue());
      }
    }

    // parse rooms
    Map<Integer, Room> rooms = new HashMap<>();
    JsonNode roomsNode = rootNode.get("rooms");
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
        if (roomNode.has("puzzle") && !roomNode.get("puzzle").isNull()) {
          problem = puzzles.get(DeserializerHelperUtils.getNodeText(roomNode, "puzzle"));
        } else if (roomNode.has("monster") && !roomNode.get("monster").isNull()) {
          problem = monsters.get(DeserializerHelperUtils.getNodeText(roomNode, "monster"));
        }

        // parse picture (not used)
        String pictureName = DeserializerHelperUtils.getNodeText(roomNode, "picture");

        Room room = new Room(id, roomName, description, exits, entityNames, problem, pictureName);
        rooms.put(id, room);
      }
    }

    return new GameWorld(name, version, rooms);
  }

  /**
   * Parse the problem (monster or puzzle) from the JSON node.

   * @param nodeArray the JSON node array
   * @param items the map of items
   * @param isMonster true if the problem is a monster, false if it's a puzzle
   * @return a map of problem names to their corresponding objects
   */
  private static Map<String, Object> parseProblem(JsonNode nodeArray, Map<String, Item> items, boolean isMonster) {
    Map<String, Object> result = new HashMap<>();
    for (JsonNode node : nodeArray) {
      String name = DeserializerHelperUtils.getNodeText(node, "name");
      Boolean active = node.get("active").asBoolean();
      Boolean affectsTarget = node.get("affects_target").asBoolean();
      Boolean affectsPlayer = node.get("affects_player").asBoolean();
      int value = DeserializerHelperUtils.getNodeInt(node, "value");
      String description = DeserializerHelperUtils.getNodeText(node, "description");
      String effects = DeserializerHelperUtils.getNodeText(node, "effects");
      String target = DeserializerHelperUtils.getNodeText(node, "target");
      String picture = DeserializerHelperUtils.getNodeText(node, "picture");
      String solutionText = DeserializerHelperUtils.getNodeText(node, "solution");

      Object solution;
      SolutionValidator<?> validator;
      if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
        String solStr = solutionText.substring(1, solutionText.length() - 1);
        solution = solStr;
        validator = new StringSolutionValidator();
      } else {
        solution = items.getOrDefault(solutionText, null);
        validator = new ItemSolutionValidator();
      }

      if (isMonster) {
        int damage = DeserializerHelperUtils.getNodeInt(node, "damage");
        Boolean canAttack = node.get("can_attack").asBoolean();
        String attack = DeserializerHelperUtils.getNodeText(node, "attack");

        if (solution instanceof String) {
          result.put(name, new Monster<>(name, description, active, affectsTarget, canAttack,
                  affectsPlayer, (String) solution, value, damage, effects, target,
                  picture, attack, (SolutionValidator<String>) validator));
        } else {
          result.put(name, new Monster<>(name, description, active, affectsTarget, canAttack,
                  affectsPlayer, (Item) solution, value, damage, effects, target,
                  picture, attack, (SolutionValidator<Item>) validator));
        }
      } else {
        if (solution instanceof String) {
          result.put(name, new Puzzle<>(name, description, active, affectsTarget,
                  affectsPlayer, (String) solution, value, effects, target,
                  picture, (SolutionValidator<String>) validator));
        } else {
          result.put(name, new Puzzle<>(name, description, active, affectsTarget,
                  affectsPlayer, (Item) solution, value, effects, target,
                  picture, (SolutionValidator<Item>) validator));
        }
      }
    }
    return result;
  }

}