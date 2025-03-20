package enginedriver;

import java.awt.*;

public class item implements IItem{
  private int maxUses;
  private int remainingUses;
  private int value;
  private int weight;
  private IdentifiableEntity identifiableItem;
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
}
