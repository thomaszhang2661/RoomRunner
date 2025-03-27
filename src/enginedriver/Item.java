package enginedriver;

import java.awt.*;

public class Item  extends IdentifiableEntity implements IItem{
  private int maxUses;
  private int remainingUses;
  private int value;
  private int weight;
  private String whenUsed;
  private IdentifiableEntity identifiableItem;

  public Item(String name, String description,int maxUses,
                                         int remainingUses,
                                         int value,
                                         int weight,
              String whenUsed) {
    super(name, description);
    this.maxUses = maxUses;
    this.remainingUses = remainingUses;
    this.value = value;
    this.weight = weight;
    this.whenUsed = whenUsed;

  }

  @Override
  public void use() {

  }

  @Override
  public int getUseRemain() {
    return remainingUses;
  }

  @Override
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

  @Override
  public String getId() {
    return super.getId();
  }

  @Override
  public String getName() {
    return super.getName();
  }

  @Override
  public String getDescription() {
    return super.getDescription();
  }

  @Override
  public Image getPicture() {
    return null;
  }
}
