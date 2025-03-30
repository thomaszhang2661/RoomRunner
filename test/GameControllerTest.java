import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import enginedriver.Fixture;
import enginedriver.GameController;
import enginedriver.GameWorld;
import enginedriver.Item;
import enginedriver.Monster;
import enginedriver.Player;
import enginedriver.Puzzle;
import enginedriver.Room;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test class for GameController.
 */
public class GameControllerTest {

  private GameWorld gameWorld;
  private Player player;
  private GameController gameController;

  //  {
  //    "name": "Align Quest",
  //          "version": "1.0.9",
  //
  //          "rooms":[
  //    { "room_name":"Courtyard", "room_number": "1",
  //            "description":"A beautiful courtyard with flowers on both sides of the stone walkway. \nThe walkway leads north. A billboard is in the distance.",
  //            "N": "2", "S": "0", "E": "0", "W": "0","puzzle": null, "monster": null, "items": "Hair Clippers", "fixtures": "Billboard","picture": "courtyard.png" },
  //    { "room_name":"Mansion Entrance", "room_number": "2",
  //            "description":"Entrance to an old, musty-smelling mansion. Some people have entered, to never return. \nThe door to the north is open. The courtyard is to your south and a foyer to your north. A chandelier hangs from the ceiling.",
  //            "N": "3", "S": "1", "E": "0", "W": "0","puzzle": null, "monster": null, "items": "Thumb Drive, Modulo 2", "fixtures": "Chandelier", "picture": "entrance.png" },
  //    { "room_name":"Foyer", "room_number": "3",
  //            "description":"The foyer of the mansion. A staircase leads upstairs but it is dilapidated and unusable. Therefore, the only exits to the south and east. \nA strange breeze moves through the room. Eastward is a small room that looks like a library. A teddy bear lies on the floor with its hair shaved",
  //            "N": "0", "S": "2", "E": "4", "W": "0","puzzle": null, "monster": "Teddy Bear", "items": "Lamp", "fixtures": null, "picture": "foyer.png" },
  //    { "room_name":"Spooky Library", "room_number": "4",
  //            "description":"You walked into this eerie library FROM the west. Another room is north. \nBooks now on the floor revealing a secret passage to the East!",
  //            "N": "6", "S": "0", "E": "-5", "W": "3","puzzle": "MOD-SPOOKY-VOICE", "monster": null, "items": "Key", "fixtures": "Bookshelf","picture": "library.png" },
  //    { "room_name":"Hidden Chamber", "room_number": "5",
  //            "description":"You've found a hidden chamber! It's a small room that's barely the size of a closet. \nThere's an old desk here. It looks like no one has sat at it in a LONG time.",
  //            "N": "0", "S": "0", "E": "0", "W": "4","puzzle": null, "monster": null, "items": "Algorithms Book", "fixtures":"Desk", "picture": "secret_room.png" },
  //    { "room_name":"Kitchen", "room_number": "6",
  //            "description":"This is a spotless kitchen. Pots and pans are hanging from the ceiling. A stove is here. Stainless steel appliances are everywhere. \nOut of the corner of your eye, you think you see something moving to your west.",
  //            "N": "0", "S": "4", "E": "0", "W": "7","puzzle": "DARKNESS", "monster": null, "items": null, "fixtures": "Stove", "picture": "kitchen.png" },
  //    { "room_name":"Dining Room", "room_number": "7",
  //            "description":"A large dining room with a Mahogany wood table. \nA cute furry rabbit is on the table eating carrots.",
  //            "N": "-9", "S": "8", "E": "6", "W": "0","puzzle": null, "monster": "Rabbit", "items": "Frying Pan","fixtures": null, "picture": "dining.png" },
  //    { "room_name":"Giant Plate", "room_number": "8",
  //            "description":"Uh oh. You're on a giant plate, full of carrots! \nI think we need to go, NOW!",
  //            "N": "7", "S": "0", "E": "0", "W": "0","puzzle": null, "monster": null, "items": "Carrot", "fixtures": null, "picture": "plate.png" },
  //    { "room_name":"Living Room", "room_number": "9",
  //            "description":"This a living room. The dining area is to your south and another room to your east. \nThe laptop is now making all kinds of noise. A file on a thumb drive has opened automatically and describes a recursive algorithm.",
  //            "N": "0", "S": "7", "E": "-10", "W": "0","puzzle": "USB", "monster": null, "items": null, "fixtures": "Laptop", "picture": "livingroom.png" },
  //    { "room_name":"Recursive Study", "room_number": "10",
  //            "description":"A study/office room. A fake mirror has been shattered showing an opening to the east.",
  //            "N": "10", "S": "0", "E": "-11", "W": "-9","puzzle": "RECURSION-PUZZLE", "monster": null, "items": null, "fixtures": null, "picture": "study.png" },
  //    { "room_name":"Align HQ", "room_number": "11",
  //            "description":"Align Headquarters. Professor Keith is here working on your architecture review requirements.\n \"Great to see you!\" he says. \"Looks like you're ready to do your code walk!\" \nThere is an open pathway to the north.",
  //            "N": "12", "S": "0", "E": "10", "W": "0","puzzle": null, "monster": null, "items": "Golden Ticket", "fixtures" : "Professor Keith","picture": "hq.png" },
  //    { "room_name":"The Bridge", "room_number": "12",
  //            "description":"Align Bridge. There's a whiteboard here. There are monitors here. Replaying all of YOUR successes, friends and encounters in Align.\nYou feel something wet on your face...I'm not crying - YOU'RE crying. (tears of joy).\nA dimly lit room is to the north.",
  //            "N": "13", "S": "11", "E": "0", "W": "0","puzzle": null, "monster": null, "items": null, "fixtures" : "Monitor, Monitors, Whiteboard", "picture": "monitor-room.png" },
  //    { "room_name":"Bridge Exit", "room_number": "13",
  //            "description":"Congratulations on all you've achieved! You're at the end of the road with the Bridge.\nBest wishes as you proceed to the MSCS/MSDS!\n *** THE END *** (of this adventure...the START of a new one for YOU! :-))",
  //            "N": "0", "S": "12", "E": "0", "W": "0","puzzle": "Robot", "monster": null, "items": "Note", "fixtures" : null, "picture": "congratulations.png" }
  //  ],
  //    "items":[
  //    { "name":"Lamp", "weight": "3", "max_uses": "100", "uses_remaining": "20", "value" : "100", "when_used" : "You light the lamp with the flint.",
  //            "description":"An old oil lamp with flint to spark.", "picture": "lamp.png" },
  //    { "name":"Thumb Drive", "weight": "1", "max_uses": "1000", "uses_remaining": "1000", "value" : "150","when_used" : "You insert the thumb drive.",
  //            "description":"A USB thumb drive for computers", "picture": null },
  //    { "name":"Algorithms Book", "weight": "2", "max_uses": "1000", "uses_remaining": "1000", "value" : "500", "when_used" : "You read the Algo Book and make note of computational theories.",
  //            "description":"A book on computer algorithms, signed by Professor Keith. \nThe inscription reads: \"You'll need this in CS5800. Best wishes to you, Aligner!\n P.S.: Don't forget: a recursive solution needs a base case!", "picture": "algorithms.png" },
  //    { "name":"Hair Clippers", "weight": "2", "max_uses": "10", "uses_remaining": "4", "value" : "5","when_used" : "You activate the clippers. It makes a loud buzzing sound as you use them!",
  //            "description":"Cordless Wahl hair clippers for pets or humans. The battery low light is blinking.", "picture": "hair-clippers.png" },
  //    { "name":"Carrot", "weight": "10", "max_uses": "1", "uses_remaining": "1", "value" : "5", "when_used" : "You take a small bite the carrot and then share it.",
  //            "description":"A carrot. But not just any carrot, but a HUGE carrot! Bigger than you've seen before.", "picture": null },
  //    { "name":"Modulo 2", "weight": "1", "max_uses": "10", "uses_remaining": "10", "value" : "500", "when_used" : "You apply the Mod 2 operator and take note of the remainder.",
  //            "description":"A old school floppy disk. The kind your parents used. \nIt has \"Computer Science (number mod 2) operator. Use in case of emergencies\" inscribed on it. Wait - it just morphed into a cloud. Hold on - it just turned back into a disk. Weird!", "picture": null },
  //    { "name":"Golden Ticket", "weight": "10", "max_uses": "1", "uses_remaining": "1", "value" : "5", "when_used" : "You read the Golden Ticket and mark your calendar. 'We cannot miss our code review!', you happily shout as you place it in the slot.",
  //            "description":"A Golden Ticket inviting your team to the Architectural Review and Code Walk with our TA Team.", "picture": "golden-ticket.png" },
  //    { "name":"Key", "weight": "1", "max_uses": "3", "uses_remaining": "3", "value" : "5", "when_used" : "You insert the key and turn it. 'Click!'",
  //            "description":"A medium-sized key. Looks like it may unlock a cabinet or desk.", "picture": "key.png" },
  //    { "name":"Frying Pan", "weight": "3", "max_uses": "10", "uses_remaining": "10", "value" : "0", "when_used" : "Sizzle, sizzle...bonk!",
  //            "description":"A stainless steel skillet for frying.", "picture": null },
  //    { "name":"Note", "weight": "0", "max_uses": "1000", "uses_remaining": "1000", "value" : "100", "when_used" : "You read the note. It's from Professor K: 'I am very proud of you!'\nWhoop Whoop! You High-5 your team and ride off into the sunset.",
  //            "description":"It's a personal note to you from Professor K. \nYou read the note: 'I am very proud of you!'\nWhoop Whoop! You High-5 your team and ride off into the sunset.", "picture": null }
  //  ],
  //    "fixtures":[
  //    { "name":"Desk", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"An old wooden desk with a mess of papers. A note says: \"use thumb drive!\"", "picture": null },
  //    { "name":"Laptop", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A ZacPro 5000 with all the latest features and a USB slot. It's bolted to the table and can't move", "picture": null },
  //    { "name":"Professor Keith", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"Professor Keith, Faculty Director of Align Boston. He smiles at you and gives a thumb's up. \"Great job! You can do this!\"", "picture": null },
  //    { "name":"Billboard", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large billboard looms in the distance. \nIt's hard to read but seems to say 'Welcome to Align Quest, adventurer! Enjoy the exploration! - Prof. K'", "picture": "billboard.png" },
  //    { "name":"Chandelier", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large and heavy chandelier hangs from the ceiling. \nThe light from it is dim, and it is too high to reach without using a mechanical lift.", "picture": null },
  //    { "name":"Bookshelf", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A tall black oak bookshelf in the corner of the room. \nIt's much too heavy to move...but there's something weird about it and the books seem to tremble on their own", "picture": null },
  //    { "name":"Monitor", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", "picture": null },
  //    { "name":"Monitors", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", "picture": null },
  //    { "name":"Stove", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large propane stove. You like electric ones, but this wasn't your decision, obviously.", "picture": null },
  //    { "name":"Whiteboard", "weight": "1000", "puzzle":null, "states" : null,
  //            "description":"A large whiteboard mounted to the wall. Some UML class and sequence diagrams are scattered on it, in various colors", "picture": null }
  //  ],
  //    "monsters":[
  //    { "name":"Rabbit", "active": "true", "affects_target":"true", "affects_player": "true", "solution" : "Carrot", "value" : "300",
  //            "description" : "Awww. A furry rabbit twitching its nose and eating a carrot. Makes you want to pet him",
  //            "effects" : "A monster Rabbit moves towards you! He's blocking the way north. \nI think you might be dinner!",
  //            "damage" : "-15",
  //            "target" : "7:Dining Room", "can_attack": "true", "attack":"licks you with a giant tongue!", "picture": "monster-rabbit.png" },
  //    { "name":"Teddy Bear", "active": "true", "affects_target":"true", "affects_player": "true", "solution" : "Hair Clippers", "value" : "200",
  //            "description" : "A peaceful, cute-looking teddy bear with its hair clipped sits on the floor",
  //            "effects" : "A monster Teddy Bear growls at you! You cannot get past!", "damage" : "-5",
  //            "target" : "3:Foyer", "can_attack": "true", "attack":"hits you with soft, fluffy paws! You might sneeze!", "picture": "monster-teddy.png" }
  //  ],
  //    "puzzles":[
  //    { "name":"DARKNESS", "active": "true", "affects_target":"true", "affects_player": "true", "solution" : "Lamp", "value" : "150",
  //            "description" : "Darkness! You cannot see!",
  //            "effects" : "It's dark! You cannot see anything! Maybe we should go back?",
  //            "target" : "6:Kitchen", "picture": "darkness.png" },
  //    { "name":"MOD-SPOOKY-VOICE", "active": "true", "affects_target":"true", "affects_player": "false", "solution" : "Modulo 2", "value" : "400",
  //            "description" : "An spooky, eerie library. You walked into this eerie library FROM the west. \nAnother room is north. Books are rustling by themselves on a bookshelf.",
  //            "effects" : "Books are rustling by themselves on the bookshelf. That's a weird bookshelf. Really weird.\nYou hear a voice whisper: \"~Find Even Numbers Only~\" \nYikes. That's creepy. Maybe we should leave?",
  //            "target" : "4:Spooky Library", "picture": null },
  //    { "name":"RECURSION-PUZZLE", "active": "true", "affects_target":"true", "affects_player": "false", "solution" : "'Base Case'", "value" : "700",
  //            "description" : "I feel like we've been here before - not quite an infinite loop but infinite...!",
  //            "effects" : "Every time you move, you end up in the same room!\nSomething is etched into the wall with a knife. It says: 'Provide a solution to end the recursive madness'",
  //            "target" : "10:Recursive Study", "picture": null },
  //    { "name":"USB", "active": "true", "affects_target":"true", "affects_player": "false", "solution" : "Thumb Drive", "value" : "500",
  //            "description" : "A laptop with a USB port",
  //            "effects" : "This is a quiet living room. \nThe dining area is to your south and another room to your east but there seems to be an invisible barrier blocking you from going in that direction. \nA dimly lit laptop is on a small table in the corner",
  //            "target" : "9:Living Room", "picture": null },
  //    { "name":"ROBOT", "active": "true", "affects_target":"true", "affects_player": "false", "solution" : "Golden Ticket", "value" : "250",
  //            "description" : "A large robot with lights flashing, humming a tune from a cartoon. You can't quite remember the song. \nThere's a slot on it's front side it reads: 'Insert Here'",
  //            "effects" : "This room is pretty much empty, except for a large robot in the corner, making a humming sound. \nIt's annoying! Turn it off!\nThere's a slot on it's front side it reads: 'INSERT TICKET HERE'", "target" : "13:Bridge Exit", "picture": "robot.png" }
  //  ]
  //  }

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
    fixtures.put("Billboard", new Fixture("Billboard", "A large billboard looms in the distance. \nIt's hard to read but seems to say 'Welcome to Align Quest, adventurer! Enjoy the exploration! - Prof. K'", 1000, "billboard.png"));
    fixtures.put("Chandelier", new Fixture("Chandelier", "A large and heavy chandelier hangs from the ceiling. \nThe light from it is dim, and it is too high to reach without using a mechanical lift.", 1000));
    fixtures.put("Bookshelf", new Fixture("Bookshelf", "A tall black oak bookshelf in the corner of the room. \nIt's much too heavy to move...but there's something weird about it and the books seem to tremble on their own", 1000));
    fixtures.put("Monitor", new Fixture("Monitor", "A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", 1000));
    fixtures.put("Monitors", new Fixture("Monitors", "A large and heavy video monitor (one of many, actually). \nIt's replaying your success here at Khoury/NU in your Align studies. Bravo!", 1000));
    fixtures.put("Stove", new Fixture("Stove", "A large propane stove. You like electric ones, but this wasn't your decision, obviously.", 1000));
    fixtures.put("Whiteboard", new Fixture("Whiteboard", "A large whiteboard mounted to the wall. Some UML class and sequence diagrams are scattered on it, in various colors", 1000));


