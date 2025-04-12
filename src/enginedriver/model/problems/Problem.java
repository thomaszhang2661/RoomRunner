package enginedriver.model.problems;

import enginedriver.model.IValuable;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.problems.validator.SolutionValidator;




/**
 * Class for problems in the game.
 * Problems are puzzles or riddles that the player must solve.

 * @param <T> the type of the solution
 */
public abstract class Problem<T> extends IdentifiableEntity
        implements  IValuable, IProblem<T> {
  private boolean active;
  private final boolean affectsTarget;
  private final boolean affectsPlayer;
  private final T solution;
  private final SolutionValidator<T> validator;
  private final int value;
  private final String effects;
  private final String  target;



  /**
   * Constructor for a problem.

   * @param name the name of the problem
   * @param description the description of the problem
   * @param active the active status of the problem
   * @param affectsTarget the affects target status of the problem
   * @param affectsPlayer the affects player status of the problem
   * @param solution the solution of the problem
   * @param value the value of the problem
   * @param effects the effects of the problem
   * @param target the target of the problem affecting
   * @param pictureName the picture name of the problem
   * @param validator the solution validator of the problem
   */
  public Problem(String name, String description, Boolean active,
                 Boolean affectsTarget, Boolean affectsPlayer,
                 T solution, int value, String effects,
                 String target, String pictureName, SolutionValidator<T> validator) {
    super(name, description, pictureName);
    this.active = active;
    this.affectsTarget = affectsTarget;
    this.affectsPlayer = affectsPlayer;
    this.value = value;
    this.effects = effects;
    this.target =  target;
    this.solution = solution;
    this.validator = validator;
  }

  /**
   * Gets affectsPlayer status of the problem.

   * @return the affectsPlayer status of the problem
   */
  @Override
  public boolean getAffectsPlayer() {
    return affectsPlayer;
  }

  /**
   * Gets affectsTarget status of the problem.

   * @return the affectsTarget status of the problem
   */
  @Override
  public boolean  getAffectsTarget() {
    return affectsTarget;
  }

  /**
   * Gets the target of the problem.

   * @return the target of the problem
   */
  @Override
  public String getTarget() {
    // target to map
    return target;
  }

  /**
   * Gets the active status of the problem.

   * @return the active status of the problem
   */
  @Override
  public boolean getActive() {
    return active;
  }

  /**
   * Sets the active status of the problem.

   * @param active the active status of the problem
   */
  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Gets the solution of the problem.

   * @return the solution of the problem
   */
  @Override
  public T getSolution() {
    return solution;
  }

  /**
   * Solves the problem.

   * @param input the string or Item to solve the problem.
   * @return 1 if the problem is solved, 0 if the problem is not active,
   *      2 if the problem is not solved.
   */
  @Override
  public int solve(T input) {
    // if the problem is not active, no need to solve.
    if (!active) {
      return 0;
    }
    // if the solution is correct, the problem is solved.
    if (validator.validate(solution, input)) {
      //active = false; moved to controller handleProblemSolved
      return 1;
    }
    // if the solution is incorrect, the problem is not solved.
    return 2;
  }

  /**
   * Gets the value of the problem.

   * @return the value of the problem
   */
  @Override
  public int getValue() {
    return value;
  }

  /**
   * Gets the effects of the problem.

   * @return the effects of the problem
   */
  @Override
   public String getEffects() {
    return effects;
  }
}
