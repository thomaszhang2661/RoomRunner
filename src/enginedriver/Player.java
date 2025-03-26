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
  private List<IItem> inventory;
  private int maxWeight;
  private IdentifiableEntity identifiablePlayer;
  private int roomNumber; //the room that player is in

  public Player(String name, int health, int maxWeight) {
    this.name = name;
    this.health = health;
    this.maxWeight = maxWeight;
  }
  /**
   * get current total weight of inventory.
   */
  private int getCurrentWeight() {
    //TODO
    return 0;
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
  /**
   * Add an item into player's inventory if it is within the maxWeight.
   *
   * @param item the item to be added
   * @return true if the item is within the maxWeight and successfully added, false otherwise
   */
  @Override
  public boolean addItem(IItem item) {
    if (item.getWeight() <= maxWeight) {
      maxWeight -= item.getWeight();
      inventory.add(item);
      return true;
    }
    return false;
  }

  @Override
  public boolean deleteItem(IItem item) {
    if (inventory.contains(item)) {
      inventory.remove(item);
      maxWeight += item.getWeight();
      return true;
    }
    return false;
  }

  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  public int getRoomNumber() {
    return roomNumber;
  }
}
