package enginedriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enginedriver.problems.IProblem;
import enginedriver.problems.Monster;
import enginedriver.problems.Problem;
import enginedriver.problems.Puzzle;

/**
 * GameEngine class to handle game logic and player commands.
 * This class is responsible for processing
 * player commands and updating the game state.
 * It interacts with the GameWorld, Player, and Viewer classes.
 * It also handles the game loop and user input.
 */
public class GameController {
  private Player player;
  private GameWorld gameWorld;
  private Viewer viewer;
  private Map<String, String> actionMap = new HashMap<>();

  /**
   * Constructor for GameController.

   * @param gameWorld the game world
   * @param player the player
   */
  public GameController(GameWorld gameWorld, Player player) {
    this.gameWorld = gameWorld;
    this.player = player;
    this.viewer = Viewer.getInstance();
    // Action abbreviations map
    // TODO：这里应该设计为一个配置文件直接读进来
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
  }

  /**
   * Get the game world.

   * @return the game world
   */
  public GameWorld getGameWorld() {
    return gameWorld;
  }

  /**
   * Get the player.

   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Standardize the command entered by the player.

   * @param command the command entered by the player
   * @return the standardized command
   */
  private String[] standardizeCommand(String command) {
    // Remove leading and trailing white spaces
    command = command.trim();

    // Split by space
    String[] commandParts = command.split("\\s+");





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
   * Process the command after it has been standardized.

   * @param command the command standardized by standardizeCommand
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
      case "A": answer(objectName);
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
   * Move the player to a new room based on the direction.

   * @param direction the direction to move (N, S, E, W)
   */
  private void move(String direction) {
    //check player's current room
    int currentRoom = player.getRoomNumber();

    Room<?> currentRoomObj = gameWorld.getRoom(currentRoom);
    //get room's exists
    Map<String, Integer> exits = currentRoomObj.getExits();

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

        // get problem from the room,get prloblem clas
        IProblem<?> problem = enteredRoom.getProblem();

        if (problem != null && problem.getActive()) {
          // show the problem effect on Entering
          viewer.showText(problem.getEffects());
          // deal with monster attack
          handleMonsterAttack(problem);
        }
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
    Item itemAttempt = currentRoom.getItem(itemName);

    // check if the item is in the room
    if (itemAttempt != null) {
      if (player.addItem(itemAttempt)) {
        currentRoom.removeEntity(itemAttempt);
        viewer.showText(itemName + "added to your inventory!");
        player.setScore(player.getScore() + itemAttempt.getValue());
      } else {
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
      viewer.showText(itemName + "dropped here in " + currentRoom.getName());
      player.setScore(player.getScore() - item.getValue());
    } else {
      viewer.showText("Sorry, you don't have " + itemName + " in your bag");
    }

  }

  /**
   * Look around the current room.

   * @return the description of the current room
   */
  private void lookAround() {
    // Logic to look around
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // check if room has
    viewer.showText("You are currently standing in the" + currentRoom.getName());
    viewer.showText(currentRoom.getDescription());

    // deal with monster attack
    IProblem<?> problem  =  currentRoom.getProblem();
    handleMonsterAttack(problem);


    //get items keys from the room
    List<String> itemNames = currentRoom.getEntities().keySet().stream()
            .toList();
    //get fixtures keys from the room
    List<String> fixtureNames = currentRoom.getEntities().keySet().stream()
            .toList();
    //get problem from the room
    String problemDescription = currentRoom.getProblem()
            .getDescription();

    // show the items, fixtures, problem in the room
    viewer.showText("Items you see here: ");
    viewer.showText(String.join(", ", itemNames));
    viewer.showText("Fixtures you see here: ");
    viewer.showText(String.join(", ", fixtureNames));

  }




  /**
   * Use an item.
   *
   * @param itemName the name of the item to use
   */
  private void useItem(String itemName) {


    // get room
    Room<?> currentRoom = gameWorld.getRoom(
            player.getRoomNumber());

    // get current problem
    IProblem<?> problem  =  currentRoom.getProblem();

    if (problem == null) {
      viewer.showText("You are trying to use" + itemName
              + " but nothing interesting happens");
      return;
    }

    //check solution type
    Class<?> solutionClass = problem.getSolution().getClass(); // 获取solution的Class对象
    // check if the prolem is a IProblem<Item>
    if (solutionClass != Item.class) {
      viewer.showText("You are trying to use" + itemName
              + " but nothing interesting happens");
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

    // check if player has this item.
    boolean hasItem = player.hasEntity(itemName);
    if (!hasItem) {
      viewer.showText("You don't have " + itemName
              + " in your bag.");
      // deal with monster attack
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

    // get item
    Item itemAttempt = player.getEntity(itemName,
            Item.class);

    Problem<Item> itemproblem = (Problem<Item>) problem;
    // slove problem
    int flag = itemproblem.solve(itemAttempt);
    switch (flag) {
      case 0:
        viewer.showText("You are trying to use" + itemName
                + " but nothing interesting happens");
        return;
      case 1:
        if (!itemAttempt.use()) {
          viewer.showText(itemName + "is empty "
                  + "or cannot be used again.");
          handleMonsterAttack(problem);
          return;
        }
        viewer.showText(itemAttempt.getWhenUsed());
        handleProblemSolved(problem);
        return;
      case 2:
        viewer.showText("You are trying to use" + itemName
                + " but nothing interesting happens");
        handleMonsterAttack(problem);
    }
  }


  /**
   * Answer a puzzle.

   * @param objectName the answer to the puzzle
   */
  private void answer(String objectName) {

    // get the current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // get puzzle or moster in the room
    IProblem<?> problem = currentRoom.getProblem();
    if (problem == null) {
      viewer.showText("There is no question to answer in this room.");
      return;
    }

    if (objectName == null || objectName.isEmpty()) {
      viewer.showText("Please provide an answer.");
      handleMonsterAttack(problem);
      return;
    }

    // check if the puzzle is solved
    if (!problem.getActive()) {
      viewer.showText("The puzzle is already solved.");
      return;
    }

    // check if the solution is a string
    if (!(problem.getSolution() instanceof String)) {
      viewer.showText("You are trying to answer a question, "
              + "but nothing interesting happens.");
      handleMonsterAttack(problem);
    }

    Problem<String> problemString = (Puzzle<String>) problem;

    // solve the problem
    int flag = problemString.solve(objectName);
    switch (flag) {
      case 0:
        viewer.showText("You are trying to answer the question, "
                + "but nothing interesting happens.");
        return;
      case 1:
        viewer.showText("You have successfully solved the puzzle!");
        handleProblemSolved(problem);
        return;
      case 2:
        viewer.showText("You are trying to answer the question, "
                + "but the answer is wrong.");
        handleMonsterAttack(problem);
    }
  }



  /**
   * Check the player's inventory.

   * @return the items in the player's inventory
   */
  private void checkInventory() {
    // Logic to check inventory
    Map<String, Item> items = player.getEntities();
    if (items.isEmpty()) {
      viewer.showText("There is nothing in your inventory.");
    } else {
      viewer.showText("Items in your inventory: ");
      String itemList = String.join(", ", items.keySet());
      viewer.showText(itemList);
    }
  }

  /**
   * Examine an item.

   * @param entityName the name of the item to examine
   */
  private void examine(String entityName) {
    // get current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());

    // check if the entity is in the room
    IIdentifiableEntity entity = currentRoom.getEntity(entityName, Item.class);
    if (entity == null) {
      entity = currentRoom.getEntity(entityName, Fixture.class);
    }
    if (entity == null) {
      viewer.showText(entityName + " is not in the room.");
    } else {
      viewer.showText(entity.getDescription());
    }
  }

  /**
   * Unlocks room.

   * @param room the room to unlock exits
   */
  private void unlockExits(Room<?> room) {
    for (String key : room.getExits().keySet()) {
      int value = room.getExits().get(key);
      if (value < 0) {
        room.unlockExit(key);  // Unlock exit if value is negative
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

  /**
   * Handle the problem solved event.
   * This method updates the game state when a problem is solved.
   * It updates the player's score, unlocks exits if necessary,and shows a message to the player.

   * @param problem the problem that was solved
   */
  private  void handleProblemSolved(IProblem<?> problem) {
    viewer.showText("You have successfully solved"
            + problem.getName());
    problem.setActive(false); // set problem to inactive

    // deal with score
    int points = problem.getValue();
    player.addScore(points); // update score
    viewer.showText("+ " + points + ". Current Score is " + player.getScore());

    if (problem.getAffectsTarget()) {
      String problemTarget = problem.getTarget();
      String[] parts = problemTarget.split(":", 2);
      int roomNumber = Integer.parseInt(parts[0].trim());
      String roomName = parts[1].trim();
      unlockExits(gameWorld.getRoom(roomNumber));
      viewer.showText("The room " + roomName + " is unlocked.");
    }
  }

  /**
   * Handle monster attack.
   * This method checks if the problem is a monster and if it can attack the player.
   * If so, it performs the attack and shows the attack message to the player.

   * @param problem the problem to check
   */
  private void handleMonsterAttack(IProblem<?> problem) {
    if (problem instanceof Monster) {
      Monster<?> monster = (Monster) problem;
      if (monster.getAffectsPlayer() && monster.getCanAttack()) {
        monster.attack(player);
        viewer.showText(monster.getAttack());
      }
    }
  }

  /**
   * Get the game controller as a string.
   * This method returns a string representation of the game controller,
   * including the game world and player information.

   * @return the string representation of the game controller
   */
  @Override
  public String toString() {
    return "{ "
            + gameWorld.toString() + ","
            + "\"player\":" + player.toString()
            + " }";
  }
}
