package enginedriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

import jsonreader.GameDataLoader;

/**
 * The GameEngineApp class is the main entry point for the game engine application.
 * It initializes the game world and player, and starts the game loop.
 */
public class GameEngineApp {
  private GameController gameController;
  private BufferedReader  source;
  private Appendable output;

  /**
   * Constructor for the GameEngineApp class.

   * @param gameFileName the name of the game file
   * @param source the input source
   * @param output the output destination
   * @throws IOException if an error occurs during input/output
   */
  public GameEngineApp(String gameFileName, BufferedReader source, Appendable output) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);

    GameWorld gameWorld = GameDataLoader.loadGameWorld(gameFileName);
    Player player = GameDataLoader.loadPlayer(gameFileName, gameWorld);

    // if player is null, create a new player from input
    if (player == null) {
      String playerName = getPlayerName(source);
      player = new Player(playerName, 100, 20, 0); // 提示玩家输入名字
    }

    this.gameController = new GameController(gameWorld, player);
  }

  /**
   * Starts the game engine application.

   * @throws IOException if an error occurs during input/output
   */
  public void start() throws IOException {
    //BufferedReader reader = new BufferedReader((Reader) source);

    String command;
    while (true) {
      System.out.println("===");
      System.out.println("To move, enter: (N)orth, (S)outh, (E)ast or (W)est.");
      System.out.println("Other actions: (I)nventory, (L)ook around the location, (U)se an item");
      System.out.println("(T)ake an item, (D)rop an item, or e(X)amine something.");
      System.out.println("(A)nswer a question or provide a text solution.");
      System.out.println("To end the game, enter (Q)uit to quit and exit.");
      System.out.print("Your choice: ");

      //command = this.source.read();
      // 从source中读取一行命令
      command = source.readLine();
      if (command == null) {
        break;
      }

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
//  public static void main(String[] args) throws IOException {
//    BufferedReader consoleReader =
//            new BufferedReader(new InputStreamReader(System.in));
//
//    GameEngineApp gameEngineApp = new GameEngineApp(
//            "./resources/align_quest_game_elements.json",
//            consoleReader,
//            System.out);
//
//    gameEngineApp.start();
//  }

  public static void main(String [] args) throws IOException {
    // smoke tests - first send synthetic data via a string
    String s = "Sir Mix-A-Lot\nT NOTEBOOK\nN\nT HAIR CLIPPERS\nT KEY\nD NOTEBOOK\nQuit";
    BufferedReader stringReader = new BufferedReader(new StringReader(s));
    GameEngineApp gameEngineApp = new GameEngineApp("./data/align_quest_game_elements.json",
            stringReader, System.out);
    gameEngineApp.start();


    // Next, comment the above and uncomment this to do some ad-hoc testing by hand via System.in
    // GameEngineApp gameEngineApp = new GameEngineApp("./resources/museum.json", new InputStreamReader(System.in), System.out);
    // gameEngineApp.start();

  }

  /**
   * Gets the player's name from input.

   * @return the player's name
   */
  private String getPlayerName(BufferedReader source) throws IOException {
  //    Scanner scanner = new Scanner(source);

    System.out.print("Enter your name: ");

    String playerName = source.readLine();

    System.out.println("Your name is: " + playerName);

    return playerName;
  }
}