    // Initialize monsters
    Map<String, Monster> monsters = new HashMap<>();
    monsters.put("Rabbit", new Monster("Rabbit", true, true, true, "Carrot", 300, "Awww. A furry rabbit twitching its nose and eating a carrot. Makes you want to pet him", "A monster Rabbit moves towards you! He's blocking the way north. \nI think you might be dinner!", -15, "7:Dining Room", true, "licks you with a giant tongue!", "monster-rabbit.png"));
    monsters.put("Teddy Bear", new Monster("Teddy Bear", true, true, true, "Hair Clippers", 200, "A peaceful, cute-looking teddy bear with its hair clipped sits on the floor", "A monster Teddy Bear growls at you! You cannot get past!", -5, "3:Foyer", true, "hits you with soft, fluffy paws! You might sneeze!", "monster-teddy.png"));

    // Initialize puzzles
    Map<String, Puzzle> puzzles = new HashMap<>();
    puzzles.put("DARKNESS", new Puzzle("DARKNESS", true, true, true, "Lamp", 150, "Darkness! You cannot see!", "It's dark! You cannot see anything! Maybe we should go back?", "6:Kitchen", "darkness.png"));
    puzzles.put("MOD-SPOOKY-VOICE", new Puzzle("MOD-SPOOKY-VOICE", true, true, false, "Modulo 2", 400, "An spooky, eerie library. You walked into this eerie library FROM the west. \nAnother room is north. Books are rustling by themselves on a bookshelf.", "Books are rustling by themselves on the bookshelf. That's a weird bookshelf. Really weird.\nYou hear a voice whisper: \"~Find Even Numbers Only~\" \nYikes. That's creepy. Maybe we should leave?", "4:Spooky Library", null));
    puzzles.put("RECURSION-PUZZLE", new Puzzle("RECURSION-PUZZLE", true, true, false, "'Base Case'", 700, "I feel like we've been here before - not quite an infinite loop but infinite...!", "Every time you move, you end up in the same room!\nSomething is etched into the wall with a knife. It says: 'Provide a solution to end the recursive madness'", "10:Recursive Study", null));
    puzzles.put("USB", new Puzzle("USB", true, true, false, "Thumb Drive", 500, "A laptop with a USB port", "This is a quiet living room. \nThe dining area is to your south and another room to your east but there seems to be an invisible barrier blocking you from going in that direction. \nA dimly lit laptop is on a small table in the corner", "9:Living Room", null));
    puzzles.put("ROBOT", new Puzzle("ROBOT", true, true, false, "Golden Ticket", 250, "A large robot with lights flashing, humming a tune from a cartoon. You can't quite remember the song. \nThere's a slot on it's front side it reads: 'Insert Here'", "This room is pretty much empty, except for a large robot in the corner, making a humming sound. \nIt's annoying! Turn it off!\nThere's a slot on it's front side it reads: 'INSERT TICKET HERE'", "13:Bridge Exit", "robot.png"));

