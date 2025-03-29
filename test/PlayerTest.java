import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import enginedriver.HEALTH_STATUS;
import enginedriver.Item;
import enginedriver.Player;

public class PlayerTest {
  private Player player;
  private Item lamp, thumbDrive, carrot;

  @Before
  public void setUp() {
    player = new Player("TestPlayer", 100, 20);

    lamp = new Item("Lamp", "An old oil lamp with flint to spark.", 100,
            20, 100, 3, "You light the lamp with the flint.");
    thumbDrive = new Item("Thumb Drive", "A USB thumb drive for computers",
            1000, 1000, 150, 1,
            "You insert the thumb drive.");
    carrot = new Item("Carrot", "A carrot. But not just any carrot, " +
            "but a HUGE carrot! Bigger than you've seen before.", 1, 1,
            5, 50, "You apply the Mod 2 operator " +
            "and take note of the remainder.");

    // Initialize player with a starting inventory
    Map<String, Item> items = new HashMap<>();
    items.put(lamp.getName(), lamp);
    items.put(thumbDrive.getName(), thumbDrive);

    player = new Player("Hero", 100, 50, items);
  }

  @Test
  public void testGainOrLoseHealth() {
    player.gainOrLoseHealth(-30);
    assertEquals(70, player.getHealth());
    player.gainOrLoseHealth(-80);
    assertEquals(0, player.getHealth()); // Health should not be negative
  }

  @Test
  public void testCheckHealthStatus() {
    // Start with full health
    player.gainOrLoseHealth(-player.getHealth()); // Reset health to 0
    Assertions.assertEquals(HEALTH_STATUS.SLEEP, player.checkStatus(), "Health at 0 should be SLEEP");

    player.gainOrLoseHealth(20);
    assertEquals(HEALTH_STATUS.WOOZY, player.checkStatus(), "Health at 20 should be WOOZY");

    player.gainOrLoseHealth(30);
    assertEquals(HEALTH_STATUS.FATIGUED, player.checkStatus(),
            "Health at 50 should be FATIGUED");

    player.gainOrLoseHealth(30);
    assertEquals(HEALTH_STATUS.AWAKE, player.checkStatus(), "Health at 80 should be AWAKE");
  }


  @Test
  public void testSetRoomNumber() {
    player.setRoomNumber(5);
    assertEquals(5, player.getRoomNumber());
  }


  @Test
  public void testRemoveItemSuccess() {
    // Verify initial weight
    assertEquals(4, player.getCurrentWeight(), "Initial weight should be the " +
            "sum of lamp (3) and thumb drive (1).");

    // Remove the lamp and verify
    boolean removedLamp = player.removeItem(lamp); // Method under test
    assertTrue(removedLamp, "Lamp should be successfully removed.");
    assertEquals(1, player.getCurrentWeight(), "Weight should " +
            "decrease by the sword's weight (3).");

    // Remove the thumb drive and verify
    boolean removedThumbDrive = player.removeItem(thumbDrive); // Method under test
    assertTrue(removedThumbDrive, "Thumb drive should be successfully removed.");
    assertEquals(0, player.getCurrentWeight(), "Weight should be 0 " +
            "after removing all items.");
  }

  @Test
  public void testAddItemWithinWeightLimit() {
    // Create an item
    Item key = new Item("Key", "A medium-sized key. Looks like it may " +
            "unlock a cabinet or desk.", 3, 3, 5, 1,
            "You insert the key and turn it. 'Click!'");

    // Add the item to inventory
    boolean added = player.addItem(key);
    assertTrue(added, "Key should be added to the inventory within the weight limit.");

    // Ensure the item exists in the inventory
    assertTrue(player.getEntities().containsKey("Key"), "Inventory should contain the Key.");

    // Verify the current weight is updated
    assertEquals(5, player.getCurrentWeight(), "Current weight should " +
                                                      "include the weight of the Key (1).");
  }

  @Test
  public void testAddItemOutsideWeightLimit() {

    // Attempt to add the item to the inventory
    boolean added = player.addItem(carrot);

    // Ensure the item is not added to the inventory
    assertFalse(added, "Carrot should not be added as it exceeds the weight limit.");

    // Ensure the item does not exist in the inventory
    assertFalse(player.getEntities().containsKey("Carrot"), "Inventory should not " +
                                                                  "contain the Carrot.");

    // Verify the current weight remains unchanged
    assertEquals(4, player.getCurrentWeight(), "Current weight should remain " +
            "unchanged when adding an item exceeds the weight limit.");
  }

}

