package enginedriver.jsonreader.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import enginedriver.model.entity.Item;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Puzzle;
import enginedriver.model.problems.validator.ItemSolutionValidator;
import enginedriver.model.problems.validator.SolutionValidator;
import enginedriver.model.problems.validator.StringSolutionValidator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * HelperUtils class provides utility methods for JSON reading and writing.
 * It's package private and not intended for public use.
 */
class DeserializerHelperUtils {

  /**
   * Helper method to get a text value from a JsonNode.

   * @param node the JsonNode
   * @param fieldName the field name
   * @return the text value, or an empty string if not found
   */
  static String getNodeText(JsonNode node, String fieldName) {
    JsonNode fieldNode = getCaseInsensitive(node, fieldName);
    return fieldNode != null ? fieldNode.asText() : "";
  }

  /**
   * Helper method to get an integer value from a JsonNode.

   * @param node the JsonNode
   * @param fieldName the field name
   * @return the integer value, or 0 if not found
   */
  static int getNodeInt(JsonNode node, String fieldName) {
    JsonNode fieldNode = getCaseInsensitive(node, fieldName);
    return fieldNode != null ? fieldNode.asInt() : 0;
  }

  /**
   * Helper method to parse items from a JsonNode and add them to the item map.

   * @param itemMap the map to store items
   * @param itemsNode the JsonNode containing items
   */
  static void parseItem(Map<String, Item> itemMap, JsonNode itemsNode) {
    if (itemsNode != null) {
      for (JsonNode itemNode : itemsNode) {
        String itemName = getNodeText(itemNode, "name");
        int weight = getNodeInt(itemNode, "weight");
        int maxUses = getNodeInt(itemNode, "max_uses");
        int remainingUses = getNodeInt(itemNode, "uses_remaining");
        int value = getNodeInt(itemNode, "value");
        String whenUsed = getNodeText(itemNode, "when_used");
        String description = getNodeText(itemNode, "description");
        String pictureName = getNodeText(itemNode, "picture");

        Item item = new Item(itemName, description, maxUses, remainingUses,
                value, weight, whenUsed, pictureName);
        itemMap.put(item.getName(), item);
      }
    }
  }

  /**
   * Parse the problem (monster or puzzle) from the JSON node.

   * @param nodeArray the JSON node array
   * @param items the map of items
   * @param isMonster true if the problem is a monster, false if it's a puzzle
   * @return a map of problem names to their corresponding objects
   */
  static Map<String, Object> parseProblem(JsonNode nodeArray, Map<String, Item> items, boolean isMonster) {
    Map<String, Object> result = new HashMap<>();
    for (JsonNode node : nodeArray) {
      String name = getNodeText(node, "name");
      Boolean active = getCaseInsensitive(node, "active").asBoolean();
      Boolean affectsTarget = getCaseInsensitive(node, "affects_target").asBoolean();
      Boolean affectsPlayer = getCaseInsensitive(node, "affects_player").asBoolean();
      int value = getNodeInt(node, "value");
      String description = getNodeText(node, "description");
      String effects = getNodeText(node, "effects");
      String target = getNodeText(node, "target");
      String picture = getNodeText(node, "picture");
      String solutionText = getNodeText(node, "solution");

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
        int damage = getNodeInt(node, "damage");
        Boolean canAttack = getCaseInsensitive(node, "can_attack").asBoolean();
        String attack = getNodeText(node, "attack");

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

  /**
   * Get a case-insensitive field from a JsonNode.

   * @param node the JsonNode
   * @param fieldName the field name
   * @return the JsonNode value if found, otherwise NullNode
   */
  static JsonNode getCaseInsensitive(JsonNode node, String fieldName) {
    if (node == null) return null;

    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> entry = fields.next();
      if (entry.getKey().equalsIgnoreCase(fieldName)) {
        return entry.getValue();
      }
    }

    return null;
  }

}
