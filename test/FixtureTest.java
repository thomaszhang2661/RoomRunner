import org.junit.jupiter.api.Test;

import enginedriver.Fixture;
import enginedriver.IdentifiableEntity;
import enginedriver.problems.Puzzle;
import enginedriver.problems.validator.StringSolutionValidator;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FixtureTest {

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object with weight.
   */
  @Test
  public void testFixtureCreation1() {
    IdentifiableEntity fixture = new Fixture("Desk", "An old wooden desk with a mess of papers.", 1000);
    // assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
  }

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object without weight.
   */
  @Test
  public void testFixtureCreation2() {
    Fixture fixture = new Fixture("Desk", "An old wooden desk with a mess of papers.");
    // assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
    assertEquals(1000, fixture.getWeight());
  }

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object with weight, puzzle, states and pictureName.
   */
  @Test
  public void testFixtureCreation3() {
    Puzzle<String> puzzle = new Puzzle<>("PuzzleName", "A puzzle description",
            true, false, false, "Solution",
            50, "puzzleEffects", "puzzleTarget", "puzzlePic"
            , new StringSolutionValidator());
    Fixture fixture = new Fixture("Desk", "An old wooden desk with a mess of papers."
            , 1000, puzzle, 0, "desk.png");
    // assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
    assertEquals(1000, fixture.getWeight());
    assertEquals("desk.png", fixture.getPictureName());
    assertTrue(puzzle.equals(fixture.getPuzzle()));
    assertEquals(0, fixture.getStates());
  }

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object with weight, puzzle and pictureName.
   */
  @Test
  public void testFixtureCreation4() {
    Puzzle<String> puzzle = new Puzzle<>("PuzzleName", "A puzzle description",
            true, false, false, "Solution",
            50, "puzzleEffects", "puzzleTarget", "puzzlePic"
            , new StringSolutionValidator());
    Fixture fixture = new Fixture("Desk", "An old wooden desk with a mess of papers."
            , 1000, puzzle, "desk.png");
    // assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
    assertEquals(1000, fixture.getWeight());
    assertEquals("desk.png", fixture.getPictureName());
    assertTrue(puzzle.equals(fixture.getPuzzle()));
    assertEquals(-1, fixture.getStates());
    fixture.setStates(0);
    assertEquals(0, fixture.getStates());
  }

  @Test
  public void testFixtureWeight() {
    Fixture fixture = new Fixture("Desk", "An old wooden desk with a mess of papers.", 1000);
    assertEquals(1000, fixture.getWeight());
  }

  @Test
  public void testFixtureToString() {
    Fixture fixture = new Fixture("Desk", "An old wooden desk with a mess of papers.", 1000);
    String expected = "{ \"name\":\"Desk\",\"weight\":\"1000\",\"puzzle\":\"null\",\"states\":\"-1\",\"description\":\"An old wooden desk with a mess of papers.\",\"picture\":\"null\" }";
    assertEquals(expected, fixture.toString());
  }
}