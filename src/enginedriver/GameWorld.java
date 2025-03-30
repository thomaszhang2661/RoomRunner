package enginedriver;

import java.util.Map;


/**
 * Class representing the game world, read from json file.
 * !!!! 这个地方需要考虑如何记录每一步的操作，方便复盘!!!!
 */
public class GameWorld {
  private String name;
  private String version;
  private Map<Integer, Room> rooms;
  //  private Map<String, Item> items;
  //  private Map<String, Fixture> fixtures;
  //  private Map<String, Monster> monsters;
  //  private Map<String, Puzzle> puzzles;
  //  private int score;
  //  private Player player; //记录生命值、item、位置等

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
    this.name = name; // ？？？need name TODO
    this.version = version;
    this.rooms = rooms;
    //  this.player = player;
    //  this.score = score;

  }


  // Getters and Setters
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public Map<Integer, Room> getRooms() {
    return rooms;
  }


  /**
   * Get the room by id.

   * @param id the id of the room
   * @return the room with the given id
   */
  public Room getRoom(int id) {
    //id starts from 1 but index st
    return rooms.get(id);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setRooms(Map<Integer, Room>  rooms) {
    this.rooms = rooms;
  }
  //  public void setScore(int score) {
  //    player.setScore(score);
  //  }

  //  public void addScore(int inputValue) {
  //    this.score += inputValue;
  //  }
  //
  //  public int getScore() {
  //    return score;
  //  }

  //  public void setPlayer(String name, int health, int maxWeight, int score) {
  //    this.player = new Player(name, health, maxWeight, score);
  //  }
  //  public void setPlayer(Player player) {
  //    this.player = player;
  //  }

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

  //  public Item findItemByName(String name) {
  //    if (name == null) {
  //      throw new IllegalArgumentException("Name cannot be null.");
  //    }
  //    return items.get(name);
  //    }
}
