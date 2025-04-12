package enginedriver;

import enginedriver.view.SwingView;
import java.io.IOException;

/**
 * Main entry point for the graphical version of the adventure game.
 * This class provides a simple launcher for the GUI version of the game.
 */
public class AdventureGameGUI {

  /**
   * Main method to start the game in GUI mode.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    System.out.println("Starting Adventure Game with GUI...");

    // Create and run the game with GUI mode enabled
    try {
      // Pass "-gui" argument to the GameEngineApp
      String[] gameArgs = new String[]{"-gui"};
      GameEngineApp.main(gameArgs);
    } catch (IOException e) {
      System.err.println("Error starting the game: " + e.getMessage());
      e.printStackTrace();
    }
  }
}