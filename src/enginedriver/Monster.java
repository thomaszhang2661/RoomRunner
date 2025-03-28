package enginedriver;

import java.awt.*;

public class Monster implements IMonster{
  @Override
  public int getDamage() {
    return 0;
  }

  @Override
  public boolean isSolved() {
    return false;
  }

  @Override
  public void solve(String input) {

  }

  @Override
  public void solve(IItem item) {

  }

  @Override
  public void get_effect() {

  }

  @Override
  public int getValue() {
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

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"active\":\"" + isActive() + "\"," +
            "\"affects_target\":\"" + affectsTarget() + "\"," +
            "\"affects_player\":\"" + affectsPlayer() + "\"," +
            "\"solution\":\"" + getSolution() + "\"," +
            "\"value\":\"" + getValue() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"effects\":\"" + getEffects() + "\"," +
            "\"damage\":\"" + getDamage() + "\"," +
            "\"target\":\"" + getTarget() + "\"," +
            "\"can_attack\":\"" + canAttack() + "\"," +
            "\"attack\":\"" + getAttack() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }
}
