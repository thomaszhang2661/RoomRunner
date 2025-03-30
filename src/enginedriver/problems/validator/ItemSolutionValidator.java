package enginedriver.problems.validator;

import enginedriver.Item;

public class ItemSolutionValidator implements SolutionValidator<Item> {
  @Override
  public boolean validate(Item problemSolution, Item userInput) {
    return problemSolution.equals(userInput) && userInput.getRemainingUses() > 0;
  }
}

