package enginedriver.problems;

import enginedriver.Item;
import enginedriver.problems.validator.SolutionValidator;

/**
 * Class for puzzles in the game.
 */
public class Puzzle<T> extends Problem<T> {

  /**
   * Constructor for Puzzle.

   * @param name the name of the puzzle
   * @param description the description of the puzzle
   * @param active the active status of the puzzle
   * @param affectsTarget the affects target status of the puzzle
   * @param affectsPlayer the affects player status of the puzzle
   * @param solution the solution of the puzzle
   * @param value the value of the puzzle
   * @param effects the effects of the puzzle
   * @param target the target of the puzzle affecting
   * @param pictureName the picture name of the puzzle
   * @param validator the solution validator of the puzzle
   */
  public Puzzle(String name,
                String description,
                Boolean active,
                Boolean affectsTarget,
                Boolean affectsPlayer,
                T solution,
                int value,
                String effects,
                String target, String pictureName, SolutionValidator<T> validator) {

    super(name, description, active, affectsTarget,
            affectsPlayer, solution, value, effects, target,
            pictureName, validator);
  }

  /**
   * toString method for Puzzle.

   * @return the string representation of the puzzle
   */
  @Override
  public String toString() {
    String solutionStr;
    Object solution = getSolution();
    // is an Item
    if (solution instanceof Item) {
      solutionStr = ((Item) solution).getName();
    // is a String or null
    } else {
      solutionStr = solution.toString();
    }

    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"active\":\"" + getActive() + "\","
            + "\"affects_target\":\"" + getAffectsTarget() + "\","
            + "\"affects_player\":\"" + getAffectsPlayer() + "\","
            + "\"solution\":\"" + solutionStr + "\","
            + "\"value\":\"" + getValue() + "\","
            + "\"description\":\"" + getDescription()
                                       .replace("\"", "\\\"")
                                       .replace("\n", "\\n") + "\","
            + "\"effects\":\"" + getEffects()
                                   .replace("\"", "\\\"")
                                   .replace("\n", "\\n") + "\","
            + "\"target\":\"" + getTarget() + "\","
            + "\"picture\":\"" + getPictureName() + "\""
            + " }";
  }
}
