package enginedriver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item  extends IdentifiableEntity implements  IValuable,IWeightable {
  private int maxUses;
  private int remainingUses;
  private int value;
  private int weight;
  private String whenUsed;
  private IdentifiableEntity identifiableItem;

  @JsonCreator
  public Item(@JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("max_uses")int maxUses,
              @JsonProperty("uses_remaining") int remainingUses,
              @JsonProperty("value") int value,
              @JsonProperty("weight") int weight,
              @JsonProperty("when_used") String whenUsed) {
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

  public int getUseRemain() {
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

  public String getWhenUsed() {
    return whenUsed;
  }

  @Override
  public int getId() {
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
