import org.junit.jupiter.api.Test;

import enginedriver.Fixture;
import enginedriver.IdentifiableEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FixtureTest {

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object with weight.
   */
  @Test
  public void testFixtureCreation1() {
    IdentifiableEntity fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000);
    assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
  }

  /**
   * Test for the Fixture class.
   * This test checks the creation of a Fixture object without weight.
   */
  @Test
  public void testFixtureCreation2() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.");
    assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
    assertEquals(1000, fixture.getWeight());
  }

  @Test
  public void testFixtureWeight() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000);
    assertEquals(1000, fixture.getWeight());
  }

  @Test
  public void testFixtureToString() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000);
    String expected = "{ \"name\":\"Desk\",\"weight\":\"1000\",\"puzzle\":\"null\",\"state\":\"null\",\"description\":\"An old wooden desk with a mess of papers.\",\"picture\":\"null\" }";
    assertEquals(expected, fixture.toString());
  }
}