package jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.GameWorld;
import enginedriver.Player;
import java.io.File;
import java.io.IOException;
import jsonreader.serializer.GameWorldSerializer;
import jsonreader.serializer.PlayerSerializer;

/**
 * The GameDataSaver class is responsible for saving game data to two JSON files.
 */
public class GameDataSaver {
  /**
   * Save the game world to a JSON file.

   * @param gameWorld the GameWorld object to save
   * @param fileName  the name of the file to save to
   * @throws IOException if an error occurs during saving
   */
  public static void saveGameJson(GameWorld gameWorld, String fileName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(GameWorld.class, new GameWorldSerializer());
    mapper.registerModule(module);

    File saveFile = new File(fileName);
    mapper.writeValue(saveFile, gameWorld);
  }

  /**
   * Save the player to a JSON file.
   *
   * @param player   the Player object to save
   * @param fileName the name of the file to save to
   * @throws IOException if an error occurs during saving
   */
  public static void savePlayerJson(Player player, String fileName) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(Player.class, new PlayerSerializer());
    mapper.registerModule(module);

    File saveFile = new File(fileName);
    mapper.writeValue(saveFile, player);
  }
}
