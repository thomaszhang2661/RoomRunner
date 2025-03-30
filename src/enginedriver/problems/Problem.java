package enginedriver.problems;

import java.awt.Image;
import java.util.Map;


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
  private Boolean active;
  private final Boolean affects_target;
  private final Boolean affects_player;
  private final T solution;
  private SolutionValidator<T> validator;
  private final int value;
  private final String effects;
  private Map<Integer,String >  target;



  /**
   * Constructor for a problem.
   */
  public Problem(String name, String description, Boolean active,
                 Boolean affects_target, Boolean affects_player,
                 T solution, int value, String effects,
                 String target, String pictureName) {
    super(name, description, pictureName);
    this.active = active;
    this.affects_target = affects_target;
    this.affects_player = affects_player;
    this.value = value;
    this.effects = effects;

    // parse target
    String[] parts = target.split(":", 2);
    int roomNumber = Integer.parseInt(parts[0].trim());
    String roomName = parts[1].trim();
    this.target = Map.of(roomNumber, roomName);

    this.solution = solution;

    //get the type of solution
    Class<?> solutionClass = solution.getClass();

    this.validator = new SolutionValidator<>();

  }


  public boolean getAffects_player() {
    return affects_player;
  }

  protected boolean getAffects_target() {
    return affects_target;
  }

  protected Map<Integer,String> getTarget() {
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
