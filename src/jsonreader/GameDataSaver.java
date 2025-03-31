package jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import enginedriver.GameController;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The GameDataSaver class is responsible for saving game data to a JSON file.
 * It uses the toString() method of GameController to write the game controller state to a file.
 */
public class GameDataSaver {
  /**
   * Save the game controller to a JSON file.

   * @param controller the GameController object to save
   * @param fileName   the name of the file to save to
   * @throws IOException if an error occurs during saving
   */
  public static void saveGameJson(GameController controller, String fileName) throws IOException {
    String fullJson = controller.toString();

    // necessary character escaping
    fullJson = fullJson.replace("\"null\"", "null");

    // check if the JSON is valid
    if (!isValidJson(fullJson)) {
      throw new IOException("Generated invalid JSON: " + fullJson);
    }

    // write the JSON to a file
    try (FileWriter writer = new FileWriter(fileName)) {
      writer.write(fullJson);
    }
  }

  /**
   * Validate if a string is a valid JSON format.
   *
   * @param jsonString the string to validate
   * @return true if the string is valid JSON, false otherwise
   */
  private static boolean isValidJson(String jsonString) {
    try {
      new ObjectMapper().readTree(jsonString); // Try to parse the string
      return true;
    } catch (IOException e) {
      return false; // If parsing fails, it's not valid JSON
    }
  }
}
