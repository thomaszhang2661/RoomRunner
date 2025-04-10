package enginedriver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
   * Get a list of all items in the game world.

   * @return a list of all items
   */
  public List<Item> getItems() {
    return rooms.values().stream()
            .flatMap(room -> ((List<Item>) room.getEntitiesByType(Item.class)).stream())
            .collect(Collectors.toList());
  }

  /**
   * Get a list of all fixtures in the game world.

   * @return a list of all fixtures
   */
  public List<Fixture> getFixtures() {
    return rooms.values().stream()
            .flatMap(room -> ((List<Fixture>) room.getEntitiesByType(Fixture.class)).stream())
            .collect(Collectors.toList());
  }

  /**
   * Get a list of all monsters in the game world.

   * @return a list of all monsters
   */
  public List<Monster<?>> getMonsters() {
    return rooms.values().stream()
            .filter(room -> room.getProblem() instanceof Monster)
            .map(room -> (Monster<?>) room.getProblem())
            .collect(Collectors.toList());
  }

  /**
   * Get a list of all puzzles in the game world.

   * @return a list of all puzzles
   */
  public List<Puzzle<?>> getPuzzles() {
    return rooms.values().stream()
            .filter(room -> room.getProblem() instanceof Puzzle)
            .map(room -> (Puzzle<?>) room.getProblem())
            .collect(Collectors.toList());
  }
}
