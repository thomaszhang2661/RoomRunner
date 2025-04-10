package jsonio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import enginedriver.Player;
import jsonio.deserializer.GameWorldDeserializer;
import jsonio.deserializer.PlayerDeserializer;

import java.io.File;
import java.io.IOException;

/**
 * The GameDataLoader class is responsible for loading and saving game data.
 * It uses Jackson to read and write JSON files representing the game world.
 */
public class GameDataLoader {
  private static final String WORLD_SAVE_BASE_PATH = "resources/worlds/";
  private static final String PLAYER_SAVE_BASE_PATH = "resources/players/";

  /**
   * Load the game world from a JSON file.

   * @param fileName the name of the file to load
   * @return the GameWorld object
   * @throws IOException if an error occurs during loading
   */
  public static GameWorld loadGameWorld(String fileName) throws IOException {
    String newFileName = WORLD_SAVE_BASE_PATH + fileName;
    // 检查文件是否存在
    File file = new File(newFileName);
    if (!file.exists()) {
      newFileName = fileName;

    }

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addDeserializer(GameWorld.class, new GameWorldDeserializer());
    mapper.registerModule(module);

    return mapper.readValue(new File(newFileName), GameWorld.class);
  }

  /**
   * Load the player from a JSON file.

   * @param fileName the name of the file to load
   * @param gameWorld    the GameWorld object
   * @return the Player object
   * @throws IOException if an error occurs during loading
   */
  public static Player loadPlayer(String fileName, GameWorld gameWorld) throws IOException {
    String newFileName = PLAYER_SAVE_BASE_PATH + fileName;

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addDeserializer(Player.class, new PlayerDeserializer(gameWorld));
    mapper.registerModule(module);

    return mapper.readValue(new File(newFileName), Player.class);
  }
}