package enginedriver;

/**
  * Interface for problems that can be solved.
 */
public interface IProblem extends IValuable {

  /**
   * check if the problem is solved.
   * @return true if solved, false otherwise
   */
  boolean isSolved();

  /**
   * Solve the problem by using a String.
   * @param input the string to solve the problem
   */
  void solve(String input);

  /**
   * Solve the problem by using an item.
   * @param item the item to solve the problem
   */
  void solve(IItem item);

  /**
   * apply effect of the problem, diminish value of health.
   * and print the effect of the problem
   */
  void get_effect();

}
