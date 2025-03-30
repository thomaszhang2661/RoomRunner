package enginedriver.problems;

import java.util.Map;

import enginedriver.IValuable;

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
   *  change probelm status to active or inactive.

   */
  void  setActive(boolean active);

  /**
   * Returns the target of the problem.

   * @return String for target
   */
  T getSolution();

  /**
   * Solve the problem by using a String.

   * @param input the string  or Item to solve the problem.
   * @return 0 if the problem do not need to solve, active is false.
   *                1 if the problem is solved, solve success.
   *                2 if the problem is not solved, solve failed.
   */
  int solve(T input);


  /**
   * Returns the effect of the problem.

   * @return String for effect
   */
  String getEffects();

  /**
   * Returns AffectsPlayer.
   */
  boolean getAffectsPlayer();

  /**
   * Returns AffectsPlayer.
   */
  boolean getAffectsTarget();

  /**
   * Returns the target of the problem.
   */
  String getTarget();

}