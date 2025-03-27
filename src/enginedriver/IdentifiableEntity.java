package enginedriver;

import java.awt.Image;

/**
 * Class for entities that have an ID, name, and description.
 */
public class IdentifiableEntity implements IIdentifiableEntity{
  private int id;
  private String name;
  private String description;

  /**
   * Constructor for an identifiable entity.
   * Room can use field of id.
   */
  public IdentifiableEntity(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  /**
   * Constructor for an identifiable entity, with out ID.
   */
  public IdentifiableEntity( String name, String description) {
    this.id = -1;
    this.name = name;
    this.description = description;
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
