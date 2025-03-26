package enginedriver;

import java.util.List;
import java.util.Map;

/**
 * class for a room in the game.
 */
public class Room implements IItemContainer{

  /**
   * Returns a map of exits from the room.
   * @return Map of exits rooms from data.
   */
  Map<String, Room> getExits();

  /**
   * Returns the items in the room.
   * @return List of items in the room.
   */
  List<IItem> getItems();


  /**
   * Returns the fixtures in the room.
   * @return List of fixtures in the room.
   */
  List<String> getFixtures();

  /**
   * Returns the monster in the room (if any).
   * @return the monster in the room (if any),
   *      return null if no monster
   */
  IMonster getMonster();

  /**
   * Returns the puzzle in the room (if any).
   * @return the puzzle in the room (if any),
   *                return null if no puzzle
   */
  IProblem getPuzzle();

  @Override
  public boolean addItem(IItem item) {

    return false;
  }

  @Override
  public boolean deleteItem(IItem item) {

    return false;
  }
}
