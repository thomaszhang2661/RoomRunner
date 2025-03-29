package enginedriver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;


public class GameDataLoader {
  private static final ObjectMapper mapper = new ObjectMapper();


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

  public static void saveGameWorld(GameWorld gameWorld, String fileName) {
    try {

      // 将 GameWorld 对象写入 JSON 文件
      mapper.writeValue(new File(fileName), gameWorld);
    } catch (IOException e) {
      System.err.println("Error saving the game file: " + e.getMessage());
    }
  }
}
