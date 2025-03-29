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
   * Constructor for an identifiable entity: empty container.
   */
  public EntityContainer(int id, String name, String description) {
    super(id, name, description);
    this.stringEntityMap = null;
  }

  /**
   * Constructor for an identifiable entity: container with entities.
   */
  public EntityContainer(int id, String name, String description, Map<String, T> entityNames) {
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
    return stringEntityMap;
  }

  /**
   * Get one entity from the container.
   */
  public <U extends IdentifiableEntity> U getEntity(String entityName, Class<U> clazz) {
    Object entity = stringEntityMap.get(entityName);
    if (clazz.isInstance(entity)) {
      return clazz.cast(entity);  // 安全地进行类型转换
    }
    return null;
  }

  /**
   * Get the list of entities from the container according to the type.
   */
  public <U> List<U> getEntitiesByType(Class<U> clazz) {
    List<U> result = new ArrayList<>();
    for (T entity : stringEntityMap.values()) {
      if (clazz.isInstance(entity)) {
        result.add(clazz.cast(entity));  // 安全类型转换
      }
    }
    return result;
  }

  //  public <T>  T  getEntity(String entityName) {
  //    return (T) stringEntityMap.get(entityName);
  //  }
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
    return stringEntityMap.containsKey(entity.getName());
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean hasEntity(String entityName) {
    return stringEntityMap.containsKey(entityName);
  }


}



//package enginedriver;

//
//import java.util.Map;
//
///**
// *  Class for a container of entities.
// */
//abstract class EntityContainer extends IdentifiableEntity {
//
//  private Map<String,IdentifiableEntity> entityNames;
//
//  /**
//   * Constructor for an identifiable entity: empty container.
//   */
//  public EntityContainer(int id, String name, String description) {
//    super(id, name, description);
//  }
//
//  /**
//   * Constructor for an identifiable entity: container with entities.
//   */
//  public EntityContainer(int id, String name,
//                         String description,
//                         Map<String,IdentifiableEntity> entityNames) {
//    super(id, name, description);
//    this.entityNames = entityNames;
//  }
//
//
//  /**
//   * Adds an entity to the container.
//   */
//  public void addEntity(IdentifiableEntity entity) {
//    entityNames.put(entity.getName(), entity);
//  }
//
//  /**
//   * Remove an entity from the container.
//   */
//  public void removeEntity(IdentifiableEntity entity) {
//    entityNames.remove(entity.getName());
//  }
//
//  /**
//   * Check if the container has an entity.
//   */
//  public Boolean  hasEntity(IdentifiableEntity entity) {
//    return entityNames.containsKey(entity.getName());
//  }
//
//  /**
//   * Check if the container has an entity.
//   */
//  public Boolean  hasEntity(String entityName) {
//    return entityNames.containsKey(entityName);
//  }
//
//  /**
//   * get all entities from the container.
//   */
//  public Map<String,IdentifiableEntity> getEntities() {
//    return entityNames;
//  }
//}
