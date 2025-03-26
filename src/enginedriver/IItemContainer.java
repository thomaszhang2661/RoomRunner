package enginedriver;

/**
 * Interface for containers that can hold items.
 * This could be used for things like player inventory, rooms
 * for player to pick up items, should consider the weight
 */
public interface IItemContainer {

  /**
   * Adds an item to the container.
   *
   * @param item the item to be added
   * @return
   */
  boolean addItem(IItem item);

  /**
   * Removes an item from the container.
   *
   * @param item the item to be removed
   * @return
   */
  boolean deleteItem(IItem item);
}

