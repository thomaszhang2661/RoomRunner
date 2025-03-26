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
  private String monsterName;
  private String puzzleName;

  /**
   * Constructor for a room.
   */
  public Room(String id, String name, String description,
              Map<String, Integer> exits, List<String>itemNames,
              List<String> fixtureNames, String monsterName,
              String puzzleName) {
    super(id, name, description);
    this.exits = exits;
    this.itemNames = itemNames;
    this.fixtureNames = fixtureNames;
    this.monsterName = monsterName;
    this.puzzleName = puzzleName;
  }

  /**
   * Returns a map of exits from the room.
   * @return Map of exits rooms from data.
   */
  Map<String, Integer> getExits() {
    return exits;
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

  @Override
  public boolean addItem(IItem item) {

    return false;
  }

  @Override
  public boolean deleteItem(IItem item) {

    return false;
  }


}
