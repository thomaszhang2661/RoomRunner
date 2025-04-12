package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Panel for displaying the current room image and visual elements.
 */
public class RoomPanel extends JPanel {
  private final GameController controller;
  private BufferedImage roomImage;
  private BufferedImage defaultImage;
  private final int PANEL_WIDTH = 500;
  private final int PANEL_HEIGHT = 350;

  /**
   * Constructor for RoomPanel.
   *
   * @param controller the game controller
   */
  public RoomPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    setBorder(BorderFactory.createLineBorder(Color.BLACK));

    // Load default image
    try {
      defaultImage = ImageIO.read(new File("./data/images/default_room.png"));
    } catch (IOException e) {
      // Create a simple default image if file not found
      defaultImage = createDefaultImage();
    }

    roomImage = defaultImage;

    // Initial update
    updateUI();
  }

  /**
   * Create a default image when no room image is available.
   *
   * @return a simple generated image
   */
  private BufferedImage createDefaultImage() {
    BufferedImage image = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    // Fill background
    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

    // Draw question mark
    g2d.setColor(Color.YELLOW);
    Font font = new Font("Arial", Font.BOLD, 150);
    g2d.setFont(font);
    FontMetrics metrics = g2d.getFontMetrics(font);
    String text = "?";
    int x = (PANEL_WIDTH - metrics.stringWidth(text)) / 2;
    int y = ((PANEL_HEIGHT - metrics.getHeight()) / 2) + metrics.getAscent();
    g2d.drawString(text, x, y);

    g2d.dispose();
    return image;
  }

  /**
   * Paint the panel with the current room image.
   *
   * @param g the Graphics context
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (roomImage != null) {
      // Calculate position to center the image
      int x = (getWidth() - roomImage.getWidth()) / 2;
      int y = (getHeight() - roomImage.getHeight()) / 2;

      // Draw the image
      g.drawImage(roomImage, x, y, this);
    }
  }

  /**
   * Update the panel's UI to reflect current game state.
   */
  @Override
  public void updateUI() {
    super.updateUI();

    if (controller != null) {
      // Get current room
      Room<?> currentRoom = controller.getGameWorld().getRoom(
              controller.getPlayer().getRoomNumber());

      if (currentRoom != null) {
        // Try to load the room's image
        BufferedImage newImage = currentRoom.getPicture();

        if (newImage != null) {
          roomImage = newImage;
        } else {
          // If no image, use default
          roomImage = defaultImage;
        }

        // Repaint to show the new image
        repaint();
      }
    }
  }
}