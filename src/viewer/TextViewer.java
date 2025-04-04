package viewer;

/**
 * Text that can be viewed for hw8.
 * This part will support pictures in hw9.
 * Use singleton pattern.
 */
public class TextViewer {

  private static TextViewer instance = null;

  /**
   * Constructor for the viewer.
   * This constructor is private to prevent instantiation.
   */
  private TextViewer() {
  }

  /**
   * Returns the instance of the viewer.
   */
  public static TextViewer getInstance() {
    if (instance == null) {
      instance = new TextViewer();
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
