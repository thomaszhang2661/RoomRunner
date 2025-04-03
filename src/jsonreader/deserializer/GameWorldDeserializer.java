package jsonreader.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.Fixture;
import enginedriver.GameWorld;
import enginedriver.IdentifiableEntity;
import enginedriver.Item;
import enginedriver.Room;
import enginedriver.problems.IProblem;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;
import enginedriver.problems.validator.ItemSolutionValidator;
import enginedriver.problems.validator.SolutionValidator;
import enginedriver.problems.validator.StringSolutionValidator;
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

    final String name = DeserializerHelperUtils.getNodeText(rootNode, "name");
    final String version = DeserializerHelperUtils.getNodeText(rootNode, "version");

    // parse items
    Map<String, Item> items = new HashMap<>();
    JsonNode itemsNode = rootNode.get("items");
    if (itemsNode != null) {
      for (JsonNode itemNode : itemsNode) {
        String itemName = DeserializerHelperUtils.getNodeText(itemNode, "name");
        int weight = DeserializerHelperUtils.getNodeInt(itemNode, "weight");
        int maxUses = DeserializerHelperUtils.getNodeInt(itemNode, "max_uses");
        int remainingUses = DeserializerHelperUtils.getNodeInt(itemNode, "uses_remaining");
        int value = DeserializerHelperUtils.getNodeInt(itemNode, "value");
        String whenUsed = DeserializerHelperUtils.getNodeText(itemNode, "when_used");
        String description = DeserializerHelperUtils.getNodeText(itemNode, "description");
        String pictureName = DeserializerHelperUtils.getNodeText(itemNode, "picture"); // not used

        Item item = new Item(itemName, description, maxUses, remainingUses,
                value, weight, whenUsed, pictureName);
        items.put(item.getName(), item);
      }
    }

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
    Map<String, Monster<?>> monsters = new HashMap<>();
    JsonNode monstersNode = rootNode.get("monsters");
    if (monstersNode != null) {
      for (JsonNode monsterNode : monstersNode) {
        String monsterName = DeserializerHelperUtils.getNodeText(monsterNode, "name");
        Boolean active = monsterNode.get("active").asBoolean();
        Boolean affectsTarget = monsterNode.get("affects_target").asBoolean();
        Boolean affectsPlayer = monsterNode.get("affects_player").asBoolean();

        int value = DeserializerHelperUtils.getNodeInt(monsterNode, "value");
        String description = DeserializerHelperUtils.getNodeText(monsterNode, "description");
        String effects = DeserializerHelperUtils.getNodeText(monsterNode, "effects");
        int damage = DeserializerHelperUtils.getNodeInt(monsterNode, "damage");
        String target = DeserializerHelperUtils.getNodeText(monsterNode, "target");
        Boolean canAttack = monsterNode.get("can_attack").asBoolean();
        String attack = DeserializerHelperUtils.getNodeText(monsterNode, "attack");
        String pictureName = DeserializerHelperUtils.getNodeText(monsterNode, "picture");
        String solutionText = DeserializerHelperUtils.getNodeText(monsterNode, "solution");
        Object solution;

        if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
          String solutionString = solutionText.substring(1, solutionText.length() - 1);
          SolutionValidator<String> validator = new StringSolutionValidator();
          Monster<String> monster = new Monster<String>(monsterName, description, active,
                  affectsTarget, canAttack, affectsPlayer, solutionString, value,
                  damage, effects, target, pictureName, attack, validator);
          monsters.put(monster.getName(), monster);
        } else {
          Item solutionItem = items.getOrDefault(solutionText, null);
          SolutionValidator<Item> validator = new ItemSolutionValidator();
          Monster<Item> monster = new Monster<Item>(monsterName, description, active,
                  affectsTarget, canAttack, affectsPlayer, solutionItem, value,
                  damage, effects, target, pictureName, attack, validator);
          monsters.put(monster.getName(), monster);
        }

      }
    }

    // parse puzzles
    Map<String, Puzzle<?>> puzzles = new HashMap<>();
    JsonNode puzzlesNode = rootNode.get("puzzles");
    if (puzzlesNode != null) {
      for (JsonNode puzzleNode : puzzlesNode) {
        String puzzleName = DeserializerHelperUtils.getNodeText(puzzleNode, "name");
        Boolean active = puzzleNode.get("active").asBoolean();
        Boolean affectsTarget = puzzleNode.get("affects_target").asBoolean();
        Boolean affectsPlayer = puzzleNode.get("affects_player").asBoolean();

        int value = DeserializerHelperUtils.getNodeInt(puzzleNode, "value");
        String description = DeserializerHelperUtils.getNodeText(puzzleNode, "description");
        String effects = DeserializerHelperUtils.getNodeText(puzzleNode, "effects");
        String target = DeserializerHelperUtils.getNodeText(puzzleNode, "target");
        String pictureName = DeserializerHelperUtils.getNodeText(puzzleNode, "picture");

        String solutionText = DeserializerHelperUtils.getNodeText(puzzleNode, "solution");
        Object solution;
        if (solutionText.startsWith("'") && solutionText.endsWith("'")) {
          String solutionString = solutionText.substring(1, solutionText.length() - 1);
          SolutionValidator<String> validator = new StringSolutionValidator();
          Puzzle<?> puzzle = new Puzzle<>(puzzleName, description, active, affectsTarget,
                  affectsPlayer, solutionString, value, effects, target, pictureName, validator);
          puzzles.put(puzzle.getName(), puzzle);

        } else {
          Item solutionItem = items.getOrDefault(solutionText, null);
          SolutionValidator<Item> validator = new ItemSolutionValidator();
          Puzzle<?> puzzle = new Puzzle<>(puzzleName, description, active, affectsTarget,
                  affectsPlayer, solutionItem, value, effects, target, pictureName, validator);
          puzzles.put(puzzle.getName(), puzzle);
        }
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

}