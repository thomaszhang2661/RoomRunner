import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import enginedriver.Player;
import enginedriver.problems.Monster;
import enginedriver.problems.validator.StringSolutionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


class MonsterTest {
  private Monster<String> rabbit;
  private Monster<String> teddyBear;
  private Player player;

  @BeforeEach
  void setUp() {
    // Create a player for testing
    player = new Player("Hero", 100, 50);

    // Initialize Rabbit from JSON data
    rabbit = new Monster<>(
            "Rabbit",
            "Awww. A furry rabbit twitching its nose and eating a carrot. "
                    + "Makes you want to pet him",
            true, // Active
            true, // Affects target
            true, // Can attack
            true, // Affects player
            "Carrot", // Solution
            300, // Value
            -15, // Damage
            "A monster Rabbit moves towards you! He's blocking the way north.\n"
                    + "I think you might be dinner!",
            "7:Dining Room",
            "monster-rabbit.png",
            "licks you with a giant tongue!",
            new StringSolutionValidator()
    );

    // Initialize Teddy Bear from JSON data
    teddyBear = new Monster<>(
            "Teddy Bear",
            "A peaceful, cute-looking teddy bear with its hair clipped sits on the floor",
            true, // Active
            true, // Affects target
            true, // Can attack
            true, // Affects player
            "Hair Clippers", // Solution
            200, // Value
            -5, // Damage
            "A monster Teddy Bear growls at you! You cannot get past!",
            "3:Foyer",
            "monster-teddy.png",
            "hits you with soft, fluffy paws! You might sneeze!",
            new StringSolutionValidator()
    );
  }

  // Test Monster Initialization
  @Test
  void testMonsterInitialization() {
    // Validate Rabbit
    assertEquals("Rabbit", rabbit.getName());
    assertEquals("Awww. A furry rabbit twitching its nose and eating a carrot. "
            + "Makes you want to pet him", rabbit.getDescription());
    assertTrue(rabbit.getActive());
    assertEquals("Carrot", rabbit.getSolution());
    assertEquals(300, rabbit.getValue());
    assertEquals(-15, rabbit.getDamage());
    assertEquals("7:Dining Room", rabbit.getTarget());
    assertEquals("licks you with a giant tongue!", rabbit.getAttack());

    // Validate Teddy Bear
    assertEquals("Teddy Bear", teddyBear.getName());
    assertEquals("A peaceful, cute-looking teddy bear with its hair clipped "
            + "sits on the floor", teddyBear.getDescription());
    assertTrue(teddyBear.getActive());
    assertEquals("Hair Clippers", teddyBear.getSolution());
    assertEquals(200, teddyBear.getValue());
    assertEquals(-5, teddyBear.getDamage());
    assertEquals("3:Foyer", teddyBear.getTarget());
    assertEquals("hits you with soft, fluffy paws! You might sneeze!",
            teddyBear.getAttack());
  }

  /**
   * Test the getID method of Monster.
   */
  @Test
  void testMonsterGetId() {
    // Validate Rabbit ID
    assertEquals(-1, rabbit.getId());

    // Validate Teddy Bear ID
    assertEquals(-1, teddyBear.getId());
  }

  /**
   * Test the getName method of Monster.
   */
  @Test
  void testMonsterGetName() {
    // Validate Rabbit Name
    assertEquals("Rabbit", rabbit.getName());

    // Validate Teddy Bear Name
    assertEquals("Teddy Bear", teddyBear.getName());
  }

  /**
   * Test the getEffects method of Monster.
   */
  @Test
  void testMonsterGetEffects() {
    // Validate Rabbit Effects
    assertEquals("A monster Rabbit moves towards you! He's blocking the way north.\n"
            + "I think you might be dinner!", rabbit.getEffects());

    // Validate Teddy Bear Effects
    assertEquals("A monster Teddy Bear growls at you! You cannot get past!",
            teddyBear.getEffects());
  }


