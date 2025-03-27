package enginedriver;

import java.util.List;
import java.util.Map;

/**
 * class for a room in the game.
 */
public class Room  extends  IdentifiableEntity
        implements IItemContainer {
  private Map<String, Integer> exits;
  private List<String> itemNames;
  private List<String> fixtureNames;
  private String problem;

  /**
   * Constructor for a room.
   */
  public Room(String id, String name, String description,
              Map<String, Integer> exits, List<String>itemNames,
              List<String> fixtureNames, String problem,
              String problem) {
    super(id, name, description);
    this.exits = exits;
    this.itemNames = itemNames;
    this.fixtureNames = fixtureNames;
    this.problem = problem;
  }

  /**
   * Returns a map of exits from the room.
   * @return Map of exits rooms from data.
   */
  Map<String, Integer> getExits() {
    return exits;
  }

  List<String> getItemNames() {
    return itemNames;
  }
  /**
   * Returns the monster in the room (if any).
   * @return the monster in the room (if any),
   *      return null if no monster
   */
  String getMonster() {
    return monsterName;
  }

  /**
   * Returns the puzzle in the room (if any).
   * @return the puzzle in the room (if any),
   *                return null if no puzzle
   */
  String getPuzzle() {
    return puzzleName;
  }

  /**
   * Adds an item to the container.
   *
   * @param item the item to be added
   */
  @Override
  public void addItem(String item) {
    if (item == null) {
      throw new IllegalArgumentException("Item name cannot be null");
    }
    itemNames.add(item);
  }

  /**
   * Removes an item from the container.
   *
   * @param item the item to be removed
   */
  @Override
  public void deleteItem(String item) {
    if (item == null) {
      throw new IllegalArgumentException("Item name cannot be null");
    }
    itemNames.remove(item);
  }

  public String getProblem() {
    // return the puzzle or monster in the room
  }

}
