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
    this.name = name;
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
   * Retrieve the version of the GameWorld.
   * @return the version of the world
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
    sb.append("{\n");
    sb.append("\"name\":\"").append(name).append("\",\n");
    sb.append("\"version\":\"").append(version).append("\",\n\n");

    sb.append("\"rooms\":[\n");
    boolean firstRoom = true;
    for (Room<?> room : rooms.values()) {
      if (!firstRoom) {
        sb.append(",\n");
      }
      sb.append(room.toString());
      firstRoom = false;
    }
    sb.append("],\n\n");

    sb.append("\"items\":[\n");
    boolean firstItem = true;
    for (Room<?> room : rooms.values()) {
      for (Item item : room.getEntitiesByType(Item.class)) {
        if (!firstItem) {
          sb.append(",\n");
        }
        sb.append(item.toString());
        firstItem = false;
      }
    }
    sb.append("],\n\n");

    sb.append("\"fixtures\":[\n");
    boolean firstFixture = true;
    for (Room<?> room : rooms.values()) {
      for (Fixture fixture : room.getEntitiesByType(Fixture.class)) {
        if (!firstFixture) {
          sb.append(",\n");
        }
        sb.append(fixture.toString());
        firstFixture = false;
      }
    }
    sb.append("],\n\n");

    sb.append("\"monsters\":[\n");
    boolean firstMonster = true;
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Monster) {
        if (!firstMonster) {
          sb.append(",\n");
        }
        sb.append(((Monster<?>) room.getProblem()).toString());
        firstMonster = false;
      }
    }
    sb.append("],\n\n");

    sb.append("\"puzzles\":[\n");
    boolean firstPuzzle = true;
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Puzzle) {
        if (!firstPuzzle) {
          sb.append(",\n");
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
