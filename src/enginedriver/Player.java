package enginedriver;

import java.awt.Image;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for player in the game.
 */
public class Player extends EntityContainer<Item> {
  private int health;
  private final int maxWeight;
  private int currentWeight;
  private int roomNumber; //the room that player is in
  private int score;

  /**
   * simple Constructor for a player.
   */
  public Player(String name, int health, int maxWeight) {
    super(-1, name, "Player");
    this.health = health;
    this.maxWeight = maxWeight;
  }

  /**
   * simple Constructor for a player with score.
   */
  public Player(String name, int health, int maxWeight, int score) {
    super(-1, name, "Player");
    this.health = health;
    this.maxWeight = maxWeight;
    this.score = score;
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
   *  Constructor for a player with items and score.
   */
  public Player(String name, int health, int maxWeight,
                Map<String, Item> items, int score) {
    super(-1, name, "Player", items);
    this.health = health;
    this.maxWeight = maxWeight;
    this.score = score;
    updateCurrentWeight();
  }

  /**
   *  Constructor for an existing player.
   */
  public Player(String name, int health, int maxWeight,
                int currentWeight, int roomNumber,
                Map<String, Item> items, int score) {
    super(-1, name, "Player", items);
    this.health = health;
    this.maxWeight = maxWeight;
    this.currentWeight = currentWeight;
    this.roomNumber = roomNumber;
    this.score = score;
    updateCurrentWeight();
  }


  /**
   * Get current total weight of inventory.
   */
  public int getCurrentWeight() {
    return currentWeight;
  }

  /**
   * Updates and returns the current weight of player's inventory.
   */
  private void updateCurrentWeight() {
    currentWeight = 0;
    // 获取泛型类型的实体
    Map<String, Item> playerEntities = super.getEntities();
    for (Map.Entry<String, Item> entry : playerEntities.entrySet()) {
      Item item =  entry.getValue();  // 强制类型转换为 Item 类型
      currentWeight += item.getWeight();    // 获取物品的重量并累加
    }

  }

  /**
   * Retrieves the max weight that the player can carry.

   * @return the max weight.
   */
  public int getMaxWeight() {
    return maxWeight;
  }


  /**
   * Determines whether a player gains or loses health.

   * @param h either the damage or the healing that affects the health.
   */
  public void gainOrLoseHealth(int h) {
    health += h;
    if (health < 0) {
      health = 0;
    }
  }


  /**
   * Determines which health status the player is in.

   * @return the health status.
   */
  public HEALTH_STATUS checkStatus() {
    if (this.health <= 0) {
      return HEALTH_STATUS.SLEEP;
    }
    if (this.health < 40) {
      return HEALTH_STATUS.WOOZY;
    }
    if (this.health < 70) {
      return HEALTH_STATUS.FATIGUED;
    }
    return HEALTH_STATUS.AWAKE;
  }

  /**
   * Retrieves the player's health.

   * @return player's health.
   */
  public int getHealth() {
    return health;
  }

  //  @Override
  //  public int getId() {
  //    return super.getId();
  //  }
  //
  //  /**
  //   * Retrieves the name.
  //
  //   * @return name of player.
  //   */
  //  public String getName() {
  //    return super.getName();
  //  }
  //
  //  /**
  //   * Retrieves the description.
  //
  //   * @return player's description.
  //   */
  //  public String getDescription() {
  //    return super.getDescription();
  //  }
  //
  //  /**
  //   * Retrieves a picture of the player.
  //
  //   * @return picture.
  //   */
  //  public Image getPicture() {
  //    return null;
  //  }

  /**
   * Retrieves the room number, which represents which room we're currently in.

   * @return the current location of the player.
   */
  public int getRoomNumber() {
    return roomNumber;
  }

  /**
   * Sets the player' score.
   */
  public void setScore(int score) {
    this.score = score;
  }

  /**
   * get the player's score.
   */
  public int getScore() {
    return score;
  }

  /**
   * Add score to player.

   * @param score the score to be added.
   */
  public void addScore(int score) {
    this.score += score;
  }

  //  /**
  //   * get all items in player's inventory.
  //   */
  //  @Override
  //  public Map<String, Item> getEntities() {
  //    return super.getEntities();
  //  }

  /**
   * Add an item into player's inventory if it is within the maxWeight.

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

  /**
   * Sets the room number.

   * @param roomNumber a number that represents a specific room.
   */
  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

  //  /**
  //   *  get an Entity from the room.
  //   */
  //  public <U extends IdentifiableEntity> U getEntity(
  //          String entityName, Class<U> clazz) {
  //    return super.getEntity(entityName, clazz);
  //  }


  @Override
  public String toString() {
    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"health\":\"" + getHealth() + "\","
            + "\"inventory\":\"" + String.join(", ", getEntities().keySet()) + "\","
            + "\"max_weight\":\"" + getMaxWeight() + "\","
            + "\"current_weight\":\"" + getCurrentWeight() + "\","
            + "\"room_number\":\"" + getRoomNumber() + "\","
            + "\"score\":\"" + getScore() + "\""
            + " }";
  }

}
