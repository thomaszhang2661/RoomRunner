package enginedriver;

/**
  * Interface for problems that can be solved.
 */
public interface IProblem<T> extends IValuable {

  /**
   * check if the problem is solved.
   * @return true if solved, false otherwise
   */
  boolean getActive();

  /**
   * Returns the target of the problem.
   * @return String for target
   */
  T getSolution();

  /**
   * Solve the problem by using a String.
   * @param input the string to solve the problem
   */
  boolean solve(T input);




/**
 * Returns the effect of the problem.
 * @return String for effect
 */
String getEffects();

}