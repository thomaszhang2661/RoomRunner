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


  //  @Override
  //  public int getId() {
  //    return super.getId();
  //  }
  //
  //  @Override
  //  public String getName() {
  //    return super.getName();
  //  }
  //
  //  @Override
  //  public String getDescription() {
  //    return super.getDescription();
  //  }
  //
  //  @Override
  //  public Image getPicture() {
  //    return super.getPicture();
  //  }
  //
  //  @Override
  //  public boolean getActive() {
  //    return super.getActive();
  //  }
  //
  //  @Override
  //  public boolean solve(T input) {
  //    return super.solve(input);
  //  }
  //
  //  @Override
  //  public int getValue() {
  //    return super.getValue();
  //  }
  //
  //  @Override
  //  public String getEffects() {
  //    return super.getEffects();
  //  }

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
   */
  public void setCantAttack(boolean input) {
    canAttack = input;
  }

  /**
   * Retrieves the attack description.

   * @return String for attack.
   */
  public String getAttack() {
    return attack;
  }



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
