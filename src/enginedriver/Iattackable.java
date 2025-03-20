package enginedriver;

/**
 * Interface for entities that can be attacked and have health.
 */
public interface  Iattackable extends IIdentifiableEntity {

  /**
   *  Reduces the health of the entity by the specified damage.
   * @param  damage is the amount of damage to be taken
   */
  void takeDamage(int damage);


  /**
   * Checks the status of the entity based on its health.
   * @return HEATH_STATUS
   */
  String checkStatus();


  /**
   *  get current health.
   * @return int for current health
   */
  int getHealth();


}
