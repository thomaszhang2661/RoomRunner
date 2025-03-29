import java.io.IOException;
import jsonreader.GameDataLoader;
import enginedriver.GameWorld;

public class GameDataLoaderTest {
  public static void main(String[] args) {
    try {
      // Load the game world from a JSON file
      GameWorld gameWorld = GameDataLoader.loadGameWorld("test/Test.json");

      // Print the loaded game world details
      System.out.println("Loaded Game World:");
      System.out.println("Name: " + gameWorld.getName());
      System.out.println("Version: " + gameWorld.getVersion());

      // Modify the game world
      gameWorld.setName("Modified Game World");
      gameWorld.setVersion("1.0.1");

      // Save the modified game world to a new JSON file
      GameDataLoader.saveGameWorld(gameWorld, "TestSaved.json");

      System.out.println("Game world saved successfully.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}