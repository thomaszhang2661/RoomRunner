package enginedriver.jsonreader.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import enginedriver.model.entity.Item;
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
    JsonNode fieldNode = node.get(fieldName);
    return fieldNode != null ? fieldNode.asText() : "";
  }

  /**
   * Helper method to get an integer value from a JsonNode.

   * @param node the JsonNode
   * @param fieldName the field name
   * @return the integer value, or 0 if not found
   */
  static int getNodeInt(JsonNode node, String fieldName) {
    JsonNode fieldNode = node.get(fieldName);
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

}
