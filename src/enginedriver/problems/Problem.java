package enginedriver.problems;

import enginedriver.IValuable;
import enginedriver.IdentifiableEntity;
import enginedriver.problems.validator.SolutionValidator;




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

    // parse target
    //    String[] parts = target.split(":", 2);
    //    int roomNumber = Integer.parseInt(parts[0].trim());
    //    String roomName = parts[1].trim();
    //    this.target = Map.of(roomNumber, roomName);
    this.target =  target;
    this.solution = solution;
    this.validator = validator;
  }

  @Override
  public boolean getAffectsPlayer() {
    return affectsPlayer;
  }

  @Override
  public boolean  getAffectsTarget() {
    return affectsTarget;
  }

  @Override
  public String getTarget() {
    // target to map
    return target;
  }

  @Override
  public boolean getActive() {
    return active;
  }

  @Override
  public T getSolution() {
    return solution;
  }


  @Override
  public boolean solve(T input) {
    if (validator.validate(solution, input)) {
      active = false;
      return true;
    }
    return false;
  }


  @Override
  public int getValue() {
    return value;
  }

  @Override
   public String getEffects() {
    return effects;
  }
}
