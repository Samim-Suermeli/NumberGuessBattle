/**
 * Manages the current state and progress of a game session.
 * <p>
 * This class keeps track of the enemy range, the random target number,
 * the player's current and highest scores, and provides helper methods
 * for generating new targets, validating guesses, and resetting progress.
 * </p>
 */
import java.util.Random;

public class GameSessionData {

    /** The current upper bound of the number range the enemy uses. */
    public int enemyRange = 2;

    /** The randomly generated target number the player must guess. */
    public int target;

    /** The player's current score in this session. */
    public int score = 0;

    /** The player's highest recorded score across sessions. */
    public int highScore = 0;

    /** Random number generator used for target creation. */
    private final Random rand = new Random();

    /**
     * Generates a new random target number within the current enemy range.
     * <p>
     * The target will be an integer between {@code 1}
     * and {@code enemyRange}.
     * </p>
     */
    public void generateTarget() {
        target = rand.nextInt(enemyRange) + 1;
    }

    /**
     * Checks if the player's guess matches the current target number.
     *
     * @param guess the number the player entered
     * @return {@code true} if the guess equals the target; {@code false} otherwise
     */
    public boolean checkGuess(int guess) {
        return guess == target;
    }

    /**
     * Increases the enemy's range by one, making the next round more difficult.
     * Typically called after a successful enemy defeat.
     */
    public void increaseRange() {
        enemyRange++;
    }

    /**
     * Resets the game session to its starting state.
     * <p>
     * This sets the range back to {@code 2}, clears the current score,
     * and generates a new target number.
     * </p>
     */
    public void reset() {
        enemyRange = 2;
        score = 0;
        generateTarget();
    }
}
