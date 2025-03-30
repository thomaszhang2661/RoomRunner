package enginedriver;

import java.awt.Image;
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
   */
  public IdentifiableEntity(int id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.pictureName = "";
  }

  /**
   * Constructor for an identifiable entity, without ID.
   */
  public IdentifiableEntity(String name, String description) {
    this.id = -1;
    this.name = name;
    this.description = description;
    this.pictureName = null;

  }

  /**
   * Constructor for an identifiable entity, without ID.
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
  @Override
  public Image getPicture() {
    if (pictureName == null || pictureName.isEmpty()) {
      return null; // 处理空文件名
    }
    try {
      // 从文件系统加载图片（需指定图片路径）
      File imageFile = new File("data/images/" + pictureName);
      return ImageIO.read(imageFile);
    } catch (IOException e) {
      System.err.println("无法加载图片: " + pictureName);
      return null; // 加载失败时返回 null
    }
  }

  public String getPictureName() {
    return pictureName;
  }

  /**
   *  equals method for IdentifiableEntity.
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
   *  hashCode method for IdentifiableEntity.
   */
  @Override
  public int hashCode() {
    int result = Integer.hashCode(id);
    result = 31 * result + name.hashCode();
    result = 31 * result + description.hashCode();
    return result;
  }
}
