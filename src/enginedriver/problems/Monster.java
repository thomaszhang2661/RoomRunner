package enginedriver.problems;

import enginedriver.Item;
import enginedriver.Player;
import enginedriver.problems.validator.SolutionValidator;

/**
 * Class for monsters in the game.
 */
public class Monster<T> extends Problem<T> {
  private final int damage;
  private final String attack;
  private boolean canAttack;

  /**
   * Constructor for Puzzle.

   * @param name the name of the monster
   * @param description the description of the monster
   * @param active the active status of the monster
   * @param affectsTarget the affects target status of the monster
   * @param affectsPlayer the affects player status of the monster
   * @param canAttack the can attack status of the monster
   * @param solution the solution of the monster
   * @param value the value of the monster
   * @param damage the damage when attacking
   * @param effects the effects of the monster
   * @param target the target of the monster affecting
   * @param pictureName the picture name of the monster
   * @param attack the attack description of the monster
   * @param validator the solution validator of the monster
   */
  public Monster(String name,
                String description,
                Boolean active,
                Boolean affectsTarget,
                Boolean canAttack,
                Boolean affectsPlayer,
                T solution,
                int value,
                int damage,
                String effects,
                String target, String pictureName, String attack,
                 SolutionValidator<T> validator) {
    super(name, description, active, affectsTarget,
            affectsPlayer, solution, value, effects, target,
            pictureName, validator);
    this.attack = attack;
    this.damage = damage;
    this.canAttack = canAttack;
  }

  /**
   * Sets the active status of the monster.

   * @param active the active status of the monster
   */
  @Override
  public void setActive(boolean active) {
    super.setActive(active);
    if (!active) {
      canAttack = active;
    }
  }

  /**
   * Retrieves the damage.

   * @return monster's damage.
   */
  public int getDamage() {
    return damage;
  }

  /**
   * Attacks the player and affects their health.

   * @param player the player being attacked.
   */
  public void attack(Player player) {
    // Attack the player
    if (super.getActive() && super.getAffectsPlayer()) {
      player.gainOrLoseHealth(-Math.abs(damage));
    }
  }

  /**
   * Determines if the monster can attack.

   * @return boolean for attack.
   */
  public boolean getCanAttack() {
    return  canAttack;
  }

  /**
   * Setter for canAttack.

   * @param input boolean for can attack.
   */
  public void setCanAttack(boolean input) {
    canAttack = input;
  }

  /**
   * Retrieves the attack description.

   * @return String for attack.
   */
  public String getAttack() {
    return attack;
  }

  /**
   * Override solve deal with can attack.

   * @param input the input to solve the puzzle
   */
  @Override
  public int solve(T input) {
    int result = super.solve(input);
    if (result == 1) {
      canAttack = false;
    }
    return result;
  }

  /**
   * Override toString to include attack and damage.

   * @return String for monster.
   */
  @Override
  public String toString() {
    String solutionStr;
    Object solution = getSolution();
    // is an Item
    if (solution instanceof Item) {
      solutionStr = ((Item) solution).getName();
      // is a String or null
    } else {
      solutionStr = solution.toString();
    }

    return "{ "
            + "\"name\":\"" + getName() + "\","
            + "\"active\":\"" + getActive() + "\","
            + "\"affects_target\":\"" + getAffectsTarget() + "\","
            + "\"affects_player\":\"" + getAffectsPlayer() + "\","
            + "\"solution\":\"" + solutionStr + "\","
            + "\"value\":\"" + getValue() + "\","
            + "\"description\":\"" + getDescription().replace("\n", "\\n") + "\","
            + "\"effects\":\"" + getEffects() + "\","
            + "\"damage\":\"" + getDamage() + "\","
            + "\"target\":\"" + getTarget() + "\","
            + "\"can_attack\":\"" + getCanAttack() + "\","
            + "\"attack\":\"" + getAttack() + "\","
            + "\"picture\":\"" + getPictureName() + "\""
            + " }";
  }
}
