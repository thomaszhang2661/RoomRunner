package enginedriver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

public class PlayerTest {
  private Player player;
  private Item lamp, thumbDrive, carrot, goldenTicket;

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
            5, 10, "You apply the Mod 2 operator " +
            "and take note of the remainder.");
    goldenTicket = new Item("Golden Ticket",
            "A Golden Ticket inviting your team to the Architectural " +
                    "Review and Code Walk with our TA Team.", 1, 1, 5,
            10, "You read the Golden Ticket and mark your calendar. 'We " +
            "cannot miss our code review!', you happily shout as you place it in the slot.");
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
    assertEquals(HEALTH_STATUS.SLEEP, player.checkStatus(), "Health at 0 should be SLEEP");

    player.gainOrLoseHealth(20);
    assertEquals(HEALTH_STATUS.WOOZY, player.checkStatus(), "Health at 20 should be WOOZY");

    player.gainOrLoseHealth(30);
    assertEquals(HEALTH_STATUS.FATIGUED, player.checkStatus(), "Health at 50 should be FATIGUED");

    player.gainOrLoseHealth(30);
    assertEquals(HEALTH_STATUS.AWAKE, player.checkStatus(), "Health at 80 should be AWAKE");
  }


  @Test
  public void testSetRoomNumber() {
    player.setRoomNumber(5);
    assertEquals(5, player.getRoomNumber());
  }
}


