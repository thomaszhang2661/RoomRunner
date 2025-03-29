import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import enginedriver.Item;

/**
 * Unit Test for Item class.
 */
public class ItemTest {

  /**
   * Test the constructor of Item.
   */
  @Test
  void testConstructor() {
    // Valid construction
    Item item = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals("A", item.getName());
    assertEquals("desc", item.getDescription());
    assertEquals(5, item.getUseMax());
    assertEquals(2, item.getRemainingUses());
    assertEquals(100, item.getValue());
    assertEquals(10, item.getWeight());
    assertEquals("when used", item.getWhenUsed());

    // invalid remaining uses
    try {
      new Item("B", "desc", 5, -1,
              100, 10, "when used");
    } catch (IllegalArgumentException e) {
      assertEquals("Remaining uses of an item cannot be less than 0", e.getMessage());
    }

    // invalid max uses
    try {
      new Item("C", "desc", -5, 1,
              100, 10, "when used");
    } catch (IllegalArgumentException e) {
      assertEquals("Max uses of an item cannot be less than 0", e.getMessage());
    }
  }

  /**
   * Test getUseRemain method.
   */
  @Test
  void testGetUseRemain() {
    Item item1 = new Item("A", "desc", 10, 5,
            100, 10, "when used");
    assertEquals(5, item1.getRemainingUses());

    Item item2 = new Item("B", "desc", 10, 0,
            50, 5, "used");
    assertEquals(0, item2.getRemainingUses());

    Item item3 = new Item("C", "desc", 10, 10,
            20, 2, "used soon");
    assertEquals(10, item3.getRemainingUses());
  }

  /**
   * Test setUseRemain method.
   */
  @Test
  void testSetUseRemain() {
    Item item = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    item.setRemainingUses(3);
    assertEquals(3, item.getRemainingUses());

    // Test invalid remaining uses
    try {
      item.setRemainingUses(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Remaining uses of an item cannot be less than 0", e.getMessage());
    }
  }
  /**
   * Test getUseMax method.
   */
  @Test
  void testGetUseMax() {
    Item item1 = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals(5, item1.getUseMax());

    Item item2 = new Item("B", "desc", 0, 0,
            50, 5, "used");
    assertEquals(0, item2.getUseMax());

    Item item3 = new Item("C", "desc", 10, 10,
            20, 2, "used soon");
    assertEquals(10, item3.getUseMax());
  }

  /**
   * Test getValue method.
   */
  @Test
  void testGetValue() {
    Item item1 = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals(100, item1.getValue());

    Item item2 = new Item("B", "desc", 5, 2,
            0, 10, "used");
    assertEquals(0, item2.getValue());
  }

  /**
   * Test getWeight method.
   */
  @Test
  void testGetWeight() {
    Item item1 = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals(10, item1.getWeight());

    Item item2 = new Item("B", "desc", 5, 2,
            50, 0, "used");
    assertEquals(0, item2.getWeight());
  }

  /**
   * Test getWhenUsed method.
   */
  @Test
  void testGetWhenUsed() {
    Item item1 = new Item("A", "desc", 5, 2,
            100, 10, "after meal");
    assertEquals("after meal", item1.getWhenUsed());

    Item item2 = new Item("B", "desc", 5, 2,
            50, 5, "");
    assertEquals("", item2.getWhenUsed());
  }

  /**
   * Test getID method.
   */
  @Test
  void testGetId() {
    // default id = -1
    Item item = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals(-1, item.getId());
  }

  /**
   * Test getName method.
   */
  @Test
  void testGetName() {
    Item item = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertEquals("A", item.getName());
  }

  /**
   * Test getDescription method.
   */
  @Test
  void testGetDescription() {
    Item item = new Item("A", "some description", 5, 2,
            100, 10, "when used");
    assertEquals("some description", item.getDescription());
  }

  /**
   * Test getPicture method.
   */
  @Test
  void testGetPicture() {
    // Since getPicture() always returns null in this implementation:
    Item item = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    assertNull(item.getPicture());
  }

  /**
   * Test equals method.
   */
  @Test
  void testEquals() {
    Item item1 = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    Item item2 = new Item("A", "desc", 5, 2,
            100, 10, "when used");
    Item item3 = new Item("B", "desc", 5, 2,
            100, 10, "when used");

    assertTrue(item1.equals(item2));
    assertFalse(item1.equals(item3));
    assertFalse(item1.equals(null));
    assertFalse(item1.equals("Not an Item"));
    assertEquals(item1.hashCode(), item2.hashCode());
  }

  /**
   * Test toString method.
   */
  @Test
  void testToString() {
    // Using assertEquals for an exact match to the current toString() format
    Item item = new Item("A", "descA", 5, 3,
            100, 2, "Now");

    // Notice getWhenUsed() always returns "", so the when_used field should be empty in the result.
    String expected =
            "{ \"name\":\"A\",\"weight\":\"2\",\"max_uses\":\"5\",\"uses_remaining"
                    + "\":\"3\",\"value\":\"100\","
                    + "\"when_used\":\"Now\",\"description\":\"descA\",\"picture\":\"null\" }";

    assertEquals(expected, item.toString());
  }
}
