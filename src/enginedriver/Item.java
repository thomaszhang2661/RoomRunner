package enginedriver;

import java.awt.Image;
import java.util.Map;


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



  //  /**
  //   * Constructor for an item with no max uses.
  //   */
  //  public void setRemainingUses(int remainingUses) {
  //    if (remainingUses >= 0) {
  //      this.remainingUses = remainingUses;
  //    } else {
  //      throw new IllegalArgumentException(
  //              "Remaining uses of an item cannot be less than 0");
  //    }
  //  }

  public int getRemainingUses() {
    return remainingUses;
  }

  public int getUseMax() {
    return maxUses;
  }

  @Override
  public int getValue() {
    return value;
  }

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
   */
  public boolean use() {
    // 1. 不能用了 返回使用失败
    if (remainingUses <= 0) {
      return false;
    }
    // 2. 还可以用,处理remainingUses，返回 whenUsed
    remainingUses--;
    return true;
  }
  //  @Override
  //  public int getId() {
  //    return super.getId();
  //  }
  //
  //  @Override
  //  public String getName() {
  //    return super.getName();
  //  }
  //
  //  @Override
  //  public String getDescription() {
  //    return super.getDescription();
  //  }
  //
  //  @Override
  //  public Image getPicture() {
  //    return null;
  //  }

  @Override
  public String toString() {
    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"weight\":\"" + getWeight() + "\","
            + "\"max_uses\":\"" + getUseMax() + "\","
            + "\"uses_remaining\":\"" + getRemainingUses() + "\","
            + "\"value\":\"" + getValue() + "\","
            + "\"when_used\":\"" + getWhenUsed() + "\","
            + "\"description\":\"" + getDescription()
                                       .replace("\"", "\\\"")
                                       .replace("\n", "\\n") + "\","
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
    //    return super.equals(o) && o instanceof Item item &&
    //            maxUses == item.maxUses &&
    //            remainingUses == item.remainingUses &&
    //            value == item.value &&
    //            weight == item.weight &&
    //            whenUsed.equals(item.whenUsed);
    return super.equals(o);
  }

  /**
   * hashcode method for the item class.

   * @return the hashcode of the item
   */
  @Override
  public int hashCode() {
    //return super.hashCode() + Objects.hash(maxUses, remainingUses, value, weight, whenUsed);
    return super.hashCode();
  }
}
