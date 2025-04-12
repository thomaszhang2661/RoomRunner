package enginedriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import enginedriver.problems.Monster;
import enginedriver.problems.Problem;
import enginedriver.problems.Puzzle;
import enginedriver.problems.IProblem;
import enginedriver.view.SwingView;
import jsonio.GameDataLoader;
import jsonio.GameDataSaver;
import enginedriver.view.View;

/**
 * GameEngine class to handle game logic and player commands.
 * This is a modified version with fixes for GUI navigation.
 */
public class GameController {
  private Player player;
  private GameWorld gameWorld;
  private View view;
  private Map<String, String> actionMap = new HashMap<>();

  // Track the current room to detect changes
  private int currentRoomNumber = 1;

  /**
   * Constructor for GameController.
   */
  public GameController(GameWorld gameWorld, Player player) {
    this.gameWorld = gameWorld;
    this.player = player;
    this.view = View.getInstance();
    this.currentRoomNumber = player.getRoomNumber();

    // Action abbreviations map
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
   * Process the command entered by the player.
   */
  public void processCommand(String command) {
    System.out.println("Processing command: " + command);

    // Standardize the command
    String[] commandParts = standardizeCommand(command);

    //take out action and objectName
    command = commandParts[0];
    String objectName = commandParts[1];

    // Store the current room number before processing command
    int previousRoom = player.getRoomNumber();

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
        view.showText("Invalid command.");
        break;
    }

    // Check if the room changed after processing the command
    int newRoom = player.getRoomNumber();
    if (newRoom != previousRoom) {
      System.out.println("Room changed from " + previousRoom + " to " + newRoom);
      roomChanged(newRoom);
    }
  }

  /**
   * Handle room change events - this method is called whenever the player moves to a new room.
   * @param newRoomNumber the new room number
   */
  private void roomChanged(int newRoomNumber) {
    System.out.println("Room changed event triggered - updating UI for room " + newRoomNumber);

    // Update the current room tracking
    currentRoomNumber = newRoomNumber;

    // Get the new room
    Room<?> newRoom = gameWorld.getRoom(newRoomNumber);
    if (newRoom == null) {
      System.err.println("Error: Room " + newRoomNumber + " not found!");
      return;
    }

    // For GUI view, directly update the description panel
    if (view instanceof SwingView) {
      SwingView swingView = (SwingView) view;
      swingView.updateRoomDisplay(newRoom);
    }

    // Force a look around to update all UI elements and show room details
    lookAround();
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
            ? capitalizeWords(
            String.join(
                    " ", Arrays.copyOfRange(commandParts, 1, commandParts.length)
            )
    )
            : "";

    // If the action exists in the map, use its abbreviation, otherwise leave as is
    action = actionMap.getOrDefault(action, action);

    return new String[]{action, objectName};
  }

  /**
   * Capitalize the word to align with the JSON style.
   */
  private static String capitalizeWords(String phrase) {
    if (phrase == null || phrase.isEmpty()) {
      return phrase;
    }

    String[] words = phrase.split(" ");
    StringBuilder capitalized = new StringBuilder();

    for (String word : words) {
      if (!word.isEmpty()) {
        capitalized.append(Character.toUpperCase(word.charAt(0)));
        capitalized.append(word.substring(1).toLowerCase());
        capitalized.append(" ");
      }
    }
    return capitalized.toString().trim();
  }

  /**
   * Move the player in the specified direction.
   * @param direction the direction to move (N, S, E, W)
   */
  private void move(String direction) {
    System.out.println("Moving in direction: " + direction);

    //check player's current room
    int currentRoom = player.getRoomNumber();

    Room<?> currentRoomObj = gameWorld.getRoom(currentRoom);
    //get room's exits
    Map<String, Integer> exits = currentRoomObj.getExits();

    System.out.println("Current room: " + currentRoom + ", available exits: " + exits);

    //check if the direction is valid
    if (exits.containsKey(direction)) {
      int attemptRoomNum = exits.get(direction);
      if (attemptRoomNum < 0) {
        view.showText("The direction is blocked.");
      } else if (attemptRoomNum == 0) {
        view.showText("Invalid direction, there is no more room in this direction.");
      } else {
        //get the room that player is going to enter
        Room enteredRoom = gameWorld.getRoom(attemptRoomNum);

        System.out.println("Moving to room " + attemptRoomNum + ": " + enteredRoom.getName());

        //move player to the new room
        player.setRoomNumber(attemptRoomNum);

        //show description when entering
        view.showText("You are moving to the direction "
                + direction + ", entered " + enteredRoom.getName()
                + ", room number " + attemptRoomNum);

        // room description
        view.showText(enteredRoom.getDescription());

        // get problem from the room, get problem class
        IProblem<?> problem = enteredRoom.getProblem();

        if (problem != null && problem.getActive()) {
          // show the problem effect on Entering
          view.showText(problem.getEffects());
          // deal with monster attack
          handleMonsterAttack(problem);
        }
      }
    } else {
      view.showText("Invalid direction.");
    }
  }

