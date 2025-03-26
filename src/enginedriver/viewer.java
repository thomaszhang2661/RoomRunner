package enginedriver;

/**
 * Text that can be viewed for hw8.
 * This part will support pictures in hw9.
 *  Use singleton pattern.
 */
public class viewer {

  private static viewer instance = null;

  private viewer() {
  }

  /**
   * Returns the instance of the viewer.
   */
  public static viewer getInstance() {
    if (instance == null) {
      instance = new viewer();
    }
    return instance;
  }

  /**
   * Shows the text.
   */
  public void show(String text) {
    System.out.println(text);
  }


}
