import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import enginedriver.GameWorld;
import enginedriver.Room;


/**
 * Example test stubs for the GameWorld class,
 * with exactly one test method per GameWorld method.
 */
public class GameWorldTest {

  /**
   * Test getName().
   */
  @Test
  void testGetName() {
    GameWorld gw = new GameWorld("WorldA", "1.0", null);
    assertEquals("WorldA", gw.getName(),
            "getName() should return the name passed into the constructor.");

    GameWorld gw2 = new GameWorld("WorldB", "2.0", null);
    assertEquals("WorldB", gw2.getName());
  }

  /**
   * Test setName(String).
   */
  @Test
  void testSetName() {
    GameWorld gw = new GameWorld();
    assertNull(gw.getName(), "Default constructor yields null name.");
    gw.setName("SomeName");
    assertEquals("SomeName", gw.getName());
  }

  /**
   * Test getVersion().
   */
  @Test
  void testGetVersion() {
    GameWorld gw = new GameWorld("WorldC", "1.5", null);
    assertEquals("1.5", gw.getVersion());
  }

  /**
   * Test setVersion(String).
   */
  @Test
  void testSetVersion() {
    GameWorld gw = new GameWorld();
    assertNull(gw.getVersion(), "Default constructor yields null version.");
    gw.setVersion("2.5");
    assertEquals("2.5", gw.getVersion());
  }

  /**
   * Test getRooms().
   */
  @Test
  void testGetRooms() {
    Map<Integer, Room> rooms = new HashMap<>();
    // Use the 4-parameter constructor for a basic room with no problem & no entities
    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 1);
    Room simpleRoom = new Room(1, "SimpleRoom", "Just a room", exits);
    rooms.put(1, simpleRoom);

    GameWorld gw = new GameWorld("WorldD", "3.0", rooms);
    assertEquals(rooms, gw.getRooms());
  }

  /**
   * Test setRooms(Map<Integer, Room>).
   */
  @Test
  void testSetRooms() {
    GameWorld gw = new GameWorld();
    assertNull(gw.getRooms(), "By default, rooms is null.");

    Map<Integer, Room> rooms = new HashMap<>();
    // Another simple room
    Map<String, Integer> exits = new HashMap<>();
    exits.put("E", 2);
    Room room1 = new Room(1, "RoomA", "DescA", exits);
    rooms.put(1, room1);

    gw.setRooms(rooms);
    assertEquals(rooms, gw.getRooms());
  }

  /**
   * Test getRoom(int).
   */
  @Test
  void testGetRoom() {
    Map<Integer, Room> rooms = new HashMap<>();
    Map<String, Integer> exits1 = new HashMap<>();
    exits1.put("N", 10);
    Room room1 = new Room<>(1, "RoomA", "DescA", exits1);

    Map<String, Integer> exits2 = new HashMap<>();
    exits2.put("S", 20);
    Room room2 = new Room<>(2, "RoomB", "DescB", exits2);

    rooms.put(1, room1);
    rooms.put(2, room2);

    GameWorld gw = new GameWorld("WorldE", "4.0", rooms);
    // Valid IDs
    assertEquals(room1, gw.getRoom(1));
    assertEquals(room2, gw.getRoom(2));
    // Non-existent ID => null
    assertNull(gw.getRoom(999));
  }

}
