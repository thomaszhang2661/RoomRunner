package enginedriver;

/**
 * GameEngine class to handle game logic and player commands.
 */
public class GameController {
  private Player player;
  private GameWorld gameWorld;
  private Room currentRoom;

  public GameController(GameWorld gameWorld, Player player) {
    this.gameWorld = gameWorld;
    this.player = player;
  }

  public void processCommand(String command) {
    switch (command.toUpperCase()) {
      case "N": moveNorth(); break;
      case "S": moveSouth(); break;
      case "E": moveEast(); break;
      case "W": moveWest(); break;
      case "T": takeItem(); break;
      case "D": dropItem(); break;
      case "L": lookAround(); break;
      case "U": useItem(); break;
      case "I": checkInventory(); break;
      case "X": examineItem(); break;
      case "A": answerPuzzle(); break;
      case "Q": quit(); break;
      case "save": save(); break;
      case "restore": restore(); break;
      default: System.out.println("Invalid command.");
    }
  }
  private void moveNorth() {
    // Logic to move player north
  }
  private void moveSouth() {
    // Logic to move player south
  }
  private void moveEast() {
    // Logic to move player east
  }

  private void moveWest() {
    // Logic to move player west
  }

  /**
   * Controller method to call the player's addItem method.
   * Print message based on the result.
   * @param item the item needs to be taken.
   */
  private void takeItem(Item item) {
    if (currentRoom.getItems().contains(item)
            && player.addItem(item)) {
      // todo
      System.out.println();
    } else {
      // todo
      System.out.println();
    }
  }

  /**
   * Controller method to call the player's delete method.
   * @param item the item needs to be dropped.
   */
  private void dropItem(Item item) {
    if (player.deleteItem(item)) {
      // todo
      System.out.println();
    } else {
      // todo
      System.out.println();
    }
  }

  private void lookAround() {
    // Logic to look around
  }

  private void useItem() {
    // Logic to use item
  }

  private void checkInventory() {
    // Logic to check inventory
  }

  private void examineItem() {
    // Logic to examine item
  }

  private void answerPuzzle() {
    // Logic to answer puzzle
  }

  private void quit() {
    // Logic to quit the game
  }

  private void save() {
    // Logic to save the game state
  }

  private void restore() {
    // Logic to restore the game state
  }

  // Additional methods for game logic can be added here
  private void record() {
    // Logic to record the game state for replay
    // 区分用户操作是否对gameworld造成了影响，
    //  如果有影响，记录操作和变化

  }
}
