package enginedriver.view;

import enginedriver.GameController;
import enginedriver.model.GameWorld;
import enginedriver.model.IValuable;
import enginedriver.model.IWeightable;
import enginedriver.model.entity.Fixture;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.IProblem;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Puzzle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
  private JTextArea probDescription;
  private JTextArea examineDescription;
  private JTextArea messageArea;
  private JTextField commandInput;
  private JButton northButton, southButton, eastButton, westButton;
  private JButton inventoryButton, lookButton, useButton, takeButton, dropButton,
          examineButton;
  private JLabel healthLabel, scoreLabel, weightLabel;
  private JPanel imagePanel;
  private JPanel examineImagePanel;
  private JPanel probPanel;
  private IdentifiableEntity examineEntity;
  private IProblem problem;

  private JList<String> inventoryList;
  private JList<String> roomItemsList;
  private JList<String> roomFixturesList;

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
    this.inputHandlingActive = false;

    setTitle(gameWorld.getName() + " - Text Adventure Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1600, 1200);
    setLocationRelativeTo(null);
    setJMenuBar(createMenuBar());
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    // File menu
    JMenu fileMenu = new JMenu("Main");
    JMenuItem quitItem = new JMenuItem("Quit");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem restoreItem = new JMenuItem("Restore");
    saveItem.addActionListener(e -> {
      try {
        controller.processCommand("SAVE");
        showText("Game saved successfully.");
      } catch (Exception ex) {
        showText("Error saving game: " + ex.getMessage());
      }
    });
    restoreItem.addActionListener(e -> {
      try {
        controller.processCommand("RESTORE");
        showText("Game restored successfully.");
      } catch (Exception ex) {
        showText("Error restoring game: " + ex.getMessage());
      }
    });
    quitItem.addActionListener(e -> System.exit(0));
    fileMenu.add(quitItem);
    fileMenu.add(saveItem);
    fileMenu.add(restoreItem);




    // ImageIcon icon = new ImageIcon(image);
    JMenuItem aboutItem = new JMenuItem("About");
    aboutItem.addActionListener(e -> showAboutDialog());
    fileMenu.add(aboutItem);

    // Add menus to the MenuBar
    menuBar.add(fileMenu);

    return menuBar;
  }


  private void showAboutDialog() {
    JOptionPane.showMessageDialog(this,
            "Welcome to Align Quest!\nThis is a text-based adventure game.",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
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

    // prob description area
    probDescription = new JTextArea();
    probDescription.setEditable(false);
    probDescription.setLineWrap(true);
    probDescription.setWrapStyleWord(true);



    // examine description area
    examineDescription = new JTextArea();
    examineDescription.setEditable(false);
    examineDescription.setLineWrap(true);
    examineDescription.setWrapStyleWord(true);


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

    imagePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
        BufferedImage roomImage = currentRoom.getPicture();

        if (roomImage != null) {
          int panelWidth = getWidth();
          int panelHeight = getHeight();
          int imageWidth = roomImage.getWidth();
          int imageHeight = roomImage.getHeight();

          // Calculate the scaling factor to preserve aspect ratio
          double widthRatio = (double) panelWidth / imageWidth;
          double heightRatio = (double) panelHeight / imageHeight;
          double scale = Math.min(widthRatio, heightRatio);

          // Calculate the new dimensions
          int scaledWidth = (int) (imageWidth * scale);
          int scaledHeight = (int) (imageHeight * scale);

          // Calculate the position to center the image
          int x = (panelWidth - scaledWidth) / 2;
          int y = (panelHeight - scaledHeight) / 2;

          // Draw the scaled image
          g.drawImage(roomImage, x, y, scaledWidth, scaledHeight, null);
        } else {
          // Draw a placeholder if no image is available
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          g.drawString("No image available for " + currentRoom.getName(), 10, getHeight() / 2);
        }
      }
    };


    //    examineImagePanel = new JPanel() {
    //      @Override
    //      protected void paintComponent(Graphics g) {
    //        super.paintComponent(g);
    //        BufferedImage image = examineImage;
    //        if (image != null) {
    //          int panelWidth = getWidth();
    //          int panelHeight = getHeight();
    //          int imageWidth = image.getWidth();
    //          int imageHeight = image.getHeight();
    //
    //          // Calculate the scaling factor to preserve aspect ratio
    //          double widthRatio = (double) panelWidth / imageWidth;
    //          double heightRatio = (double) panelHeight / imageHeight;
    //          double scale = Math.min(widthRatio, heightRatio);
    //
    //          // Calculate the new dimensions
    //          int scaledWidth = (int) (imageWidth * scale);
    //          int scaledHeight = (int) (imageHeight * scale);
    //
    //          // Calculate the position to center the image
    //          int x = (panelWidth - scaledWidth) / 2;
    //          int y = (panelHeight - scaledHeight) / 2;
    //
    //          // Draw the scaled image
    //          g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
    //        } else {
    //          // Draw a placeholder if no image is available
    //          g.setColor(Color.LIGHT_GRAY);
    //          g.fillRect(0, 0, getWidth(), getHeight());
    //          g.setColor(Color.BLACK);
    //          g.drawString("No image available ", 10, getHeight() / 2);
    //        }
    //      }
    //    };

    examineImagePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (examineEntity == null) {
          // 可以画个灰背景和提示文字
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          g.drawString("No entity selected", 10, getHeight() / 2);
          return;
        }
        BufferedImage examineImage = examineEntity.getPicture();
        if (examineImage != null) {
          int panelWidth = getWidth();
          int panelHeight = getHeight();
          int imageWidth = examineImage.getWidth();
          int imageHeight = examineImage.getHeight();

          double widthRatio = (double) panelWidth / imageWidth;
          double heightRatio = (double) panelHeight / imageHeight;
          double scale = Math.min(widthRatio, heightRatio);

          int scaledWidth = (int) (imageWidth * scale);
          int scaledHeight = (int) (imageHeight * scale);
          int x = (panelWidth - scaledWidth) / 2;
          int y = (panelHeight - scaledHeight) / 2;

          g.drawImage(examineImage, x, y, scaledWidth, scaledHeight, null);
        } else {
          // Draw a placeholder if no image is available
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          //g.drawString("No image selected", 10, getHeight() / 2);
        }
      }
    };


    probPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (problem == null) {
          // 可以画个灰背景和提示文字
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          g.drawString("No Puzzle or Monster here", 10, getHeight() / 2);
          return;
        }
        //checkprob type
        BufferedImage probImage = null;
        if (problem instanceof Monster) {
          probImage =  loadImage("data/images/generic-monster.png");
        } else if (problem instanceof Puzzle) {
          probImage =  loadImage("data/images/generic_puzzle.png");
        }


        if (probImage != null) {
          int panelWidth = getWidth();
          int panelHeight = getHeight();
          int imageWidth = probImage.getWidth();
          int imageHeight = probImage.getHeight();

          double widthRatio = (double) panelWidth / imageWidth;
          double heightRatio = (double) panelHeight / imageHeight;
          double scale = Math.min(widthRatio, heightRatio);

          int scaledWidth = (int) (imageWidth * scale);
          int scaledHeight = (int) (imageHeight * scale);
          int x = (panelWidth - scaledWidth) / 2;
          int y = (panelHeight - scaledHeight) / 2;

          g.drawImage(probImage, x, y, scaledWidth, scaledHeight, null);
        } else {
          // Draw a placeholder if no image is available
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
          g.setColor(Color.BLACK);
          //g.drawString("No image selected", 10, getHeight() / 2);
        }
      }
    };
    // Item lists
    inventoryList = new JList<>();
    roomItemsList = new JList<>();
    roomFixturesList = new JList<>();
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
    JPanel centerNorthPanel = new JPanel();
    centerNorthPanel.setLayout(new BorderLayout());
    centerNorthPanel.add(imagePanel, BorderLayout.CENTER);
    probPanel.setPreferredSize(new Dimension(200, 200));

    centerNorthPanel.add(probPanel, BorderLayout.EAST);

    JScrollPane probScrollPane = new JScrollPane(probDescription);
    probScrollPane.setPreferredSize(new Dimension(200, 100));
    centerNorthPanel.add(probScrollPane, BorderLayout.WEST);

    centerPanel.add(centerNorthPanel, BorderLayout.NORTH);

    // Room description area
    JScrollPane descScrollPane = new JScrollPane(roomDescription);
    descScrollPane.setPreferredSize(new Dimension(400, 100));
    centerPanel.add(descScrollPane, BorderLayout.CENTER);



    // Message area
    JScrollPane messageScrollPane = new JScrollPane(messageArea);
    messageScrollPane.setPreferredSize(new Dimension(400, 100));
    centerPanel.add(messageScrollPane, BorderLayout.SOUTH);
    add(centerPanel, BorderLayout.CENTER);


    // CenterLeft panel - problem area
    JPanel centerLeftPanel = new JPanel();
    centerLeftPanel.setLayout(new BorderLayout());




    // left panel - examine  area
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout());
    // exaime  image area
    examineImagePanel.setPreferredSize(new Dimension(200, 200));
    leftPanel.add(examineImagePanel, BorderLayout.NORTH);

    // examine description area
    JScrollPane descExamePane = new JScrollPane(examineDescription);
    descExamePane.setPreferredSize(new Dimension(200, 200));
    leftPanel.add(descExamePane, BorderLayout.CENTER);
    add(leftPanel, BorderLayout.WEST);




    // Right panel - Item lists
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());

    JPanel inventoryPanel = new JPanel(new BorderLayout());
    inventoryPanel.add(new JLabel("Inventory:"), BorderLayout.NORTH);
    inventoryPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

    JPanel roomItemsPanel = new JPanel(new BorderLayout());
    roomItemsPanel.add(new JLabel("Room Items:"), BorderLayout.NORTH);
    roomItemsPanel.add(new JScrollPane(roomItemsList), BorderLayout.CENTER);

    JPanel roomFixturesPanel = new JPanel(new BorderLayout());
    roomFixturesPanel.add(new JLabel("Room Fixtures:"), BorderLayout.NORTH);
    roomFixturesPanel.add(new JScrollPane(roomFixturesList), BorderLayout.CENTER);


    rightPanel.add(inventoryPanel, BorderLayout.NORTH);
    rightPanel.add(roomItemsPanel, BorderLayout.CENTER);
    rightPanel.add(roomFixturesPanel, BorderLayout.SOUTH);


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

  /**
   *  show Picture
   */
  public void showEntity(IdentifiableEntity entity) {
    this.examineEntity = entity;
    String output = entity.getDescription() + "\n";

    if (entity instanceof IWeightable) {
      output += "Weight: " + ((IWeightable) entity).getWeight() + "\n";
    }

    if (entity instanceof IValuable) {
      output += "Value: " + ((IValuable) entity).getValue() + "\n";
    }

    examineDescription.setText(output);
    //??????
    examineImagePanel.repaint(); //

  }


  @Override
  public void showProblem(IProblem problem) {
    this.problem = problem;
    // if null
    if (this.problem == null) {
      probPanel.repaint();
      probDescription.setText("No Puzzle or Monster here");
      return;
    }
    String output = problem.getDescription() + "\n";

    if (problem instanceof Monster) {
      output += "Monster: " + ((Monster) problem).getName() + "\n";
    } else if (problem instanceof Puzzle) {
      output += "Puzzle: " + ((Puzzle) problem).getName() + "\n";
    }

    probDescription.setText(output);
    probPanel.repaint();
  }
  @Override
  public void displayRoom() {
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    roomDescription.setText("Current location: " + currentRoom.getName() + "\n\n" +
            currentRoom.getDescription());
  }


  //  @Override
  //  public void displayProb() {
  //    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
  //    //get problem description
  //    String probDescriptionText = currentRoom.getProblem().getDescription();
  //    // check is puzzle or monster
  //    if (currentRoom.getProblem() instanceof Puzzle) {
  //      probDescriptionText = "Puzzle: " + probDescriptionText;
  //
  //    } else if (currentRoom.getProblem() instanceof Monster) {
  //      probDescriptionText = "Monster: " + probDescriptionText;
  //    }
  //
  //    probDescription.setText(probDescriptionText);
  //    probPanel.repaint();
  //  }

  @Override
  public void displayPlayerStatus() {
    healthLabel.setText("Health: " + player.getHealth());
    scoreLabel.setText("Score: " + player.getScore());
    weightLabel.setText("Weight: " + player.getCurrentWeight() + "/" + player.getMaxWeight());
  }

  /**
   * Update the entity lists.
   */
  private void updateLists() {
    // Update inventory list
    Map<String, Item> playerItems = player.getEntities();
    String[] playerItemsArray = playerItems.keySet().toArray(new String[0]);
    inventoryList.setListData(playerItemsArray);

    // Update room items list
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    String itemsInRoom = currentRoom.getElementNames(Item.class);
    String[] roomItemsArray = itemsInRoom.isEmpty() ?
            new String[0] : itemsInRoom.split(", ");
    roomItemsList.setListData(roomItemsArray);
    // Update room fixtures list
    String fixturesInRoom = currentRoom.getElementNames(Fixture.class);
    String[] roomFixturesArray = fixturesInRoom.isEmpty() ?
            new String[0] : fixturesInRoom.split(", ");
    roomFixturesList.setListData(roomFixturesArray);
  }

  @Override
  public void displayCommandPrompt() {
    // Not needed for graphical interface as buttons and input field provide the interface
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
          selected = roomFixturesList.getSelectedValue();
          if (selected != null) {
            processCommand("X " + selected);
          } else {
            // No item selected for examination
            showText("Please select an item to examine first");
          }
        }
      }
    }
  }

  private BufferedImage loadImage(String filePath) {

    // 加载自定义图标
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(filePath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return image;
    }
}