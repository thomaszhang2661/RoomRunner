package enginedriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;
import jsonreader.GameWorldDeserializer;
import jsonreader.PlayerDeserializer;

/**
 * The GameEngineApp class is the main entry point for the game engine application.
 * It initializes the game world and player, and starts the game loop.
 */
public class GameEngineApp {
  private GameController gameController;
  private Readable source;
  private Appendable output;

  /**
   * Constructor for the GameEngineApp class.

   * @param gameFileName the name of the game file
   * @param source the input source
   * @param output the output destination
   * @throws IOException if an error occurs during input/output
   */
  public GameEngineApp(String gameFileName, Readable source, Appendable output) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);

    // create ObjectMapper
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    // register GameWorldDeserializer
    module.addDeserializer(GameWorld.class, new GameWorldDeserializer());
    mapper.registerModule(module);
    // parse gameWorld
    GameWorld gameWorld = mapper.readValue(new File(gameFileName), GameWorld.class);

    // register PlayerDeserializer
    module.addDeserializer(Player.class, new PlayerDeserializer(gameWorld));
    mapper.registerModule(module);
    // parse player
    Player player = mapper.readValue(new File(gameFileName), Player.class);

    // if player is null, create a new player from input
    // TODO： 检测是否存在同名文件，有重名要提示不能用
    if (player == null) {
      String playerName = getPlayerName();
      player = new Player(playerName, 100, 20, 0); // 提示玩家输入名字
    }

    this.gameController = new GameController(gameWorld, player);
  }


  /**
   * Starts the game engine application.

   * @throws IOException if an error occurs during input/output
   */
  public void start() throws IOException {
    BufferedReader reader = new BufferedReader((Reader) source);

    String command;
    while ((command = reader.readLine()) != null) {
      if (command.equalsIgnoreCase("Q")) {
        System.out.println("Exiting game.");
        break;
      }
      gameController.processCommand(command);
    }
  }

  /**
   * Main method to start the game.

   * @param args command line arguments
   * @throws IOException if an error occurs during input/output
   */
  public static void main(String[] args) throws IOException {
    String s = "Sir Mix-A-Lot\nT NOTEBOOK\nN\nT HAIR CLIPPERS\nT KEY\nD NOTEBOOK\nQuit";
    BufferedReader stringReader = new BufferedReader(new StringReader(s));
    GameEngineApp gameEngineApp = new GameEngineApp(
            "./resources/align_quest_game_elements.json",
            stringReader,
            System.out);

    gameEngineApp.start();
  }

  private String getPlayerName() {
    // 创建 Scanner 对象以读取用户输入
    Scanner scanner = new Scanner(System.in);

    // 提示用户输入姓名
    System.out.print("Enter your name: ");

    // 读取用户输入的字符串
    String playerName = scanner.nextLine();

    // 输出用户输入的姓名
    System.out.println("Your name is: " + playerName);

    // 关闭 Scanner 对象
    scanner.close();
    return playerName;
  }
}
