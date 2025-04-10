package viewer;

import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Player;
import enginedriver.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphical implementation of the GameView interface.
 * This provides a GUI interface for the game using Swing.
 */
public class GraphicView extends JFrame implements IView, ActionListener {
  private final GameController controller;
  private final GameWorld gameWorld;
  private final Player player;

  // UI components
  private JTextArea roomDescription;
  private JTextArea messageArea;
  private JTextField commandInput;
  private JButton northButton, southButton, eastButton, westButton;
  private JButton inventoryButton, lookButton, useButton, takeButton, dropButton, examineButton;
  private JLabel healthLabel, scoreLabel, weightLabel;
  private JPanel imagePanel;
  private JList<String> inventoryList;
  private JList<String> roomItemsList;

  // Image cache
  private Map<String, ImageIcon> imageCache;
  private boolean inputHandlingActive;

  /**
   * Constructor for GraphicalView.
   *
   * @param controller The game controller
   * @param gameWorld The game world
   * @param player The player
   */
  public GraphicView(GameController controller, GameWorld gameWorld, Player player) {
    this.controller = controller;
    this.gameWorld = gameWorld;
    this.player = player;
    this.imageCache = new HashMap<>();
    this.inputHandlingActive = false;

    setTitle(gameWorld.getName() + " - Text Adventure Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
  }

  @Override
  public void initialize() {
    // Create UI components
    createUIComponents();

    // Set up layout
    setupLayout();

    // Initial display
    update();

    // Show window
    setVisible(true);
  }

  /**
   * Create the UI components.
   */
  private void createUIComponents() {
    // Room description area
    roomDescription = new JTextArea();
    roomDescription.setEditable(false);
    roomDescription.setLineWrap(true);
    roomDescription.setWrapStyleWord(true);

    // Message area
    messageArea = new JTextArea();
    messageArea.setEditable(false);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);

    // Command input field
    commandInput = new JTextField();

    // Direction buttons
    northButton = new JButton("North (N)");
    southButton = new JButton("South (S)");
    eastButton = new JButton("East (E)");
    westButton = new JButton("West (W)");

    // Action buttons
    inventoryButton = new JButton("Inventory (I)");
    lookButton = new JButton("Look (L)");
    useButton = new JButton("Use (U)");
    takeButton = new JButton("Take (T)");
    dropButton = new JButton("Drop (D)");
    examineButton = new JButton("Examine (X)");

    // Status labels
    healthLabel = new JLabel("Health: " + player.getHealth());
    scoreLabel = new JLabel("Score: " + player.getScore());
    weightLabel = new JLabel("Weight: " + player.getCurrentWeight() + "/" + player.getMaxWeight());

    // Image panel
    imagePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
        //          String pictureName = currentRoom.getPictureName();
        //          if (pictureName != null && !pictureName.isEmpty()) {
        //            ImageIcon icon = getImage(pictureName);
        //            if (icon != null) {
        //              Image img = icon.getImage();
        //              g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        //            }
        //        }
        // Get room's image using the getPicture() method
        BufferedImage roomImage = currentRoom.getPicture();

        // If image exists, draw it
        if (roomImage != null) {
          g.drawImage(roomImage, 0, 0, getWidth(), getHeight(), null);
        } else {
          // Draw a placeholder if no image is available
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          g.drawString("No image available for " + currentRoom.getName(), 10, getHeight() / 2);
        }
      }
    };

    // Item lists
    inventoryList = new JList<>();
    roomItemsList = new JList<>();
  }

  /**
   * Set up the layout of the UI components.
   */
  private void setupLayout() {
    setLayout(new BorderLayout());

    // Top panel - Player status
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(healthLabel);
    topPanel.add(scoreLabel);
    topPanel.add(weightLabel);
    add(topPanel, BorderLayout.NORTH);

    // Center panel - Main game area
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BorderLayout());

    // Room image area
    imagePanel.setPreferredSize(new Dimension(400, 300));
    centerPanel.add(imagePanel, BorderLayout.NORTH);

    // Room description area
    JScrollPane descScrollPane = new JScrollPane(roomDescription);
    descScrollPane.setPreferredSize(new Dimension(400, 100));
    centerPanel.add(descScrollPane, BorderLayout.CENTER);

    // Message area
    JScrollPane messageScrollPane = new JScrollPane(messageArea);
    messageScrollPane.setPreferredSize(new Dimension(400, 100));
    centerPanel.add(messageScrollPane, BorderLayout.SOUTH);

    add(centerPanel, BorderLayout.CENTER);

    // Right panel - Item lists
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());

    JPanel inventoryPanel = new JPanel(new BorderLayout());
    inventoryPanel.add(new JLabel("Inventory:"), BorderLayout.NORTH);
    inventoryPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

    JPanel roomItemsPanel = new JPanel(new BorderLayout());
    roomItemsPanel.add(new JLabel("Room Items:"), BorderLayout.NORTH);
    roomItemsPanel.add(new JScrollPane(roomItemsList), BorderLayout.CENTER);

    rightPanel.add(inventoryPanel, BorderLayout.NORTH);
    rightPanel.add(roomItemsPanel, BorderLayout.CENTER);

    add(rightPanel, BorderLayout.EAST);

    // Bottom panel - Control area
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());

    // Direction buttons panel
    JPanel directionPanel = new JPanel(new GridLayout(3, 3));
    directionPanel.add(new JLabel(""));
    directionPanel.add(northButton);
    directionPanel.add(new JLabel(""));
    directionPanel.add(westButton);
    directionPanel.add(new JLabel(""));
    directionPanel.add(eastButton);
    directionPanel.add(new JLabel(""));
    directionPanel.add(southButton);
    directionPanel.add(new JLabel(""));

    // Action buttons panel
    JPanel actionPanel = new JPanel(new FlowLayout());
    actionPanel.add(inventoryButton);
    actionPanel.add(lookButton);
    actionPanel.add(useButton);
    actionPanel.add(takeButton);
    actionPanel.add(dropButton);
    actionPanel.add(examineButton);

    // Command input panel
    JPanel commandPanel = new JPanel(new BorderLayout());
    commandPanel.add(new JLabel("Enter command:"), BorderLayout.WEST);
    commandPanel.add(commandInput, BorderLayout.CENTER);
    JButton sendButton = new JButton("Send");
    sendButton.addActionListener(e -> {
      if (!commandInput.getText().isEmpty()) {
        processCommand(commandInput.getText());
        commandInput.setText("");
      }
    });
    commandPanel.add(sendButton, BorderLayout.EAST);

    bottomPanel.add(directionPanel, BorderLayout.WEST);
    bottomPanel.add(actionPanel, BorderLayout.CENTER);
    bottomPanel.add(commandPanel, BorderLayout.SOUTH);

    add(bottomPanel, BorderLayout.SOUTH);
  }

  @Override
  public void update() {
    displayRoom();
    displayPlayerStatus();
    updateLists();

    // Update direction button states
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    Map<String, Integer> exits = currentRoom.getExits();

    northButton.setEnabled(exits.containsKey("N") && exits.get("N") > 0);
    southButton.setEnabled(exits.containsKey("S") && exits.get("S") > 0);
    eastButton.setEnabled(exits.containsKey("E") && exits.get("E") > 0);
    westButton.setEnabled(exits.containsKey("W") && exits.get("W") > 0);

    // Repaint image panel
    imagePanel.repaint();
  }

  @Override
  public void showText(String text) {
    messageArea.append(text + "\n");
    // Automatically scroll to bottom
    messageArea.setCaretPosition(messageArea.getDocument().getLength());
  }

  @Override
  public void displayRoom() {
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    roomDescription.setText("Current location: " + currentRoom.getName() + "\n\n" +
            currentRoom.getDescription());
  }

  @Override
  public void displayPlayerStatus() {
    healthLabel.setText("Health: " + player.getHealth());
    scoreLabel.setText("Score: " + player.getScore());
    weightLabel.setText("Weight: " + player.getCurrentWeight() + "/" + player.getMaxWeight());
  }

  /**
   * Update the item lists.
   */
  private void updateLists() {
    // Update inventory list
    Map<String, ? extends Object> playerItems = player.getEntities();
    String[] playerItemsArray = playerItems.keySet().toArray(new String[0]);
    inventoryList.setListData(playerItemsArray);

    // Update room items list
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    String itemsInRoom = currentRoom.getElementNames(Item.class);
    String[] roomItemsArray = itemsInRoom.isEmpty() ?
            new String[0] : itemsInRoom.split(", ");
    roomItemsList.setListData(roomItemsArray);
  }

  @Override
  public void displayCommandPrompt() {
    // Not needed for graphical interface as buttons and input field provide the interface
  }

  /**
   * Get an image, loading it if not cached.
   *
   * @param filename The image filename
   * @return The image icon
   */
  private ImageIcon getImage(String filename) {
    if (!imageCache.containsKey(filename)) {
      try {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + filename));
        imageCache.put(filename, icon);
        return icon;
      } catch (Exception e) {
        System.err.println("Could not load image: " + filename);
        return null;
      }
    }
    return imageCache.get(filename);
  }

  @Override
  public void startInputHandling() {
    this.inputHandlingActive = true;
    // Add event listeners for UI components
    addEventListeners();
  }

  @Override
  public void stopInputHandling() {
    this.inputHandlingActive = false;
    // Remove event listeners
    removeEventListeners();
  }

  @Override
  public void processCommand(String command) {
    if (inputHandlingActive) {
      controller.processCommand(command);
    }
  }

  /**
   * Add event listeners to UI components.
   */
  private void addEventListeners() {
    northButton.addActionListener(this);
    southButton.addActionListener(this);
    eastButton.addActionListener(this);
    westButton.addActionListener(this);
    inventoryButton.addActionListener(this);
    lookButton.addActionListener(this);
    useButton.addActionListener(this);
    takeButton.addActionListener(this);
    dropButton.addActionListener(this);
    examineButton.addActionListener(this);

    commandInput.addActionListener(e -> {
      if (!commandInput.getText().isEmpty()) {
        processCommand(commandInput.getText());
        commandInput.setText("");
      }
    });
  }

  /**
   * Remove event listeners from UI components.
   */
  private void removeEventListeners() {

    northButton.removeActionListener(this);
    southButton.removeActionListener(this);
    eastButton.removeActionListener(this);
    westButton.removeActionListener(this);
    inventoryButton.removeActionListener(this);
    lookButton.removeActionListener(this);
    useButton.removeActionListener(this);
    takeButton.removeActionListener(this);
    dropButton.removeActionListener(this);
    examineButton.removeActionListener(this);

    // Remove action listener from command input
    for (ActionListener al : commandInput.getActionListeners()) {
      commandInput.removeActionListener(al);
    }
  }

  /**
   * Handle button click events.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!inputHandlingActive) {
      return;
    }

    Object source = e.getSource();

    if (source == northButton) {
      processCommand("N");
    } else if (source == southButton) {
      processCommand("S");
    } else if (source == eastButton) {
      processCommand("E");
    } else if (source == westButton) {
      processCommand("W");
    } else if (source == inventoryButton) {
      processCommand("I");
    } else if (source == lookButton) {
      processCommand("L");
    } else if (source == useButton) {
      String selected = inventoryList.getSelectedValue();
      if (selected != null) {
        processCommand("U " + selected);
      } else {
        showText("Please select an item to use first");
      }
    } else if (source == takeButton) {
      String selected = roomItemsList.getSelectedValue();
      if (selected != null) {
        processCommand("T " + selected);
      } else {
        showText("Please select an item to take first");
      }
    } else if (source == dropButton) {
      String selected = inventoryList.getSelectedValue();
      if (selected != null) {
        processCommand("D " + selected);
      } else {
        showText("Please select an item to drop first");
      }
    } else if (source == examineButton) {
      String selected = inventoryList.getSelectedValue();
      if (selected != null) {
        processCommand("X " + selected);
      } else {
        selected = roomItemsList.getSelectedValue();
        if (selected != null) {
          processCommand("X " + selected);
        } else {
          showText("Please select an item to examine first");
        }
      }
    }
  }
}