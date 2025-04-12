package enginedriver.model.entity;

import java.awt.image.BufferedImage;

/**
 * Interface for entities that have an ID, name, and description.
 */
public interface IIdentifiableEntity {

  /**
   * Returns the unique identifier of the entity.
   */
  int getId();

  /**
   * Returns the name of the entity.
   */
  String getName();

  /**
   * Returns the description of the entity.
   */
  String getDescription();

  /**
   * Returns the picture of the entity.
   */
  BufferedImage getPicture();

  /**
   * Returns the name of the picture of the entity.
   */
  String getPictureName();

  /**
   *  equals method for IdentifiableEntity.
   */
  boolean equals(Object obj);

  /**
   *  hashCode method for IdentifiableEntity.
   */
  int hashCode();
}
