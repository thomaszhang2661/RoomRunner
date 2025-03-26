package enginedriver;

import java.awt.Image;

/**
 * Class for entities that have an ID, name, and description.
 */
public class IdentifiableEntity implements IIdentifiableEntity{

  /**
   * Constructor for an identifiable entity.
   */
  public IdentifiableEntity(String id, String name, String description) {
  }

  /**
   * Returns the unique identifier of the entity.
   * @return String for id
   */
  @Override
  public String getId() {
    return "";
  }

  /**
   * Returns the name of the entity.
   * @return String for name
   */
  @Override
  public String getName() {
    return "";
  }

  /**
   *  Returns the description of the entity.
   * @return  String for description
   */
  @Override
  public String getDescription() {
    return "";
  }

  /**
   * Returns the picture of the entity.
   * @return  Image for picture
   */
  @Override
  public Image getPicture() {
    return null;
  }
}
