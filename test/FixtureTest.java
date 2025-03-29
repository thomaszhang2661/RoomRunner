import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import enginedriver.Fixture;

public class FixtureTest {

  @Test
  public void testFixtureCreation() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000, null);
    assertEquals(1, fixture.getId());
    assertEquals("Desk", fixture.getName());
    assertEquals("An old wooden desk with a mess of papers.", fixture.getDescription());
  }

  @Test
  public void testFixtureWeight() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000, null);
    assertEquals(1000, fixture.getWeight());
  }

  @Test
  public void testFixtureToString() {
    Fixture fixture = new Fixture(1, "Desk", "An old wooden desk with a mess of papers.", 1000, null);
    String expected = "{ \"name\":\"Desk\",\"weight\":\"1000\",\"puzzle\":\"null\",\"state\":\"null\",\"description\":\"An old wooden desk with a mess of papers.\",\"picture\":\"null\" }";
    assertEquals(expected, fixture.toString());
  }
}