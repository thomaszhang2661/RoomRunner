package enginedriver;

import java.awt.*;

public class Monster<T> extends Problem<T>{
  private int damage;
  private String attack;
  /**
   * Constructor for Puzzle.
   */
  public Monster(String name,
                String description,
                Boolean active,
                Boolean affects_target,
                Boolean affects_player,
                T solution,
                int value,
                int damage,
                String effects,
                String target, String pictureName, String attack) {
    super(name, description, active, affects_target,
            affects_player, solution, value, effects, target,
            pictureName);
    this.attack = attack;
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
    return super.getPicture();
  }

  @Override
  public boolean getActive() {
    return super.getActive();
  }

  @Override
  public boolean solve(T input) {
    return super.solve(input);
  }

  @Override
  public int getValue() {
    return super.getValue();
  }

  @Override
  public String getEffects() {
    return super.getEffects();
  }

  public int getDamage() {
    return damage;
  }

  public void attack(Player player) {
    // Attack the player
    if (super.getActive() && super.getAffect_player()) {
      player.gainOrLoseHealth(-Math.abs(damage));
    }
  }

  public boolean canAttack() {
    return super.isSolved();
  }

  public String getAttack() {
    return attack;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"active\":\"" + isSolved() + "\"," +
            "\"affects_target\":\"" + getAffects_target() + "\"," +
            "\"affects_player\":\"" + getAffect_player() + "\"," +
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
