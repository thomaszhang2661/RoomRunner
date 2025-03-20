package enginedriver;

/**
 * Interface for items that can be used by players.
 */
public interface IItem extends IValuable, IWeightable {

  /**
   * Uses the item, which may have some effect.
   */
  void use();


  /**
   * Returns the remaining uses of the item.
   * @return int for remaining uses.
   */
  int getUseRemain();

  /**
   * Returns the maximum uses of the item.
   * @return int for maximum uses.
   */
  int getUseMax();


}
