package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Panel containing directional navigation buttons.
 */
public class NavigationPanel extends JPanel {
  private final GameController controller;
  private final JButton northButton;
  private final JButton southButton;
  private final JButton eastButton;
  private final JButton westButton;

  /**
   * Constructor for NavigationPanel.
   *
   * @param controller the game controller
   */
  public NavigationPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder("Navigation"));

    // Create directional buttons
    northButton = createNavigationButton("N", "North");
    southButton = createNavigationButton("S", "South");
    eastButton = createNavigationButton("E", "East");
    westButton = createNavigationButton("W", "West");

    // Create sub-panels for layout
    JPanel centerPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    // North button
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(5, 5, 5, 5);
    centerPanel.add(northButton, gbc);

    // West button
    gbc.gridx = 0;
    gbc.gridy = 1;
    centerPanel.add(westButton, gbc);

    // Blank center space
    gbc.gridx = 1;
    gbc.gridy = 1;
    JPanel compass = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a compass circle
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

        g.setColor(Color.BLUE);
        g.drawOval(5, 5, getWidth() - 10, getHeight() - 10);

        // Draw compass lines
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
      }
    };
    compass.setPreferredSize(new Dimension(50, 50));
    centerPanel.add(compass, gbc);

    // East button
    gbc.gridx = 2;
    gbc.gridy = 1;
    centerPanel.add(eastButton, gbc);

    // South button
    gbc.gridx = 1;
    gbc.gridy = 2;
    centerPanel.add(southButton, gbc);

    // Add center panel to main panel
    add(centerPanel, BorderLayout.CENTER);

    // Initial update
    updateUI();
  }

  /**
   * Create a navigation button with the given direction.
   *
   * @param direction the direction (N, S, E, W)
   * @param tooltip the tooltip text
   * @return the created button
   */
  private JButton createNavigationButton(String direction, String tooltip) {
    JButton button = new JButton(direction);
    button.setToolTipText(tooltip);
    button.setPreferredSize(new Dimension(60, 40));
    button.setFont(new Font("Arial", Font.BOLD, 14));

    // Set action command and listener
    button.setActionCommand(direction);
    button.addActionListener(e -> {
      System.out.println("Navigation button pressed: " + direction);
      controller.processCommand(direction);

      // Force update after moving
      SwingUtilities.invokeLater(this::updateUI);
    });

    return button;
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
        Map<String, Integer> exits = currentRoom.getExits();

        // Debug output
        System.out.println("Current room: " + currentRoom.getName() +
                " (" + controller.getPlayer().getRoomNumber() + ")");
        System.out.println("Available exits: " + exits);

        // Enable/disable buttons based on available exits
        updateButtonState(northButton, exits.getOrDefault("N", 0));
        updateButtonState(southButton, exits.getOrDefault("S", 0));
        updateButtonState(eastButton, exits.getOrDefault("E", 0));
        updateButtonState(westButton, exits.getOrDefault("W", 0));
      }
    }
  }

  /**
   * Update a direction button's state based on exit availability.
   *
   * @param button the button to update
   * @param exitValue the exit value from the room
   */
  private void updateButtonState(JButton button, int exitValue) {
    String direction = button.getActionCommand();
    System.out.println("Setting " + direction + " button state: exitValue=" + exitValue);

    if (exitValue > 0) {
      // Available exit
      button.setEnabled(true);
      button.setBackground(new Color(200, 255, 200)); // Light green
      System.out.println(direction + " button enabled");
    } else if (exitValue < 0) {
      // Locked exit
      button.setEnabled(false);
      button.setBackground(new Color(255, 200, 200)); // Light red
      System.out.println(direction + " button disabled (locked)");
    } else {
      // No exit
      button.setEnabled(false);
      button.setBackground(UIManager.getColor("Button.background"));
      System.out.println(direction + " button disabled (no exit)");
    }
  }
}