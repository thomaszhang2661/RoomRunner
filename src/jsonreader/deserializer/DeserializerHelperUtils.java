package jsonreader.deserializer;

import com.fasterxml.jackson.databind.JsonNode;

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
}
