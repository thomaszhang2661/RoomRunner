package enginedriver;

import java.awt.*;

/**
 * Class for fixtures in the game.
 * Fixtures are objects that can be used to solve puzzles.
 */
public class Fixture extends IdentifiableEntity implements IWeightable {
  private final int weight;
  private Puzzle<?> puzzle;
  private Object states;
  private String pictureName;
  //TODO: accomposition for weightable
  /**
   * Simple constructor for fixture without weight
   */
  public Fixture(String name, String description) {
    super(name, description);
    this.weight = 1000;//TODO move to config file
  }

  /**
   * Constructor for a fixture with weight.
   */
  public Fixture(String name, String description, int weight) {
    super(name, description);
    this.weight = weight;
  }

  public Fixture(String name, String description, int weight, Puzzle puzzle, Object states,
                 String pictureName) {
    super(name, description);
    this.weight = weight;
    this.puzzle = puzzle;
    this.states = states;
    this.pictureName = pictureName;
  }

  public Object getStates() {
    return null;
  }

  public Puzzle<?> getPuzzle() {
    return puzzle;
  }

  @Override
  public int getWeight() {
    return weight;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"weight\":\"" + getWeight() + "\"," +
            "\"puzzle\":\"" + getPuzzle() + "\"," +
            "\"state\":\"" + getStates() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }
}
