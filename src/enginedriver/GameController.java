package enginedriver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enginedriver.problems.Monster;
import enginedriver.problems.Puzzle;
import enginedriver.problems.IProblem;
import jsonreader.GameDataLoader;

/**
 * GameEngine class to handle game logic and player commands.
 */
public class GameController {
  private Player player;
  private GameWorld gameWorld;
  private Viewer viewer;
  private Map<String, String> actionMap = new HashMap<>();

  /**
   * Constructor for GameController.
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

  public GameWorld getGameWorld() {
    return gameWorld;
  }

  public Player getPlayer() {
    return player;
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
      case "A": answerPuzzle(objectName);
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
    String problemDescription = currentRoom.getProblem().getDescription();

    // show the items, fixtures, problem in the room
    viewer.showText("Items you see here: ");
    viewer.showText(String.join(", ", itemNames));
    viewer.showText("Fixtures you see here: ");
    viewer.showText(String.join(", ", fixtureNames));

  }




  /**
   * Use an item.
   */
  private void useItem(String itemName) {
    // get room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    boolean hasItem = player.hasEntity(itemName);

    if (!hasItem) {
      viewer.showText("You don't have " + itemName + " in your bag.");
      // deal with monster attack
      handleMonsterAttack(currentRoom.getProblem());
      return;
    }

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
    if (solutionClass == Item.class) {
      IProblem<Item> itemproblem = (IProblem<Item>) problem;
      int flag = itemproblem.solve(itemAttempt);
      if (flag == 1) {
        viewer.showText("You have successfully solved the problem with " + itemName);
        if (itemproblem.getAffectsTarget()) {
          itemproblem.getAffectsPlayer();
        }
        unlockExits(currentRoom); // update room exits

        int points = itemproblem.getValue();
        player.addScore(points); // update score
        viewer.showText("+ " + points + ". Current Score is " + player.getScore());
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

  //  private void handleProblemSolution(IProblem<?> problem, solution) {
  //    viewer.showText("You have successfully solved the puzzle!");
  //    // Unlock the exit of the room
  //    unlockExits(room);
  //    // Add the score to the player
  //    player.addScore(problem.getValue());
  //  }


  private void handleProblemSolution<T>(IProblem<T> problem, String solutionAttempt) {

    //useItem 逻辑
            // 1 判断 solution 类型
            //   getWhenUsed
            //  调用solve 如果成功   handleAffectTarget
            //  调用solve 如果失败  handleMonsterAttack
            //   如果是0，viewer 显示

    // Answer和Use共用逻辑


    // 4 处理结果
        //返回1，且affect_target,把房间打开，加分， viewer显示
        //返回2，处理怪物攻击， viewer显示（getAttack）
        // 返回0, 不处理，viewer显示
  }


    /**
     * Answer a puzzle.
     */
  private void answerPuzzle(String objectName) {

    if (objectName == null || objectName.isEmpty()) {
      viewer.showText("Please provide an answer.");
      return;
    }

    // get the current room
    Room<?> currentRoom = gameWorld.getRoom(player.getRoomNumber());
    // get puzzle or moster in the room
    IProblem<?> problem = currentRoom.getProblem();
    if (problem == null) {
      viewer.showText("There is no puzzle or monster in this room.");
      return;
    }
    // check if the problem is a puzzle
    if (!(problem instanceof Puzzle)) {
      viewer.showText("There is no puzzle in this room.");
      return;
    }

    Puzzle<?> puzzle = (Puzzle<?>) problem;

    // check if the puzzle is solved
    if (!puzzle.getActive()) {
      viewer.showText("The puzzle is already solved.");
      return;
    }

    // 确认puzzle类型
    // check if the solution is a string
    if (puzzle.getSolution() instanceof String) {
      Puzzle<String> puzzleString = (Puzzle<String>) puzzle;
      if (puzzleString.solve(objectName)) {
        //handlePuzzleSolution(currentRoom, puzzleString);

      } else {
        viewer.showText("Your answer is not right.");
      }
    } else {
      viewer.showText("Your answer is not right, try to use an item.");

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

  // 处理成功时解开房间
  private  void handleAffectTarget(IProblem<?> problem) {
    if (problem.getAffectsTarget()) {
      String problemTarget = problem.getTarget();
      String[] parts = problemTarget.split(":", 2);
      int roomNumber = Integer.parseInt(parts[0].trim());
      String roomName = parts[1].trim();
      unlockExits(gameWorld.getRoom(roomNumber));
      viewer.showText("The room " + roomName + " is unlocked.");
    }
  }

  //处理失败时怪物攻击
  private void handleMonsterAttack(IProblem<?> problem) {
    if (problem instanceof Monster ) {
      Monster<?> monster = (Monster) problem;
      if (monster.getAffectsPlayer() && monster.getCanAttack()) {
        monster.attack(player);
        viewer.showText(monster.getAttack());
      }
    }
  }

  @Override
  public String toString() {
    return "{ "
            + gameWorld.toString() + ","
            + "\"player\":" + player.toString()
            + " }";
  }
}
