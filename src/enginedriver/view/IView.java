package enginedriver.view;

import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.problems.IProblem;

/**
 * Interface for game views in MVC architecture.
 * This represents the View component of the MVC pattern.
 */
public interface IView {
  /**
   * Initialize the view.
   */
  void initialize();

  /**
   * Display text to the user.
   * @param text The text to display
   */
  void showText(String text);

  /**
   *   Display Image to the user.
   */
  void showEntity(IdentifiableEntity entity);

  /**
   * Update the view to reflect the current game state.
   */
  void update();

  /**
   * Display information about the current room.
   */
  void displayRoom();

/**
   * Display the probability of the player winning.
   * This method should display the probability of the player winning in the current game state.
   */
 void showProblem(IProblem problem);

    /**
     * Display player status information.
     */
  void displayPlayerStatus();

  /**
   * Display command prompt or menu options.
   */
  void displayCommandPrompt();

  /**
   * Start handling user input.
   * This method should begin the process of accepting and processing user input.
   * For text views, this would be a command loop.
   * For graphical views, this would set up event listeners.
   */
  void startInputHandling();

  /**
   * Stop handling user input.
   * This method should terminate the input processing.
   */
  void stopInputHandling();

  /**
   * Process a specific command.
   * This allows views to directly process commands (via the controller).
   * @param command The command to process
   */
  void processCommand(String command);
}