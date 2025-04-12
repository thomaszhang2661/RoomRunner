package enginedriver.view;

/**
 * Abstract base class for different view implementations.
 * This is an updated version of the original View class,
 * refactored to support multiple view types.
 */
public abstract class View {

  private static View instance = null;

  /**
   * Constructor for the viewer.
   * This constructor is protected to allow inheritance.
   */
  protected View() {
  }

  /**
   * Returns the instance of the viewer.
   * Default implementation returns a text-based view.
   *
   * @return the View instance
   */
  public static View getInstance() {
    if (instance == null) {
      instance = new TextView();
    }
    return instance;
  }

  /**
   * Sets the active view implementation.
   * This allows switching between different view types.
   *
   * @param newView the new view instance
   */
  public static void setInstance(View newView) {
    instance = newView;
  }

  /**
   * Shows text to the user.
   * Each view implementation will handle this differently.
   *
   * @param text the text to display
   */
  public abstract void showText(String text);

  /**
   * Simple text-based view implementation that outputs to console.
   * This preserves the original View behavior.
   */
  private static class TextView extends View {
    @Override
    public void showText(String text) {
      System.out.println(text);
    }
  }
}