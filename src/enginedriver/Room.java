package enginedriver;

import java.util.List;
import java.util.Map;

import enginedriver.problems.IProblem;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;

/**
 * class for a room in the game.
 */
public class Room<T extends IProblem<?>>  extends  EntityContainer<IdentifiableEntity> {
  private Map<String, Integer> exits;
  private final T problem;


  /**
   * Simple Constructor for a room.

   * @param id the id of the room
   * @param name the name of the room
   * @param description the description of the room
   * @param exits the exits of the room
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits) {
    super(id, name, description);
    this.exits = exits;
    this.problem = null;
  }

  /**
   * Simple Constructor for a room with problem.

   * @param id the id of the room
   * @param name the name of the room
   * @param description the description of the room
   * @param exits the exits of the room
   * @param problem the problem of the room
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,  T problem) {
    super(id, name, description);
    this.exits = exits;
    this.problem = problem;
  }

  /**
   * Constructor for a room with Map of Entities.

   * @param id the id of the room
   * @param name the name of the room
   * @param description the description of the room
   * @param exits the exits of the room
   * @param entityNames the map of entities
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String, IdentifiableEntity> entityNames) {
    super(id, name, description, entityNames);
    this.exits = exits;
    this.problem = null;
  }

  /**
   * Constructor for a room with Map of Entities and a problem.

   * @param id the id of the room
   * @param name the name of the room
   * @param description the description of the room
   * @param exits the exits of the room
   * @param entityNames the map of entities
   * @param problem the problem of the room
   * @param pictureName the picture name of the room
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String, IdentifiableEntity> entityNames,
              T problem, String pictureName) {
    super(id, name, description, entityNames, pictureName);
    this.exits = exits;
    this.problem = problem;
  }

  /**
   * Returns a map of exits from the room.

   * @return Map of exits rooms from data.
   */
  public Map<String, Integer> getExits() {
    return exits;
  }

  /**
   * unlocks an exit from the room.

   * @param key the key of the exit
   */
  void unlockExit(String key) {
    exits.put(key, Math.abs(exits.get(key)));
  }

  /**
   *  get an Item from the room.

   *  @param itemName the name of the item
   *  @return the item
   */
  public Item  getItem(String itemName) {
    return super.getEntity(itemName, Item.class);
  }

  /**
   * get the problem in the room.

   * @return the problem in the room
   */
  public T getProblem() {
    return problem;
  }

  /**
   * get the String list of items in the room.

   * @param clazz the class of the entity
   *  @return the string list of items
   */
  public <U extends IdentifiableEntity> String getElementNames(Class<U> clazz) {
    return String.join(", ", getEntitiesByType(clazz).stream()
            .map(U::getName)
            .toList());
  }
}
