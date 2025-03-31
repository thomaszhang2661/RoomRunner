package enginedriver.problems.validator;

/**
 * Interface for validating solutions to problems.
 *
 * @param <T> the type of the problem solution
 */
public interface SolutionValidator<T> {

  /**
   * Validates the solution to a problem.

   * @param problemSolution the solution to the problem
   * @param userInput the user input to validate
   * @return true if the solution is valid, false otherwise
   */
  boolean validate(T problemSolution, T userInput);
}