  /**
   * Controller method to call the player's addItem method.
   * Print message based on the result.
   *
   * @param itemName the name of the item that needs to be taken.
   */
  private void takeItem(String itemName) {
    //get room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    //get item
    Item itemAttempt = currentRoom.getItem(itemName);

    if (itemAttempt != null) {
      if (player.addItem(itemAttempt)) {
        currentRoom.removeEntity(itemAttempt);
        view.showText(itemName + " added to your inventory!");
        player.setScore(player.getScore() + itemAttempt.getValue());
      } else {
        view.showText("Sorry, you can not add " + itemName + " to your bag. Because"
                + "  your bag is full.");
      }
    } else {
      view.showText("Sorry, you can not add " + itemName + " to your bag. Because"
              + " the item is not in the room .");
    }
  }

  /**
   * Controller method to call the player's delete method.
   *
   * @param itemName the name of the item that needs to be dropped.
   */
  private void dropItem(String itemName) {
    Item item = player.getEntity(itemName, Item.class);
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // Check if player has this item
    if (item != null) {
      player.removeItem(item);
      currentRoom.addEntity(item);
      view.showText(itemName + " dropped here in " + currentRoom.getName());
      player.setScore(player.getScore() - item.getValue());
    } else {
      view.showText("Sorry, you don't have " + itemName + " in your bag");
    }
  }

