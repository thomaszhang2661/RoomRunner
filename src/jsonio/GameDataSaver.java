package jsonio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import enginedriver.Player;
import java.io.File;
import java.io.IOException;
import jsonio.serializer.GameWorldSerializer;
import jsonio.serializer.PlayerSerializer;

/**
 * The GameDataSaver class is responsible for saving game data to two JSON files.
 */
public class GameDataSaver {
  private static final String WORLD_SAVE_BASE_PATH = "resources/worlds/";
  private static final String PLAYER_SAVE_BASE_PATH = "resources/players/";
  /**
   * Save the game world to a JSON file.
   *
   * @param fileName  the name of the file to save to
   * @param gameWorld the GameWorld object to save
   * @throws IOException if an error occurs during saving
   */
  public static void saveGameJson(String fileName, GameWorld gameWorld) throws IOException {
    String newFileName = WORLD_SAVE_BASE_PATH + fileName;
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(GameWorld.class, new GameWorldSerializer());
    mapper.registerModule(module);

    File saveFile = new File(newFileName);
    mapper.writeValue(saveFile, gameWorld);
  }

  /**
   * Save the player to a JSON file.
   *
   * @param fileName the name of the file to save to
   * @param player   the Player object to save
   * @throws IOException if an error occurs during saving
   */
  public static void savePlayerJson(String fileName, Player player) throws IOException {
    String newFileName = PLAYER_SAVE_BASE_PATH + fileName;
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(Player.class, new PlayerSerializer());
    mapper.registerModule(module);

    File saveFile = new File(newFileName);
    mapper.writeValue(saveFile, player);
  }
}
