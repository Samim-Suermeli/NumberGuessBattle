// Main.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Main class of the Number Battle game.
 * <p>
 * This class sets up the JavaFX application, including:
 * <ul>
 *     <li>A start screen with a background image and three buttons (Start, Help, Exit).</li>
 *     <li>The main game scene with player/enemy sprites, health bars, input controls, and a log area.</li>
 *     <li>Integration with GameController for handling game logic and interactions.</li>
 * </ul>
 */
public class Main extends Application {

    private Scene gameScene;  // Scene for the main game

    @Override
    public void start(Stage stage) {

        // Layout
        double sceneWidth = 500;
        double sceneHeight = 500;
        double statusHeight = 120;     // Height of the status area at the bottom
        double playAreaHeight = sceneHeight - statusHeight; // Height of the play area
        double spriteSize = 150;       // Size of player/enemy sprites
        double margin = 10;            // Margin for positioning sprites and HP bars

        // Start screen setup
        Image startBgImage = new Image("file:images/startscreen.png");
        ImageView startBgView = new ImageView(startBgImage);
        startBgView.setPreserveRatio(false); // Stretch to fit screen

        // Start screen buttons
        Button startBtn = new Button("Start");
        Button helpBtn  = new Button("Help");
        Button exitBtn  = new Button("Exit");

        // Layout for buttons stacked vertically
        VBox startButtons = new VBox(10, startBtn, helpBtn, exitBtn);
        startButtons.setAlignment(Pos.CENTER);

        // StackPane allows background image behind buttons
        StackPane startPane = new StackPane(startBgView, startButtons);
        startBgView.fitWidthProperty().bind(startPane.widthProperty());
        startBgView.fitHeightProperty().bind(startPane.heightProperty());

        // Scene for the start screen
        Scene startScene = new Scene(startPane, sceneWidth, sceneHeight);

        // Game Scene Setup
        // ImageViews for sprites
        ImageView pView = new ImageView();
        ImageView eView = new ImageView();
        // Health bars
        ProgressBar pBar = new ProgressBar(1);
        ProgressBar eBar = new ProgressBar(1);

        // Player and enemy sprite images
        Image pIdle = new Image("file:images/player0.png");
        Image pLow  = new Image("file:images/player1.png");
        Image pAtk  = new Image("file:images/player2.png");
        Image pDead = new Image("file:images/player3.png");
        Image eSprite = new Image("file:images/enemy.png");
        Image bgImage = new Image("file:images/background.png");

        // Game Models
        PlayerData player = new PlayerData();
        EnemyData enemy = new EnemyData();
        GameSessionData session = new GameSessionData();

        // Game Views
        PlayerView playerView = new PlayerView(pView, pBar, pIdle, pLow, pAtk, pDead);
        EnemyView  enemyView  = new EnemyView(eView, eBar, eSprite);

        // Overlay pane for floating popups
        Pane overlay = new Pane();
        overlay.setPickOnBounds(false);
        playerView.setOverlay(overlay);

        // Status Box
        Label rangeLabel = new Label("Current range: 1-2"); // Shows number range
        Label scoreLabel = new Label("Score: 0 | High: 0"); // Shows score
        TextArea logArea = new TextArea();                  // Scrollable log area
        logArea.setEditable(false);
        logArea.setPrefRowCount(4);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-size: 12px;");

        // Input field and buttons for guesses
        TextField input = new TextField();
        input.setPromptText("1-2");
        Button guessBtn = new Button("Guess");
        Button resetBtn = new Button("Reset");

        // HBox for input + guess button
        HBox controls = new HBox(6, input, guessBtn);
        controls.setAlignment(Pos.CENTER);

        // VBox status box containing labels, log, and controls
        VBox statusBox = new VBox(4, rangeLabel, scoreLabel, logArea, controls);
        statusBox.setPrefHeight(statusHeight);
        statusBox.setStyle("-fx-background-color: #ddd; -fx-padding:8;");

        // Play Area
        Pane playArea = new Pane();
        playArea.setPrefSize(sceneWidth, playAreaHeight);

        // Background image
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(sceneWidth);
        bgView.setFitHeight(playAreaHeight);
        bgView.setPreserveRatio(false);
        playArea.getChildren().add(bgView);

        // Position enemy in top-right
        eView.setFitWidth(spriteSize);
        eView.setPreserveRatio(true);
        eView.setLayoutX(sceneWidth - spriteSize - margin);
        eView.setLayoutY(margin);
        eBar.setPrefWidth(100);
        eBar.setLayoutX(eView.getLayoutX() - eBar.getPrefWidth() - 6);
        eBar.setLayoutY(eView.getLayoutY() + (spriteSize - 20)/2.0);
        playArea.getChildren().addAll(eBar, eView);

        // Position player in bottom-left
        pView.setFitWidth(spriteSize);
        pView.setPreserveRatio(true);
        pView.setLayoutX(margin);
        pView.setLayoutY(playAreaHeight - spriteSize);
        pBar.setPrefWidth(100);
        pBar.setLayoutX(pView.getLayoutX() + spriteSize + 6);
        pBar.setLayoutY(pView.getLayoutY() + (spriteSize - 20)/2.0);
        playArea.getChildren().addAll(pView, pBar, overlay);

        // StackPane root allows overlaying reset button
        VBox rootVBox = new VBox(playArea, statusBox);
        StackPane container = new StackPane(rootVBox);
        StackPane.setAlignment(resetBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(resetBtn, new Insets(8));
        container.getChildren().add(resetBtn);

        gameScene = new Scene(container, sceneWidth, sceneHeight);

        // Game Controller
        // Handles game logic and interactions
        GameController controller = new GameController(
                player, enemy, playerView, enemyView, session,
                rangeLabel, scoreLabel, logArea, input, guessBtn, resetBtn
        );

        // Start Screen Button Actions
        startBtn.setOnAction(e -> stage.setScene(gameScene));
        helpBtn.setOnAction(e -> {
            String englishText = "Guess the number and defeat your enemies! The initial range is 1-2 and increases by 1 with each new robot. " +
                    "Robots take two correct guesses to defeat. Guessing wrong reduces your life bar (HP), but defeating an enemy restores it by a bit. " +
                    "If your HP reaches zero, it's Game Over!";
            String germanText  = "Errate die Zahl und besiege Gegner! Der Bereich liegt erst bei 1-2 und erhöht sich für neue Roboter um 1. " +
                    "Diese benötigen zwei korrekte Inputs um besiegt zu werden. Fehler kosten Leben (HP), Gegner besiegen stellt diese jedoch wieder her. " +
                    "Falls deine HP null erreichen, ist das Spiel vorbei!";
            String[] currentText = {englishText};

            // Popup window
            Stage popup = new Stage();
            popup.setTitle("How to Play");

            Label contentLabel = new Label(currentText[0]);
            contentLabel.setWrapText(true);
            contentLabel.setStyle("-fx-font-size: 14px;");

            Button closeBtn = new Button("Close");
            closeBtn.setOnAction(ev -> popup.close());

            Button langBtn = new Button("ENG/GER");
            langBtn.setOnAction(ev -> {
                if(currentText[0].equals(englishText)) currentText[0] = germanText;
                else currentText[0] = englishText;
                contentLabel.setText(currentText[0]);
            });

            HBox buttons = new HBox(10, langBtn, closeBtn);
            buttons.setAlignment(Pos.CENTER);

            VBox layout = new VBox(10, contentLabel, buttons);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout, 300, 200);
            popup.setScene(scene);
            popup.initOwner(stage); // Makes popup modal
            popup.show();
        });
        exitBtn.setOnAction(e -> stage.close());

        // Show start screen initially
        stage.setScene(startScene);
        stage.setTitle("Number Battle");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch();
    }
}
