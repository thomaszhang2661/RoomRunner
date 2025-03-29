package enginedriver;

import java.awt.*;

public class Fixture extends IdentifiableEntity implements IWeightable {
  private int weight;
  private Puzzle puzzle;
  private Object states;
  private Image picture;

  /**
   * Constructor for an identifiable entity.
   * Room can use field of id.
   *
   * @param id
   * @param name
   * @param description
   */
  public Fixture(int id, String name, String description, int weight, Puzzle puzzle) {
    super(id, name, description);
    this.weight = weight;
    this.puzzle = puzzle;
    this.picture = null;
    this.states = null;
  }

  public Object getStates() {
    return null;
  }

  public Puzzle getPuzzle() {
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
