import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The view component for an enemy in the game.
 *
 * This class handles the visual representation of the enemy's sprite,
 * health bar, and numerical HP display. It does not modify game logic;
 * it only reflects the current state from the EnemyData model.
 */
public class EnemyView {

    /** The ImageView used to display the enemy sprite. */
    private ImageView view;

    /** The ProgressBar showing the enemy's health visually. */
    private ProgressBar hpBar;

    /** The Image representing the enemy sprite. */
    private Image sprite;

    /** Label displaying the numerical HP on top of the health bar. */
    private Label hpLabel;

    /**
     * Creates an EnemyView with a sprite, health bar, and numerical HP label.
     *
     * @param view The ImageView used to display the enemy sprite.
     * @param hpBar The ProgressBar showing the enemy's health.
     * @param sprite The Image for the enemy's default appearance.
     */
    public EnemyView(ImageView view, ProgressBar hpBar, Image sprite) {
        this.view = view;
        this.hpBar = hpBar;
        this.sprite = sprite;

        view.setImage(sprite);
        view.setFitWidth(150);
        view.setPreserveRatio(true);
        hpBar.setProgress(1);

        // Initialize HP label
        hpLabel = new Label("100");
        hpLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
    }

    /**
     * Updates the enemy's health bar and numerical HP label based
     * on the provided EnemyData.
     *
     * @param enemy The EnemyData object representing the enemy's current state.
     */
    public void update(EnemyData enemy) {
        double health = enemy.getHealth();

        // Update health bar progress
        hpBar.setProgress(health / 100.0);

        // Update HP number label
        hpLabel.setText(String.valueOf((int) health));
        hpLabel.setLayoutX(hpBar.getLayoutX() + hpBar.getWidth() / 2 - 10);
        hpLabel.setLayoutY(hpBar.getLayoutY() - 20);

        // Ensure the label is added to the same parent as the progress bar
        if (hpBar.getParent() != null && !((Pane) hpBar.getParent()).getChildren().contains(hpLabel)) {
            ((Pane) hpBar.getParent()).getChildren().add(hpLabel);
        }
    }
}
