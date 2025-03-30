package enginedriver.problems.validator;

public interface SolutionValidator<T> {
  boolean validate(T problemSolution, T userInput);
}
