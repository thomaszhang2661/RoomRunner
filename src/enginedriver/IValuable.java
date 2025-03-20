package enginedriver;

/**
 * Interface for entities that have a value.
 */
public interface IValuable extends IIdentifiableEntity {

  /**
   * Returns the value of the entity.
   * @return int for value
   */
  int getValue();
}
