package enginedriver;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Class for entities that have an ID, name, and description.
 */
public abstract class IdentifiableEntity
        implements IIdentifiableEntity {
  private final int id;
  private final String name;
  private final String description;
  private final String pictureName;

  /**
   * Constructor for an identifiable entity.
   * Room can use field of id.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   * @param pictureName the picture name of the entity
   */
  public IdentifiableEntity(int id, String name, String description, String pictureName) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.pictureName = pictureName;
  }

  /**
   * Constructor for an identifiable entity.
   * Room can use field of id.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   */
  public IdentifiableEntity(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.pictureName = "";
  }

  /**
   * Constructor for an identifiable entity, without ID.

   * @param name the name of the game entity
   * @param description the description of the entity
   */
  public IdentifiableEntity(String name, String description) {
    this.id = -1;
    this.name = name;
    this.description = description;
    this.pictureName = null;

  }

  /**
   * Constructor for an identifiable entity, without ID.

   * @param name the name of the game entity
   * @param description the description of the entity
   * @param pictureName the picture name of the entity
   */
  public IdentifiableEntity(String name, String description, String pictureName) {
    this.id = -1;
    this.name = name;
    this.description = description;
    this.pictureName = pictureName;
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
  public BufferedImage getPicture() {
    if (pictureName == null || pictureName.isEmpty()) {
      return null;
    }
    try {
      File imageFile = new File("data/images/" + pictureName);
      return ImageIO.read(imageFile);  // return BufferedImage
    } catch (IOException e) {
      System.err.println("Failed to load picture: " + pictureName);
      return null;
    }
  }

  /**
   * Returns the name of the picture of the entity.

   * @return  String for pictureName
   */
  public String getPictureName() {
    return pictureName;
  }

  /**
   * equals method for IdentifiableEntity.

   * @param obj the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    IdentifiableEntity that = (IdentifiableEntity) obj;
    return id == that.id
            && name.equals(that.name)
            && description.equals(that.description);
  }

  /**
   * hashCode method for IdentifiableEntity.

   * @return the hash code of the entity
   */
  @Override
  public int hashCode() {
    int result = Integer.hashCode(id);
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    return result;
  }
}
