package enginedriver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

import enginedriver.jsonreader.GameDataLoader;
import enginedriver.model.GameWorld;
import enginedriver.model.entitycontainer.Player;
import enginedriver.view.TextView;
import enginedriver.view.GraphicView;
import enginedriver.view.IView;

/**
 * The GameEngineApp class is the main entry point for the game engine application.
 * It initializes the game world and player, and starts the game loop.
 */
public class GameEngineApp {
  private static final String RESOURCE_FILE = "resources/";

  private BufferedReader source;
  private Appendable output;

  private final GameController gameController;
//  private final Readable source;
//  private final Appendable output;
  private final IView viewer;

  /**
   * Constructor for the GameEngineApp class.
   *
   * @param fileName the name of the game file
   * @param source the input source
   * @param output the output destination
   * @param graphicsMode whether to run in graphics mode
   * @throws IOException if an error occurs during input/output
   */
  public GameEngineApp(String fileName, BufferedReader source, Appendable output, boolean graphicsMode) throws IOException {
    this.source = source;
    this.output = output;

    // rawFileName removing .json suffix that is used to save the game
    String rawFileName = fileName.endsWith(".json")
            ? fileName.substring(0, fileName.length() - 5) : fileName;
    // fileNameWithPrefix is the name of the file that is used to load the game
    String fileNameWithPrefix = RESOURCE_FILE + fileName;

    // Get player name first
    String playerName = graphicsMode ? getPlayerNameWithDialog() : getPlayerNameFromConsole();

    // Check if combined game file exists (gameFileName_playerName.json)
    String combinedFileName = RESOURCE_FILE + rawFileName + "_" + playerName + ".json";
    File combinedFile = new File(combinedFileName);

    GameWorld gameWorld;
    Player player;

    if (combinedFile.exists()) {
      // Load combined game file if it exists
      GameController gameController = GameDataLoader.loadGame(combinedFileName);
      gameWorld = gameController.getGameWorld();
      player = gameController.getPlayer();
    } else {
      // Otherwise load world from initial file and create new player
      gameWorld = GameDataLoader.loadGameWorld(fileNameWithPrefix);
      player = new Player(playerName, 100, 20, 0);
    }

    // Create controller
    this.gameController = new GameController(gameWorld, player);
    this.gameController.setRawFileName(rawFileName);

    // Initialize the appropriate view based on mode
    if (graphicsMode) {
      this.viewer = new GraphicView(gameController, gameWorld, player);
    } else {
      this.viewer = new TextView(gameController, gameWorld, player, output);
    }

    // Set view in controller
    gameController.setView(this.viewer);
  }




  /**
   * Starts the game engine application.
   *
   * @throws IOException if an error occurs during input/output
   */
  public void start() throws IOException {
    // Initialize the view
    viewer.initialize();

    // Start handling user input in the view
    viewer.startInputHandling();
  }

  /**
   * Main method to start the game.
   *
   * @param args command line arguments
   * @throws IOException if an error occurs during input/output
   */
  public static void main(String[] args) throws IOException {
    // Check if we have enough arguments
    if (args.length < 2) {
      System.out.println("Usage:");
      System.out.println("  java -jar game_engine <filename> -text");
      System.out.println("  java -jar game_engine <filename> -graphics");
      System.out.println("  java -jar game_engine <filename> -batch <source>");
      System.out.println("  java -jar game_engine <filename> -batch <source> <target>");
      return;
    }

    String gameFileName = args[0];
    String mode = args[1];

    BufferedReader inputSource;
    Appendable outputDest;
    boolean isGraphicsMode = false;

    switch (mode) {
      case "-text":
        // Text mode: interactive with console I/O
        inputSource = new BufferedReader(new InputStreamReader(System.in));
        outputDest = System.out;
        break;

      case "-graphics":
        // Graphics mode: GUI interface
        inputSource = new BufferedReader(new InputStreamReader(System.in));
        outputDest = System.out;
        isGraphicsMode = true;
        break;

      case "-batch":
        if (args.length < 3) {
          System.out.println("Error: Batch mode requires a source file.");
          return;
        }

        // Batch mode: input from file
        String sourceFile = args[2];
        try {
          inputSource = new BufferedReader(new FileReader(sourceFile));

          // If a target file is specified, redirect output there
          if (args.length >= 4) {
            String targetFile = args[3];
            outputDest = new PrintWriter(new FileWriter(targetFile));
          } else {
            // Otherwise, output to console
            outputDest = System.out;
          }
        } catch (IOException e) {
          System.out.println("Error: Could not open source file: " + e.getMessage());
          return;
        }
        break;

      default:
        System.out.println("Error: Unknown mode '" + mode + "'");
        return;
    }

    try {
      // Create and start the game engine
      GameEngineApp gameEngineApp = new GameEngineApp(
              gameFileName,
              inputSource,
              outputDest,
              isGraphicsMode);

      gameEngineApp.start();

      // Close file resources if we opened any
      inputSource.close();

      if (outputDest instanceof PrintWriter) {
        ((PrintWriter) outputDest).close();
      }

    } catch (IOException e) {
      System.out.println("Error running game: " + e.getMessage());
    }
  }

  /**
   * Gets the player's name from input.
   *
   * @return the player's name
   */
  private String getPlayerNameFromConsole() {
    Scanner scanner = new Scanner(this.source);

    System.out.print("Enter your name: ");
    String playerName = scanner.nextLine();
    System.out.println("Welcome, " + playerName + "!");

    return playerName;
  }

  /**
   * Gets the player's name from input, using a dialog box.
   *
   * @return the player's name
   */
  private String getPlayerNameWithDialog() {
    String playerName;
    do {
      // 加载自定义图标
      BufferedImage image = null;
      try {
        image = ImageIO.read(new File(RESOURCE_FILE + "images/game_engine.png"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      ImageIcon icon = new ImageIcon(image);
      playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Player Name",
              JOptionPane.QUESTION_MESSAGE, icon, null, null).toString();
      if (playerName == null || playerName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } while (playerName == null || playerName.trim().isEmpty());
    return playerName.trim();
  }

}