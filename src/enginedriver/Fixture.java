package enginedriver;

import java.awt.*;

public class Fixture implements IWeightable{
  private String ID;
  private String name;
  private String description;
  private int weight;
  private Puzzle puzzle;
  // ?
  private boolean state;
  private Image picture;

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
  public String getId() {
    return "";
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public Image getPicture() {
    return null;
  }
}
