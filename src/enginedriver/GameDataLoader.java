package enginedriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


/**
 * The GameDataLoader class is responsible for loading and saving game data.
 * It uses Jackson to read and write JSON files representing the game world.
 */
public class GameDataLoader {
  private static final ObjectMapper mapper = new ObjectMapper();


  /**
   * Load the game world from a JSON file.

   * @param fileName the name of the file to load
   * @return the GameWorld object
   * @throws IOException if an error occurs during loading
   */
  public static GameWorld loadGameWorld(String fileName) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    GameWorld gameWorld = null;
    try {
      // 解析 JSON 文件并将其映射为 GameWorld 类
      gameWorld = objectMapper.readValue(new File(fileName), GameWorld.class);
    } catch (IOException e) {
      System.err.println("Error reading the game file: " + e.getMessage());
    }

    return gameWorld;
  }

  /**
   * Save the game world to a JSON file.

   * @param gameWorld the GameWorld object to save
   * @param fileName  the name of the file to save to
   */
  public static void saveGameWorld(GameWorld gameWorld, String fileName) {
    try {

      // 将 GameWorld 对象写入 JSON 文件
      mapper.writeValue(new File(fileName), gameWorld);
    } catch (IOException e) {
      System.err.println("Error saving the game file: " + e.getMessage());
    }
  }
}
