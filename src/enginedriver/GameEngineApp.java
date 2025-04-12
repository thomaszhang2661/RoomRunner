package enginedriver;

import enginedriver.view.SwingView;
import enginedriver.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import javax.swing.SwingUtilities;

import jsonio.GameDataLoader;

/**
 * The GameEngineApp class is the main entry point for the game engine application.
 * It initializes the game world and player, and starts the game loop.
 * This version has been updated to support both text-based and GUI modes.
 */
public class GameEngineApp {
  private GameController gameController;
  private BufferedReader source;
  private Appendable output;
  private boolean guiMode;

  /**
   * Constructor for the GameEngineApp class.
   *
   * @param gameFileName the name of the game file
   * @param source the input source
   * @param output the output destination
   * @param guiMode true to use GUI, false for text mode
   * @throws IOException if an error occurs during input/output
   */
  public GameEngineApp(String gameFileName, BufferedReader source, Appendable output, boolean guiMode) throws IOException {
    this.source = Objects.requireNonNull(source);
    this.output = Objects.requireNonNull(output);
    this.guiMode = guiMode;

    GameWorld gameWorld = GameDataLoader.loadGameWorld(gameFileName);

    String playerName = getPlayerName(source);
    String playerFileName = playerName + ".json";
    File playerFile = new File(playerFileName);
    Player player = playerFile.exists()
            ? GameDataLoader.loadPlayer(playerFileName, gameWorld)
            : new Player(playerName, 100, 20, 0);

    this.gameController = new GameController(gameWorld, player);

    // Initialize the appropriate view
    if (guiMode) {
      SwingView.setInstance(SwingView.getInstance());
      SwingView.getInstance().initialize(gameController);
    }
  }

  /**
   * Starts the game engine application.
   *
   * @throws IOException if an error occurs during input/output
   */
  public void start() throws IOException {
    if (guiMode) {
      // GUI mode uses event-driven input, so we don't need a command loop here
      // Just display the initial room
      gameController.processCommand("L");
    } else {
      // Text-based mode - run the command loop
      String command;
      while (true) {
        System.out.println("===");
        System.out.println("To move, enter: (N)orth, (S)outh, (E)ast or (W)est.");
        System.out.println("Other actions: (I)nventory, (L)ook around the location, (U)se an item");
        System.out.println("(T)ake an item, (D)rop an item, or e(X)amine something.");
        System.out.println("(A)nswer a question or provide a text solution.");
        System.out.println("To end the game, enter (Q)uit to quit and exit.");
        System.out.print("Your choice: ");

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
  }

  /**
   * Main method to start the game.
   *
   * @param args command line arguments
   * @throws IOException if an error occurs during input/output
   */
  public static void main(String[] args) throws IOException {
    // Parse command line arguments
    boolean useGui = true;  // Default to GUI mode
    String gameFile = "./data/align_quest_game_elements.json";

    for (int i = 0; i < args.length; i++) {
      if (args[i].equalsIgnoreCase("-text")) {
        useGui = false;
      } else if (args[i].equalsIgnoreCase("-game") && i + 1 < args.length) {
        gameFile = args[i + 1];
        i++;
      }
    }

    // Create and start the game application
    if (useGui) {
      // GUI mode setup
      String finalGameFile = gameFile;
      SwingUtilities.invokeLater(() -> {
        try {
          // For GUI mode, we use a special input source that can be written to by the GUI
          PipedBufferedReader guiInput = new PipedBufferedReader();

          GameEngineApp gameEngineApp = new GameEngineApp(
                  finalGameFile,
                  guiInput,
                  System.out,
                  true);

          gameEngineApp.start();
        } catch (IOException e) {
          System.err.println("Error starting game: " + e.getMessage());
          e.printStackTrace();
        }
      });
    } else {
      // Text mode setup
      BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

      GameEngineApp gameEngineApp = new GameEngineApp(
              gameFile,
              consoleReader,
              System.out,
              false);

      gameEngineApp.start();
    }
  }

  /**
   * Gets the player's name from input.
   *
   * @param source the input source
   * @return the player's name
   * @throws IOException if an I/O error occurs
   */
  private String getPlayerName(BufferedReader source) throws IOException {
    if (guiMode) {
      // In GUI mode, show a dialog to get the player name
      return javax.swing.JOptionPane.showInputDialog(
              null,
              "Enter your name:",
              "Player Name",
              javax.swing.JOptionPane.QUESTION_MESSAGE);
    } else {
      // In text mode, get the name from console
      System.out.print("Enter your name: ");
      String playerName = source.readLine();
      System.out.println("Your name is: " + playerName);
      return playerName;
    }
  }

  /**
   * A special BufferedReader that can be written to by the GUI components.
   * This allows the GUI to send commands to the game engine as if they were typed.
   */
  private static class PipedBufferedReader extends BufferedReader {
    private StringBuffer buffer = new StringBuffer();
    private boolean hasLine = false;

    public PipedBufferedReader() {
      super(new StringReader(""));
    }

    /**
     * Add a command to the buffer.
     *
     * @param command the command to add
     */
    public synchronized void addCommand(String command) {
      buffer.append(command);
      hasLine = true;
      notify(); // Wake up any waiting readLine() calls
    }

    @Override
    public synchronized String readLine() throws IOException {
      // Wait until we have a line
      while (!hasLine) {
        try {
          wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new IOException("Interrupted while waiting for input");
        }
      }

      // Return the buffered line
      String line = buffer.toString();
      buffer.setLength(0);
      hasLine = false;
      return line;
    }
  }
}