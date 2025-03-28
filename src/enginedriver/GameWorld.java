package enginedriver;

import java.util.List;
import java.util.Map;


/**
 * Class representing the game world, read from json file
 * !!!! 这个地方需要考虑如何记录每一步的操作，方便复盘!!!!
 */
public class GameWorld {
  private String name;
  private String version;
  private Map<Integer, Room> rooms;
  private Map<String, Item> items;
  private Map<String, Fixture> fixtures;
  private Map<String, Monster> monsters;
  private Map<String, Puzzle> puzzles;

  private Player player; //记录生命值、item、位置等

  public GameWorld(String name, String version,
                   Map<Integer, Room> rooms,
                   Map<String, Item> items,
                   Map<String, Fixture> fixtures,
                   Map<String, Monster> monsters,
                   Map<String, Puzzle> puzzles,
                   Player player) {
    this.name = name;
    this.version = version;
    this.rooms = rooms;
    this.items = items;
    this.fixtures = fixtures;
    this.monsters = monsters;
    this.puzzles = puzzles;
    //TODO: from data to Maps ??
    this.player = player;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public Map<String,Room> getRooms() {
    return rooms;
  }


  public Room getRoom(int id) {
    //id starts from 1 but index st
    return rooms.get(id);
  }



  public Fixture getFixture(String name) {
    return fixtures.get(name);
  }

  public Monster getMonster(String name) {
    return monsters.get(name);
  }

  public Puzzle getPuzzle(String name) {
    return puzzles.get(name);
  }

  public Player getPlayer() {
    return player;
  }

  public void setName(String name) {
    this.name = name;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }
  public void setItems(List<IItem> items) {
    this.items = items;
  }
  public void setFixtures(List<Fixture> fixtures) {
    this.fixtures = fixtures;
  }
  public void setMonsters(List<Monster> monsters) {
    this.monsters = monsters;
  }
  public void setPuzzles(List<Puzzle> puzzles) {
    this.puzzles = puzzles;
  }
  public void setPlayer(String name, int health, int maxWeight) {
    this.player = new Player(name, health, maxWeight);
  }
  public void setPlayer(Player player) {
    this.player = player;
  }
//  @Override
//  public String toString() {
//    return "GameWorld{" +
//            "name='" + name + '\'' +
//            ", version='" + version + '\'' +
//            ", rooms=" + rooms +
//            ", items=" + items +
//            ", fixtures=" + fixtures +
//            ", monsters=" + monsters +
//            ", puzzles=" + puzzles +
//            ", player=" + player +
//            '}';
//  }
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    sb.append("\"name\":\"").append(name).append("\",");
    sb.append("\"version\":\"").append(version).append("\",");

    sb.append("\"rooms\":{");
    for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
      sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toString()).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("},");

    sb.append("\"items\":{");
    for (Map.Entry<String, Item> entry : items.entrySet()) {
      sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toString()).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("},");

    sb.append("\"fixtures\":{");
    for (Map.Entry<String, Fixture> entry : fixtures.entrySet()) {
      sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toString()).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("},");

    sb.append("\"monsters\":{");
    for (Map.Entry<String, Monster> entry : monsters.entrySet()) {
      sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toString()).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("},");

    sb.append("\"puzzles\":{");
    for (Map.Entry<String, Puzzle> entry : puzzles.entrySet()) {
      sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toString()).append(",");
    }
    sb.deleteCharAt(sb.length() - 1); // Remove trailing comma
    sb.append("},");

    sb.append("\"player\":").append(player.toString());
    sb.append(" }");

    return sb.toString();
  }

  public Item findItemByName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    return items.get(name);
    }
}
