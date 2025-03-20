package enginedriver;

/**
 * Interface for monsters in the game.
 */
public interface IMonster extends IProblem {

  /**
   * 玩家进入房间时如果有怪物在场景中，调用本方法
   * Returns the damage the monster can inflict.
   * @return int for damage
   */
  int getDamage();
}
