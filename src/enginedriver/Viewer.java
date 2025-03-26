package enginedriver;

/**
 * Text that can be viewed for hw8.
 * This part will support pictures in hw9.
 *  Use singleton pattern.
 */
public class Viewer {

  private static Viewer instance = null;

  private Viewer() {
  }

  /**
   * Returns the instance of the viewer.
   */
  public static Viewer getInstance() {
    if (instance == null) {
      instance = new Viewer();
    }
    return instance;
  }

  /**
   * Shows the text.
   */
  public void showText(String text) {
    System.out.println(text);
  }


}
