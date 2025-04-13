package enginedriver.view;

import enginedriver.GameController;
import enginedriver.model.GameWorld;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.IProblem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Text-based implementation of the GameView interface.
 * This provides a console-based text interface for the game.
 */
public class TextView implements IView {
  private final GameController controller;
  private final GameWorld gameWorld;
  private final Player player;
  private final Appendable output;
  private BufferedReader reader;
  private boolean isRunning;
  private Thread inputThread;

  /**
   * Constructor for TextView.
   *
   * @param controller The game controller
   * @param gameWorld The game world
   * @param player The player
   * @param output The output destination
   */
  public TextView(GameController controller, GameWorld gameWorld,
                  Player player, Appendable output) {
    this.controller = controller;
    this.gameWorld = gameWorld;
    this.player = player;
    this.output = output;
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    this.isRunning = false;
  }

  /**
   * Sets the input source for reading commands.
   *
   * @param source The input source
   */
  public void setInputSource(BufferedReader source) {
    this.reader = source;
  }

  @Override
  public void initialize() {
    try {
      output.append("Welcome to ").append(gameWorld.getName()).append("!\n");
      output.append("Your character: ").append(player.getName()).append("\n");
      displayRoom();
    } catch (IOException e) {
      System.err.println("Output error: " + e.getMessage());
    }
  }

  @Override
  public void showText(String text) {
    try {
      output.append(text).append("\n");
    } catch (IOException e) {
      System.err.println("Output error: " + e.getMessage());
    }
  }

  @Override
  public void showEntity(IdentifiableEntity entity) {
  }

  @Override
  public void update() {
    displayRoom();
    displayPlayerStatus();
  }

  @Override
  public void displayRoom() {
    try {
      Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
      output.append("\nCurrent location: ").append(currentRoom.getName()).append("\n");
      output.append(currentRoom.getDescription()).append("\n");

      // Show items in the room
      //      String items = currentRoom.getElementNames("Item");
      //      if (!items.isEmpty()) {
      //        output.append("You see the following items: ").append(items).append("\n");
      //      }

      // Show fixtures in the room
      //      String fixtures = currentRoom.getElementNames("Fixture");
      //      if (!fixtures.isEmpty()) {
      //        output.append("You see the following fixtures: ").append(fixtures).append("\n");
      //      }
    } catch (IOException e) {
      System.err.println("Output error: " + e.getMessage());
    }
  }

  @Override
  public void showProblem(IProblem problem) {

  }

  @Override
  public void displayPlayerStatus() {
    try {
      output.append("\nPlayer Status:\n");
      output.append("Location: ").append(String.valueOf(player.getRoomNumber())).append("\n");
      output.append("Health: ").append(String.valueOf(player.getHealth())).append("\n");
      output.append("Score: ").append(String.valueOf(player.getScore())).append("\n");
      output.append("Inventory: ").append(player.getEntities().keySet().toString()).append("\n");
      output.append("Current weight: ").append(String.valueOf(player.getCurrentWeight()))
              .append("/").append(String.valueOf(player.getMaxWeight())).append("\n");
    } catch (IOException e) {
      System.err.println("Output error: " + e.getMessage());
    }
  }

  @Override
  public void displayCommandPrompt() {
    try {
      output.append("\n===\n");
      output.append("To move, enter: (N)orth, (S)outh, (E)ast or (W)est.\n");
      output.append("Other actions: (I)nventory, (L)ook around the location, (U)se an item\n");
      output.append("(T)ake an item, (D)rop an item, or e(X)amine something.\n");
      output.append("(A)nswer a question or provide a text solution.\n");
      output.append("To end the game, enter (Q)uit to quit and exit.\n");
      output.append("Your choice: ");
    } catch (IOException e) {
      System.err.println("Output error: " + e.getMessage());
    }
  }

  @Override
  public void startInputHandling() {
    isRunning = true;

    // Create a new thread for input handling to prevent blocking
    inputThread = new Thread(() -> {
      String command;

      try {
        while (isRunning) {
          displayCommandPrompt();

          command = reader.readLine();
          if (command == null) {
            break;
          }

          if (command.equalsIgnoreCase("Q")) {
            showText("Exiting game.");
            isRunning = false;
            break;
          }

          processCommand(command);
        }
      } catch (IOException e) {
        System.err.println("Error reading input: " + e.getMessage());
      }
    });

    inputThread.start();
  }

  @Override
  public void stopInputHandling() {
    isRunning = false;
    if (inputThread != null && inputThread.isAlive()) {
      inputThread.interrupt();
    }
  }

  @Override
  public void processCommand(String command) {
    // Pass the command to the controller
    controller.processCommand(command);
  }

  /**
   * Waits for the input handling thread to complete.
   *
   * @throws InterruptedException if the thread is interrupted
   */
  public void waitForCompletion() throws InterruptedException {
    if (inputThread != null && inputThread.isAlive()) {
      inputThread.join();
    }
  }
}