package enginedriver;

import java.util.Map;

/**
 *  Class for a container of entities.
 */
abstract class EntityContainer extends IdentifiableEntity {

  private Map<String,IdentifiableEntity> entityNames;

  /**
   * Constructor for an identifiable entity: empty container.
   */
  public EntityContainer(int id, String name, String description) {
    super(id, name, description);
  }

  /**
   * Constructor for an identifiable entity: container with entities.
   */
  public EntityContainer(int id, String name,
                         String description,
                         Map<String,IdentifiableEntity> entityNames) {
    super(id, name, description);
    this.entityNames = entityNames;
  }


  /**
   * Adds an entity to the container.
   */
  public void addEntity(IdentifiableEntity entity) {
    entityNames.put(entity.getName(), entity);
  }

  /**
   * Remove an entity from the container.
   */
  public void removeEntity(IdentifiableEntity entity) {
    entityNames.remove(entity.getName());
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean  hasEntity(IdentifiableEntity entity) {
    return entityNames.containsKey(entity.getName());
  }

  /**
   * Check if the container has an entity.
   */
  public Boolean  hasEntity(String entityName) {
    return entityNames.containsKey(entityName);
  }



}
