import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import enginedriver.problems.validator.SolutionValidator;
import enginedriver.problems.validator.StringSolutionValidator;
import  enginedriver.problems.validator.ItemSolutionValidator;
import enginedriver.Fixture;
import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Player;
import enginedriver.Room;
import java.util.HashMap;
import java.util.Map;
import enginedriver.*;
import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test class for GameController.
 */
public class GameControllerTest {

  private GameWorld gameWorld;
  private Player player;
  private GameController gameController;


  @BeforeEach
  void setUp() {
    // Initialize items
    Map<String, Item> items = new HashMap<>();
    items.put("Lamp", new Item("Lamp", "An old oil lamp with flint to spark.",
            100, 20, 100, 3, "You light the lamp with the flint."));
    items.put("Thumb Drive", new Item("Thumb Drive",
            "A USB thumb drive for computers", 1000, 1000, 150, 1, "You insert the thumb drive."));
    items.put("Algorithms Book", new Item("Algorithms Book",
            "A book on computer algorithms, signed by Professor Keith. \nThe inscription reads: \"You'll need this in CS5800. Best wishes to you, Aligner!\n P.S.: Don't forget: a recursive solution needs a base case!", 1000, 1000, 500, 2, "You read the Algo Book and make note of computational theories."));
    items.put("Hair Clippers", new Item("Hair Clippers",
            "Cordless Wahl hair clippers for pets or humans. The battery low light is blinking.", 10, 4, 5, 2, "You activate the clippers. It makes a loud buzzing sound as you use them!"));
    items.put("Carrot", new Item("Carrot",
            "A carrot. But not just any carrot, but a HUGE carrot! Bigger than you've seen before.", 1, 1, 5, 10, "You take a small bite the carrot and then share it."));
    items.put("Modulo 2", new Item("Modulo 2",
            "A old school floppy disk. The kind your parents used. \nIt has \"Computer Science (number mod 2) operator. Use in case of emergencies\" inscribed on it. Wait - it just morphed into a cloud. Hold on - it just turned back into a disk. Weird!",
            10, 10, 500, 1,
            "You apply the Mod 2 operator and take note of the remainder."));
    items.put("Golden Ticket", new Item("Golden Ticket", "A Golden Ticket inviting your team to the Architectural Review and Code Walk with our TA Team.", 1, 1, 5, 10, "You read the Golden Ticket and mark your calendar. 'We cannot miss our code review!', you happily shout as you place it in the slot."));
    items.put("Key", new Item("Key", "A medium-sized key. Looks like it may unlock a cabinet or desk.", 3, 3, 5, 1, "You insert the key and turn it. 'Click!'"));
    items.put("Frying Pan", new Item("Frying Pan", "A stainless steel skillet for frying.", 10, 10, 0, 3, "Sizzle, sizzle...bonk!"));
    items.put("Note", new Item("Note", "It's a personal note to you from Professor K. \nYou read the note: 'I am very proud of you!'\nWhoop Whoop! You High-5 your team and ride off into the sunset.", 1000, 1000, 100, 0, "You read the note. It's from Professor K: 'I am very proud of you!'\nWhoop Whoop! You High-5 your team and ride off into the sunset."));

    // Initialize fixtures
    Map<String, Fixture> fixtures = new HashMap<>();
    fixtures.put("Desk", new Fixture("Desk", "An old wooden desk with a mess of papers. A note "
            + "says: \"use thumb drive!\"", 1000));
    fixtures.put("Laptop", new Fixture("Laptop", "A ZacPro 5000 with all the latest features and a USB slot. It's bolted to the table and can't move", 1000));
    fixtures.put("Professor Keith", new Fixture("Professor Keith", "Professor Keith, Faculty Director of Align Boston. He smiles at you and gives a thumb's up. \"Great job! You can do this!\"", 1000));
    fixtures.put("Billboard", new Fixture("Billboard", "A large billboard looms in the distance. \nIt's hard to read but seems to say 'Welcome to Align Quest, adventurer! Enjoy the exploration! - Prof. K'", 1000));
    fixtures.put("Chandelier", new Fixture("Chandelier", "A large and heavy chandelier hangs from the ceiling. \nThe light from it is dim, and it is too high to reach without using a mechanical lift.", 1000));
    fixtures.put("Bookshelf", new Fixture("Bookshelf", "A tall black oak bookshelf in the corner of the room. \nIt's much too heavy to move...but there's something weird about it and the books seem to tremble on their own", 1000));
    fixtures.put("Monitor", new Fixture("Monitor", "A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", 1000));
    fixtures.put("Monitors", new Fixture("Monitors", "A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", 1000));
    fixtures.put("Stove", new Fixture("Stove", "A large propane stove. You like electric ones, but this wasn't your decision, obviously.", 1000));
    fixtures.put("Whiteboard", new Fixture("Whiteboard", "A large whiteboard mounted to the wall. Some UML class and sequence diagrams are scattered on it, in various colors", 1000));


    // Initialize monsters
    Map<String, Monster> monsters = new HashMap<>();
    String solutionName = "Carrot";
    Item solution = items.get(solutionName);
    monsters.put("Rabbit", new Monster("Rabbit",
            "Awww. A furry rabbit twitching its nose and eating a carrot. Makes you want to pet him",
            true, true, true, true,
            solution, 300, -15,
            "A monster Rabbit moves towards you! He's blocking the way north."
                    + " \nI think you might be dinner!", "7:Dining Room",
            "monster-rabbit.png", "licks you with a giant tongue!", new ItemSolutionValidator()));

    solutionName = "Hair Clippers";
    solution = items.get(solutionName);
    monsters.put("Teddy Bear", new Monster<Item>("Teddy Bear",
            "A peaceful, cute-looking teddy bear with its hair clipped sits on the floor",
            true, true, true, true,
            solution, 200, -5,
            "A monster Teddy Bear growls at you! You cannot get past!", "3:Foyer",
            "monster-teddy.png", "hits you with soft, fluffy paws! You might sneeze!",
            new ItemSolutionValidator()));

    // Initialize puzzles
    Map<String, Puzzle> puzzles = new HashMap<>();

    solutionName = "Lamp";
    solution = items.get(solutionName);
    puzzles.put("DARKNESS", new Puzzle<Item>("DARKNESS", "Darkness! You cannot see!", true, true,
              true, solution,
              150, "Darkness! You cannot see!", "6:Kitchen", "darkness.png",
              new ItemSolutionValidator()));

    solutionName = "Modulo 2";
    solution = items.get(solutionName);
    puzzles.put("MOD-SPOOKY-VOICE", new Puzzle<Item>(
            "MOD-SPOOKY-VOICE",
            "An spooky, eerie library. You walked into this eerie library FROM the west. \nAnother room is north. Books are rustling by themselves on a bookshelf.", true, true,
              false, solution,
              400,
            "Books are rustling by themselves on the bookshelf. That's a weird bookshelf. Really weird.\nYou hear a voice whisper: \"~Find Even Numbers Only~\" \nYikes. That's creepy. Maybe we should leave?", "4:Spooky Library",
            "", new ItemSolutionValidator()));

    solutionName = "'Base Case'";
    puzzles.put("RECURSION-PUZZLE", new Puzzle<String>("RECURSION-PUZZLE", "I feel like we've " +
            "been here before - not quite an infinite loop but infinite...!", true, true,
              false, solutionName,
              700, "Every time you move, you end up in the same room!\nSomething is etched into the wall with a knife. It says: 'Provide a solution to end the recursive madness'", "10:Recursive Study",
            "", new StringSolutionValidator()));

    solutionName = "Thumb Drive";
    solution = items.get(solutionName);
    puzzles.put("USB", new Puzzle<Item>("USB", "A laptop with a USB port", true, true,
              false, solution,
              500, "This is a quiet living room. \nThe dining area is to your south and another " +
            "room to your east but there seems to be an invisible barrier blocking you from going in that direction. \nA dimly lit laptop is on a small table in the corner", "9:Living Room", null,
              new ItemSolutionValidator()));

    solutionName = "Golden Ticket";
    solution = items.get(solutionName);
    puzzles.put("ROBOT", new Puzzle<Item>("ROBOT", "A large robot with lights flashing, humming a tune from a cartoon. You can't quite remember the song. \nThere's a slot on it's front side it reads: 'Insert Here'", true, true,
              false, solution,
              250, "This room is pretty much empty, except for a large robot in the corner, " +
            "making a humming sound. \nIt's annoying! Turn it off!\nThere's a slot on it's front side it reads: 'INSERT TICKET HERE'", "13:Bridge Exit", "robot.png",
            new ItemSolutionValidator()));

    // Initialize rooms
    Map<Integer, Room> rooms = new HashMap<>();

    Map<String, Integer> exits = new HashMap<>();
    exits.put("N", 2);
    exits.put("S", 0);
    exits.put("E", 0);
    exits.put("W", 0);

    Item currentItem = items.get("Hair Clippers");
    Fixture currentFixture = fixtures.get("Billboard");
    Map<String,IdentifiableEntity> entities = new HashMap<>();
    entities.put("Hair Clippers", currentItem);
    entities.put("Billboard", currentFixture);

    rooms.put(1, new Room<Puzzle<?>>(1, "Courtyard",
            "A beautiful courtyard with flowers on "
                    + "both sides of the stone walkway. "
                    + "\nThe walkway leads north. A billboard is in the "
                    + "distance.",
            exits,entities,
            puzzles.get("DARKNESS"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 3);
    exits.put("S", 1);
    exits.put("E", 0);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Thumb Drive", items.get("Thumb Drive"));
    entities.put("Chandelier", fixtures.get("Chandelier"));
    rooms.put(2, new Room<Puzzle<?>>(2, "Mansion Entrance",
            "Entrance to an old, musty-smelling mansion. Some people have entered, to never return. \nThe door to the north is open. The courtyard is to your south and a foyer to your north. A chandelier hangs from the ceiling.",
            exits, entities, null,""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 0);
    exits.put("S", 1);
    exits.put("E", 4);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Teddy Bear", monsters.get("Teddy Bear"));
    entities.put("Lamp", items.get("Lamp"));
    rooms.put(3, new Room<Monster<?>>(3, "Foyer",
            "The foyer of the mansion. A staircase leads upstairs but it is dilapidated and unusable. Therefore, the only exits to the south and east. \nA strange breeze moves through the room. Eastward is a small room that looks like a library. A teddy bear lies on the floor with its hair shaved",
            exits, entities, monsters.get("Teddy Bear"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 6);
    exits.put("S", 0);
    exits.put("E", -5);
    exits.put("W", 3);
    //update entities
    entities = new HashMap<>();
    entities.put("Key", items.get("Key"));
    entities.put("Bookshelf", fixtures.get("Bookshelf"));
    rooms.put(4, new Room<Puzzle<?>>(4, "Spooky Library",
            "You walked into this eerie library FROM the west. Another room is north. \nBooks now on the floor revealing a secret passage to the East!",
            exits, entities, puzzles.get("MOD-SPOOKY-VOICE"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 0);
    exits.put("S", 0);
    exits.put("E", 0);
    exits.put("W", 4);
    //update entities
    entities = new HashMap<>();
    entities.put("Algorithms Book", items.get("Algorithms Book"));
    entities.put("Desk", fixtures.get("Desk"));
    rooms.put(5, new Room<Monster<?>>(5, "Hidden Chamber",
            "You've found a hidden chamber! It's a small room that's barely the size of a closet. \nThere's an old desk here. It looks like no one has sat at it in a LONG time.",
            exits, entities, null,""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 0);
    exits.put("S", 4);
    exits.put("E", 0);
    exits.put("W", 7);
    //update entities
    entities = new HashMap<>();
    entities.put("Stove", fixtures.get("Stove"));
    rooms.put(6, new Room<Puzzle<?>>(6, "Kitchen",
            "This is a spotless kitchen. Pots and pans are hanging from the ceiling. A stove is here. Stainless steel appliances are everywhere. \nOut of the corner of your eye, you think you see something moving to your west.",
            exits, entities, puzzles.get("DARKNESS"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", -9);
    exits.put("S", 8);
    exits.put("E", 6);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Frying Pan", items.get("Frying Pan"));
    entities.put("Rabbit", monsters.get("Rabbit"));
    rooms.put(7, new Room<Monster<?>>(7, "Dining Room",
            "A large dining room with a Mahogany wood table. \nA cute furry rabbit is on the table eating carrots.",
            exits, entities, monsters.get("Rabbit"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 7);
    exits.put("S", 0);
    exits.put("E", 0);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Carrot", items.get("Carrot"));
    rooms.put(8, new Room<Monster<?>>(8, "Giant Plate",
            "Uh oh. You're on a giant plate, full of carrots! \nI think we need to go, NOW!",
            exits, entities, null,""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 0);
    exits.put("S", 7);
    exits.put("E", -10);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Laptop", fixtures.get("Laptop"));
    rooms.put(9, new Room<Puzzle<?>>(9, "Living Room",
            "This a living room. The dining area is to your south and another room to your east. \nThe laptop is now making all kinds of noise. A file on a thumb drive has opened automatically and describes a recursive algorithm.",
            exits, entities, puzzles.get("USB"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 10);
    exits.put("S", 0);
    exits.put("E", -11);
    exits.put("W", -9);
    //update entities
    entities = new HashMap<>();
    entities.put("Professor Keith", fixtures.get("Professor Keith"));
    rooms.put(10, new Room<Puzzle<?>>(10, "Recursive Study",
            "A study/office room. A fake mirror has been shattered showing an opening to the east.",
            exits, entities, puzzles.get("RECURSION-PUZZLE"),""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 12);
    exits.put("S", 11);
    exits.put("E", 10);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Golden Ticket", items.get("Golden Ticket"));
    entities.put("Professor Keith", fixtures.get("Professor Keith"));
    rooms.put(11, new Room<Puzzle<?>>(11, "Align HQ",
            "Align Headquarters. Professor Keith is here working on your architecture review requirements.\n \"Great to see you!\" he says. \"Looks like you're ready to do your code walk!\" \nThere is an open pathway to the north.",
            exits, entities, null,""));

    //update exits
    exits = new HashMap<>();
    exits.put("N", 13);
    exits.put("S", 11);
    exits.put("E", 0);
    exits.put("W", 0);
    //update entities
    entities = new HashMap<>();
    entities.put("Monitor", fixtures.get("Monitor"));
    entities.put("Monitors", fixtures.get("Monitors"));


    // Initialize game world
    gameWorld = new GameWorld("Align Quest", "1.0.9", rooms);

    // Initialize player
    player = new Player("TestPlayer", 100, 10, 0);
    player.setRoomNumber(1);

    // Initialize game controller
    gameController = new GameController(gameWorld, player);
  }

  @Test
  void testMoveNorth() {
    gameController.processCommand("N");
    assertEquals(2, player.getRoomNumber());
  }

  @Test
  void testTakeItem() {
    gameController.processCommand("TAKE Hair Clippers");
    assertTrue(player.getEntities().containsKey("Hair Clippers"));
  }

  @Test
  void testDropItem() {
    gameController.processCommand("TAKE Hair Clippers");
    gameController.processCommand("DROP Hair Clippers");
    assertFalse(player.getEntities().containsKey("Hair Clippers"));
    assertTrue(gameWorld.getRoom(1).getEntities().containsKey("Hair Clippers"));
    player.addItem(gameWorld.getRoom(8).getItem("Carrot"));
    gameController.processCommand("DROP Carrot");
    assertFalse(player.getEntities().containsKey("Carrot"));
    assertTrue(gameWorld.getRoom(1).getEntities().containsKey("Carrot"));
  }

  @Test
  void testExamineItem() {
    gameController.processCommand("EXAMINE Billboard");
    // Assuming viewer.showText() prints to console or logs, we can't assert its output directly
    // We can check if the method runs without exceptions
  }

  @Test
  void testSolvePuzzle() {
    gameController.processCommand("TAKE Key");
    gameController.processCommand("USE Key");
    gameController.processCommand("take Hair Clippers");
    gameController.processCommand("N");
    gameController.processCommand("N");
    gameController.processCommand("USE Hair Clippers");
  }

  @Test
  void testHandleMonster() {
    player.setRoomNumber(7); //get in monster room
    gameController.processCommand("look");
    // add item carrot to player
    player.addItem(gameWorld.getRoom(8).getItem("Carrot"));
    assertTrue(gameWorld.getRoom(7).getProblem().getActive());
    player.setRoomNumber(7);
    gameController.processCommand("USE Carrot");
    assertFalse(gameWorld.getRoom(7).getProblem().getActive());
  }

  @Test
  void testCheckInventory() {
    gameController.processCommand("TAKE Hair Clippers");
    gameController.processCommand("INVENTORY");
    // Assuming viewer.showText() prints to console or logs, we can't assert its output directly
    // We can check if the method runs without exceptions
  }

  @Test
  void testSaveAndRestore() {
    // initial state for checking
    int initialRoom = player.getRoomNumber();
    int initialScore = player.getScore();

    // save, modify state, then restore
    gameController.processCommand("SAVE");
    player.setRoomNumber(initialRoom + 1);
    player.setScore(initialScore + 100);
    gameController.processCommand("RESTORE");

    // ensure original state is restored
    assertEquals(initialRoom, player.getRoomNumber());
    assertEquals(initialScore, player.getScore());
  }
}