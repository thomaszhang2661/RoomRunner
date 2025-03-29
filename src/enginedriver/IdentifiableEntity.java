package enginedriver;

import java.awt.Image;

/**
 * Class for entities that have an ID, name, and description.
 */
public abstract class IdentifiableEntity
        implements IIdentifiableEntity {
  private final int id;
  private final String name;
  private final String description;

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
   * Constructor for an identifiable entity, without ID.
   */
  public IdentifiableEntity( String name, String description) {
    this.id = -1;
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the unique identifier of the entity.
   *
   * @return String for id
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Returns the name of the entity.
   * @return String for name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   *  Returns the description of the entity.
   * @return  String for description
   */
  @Override
  public String getDescription() {
    return description;
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
