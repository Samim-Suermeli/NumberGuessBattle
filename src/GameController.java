// GameController.java
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Controls the main game logic and mediates between the model and view layers.
 * <p>
 * The GameController connects {@link PlayerData}, {@link EnemyData} and {@link GameSessionData}
 * (model) with {@link PlayerView} and {@link EnemyView} (view). It handles user input,
 * applies game rules (hits, misses, healing, range progression), updates the UI and
 * writes messages into the log area.
 * </p>
 */
public class GameController {
    private PlayerData player;
    private EnemyData enemy;
    private PlayerView playerView;
    private EnemyView enemyView;
    private GameSessionData session;
    private Label rangeLabel, scoreLabel;
    private TextArea logArea;
    private TextField input;
    private Button guessBtn, resetBtn;

    /**
     * Construct a GameController and wire up UI event handlers.
     *
     * @param player      player data model
     * @param enemy       enemy data model
     * @param playerView  player visual view
     * @param enemyView   enemy visual view
     * @param session     current game session (range, score, target)
     * @param rangeLabel  label that displays the current guess range
     * @param scoreLabel  label that displays score and high score
     * @param logArea     text area used for the scrollable log
     * @param input       text field where the player types guesses
     * @param guessBtn    button to submit a guess
     * @param resetBtn    button to reset the game
     */
    public GameController(PlayerData player, EnemyData enemy,
                          PlayerView playerView, EnemyView enemyView,
                          GameSessionData session,
                          Label rangeLabel, Label scoreLabel, TextArea logArea,
                          TextField input, Button guessBtn, Button resetBtn) {

        this.player = player;
        this.enemy = enemy;
        this.playerView = playerView;
        this.enemyView = enemyView;
        this.session = session;
        this.rangeLabel = rangeLabel;
        this.scoreLabel = scoreLabel;
        this.logArea = logArea;
        this.input = input;
        this.guessBtn = guessBtn;
        this.resetBtn = resetBtn;

        session.generateTarget();
        updateViews();

        // Button actions
        guessBtn.setOnAction(e -> handleGuess());
        input.setOnAction(e -> handleGuess());
        resetBtn.setOnAction(e -> resetGame());
    }

    /**
     * Process a player's guess: validate input, apply hit/miss logic, update models and views.
     * <p>
     * Correct guess: enemy takes damage, player gets score, may defeat enemy and increase range.
     * Incorrect guess: player takes damage.
     * </p>
     */
    public void handleGuess() {
        int g;
        try {
            g = Integer.parseInt(input.getText());
        } catch (Exception ex) {
            log("Enter a number between 1-" + session.enemyRange);
            input.clear();
            return;
        }

        input.setDisable(true);

        if (session.checkGuess(g)) {
            enemy.takeDamage(50);
            session.score += 10;
            if (session.score > session.highScore) session.highScore = session.score;

            playerView.setAttack();

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(ev -> {
                playerView.update(player);
                enemyView.update(enemy);
                updateScore();
                updateRange();

                if (enemy.isDead()) {
                    session.score += 50;
                    player.heal(20);
                    playerView.showPopup("+20 HP", "lime");
                    enemy.reset();
                    session.increaseRange();
                    session.generateTarget();
                    updateRange();
                    log("Enemy defeated! New range: 1-" + session.enemyRange);
                    enemyView.update(enemy);
                    playerView.update(player);
                }

                input.setDisable(false);
                input.clear();
            });
            pause.play();
            log("Hit! The enemy lost 50 HP.");

        } else {
            player.takeDamage(10);
            playerView.showPopup("-10 HP", "red");
            playerView.update(player);
            log("Miss! You lost 10 HP.");
            input.setDisable(false);
            input.clear();
        }

        if (player.isDead()) {
            log("You're out of HP! Game Over!");
            guessBtn.setDisable(true);
        }
    }

    /**
     * Reset the game state (models and views), re-enable controls and clear the log.
     */
    public void resetGame() {
        player.reset();
        enemy.reset();
        session.reset();
        session.generateTarget();
        updateViews();
        guessBtn.setDisable(false);
        input.setDisable(false);
        input.clear();
        logArea.clear();
        log("New game! Range: 1-" + session.enemyRange);
    }

    /**
     * Push the model state into the views (player/enemy HP, score and range).
     */
    private void updateViews() {
        playerView.update(player);
        enemyView.update(enemy);
        updateScore();
        updateRange();
    }

    /**
     * Update the score display label.
     */
    private void updateScore() {
        scoreLabel.setText("Score: " + session.score + " | High: " + session.highScore);
    }

    /**
     * Update the range display label.
     */
    private void updateRange() {
        rangeLabel.setText("Current range: 1-" + session.enemyRange);
    }

    /**
     * Append a line to the log area and auto-scroll to the bottom.
     *
     * @param text message to append
     */
    private void log(String text) {
        logArea.appendText(text + "\n");
        logArea.setScrollTop(Double.MAX_VALUE); // auto-scroll
    }
}