  /**
   * Look around the current room.
   */
  private void lookAround() {
    // Get the current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());

    System.out.println("Looking around room: " + currentRoom.getName());

    // check if room has problem
    view.showText("You are currently standing in the " + currentRoom.getName());
    view.showText(currentRoom.getDescription());

    // deal with monster attack
    IProblem<?> problem = currentRoom.getProblem();
    handleMonsterAttack(problem);

    //get items keys from the room
    String itemNames = currentRoom.getElementNames(Item.class);
    //get fixtures keys from the room
    String fixtureNames = currentRoom.getElementNames(Fixture.class);

    // show the items, fixtures, problem in the room
    view.showText(itemNames.isEmpty() ? "There is no items here" : "Items you see here: "
            + "\n" + itemNames);
    view.showText(fixtureNames.isEmpty() ? "There is no fixtures here" : "Fixtures you see here: "
            + "\n" + fixtureNames);
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
    IProblem<?> problem = currentRoom.getProblem();

    if (problem == null) {
      view.showText("You are trying to use " + itemName
              + " but nothing interesting happens");
      return;
    }

    //check solution type
    Class<?> solutionClass = problem.getSolution().getClass(); // 获取solution的Class对象
    // check if the prolem is a IProblem<Item>
    if (solutionClass != Item.class) {
      view.showText("You are trying to use " + itemName
              + " but nothing interesting happens");
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

    // check if player has this item.
    boolean hasItem = player.hasEntity(itemName);
    if (!hasItem) {
      view.showText("You don't have " + itemName
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
        view.showText("You are trying to use " + itemName
                + " but nothing interesting happens");
        return;
      case 1:
        if (!itemAttempt.use()) {
          view.showText(itemName + " is empty "
                  + "or cannot be used again.");
          handleMonsterAttack(problem);
          return;
        }
        view.showText(itemAttempt.getWhenUsed());
        handleProblemSolved(problem);
        return;
      case 2:
        view.showText("You are trying to use " + itemName
                + " but nothing interesting happens");
        handleMonsterAttack(problem);
    }
  }

  /**
   * Answer a puzzle.
   *
   * @param objectName the answer to the puzzle
   */
  private void answer(String objectName) {
    // get the current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // get puzzle or moster in the room
    IProblem<?> problem = currentRoom.getProblem();
    if (problem == null) {
      view.showText("There is no question to answer in this room.");
      return;
    }

    if (objectName == null || objectName.isEmpty()) {
      view.showText("Please provide an answer.");
      handleMonsterAttack(problem);
      return;
    }

    // check if the puzzle is solved
    if (!problem.getActive()) {
      view.showText("The puzzle is already solved.");
      return;
    }

    // check if the solution is a string
    if (!(problem.getSolution() instanceof String)) {
      view.showText("You are trying to answer a question, "
              + "but nothing interesting happens.");
      handleMonsterAttack(problem);
      return;
    }

    Problem<String> problemString = (Puzzle<String>) problem;

    // solve the problem
    int flag = problemString.solve(objectName);
    switch (flag) {
      case 0:
        view.showText("You are trying to answer the question, "
                + "but nothing interesting happens.");
        return;
      case 1:
        view.showText("You have successfully solved the puzzle!");
        handleProblemSolved(problem);
        return;
      case 2:
        view.showText("You are trying to answer the question, "
                + "but the answer is wrong.");
        handleMonsterAttack(problem);
    }
  }

  /**
   * Check the player's inventory.
   */
  private void checkInventory() {
    // Logic to check inventory
    Map<String, Item> items = player.getEntities();
    if (items.isEmpty()) {
      view.showText("There is nothing in your inventory.");
    } else {
      view.showText("Items in your inventory: ");
      String itemList = String.join(", ", items.keySet());
      view.showText(itemList);
    }
  }

  /**
   * Examine an item.
   */
  private void examine(String entityName) {
    // Logic to examine item
    // get current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    IIdentifiableEntity entity = currentRoom.getEntity(entityName, Item.class);
    if (entity == null) {
      entity = currentRoom.getEntity(entityName, Fixture.class);
    }
    if (entity == null) {
      view.showText(entityName + " is not in the room.");
    } else {
      view.showText(entity.getDescription());
    }
  }

  /**
   * Unlocks room.
   *
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
    view.showText("Quitting...");
    System.exit(0);
  }

  /**
   * Save the game state.
   */
  private void save() {
    try {
      String gameFileName = gameWorld.getName() + ".json";
      String playerFileName = player.getName() + ".json";

      GameDataSaver.saveGameJson(gameFileName, gameWorld);
      GameDataSaver.savePlayerJson(playerFileName, player);

      view.showText("Game saved successfully as " + gameFileName + " and " + playerFileName);
    } catch (Exception e) {
      view.showText("Failed to save game: " + e.getMessage());
    }
  }

  /**
   * Restore the game state.
   */
  private void restore() {
    try {
      String gameFileName = gameWorld.getName() + ".json";
      String playerFileName = player.getName() + ".json";

      GameWorld newGameWorld = GameDataLoader.loadGameWorld(gameFileName);
      Player newPlayer = GameDataLoader.loadPlayer(playerFileName, newGameWorld);

      this.gameWorld = newGameWorld;
      this.player = newPlayer;

      view.showText("Game restored successfully from " + gameFileName + " and " + playerFileName);
    } catch (Exception e) {
      view.showText("Failed to restore game: " + e.getMessage());
    }
  }

  /**
   * Handle the situation when the problem is successfully solved.
   * @param problem the problem that will be solved
   */
  private void handleProblemSolved(IProblem<?> problem) {
    view.showText("You have successfully"
            + (problem instanceof Puzzle<?> ? " solved " : " killed ")
            + problem.getName());
    problem.setActive(false); // set problem to inactive

    // deal with score
    int points = problem.getValue();
    player.addScore(points); // update score
    view.showText("Your score" + " + " + points + ". Current Score is " + player.getScore());

    if (problem.getAffectsTarget()) {
      String problemTarget = problem.getTarget();
      String[] parts = problemTarget.split(":", 2);
      int roomNumber = Integer.parseInt(parts[0].trim());
      String roomName = parts[1].trim();
      unlockExits(gameWorld.getRoom(roomNumber));
      view.showText("The room " + roomName + " is unlocked.");
    }
  }

  /**
   * Handle situation when monster attacks the player by checking its attack effect and dealing
   * with the damage.
   * @param problem the puzzle/monster that might attack player
   */
  private void handleMonsterAttack(IProblem<?> problem) {
    if (problem instanceof Monster) {
      Monster<?> monster = (Monster) problem;
      if (monster.getAffectsPlayer() && monster.getCanAttack()) {
        monster.attack(player);
        view.showText(monster.getAttack());
        view.showText("Player takes " + monster.getDamage() + " damage!");
        view.showText(player.checkStatus().getMessage());
      }
    }
    if (player.checkStatus() == HEALTH_STATUS.SLEEP) {
      view.showText("Game ends...");
      quit();
    }
  }
}