import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import enginedriver.Fixture;
import enginedriver.IdentifiableEntity;
import enginedriver.Item;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;
import enginedriver.Room;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;



/**
 * Tests for the Room class.
 * One test method per public method in Room.
 */
public class RoomTest {

  /**
   * Test constructor:
   * Room(int id, String name, String description, Map<String, Integer> exits, String problem)
   */
  @Test
  void testConstructorOne() {
    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 2);
    exits.put("S", -3);

    Puzzle<String> puzzle = new Puzzle<>(
            "PuzzleName", "A puzzle description",
            true, false, false, "Solution",
            50, "puzzleEffects", "puzzleTarget", "puzzlePic"
    );

    // Normal usage
    Room room = new Room(1, "A", "descA", exits, puzzle);
    assertEquals(1, room.getId());
    assertEquals("A", room.getName());
    assertEquals("descA", room.getDescription());
    assertEquals(exits, room.getExits());
    // The constructor sets IProblem to null in this signature
    assertEquals(room.getProblem(), puzzle);
  }

  /**
   * Test constructor:
   * Room(int id, String name, String description, Map<String, Integer> exits,
   *      Map<String, IdentifiableEntity> entityNames)
   */
  @Test
  void testConstructorTwo() {
    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 3);
    exits.put("E", 4);

    // Prepare an entity map
    Map<String, IdentifiableEntity> entities = new HashMap<>();
    entities.put("itemA", new Item("itemA", "descA", 1, 1,
            10, 5, "used"));
    entities.put("fixtureB", new Fixture("fixtureB", "fixtureDesc", 1000));

    Room<?> room = new Room(10, "C", "descC", exits, entities);
    assertEquals(10, room.getId());
    assertEquals("C", room.getName());
    assertEquals("descC", room.getDescription());
    assertEquals(exits, room.getExits());
    // The constructor sets problem to null
    assertNull(room.getProblem());

    // Confirm entities are there
    assertTrue(room.hasEntity("itemA"));
    assertTrue(room.hasEntity("fixtureB"));
  }

  /**
   * Test constructor:
   * Room(int id, String name, String description, Map<String, Integer> exits,
   *      Map<String, IdentifiableEntity> entityNames, IProblem problem)
   */
  @Test
  void testConstructorThree() {
    Map<String, Integer> exits = new HashMap<>();
    exits.put("S", 5);
    exits.put("W", 6);

    // Initialize an entity map
    Map<String, IdentifiableEntity> entities = new HashMap<>();
    entities.put("itemB", new Item("B", "descB", 2, 2, 20, 10, "used"));
    entities.put("fixtureY", new Fixture("Y", "fixtureDescY", 1000));

    // Create Monster
    Monster<String> monster = new Monster<>(
            "MonsterName", "A monster description",
            true, true, true, "MonsterSolution",
            100, 10, "monsterEffects", "monsterTarget",
            "monsterPic", "BiteAttack"
    );

    // Construct with a puzzle
    Room roomPuzzle = new Room(20, "D", "descD", exits, entities, monster);
    assertEquals(20, roomPuzzle.getId());
    assertEquals("D", roomPuzzle.getName());
    assertEquals("descD", roomPuzzle.getDescription());
    assertEquals(exits, roomPuzzle.getExits());
    assertEquals(monster, roomPuzzle.getProblem());
  }

  /**
   * Test getExits().
   */
  @Test
  void testGetExits() {
    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 1);
    exits.put("S", -2);
    Room room = new Room(1, "A", "descA", exits);

    Map<String, Integer> returnedExits = room.getExits();
    assertEquals(1, returnedExits.get("N"));
    assertEquals(-2, returnedExits.get("S"));
  }

  /**
   * Test addEntity(IdentifiableEntity entity).
   */
  @Test
  void testAddEntity() {
    Map<String, Integer> exits = new HashMap<>();

    Item itemA = new Item("itemA", "descA", 1, 1,
            10, 5, "used");

    Map<String, Item> items = new HashMap<>();
    items.put(itemA.getName(), itemA);

    Room room = new Room(3, "C", "descC", exits, items);

    Item itemB = new Item("itemB", "descB", 2, 2,
            20, 10, "used");
    // Add entities
    assertTrue(room.addEntity(itemB));
    assertTrue(room.hasEntity(itemB));
  }

  /**
   * Test removeEntity(IdentifiableEntity entity).
   */
  @Test
  void testRemoveEntity() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, IdentifiableEntity> entities = new HashMap<>();
    IdentifiableEntity itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    IdentifiableEntity itemB = new Item("B", "descB", 2, 2,
            20, 10, "used");
    entities.put(itemA.getName(), itemA);
    entities.put(itemB.getName(), itemB);

    Room room = new Room(4, "D", "descD", exits, entities);

    // Remove existing entity
    assertTrue(room.removeEntity(itemA));
    assertFalse(room.hasEntity(itemA));

    assertTrue(room.removeEntity(itemB));
    assertFalse(room.hasEntity(itemB));
  }

  /**
   * Test hasEntity(IdentifiableEntity entity).
   */
  @Test
  void testHasEntityWithEntity() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, Item> items = new HashMap<>();
    Item itemA = new Item("itemA", "descA", 1, 1,
            10, 5, "used");
    items.put(itemA.getName(), itemA);
    Room room = new Room(5, "E", "descE", exits, items);

    assertTrue(room.hasEntity(itemA));
    Item itemB = new Item("itemB", "descB", 2, 2,
            20, 10, "used");
    assertFalse(room.hasEntity(itemB));
  }

  /**
   * Test hasEntity(String entityName).
   */
  @Test
  void testHasEntityWithString() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, Item> items = new HashMap<>();
    Item itemA = new Item("itemA", "descA", 1, 1,
            10, 5, "used");
    items.put(itemA.getName(), itemA);
    Room room = new Room(5, "E", "descE", exits, items);

    assertTrue(room.hasEntity("itemA"));
    Item itemB = new Item("itemB", "descB", 2, 2,
            20, 10, "used");
    assertFalse(room.hasEntity("itemB"));
  }

  /**
   * Test getEntity(String entityName, Class<U> clazz).
   */
  @Test
  void testGetEntity() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, Item> elements = new HashMap<>();
    Item itemA = new Item("itemA", "descA", 1, 1,
            10, 5, "used");
    elements.put("itemA", itemA);

    Room room = new Room(7, "G", "descG", exits, elements);

    Fixture fixtureX = new Fixture("fixtureX", "fixtureX", 1000);
    room.addEntity(fixtureX);

    // Retrieve as Item
    Item retrievedItem = (Item) room.getEntity("itemA", Item.class);
    assertEquals("itemA", retrievedItem.getName());

    // Retrieve as Fixture
    Fixture retrievedFixture = (Fixture) room.getEntity("fixtureX", Fixture.class);
    assertEquals("fixtureX", retrievedFixture.getName());

    // Mismatched type => null
    Fixture shouldBeNull = (Fixture) room.getEntity("fixtureA", Fixture.class);
    assertNull(shouldBeNull);

    // Non-existent name => null
    Item nonExistent = (Item) room.getEntity("Z", Item.class);
    assertNull(nonExistent);
  }

  /**
   * Test getProblem().
   */
  @Test
  void testGetProblem() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, IdentifiableEntity> entities = new HashMap<>();

    // Create a Puzzle
    Puzzle<String> puzzle = new Puzzle<>(
            "PuzzleName", "A puzzle description",
            true, false, false, "Solution",
            50, "puzzleEffects", "puzzleTarget", "puzzlePic"
    );

    Room<?> puzzleRoom = new Room(8, "H", "descH", exits, entities, puzzle);
    assertEquals(puzzle, puzzleRoom.getProblem());
  }

  /**
   * Test getElementNames(Class<U> clazz).
   */
  @Test
  void testGetElementNames() {
    Map<String, Integer> exits = new HashMap<>();
    Map<String, IdentifiableEntity> entities = new HashMap<>();
    Item itemA = new Item("itemA", "descA", 1, 1,
            10, 5, "used");
    Item itemB = new Item("itemB", "descB", 2, 2,
            20, 10, "used");
    Fixture fixtureX = new Fixture("fixtureX", "fixtureX", 9);
    entities.put(itemA.getName(), itemA);
    entities.put(itemB.getName(), itemB);
    entities.put(fixtureX.getName(), fixtureX);

    Room<?> room = new Room(10, "J", "descJ", exits, entities);

    // Get items' names
    String itemNames = room.getElementNames(Item.class);
    assertTrue(itemNames.contains("itemA"));
    assertTrue(itemNames.contains("itemB"));
    assertFalse(itemNames.contains("fixtureX"));

    // Get fixtures' names
    String fixtureNames = room.getElementNames(Fixture.class);
    assertTrue(fixtureNames.contains("fixtureX"));
    assertFalse(fixtureNames.contains("itemA"));
    assertFalse(fixtureNames.contains("itemB"));
  }

}