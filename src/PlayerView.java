import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The view component for the player character in the game.
 *
 * This class handles the visual representation of the player's sprite,
 * health bar, numerical HP display, and floating damage/heal popups.
 * It does not store any game logic; it updates visuals based on the
 * PlayerData model.
 */
public class PlayerView {

    /** The ImageView displaying the player's sprite. */
    private ImageView view;

    /** The ProgressBar showing the player's health visually. */
    private ProgressBar hpBar;

    /** Player sprite images for different states. */
    private Image idle, low, attack, dead;

    /** Overlay Pane for showing floating popups like damage/heal. */
    private Pane overlay;

    /** Label displaying the numerical HP on top of the health bar. */
    private Label hpLabel;

    /**
     * Creates a PlayerView with the given images and UI components.
     *
     * @param view The ImageView used to display the player sprite.
     * @param hpBar The ProgressBar showing the player's health.
     * @param idle Image for normal health state.
     * @param low Image for low health state.
     * @param attack Image shown during attack animation.
     * @param dead Image shown when the player has zero health.
     */
    public PlayerView(ImageView view, ProgressBar hpBar,
                      Image idle, Image low, Image attack, Image dead) {
        this.view = view;
        this.hpBar = hpBar;
        this.idle = idle;
        this.low = low;
        this.attack = attack;
        this.dead = dead;

        view.setImage(idle);
        view.setFitWidth(150);
        view.setPreserveRatio(true);
        hpBar.setProgress(1);

        // Create HP number label on top of health bar
        hpLabel = new Label("100");
        hpLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
    }

    /**
     * Sets the overlay pane used for floating popups.
     * The HP number label is added to this overlay as well.
     *
     * @param overlay The Pane to be used as overlay for popups.
     */
    public void setOverlay(Pane overlay) {
        this.overlay = overlay;
        overlay.getChildren().add(hpLabel);
    }

    /**
     * Updates the player sprite, health bar, and numerical HP based
     * on the current PlayerData.
     *
     * @param player The PlayerData object representing the current state.
     */
    public void update(PlayerData player) {
        double health = player.getHealth();

        // Change sprite based on health
        if (health <= 0) view.setImage(dead);
        else if (health < 40) view.setImage(low);
        else view.setImage(idle);

        // Update progress bar
        hpBar.setProgress(health / 100.0);

        // Update HP number label position and text
        hpLabel.setText(String.valueOf((int) health));
        hpLabel.setLayoutX(hpBar.getLayoutX() + hpBar.getWidth()/2 - 10);
        hpLabel.setLayoutY(hpBar.getLayoutY() - 20);
    }

    /**
     * Sets the player's sprite to the attack image for visual feedback.
     */
    public void setAttack() {
        view.setImage(attack);
    }

    /**
     * Shows a floating popup label with text above the health bar.
     * Typically used to display damage or healing amounts.
     *
     * @param text The text to display in the popup.
     * @param color The color of the popup text.
     */
    public void showPopup(String text, String color) {
        if (overlay == null) return;

        Label popup = new Label(text);
        popup.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 16px; -fx-font-weight: bold;");

        double x = hpBar.localToScene(hpBar.getWidth()/2, 0).getX();
        double y = hpBar.localToScene(0, 0).getY() - 20;

        popup.setLayoutX(x);
        popup.setLayoutY(y);

        overlay.getChildren().add(popup);

        // Animate the popup moving upward and fading out
        javafx.animation.TranslateTransition moveUp =
                new javafx.animation.TranslateTransition(javafx.util.Duration.seconds(1), popup);
        moveUp.setByY(-30);

        javafx.animation.FadeTransition fade =
                new javafx.animation.FadeTransition(javafx.util.Duration.seconds(1), popup);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> overlay.getChildren().remove(popup));

        moveUp.play();
        fade.play();
    }
}
