package enginedriver.problems.validator;

import enginedriver.Item;

/**
 * Class for validating item solutions.
 * This class implements the SolutionValidator interface
 * and provides a method to validate item solutions.
 */
public class ItemSolutionValidator implements SolutionValidator<Item> {

  /**
   * Validates the item solution.
   *
   * @param problemSolution the correct item solution
   * @param userInput the user's input item
   * @return true if the solution is valid, false otherwise
   */
  @Override
  public boolean validate(Item problemSolution, Item userInput) {
    return problemSolution.equals(userInput) && userInput.getRemainingUses() > 0;
  }
}

