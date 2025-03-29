package enginedriver;

import java.awt.*;

abstract public class Problem<T> extends IdentifiableEntity
        implements IProblem<T>, IValuable {
  private Boolean active;
  private Boolean affects_target;
  private Boolean affects_player;
  private T solution;
  private int value;
  private String effects;
  private String target;
  private String pictureName;



  public Problem(String name, String description,Boolean active,
                 Boolean affects_target, Boolean affects_player,
                 T solution, int value, String effects,
                 String target, String pictureName) {
    super(name, description);
    this.active = active;
    this.affects_target = affects_target;
    this.affects_player = affects_player;
    this.value = value;
    this.effects = effects;
    this.target = target;
    this.pictureName = pictureName;
    this.solution = solution;

  }


  public boolean getAffect_player() {
    return affects_player;
  }

  public Boolean getAffects_target() {
    return affects_target;
  }

  public String getTarget() {
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

  public boolean isSolved() {
    return !active;
  }

  @Override
  public boolean solve(T input) {
    // if input is String
    if (input instanceof String) {
      if (input.equals(solution)) {
        active = false;
        return true;
      }
    }
    // if input is Item
    if (input instanceof Item) {
      if (((Item) input).getName().equals(solution)) {
        active = false;
        return true;
      }
    }
    return false;
  }



  @Override
  public int getValue() {
    return value;
  }

  @Override
  public int getId() {
    return super.getId();
  }

  @Override
  public String getName() {
    return super.getName();
  }

  @Override
  public String getDescription() {
    return super.getDescription();
  }

  @Override
  public Image getPicture() {
    return null;
  }

  @Override
  public String getEffects() {
    return effects;
  }
}
