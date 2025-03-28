package enginedriver;

import java.awt.Image;

/**
 * Class for puzzles in the game.
 */
public class Puzzle<T> extends Problem<T>{

  /**
   * Constructor for Puzzle.
   */
  public Puzzle(String name,
                String description,
                Boolean active,
                Boolean affects_target,
                Boolean affects_player,
                T solution,
                int value,
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







}
