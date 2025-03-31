package enginedriver;

/**
 * Class for items in the game.
 * Items are objects that can be used by the player.
 * Items have a name, description, max uses, remaining uses,
 * value, weight, and when used.
 */
public class Item  extends IdentifiableEntity implements  IValuable, IWeightable {
  private final int maxUses;
  private int remainingUses;
  private final int value;
  private final int weight;
  private final String whenUsed;

  /**
   * Constructor for an item.

   * @param name the name of the item
   * @param description the description of the item
   * @param maxUses the max uses of the item
   * @param remainingUses the remaining uses of the item
   * @param value the value of the item
   * @param weight the weight of the item
   * @param whenUsed a description when used the item
   * @param pictureName the picture name of the item
   */
  public Item(String name, String description, int maxUses,
              int remainingUses, int value,
              int weight, String whenUsed, String pictureName) {
    super(name, description, pictureName);



    if (remainingUses < 0) {
      throw new IllegalArgumentException("Remaining uses of an item cannot be less than 0");
    }
    if (maxUses < 0) {
      throw new IllegalArgumentException("Max uses of an item cannot be less than 0");
    }
    this.maxUses = maxUses;
    this.remainingUses = remainingUses;
    this.value = value;
    this.weight = weight;
    this.whenUsed = whenUsed;

  }

  /**
   * Constructor for an item.

   * @param name the name of the item
   * @param description the description of the item
   * @param maxUses the max uses of the item
   * @param remainingUses the remaining uses of the item
   * @param value the value of the item
   * @param weight the weight of the item
   * @param whenUsed a description when used the item
   */
  public Item(String name, String description, int maxUses,
              int remainingUses, int value,
              int weight, String whenUsed) {
    super(name, description);



    if (remainingUses < 0) {
      throw new IllegalArgumentException("Remaining uses of an item cannot be less than 0");
    }
    if (maxUses < 0) {
      throw new IllegalArgumentException("Max uses of an item cannot be less than 0");
    }
    this.maxUses = maxUses;
    this.remainingUses = remainingUses;
    this.value = value;
    this.weight = weight;
    this.whenUsed = whenUsed;

  }

  /**
   * Getter for remainingUses.

   * @return int remainingUses
   */
  public int getRemainingUses() {
    return remainingUses;
  }

  /**
   * Getter for maxUses.

   * @return int maxUses
   */
  public int getUseMax() {
    return maxUses;
  }

  /**
   * Getter for value.

   * @return int value
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * Getter for weight.

   * @return int weight
   */
  @Override
  public int getWeight() {
    return weight;
  }

  /**
   * Getter for whenUsed.
   *
   * @return string whenUsed
   */
  public String getWhenUsed() {
    return whenUsed;
  }

  /**
   * Use the item.
   * If the item can be used, the remaining uses are decremented by 1.
   * If the item cannot be used, the remaining uses are not decremented.

   * @return true if the item can be used, false otherwise
   */
  public boolean use() {
    // remaining uses <= 0, cannot use
    if (remainingUses <= 0) {
      return false;
    }
    // remaining uses > 0, can use
    remainingUses--;
    return true;
  }

  /**
   * toString method for the item class.

   * @return a string representation of the item
   */
  @Override
  public String toString() {
    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"weight\":\"" + getWeight() + "\","
            + "\"max_uses\":\"" + getUseMax() + "\","
            + "\"uses_remaining\":\"" + getRemainingUses() + "\","
            + "\"value\":\"" + getValue() + "\","
            + "\"when_used\":\"" + getWhenUsed() + "\","
            + "\"description\":\"" + getDescription().replace("\n", "\\n") + "\","
            + "\"picture\":\"" + getPictureName() + "\""
            + " }";
  }

  /**
   * equals method for the item class.

   * @param o the object to compare to
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  /**
   * hashcode method for the item class.

   * @return the hashcode of the item
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
