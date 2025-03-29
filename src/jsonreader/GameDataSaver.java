package jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import enginedriver.GameController;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The GameDataSaver class is responsible for saving game data to a JSON file.
 * It uses Jackson to write the game controller state to a file.
 */
public class GameDataSaver {
  /**
   * Save the game controller to a JSON file.

   * @param controller the GameController object to save
   * @param fileName   the name of the file to save to
   * @throws IOException if an error occurs during saving
   */
  public static void saveGameController(GameController controller, String fileName)
          throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing

    try (FileWriter writer = new FileWriter(fileName)) {
      writer.write(mapper.writeValueAsString(controller));
    }
  }
}