    // Initialize rooms
    Map<Integer, Room> rooms = new HashMap<>();
    rooms.put(1, new Room(1, "Courtyard", "A beautiful courtyard with flowers on both sides of the stone walkway. \nThe walkway leads north. A billboard is in the distance.", Map.of("N", 2, "S", 0, "E", 0, "W", 0), null, null, "Hair Clippers", "Billboard", "courtyard.png"));
    rooms.put(2, new Room(2, "Mansion Entrance", "Entrance to an old, musty-smelling mansion. Some people have entered, to never return. \nThe door to the north is open. The courtyard is to your south and a foyer to your north. A chandelier hangs from the ceiling.", Map.of("N", 3, "S", 1, "E", 0, "W", 0), null, null, "Thumb Drive, Modulo 2", "Chandelier", "entrance.png"));
    rooms.put(3, new Room(3, "Foyer", "The foyer of the mansion. A staircase leads upstairs but it is dilapidated and unusable. Therefore, the only exits to the south and east. \nA strange breeze moves through the room. Eastward is a small room that looks like a library. A teddy bear lies on the floor with its hair shaved", Map.of("N", 0, "S", 2, "E", 4, "W", 0), null, monsters.get("Teddy Bear"), "Lamp", null, "foyer.png"));
    rooms.put(4, new Room(4, "Spooky Library", "You walked into this eerie library FROM the west. Another room is north. \nBooks now on the floor revealing a secret passage to the East!", Map.of("N", 6, "S", 0, "E", -5, "W", 3), puzzles.get("MOD-SPOOKY-VOICE"), null, "Key", "Bookshelf", "library.png"));
    rooms.put(5, new Room(5, "Hidden Chamber", "You've found a hidden chamber! It's a small room that's barely the size of a closet. \nThere's an old desk here. It looks like no one has sat at it in a LONG time.", Map.of("N", 0, "S", 0, "E", 0, "W", 4), null, null, "Algorithms Book", "Desk", "secret_room.png"));
    rooms.put(6, new Room(6, "Kitchen", "This is a spotless kitchen. Pots and pans are hanging from the ceiling. A stove is here. Stainless steel appliances are everywhere. \nOut of the corner of your eye, you think you see something moving to your west.", Map.of("N", 0, "S", 4, "E", 0, "W", 7), puzzles.get("DARKNESS"), null, null, "Stove", "kitchen.png"));
    rooms.put(7, new Room(7, "Dining Room", "A large dining room with a Mahogany wood table. \nA cute furry rabbit is on the table eating carrots.", Map.of("N", -9, "S", 8, "E", 6, "W", 0), null, monsters.get("Rabbit"), "Frying Pan", null, "dining.png"));
    rooms.put(8, new Room(8, "Giant Plate", "Uh oh. You're on a giant plate, full of carrots! \nI think we need to go, NOW!", Map.of("N", 7, "S", 0, "E", 0, "W", 0), null, null, "Carrot", null, "plate.png"));
    rooms.put(9, new Room(9, "Living Room", "This a living room. The dining area is to your south and another room to your east. \nThe laptop is now making all kinds of noise. A file on a thumb drive has opened automatically and describes a recursive algorithm.", Map.of("N", 0, "S", 7, "E", -10, "W", 0), puzzles.get("USB"), null, null, "Laptop", "livingroom.png"));
    rooms.put(10, new Room(10, "Recursive Study", "A study/office room. A fake mirror has been shattered showing an opening to the east.", Map.of("N", 10, "S", 0, "E", -11, "W", -9), puzzles.get("RECURSION-PUZZLE"), null, null, null, "study.png"));
    rooms.put(11, new Room(11, "Align HQ", "Align Headquarters. Professor Keith is here working on your architecture review requirements.\n \"Great to see you!\" he says. \"Looks like you're ready to do your code walk!\" \nThere is an open pathway to the north.", Map.of("N", 12, "S", 0, "E", 10, "W", 0), null, null, "Golden Ticket", "Professor Keith", "hq.png"));
    rooms.put(12, new Room(12, "The Bridge", "Align Bridge. There's a whiteboard here. There are monitors here. Replaying all of YOUR successes, friends and encounters in Align.\nYou feel something wet on your face...I'm not crying - YOU'RE crying. (tears of joy).\nA dimly lit room is to the north.", Map.of("N", 13, "S", 11, "E", 0, "W", 0), null, null, null, "Monitor, Monitors, Whiteboard", "monitor-room.png"));
    rooms.put(13, new Room(13, "Bridge Exit", "Congratulations on all you've achieved! You're at the end of the road with the Bridge.\nBest wishes as you proceed to the MSCS/MSDS!\n *** THE END *** (of this adventure...the START of a new one for YOU! :-))", Map.of("N", 0, "S", 12, "E", 0, "W", 0), puzzles.get("ROBOT"), null, "Note", null, "congratulations.png"));

    // Initialize game world
    gameWorld = new GameWorld("Align Quest", "1.0.9", rooms);

    // Initialize player
    player = new Player("TestPlayer", 100, 10, 0);

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
    assertFalse(gameWorld.getRoom(4).getProblem().getActive());
  }

  @Test
  void testHandleMonster() {
    gameController.processCommand("N");
    gameController.processCommand("N");
    gameController.processCommand("USE Hair Clippers");
    assertFalse(gameWorld.getRoom(3).getProblem().getActive());
  }

  @Test
  void testCheckInventory() {
    gameController.processCommand("TAKE Hair Clippers");
    gameController.processCommand("INVENTORY");
    // Assuming viewer.showText() prints to console or logs, we can't assert its output directly
    // We can check if the method runs without exceptions
  }
}