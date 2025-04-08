package enginedriver;

import enginedriver.problems.Puzzle;

/**
 * Class for fixtures in the game.
 * Fixtures are objects that can be used to solve puzzles.
 */
public class Fixture extends IdentifiableEntity implements IWeightable {
  private final int weight;
  private Puzzle<?> puzzle;
  private int states;

  /**
   * Simple constructor for fixture without weight.

   * @param name the name of the fixture
   * @param description the description of the fixture
   */
  public Fixture(String name, String description) {
    super(name, description);
    this.weight = 1000; //TODO move to config file
    this.states = -1;
  }

  /**
   * Constructor for a fixture with weight.

   * @param name the name of the fixture
   * @param description the description of the fixture
   * @param weight the weight of the fixture
   */
  public Fixture(String name, String description, int weight) {
    super(name, description);
    this.weight = weight;
    this.states = -1;
  }

  /**
   * Constructor for a fixture with weight, puzzle, states and pictureName.

   * @param name the name of the fixture
   * @param description the description of the fixture
   * @param weight the weight of the fixture
   * @param puzzle the puzzle of the fixture
   * @param states the states of the fixture
   * @param pictureName the picture name of the fixture
   */
  public Fixture(String name, String description, int weight,
                 Puzzle<?> puzzle, int states,
                 String pictureName) {
    super(name, description, pictureName);
    this.weight = weight;
    this.puzzle = puzzle;
    this.states = states;
  }

  /**
   * Constructor for a fixture with weight, puzzle and pictureName.

   * @param name the name of the fixture
   * @param description the description of the fixture
   * @param weight the weight of the fixture
   * @param puzzle the puzzle of the fixture
   * @param pictureName the picture name of the fixture
   */
  public Fixture(String name, String description, int weight,
                 Puzzle<?> puzzle,
                 String pictureName) {
    super(name, description, pictureName);
    this.weight = weight;
    this.puzzle = puzzle;
    this.states = -1;
  }

  /**
   * getter of states.

   * @return int states
   */
  public int getStates() {
    return states;
  }

  /**
   * setter of states.

   * @param input the states of the fixture
   */
  public void setStates(int input) {
    this.states = input;
  }

  /**
   * getter of puzzle.

   * @return Puzzle<?> puzzle
   */
  public Puzzle<?> getPuzzle() {
    return puzzle;
  }

  /**
   * getter of weight.

   * @return int weight
   */
  @Override
  public int getWeight() {
    return weight;
  }
}
