package enginedriver;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import enginedriver.model.entity.Fixture;
import enginedriver.model.GameWorld;
import enginedriver.model.entitycontainer.HEALTH_STATUS;
import enginedriver.model.entity.IdentifiableEntity;
import enginedriver.model.entity.Item;
import enginedriver.model.entitycontainer.Player;
import enginedriver.model.entitycontainer.Room;
import enginedriver.model.problems.Monster;
import enginedriver.model.problems.Problem;
import enginedriver.model.problems.Puzzle;
import enginedriver.model.problems.IProblem;
import enginedriver.jsonreader.GameDataLoader;
import enginedriver.jsonreader.GameDataSaver;
import enginedriver.view.IView;

/**
 * GameController class to handle game logic and player commands.
 * This is the Controller component in the MVC architecture.
 */
public class GameController {
  private static final String RESOURCE_FILE = "resources/";
  private String rawFileName = null;
  private boolean isBatchMode = false;

  private Player player;
  private GameWorld gameWorld;
  private IView view;
  private Map<String, String> actionMap = new HashMap<>();

  /**
   * Constructor for GameController.
   *
   * @param gameWorld The game world
   * @param player The player
   */
  public GameController(GameWorld gameWorld, Player player) {
    this.gameWorld = gameWorld;
    this.player = player;
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

  public void setRawFileName(String rawFileName) {
    this.rawFileName = rawFileName;
  }

  /**
   * Set the view for the game controller.
   *
   * @param viewer The game view
   */
  public void setView(IView viewer) {
    this.view = viewer;
  }


  /**
   * Set whether the controller is running in batch mode
   *
   * @param batchMode Whether in batch mode
   */
  public void setBatchMode(boolean batchMode) {
    this.isBatchMode = batchMode;
  }

  /**
   * Process the command entered by the player.
   *
   * @param command The command entered by the player
   * @return
   */
  public ActionListener processCommand(String command) {
    // Standardize the command
    String[] commandParts = standardizeCommand(command);

    // Extract action and object name
    String action = commandParts[0];
    String objectName = commandParts[1];

    switch (action.toUpperCase()) {
      case "N":
        move("N");
        break;
      case "S":
        move("S");
        break;
      case "E":
        move("E");
        break;
      case "W":
        move("W");
        break;
      case "T":
        takeItem(objectName);
        break;
      case "D":
        dropItem(objectName);
        break;
      case "L":
        lookAround();
        break;
      case "U":
        useItem(objectName);
        break;
      case "I":
        checkInventory();
        break;
      case "X":
        examine(objectName);
        break;
      case "A":
        answer(objectName);
        break;
      case "Q":
        quit();
        break;
      case "SAVE":
        save();
        break;
      case "RESTORE":
        restore();
        break;
      default:
        view.showText("Invalid command.");
        break;
    }

    // Update the view after processing command
    view.update();
    return null;
  }

  /**
   * Get the game world.
   *
   * @return The game world
   */
  public GameWorld getGameWorld() {
    return gameWorld;
  }

  /**
   * Get the player.
   *
   * @return The player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Standardize the command entered by the player.
   *
   * @param command The command entered by the player
   * @return The standardized command as a string array [action, object]
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
   * Capitalize the words in a phrase to align with the JSON style.
   *
   * @param phrase The phrase to capitalize
   * @return The capitalized phrase
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
   *
   * @param direction The direction to move (N, S, E, W)
   */
  private void move(String direction) {
    // Check player's current room
    int currentRoom = player.getRoomNumber();
    Room<?> currentRoomObj = gameWorld.getRoom(currentRoom);

    // Get room exits
    Map<String, Integer> exits = currentRoomObj.getExits();

    // Check if the direction is valid
    if (exits.containsKey(direction)) {
      int attemptRoomNum = exits.get(direction);
      if (attemptRoomNum < 0) {
        view.showText("The direction is blocked.");
        return;
      } else if (attemptRoomNum == 0) {
        view.showText("Invalid direction, there is no more room in this direction.");
      } else {
        // Get the room that player is going to enter
        Room enteredRoom = gameWorld.getRoom(attemptRoomNum);
        // Move player to the new room
        player.setRoomNumber(attemptRoomNum);
        // Show description when entering
        view.showText("You are moving " + getDirectionName(direction) + ", entered " +
                enteredRoom.getName() + ", room number " + attemptRoomNum);

        // Room description
        view.showText(enteredRoom.getDescription());

        // Get problem from the room
        IProblem<?> problem = enteredRoom.getProblem();

        if (problem != null && problem.getActive()) {
          // Show the problem effect on entering
          view.showText(problem.getEffects());
          view.showProblem(problem);
          // Deal with monster attack
          handleMonsterAttack(problem);
        } else {
          view.showProblem(null);

        }
      }
    } else {
      view.showText("Invalid direction.");
    }
  }

  /**
   * Get the full name of a direction.
   *
   * @param direction The direction code (N, S, E, W)
   * @return The full direction name
   */
  private String getDirectionName(String direction) {
    switch (direction) {
      case "N":
        return "north";
      case "S":
        return "south";
      case "E":
        return "east";
      case "W":
        return "west";
      default:
        return direction;
    }
  }

  /**
   * Take an item from the current room.
   *
   * @param itemName The name of the item to take
   */
  private void takeItem(String itemName) {
    // Get current room
    Room currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // Get item
    Item itemAttempt = currentRoom.getItem(itemName);

    if (itemAttempt != null) {
      if (player.addItem(itemAttempt)) {
        currentRoom.removeEntity(itemAttempt);
        view.showText(itemName + " added to your inventory!");
        player.setScore(player.getScore() + itemAttempt.getValue());
      } else {
        view.showText("Sorry, you cannot add " + itemName + " to your bag. Your bag is full.");
      }
    } else {
      view.showText("Sorry, you cannot add " + itemName + " to your bag. The item is not in the room.");
    }
  }

  /**
   * Drop an item from the player's inventory.
   *
   * @param itemName The name of the item to drop
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
    // Get current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());

    // Show room info
    view.showText("You are currently standing in the " + currentRoom.getName());
    view.showText(currentRoom.getDescription());

    // Check for monster attack
    IProblem<?> problem = currentRoom.getProblem();
    handleMonsterAttack(problem);

    // Get items and fixtures in the room
    String itemNames = currentRoom.getElementNames(Item.class);
    String fixtureNames = currentRoom.getElementNames(Fixture.class);

    // Show the items and fixtures
    view.showText(itemNames.isEmpty() ? "There are no items here" : "Items you see here: " +
            "\n" + itemNames);
    view.showText(fixtureNames.isEmpty() ? "There are no fixtures here" : "Fixtures you see here: " +
            "\n" + fixtureNames);
  }

  /**
   * Use an item.
   *
   * @param itemName The name of the item to use
   */
  private void useItem(String itemName) {
    // Get current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());

    // Get current problem
    IProblem<?> problem = currentRoom.getProblem();

    if (problem == null) {
      view.showText("You are trying to use " + itemName + " but nothing interesting happens");
      return;
    }

    Object solution = problem.getSolution();

    if (solution == null) {
      // 根据你的业务逻辑，可以抛异常、给提示、跳过处理等：
      System.out.println("Warning: problem solution is null!");
      return;
    }

    // Check solution type
    Class<?> solutionClass = problem.getSolution().getClass();
    // Check if the problem is an IProblem<Item>
    if (solutionClass != Item.class) {
      view.showText("You are trying to use " + itemName + " but nothing interesting happens");
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

    // Check if player has this item
    boolean hasItem = player.hasEntity(itemName);
    if (!hasItem) {
      view.showText("You don't have " + itemName + " in your inventory.");
      // Deal with monster attack
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

    // Get item
    Item itemAttempt = player.getEntity(itemName, Item.class);

    Problem<Item> itemProblem = (Problem<Item>) problem;
    // Solve problem
    int flag = itemProblem.solve(itemAttempt);
    switch (flag) {
      case 0:
        view.showText("You are trying to use " + itemName + " but nothing interesting happens");
        return;
      case 1:
        if (!itemAttempt.use()) {
          view.showText(itemName + " is empty or cannot be used again.");
          handleMonsterAttack(problem);
          return;
        }
        view.showText(itemAttempt.getWhenUsed());
        handleProblemSolved(problem);
        return;
      case 2:
        view.showText("You are trying to use " + itemName + " but nothing interesting happens");
        handleMonsterAttack(problem);
    }
  }

  /**
   * Answer a puzzle.
   *
   * @param objectName The answer to the puzzle
   */
  private void answer(String objectName) {
    // Get the current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // Get puzzle or monster in the room
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

    // Check if the puzzle is solved
    if (!problem.getActive()) {
      view.showText("The puzzle is already solved.");
      return;
    }

    // Check if the solution is a string
    if (!(problem.getSolution() instanceof String)) {
      view.showText("You are trying to answer a question, but nothing interesting happens.");
      handleMonsterAttack(problem);
      return;
    }

    Problem<String> problemString = (Puzzle<String>) problem;

    // Solve the problem
    int flag = problemString.solve(objectName);
    switch (flag) {
      case 0:
        view.showText("You are trying to answer the question, but nothing interesting happens.");
        return;
      case 1:
        view.showText("You have successfully solved the puzzle!");
        handleProblemSolved(problem);
        return;
      case 2:
        view.showText("You are trying to answer the question, but the answer is wrong.");
        handleMonsterAttack(problem);
    }
  }

  /**
   * Check the player's inventory.
   */
  private void checkInventory() {
    // Get inventory items
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
   * Examine an item or fixture.
   *
   * @param entityName The name of the entity to examine
   */
  private void examine(String entityName) {
    // Get current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    Map<String, IdentifiableEntity> roomEntities = currentRoom.getEntities();
    Map<String, Item> playerEntities = player.getEntities();
    roomEntities.putAll(playerEntities);
    // Check if the entity is in the room or in the player's inventory
    IdentifiableEntity entity = roomEntities.get(entityName);


    if (entity == null) {
      view.showText(entityName + " is not in the room or in your inventory.");
    } else {
      view.showText(entity.getDescription());
      view.showEntity(entity);
    }
  }

  /**
   * Unlocks room exits.
   *
   * @param room The room to unlock exits for
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

    // In batch mode, we don't want to call System.exit() as we want to complete processing
    // This allows the batch file to be fully processed
    if (!isBatchMode) {
      System.exit(0);
    }
    // In batch mode, the method just returns, allowing processing to continue
  }

  /**
   * Save the game state.
   */
  private void save() {
    try {
      String gameFileName = RESOURCE_FILE + rawFileName + "_" + player.getName() + ".json";

      GameDataSaver.saveGameJson(gameFileName, new GameController(gameWorld, player));

      view.showText("Game saved successfully as " + gameFileName);
    } catch (Exception e) {
      view.showText("Failed to save game: " + e.getMessage());
    }
  }

  /**
   * Restore the game state.
   */
  private void restore() {
    try {
      String gameFileName = RESOURCE_FILE + rawFileName + "_" + player.getName() + ".json";

      GameController restoredGame = GameDataLoader.loadGame(gameFileName);
      GameWorld restoredWorld = restoredGame.getGameWorld();
      Player restoredPlayer = restoredGame.getPlayer();

      this.gameWorld = restoredWorld;
      this.player = restoredPlayer;

      view.update();

      view.showText("Game restored successfully from " + gameFileName);
    } catch (Exception e) {
      view.showText("Failed to restore game: " + e.getMessage());
    }
  }

  /**
   * Handle the situation when the problem is successfully solved.
   * @param problem The problem that has been solved
   */
  private void handleProblemSolved(IProblem<?> problem) {
    view.showText("You have successfully"
            + (problem instanceof Puzzle<?> ? " solved " : " defeated ")
           + problem.getName());
    problem.setActive(false); // Set problem to inactive
    view.showProblem(null);

    // Update score
    int points = problem.getValue();
    player.addScore(points);
    view.showText("Your score increased by " + points + ". Current score is " + player.getScore());

    // Check if problem affects a target
    if (problem.getAffectsTarget()) {
      String problemTarget = problem.getTarget();
      String[] parts = problemTarget.split(":", 2);
      int roomNumber = Integer.parseInt(parts[0].trim());
      String roomName = parts[1].trim();
      unlockExits(gameWorld.getRoom(roomNumber));
      view.showText("The room " + roomName + " is now unlocked.");
    }
  }

  /**
   * Handle situation when monster attacks the player.
   *
   * @param problem The puzzle/monster that might attack player
   */
  private void handleMonsterAttack(IProblem<?> problem) {
    if (problem instanceof Monster<?> monster) {
      if (monster.getAffectsPlayer() && monster.getCanAttack()) {
        monster.attack(player);
        view.showText(monster.getAttack());
        view.showText("Player takes " + monster.getDamage() + " damage!");
        view.showText(player.checkStatus().getMessage());
        view.showProblem(monster);
      } else {
        view.showProblem(null);
      }
    }

    // Check if player has died
    if (player.checkStatus() == HEALTH_STATUS.SLEEP) {
      view.showText("Game over...");
      quit();
    }
  }
}