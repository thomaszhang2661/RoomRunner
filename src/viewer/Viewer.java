package viewer;

/**
 * Interface for the viewer.
 * This interface is used to show text.
 * This interface is used in the controller.
 */
public interface Viewer {

  /**
   *  text output.
   */
  void showText(String text);

  /**
   *  moving to  the next room
   */
  EXIT getNextRoom();
}
