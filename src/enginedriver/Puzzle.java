package enginedriver;

import java.awt.Image;

/**
 * Class for puzzles in the game.
 */
public class Puzzle<T> extends Problem<T> {

  /**
   * Constructor for Puzzle.
   */
  public Puzzle(String name,
                String description,
                Boolean active,
                Boolean affectsTarget,
                Boolean affectsPlayer,
                T solution,
                int value,
                String effects,
                String target, String pictureName) {
    super(name, description, active, affectsTarget,
            affectsPlayer, solution, value, effects, target,
            pictureName);
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

  @Override
  public String toString() {
    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"active\":\"" + getActive() + "\","
            + "\"affects_target\":\"" + getAffects_target() + "\","
            + "\"affects_player\":\"" + getAffects_player() + "\","
            + "\"solution\":\"" + getSolution() + "\","
            + "\"value\":\"" + getValue() + "\","
            + "\"description\":\"" + getDescription() + "\","
            + "\"effects\":\"" + getEffects() + "\","
            + "\"target\":\"" + getTarget() + "\","
            + "\"picture\":\"" + getPictureName() + "\""
            + " }";
  }
}
