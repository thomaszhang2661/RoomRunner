package enginedriver;

import java.util.Map;

import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;


/**
 * Class representing the game world, read from json file.
 * The game world contains rooms, items, fixtures, monsters, and puzzles.
 */
public class GameWorld {
  private String name;
  private String version;
  private Map<Integer, Room> rooms;

  /**
   * Default constructor for deserialization.
   * This constructor is used by Jackson to create an instance of GameWorld
   */
  public GameWorld() {}

  /**
   * Constructor for GameWorld.

   * @param name    the name of the game world
   * @param version the version of the game world
   * @param rooms   the rooms in the game world
   */
  public GameWorld(String name, String version,
                   Map<Integer, Room> rooms) {
    this.name = name; // ？？？need name TODO
    this.version = version;
    this.rooms = rooms;
  }


  /**
   * Get the name of the game world.

   * @return the name of the game world
   */
  public String getName() {
    return name;
  }

  /**
   * Get the version of the game world.

   * @return the version of the game world
   */
  public String getVersion() {
    return version;
  }

  /**
   * Get the rooms in the game world.

   * @return the rooms in the game world
   */
  public Map<Integer, Room> getRooms() {
    return rooms;
  }

  /**
   * Get a room by its ID.

   * @param id The ID of the room.
   * @return The room with the specified ID.
   */
  public Room<?> getRoom(int id) {
    //id starts from 1 but index st
    return rooms.get(id);
  }

  /**
   * Set the name of the gameWorld object.

   * @param name the name of the game world
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the version of the game world.

   * @param version the version of the game world
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Set the rooms in the game world.

   * @param rooms the rooms in the game world
   */
  public void setRooms(Map<Integer, Room>  rooms) {
    this.rooms = rooms;
  }

  /**
   * Get a string representation of the game world.

   * @return a string representation of the game world
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"name\":\"").append(name).append("\",");
    sb.append("\"version\":\"").append(version).append("\",");

    sb.append("\"rooms\":[");
    boolean firstRoom = true;
    for (Room<?> room : rooms.values()) {
      if (!firstRoom) {
        sb.append(",");
      }
      sb.append(room.toString());
      firstRoom = false;
    }
    sb.append("],");

    sb.append("\"items\":[");
    boolean firstItem = true;
    for (Room<?> room : rooms.values()) {
      for (Item item : room.getEntitiesByType(Item.class)) {
        if (!firstItem) {
          sb.append(",");
        }
        sb.append(item.toString());
        firstItem = false;
      }
    }
    sb.append("],");

    sb.append("\"fixtures\":[");
    boolean firstFixture = true;
    for (Room<?> room : rooms.values()) {
      for (Fixture fixture : room.getEntitiesByType(Fixture.class)) {
        if (!firstFixture) {
          sb.append(",");
        }
        sb.append(fixture.toString());
        firstFixture = false;
      }
    }
    sb.append("],");

    sb.append("\"monsters\":[");
    boolean firstMonster = true;
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Monster) {
        if (!firstMonster) {
          sb.append(",");
        }
        sb.append(((Monster<?>) room.getProblem()).toString());
        firstMonster = false;
      }
    }
    sb.append("],");

    sb.append("\"puzzles\":[");
    boolean firstPuzzle = true;
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Puzzle) {
        if (!firstPuzzle) {
          sb.append(",");
        }
        sb.append(((Puzzle<?>) room.getProblem()).toString());
        firstPuzzle = false;
      }
    }
    sb.append("]");

    sb.append("}");
    return sb.toString();
  }
}
