package enginedriver.jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameController;
import enginedriver.jsonreader.deserializer.GameDeserializer;
import enginedriver.model.GameWorld;
import java.io.File;
import java.io.IOException;

/**
 * The GameDataLoader class is responsible for loading and saving game data.
 * It uses Jackson to read and write JSON files representing the game.
 */
public class GameDataLoader {

  /**
   * Load the entire game (world and player) from a single JSON file.
   * This method can handle both combined format (with "world" and "player" nodes)
   * and raw format (only game world data).

   * @param fileName the name of the file to load
   * @return the GameController object containing both world and player
   * @throws IOException if an error occurs during loading
   */
  public static GameController loadGame(String fileName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addDeserializer(GameController.class, new GameDeserializer());
    mapper.registerModule(module);

    return mapper.readValue(new File(fileName), GameController.class);
  }

  /**
   * Load only the game world from a JSON file.
   * This method simply uses the GameDeserializer and returns only the world part.

   * @param fileName the name of the file to load
   * @return the GameWorld object
   * @throws IOException if an error occurs during loading
   */
  public static GameWorld loadGameWorld(String fileName) throws IOException {
    // Use the enhanced GameDeserializer to handle the raw format directly
    GameController controller = loadGame(fileName);
    return controller.getGameWorld();
  }
}