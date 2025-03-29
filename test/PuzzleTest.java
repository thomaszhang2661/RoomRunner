import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import enginedriver.Puzzle;

public class PuzzleTest {

  @Test
  public void testPuzzleCreation() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness", "It's dark!", true, true, true, "Lamp", 150, "You cannot see!", "6:Kitchen", "darkness.png");
    assertEquals("Darkness", puzzle.getName());
    assertEquals("It's dark!", puzzle.getDescription());
    assertTrue(puzzle.getActive());
    assertTrue(puzzle.getAffects_target());
    assertTrue(puzzle.getAffect_player());
    assertEquals("Lamp", puzzle.getSolution());
    assertEquals(150, puzzle.getValue());
    assertEquals("You cannot see!", puzzle.getEffects());
    assertEquals("6:Kitchen", puzzle.getTarget());
  }

  @Test
  public void testPuzzleSolve() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness", "It's dark!", true, true, true, "Lamp", 150, "You cannot see!", "6:Kitchen", "darkness.png");
    assertTrue(puzzle.solve("Lamp"));
    assertFalse(puzzle.getActive());
  }

  @Test
  public void testPuzzleToString() {
    Puzzle<String> puzzle = new Puzzle<>("Darkness", "It's dark!", true, true, true, "Lamp", 150, "You cannot see!", "6:Kitchen", "darkness.png");
    String expected = "{ \"name\":\"Darkness\",\"active\":\"false\",\"affects_target\":\"true\",\"affects_player\":\"true\",\"solution\":\"Lamp\",\"value\":\"150\",\"description\":\"It's dark!\",\"effects\":\"You cannot see!\",\"target\":\"6:Kitchen\",\"picture\":\"null\" }";
    assertEquals(expected, puzzle.toString());
  }
}