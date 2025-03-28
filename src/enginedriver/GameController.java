package enginedriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
      case "U": useItem(objectName);
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
        // deal with monster attack
        IProblem<?> problem  =  enteredRoom.getProblem();
        handleMonsterAttack(problem);

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
    //get room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    //get item
    Item itemAttempt = currentRoom.getEntity(itemName, Item.class);

    if (itemAttempt != null) {
      if(player.addItem(itemAttempt)) {
        currentRoom.removeEntity(itemAttempt);
        viewer.showText("You have successfully add " + itemName + " to your bag!");
      }else {
        viewer.showText("Sorry, you can not add " + itemName + " to your bag. Because"
                + "  your bag is full.");
      }
    } else {
      viewer.showText("Sorry, you can not add " + itemName + " to your bag. Because"
              + " the item is not in the room .");
    }
  }

  /**
   * Controller method to call the player's delete method.
   * @param itemName the name of the item that needs to be dropped.
   */
  private void dropItem(String itemName) {
    Item item = player.getEntity(itemName, Item.class);
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // Check if player has this item
    if (item != null) {
      player.removeItem(item);
      currentRoom.addEntity(item);
      viewer.showText("You have successfully drop " + itemName + "!");
    } else {
      viewer.showText("Sorry, you can not drop " + itemName + ". It's mostly because"
              + " you don't have this item in your bag.");
    }

  }

  private void lookAround(){
    // Logic to look around
    //TODO check if need to show what is inside the room, print names?
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // deal with monster attack
    IProblem<?> problem  =  currentRoom.getProblem();
    handleMonsterAttack(problem);

    // check if room has
    viewer.showText(currentRoom.getDescription());
    //get items keys from the room
    List<String> itemNames = currentRoom.getEntities().keySet().stream()
            .toList();
    //get fixtures keys from the room
    List<String> fixtureNames = currentRoom.getEntities().keySet().stream()
            .toList();
    //get problem from the room
    String problemDescription = currentRoom.getProblem().getDescription();

    // show the items, fixtures, problem in the room
    viewer.showText("You see the following items: ");
    viewer.showText(String.join(", ", itemNames));
    viewer.showText("You see the following fixtures: ");
    viewer.showText(String.join(", ", fixtureNames));
    viewer.showText("You see the following problem: ");
    viewer.showText(problemDescription);
  }




  /**
   * Use an item.
   */
  private void useItem(String itemName) {
    // get room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    Item itemAttempt = player.getEntity(itemName, Item.class);
    IProblem<?> problem  =  currentRoom.getProblem();

    if (itemAttempt == null) {
      viewer.showText("You don't have " + itemName + " in your bag.");
      // deal with monster attack
      handleMonsterAttack(problem);
      return;
    }

    //check solution type
    Class<?> solutionClass = problem.getSolution().getClass(); // 获取solution的Class对象
    if(solutionClass== Item.class) {
      IProblem<Item> itemproblem = (IProblem<Item>) problem;
      boolean flag = itemproblem.solve(itemAttempt);
      if (flag) {
        viewer.showText("You have successfully solved the problem with " + itemName);
        //TODO update room exits
        //TODO update score
        return;
      } else {
        viewer.showText("You have failed to solve the problem with " + itemName);
        // deal with monster attack
        handleMonsterAttack(problem);
      }

    }


  }





  /**
   * Check the player's inventory.
   */
  private void checkInventory() {
    // Logic to check inventory
    Map<String, Item> items = player.getItems();
    if (items.isEmpty()) {
      viewer.showText("There is nothing in your inventory.");
    } else {
      viewer.showText("You currently have ");
      String itemList = String.join(", ", items.keySet());
      viewer.showText(itemList + " in your bag");
    }
  }

  /**
   * Examine an item.
   */
  private void examine(String entityName) {
    // Logic to examine item
    // get current room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    IIdentifiableEntity entity= currentRoom.getEntity(entityName, Item.class);
    if (entity == null) {
      entity = currentRoom.getEntity(entityName, Fixture.class);
    }
    if (entity == null) {
      viewer.showText(entityName +" is not in the room.");
    } else {
      viewer.showText(entity.getDescription());
    }
  }

  /**
   * Helper method to unlock exits of a room.
   */
  private void unlockExits(Room room) {
    for (String key : room.getExits().keySet()) {
      int value = room.getExits().get(key);
      if (value < 0) {
        room.unlockExit(key);  // Unlock exit if value is negative
      }
    }
  }

  /**
   * Helper method to handle common logic for solving the puzzle
   */
  private void handlePuzzleSolution(boolean isCorrect, Room room, IProblem<?> problem) {
    if (isCorrect) {
      viewer.showText("You have successfully solved the puzzle!");
      // Unlock the exit of the room
      unlockExits(room);
      // Add the score to the player
      gameWorld.addScore(problem.getValue());
    } else {
      viewer.showText("Sorry, your answer is incorrect.");
      // Puzzle affects the player
      if (((Puzzle<?>) problem).getAffect_player()) {
        player.gainOrLoseHealth(-5);
        viewer.showText("Player takes -5 damage!");
      }
    }
  }

  /**
   * Helper function to capitalize the first letter of each word in a string.
   * @param input the input string
   * @return the string with each word's first letter capitalized
   */
  private String capitalizeWords(String input) {
    return Arrays.stream(input.split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
            .collect(Collectors.joining(" "));
  }

  /**
   * Answer a puzzle.
   */
  private void answerPuzzle() {

    // get the current room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // get puzzle or moster in the room
    IProblem problem = currentRoom.getProblem();
    if (problem == null) {
      viewer.showText("There is no puzzle or monster in this room.");
      return;
    }
    // check if the problem is a puzzle
    if (problem.getClass() != Puzzle.class) {
      viewer.showText("There is no puzzle in this room.");
      return;
    }

    // check if the puzzle is solved
    if (!problem.getActive()) {
      viewer.showText("The puzzle is already solved.");
      return;
    }

    // get player's input
    String input;
    while (true) {
      viewer.showText("Please enter your answer: ");
      Scanner scanner = new Scanner(System.in);
      input = scanner.nextLine();
      if (!input.isEmpty()) {
        break;
      }
      viewer.showText("You must enter an answer.");
    }
    input = input.trim().replaceAll("\\s+", " ").toLowerCase();

    // get the solution of the puzzle
    String correctSolution = problem.getSolution().toString();
    // check if the solution is a string
    if (correctSolution.startsWith("'") && correctSolution.endsWith("'")) {
      input = "'" + capitalizeWords(input) + "'";
      // try to solve the puzzle with the input
      handlePuzzleSolution(problem.solve(input), currentRoom, problem);
    } else {
      // the solution is an item
      input = capitalizeWords(input);
      Item itemAttempt = player.getEntity(input, Item.class);
      if (itemAttempt == null) {
        viewer.showText("You don't have " + input + " in your bag.");
        // try to solve the puzzle with the input
        handlePuzzleSolution(problem.solve(itemAttempt), currentRoom, problem);
        return;
      }

    }
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



  private void handleMonsterAttack(IProblem<?> problem) {
    if (problem instanceof Monster && problem.getActive()) {
      Monster monster = (Monster) problem;
      monster.attack(player);
      //TODO 这里好像要说话，需要完善
    }
  }
}
