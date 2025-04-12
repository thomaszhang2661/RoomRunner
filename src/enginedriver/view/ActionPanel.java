package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Item;
import enginedriver.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel containing action buttons for game interactions.
 */
public class ActionPanel extends JPanel {
  private final GameController controller;
  private final JButton takeButton;
  private final JButton dropButton;
  private final JButton examineButton;
  private final JButton useButton;
  private final JButton lookButton;
  private final JButton answerButton;
  private final JButton inventoryButton;

  /**
   * Constructor for ActionPanel.
   *
   * @param controller the game controller
   */
  public ActionPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    // Create action buttons
    takeButton = createActionButton("Take", "T");
    examineButton = createActionButton("Examine", "X");
    useButton = createActionButton("Use", "U");
    dropButton = createActionButton("Drop", "D");
    lookButton = createActionButton("Look", "L");
    answerButton = createActionButton("Answer", "A");
    inventoryButton = createActionButton("Inventory", "I");

    // Add buttons to panel
    add(takeButton);
    add(examineButton);
    add(useButton);
    add(dropButton);
    add(lookButton);
    add(answerButton);
    add(inventoryButton);

    // Initial update
    updateUI();
  }

  /**
   * Create an action button with the given label and command.
   *
   * @param label the button label
   * @param command the command to send
   * @return the created button
   */
  private JButton createActionButton(String label, String command) {
    JButton button = new JButton(label);
    button.setFont(new Font("SansSerif", Font.PLAIN, 12));
    button.setPreferredSize(new Dimension(100, 30));

    button.addActionListener(e -> {
      if (command.equals("T") || command.equals("X")) {
        // These commands need an object name
        showObjectSelectionDialog(command);
      } else if (command.equals("U") || command.equals("D")) {
        // These commands need an inventory item
        showInventorySelectionDialog(command);
      } else if (command.equals("A")) {
        // Answer needs text input
        showAnswerDialog();
      } else {
        // Direct commands
        controller.processCommand(command);
      }
    });

    return button;
  }

  /**
   * Show dialog for selecting an object in the room.
   *
   * @param command the command to apply to the selected object
   */
  private void showObjectSelectionDialog(String command) {
    if (controller == null) return;

    Room<?> currentRoom = controller.getGameWorld().getRoom(
            controller.getPlayer().getRoomNumber());

    if (currentRoom == null) return;

    // Get items and fixtures from the room
    List<String> objects = new ArrayList<>();

    if (command.equals("T")) {
      // For TAKE, only show items
      String itemNames = currentRoom.getElementNames(Item.class);
      if (!itemNames.isEmpty()) {
        for (String name : itemNames.split(", ")) {
          objects.add(name.trim());
        }
      }
    } else {
      // For EXAMINE, show all visible entities
      String itemNames = currentRoom.getElementNames(Item.class);
      String fixtureNames = currentRoom.getElementNames(enginedriver.Fixture.class);

      if (!itemNames.isEmpty()) {
        for (String name : itemNames.split(", ")) {
          objects.add(name.trim());
        }
      }

      if (!fixtureNames.isEmpty()) {
        for (String name : fixtureNames.split(", ")) {
          objects.add(name.trim());
        }
      }
    }

    if (objects.isEmpty()) {
      JOptionPane.showMessageDialog(
              this,
              "There are no objects to " +
                      (command.equals("T") ? "take" : "examine") +
                      " in this room.",
              "No Objects Available",
              JOptionPane.INFORMATION_MESSAGE
      );
      return;
    }

    // Create and show dialog
    String selectedObject = (String) JOptionPane.showInputDialog(
            this,
            "Select an object to " + (command.equals("T") ? "take" : "examine") + ":",
            "Select Object",
            JOptionPane.QUESTION_MESSAGE,
            null,
            objects.toArray(),
            objects.get(0)
    );

    if (selectedObject != null) {
      controller.processCommand(command + " " + selectedObject);
    }
  }

  /**
   * Show dialog for selecting an item from inventory.
   *
   * @param command the command to apply to the selected item
   */
  private void showInventorySelectionDialog(String command) {
    if (controller == null) return;

    // Get player's inventory items
    java.util.Map<String, Item> inventory = controller.getPlayer().getEntities();

    if (inventory.isEmpty()) {
      JOptionPane.showMessageDialog(
              this,
              "You don't have any items in your inventory.",
              "Empty Inventory",
              JOptionPane.INFORMATION_MESSAGE
      );
      return;
    }

    // Create and show dialog
    String selectedItem = (String) JOptionPane.showInputDialog(
            this,
            "Select an item to " + (command.equals("U") ? "use" : "drop") + ":",
            "Select Item",
            JOptionPane.QUESTION_MESSAGE,
            null,
            inventory.keySet().toArray(),
            inventory.keySet().iterator().next()
    );

    if (selectedItem != null) {
      controller.processCommand(command + " " + selectedItem);
    }
  }

  /**
   * Show dialog for entering an answer.
   */
  private void showAnswerDialog() {
    String answer = JOptionPane.showInputDialog(
            this,
            "Enter your answer:",
            "Answer",
            JOptionPane.QUESTION_MESSAGE
    );

    if (answer != null && !answer.trim().isEmpty()) {
      controller.processCommand("A " + answer);
    }
  }

  /**
   * Update the panel's UI to reflect current game state.
   */
  @Override
  public void updateUI() {
    super.updateUI();

    // Additional UI updates could be added here if needed
    // For example, disabling certain buttons based on game state
  }
}