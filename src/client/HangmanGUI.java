package client;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Game;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author YannL
 */
public class HangmanGUI extends Application {
    // Help message to use the application
    private static final String USAGE = "java HangmanGUI [serverIP] [port]";

    // IP adress of the server
    private String ipAddress = "localhost";
    // Port number of the server
    private int port = 80;

    // Dynamic elements
    private TextField wordPropTextField;
    private Button validatePropButton;
    private Button newGameButton;
    private Text wordToGuess;
    private Text gameStatus;
    private Text attemptsValue;
    private Text scoreValue;
    private List<Button> letterButtons;

    private ServerConnection serverConnection;
    private Game game;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Build and launch GUI
        launch(args);
    }

    /**
     * TODO
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get the parameters
        List<String> args = getParameters().getUnnamed();

        // Parse input arguments
        if (args.size() > 0) {
            ipAddress = args.get(0);
        }

        // Display help if asked
        if (ipAddress.equalsIgnoreCase("-h") || ipAddress.equalsIgnoreCase("-help")) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.size() > 1) {
            try {
                port = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                System.err.println(USAGE);
                System.exit(1);
            }
        }

        // Set parameters of our application
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(350);
        primaryStage.setTitle("Hangman Game");


        // Scene
        GridPane rootNode = new GridPane();
        rootNode.setAlignment(Pos.CENTER);
        // Set the space size between row and columns
        rootNode.setHgap(10);
        rootNode.setVgap(20);
        rootNode.setPadding(new Insets(25, 25, 25, 25));


        // Add a title
        Text title = new Text("Hangman Game");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        HBox hbTitle = new HBox(title);
        hbTitle.setAlignment(Pos.CENTER);
        rootNode.add(hbTitle, 0, 0, 2, 1);


        // Stats section
        GridPane statsPane = new GridPane();
        statsPane.setAlignment(Pos.CENTER);
        statsPane.setHgap(20);
        HBox hbScore = new HBox(statsPane);
        hbScore.setAlignment(Pos.CENTER_RIGHT);
        // Score
        Text score = new Text("Score :");
        statsPane.add(score, 0, 0);
        scoreValue = new Text();
        statsPane.add(scoreValue, 1, 0);
        // Attempts
        Text attempts = new Text("Failed attempts :");
        attempts.setFill(Color.RED);
        statsPane.add(attempts, 0, 1);
        attemptsValue = new Text();
        attemptsValue.setFill(Color.RED);
        statsPane.add(attemptsValue, 1, 1);
        rootNode.add(hbScore, 1, 1);


        // Word to guess
        wordToGuess = new Text();
        wordToGuess.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        HBox hbWord = new HBox(wordToGuess);
        hbWord.setAlignment(Pos.CENTER);
        rootNode.add(hbWord, 0, 2, 2, 1);


        // Letters
        GridPane alphabetPane = new GridPane();
        int aCode= (int)'A';
        letterButtons = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            char letter = (char) (aCode + i); // Corresponding letter
            Button b = new Button(Character.toString(letter));
            // Add Handler
            b.setOnAction(new letterButtonHandler(letter));
            b.setMinWidth(30);
            letterButtons.add(b);
            alphabetPane.add(b, i%13, (i < 13 ? 0 : 1));
        }
        rootNode.add(alphabetPane, 0, 3, 2, 1);


        // Word proposition
        GridPane wordPropPane = new GridPane();
        wordPropPane.setAlignment(Pos.CENTER);
        wordPropPane.setHgap(20);
        Text wordProp = new Text("Word proposition :");
        wordPropPane.add(wordProp, 0, 0);
        wordPropTextField = new TextField();
        wordPropPane.add(wordPropTextField, 1, 0);
        validatePropButton = new Button("Validate");
        // Add Handler
        validatePropButton.setOnAction(new wordButtonHandler());
        wordPropPane.add(validatePropButton, 2, 0);
        rootNode.add(wordPropPane, 0, 4, 2, 1);

        // Status Game
        gameStatus= new Text();
        gameStatus.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        rootNode.add(gameStatus, 0, 5);

        // New Game Button
        newGameButton = new Button("New Game");
        newGameButton.setOnAction(new newGameButtonHandler());
        HBox hbBtn = new HBox(newGameButton);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        rootNode.add(hbBtn, 1, 5);

        // Scene creation
        Scene scene = new Scene(rootNode);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connection to the server
        new ConnectService(ipAddress, port).start();
        game = new Game();
        //(new CallService(Game.NEW_GAME)).start();
    }

    /**
     * TODO
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        serverConnection.deconnection();
    }

    private void updateGUI() {
        if (!wordToGuess.equals(game.getCurrentViewOfWord())) //TODO incompatible types
            wordToGuess.setText(game.getCurrentViewOfWord().toString());

        try {
            if (Integer.parseInt(attemptsValue.getText()) != game.getNbFailedAttempts())
                attemptsValue.setText(String.valueOf(game.getNbFailedAttempts()));
        } catch (NumberFormatException n) {
            attemptsValue.setText(String.valueOf(game.getNbFailedAttempts()));
        }

        try {
            if (Integer.parseInt(scoreValue.getText()) != game.getScore())
                scoreValue.setText(String.valueOf(game.getScore()));
        } catch (NumberFormatException n) {
            scoreValue.setText(String.valueOf(game.getScore()));
        }
    }

    /**
     * TODO
     */
    private class ConnectService extends Service<ServerConnection> {
        private String host;
        private int port;
        private ConnectService(String host, int port) {
            this.host = host;
            this.port = port;
            setOnSucceeded((WorkerStateEvent event) -> {
                serverConnection = getValue();
                // If connection to the server has failed :
                // print a message to inform the client
                // quit the application
                if (!serverConnection.isConnected()) {
                    wordToGuess.setText("Unable to connect to the server");
                    //TODO See what to do : display a popup and quit application ?
                }
            });
        }

        @Override
        protected Task<ServerConnection> createTask() {
            return new Task<ServerConnection>() {
                @Override
                protected ServerConnection call() {
                    return new ServerConnection(host, port);
                }
            };
        }
    }

    private class CallService extends Service<String> {
        String toServer;
        private CallService(String toServer) {
            this.toServer = toServer;
            if (toServer.equals(Game.NEW_GAME)) {
                gameStatus.setVisible(false);
                wordPropTextField.setDisable(false);
                validatePropButton.setDisable(false);
                for (Button b : letterButtons)
                    b.setDisable(false);
                wordPropTextField.clear();
                game.newGame();
            }

            setOnSucceeded((WorkerStateEvent event) -> {
                String[] fromServer = getValue().split("\\+");

                System.out.println("From Server : " + getValue()); // TODO : to remove

                if (fromServer[0].equals(Game.GAME_OVER)) {
                    // Client loses
                    wordPropTextField.setDisable(true);
                    validatePropButton.setDisable(true);
                    for (Button b : letterButtons)
                        b.setDisable(true);

                    game.setScore(Integer.parseInt(fromServer[1]));
                    game.setStatusToFinished();
                    gameStatus.setText("Game Over !");
                    gameStatus.setFill(Color.RED);
                    gameStatus.setVisible(true);
                } else if (fromServer[0].equals(Game.GAME_WIN)) {
                    // Client wins
                    wordPropTextField.setDisable(true);
                    validatePropButton.setDisable(true);
                    for (Button b : letterButtons)
                        b.setDisable(true);

                    game.modifyCurrentViewOfWord(fromServer[1]);
                    game.setStatusToFinished();
                    game.setScore(Integer.parseInt(fromServer[2]));
                    gameStatus.setText("Congratulation, you win !");
                    gameStatus.setFill(Color.GREEN);
                    gameStatus.setVisible(true);
                } else {
                    game.modifyCurrentViewOfWord(fromServer[0]);
                    game.setNbFailedAttempts(Integer.parseInt(fromServer[1]));
                }
                updateGUI();
            });
        }

        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return serverConnection.callServer(toServer);
                }
            };
        }
    }


    /**
     * Class corresponding to the eventHandler applied when
     * a letter is proposed by the client
     */
    private class letterButtonHandler implements EventHandler<ActionEvent> {
        private final char letter;

        public letterButtonHandler(char l) {
            letter = l;
        }

        @Override
        public void handle(ActionEvent event) {
            new CallService(Character.toString(letter)).start();
        }
    }


    /**
     * Class corresponding to the eventHandler applied when
     * a word is proposed by the client
     */
    private class wordButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            new CallService(wordPropTextField.getText().toUpperCase()).start();
        }
    }


    /**
     * Class corresponding to the eventHandler applied when
     * the client wants start a new game
     */
    private class newGameButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            new CallService(Game.NEW_GAME).start();
        }
    }
}
