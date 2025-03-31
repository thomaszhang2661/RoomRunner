import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Player;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import jsonreader.GameDataLoader;
import jsonreader.GameDataSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for GameDataSaver.
 */
class GameDataSaverTest {

  private GameController controller;
  private static final String TEST_FILE_NAME = "test_save_game.json"; // 测试文件名

  @BeforeEach
  public void setUp() {
    String setupFileName = "test/TestGameWorld.json";
    try {
      GameWorld gameWorld = GameDataLoader.loadGameWorld(setupFileName);
      Player player = GameDataLoader.loadPlayer(setupFileName, gameWorld);
      controller = new GameController(gameWorld, player);
    } catch (IOException e) {
      fail("IOException was thrown during setup: " + e.getMessage());
    }

    // ensure the test file does not exist before the test
    new File(TEST_FILE_NAME).delete();
  }

  @Test
  void testGameDataSaver() {
    try {
      // save the game data to a JSON file
      GameDataSaver.saveGameJson(controller, TEST_FILE_NAME);

      // verify if the file was created
      File file = new File(TEST_FILE_NAME);
      assertTrue(file.exists(), "File should be created");

      // verify if the file is not empty
      String content = Files.readString(Paths.get(TEST_FILE_NAME));
      assertNotNull(content, "Content should not be null");
      assertFalse(content.isEmpty(), "Content should not be empty");

      // verify if the content conforms to expected JSON structure
      assertTrue(content.contains("\"name\":"), "Should contain 'name' field");

      // use Jackson to parse the content and validate JSON format
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(content); // Try parsing the content as JSON
      assertNotNull(jsonNode, "Parsed JSON should not be null");

      // verify expected fields are present in the JSON
      assertTrue(jsonNode.has("name"), "JSON should contain 'name' field");
      assertTrue(jsonNode.has("version"), "JSON should contain 'gameWorld' field");
      assertTrue(jsonNode.has("player"), "JSON should contain 'player' field");

      // TODO: Add more specific checks depending on the JSON structure

    } catch (IOException e) {
      fail("IOException was thrown: " + e.getMessage());
    }
  }

  @AfterEach
  public void tearDown() {
    // delete the test file after each test
    File file = new File(TEST_FILE_NAME);
    if (file.exists()) {
      boolean deleted = file.delete();
      if (!deleted) {
        System.err.println("Failed to delete test file: " + TEST_FILE_NAME);
      }
    }
  }
}
