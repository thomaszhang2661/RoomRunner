package enginedriver;

import java.util.Map;


/**
 * Class representing the game world, read from json file
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

  // Default constructor for deserialization
  public GameWorld() {
  }

  public GameWorld(String name, String version,
                   Map<Integer, Room> rooms) {
    this.name = name; // ？？？need name TODO
    this.version = version;
    this.rooms = rooms;
//    this.player = player;
//    this.score = score;

  }


  // Getters and Setters
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public Map<Integer,Room> getRooms() {
    return rooms;
  }


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
    sb.append("{ ");
    sb.append("\"name\":\"").append(name).append("\",");
    sb.append("\"version\":\"").append(version).append("\",");

    sb.append("\"rooms\":[");
    for (Room<?> room : rooms.values()) {
      sb.append(room.toString()).append(",");
    }
    if (!rooms.isEmpty()) sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("],");

    sb.append("\"items\":[");
    for (Room<?> room : rooms.values()) {
      for (Item item : room.getEntitiesByType(Item.class)) {
        sb.append(item.toString()).append(",");
      }
    }
    if (!rooms.isEmpty()) sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("],");

    sb.append("\"fixtures\":[");
    for (Room<?> room : rooms.values()) {
      for (Fixture fixture : room.getEntitiesByType(Fixture.class)) {
        sb.append(fixture.toString()).append(",");
      }
    }
    if (!rooms.isEmpty()) sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("],");

    sb.append("\"monsters\":[");
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Monster) {
        Monster<?> monster = (Monster<?>) room.getProblem();
        sb.append(monster.toString()).append(",");
      }
    }
    if (!rooms.isEmpty()) sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("],");

    sb.append("\"puzzles\":[");
    for (Room<?> room : rooms.values()) {
      if (room.getProblem() instanceof Puzzle) {
        Puzzle<?> puzzle = (Puzzle<?>) room.getProblem();
        sb.append(puzzle.toString()).append(",");
      }
    }
    if (!rooms.isEmpty()) sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("],");

//    sb.append("\"player\":").append(player.toString());
    sb.append(" }");
    return sb.toString();
  }

//  public Item findItemByName(String name) {
//    if (name == null) {
//      throw new IllegalArgumentException("Name cannot be null.");
//    }
//    return items.get(name);
//    }
}
