import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameController;
import enginedriver.jsonreader.deserializer.GameDeserializer;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Test class for deserialization of GameController objects.
 * This test class tests the combined deserializer that handles both raw and combined JSON formats.
 */
class DeserializationTest {

  /**
   * Test the deserialization of GameController object from raw GameWorld JSON file.
   * The test checks if the GameController object is correctly deserialized and if its
   * GameWorld and default Player properties match the expected values.

   * @throws IOException if an error occurs during deserialization
   */
  @Test
  void testDeserializeRawGameWorld() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addDeserializer(GameController.class,
            new GameDeserializer()));

    GameController controller = mapper.readValue(new File("test/TestGameWorld.json"), GameController.class);

    // test game world that exists in the file
    assertNotNull(controller);
    assertNotNull(controller.getGameWorld());
    assertEquals("Simple Scenarios", controller.getGameWorld().getName());
    assertEquals("0.0.1", controller.getGameWorld().getVersion());
    assertEquals(5, controller.getGameWorld().getRooms().size());
    assertTrue(controller.getGameWorld().getRooms().containsKey(1));
    assertTrue(controller.getGameWorld().getRooms().containsKey(5));
    assertNotNull(controller.getGameWorld().getRooms().get(1).getProblem());

    // test default player
    assertNotNull(controller.getPlayer());
    assertEquals("DefaultPlayer", controller.getPlayer().getName());
  }

  /**
   * Test the deserialization of GameController object from combined JSON file.
   * The test checks if the GameController object is correctly deserialized and if its
   * GameWorld and Player properties match the expected values.

   * @throws IOException if an error occurs during deserialization
   */
  @Test
  void testDeserializeCombinedGame() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new SimpleModule().addDeserializer(GameController.class,
            new GameDeserializer()));

    GameController controller = mapper.readValue(new File("test/TestGame.json"), GameController.class);

    // check the game world
    assertNotNull(controller);
    assertNotNull(controller.getGameWorld());
    assertEquals("Simple Scenarios", controller.getGameWorld().getName());
    assertEquals("0.0.1", controller.getGameWorld().getVersion());
    assertEquals(5, controller.getGameWorld().getRooms().size());

    // check the player
    assertNotNull(controller.getPlayer());
    assertEquals("Avatar", controller.getPlayer().getName());
    assertEquals(100, controller.getPlayer().getHealth());
    assertEquals(20, controller.getPlayer().getMaxWeight());
    assertEquals(0, controller.getPlayer().getCurrentWeight());
    assertEquals(1, controller.getPlayer().getRoomNumber());
    assertEquals(0, controller.getPlayer().getScore());
  }
}