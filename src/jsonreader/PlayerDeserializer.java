package jsonreader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Player;
import enginedriver.Room;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerDeserializer extends JsonDeserializer<Player> {

  private GameWorld gameWorld; // for finding items

  public PlayerDeserializer(GameWorld gameWorld) {
    this.gameWorld = gameWorld;
  }

  @Override
  public Player deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
    Player player = null;

    if (rootNode.has("player")) {
      String name = rootNode.get("name").asText();
      int health = rootNode.get("health").asInt();
      int maxWeight = rootNode.get("max_weight").asInt();
      int currentWeight = rootNode.get("current_weight").asInt();
      int roomNumber = rootNode.get("room_number").asInt();
      int score = rootNode.get("score").asInt();

      // parse inventory
      Map<String, Item> inventory = new HashMap<>();
      JsonNode inventoryNode = rootNode.get("inventory");

      String[] itemNames = inventoryNode.asText().split(",\\s*");
      for (String itemName : itemNames) {
        if (!itemName.isEmpty()) {
          Item item = findItemInGameWorld(itemName);
          if (item != null) {
            inventory.put(itemName, item);
          }
        }
      }


      player = new Player(name, health, maxWeight, currentWeight, roomNumber, inventory, score);
    }

    return player;
  }

  /**
   * 在 GameWorld 的所有房间中查找指定名称的 Item
   */
  private Item findItemInGameWorld(String itemName) {
    for (Room<?> room : gameWorld.getRooms().values()) {
      Item item = room.getItem(itemName);
      if (item != null) {
        return item;
      }
    }
    return null;
  }
}