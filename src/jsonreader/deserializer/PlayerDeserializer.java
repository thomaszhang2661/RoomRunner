package jsonreader.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Player;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom deserializer for the Player class, which converts a JSON representation of
 * a player into a Player object.
 */
public class PlayerDeserializer extends JsonDeserializer<Player> {

  // Initialize gameWorld for reference
  private final GameWorld gameWorld;

  /**
   * Constructor for PlayerDeserializer.
   * @param gameWorld the GameWorld instance
   */
  public PlayerDeserializer(GameWorld gameWorld) {
    this.gameWorld = gameWorld;
  }

  /**
   * Deserialize a JSON representation of a Player into a Player object.
   */
  @Override
  public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
          throws IOException, JsonProcessingException {
    JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
    Player player = null;


    // Parse all items from the JSON first
    Map<String, Item> allItems = new HashMap<>();
    JsonNode itemsNode = rootNode.get("items");
    if (itemsNode != null) {
      for (JsonNode itemNode : itemsNode) {
        String itemName = DeserializerHelperUtils.getNodeText(itemNode, "name");
        String description = DeserializerHelperUtils.getNodeText(itemNode, "description");
        int weight = DeserializerHelperUtils.getNodeInt(itemNode, "weight");
        int maxUses = DeserializerHelperUtils.getNodeInt(itemNode, "max_uses");
        int remainingUses = DeserializerHelperUtils.getNodeInt(itemNode, "uses_remaining");
        int value = DeserializerHelperUtils.getNodeInt(itemNode, "value");
        String whenUsed = DeserializerHelperUtils.getNodeText(itemNode, "when_used");
        String pictureName = DeserializerHelperUtils.getNodeText(itemNode, "picture");

        Item item = new Item(itemName, description, maxUses, remainingUses,
                value, weight, whenUsed, pictureName);
        allItems.put(itemName, item);
      }
    }

    if (rootNode.has("player")) {
      JsonNode playerNode = rootNode.get("player");
      String name = playerNode.get("name").asText();
      int health = playerNode.get("health").asInt();
      int maxWeight = playerNode.get("max_weight").asInt();
      int currentWeight = playerNode.get("current_weight").asInt();
      int roomNumber = playerNode.get("room_number").asInt();
      int score = playerNode.get("score").asInt();

      // Parse inventory string and lookup items from allItems map
      Map<String, Item> inventory = new HashMap<>();
      JsonNode inventoryNode = playerNode.get("inventory");

      if (inventoryNode != null && !inventoryNode.asText().isEmpty()) {
        String[] itemNames = inventoryNode.asText().split(",\\s*");
        for (String itemName : itemNames) {
          if (!itemName.isEmpty() && allItems.containsKey(itemName)) {
            inventory.put(itemName, allItems.get(itemName));
          }
        }
      }

      player = new Player(name, health, maxWeight, currentWeight, roomNumber, inventory, score);
    }

    return player;
  }
}