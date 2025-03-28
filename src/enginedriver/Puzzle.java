package enginedriver;

import java.awt.Image;

public class Puzzle implements IProblem{
  private String ID;
  private String name;
  private String description;
  private boolean active = true;
  private boolean affectTarget;
  private boolean affectPlayer;
  private String solution;
  private int value;
  // effects;
  private Room target;
  private Image picture;

  public boolean isAffectTarget() {
    return affectTarget;
  }

  public boolean isAffectPlayer() {
    return affectPlayer;
  }

  public String getSolution() {
    return solution;
  }

  public Room getTarget() {
    return target;
  }

  @Override
  public boolean isSolved() {
    return active;
  }

  // ?
  @Override
  public void solve(String solution) {

  }

  @Override
  public void solve(IItem item) {

  }

  // Return effects, whatever that type is.
  @Override
  public void get_effect() {

  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  // ID用什么？string还是long？
  public String getId() {
    return ID;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Image getPicture() {
    return picture;
  }

  @Override
  public String toString() {
    return "{ " +
            "\"name\":\"" + getName() + "\"," +
            "\"active\":\"" + isSolved() + "\"," +
            "\"affects_target\":\"" + isAffectTarget() + "\"," +
            "\"affects_player\":\"" + isAffectPlayer() + "\"," +
            "\"solution\":\"" + getSolution() + "\"," +
            "\"value\":\"" + getValue() + "\"," +
            "\"description\":\"" + getDescription() + "\"," +
            "\"effects\":\"" + getEffects() + "\"," +
            "\"target\":\"" + getTarget() + "\"," +
            "\"picture\":\"" + getPicture() + "\"" +
            " }";
  }
}
