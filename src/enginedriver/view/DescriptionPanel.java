package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel for displaying text descriptions of rooms, items, etc.
 */
public class DescriptionPanel extends JPanel {
  private final GameController controller;
  private final JTextArea descriptionArea;
  private final int MAX_DESCRIPTION_LENGTH = 5000; // Characters

  /**
   * Constructor for DescriptionPanel.
   *
   * @param controller the game controller
   */
  public DescriptionPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder("Description"));
    setPreferredSize(new Dimension(600, 150));

    // Create description text area
    descriptionArea = new JTextArea();
    descriptionArea.setEditable(false);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);
    descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
    descriptionArea.setBorder(new EmptyBorder(5, 5, 5, 5));

    // Add to a scroll pane
    JScrollPane scrollPane = new JScrollPane(descriptionArea);
    add(scrollPane, BorderLayout.CENTER);

    // Initial update
    updateUI();
  }

  /**
   * Get the description text area.
   *
   * @return the description JTextArea
   */
  public JTextArea getDescriptionArea() {
    return descriptionArea;
  }

  /**
   * Update the panel's UI to reflect current game state.
   */
  @Override
  public void updateUI() {
    super.updateUI();

    if (controller != null) {
      Room<?> currentRoom = controller.getGameWorld().getRoom(
              controller.getPlayer().getRoomNumber());

      if (currentRoom != null) {
        // Only update description if it's empty (let the controller handle updates)
        if (descriptionArea.getText().isEmpty()) {
          updateRoomDescription(currentRoom);
        }
      }
    }
  }

  /**
   * Update the description area with the current room information.
   *
   * @param room the current room
   */
  public void updateRoomDescription(Room<?> room) {
    if (room != null) {
      // Build a complete room description
      StringBuilder roomInfo = new StringBuilder();
      roomInfo.append("Location: ").append(room.getName()).append("\n\n");
      roomInfo.append(room.getDescription()).append("\n\n");

      // Add items in the room if any
      String items = room.getElementNames(enginedriver.Item.class);
      if (!items.isEmpty()) {
        roomInfo.append("Items you see here: ").append(items).append("\n");
      }

      // Add fixtures in the room if any
      String fixtures = room.getElementNames(enginedriver.Fixture.class);
      if (!fixtures.isEmpty()) {
        roomInfo.append("Fixtures you see here: ").append(fixtures).append("\n");
      }

      // Set the complete description
      setDescription(roomInfo.toString());
    }
  }

  /**
   * Set the description text.
   *
   * @param text the text to display
   */
  public void setDescription(String text) {
    descriptionArea.setText(text);
    // Make sure it scrolls to the top
    descriptionArea.setCaretPosition(0);
  }

  /**
   * Append text to the description.
   *
   * @param text the text to append
   */
  public void appendDescription(String text) {
    // Check if we need to trim the existing content (to prevent unlimited growth)
    String currentText = descriptionArea.getText();
    if (currentText.length() > MAX_DESCRIPTION_LENGTH) {
      // Trim to the last 2000 characters
      currentText = currentText.substring(currentText.length() - 2000);
      descriptionArea.setText(currentText);
    }

    // Append the new text
    descriptionArea.append("\n" + text);
    // Scroll to make the new text visible
    descriptionArea.setCaretPosition(descriptionArea.getDocument().getLength());
  }
}