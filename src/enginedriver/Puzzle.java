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
    return !active;
  }

  // ?
  @Override
  public boolean solve(String solution) {

  }

  @Override
  public boolean solve(IItem item) {

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
}
