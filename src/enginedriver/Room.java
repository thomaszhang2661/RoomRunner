package enginedriver;

import java.util.List;
import java.util.Map;

/**
 * class for a room in the game.
 */
public class Room  extends  EntityContainer<IdentifiableEntity> {
  private Map<String, Integer> exits;
  private IProblem problem;

  /**
   * Simple Constructor for a room
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits) {
    super(id, name, description);
    this.exits = exits;
    this.problem = null;
  }

  /**
   * Constructor for a room with Map of Entities.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String,IdentifiableEntity> entityNames) {
    super(id, name, description, entityNames);
    this.exits = exits;
    this.problem = null;
  }

  /**
   * Constructor for a room with Map of Entities.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String,IdentifiableEntity> entityNames,
              IProblem problem) {
    super(id, name, description, entityNames);
    this.exits = exits;
    this.problem = problem;
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

  /**
   * adds an entity to the room.
   */
  public boolean addEntity(IdentifiableEntity entity) {
    return super.addEntity(entity);
  }

  /**
   * removes an entity from the room.
   */
  public boolean removeEntity(IdentifiableEntity entity) {
    return super.removeEntity(entity);
  }

  /**
   *  Check if the room has an entity.
   */
  public Boolean  hasEntity(IdentifiableEntity entity) {
    return super.hasEntity(entity);
  }

  /**
   *  get an Entity from the room.
   */
  public <U> U getEntity(String entityName, Class<U> clazz) {
    return super.getEntity(entityName, clazz);
  public String getProblem() {
    // return the puzzle or monster in the room
  return problem;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"room_name\":\"" + getRoomName() + "\"," +
            "\"room_number\":\"" + getRoomNumber() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"N\":\"" + getExits().get("N") + "\"," +
            "\"S\":\"" + getExits().get("S") + "\"," +
            "\"E\":\"" + getExits().get("E") + "\"," +
            "\"W\":\"" + getExits().get("W") + "\"," +
            "\"puzzle\":\"" + getPuzzle() + "\"," +
            "\"monster\":\"" + getMonster() + "\"," +
            "\"items\":\"" + getItemNames() + "\"," +
            "\"fixtures\":\"" + getFixtureNames() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }

  /**
   * get the problem in the room.
   */
  public IProblem getProblem() {
    return problem;
  }
}
