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
import jsonio.GameDataLoader;
import jsonio.GameDataSaver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for GameDataSaver.
 */
class GameDataLoaderSaverTest {

  private GameController controller;
  private static String testGameFileName;
  private static String testPlayerFileName;
  private static String testGameFilePath;
  private static String testPlayerFilePath;

  /**
   * Set up the test environment.
   * Load the game world and player from a JSON file.
   */
  @BeforeEach
  public void setUp() {
    // Load the game world and player from JSON files in the test folder in the resources directory
    String setupGameFileName = "TestGameWorld.json";
    String setupPlayerFileName = "TestPlayer.json";

    try {
      GameWorld gameWorld = GameDataLoader.loadGameWorld(setupGameFileName);
      Player player = GameDataLoader.loadPlayer(setupPlayerFileName, gameWorld);
      controller = new GameController(gameWorld, player);

      // Add copy suffix to the test file names to avoid overwriting the original files
      testGameFileName = controller.getGameWorld().getName() + "_copy.json";
      testPlayerFileName = controller.getPlayer().getName() + "_copy.json";

      testGameFilePath = "resources/worlds/" + testGameFileName;
      testPlayerFilePath = "resources/players/" + testPlayerFileName;
    } catch (IOException e) {
      fail("IOException was thrown during setup: " + e.getMessage());
    }

    // ensure the test file does not exist before the test
    new File(testGameFileName).delete();
    new File(testPlayerFileName).delete();
  }

  /**
   * Test the saveGameJson method.
   * It should create a JSON file with the game data.
   */
  @Test
  void testGameDataSaver() {
    try {
      // save the game data to a JSON file in the same directory
      GameDataSaver.saveGameJson(testGameFileName, controller.getGameWorld());

      GameDataSaver.savePlayerJson(testPlayerFileName, controller.getPlayer());

      // verify if the file was created
      File gameFile = new File(testGameFilePath);
      assertTrue(gameFile.exists(), "File should be created");

      File playerFile = new File(testPlayerFilePath);
      assertTrue(playerFile.exists(), "File should be created");

      // verify if the file is not empty
      String gameContent = Files.readString(Paths.get(testGameFilePath));
      assertNotNull(gameContent, "Content should not be null");
      assertFalse(gameContent.isEmpty(), "Content should not be empty");

      String playerContent = Files.readString(Paths.get(testPlayerFilePath));
      assertNotNull(playerContent, "Content should not be null");
      assertFalse(playerContent.isEmpty(), "Content should not be empty");

      // verify if the content conforms to expected JSON structure
      assertTrue(gameContent.contains("\"name\":"), "Should contain 'name' field");

      assertTrue(playerContent.contains("\"name\":"), "Should contain 'name' field");

      // use Jackson to parse the content and validate JSON format
      ObjectMapper gameObjectMapper = new ObjectMapper();
      JsonNode gameJsonNode = gameObjectMapper.readTree(gameContent); // Try parsing the content as JSON
      assertNotNull(gameJsonNode, "Parsed JSON should not be null");

      ObjectMapper playerObjectMapper = new ObjectMapper();
      JsonNode playerJsonNode = playerObjectMapper.readTree(playerContent); // Try parsing the content as JSON
      assertNotNull(playerJsonNode, "Parsed JSON should not be null");

      // verify expected fields are present in the JSON
      assertTrue(gameJsonNode.has("name"), "JSON should contain 'name' field");
      assertTrue(gameJsonNode.has("version"), "JSON should contain 'gameWorld' field");
      assertTrue(gameJsonNode.has("rooms"), "JSON should contain 'rooms' field");
      assertTrue(gameJsonNode.has("items"), "JSON should contain 'items' field");
      assertTrue(gameJsonNode.has("fixtures"), "JSON should contain 'fixtures' field");
      assertTrue(gameJsonNode.has("monsters"), "JSON should contain 'monsters' field");
      assertTrue(gameJsonNode.has("puzzles"), "JSON should contain 'puzzles' field");

      assertTrue(playerJsonNode.has("name"), "JSON should contain 'name' field");
      assertTrue(playerJsonNode.has("health"), "JSON should contain 'health' field");
      assertTrue(playerJsonNode.has("inventory"), "JSON should contain 'inventory' field");
      assertTrue(playerJsonNode.has("max_weight"), "JSON should contain 'max_weight' field");
      assertTrue(playerJsonNode.has("current_weight"), "JSON should contain 'current_weight' field");
      assertTrue(playerJsonNode.has("room_number"), "JSON should contain 'room_number' field");
      assertTrue(playerJsonNode.has("score"), "JSON should contain 'score' field");
    } catch (IOException e) {
      fail("IOException was thrown: " + e.getMessage());
    }
  }

  /**
   * Delete the test file after each test.
   */
  @AfterEach
  public void tearDown() {
    // delete the test file after each test
    File gameFile = new File(testGameFilePath);
    if (gameFile.exists()) {
      boolean deleted = gameFile.delete();
      if (!deleted) {
        System.err.println("Failed to delete test file: " + testGameFilePath);
      }
    }

    File playerFile = new File(testPlayerFilePath);
    if (playerFile.exists()) {
      boolean deleted = playerFile.delete();
      if (!deleted) {
        System.err.println("Failed to delete test file: " + testPlayerFilePath);
      }
    }
  }
}
