package enginedriver.jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameController;
import enginedriver.jsonreader.serializer.GameSerializer;
import java.io.File;
import java.io.IOException;

/**
 * The GameDataSaver class is responsible for saving game data to a JSON file.
 * It uses Jackson to serialize the game data.
 */
public class GameDataSaver {

  /**
   * Save the full game data (GameWorld + Player) to a JSON file.

   * @param fileName the name of the file to save to
   * @param controller the GameController that contains world and player
   * @throws IOException if saving fails
   */
  public static void saveGameJson(String fileName, GameController controller) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(GameController.class, new GameSerializer());
    mapper.registerModule(module);

    File saveFile = new File(fileName);
    mapper.writeValue(saveFile, controller);
  }
}