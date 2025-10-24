/**
 * Represents the enemy's state and related actions in the game.
 * <p>
 * This class stores the enemy's current health and provides methods to
 * modify it, check if the enemy is dead, and reset to full health.
 * </p>
 */
public class EnemyData {

    /** The enemy's current health, ranges from 0 (dead) to 100 (full health). */
    private double health = 100;

    /**
     * Gets the current health of the enemy.
     *
     * @return The current health value.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Reduces the enemy's health by a specified amount.
     * Health cannot go below 0.
     *
     * @param amt The amount of damage to inflict.
     */
    public void takeDamage(double amt) {
        health = Math.max(0, health - amt);
    }

    /**
     * Checks whether the enemy has no health left.
     *
     * @return true if health is 0 or below, false otherwise.
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Resets the enemy's health to full (100).
     * Typically called when starting a new game or after an enemy is defeated.
     */
    public void reset() {
        health = 100;
    }
}
