package enginedriver.model.problems.validator;

/**
 * Class for validating string solutions to problems.
 *
 * @param <T> the type of the problem solution
 */
public class StringSolutionValidator implements SolutionValidator<String> {

  /**
   * Validates the solution to a problem.
   *
   * @param problemSolution the solution to the problem
   * @param userInput the user input to validate
   * @return true if the solution is valid, false otherwise
   */
  @Override
  public boolean validate(String problemSolution, String userInput) {
    return problemSolution.equals(userInput);
  }
}

