package enginedriver;

import java.awt.*;

public class Item implements IItem{
  private int maxUses;
  private int remainingUses;
  private int value;
  private int weight;
  private String whenUsed;
  private IdentifiableEntity identifiableItem;

  public Item(int maxUses, int remainingUses, int value, int weight,
              String whenUsed, IdentifiableEntity identifiableItem) {
    this.maxUses = maxUses;
    this.remainingUses = remainingUses;
    this.value = value;
    this.weight = weight;
    this.whenUsed = whenUsed;
    this.identifiableItem = identifiableItem;
  }

  @Override
  public void use() {

  }

  @Override
  public int getUseRemain() {
    return 0;
  }

  @Override
  public int getUseMax() {
    return 0;
  }

  @Override
  public int getValue() {
    return 0;
  }

  @Override
  public int getWeight() {
    return 0;
  }

  public String getWhenUsed() {return "";}

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

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"weight\":\"" + getWeight() + "\"," +
            "\"max_uses\":\"" + getUseMax() + "\"," +
            "\"uses_remaining\":\"" + getUseRemain() + "\"," +
            "\"value\":\"" + getValue() + "\"," +
            "\"when_used\":\"" + getWhenUsed() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }
}
