package enginedriver.problems.validator;


public class StringSolutionValidator implements SolutionValidator<String> {
  @Override
  public boolean validate(String problemSolution, String userInput) {
    return problemSolution.equals(userInput);
  }
}

