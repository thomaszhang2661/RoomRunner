package enginedriver.view;

import enginedriver.GameController;
import enginedriver.Item;
import enginedriver.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * Panel for displaying and interacting with the player's inventory.
 */
public class InventoryPanel extends JPanel {
  private final GameController controller;
  private final JList<String> inventoryList;
  private final DefaultListModel<String> inventoryModel;
  private final JLabel weightLabel;

  /**
   * Constructor for InventoryPanel.
   *
   * @param controller the game controller
   */
  public InventoryPanel(GameController controller) {
    this.controller = controller;

    // Set up the panel
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    setPreferredSize(new Dimension(250, 300));

    // Create list model and JList
    inventoryModel = new DefaultListModel<>();
    inventoryList = new JList<>(inventoryModel);
    inventoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    inventoryList.setFont(new Font("SansSerif", Font.PLAIN, 14));
    inventoryList.setBackground(new Color(250, 250, 240));
    inventoryList.setBorder(new EmptyBorder(5, 5, 5, 5));

    // Add right-click popup menu
    JPopupMenu popupMenu = createPopupMenu();
    inventoryList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
          // Select the item under the mouse pointer
          int index = inventoryList.locationToIndex(e.getPoint());
          if (index >= 0) {
            inventoryList.setSelectedIndex(index);
            popupMenu.show(inventoryList, e.getX(), e.getY());
          }
        } else if (e.getClickCount() == 2) {
          // Double-click to examine item
          examineSelectedItem();
        }
      }
    });

    // Create scroll pane for inventory list
    JScrollPane scrollPane = new JScrollPane(inventoryList);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Items"));

    // Create weight display panel
    JPanel weightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    weightLabel = new JLabel("Weight: 0/0");
    weightPanel.add(weightLabel);

    // Add components to panel
    add(scrollPane, BorderLayout.CENTER);
    add(weightPanel, BorderLayout.SOUTH);

    // Initial update
    updateUI();
  }

  /**
   * Create the right-click popup menu for inventory items.
   *
   * @return the popup menu
   */
  private JPopupMenu createPopupMenu() {
    JPopupMenu menu = new JPopupMenu();

    JMenuItem examineItem = new JMenuItem("Examine");
    examineItem.addActionListener(e -> examineSelectedItem());

    JMenuItem useItem = new JMenuItem("Use");
    useItem.addActionListener(e -> useSelectedItem());

    JMenuItem dropItem = new JMenuItem("Drop");
    dropItem.addActionListener(e -> dropSelectedItem());

    menu.add(examineItem);
    menu.add(useItem);
    menu.add(dropItem);

    return menu;
  }

  /**
   * Examine the currently selected inventory item.
   */
  private void examineSelectedItem() {
    String selectedItem = inventoryList.getSelectedValue();
    if (selectedItem != null) {
      controller.processCommand("X " + selectedItem);
    }
  }

  /**
   * Use the currently selected inventory item.
   */
  private void useSelectedItem() {
    String selectedItem = inventoryList.getSelectedValue();
    if (selectedItem != null) {
      controller.processCommand("U " + selectedItem);
    }
  }

  /**
   * Drop the currently selected inventory item.
   */
  private void dropSelectedItem() {
    String selectedItem = inventoryList.getSelectedValue();
    if (selectedItem != null) {
      controller.processCommand("D " + selectedItem);
    }
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
        // Clear current list
        inventoryModel.clear();

        // Add all items from player's inventory
        Map<String, Item> items = player.getEntities();
        for (String itemName : items.keySet()) {
          inventoryModel.addElement(itemName);
        }

        // Update weight display
        updateWeightLabel(player);
      }
    }
  }

  /**
   * Update the weight display label.
   *
   * @param player the player
   */
  private void updateWeightLabel(Player player) {
    int currentWeight = player.getCurrentWeight();
    int maxWeight = player.getMaxWeight();
    weightLabel.setText(String.format("Weight: %d/%d", currentWeight, maxWeight));

    // Colorize based on capacity
    float ratio = (float) currentWeight / maxWeight;
    if (ratio > 0.9f) {
      weightLabel.setForeground(Color.RED);
    } else if (ratio > 0.7f) {
      weightLabel.setForeground(new Color(200, 120, 0)); // Orange
    } else {
      weightLabel.setForeground(Color.BLACK);
    }
  }
}