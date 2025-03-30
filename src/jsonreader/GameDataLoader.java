package jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import enginedriver.Player;
import java.io.File;
import java.io.IOException;

/**
 * The GameDataLoader class is responsible for loading and saving game data.
 * It uses Jackson to read and write JSON files representing the game world.
 */
public class GameDataLoader {

  /**
   * Load the game world from a JSON file.

   * @param gameFileName the name of the file to load
   * @return the GameWorld object
   * @throws IOException if an error occurs during loading
   */
  public static GameWorld loadGameWorld(String gameFileName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addDeserializer(GameWorld.class, new GameWorldDeserializer());
    mapper.registerModule(module);

    return mapper.readValue(new File(gameFileName), GameWorld.class);
  }

  /**
   * Load the player from a JSON file.

   * @param gameFileName the name of the file to load
   * @param gameWorld    the GameWorld object
   * @return the Player object
   * @throws IOException if an error occurs during loading
   */
  public static Player loadPlayer(String gameFileName, GameWorld gameWorld) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addDeserializer(Player.class, new PlayerDeserializer(gameWorld));
    mapper.registerModule(module);

    return mapper.readValue(new File(gameFileName), Player.class);
  }
}