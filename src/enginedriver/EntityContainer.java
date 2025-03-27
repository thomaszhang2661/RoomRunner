package enginedriver;

import java.util.Map;

/**
 *  Class for a container of entities.
 */
abstract class EntityContainer<T extends IdentifiableEntity> extends IdentifiableEntity {

  private Map<String, T> entityNames;  // 泛型类型 T 代替 IdentifiableEntity

  /**
   * Constructor for an identifiable entity: empty container.
   */
  public EntityContainer(int id, String name, String description) {
    super(id, name, description);
  }

  /**
   * Constructor for an identifiable entity: container with entities.
   */
  public EntityContainer(int id, String name, String description, Map<String, T> entityNames) {
    super(id, name, description);
    this.entityNames = entityNames;
  }

  /**
   * set entities to the container.
   */
  public void setEntities(Map<String, T> entityNames) {
    this.entityNames = entityNames;
  }

  /**
   * Adds an entity to the container.
   */
  public boolean addEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    if (entityNames.containsKey(entity.getName())) {
      return false;
    }
    entityNames.put(entity.getName(), entity);
    return true;
  }

  /**
   * Remove an entity from the container.
   */
  public boolean removeEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    return entityNames.remove(entity.getName()) != null;
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean hasEntity(T entity) {  // 使用 T 类型代替 IdentifiableEntity
    return entityNames.containsKey(entity.getName());
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean hasEntity(String entityName) {
    return entityNames.containsKey(entityName);
  }

  /**
   * Get all entities from the container.
   */
  public Map<String, T> getEntities() {
    return entityNames;
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
