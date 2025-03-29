package jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import enginedriver.GameWorld;
import enginedriver.Player;

public class GameDataLoader {
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(GameWorld.class, new GameWorldDeserializer());
    module.addDeserializer(Player.class, new PlayerDeserializer());
    mapper.registerModule(module);
  }

  public static GameWorld loadGameWorld(String fileName) throws IOException {
    return mapper.readValue(new File(fileName), GameWorld.class);
  }

  public static void saveGameWorld(GameWorld gameWorld, String fileName) {
    try (FileWriter fileWriter = new FileWriter(fileName)) {
      fileWriter.write(gameWorld.toString());
    } catch (IOException e) {
      System.err.println("Error saving the game file: " + e.getMessage());
    }
  }
}