package enginedriver;

import java.util.Map;

import enginedriver.problems.Monster;
import enginedriver.problems.IProblem;
import enginedriver.problems.Puzzle;

/**
 * class for a room in the game.
 */
public class Room<T extends IProblem<?>>  extends  EntityContainer<IdentifiableEntity> {
  private Map<String, Integer> exits;
  private final T problem;


  /**
   * Simple Constructor for a room.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits) {
    super(id, name, description);
    this.exits = exits;
    this.problem = null;
  }

  /**
   * Simple Constructor for a room with problem.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,  T problem) {
    super(id, name, description);
    this.exits = exits;
    this.problem = problem;
  }

  /**
   * Constructor for a room with Map of Entities.
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
   */
  void unlockExit(String key) {
    exits.put(key, Math.abs(exits.get(key)));
  }

  //  /**
  //   * adds an entity to the room.
  //   */
  //  public boolean addEntity(IdentifiableEntity entity) {
  //    return super.addEntity(entity);
  //  }

  //  /**
  //   * removes an entity from the room.
  //   */
  //  public boolean removeEntity(IdentifiableEntity entity) {
  //    return super.removeEntity(entity);
  //  }

  //  /**
  //   *  Check if the room has an entity, check by object.
  //   */
  //  public Boolean  hasEntity(IdentifiableEntity entity) {
  //    return super.hasEntity(entity);
  //  }

  //  /**
  //   *  Check if the room has an entity check by name.
  //   */
  //  public Boolean  hasEntity(String entityName) {
  //    return super.hasEntity(entityName);
  //  }

  //  /**
  //   *  get an Entity from the room.
  //   */
  //  public <U extends IdentifiableEntity> U getEntity(
  //          String entityName, Class<U> clazz) {
  //    return super.getEntity(entityName, clazz);
  //  }
  //
  //  public  Map<String, IdentifiableEntity> getEntities() {
  //    return super.getEntities();
  //  }



  /**
   *  get an Item from the room.
   */
  public Item  getItem(String itemName) {
    return super.getEntity(itemName, Item.class);
  }



  /**
   * get the problem in the room.
   */
  public T getProblem() {
    return problem;
  }

  /**
   * get the String list of items in the room.
   */
  public <U extends IdentifiableEntity> String getElementNames(Class<U> clazz) {
    return String.join(", ", getEntitiesByType(clazz).stream()
            .map(U::getName)
            .toList());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    sb.append("\"room_name\":\"").append(getName()).append("\",");
    sb.append("\"room_number\":\"").append(getId()).append("\",");
    sb.append("\"description\":\"").append(getDescription()).append("\",");

    sb.append("\"N\":\"").append(exits.get("N")).append("\",");
    sb.append("\"S\":\"").append(exits.get("S")).append("\",");
    sb.append("\"E\":\"").append(exits.get("E")).append("\",");
    sb.append("\"W\":\"").append(exits.get("W")).append("\",");

    if (problem instanceof Puzzle) {
      sb.append("\"puzzle\":\"").append(problem.getName()).append("\",");
      sb.append("\"monster\":null,");
    } else if (problem instanceof Monster) {
      sb.append("\"puzzle\":null,");
      sb.append("\"monster\":\"").append(problem.getName()).append("\",");
    } else {
      sb.append("\"puzzle\":null,");
      sb.append("\"monster\":null,");
    }

    String items = getElementNames(Item.class);
    sb.append("\"items\":").append(items.isEmpty() ? "null" : "\"" + items + "\"").append(",");

    String fixtures = getElementNames(Fixture.class);
    sb.append("\"fixtures\":").append(fixtures.isEmpty() ? "null" : "\"" + fixtures + "\"").append(",");

    sb.append("\"picture\":\"").append(getPictureName()).append("\"");

    sb.append("}");
    return sb.toString();
  }
}
