package enginedriver;

import java.awt.*;

public class Fixture implements IWeightable{
  private final String ID;
  private final String name;
  private final String description;
  private final int weight;
  private Puzzle puzzle;
  private Object states;
  private final Image picture;

  public Fixture(String ID, String name, String description,
                 int weight, Image picture) {
    this.ID = ID;
    this.name = name;
    this.description = description;
    this.weight = weight;
    this.puzzle = null;
    this.states = null;
    this.picture = picture;
  }

  public Puzzle getPuzzle() {
    return puzzle;
  }

  @Override
  public int getWeight() {
    return weight;
  }

  @Override
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

  public Object getStates() {
    return states;
  }

}
