package enginedriver;

import java.awt.*;

public class Fixture extends IdentifiableEntity implements IWeightable {
  private int weight;
  private Puzzle puzzle;
  // ?
  private boolean state;
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

  public boolean isState() {
    return state;
  }

  public Puzzle getPuzzle() {
    return puzzle;
  }

  @Override
  public int getWeight() {
    return 0;
  }

  @Override
  public Image getPicture() {
    return null;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"weight\":\"" + getWeight() + "\"," +
            "\"puzzle\":\"" + getPuzzle() + "\"," +
            "\"state\":\"" + isState() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }
}
