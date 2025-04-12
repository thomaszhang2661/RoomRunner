package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Room;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SwingView implements a graphical view of the game using Swing.
 * It follows the Singleton pattern and extends the existing View class.
 */
public class SwingView extends View {
  private static SwingView instance = null;
  private MainFrame mainFrame;
  private List<String> messageLog;
  private JTextArea consoleOutput;
  private StringBuilder descriptionBuffer = new StringBuilder();

  /**
   * Private constructor for SwingView singleton.
   */
  private SwingView() {
    super();
    messageLog = new ArrayList<>();
  }

  /**
   * Returns the singleton instance of SwingView.
   *
   * @return the SwingView instance
   */
  public static SwingView getInstance() {
    if (instance == null) {
      instance = new SwingView();
    }
    return instance;
  }

  /**
   * Register the main frame with the view.
   *
   * @param mainFrame the main game frame
   */
  public void registerMainFrame(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
  }

  /**
   * Get the main frame.
   *
   * @return the main frame
   */
  public MainFrame getMainFrame() {
    return mainFrame;
  }

  /**
   * Set the console output text area.
   *
   * @param consoleOutput the JTextArea for console output
   */
  public void setConsoleOutput(JTextArea consoleOutput) {
    this.consoleOutput = consoleOutput;
  }

  /**
   * Initialize the GUI with the game controller.
   *
   * @param controller the game controller
   */
  public void initialize(GameController controller) {
    // Create and show the main frame on the EDT
    SwingUtilities.invokeLater(() -> {
      mainFrame = new MainFrame(controller);
      mainFrame.setVisible(true);

      // Initial room display
      controller.processCommand("L");
    });
  }

  /**
   * Update the display for a new room.
   * This method is called when the player moves to a new room.
   *
   * @param room the new room
   */
  public void updateRoomDisplay(Room<?> room) {
    if (mainFrame == null) return;

    System.out.println("SwingView.updateRoomDisplay called for room: " + room.getName());

    // Clear the description buffer for a new room
    descriptionBuffer.setLength(0);

    // Format complete room description
    descriptionBuffer.append("Location: ").append(room.getName())
            .append(" (Room #").append(room.getId()).append(")\n\n")
            .append(room.getDescription()).append("\n\n");

    // Add items if any
    String items = room.getElementNames(enginedriver.Item.class);
    if (!items.isEmpty()) {
      descriptionBuffer.append("Items you see here: ").append(items).append("\n\n");
    }

    // Add fixtures if any
    String fixtures = room.getElementNames(enginedriver.Fixture.class);
    if (!fixtures.isEmpty()) {
      descriptionBuffer.append("Fixtures you see here: ").append(fixtures).append("\n\n");
    }

    // Update the description panel
    SwingUtilities.invokeLater(() -> {
      if (mainFrame != null) {
        // Update description
        mainFrame.getDescriptionPanel().setDescription(descriptionBuffer.toString());

        // Update all other UI components
        mainFrame.getRoomPanel().updateUI();
        mainFrame.getNavigationPanel().updateUI();
        mainFrame.getInventoryPanel().updateUI();
        mainFrame.getStatusPanel().updateUI();
      }
    });
  }

  /**
   * Shows text in both the GUI and console.
   * Overrides the base View.showText method.
   *
   * @param text the text to display
   */
  @Override
  public void showText(String text) {
    // Add to message log
    messageLog.add(text);

    // Print to console for debugging/legacy support
    System.out.println(text);

    // Add to description buffer
    if (!text.contains("You are currently standing in") &&
            !text.contains("Items you see here") &&
            !text.contains("Fixtures you see here")) {

      if (descriptionBuffer.length() > 0) {
        descriptionBuffer.append("\n");
      }
      descriptionBuffer.append(text);
    }

    // Update the GUI on the EDT if it's been initialized
    if (mainFrame != null) {
      SwingUtilities.invokeLater(() -> {
        // Update console output if available
        if (consoleOutput != null) {
          consoleOutput.append(text + "\n");
          // Auto-scroll to the bottom
          consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
        }
      });
    }
  }

  /**
   * Get the message log.
   *
   * @return list of all messages shown
   */
  public List<String> getMessageLog() {
    return new ArrayList<>(messageLog);
  }

  /**
   * Get the last message shown.
   *
   * @return the last message or empty string if no messages
   */
  public String getLastMessage() {
    if (messageLog.isEmpty()) {
      return "";
    }
    return messageLog.get(messageLog.size() - 1);
  }
}