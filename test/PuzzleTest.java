import org.junit.jupiter.api.Test;

import enginedriver.Item;
import enginedriver.Puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit Test for Puzzle class.
 */
public class PuzzleTest {

  /**
   * Test the constructor of Puzzle.
   */
  @Test
  public void testPuzzleCreation() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true,
            true, true,
            "Lamp", 150, "You cannot see!",
            "6:Kitchen", "darkness.png");
    assertEquals("Darkness", puzzle.getName());
    assertEquals("It's dark!", puzzle.getDescription());
    assertTrue(puzzle.getActive());
    assertTrue(puzzle.getAffects_target());
    assertTrue(puzzle.getAffects_player());
    assertEquals("Lamp", puzzle.getSolution());
    assertEquals(150, puzzle.getValue());
    assertEquals("You cannot see!", puzzle.getEffects());
    assertEquals("6:Kitchen", puzzle.getTarget());
  }

  /**
   * Test the getID method of Puzzle.
   */
  @Test
  public void testPuzzleGetID() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true,
            true, true,
            "Lamp", 150, "You cannot see!",
            "6:Kitchen", "darkness.png");
    assertEquals(-1, puzzle.getId());
  }

  /**
   * Test solve method of Puzzle with solution of type String.
   */
  @Test
  public void testPuzzleSolve() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true, true,
            true, "Lamp", 150, "You cannot see!",
            "6:Kitchen", "darkness.png");
    assertTrue(puzzle.solve("Lamp"));
    assertFalse(puzzle.getActive());
  }

  /**
   * Test solve method of Puzzle with solution of type Item.
   */
  @Test
  public void testPuzzleSolveItem() {
    // create item
    enginedriver.Item lamp = new enginedriver.Item("Lamp", "A lamp", 1, 1, 1, 1, "Bright light");

    Puzzle<Item> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true, true,
            true, lamp, 150, "You cannot see!",
            "6:Kitchen", "darkness.png");

    assertTrue(puzzle.solve(lamp));
    assertFalse(puzzle.getActive());
    assertTrue(puzzle.isSolved());
  }
  /**
   * Test solve method of Puzzle with wrong input.
   */
  @Test
  public void testPuzzleSolveWrongInput() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true, true,
            true, "Lamp", 150, "You cannot see!",
            "6:Kitchen", "darkness.png");
    assertFalse(puzzle.solve("WrongInput"));
    assertTrue(puzzle.getActive());
    assertFalse(puzzle.isSolved());
  }

  /**
   * Test the toString method of Puzzle.
   */
  @Test
  public void testPuzzleToString() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness",
            "It's dark!", true, true,
            true, "Lamp", 150,
            "You cannot see!", "6:Kitchen", "null");
    String expected = "{ \"name\":\"Darkness\",\"active\":\"true\",\"affects_target\":\"true\"," +
            "\"affects_player\":\"true\",\"solution\":\"Lamp\",\"value\":\"150\"," +
            "\"description\":\"It's dark!\",\"effects\":\"You cannot see!\"," +
            "\"target\":\"6:Kitchen\",\"picture\":\"null\" }";

    assertEquals(expected, puzzle.toString());
  }
}