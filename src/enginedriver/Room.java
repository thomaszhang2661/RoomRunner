package enginedriver;

import java.util.List;
import java.util.Map;

/**
 * class for a room in the game.
 */
public class Room  extends  EntityContainer {
  private Map<String, Integer> exits;

  /**
   * Constructor for a room.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String,IdentifiableEntity> entityNames) {
    super(id, name, description, entityNames);
    this.exits = exits;
  }

  /**
   * Returns a map of exits from the room.
   * @return Map of exits rooms from data.
   */
  Map<String, Integer> getExits() {
    return exits;
  }

  /**
   * unlocks an exit from the room.
   */
  void unlockExit(String key) {
    exits.put(key, Math.abs(exits.get(key)));
  }
}
