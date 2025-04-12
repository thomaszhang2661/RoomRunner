package enginedriver.view;

import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * MainFrame is the main window of the game GUI.
 * It contains all panels and implements the overall layout.
 */
public class MainFrame extends JFrame {
  private final GameController controller;
  private final RoomPanel roomPanel;
  private final NavigationPanel navigationPanel;
  private final InventoryPanel inventoryPanel;
  private final DescriptionPanel descriptionPanel;
  private final StatusPanel statusPanel;
  private final ActionPanel actionPanel;

  // Menu components
  private JMenuBar menuBar;
  private JMenu fileMenu;
  private JMenu viewMenu;

  /**
   * Constructor for MainFrame.
   *
   * @param controller the game controller
   */
  public MainFrame(GameController controller) {
    this.controller = controller;

    // Set up the frame properties
    setTitle(controller.getGameWorld().getName());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(900, 700));
    setLayout(new BorderLayout());

    // Create panels
    roomPanel = new RoomPanel(controller);
    navigationPanel = new NavigationPanel(controller);
    inventoryPanel = new InventoryPanel(controller);
    descriptionPanel = new DescriptionPanel(controller);
    statusPanel = new StatusPanel(controller);
    actionPanel = new ActionPanel(controller);

    // Add components to panel sections
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(roomPanel, BorderLayout.CENTER);
    centerPanel.add(navigationPanel, BorderLayout.SOUTH);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.add(new JLabel("Inventory"), BorderLayout.NORTH);
    rightPanel.add(inventoryPanel, BorderLayout.CENTER);
    rightPanel.add(statusPanel, BorderLayout.SOUTH);

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(new JLabel("Description"), BorderLayout.NORTH);
    bottomPanel.add(descriptionPanel, BorderLayout.CENTER);
    bottomPanel.add(actionPanel, BorderLayout.SOUTH);

    // Add panels to the frame
    add(centerPanel, BorderLayout.CENTER);
    add(rightPanel, BorderLayout.EAST);
    add(bottomPanel, BorderLayout.SOUTH);

    // Create menu bar
    createMenuBar();

    // Register this frame with SwingView
    SwingView.getInstance().registerMainFrame(this);

    // Handle window closing
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
                MainFrame.this,
                "Would you like to save the game before exiting?",
                "Exit Game",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
          controller.processCommand("SAVE");
          System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
          System.exit(0);
        }
        // If CANCEL, do nothing
      }
    });

    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Create the menu bar and items.
   */
  private void createMenuBar() {
    menuBar = new JMenuBar();

    // File menu
    fileMenu = new JMenu("File");
    JMenuItem newGameItem = new JMenuItem("New Game");
    JMenuItem saveGameItem = new JMenuItem("Save Game");
    JMenuItem loadGameItem = new JMenuItem("Load Game");
    JMenuItem exitItem = new JMenuItem("Exit");

    newGameItem.addActionListener(e -> {
      // Implement new game functionality
      int confirm = JOptionPane.showConfirmDialog(
              this,
              "Start a new game? Any unsaved progress will be lost.",
              "New Game",
              JOptionPane.YES_NO_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        // Call code to start a new game
        try {
          restartGame();
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(
                  this,
                  "Error starting new game: " + ex.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    saveGameItem.addActionListener(e -> controller.processCommand("SAVE"));
    loadGameItem.addActionListener(e -> controller.processCommand("RESTORE"));

    exitItem.addActionListener(e -> {
      int confirm = JOptionPane.showConfirmDialog(
              this,
              "Exit the game? Any unsaved progress will be lost.",
              "Exit Game",
              JOptionPane.YES_NO_CANCEL_OPTION);

      if (confirm == JOptionPane.YES_OPTION) {
        controller.processCommand("SAVE");
        System.exit(0);
      } else if (confirm == JOptionPane.NO_OPTION) {
        System.exit(0);
      }
    });

    fileMenu.add(newGameItem);
    fileMenu.add(saveGameItem);
    fileMenu.add(loadGameItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);

    // View menu
    viewMenu = new JMenu("View");
    JMenuItem fullScreenItem = new JMenuItem("Toggle Full Screen");
    JMenuItem helpItem = new JMenuItem("Help");

    fullScreenItem.addActionListener(e -> {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice gd = ge.getDefaultScreenDevice();

      if (gd.getFullScreenWindow() == this) {
        gd.setFullScreenWindow(null);
      } else {
        gd.setFullScreenWindow(this);
      }
    });

    helpItem.addActionListener(e -> showHelp());

    viewMenu.add(fullScreenItem);
    viewMenu.add(helpItem);

    // Add menus to menubar
    menuBar.add(fileMenu);
    menuBar.add(viewMenu);

    // Set the menu bar
    setJMenuBar(menuBar);
  }

  /**
   * Show help dialog.
   */
  private void showHelp() {
    JTextArea helpText = new JTextArea(
            "Adventure Game Help\n\n" +
                    "Navigation: Use the direction buttons (N, S, E, W) to move between rooms.\n" +
                    "Actions:\n" +
                    "- Take: Pick up an item from the current room\n" +
                    "- Examine: Get details about an item or fixture\n" +
                    "- Use: Use an item from your inventory\n" +
                    "- Drop: Remove an item from your inventory\n" +
                    "- Answer: Provide a solution to a puzzle\n\n" +
                    "Menus:\n" +
                    "File > Save Game: Save your current progress\n" +
                    "File > Load Game: Load a previously saved game\n" +
                    "File > Exit: Quit the game\n\n" +
                    "Enjoy your adventure!"
    );
    helpText.setEditable(false);
    helpText.setLineWrap(true);
    helpText.setWrapStyleWord(true);

    JScrollPane scrollPane = new JScrollPane(helpText);
    scrollPane.setPreferredSize(new Dimension(500, 300));

    JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Game Help",
            JOptionPane.INFORMATION_MESSAGE
    );
  }

  /**
   * Restart the game.
   */
  private void restartGame() throws IOException {
    // To be implemented - restart the game engine
    // This would involve creating a new GameEngineApp instance
    JOptionPane.showMessageDialog(
            this,
            "New game functionality will be implemented in a future version.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE
    );
  }

  /**
   * Update all UI components to reflect the current game state.
   */
  public void updateUI() {
    roomPanel.updateUI();
    navigationPanel.updateUI();
    inventoryPanel.updateUI();
    descriptionPanel.updateUI();
    statusPanel.updateUI();
    actionPanel.updateUI();
  }

  /**
   * Get the room panel.
   *
   * @return the room panel
   */
  public RoomPanel getRoomPanel() {
    return roomPanel;
  }

  /**
   * Get the navigation panel.
   *
   * @return the navigation panel
   */
  public NavigationPanel getNavigationPanel() {
    return navigationPanel;
  }

  /**
   * Get the inventory panel.
   *
   * @return the inventory panel
   */
  public InventoryPanel getInventoryPanel() {
    return inventoryPanel;
  }

  /**
   * Get the description panel.
   *
   * @return the description panel
   */
  public DescriptionPanel getDescriptionPanel() {
    return descriptionPanel;
  }

  /**
   * Get the status panel.
   *
   * @return the status panel
   */
  public StatusPanel getStatusPanel() {
    return statusPanel;
  }

  /**
   * Get the action panel.
   *
   * @return the action panel
   */
  public ActionPanel getActionPanel() {
    return actionPanel;
  }
}