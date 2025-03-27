package enginedriver;

import java.awt.*;

public class Monster<T> extends Problem<T>{
  private int damage;
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
                String target, String pictureName) {
    super(name, description, active, affects_target,
            affects_player, solution, value, effects, target,
            pictureName);
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
    return super.getPicture();
  }

  @Override
  public boolean isSolved() {
    return super.isSolved();
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

  public void attack(Player player) {
    // Attack the player
    player.gainOrLoseHealth(damage);
  }
}
