package enginedriver;

import java.util.List;


/**
 * Class representing the game world, read from json file
 * !!!! 这个地方需要考虑如何记录每一步的操作，方便复盘!!!!
 */
public class GameWorld {
  private String name;
  private String version;
  private List<Room> rooms;
  private List<IItem> items;
  private List<Fixture> fixtures;
  private List<Monster> monsters;
  private List<Puzzle> puzzles;
  private Player player; //记录生命值、item、位置等

  public GameWorld(String name, String version, List<Room> rooms,
                   List<IItem> items, List<Fixture> fixtures,
                   List<Monster> monsters, List<Puzzle> puzzles,
                   Player player) {
    this.name = name;
    this.version = version;
    this.rooms = rooms;
    this.items = items;
    this.fixtures = fixtures;
    this.monsters = monsters;
    this.puzzles = puzzles;
    this.player = player;
  }

  // Getters and Setters
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public List<Room> getRooms() {
    return rooms;
  }


  public Room getRoom(int id) {
    //id starts from 1 but index starts from 0
    if(id > rooms.size() || id < 1) {
      return null;
    }
    return rooms.get(id - 1);

  }

  public List<IItem> getItems() {
    return items;
  }

  public List<Fixture> getFixtures() {
    return fixtures;
  }

  public List<Monster> getMonsters() {
    return monsters;
  }

  public List<Puzzle> getPuzzles() {
    return puzzles;
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
  @Override
  public String toString() {
    return "GameWorld{" +
            "name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", rooms=" + rooms +
            ", items=" + items +
            ", fixtures=" + fixtures +
            ", monsters=" + monsters +
            ", puzzles=" + puzzles +
            ", player=" + player +
            '}';
  }


}
