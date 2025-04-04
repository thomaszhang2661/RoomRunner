package enginedriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Objects;
import java.util.Scanner;

import graphicsview.GraphicalView;
import jsonreader.GameDataLoader;
import textview.TextView;

/**
 * The GameEngineApp class is the main entry point for the game engine application.
 * It initializes the game world and player, and starts the game loop.
 */
public class GameEngineApp {
  private GameController gameController;
  private Readable source;
  private Appendable output;
  private GameView view;
  private boolean graphicsMode;

  /**
   * Constructor for the GameEngineApp class.
   *
   * @param gameFileName the name of the game file
   * @param source the input source
   * @param output the output destination
   * @param graphicsMode whether to run in graphics mode
   * @throws IOException if an error occurs during input/output
   */
  public GameEngineApp(String gameFileName, Readable source, Appendable output, boolean graphicsMode) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);
    this.graphicsMode = graphicsMode;

    GameWorld gameWorld = GameDataLoader.loadGameWorld(gameFileName);
    Player player = GameDataLoader.loadPlayer(gameFileName, gameWorld);

    // if player is null, create a new player from input
    if (player == null) {
      String playerName = getPlayerName();
      player = new Player(playerName, 100, 20, 0); // 提示玩家输入名字
    }

    this.gameController = new GameController(gameWorld, player);

    // Initialize the appropriate view based on mode
    if (graphicsMode) {
      this.view = new GraphicalView(gameController, gameWorld, player);
    } else {
      this.view = new TextView(gameController, gameWorld, player, output);
    }

    gameController.setView(this.view);
  }

  /**
   * Starts the game engine application.
   *
   * @throws IOException if an error occurs during input/output
   */
  public void start() throws IOException {
    if (graphicsMode) {
      // For graphics mode, start the graphical interface
      ((GraphicalView) view).initialize();
    } else {
      // For text mode, run the command loop
      runTextCommandLoop();
    }
  }

  /**
   * Runs the text-based command loop.
   *
   * @throws IOException if an error occurs during input/output
   */
  private void runTextCommandLoop() throws IOException {
    BufferedReader reader = new BufferedReader((Reader) source);

    String command;
    while (true) {
      output.append("===\n");
      output.append("To move, enter: (N)orth, (S)outh, (E)ast or (W)est.\n");
      output.append("Other actions: (I)nventory, (L)ook around the location, (U)se an item\n");
      output.append("(T)ake an item, (D)rop an item, or e(X)amine something.\n");
      output.append("(A)nswer a question or provide a text solution.\n");
      output.append("To end the game, enter (Q)uit to quit and exit.\n");
      output.append("Your choice: ");

      command = reader.readLine();
      if (command == null) {
        break;
      }

      if (command.equalsIgnoreCase("Q")) {
        output.append("Exiting game.\n");
        break;
      }

      gameController.processCommand(command);
    }
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

    Readable inputSource;
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
      GameEngineApp gameEngineApp = new GameEngineApp(
              gameFileName,
              inputSource,
              outputDest,
              isGraphicsMode);

      gameEngineApp.start();

      // Close file resources if we opened any
      if (inputSource instanceof FileReader) {
        ((FileReader) inputSource).close();
      }

      if (outputDest instanceof FileWriter) {
        ((FileWriter) outputDest).close();
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
  private String getPlayerName() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter your name: ");

    String playerName = scanner.nextLine();

    System.out.println("Your name is: " + playerName);

    return playerName;
  }
}