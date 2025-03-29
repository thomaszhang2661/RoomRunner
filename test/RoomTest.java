import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import enginedriver.Fixture;
import enginedriver.IProblem;
import enginedriver.IdentifiableEntity;
import enginedriver.Item;
import enginedriver.Room;
import enginedriver.Puzzle;
import enginedriver.Monster;


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
            true,false, false, "Solution",
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
    entities.put("itemA", new Item("A", "descA", 1, 1,
            10, 5, "used"));
    entities.put("fixtureB", new Fixture(11, "B", "fixtureDesc"));

    Room room = new Room(10, "C", "descC", exits, entities);
    assertEquals(10, room.getId());
    assertEquals("C", room.getName());
    assertEquals("descC", room.getDescription());
    assertEquals(exits, room.getExits());
    // The constructor sets problem to null
    assertNull(room.getProblem());

    // Confirm entities are there
    assertTrue(room.hasEntity("A"));
    assertTrue(room.hasEntity("X"));
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
    entities.put("fixtureY", new Fixture(12, "Y", "fixtureDescY"));

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
    Room<?> room = new Room(1, "A", "descA", exits);

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
    Room<?> room = new Room(3, "C", "descC", exits);

    IdentifiableEntity itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    IdentifiableEntity itemB = new Item("B", "descB", 2, 2,
            20, 10, "used");

    // Add entities
    assertTrue(room.addEntity(itemA));
    assertTrue(room.hasEntity(itemA));
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

    Room<?> room = new Room(4, "D", "descD", exits, entities);

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
    Room<?> room = new Room(5, "E", "descE", exits);

    IdentifiableEntity itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    room.addEntity(itemA);

    assertTrue(room.hasEntity(itemA));
    IdentifiableEntity itemB = new Item("B", "descB", 2, 2,
            20, 10, "used");
    assertFalse(room.hasEntity(itemB));
  }

  /**
   * Test hasEntity(String entityName).
   */
  @Test
  void testHasEntityWithString() {
    Map<String, Integer> exits = new HashMap<>();
    Room<?> room = new Room(6, "F", "descF", exits);

    IdentifiableEntity itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    room.addEntity(itemA);

    assertTrue(room.hasEntity("A"));
    assertFalse(room.hasEntity("B"));
  }

  /**
   * Test getEntity(String entityName, Class<U> clazz).
   */
  @Test
  void testGetEntity() {
    Map<String, Integer> exits = new HashMap<>();
    Room<?> room = new Room(7, "G", "descG", exits);

    Item itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    Fixture fixtureX = new Fixture(7, "X", "fixtureX");
    room.addEntity(itemA);
    room.addEntity(fixtureX);

    // Retrieve as Item
    Item retrievedItem = (Item) room.getEntity("A", Item.class);
    assertEquals("A", retrievedItem.getName());

    // Retrieve as Fixture
    Fixture retrievedFixture = (Fixture) room.getEntity("X", Fixture.class);
    assertEquals("X", retrievedFixture.getName());

    // Mismatched type => null
    Fixture shouldBeNull = (Fixture) room.getEntity("A", Fixture.class);
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
            true,false, false, "Solution",
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
    Item itemA = new Item("A", "descA", 1, 1,
            10, 5, "used");
    Item itemB = new Item("B", "descB", 2, 2,
            20, 10, "used");
    Fixture fixtureX = new Fixture(9, "X", "fixtureX");
    entities.put(itemA.getName(), itemA);
    entities.put(itemB.getName(), itemB);
    entities.put(fixtureX.getName(), fixtureX);

    Room<?> room = new Room(10, "J", "descJ", exits, entities);

    // Get items' names
    String itemNames = room.getElementNames(Item.class);
    assertTrue(itemNames.contains("A"));
    assertTrue(itemNames.contains("B"));
    assertFalse(itemNames.contains("X"));

    // Get fixtures' names
    String fixtureNames = room.getElementNames(Fixture.class);
    assertTrue(fixtureNames.contains("X"));
    assertFalse(fixtureNames.contains("A"));
    assertFalse(fixtureNames.contains("B"));
  }

  /**
   * Test toString().
   */
  @Test
  void testToString() {
    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 10);
    exits.put("S", 20);
    exits.put("E", 30);
    exits.put("W", -40);

    Map<String, IdentifiableEntity> entities = new HashMap<>();
    // Single item and single fixture for a predictable toString outcome:
    entities.put("A", new Item("A", "descA", 1, 1, 10, 5, "used"));
    entities.put("X", new Fixture(10, "X", "fixtureX"));

    // A Puzzle
    Puzzle<String> puzzle = new Puzzle<>(
            "PuzzleName", "PuzzleDesc", true, false,
            false, "PuzzleSolution", 99, "puzzleEffects",
            "puzzleTarget", "puzzlePic"
    );

    // A Monster
    Monster<String> monster = new Monster<>(
            "MonsterName", "MonsterDesc", true, true,
            true, "MonsterSolution", 200, 15,
            "monsterEffects", "monsterTarget", "monsterPic", "Bite"
    );

    // Room with puzzle
    Room<?> puzzleRoom = new Room(11, "K", "descK", exits, entities, puzzle);

    String puzzleJSON = "{ \"name\":\"PuzzleName\",\"active\":\"true\",\"affects_target\":\"false\","
            + "\"affects_player\":\"false\",\"solution\":\"PuzzleSolution\",\"value\":\"99\","
            + "\"description\":\"PuzzleDesc\",\"effects\":\"puzzleEffects\",\"target\":\"puzzleTarget\","
            + "\"picture\":\"null\" }";

    String expectedPuzzleRoomString =
            "{ " +
                    "\"room_name\":\"K\"," +
                    "\"room_number\":\"11\"," +
                    "\"description\":\"descK\"," +
                    "\"N\":\"10\"," +
                    "\"S\":\"20\"," +
                    "\"E\":\"30\"," +
                    "\"W\":\"-40\"," +
                    "\"puzzle\":\"" + puzzleJSON.replace("\"", "\\\"") + "\"," +
                    "\"monster\":null," +
                    "\"items\":\"A\"," +
                    "\"fixtures\":\"X\"," +
                    "\"picture\":\"null\"" +
                    " }";

    assertEquals(expectedPuzzleRoomString, puzzleRoom.toString());

    // Room with monster
    Room<?> monsterRoom = new Room(12, "L", "descL", exits, entities, monster);


    String monsterJSON = "{ \"name\":\"MonsterName\",\"active\":\"true\",\"affects_target\":\"true\","
            + "\"affects_player\":\"true\",\"solution\":\"MonsterSolution\",\"value\":\"200\","
            + "\"description\":\"MonsterDesc\",\"effects\":\"monsterEffects\",\"damage\":\"15\","
            + "\"target\":\"monsterTarget\",\"can_attack\":\"true\",\"attack\":\"Bite\",\"picture\":\"null\" }";


    String expectedMonsterRoomString =
            "{ " +
                    "\"room_name\":\"L\"," +
                    "\"room_number\":\"12\"," +
                    "\"description\":\"descL\"," +
                    "\"N\":\"10\"," +
                    "\"S\":\"20\"," +
                    "\"E\":\"30\"," +
                    "\"W\":\"-40\"," +
                    "\"puzzle\":null," +
                    "\"monster\":\"" + monsterJSON.replace("\"", "\\\"") + "\"," +
                    "\"items\":\"A\"," +
                    "\"fixtures\":\"X\"," +
                    "\"picture\":\"null\"" +
                    " }";

    assertEquals(expectedMonsterRoomString, monsterRoom.toString());
  }
}