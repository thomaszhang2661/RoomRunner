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

  // Getters and Setters
}