  // 图片比对方法（逐像素比对）
  private boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
    if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
      return false;
    }

    for (int x = 0; x < imgA.getWidth(); x++) {
      for (int y = 0; y < imgA.getHeight(); y++) {
        if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
          return false;
        }
      }
    }
    return true;
  }
  /**
   * Test getPicture method of Monster.
   */

  @Test
  void testMonsterGetPicture() {


    // Validate Rabbit Picture
    // read Image from file
    String imageName = "monster-rabbit.png";
    File imageFile = new File("data/images/" + imageName);
    BufferedImage expectedImage = null;
    // show picture
    try {
      expectedImage = ImageIO.read(imageFile);
    } catch (Exception e) {
      e.printStackTrace();
    }

    assertTrue(compareImages(expectedImage, rabbit.getPicture()), "图片内容不匹配！");



    // Validate Rabbit Picture
    // read Image from file
    imageName = "monster-teddy.png";
    imageFile = new File("data/images/" + imageName);
    expectedImage = null;
    // show picture
    try {
      expectedImage = ImageIO.read(imageFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Validate Teddy Bear Picture
    assertTrue(compareImages(expectedImage, teddyBear.getPicture()), "图片内容不匹配！");
  }

  // Test Solve with Correct Solution
  @Test
  void testSolveCorrectSolution() {
    // Solve Rabbit
    boolean rabbitSolved = rabbit.solve("Carrot");
    assertTrue(rabbitSolved, "Rabbit should be solved with the correct solution.");
    assertFalse(rabbit.getActive(), "Rabbit should become inactive after solving.");

    // Solve Teddy Bear
    boolean teddyBearSolved = teddyBear.solve("Hair Clippers");
    assertTrue(teddyBearSolved, "Teddy Bear should be solved with the correct solution.");
    assertFalse(teddyBear.getActive(), "Teddy Bear should become inactive after solving.");
  }

  // Test Solve with Incorrect Solution
  @Test
  void testSolveIncorrectSolution() {
    // Incorrect solution for Rabbit
    boolean rabbitSolved = rabbit.solve("WrongSolution");
    assertFalse(rabbitSolved, "Rabbit should not be solved with an incorrect solution.");
    assertTrue(rabbit.getActive(), "Rabbit should remain active after failed solution.");

    // Incorrect solution for Teddy Bear
    boolean teddyBearSolved = teddyBear.solve("WrongSolution");
    assertFalse(teddyBearSolved, "Teddy Bear should not be "
            + "solved with an incorrect solution.");
    assertTrue(teddyBear.getActive(), "Teddy Bear should "
            + "remain active after failed solution.");
  }

  // Test Monster Attack on Player
  @Test
  void testMonsterAttack() {
    // Verify initial player health
    assertEquals(100, player.getHealth(), "Player should start with 100 health.");

    // Rabbit attacks the player
    rabbit.attack(player);
    assertEquals(85, player.getHealth(), "Player health should "
            + "decrease by 15 due to Rabbit attack.");

    // Teddy Bear attacks the player
    teddyBear.attack(player);
    assertEquals(80, player.getHealth(), "Player health should "
            + "decrease by 5 due to Teddy Bear attack.");
  }

  // Test Monster Attack When Inactive
  @Test
  void testInactiveMonsterCannotAttack() {
    // Solve Rabbit, making it inactive
    rabbit.solve("Carrot");
    rabbit.attack(player);
    assertEquals(100, player.getHealth(), "Player health should "
            + "remain unchanged when inactive Rabbit attacks.");

    // Solve Teddy Bear, making it inactive
    teddyBear.solve("Hair Clippers");
    teddyBear.attack(player);
    assertEquals(100, player.getHealth(), "Player health should "
            + "remain unchanged when inactive Teddy Bear attacks.");
  }

  /**
   * Test the toString method of Monster.
   */
  @Test
  void testMonsterToString() {
    String expected = "{ \"name\":\"Rabbit\",\"active\":\"true\",\"affects_target\":\"true\","
            + "\"affects_player\":\"true\",\"solution\":\"Carrot\",\"value\":\"300\","
            + "\"description\":\"Awww. A furry rabbit twitching its nose and eating a carrot. "
            + "Makes you want to pet him\",\"effects\":\"A monster Rabbit moves towards you! "
            + "He's blocking the way north.\nI think you might be dinner!\","
            + "\"damage\":\"-15\",\"target\":\"7:Dining Room\",\"can_attack\":\"true\","
            + "\"attack\":\"licks you with a giant tongue!\",\"picture\":\"monster-rabbit.png\" }";

    assertEquals(expected, rabbit.toString());
  }
}