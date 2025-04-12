package enginedriver.jsonreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import enginedriver.model.GameWorld;
import enginedriver.model.entitycontainer.Player;
import java.io.File;
import java.io.IOException;
import enginedriver.jsonreader.serializer.GameWorldSerializer;
import enginedriver.jsonreader.serializer.PlayerSerializer;

/**
 * The GameDataSaver class is responsible for saving game data to two JSON files.
 */
public class GameDataSaver {
  /**
   * Save the game world to a JSON file.
   *
   * @param fileName  the name of the file to save to
   * @param gameWorld the GameWorld object to save
   * @throws IOException if an error occurs during saving
   */
  public static void saveGameJson(String fileName, GameWorld gameWorld) throws IOException {
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
   * @param fileName the name of the file to save to
   * @param player   the Player object to save
   * @throws IOException if an error occurs during saving
   */
  public static void savePlayerJson(String fileName, Player player) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(Player.class, new PlayerSerializer());
    mapper.registerModule(module);

    File saveFile = new File(fileName);
    mapper.writeValue(saveFile, player);
  }
}
