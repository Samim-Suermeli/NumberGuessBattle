/**
 * Represents the player’s state and related actions in the game.
 * <p>
 * This class stores the player's current health and provides methods to
 * modify it, check if the player is dead, and reset to full health.
 * </p>
 */
public class PlayerData {

    /** The current health of the player, between 0 and 100. */
    private double health = 100;

    /**
     * Returns the current health of the player.
     *
     * @return the player's current health as a double
     */
    public double getHealth() {
        return health;
    }

    /**
     * Reduces the player's health by the specified amount.
     * Health cannot go below 0.
     *
     * @param amt the amount of damage to apply
     */
    public void takeDamage(double amt) {
        health = Math.max(0, health - amt);
    }

    /**
     * Increases the player's health by the specified amount.
     * Health cannot exceed 100.
     *
     * @param amt the amount of healing to apply
     */
    public void heal(double amt) {
        health = Math.min(100, health + amt);
    }

    /**
     * Checks whether the player is dead (health is 0 or below).
     *
     * @return true if the player's health is 0 or less, false otherwise
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Resets the player's health back to full (100).
     */
    public void reset() {
        health = 100;
    }
}
