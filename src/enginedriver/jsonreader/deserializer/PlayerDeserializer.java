package enginedriver.jsonreader.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import enginedriver.model.GameWorld;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;

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
          throws IOException {
    JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
    Player player = null;

    // Parse all items from the JSON first
    Map<String, Item> allItems = new HashMap<>();
    for (Room<?> room : gameWorld.getRooms().values()) {
      for (Item obj : room.getEntitiesByType(Item.class)) {
        allItems.put(obj.getName(), obj);
      }
    }

    String name = rootNode.get("name").asText();
    int health = rootNode.get("health").asInt();
    int maxWeight = rootNode.get("max_weight").asInt();
    int currentWeight = rootNode.get("current_weight").asInt();
    int roomNumber = rootNode.get("room_number").asInt();
    int score = rootNode.get("score").asInt();

    // Parse inventory string and lookup items from allItems map
    Map<String, Item> inventory = new HashMap<>();
    JsonNode inventoryNode = rootNode.get("inventory");

    if (inventoryNode != null && !inventoryNode.asText().isEmpty()) {
      String[] itemNames = inventoryNode.asText().split(",\\s*");
      for (String itemName : itemNames) {
        if (!itemName.isEmpty() && allItems.containsKey(itemName)) {
          inventory.put(itemName, allItems.get(itemName));
        }
      }
    }

    player = new Player(name, health, maxWeight, currentWeight, roomNumber, inventory, score);

    return player;
  }
}