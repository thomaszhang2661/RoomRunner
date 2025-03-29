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
  public Fixture(int id, String name, String description) {
    super(id, name, description);
  }

  public Fixture(String name, String description, int weight, Puzzle puzzle, Object states, String picture) {
    super(name, description);
    this.weight = weight;
    this.puzzle = puzzle;
    this.states = states;
    this.picture = Toolkit.getDefaultToolkit().getImage(picture);
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
