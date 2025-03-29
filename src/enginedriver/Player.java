package enginedriver;

import java.awt.Image;
import java.util.Map;

/**
 * Class for player in the game.
 */
public class Player extends EntityContainer<Item> {
  private int health;
  private int maxWeight;
  private int currentWeight;
  private int roomNumber; //the room that player is in

  /**
   * simple Constructor for a player.
   */
  public Player(String name, int health, int maxWeight) {
    super(-1, name, "Player");
    this.health = health;
    this.maxWeight = maxWeight;
  }


  /**
   *  Constructor for a player with items.
   */
  public Player(String name, int health, int maxWeight,
                Map<String, Item> items) {
    super(-1, name, "Player", items);
    this.health = health;
    this.maxWeight = maxWeight;
    updateCurrentWeight();
  }


  /**
   * get current total weight of inventory.
   */
  public int getCurrentWeight() {
    return currentWeight;
  }

  private void updateCurrentWeight() {
    currentWeight = 0;
    // 获取泛型类型的实体
    Map<String, Item> playerEntities = super.getEntities();
    for (Map.Entry<String, Item> entry : playerEntities.entrySet()) {
      //if (entry.getValue() != null) {
      Item item =  entry.getValue();  // 强制类型转换为 Item 类型
      currentWeight += item.getWeight();    // 获取物品的重量并累加
      //}
    }

  }

  public int getMaxWeight() {
    return maxWeight;
  }
  //  public int getCurrentWeight() {
  //    int currentWeight = 0;
  //    Map<String,IdentifiableEntity> playerEntities = super.getEntities();
  //    for (Map.Entry<String, IdentifiableEntity>  item : playerEntities.entrySet()) {
  //      currentWeight += item.getWeight();
  //    }
  //    for (IItem item : inventory) {
  //      currentWeight += item.getWeight();
  //    }
  //  }


  //TODO 还需要吗？直接updateCurrentWeight()就可以了？
  private void gainOrLoseWeight(int w) {
    maxWeight += w;
  }

  // move to gameWorld
//  public void gainOrLoseScore(int s) {
//    score += s;
//  }

  public void gainOrLoseHealth(int h) {
    health += h;
    if (health < 0) {
      health = 0;
    }
  }


  public HEALTH_STATUS checkStatus() {
  //TODO check homework websit, may want to use ENUM HEALTH_STATUS
//    if (health > 75) {
//      return "Healthy";
//    } else if (health > 50) {
//      return "Injured";
//    } else if (health > 25) {
//      return "Badly Injured";
//    } else {
//      return "Near Death";
//    }
    return null;
  }

  public int getHealth() {
    return health;
  }


  @Override
  public String getId() {
    return super.getId();
  }

  public String getName() {
    return super.getName();
  }

  public String getDescription() {
    return super.getDescription();
  }

  public Image getPicture() {
    return null;
  }

  public int getRoomNumber() {
    return roomNumber;
  }


  @Override
  public Map<String, Item> getEntities() {
    return super.getEntities();
  }

  /**
   * Add an item into player's inventory if it is within the maxWeight.
   *
   * @param item the name of the item to be added
   * @return true if the item is within the maxWeight and successfully added, false otherwise
   */


  public boolean addItem(Item item) {
    if (item.getWeight() + getCurrentWeight() <= maxWeight) {
      super.addEntity(item);
      updateCurrentWeight();
      return true;
    }
    return false;
  }

  /**
   * Delete an item from player's inventory.
   */
  public boolean removeItem(Item item) {
    if (super.removeEntity(item)) {
      updateCurrentWeight();
      return true;
    }
    return false;
  }

  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  public Map<String, Item> getItems() {
    return super.getEntities();
  }

  /**
   * Get an item from player's inventory.
   */
  public <U> U getEntity(String entityName, Class<U> clazz) {
    return super.getEntity(entityName, clazz);  // 直接调用父类的泛型方法
  }

}
