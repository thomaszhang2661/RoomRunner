package enginedriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GameEngine class to handle game logic and player commands.
 */
public class GameController {
  private Player player;
  private GameWorld gameWorld;
  private Viewer viewer;

  /**
   * Constructor for GameController.

   */
  public GameController(GameWorld gameWorld, Player player) {
    this.gameWorld = gameWorld;
    this.player = player;
    this.viewer = Viewer.getInstance();
  }

  /*
  (N)orth, (S)outh, (E)ast, (W)est,
   (T)ake, (D)rop,
   e(X)amine, (L)ook,
    (U)se, (I)nventory, (A)nswer,
     Save and Restore.
   */

  /**
   * Process the command entered by the player.
   */
  private String[] standardizeCommand(String command) {
    // Remove leading and trailing white spaces
    command = command.trim();

    // Split by space
    String[] commandParts = command.split("\\s+");


    // Action abbreviations map
    // TODO：这里应该设计为一个配置文件直接读进来
    Map<String, String> actionMap = new HashMap<>();
    actionMap.put("NORTH", "N");
    actionMap.put("SOUTH", "S");
    actionMap.put("EAST", "E");
    actionMap.put("WEST", "W");
    actionMap.put("TAKE", "T");
    actionMap.put("DROP", "D");
    actionMap.put("EXAMINE", "X");
    actionMap.put("LOOK", "L");
    actionMap.put("USE", "U");
    actionMap.put("INVENTORY", "I");
    actionMap.put("ANSWER", "A");

    // Default action is the first part of the command (converted to uppercase)
    String action = commandParts[0].toUpperCase();

    // Prepare the object name by joining the rest of the command parts
    String objectName = (commandParts.length > 1)
            ? String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length))
            : "";

    // If the action exists in the map, use its abbreviation, otherwise leave as is
    action = actionMap.getOrDefault(action, action);

    return new String[]{action, objectName};
  }


  /**
   * Process the command entered by the player.
   */
  public void processCommand(String command) {
    // Standardize the command
    String[] commandParts = standardizeCommand(command);

    //take out action 和 objectName
    command = commandParts[0];
    String objectName = commandParts[1];

    switch (command.toUpperCase()) {
      case "N": move("N");
      break;
      case "S": move("S");
      break;
      case "E": move("E");
      break;
      case "W": move("W");
      break;
      case "T": takeItem(objectName);
      break;
      case "D": dropItem(objectName);
      break;
      case "L": lookAround();
      break;
      case "U": useItem();
      break;
      case "I": checkInventory();
      break;
      case "X": examine(objectName);
      break;
      case "A": answerPuzzle();
      break;
      case "Q": quit();
      break;
      case "SAVE": save();
      break;
      case "RESTORE": restore();
      break;
      default:
        viewer.showText("Invalid command.");
        break;
    }
  }



  /**
   * Move the player north.
   */
  private void move(String direction) {
    //check player's current room
    int currentRoom = player.getRoomNumber();

    //get room's exists
    Map<String, Integer> exits = gameWorld.getRoom(currentRoom).getExits();

    //check if the direction is valid
    if (exits.containsKey(direction)) {
      int attempRoomNum = exits.get(direction);
      if (attempRoomNum < 0) {
        viewer.showText("The direction is blocked.");
        return;
      } else if (attempRoomNum == 0) {
        viewer.showText("Invalid direction, there is no more room in this direction.");
      } else {

        //get the room that player is going to enter
        Room enteredRoom = gameWorld.getRoom(attempRoomNum);
        //move player to the new room
        player.setRoomNumber(attempRoomNum);

        //show the enter discription
        viewer.showText("You are moving to the derection "
                + direction + ",enterred " + enteredRoom.getName()
                + ", room number" + attempRoomNum);

        // room description
        viewer.showText(enteredRoom.getDescription());
      }

    } else {
      viewer.showText("Invalid direction.");
    }
  }

  /**
   * Controller method to call the player's addItem method.
   * Print message based on the result.
   * @param itemName the name of the item that needs to be taken.
   */
  private void takeItem(String itemName) {
    Item item = gameWorld.findItemByName(itemName);
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());

    // Check if item in the room and player still have enough weight
    if (currentRoom.getItemNames().contains(itemName)
            && player.getCurrentWeight() < item.getWeight()) {
      player.addItem(itemName);
      player.gainOrLoseWeight(-(item.getWeight()));
      player.gainOrLoseScore(item.getValue());

      currentRoom.deleteItem(itemName);
      viewer.showText("You have successfully add " + itemName + " to your bag!");

    } else {
      viewer.showText("Sorry, you can not add " + itemName + " to your bag. It's either because"
                      + " the item is not in the room or your bag is full.");
    }
  }

  /**
   * Controller method to call the player's delete method.
   * @param itemName the name of the item that needs to be dropped.
   */
  private void dropItem(String itemName) {
    Item item = gameWorld.findItemByName(itemName);
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());

    // Check if player has this item
    if (player.getItems().contains(itemName)) {
      player.deleteItem(itemName);
      player.gainOrLoseWeight(item.getWeight());
      player.gainOrLoseScore(-(item.getValue()));

      currentRoom.addItem(itemName);
      viewer.showText("You have successfully drop " + itemName + "!");

    } else {
      viewer.showText("Sorry, you can not drop " + itemName + ". It's mostly because"
              + " you don't have this item in your bag.");
    }
  }

  private void lookAround() {
    // Logic to look around
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    viewer.showText(currentRoom.getDescription());
  }

  /**
   * Use an item.
   */
  private void useItem() {
    // Logic to use item
    // get player room
    int roomNumber = player.getRoomNumber();
    // get puzzle or moster in the room
    String problemName = gameWorld.getRoom(roomNumber).getProblem();
    // get problem from gameworld
    IProblem problem;
    // check if the problem is a puzzle or a monster
    //IProblem problem = gameWorld.getMonster(problemName);
    // 处理解问题的逻辑
    //TODO

    }




  /**
   * Check the player's inventory.
   */
  private void checkInventory() {
    // Logic to check inventory
    if (player.getItems().isEmpty()) {
      viewer.showText("There is nothing in your inventory.");
    } else {
      viewer.showText("You currently have ");
      String itemList = String.join(", ", player.getItems());
      viewer.showText(itemList + " in your bag");
    }
  }

  /**
   * Examine an item.
   */
  private void examine(String objectName) {
    // Logic to examine item
    // get current room number
    int roomNumber = player.getRoomNumber();
    // get room
    Room room = gameWorld.getRoom(roomNumber);
    // get items and fixtures from the room
    room.isFixturein(objectName);
    room.isItemIn(objectName);

//    // get items from the room
//    // check if the objectName is in the items
//    if (itemNames.contains(objectName)) {
//      // get the item
//      gameWorld.getItem(objectName);
//    } else if (fixtureNames.contains(objectName)) {
//      // get the fixture
//      //TODO
//    } else {
//      // objectName is not in the items or fixtures
//      viewer.showText(objectName + " is not in the room.");
//    }
    // get fixtures from the room
  }

  /**
   * Diminish the player's health
   */
  private void attackPlayer() {
    //TODO

  }

  /**
   * Block the description of the room the puzzle's in, keep the player from
   * entering said room.
   */
  private void blockRoom() {
    //TODO

  }

  /**
   * Answer a puzzle.
   */
  private void answerPuzzle() {
    // Logic to answer puzzle
    //TODO
  }

  /**
   * Quit the game.
   */
  private void quit() {
    // Logic to quit the game
    //TODO
  }

  /**
   * Save the game state.
   */
  private void save() {
    // Logic to save the game state
    //TODO
  }

  /**
   * Restore the game state.
   */
  private void restore() {
    // Logic to restore the game state
    //TODO
  }

  /**
   * record the game state.
   */
  // Additional methods for game logic can be added here
  private void record() {
    // Logic to record the game state for replay
    // 区分用户操作是否对gameworld造成了影响，
    //  如果有影响，记录操作和变化
    //TODO
  }
}
