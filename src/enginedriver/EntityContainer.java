package enginedriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Class for a container of entities.
 *  Entities are objects that can be used by the player.
 */
abstract class EntityContainer<T extends IdentifiableEntity> extends IdentifiableEntity {

  private Map<String, T> stringEntityMap;

  /**
   * Constructor for an identifiable entity: empty container, picture name.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   * @param pictureName the picture name of the entity
   */
  protected EntityContainer(int id, String name, String description, String pictureName) {
    super(id, name, description, pictureName);
    //分配一个新的 HashMap 实例，用于存储实体
    this.stringEntityMap = new java.util.HashMap<>();
  }

  /**
   * Constructor for an identifiable entity: empty container.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   */
  protected EntityContainer(int id, String name, String description) {
    super(id, name, description);
    //分配一个新的 HashMap 实例，用于存储实体
    this.stringEntityMap = new java.util.HashMap<>();
  }

  /**
   * Constructor for an identifiable entity: container with entities.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   * @param entityNames the map of entities
   * @param pictureName the picture name of the entity
   */
  protected EntityContainer(int id, String name, String description, Map<String, T> entityNames, String pictureName) {
    super(id, name, description, pictureName);
    this.stringEntityMap = entityNames;
  }

  /**
   * Constructor for an identifiable entity: container with entities.

   * @param id the id of the entity
   * @param name the name of the game entity
   * @param description the description of the entity
   * @param entityNames the map of entities
   */
  protected EntityContainer(int id, String name, String description, Map<String, T> entityNames) {
    super(id, name, description);
    this.stringEntityMap = entityNames;
  }


  /**
   * set entities to the container.

   * @param entityNames the map of entities
   */
  public void setEntities(Map<String, T> entityNames) {
    this.stringEntityMap = entityNames;
  }

  /**
   * Get all entities from the container.

   * @return Map<String, T> the map of entities
   */
  public Map<String, T> getEntities() {
    if (stringEntityMap == null) {
      return null;
    }
    return stringEntityMap;
  }

  /**
   * Get one entity from the container.

   * @param entityName the name of the entity
   * @param clazz the class of the entity
   * @return U the entity
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

   * @param clazz the class of the entity
   * @return List<U> the list of entities
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

   * @param entity the entity to add
   * @return true if the entity was added, false if it already exists
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

   * @param entity the entity to remove
   * @return true if the entity was removed, false if it did not exist
   */
  public boolean removeEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    return stringEntityMap.remove(entity.getName()) != null;
  }

  /**
   * Check if the container has an entity.

   * @param entity the entity to check
   * @return true if the entity exists, false otherwise
   */
  public Boolean hasEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    if (stringEntityMap == null) {
      return  false;
    }
    return stringEntityMap.containsKey(entity.getName());
  }

  /**
   * Check if the container has an entity.

   * @param entityName the name of the entity to check
   * @return true if the entity exists, false otherwise
   */
  public Boolean hasEntity(String entityName) {
    if (stringEntityMap == null) {
      return false;
    }
    return stringEntityMap.containsKey(entityName);
  }


}


