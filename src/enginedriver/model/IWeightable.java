package enginedriver.model;

import enginedriver.model.entity.IIdentifiableEntity;

/**
 * Interface for entities that have a weight.
 */
public interface IWeightable extends IIdentifiableEntity {

  /**
   * Returns the weight of the entity.

   * @return int for weight
   */
  int getWeight();
}
