package enginedriver;

import java.awt.Image;
import java.util.List;

/**
 * Class for player in the game.
 */
public class Player implements IIdentifiableEntity,
        Iattackable, IItemContainer {
  private String name;
  private int health;
  private List<String> inventory;
  private int maxWeight;
  private IdentifiableEntity identifiablePlayer;
  private int roomNumber; //the room that player is in
  private int score;

  public Player(String name, int health, int maxWeight) {
    this.name = name;
    this.health = health;
    this.maxWeight = maxWeight;
    score = 0;
  }
  /**
   * get current total weight of inventory.
   */
  public int getCurrentWeight() {
    return maxWeight;
  }

  public void gainOrLoseWeight(int w) {
    maxWeight += w;
  }

  public void gainOrLoseScore(int s) {
    score += s;
  }


  @Override
  public void takeDamage(int damage) {

  }

  @Override
  public String checkStatus() {
    return "";
  }

  @Override
  public int getHealth() {
    return 0;
  }


  @Override
  public String getId() {
    return "";
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public Image getPicture() {
    return null;
  }

  public int getRoomNumber() {
    return roomNumber;
  }

  public List<String> getItems() {
    return inventory;
  }

  /**
   * Add an item into player's inventory if it is within the maxWeight.
   *
   * @param item the name of the item to be added
   * @return true if the item is within the maxWeight and successfully added, false otherwise
   */
  @Override
  public void addItem(String item) {
    inventory.add(item);
  }

  @Override
  public void deleteItem(String item) {
    inventory.remove(item);
  }

  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"health\":\"" + getHealth() + "\"," +
            "\"inventory\":\"" + getItems() + "\"," +
            "\"maxWeight\":\"" + getCurrentWeight() + "\"," +
            "\"roomNumber\":\"" + getRoomNumber() + "\"," +
            "\"score\":\"" + score + "\"" +
            " }";
  }

}
