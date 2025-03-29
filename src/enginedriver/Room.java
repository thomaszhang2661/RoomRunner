package enginedriver;

import java.util.Map;

/**
 * class for a room in the game.
 */
public class Room<T> extends  EntityContainer<IdentifiableEntity> {
  private Map<String, Integer> exits;
  private T problem;

  /**
   * Simple Constructor for a room
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits, T problem) {
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
   * Constructor for a room with Map of Entities.
   */
  public Room(int id, String name, String description,
              Map<String, Integer> exits,
              Map<String,IdentifiableEntity> entityNames,
              T problem) {
    super(id, name, description, entityNames);
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
    return String.join(", " + getEntitiesByType(clazz).stream()
            .map(U::getName)
            .toList());
  }

  @Override
  public String toString() {
    return "{ " +
            "\"room_name\":\"" + getName() + "\"," +
            "\"room_number\":\"" + getId() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"N\":\"" + getExits().get("N") + "\"," +
            "\"S\":\"" + getExits().get("S") + "\"," +
            "\"E\":\"" + getExits().get("E") + "\"," +
            "\"W\":\"" + getExits().get("W") + "\"," +
            (getProblem() instanceof Monster
                    ? "\"puzzle\":null,\"monster\":\"" + getProblem() + "\","
                    : "\"puzzle\":\"" + getProblem() + "\",\"monster\":null," + "\",") +
            "\"items\":\"" + getElementNames(Item.class) + "\"," +
            "\"fixtures\":\"" + getElementNames(Fixture.class) + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }

}
