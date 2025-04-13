import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.GameController;
import enginedriver.jsonreader.GameDataLoader;
import enginedriver.jsonreader.GameDataSaver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for GameDataLoader and GameDataSaver functionality.
 */
class GameDataLoaderSaverTest {

  private static final String TEST_RAW_FILE = "test/TestGameWorld.json";
  private static final String TEST_COMBINED_FILE = "test/TestGame.json";
  private static final String TEST_OUTPUT_FILE = "test/TestOutput.json";

  /**
   * Test loading from raw GameWorld file and saving to combined format.
   */
  @Test
  void testLoadRawAndSave() {
    try {
      GameController controller = GameDataLoader.loadGame(TEST_RAW_FILE);
      assertNotNull(controller);

      GameDataSaver.saveGameJson(TEST_OUTPUT_FILE, controller);
      verifyOutputFile();

      // load the saved file and check
      GameController loadedController = GameDataLoader.loadGame(TEST_OUTPUT_FILE);
      assertEquals("DefaultPlayer", loadedController.getPlayer().getName());
      assertEquals("Simple Scenarios", loadedController.getGameWorld().getName());
    } catch (IOException e) {
      fail("IOException was thrown: " + e.getMessage());
    }
  }

  /**
   * Test loading from combined format file and saving to combined format.
   */
  @Test
  void testLoadCombinedAndSave() {
    try {
      GameController controller = GameDataLoader.loadGame(TEST_COMBINED_FILE);
      assertNotNull(controller);

      // check the player name
      assertEquals("Avatar", controller.getPlayer().getName());

      GameDataSaver.saveGameJson(TEST_OUTPUT_FILE, controller);
      verifyOutputFile();

      // load the saved file and check
      GameController loadedController = GameDataLoader.loadGame(TEST_OUTPUT_FILE);
      assertEquals("Avatar", loadedController.getPlayer().getName());
      assertEquals("Simple Scenarios", loadedController.getGameWorld().getName());
    } catch (IOException e) {
      fail("IOException was thrown: " + e.getMessage());
    }
  }

  /**
   * Helper method to verify the output file.
   */
  private void verifyOutputFile() throws IOException {
    // check if the file exists
    File outputFile = new File(TEST_OUTPUT_FILE);
    assertTrue(outputFile.exists(), "Output file should be created");

    // check if the file is not empty
    String content = Files.readString(Paths.get(TEST_OUTPUT_FILE));
    assertNotNull(content, "Content should not be null");
    assertFalse(content.isEmpty(), "Content should not be empty");

    // check if the content contains expected fields
    assertTrue(content.contains("\"world\":"), "Should contain 'world' field");
    assertTrue(content.contains("\"player\":"), "Should contain 'player' field");

    // parse the JSON content
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(content);

    // check if the JSON structure is correct
    assertTrue(jsonNode.has("world"), "JSON should contain 'world' field");
    assertTrue(jsonNode.has("player"), "JSON should contain 'player' field");

    // check the world name
    JsonNode worldNode = jsonNode.get("world");
    assertTrue(worldNode.has("name"), "World JSON should contain 'name' field");
    assertEquals("Simple Scenarios", worldNode.get("name").asText());

    // check the number of rooms, items, fixtures, monsters, and puzzles
    assertEquals(5, worldNode.get("rooms").size(), "There should be 5 rooms");
    assertEquals(4, worldNode.get("items").size(), "There should be 4 items");
    assertEquals(1, worldNode.get("fixtures").size(), "There should be 1 fixture");
    assertEquals(1, worldNode.get("monsters").size(), "There should be 1 monster");
    assertEquals(2, worldNode.get("puzzles").size(), "There should be 2 puzzles");
  }

  /**
   * Delete the test output file after each test.
   */
  @AfterEach
  public void tearDown() {
    File outputFile = new File(TEST_OUTPUT_FILE);
    if (outputFile.exists()) {
      boolean deleted = outputFile.delete();
      if (!deleted) {
        System.err.println("Failed to delete test file: " + TEST_OUTPUT_FILE);
      }
    }
  }
}