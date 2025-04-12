package enginedriver.model.entitycontainer;

import java.util.Map;

import enginedriver.model.entitycontainer.EntityContainer;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entity.Item;

/**
 * Class for player in the game.
 */
public class Player extends EntityContainer<Item> {
  private int health;
  private final int maxWeight;
  private int currentWeight;
  private int roomNumber = 1; //the room that player is in
  private int score;

  /**
   * simple Constructor for a player.

   * @param name the name of the player
   * @param health the health of the player
   * @param maxWeight the max weight of the player
   */
  public Player(String name, int health, int maxWeight) {
    super(-1, name, "Player");
    this.health = health;
    this.maxWeight = maxWeight;
  }

  /**
   * simple Constructor for a player with score.

   * @param name the name of the player
   * @param health the health of the player
   * @param maxWeight the max weight of the player
   * @param score the score of the player
   */
  public Player(String name, int health, int maxWeight, int score) {
    super(-1, name, "Player");
    this.health = health;
    this.maxWeight = maxWeight;
    this.score = score;
  }

  /**
   *  Constructor for a player with items.

   * @param name the name of the player
   * @param health the health of the player
   * @param maxWeight the max weight of the player
   * @param items the items of the player
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

   * @param name the name of the player
   * @param health the health of the player
   * @param maxWeight the max weight of the player
   * @param items the items of the player
   * @param score the score of the player
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
   * Constructor for an existing player.

   * @param name the name of the player
   * @param health the health of the player
   * @param maxWeight the max weight of the player
   * @param currentWeight the current weight of the player
   * @param roomNumber the room number of the player
   * @param items the items of the player
   * @param score the score of the player
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

   * @return current weight of inventory.
   */
  public int getCurrentWeight() {
    return currentWeight;
  }

  /**
   * Update current weight of inventory.
   */
  private void updateCurrentWeight() {
    currentWeight = 0;
    // get all items in the inventory
    Map<String, Item> playerEntities = super.getEntities();
    for (Map.Entry<String, Item> entry : playerEntities.entrySet()) {
      Item item =  entry.getValue();  // get the item
      // get the weight of the item and add it to the current weight
      currentWeight += item.getWeight();
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

  /**
   * Retrieves the room number, which represents which room we're currently in.

   * @return the current location of the player.
   */
  public int getRoomNumber() {
    return roomNumber;
  }

  /**
   * Sets the player's score.
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
   * get the String list of items in the room.

   * @param clazz the class of the entity
   *  @return the string list of items
   */
  public <U extends IdentifiableEntity> String getElementNames(Class<U> clazz) {
    return String.join(", ", getEntitiesByType(clazz).stream()
            .map(U::getName)
            .toList());
  }

  /**
   * Sets the room number.

   * @param roomNumber a number that represents a specific room.
   */
  public void setRoomNumber(int roomNumber) {
    this.roomNumber = roomNumber;
  }

}
