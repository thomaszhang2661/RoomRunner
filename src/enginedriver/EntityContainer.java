package enginedriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Class for a container of entities.
 */
abstract class EntityContainer<T extends IdentifiableEntity> extends IdentifiableEntity {

  private Map<String, T> stringEntityMap;  // 泛型类型 T 代替 IdentifiableEntity

  /**
   * Constructor for an identifiable entity: empty container, picture name.
   */
  protected EntityContainer(int id, String name, String description, String pictureName) {
    super(id, name, description, pictureName);
    //分配一个新的 HashMap 实例，用于存储实体
    this.stringEntityMap = new java.util.HashMap<>();
  }

  /**
   * Constructor for an identifiable entity: empty container.
   */
  protected EntityContainer(int id, String name, String description) {
    super(id, name, description);
    //分配一个新的 HashMap 实例，用于存储实体
    this.stringEntityMap = new java.util.HashMap<>();
  }

  /**
   * Constructor for an identifiable entity: container with entities.
   */
  protected EntityContainer(int id, String name, String description, Map<String, T> entityNames, String pictureName) {
    super(id, name, description, pictureName);
    this.stringEntityMap = entityNames;
  }

  /**
   * Constructor for an identifiable entity: container with entities.
   */
  protected EntityContainer(int id, String name, String description, Map<String, T> entityNames) {
    super(id, name, description);
    this.stringEntityMap = entityNames;
  }


  /**
   * set entities to the container.
   */
  public void setEntities(Map<String, T> entityNames) {
    this.stringEntityMap = entityNames;
  }

  /**
   * Get all entities from the container.
   */
  public Map<String, T> getEntities() {
    if (stringEntityMap == null) {
      return null;
    }
    return stringEntityMap;
  }

  /**
   * Get one entity from the container.
   */
  public  <U extends IdentifiableEntity> U getEntity(String entityName, Class<U> clazz) {
    Object entity = stringEntityMap.get(entityName);
    if (clazz.isInstance(entity)) {
      return clazz.cast(entity);  // 安全地进行类型转换
    }
    return null;
  }

  /**
   * Get the list of entities from the container according to the type.
   */
  public  <U> List<U> getEntitiesByType(Class<U> clazz) {
    List<U> result = new ArrayList<>();
    for (T entity : stringEntityMap.values()) {
      if (clazz.isInstance(entity)) {
        result.add(clazz.cast(entity));  // 安全类型转换
      }
    }
    return result;
  }


  /**
   * Adds an entity to the container.
   */
  public boolean addEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    if (stringEntityMap.containsKey(entity.getName())) {
      return false;
    }
    stringEntityMap.put(entity.getName(), entity);
    return true;
  }

  /**
   * Remove an entity from the container.
   */
  public boolean removeEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    return stringEntityMap.remove(entity.getName()) != null;
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean hasEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    if (stringEntityMap == null) {
      return  false;
    }
    return stringEntityMap.containsKey(entity.getName());
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean hasEntity(String entityName) {
    if (stringEntityMap == null) {
      return false;
    }
    return stringEntityMap.containsKey(entityName);
  }


}


