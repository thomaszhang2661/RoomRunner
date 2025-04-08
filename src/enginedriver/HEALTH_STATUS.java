package enginedriver;

/**
 * Enum for the health status of the player.
 */
public enum HEALTH_STATUS {
  AWAKE("The player is still healthy and awake."),
  FATIGUED("The player is taking some damages and feeling fatigued."),
  SLEEP("The player has lost all the HP and has fallen asleep."),
  WOOZY("The player is taking too much damage and feeling woozy right now!");

  private final String message;

  HEALTH_STATUS(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
