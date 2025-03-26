package enginedriver;

import java.util.Map;

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
  private String standarlizeCommand(String command) {
    //TODO
    return command.trim().toUpperCase();
  }

  /**
   * Process the command entered by the player.
   */
  public void processCommand(String command) {
    command = standarlizeCommand(command);
    switch (command.toUpperCase()) {
      case "N": move("N");
      break;
      case "S": move("S");
      break;
      case "E": move("E");
      break;
      case "W": move("W");
      break;
      case "T": takeItem();
      break;
      case "D": dropItem();
      break;
      case "L": lookAround();
      break;
      case "U": useItem();
      break;
      case "I": checkInventory();
      break;
      case "X": examineItem();
      break;
      case "A": answerPuzzle();
      break;
      case "Q": quit();
      break;
      case "SAVE": save();
      break;
      case "RESTORE": restore();
      break;
      default: System.out.println("Invalid command.");
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
      }
    }
  }

  /**
   * Take an item.
   */
  private void takeItem() {
    // Logic to take item
    //TODO
  }


  /**
   * Drop an item.
   */
  private void dropItem() {
    // Logic to drop item
    //TODO
  }

  private void lookAround() {
    // Logic to look around
    //TODO
  }

  /**
   * Use an item.
   */
  private void useItem() {
    // Logic to use item
    //TODO
  }

  /**
   * Check the player's inventory.
   */
  private void checkInventory() {
    // Logic to check inventory
    //TODO
  }

  /**
   * Examine an item.
   */
  private void examineItem() {
    // Logic to examine item
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
