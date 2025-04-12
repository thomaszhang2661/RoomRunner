package enginedriver.view;

import enginedriver.GameController;
import enginedriver.HEALTH_STATUS;
import enginedriver.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying player status information.
 */
public class StatusPanel extends JPanel {
  private final GameController controller;
  private final JProgressBar healthBar;
  private final JLabel statusLabel;
  private final JLabel scoreLabel;
  private final JLabel roomLabel;

  /**
   * Constructor for StatusPanel.
   *
   * @param controller the game controller
   */
  public StatusPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Status"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    setPreferredSize(new Dimension(250, 120));

    // Create status components
    healthBar = new JProgressBar(0, 100);
    healthBar.setStringPainted(true);
    healthBar.setString("Health: 100%");

    statusLabel = new JLabel("You are awake and healthy");
    scoreLabel = new JLabel("Score: 0");
    roomLabel = new JLabel("Location: Unknown");

    // Layout the components
    JPanel topPanel = new JPanel(new GridLayout(3, 1, 0, 5));
    topPanel.add(healthBar);
    topPanel.add(statusLabel);

    JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 5));
    bottomPanel.add(scoreLabel);
    bottomPanel.add(roomLabel);

    add(topPanel, BorderLayout.NORTH);
    add(bottomPanel, BorderLayout.SOUTH);

    // Initial update
    updateUI();
  }

  /**
   * Update the panel's UI to reflect current game state.
   */
  @Override
  public void updateUI() {
    super.updateUI();

    if (controller != null) {
      Player player = controller.getPlayer();

      if (player != null) {
        // Update health bar
        int health = player.getHealth();
        healthBar.setValue(health);
        healthBar.setString("Health: " + health + "%");

        // Set color based on health
        if (health > 70) {
          healthBar.setForeground(new Color(50, 200, 50)); // Green
        } else if (health > 40) {
          healthBar.setForeground(new Color(200, 200, 50)); // Yellow
        } else {
          healthBar.setForeground(new Color(200, 50, 50)); // Red
        }

        // Update status message
        HEALTH_STATUS status = player.checkStatus();
        statusLabel.setText(status.getMessage());

        // Update score
        scoreLabel.setText("Score: " + player.getScore());

        // Update room location
        int roomNumber = player.getRoomNumber();
        String roomName = controller.getGameWorld().getRoom(roomNumber).getName();
        roomLabel.setText("Location: " + roomName);
      }
    }
  }
}