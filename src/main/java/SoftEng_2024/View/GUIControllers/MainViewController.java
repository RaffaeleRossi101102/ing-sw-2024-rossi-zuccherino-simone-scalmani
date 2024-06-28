package SoftEng_2024.View.GUIControllers;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.CellState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.LocalModel;
import SoftEng_2024.View.ViewMessages.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Main GUI controller. Once the GUI has been launched the management of the interface is handed to this class
 * which manages scene switching, graphic elements such as buttons, images, labels and everything that is game
 * related and could be dynamically updated (eg. score)
 */
public class MainViewController {
    private static Cell[][] cardBoard;
    private static ArrayList<Cell> cardList;
    private static int[] anglesCounter;
    private static int score;
    private static ClientInterface client;
    private static double ID;
    private static boolean flipped, flipped1, flipped2, flipped3, auxFlip, visibleChat = false;
    private static int columnIndex, rowIndex, columnIndex1, rowIndex1, col, row;
    private static LocalModel localModel = new LocalModel();
    private Stage stage;
    private Scene scene;
    private static Thread clientQueueExecutor;
    private GameState nextState, prevState;
    private int deck, publicCard;
    private static final String IMAGE_PATH_FRONT = "/Cards/CODEX_cards_gold_front-%d.jpg";
    private static final String IMAGE_PATH_BACK = "/Cards/CODEX_cards_gold_back-%d.jpg";
    private double scale = 1.0;
    int playerCount = 0, choice = 0, handIndex, cardID, deckTopID;
    private static int chosenBoard, prevChosen;
    private static String nickname, destNickname;
    private ArrayList<String> destNicknames;
    private static ArrayList<String> boardNicknames = new ArrayList<>();
    private static Color color;
    private static ArrayDeque<String> chatMsg;
    private Thread t1;
    @FXML
    private TextField textField, intField, messageField;
    @FXML
    private static TextArea chatArea;
    @FXML
    ChoiceBox<String> selectNick;
    @FXML
    Label nickLabel, pcLabel, playerScore, playerTurn, winnerLabel;
    @FXML
    ImageView backImage, frontImage, goalCardHand, goal1, goal2, hand1, hand2, hand3, deck1, deck2, public1, public2, public3, public4, publicGoal1, publicGoal2, prevClickedCell, pin, pin2;
    @FXML
    Button redBTN, ylwBTN, bluBTN, grnBTN, prvBTN, chatBTN, playBTN, flipBTN, showBoard1, showBoard2, showBoard3, scoreBTN;
    @FXML
    GridPane cardPane, otherPlayersPane;
    @FXML
    AnchorPane chatPane, plateauPane;
    @FXML
    ScrollPane otherPlayersBoard;
    @FXML
    HBox resourceBox;

    /**
     * Sets the ID of the client.
     *
     * @param id The ID to set.
     */
    public static void setID(double id) {
        MainViewController.ID = id;
    }

    /**
     * Sets the client interface for networking purposes.
     *
     * @param clientVal The client to set.
     */
    public static void setClient(ClientInterface clientVal) {
        MainViewController.client = clientVal;
    }

    /**
     * Sets the selected card to flipped or not.
     *
     * @param flippedExt The flipped to set.
     */
    public static void setFlipped(boolean flippedExt) {
        MainViewController.flipped = flippedExt;
    }

    /**
     * Sets the nickname.
     *
     * @param nickname The nickname to set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the playerCount. Used only during game creation.
     *
     * @param playerCount The playerCount to set.
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * Configures the text field to update the nickname when the ENTER key is pressed.
     * If the entered text is max 20 characters, it sets the nickname, updates
     * the nickname label, clears the text field, and shifts focus to the nickname label.
     */
    public void setNicknameArea() {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && textField.getText().length() < 21) {
                setNickname(textField.getText());
                nickLabel.setText(nickname);
                textField.clear();
                nickLabel.requestFocus();
            }
        });
    }

    /**
     * Configures the integer input field to update the player count when the ENTER key is pressed.
     * If the entered value is 2, 3, or 4, it sets the player count, updates the player count label,
     * clears the input field, and shifts focus to the player count label.
     */
    public void setPlayerCountArea() {
        intField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String temp = intField.getText();
                int playerCount = Integer.parseInt(temp);
                if (playerCount == 2 || playerCount == 3 || playerCount == 4) {
                    setPlayerCount(playerCount);
                    pcLabel.setText(String.valueOf(playerCount));
                    intField.clear();
                    pcLabel.requestFocus();
                }
            }
        });
    }

    /**
     * Sets the local model and starts the client queue executor thread.
     * This method initializes the local model, starts a new thread to run the client,
     * and sets the player and game states to the connection state. Without the run() method
     * ACKs won't be received by any of the players starting the GUI, so it's fundamental that
     * this method gets invoked as soon as the GUI gets launched.
     *
     * @param localModel The local model to set.
     */
    public static void setLocalModel(LocalModel localModel) {
        MainViewController.localModel = localModel;
        clientQueueExecutor = new Thread(() ->
        {
            try {
                MainViewController.client.run();
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        clientQueueExecutor.start();
        localModel.setPlayerState(GameState.CONNECTION);
        localModel.setGameState(GameState.CONNECTION);
    }

    /**
     * Transitions the application to the next game state asynchronously.
     * This method initiates a loop that continuously checks if the current state
     * matches the state in the local model. Once the state changes, it switches
     * to the corresponding game view on the JavaFX application thread.
     *
     * @param state The current game state.
     * @throws IOException If an I/O error occurs during the state transition.
     */
    public void nextState(GameState state) throws IOException {
        nextState = state.nextState();
        new Thread(() -> {
            while (state == localModel.getState()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Platform.runLater(() -> {
                try {
                    switch (nextState) {
                        case STARTER:
                            switchToStarterPlay();
                            break;
                        case SETCOLOR:
                            switchToColorChoice();
                            break;
                        case CHOOSEGOAL:
                            switchToGoalChoice();
                            break;
                        case PLAY:
                            switchToMainGame();
                            break;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }).start();
    }

    /**
     * Updates the image of the deck based on the top resource card.
     * Depending on the specified resource type, this method sets the appropriate
     * deckTopID and updates the image of resource deck accordingly.
     *
     * @param resource The resource type to set at the top of the deck.
     */
    private void deck1TopResource(Angles resource) {
        switch (resource) {
            case FUNGI:
                deckTopID = 10;
                break;
            case PLANTS:
                deckTopID = 20;
                break;
            case ANIMALS:
                deckTopID = 30;
                break;
            case INSECTS:
                deckTopID = 40;
                break;
            case EMPTY:
                deckTopID = 0;
                break;
        }
        if (deckTopID != 0)
            deck1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, deckTopID)))));
        else deck1.setImage(null);
    }

    /**
     * Updates the image of the deck based on the top resource card.
     * Depending on the specified resource type, this method sets the appropriate
     * deckTopID and updates the image of gold deck accordingly.
     *
     * @param resource The resource type to set at the top of the deck.
     */
    private void deck2TopResource(Angles resource) {
        switch (resource) {
            case FUNGI:
                deckTopID = 50;
                break;
            case PLANTS:
                deckTopID = 60;
                break;
            case ANIMALS:
                deckTopID = 70;
                break;
            case INSECTS:
                deckTopID = 80;
                break;
            case EMPTY:
                deckTopID = 0;
                break;
        }
        if (deckTopID != 0)
            deck2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, deckTopID)))));
        else deck1.setImage(null);
    }

    /**
     * Handles zooming functionality in response to a scroll event.
     * Adjusts the scale of the `cardPane` based on the direction of the scroll:
     * - Increases the scale when scrolling up.
     * - Decreases the scale when scrolling down.
     * <p>
     * The cardPane corresponds to the main player's board, where he would play his cards.
     *
     * @param event The scroll event triggering the zoom action.
     */
    private void handleZoom(ScrollEvent event) {
        double zoomFactor = 1.1;
        if (event.getDeltaY() > 0) {
            scale *= zoomFactor;
        } else {
            scale /= zoomFactor;
        }
        cardPane.setScaleX(scale);
        cardPane.setScaleY(scale);
    }

    /**
     * Handles zooming functionality for the `otherPlayersPane` in response to a scroll event.
     * Adjusts the scale of the pane based on the direction of the scroll:
     * - Increases the scale when scrolling up.
     * - Decreases the scale when scrolling down.
     * <p>
     * The otherPlayersPane corresponds to the other players's pane, and since it occupies a small
     * portion of the game GUI, needs scrolling and panning functionality in order to see the whole board
     * of the other players.
     *
     * @param event The scroll event triggering the zoom action.
     */
    private void handleZoom2(ScrollEvent event) {
        double zoomFactor = 1.1;
        if (event.getDeltaY() > 0) {
            scale *= zoomFactor;
        } else {
            scale /= zoomFactor;
        }
        otherPlayersPane.setScaleX(scale);
        otherPlayersPane.setScaleY(scale);
    }

    /**
     * Switches the application view to the waiting screen.
     * Loads the WaitingScreen.fxml file as a new scene and initializes necessary components such as the chat.
     * Disables and hides the chat button if the current game state is CONNECTION.
     *
     * @param event The action event that triggered the method call.
     * @throws IOException If an error occurs while loading the WaitingScreen.fxml file.
     */
    public void switchToWaitingScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/WaitingScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        chatInit(visibleChat);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
        if (chatMsg == null) chatMsg = new ArrayDeque<>();
        if (localModel.getState() == GameState.CONNECTION) {
            chatBTN = (Button) scene.getRoot().lookup("#chatBTN");
            chatBTN.setDisable(true);
            chatBTN.setVisible(false);
        }
    }

    /**
     * Switches the application view to the create setup screen.
     * Loads the CreateScreen.fxml file as a new scene and sets it in the current stage.
     *
     * @param event The action event that triggered the method call.
     * @throws IOException If an error occurs while loading the CreateScreen.fxml file.
     */
    public void switchToCreateSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CreateScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Switches the application view to the join setup screen.
     * Loads the JoinScreen.fxml file as a new scene and sets it in the current stage.
     *
     * @param event The action event that triggered the method call.
     * @throws IOException If an error occurs while loading the JoinScreen.fxml file.
     */
    public void switchToJoinSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/JoinScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Switches the application view to the main menu screen.
     * Loads the MainMenu.fxml file as a new scene and sets it in the current stage.
     *
     * @param event The action event that triggered the method call.
     * @throws IOException If an error occurs while loading the MainMenu.fxml file.
     */
    public void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Switches the application view to the starter play screen.
     * Loads StarterPlay.fxml as the new scene and initializes necessary components like the chat.
     * Displays the back and front images of the starter card.
     * Starts a new thread to continuously monitor chat messages. Updates the chat area accordingly.
     * Handles special conditions such as quitting the game or being the last player remaining.
     *
     * @throws IOException If an error occurs while loading StarterPlay.fxml.
     */
    public void switchToStarterPlay() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/StarterPlay.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);

        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

        backImage = (ImageView) scene.lookup("#backImage");
        frontImage = (ImageView) scene.lookup("#frontImage");
        backImage.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getStarterCard().getCardID())))));
        frontImage.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getStarterCard().getCardID())))));

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (!localModel.getChat().isEmpty()) {
                    synchronized (localModel.getChat()) {
                        Platform.runLater(() -> {
                            if (!localModel.getChat().isEmpty()) {
                                chatMsg.add(localModel.getChat().element());
                                chatArea.appendText(localModel.getChat().poll() + "\n");
                            }
                        });
                    }
                }
                if (localModel.getChatError() != null) {
                    chatMsg.add(localModel.getChatError());
                    chatArea.appendText(localModel.getChatError() + "\n");
                    localModel.setChatError(null);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (localModel.getQuitALL()) {
                    Platform.runLater(() -> {
                        aloneInGameHandler();
                        localModel.setQuitAll(false);
                    });
                }
                if (localModel.getLastManStanding()) {
                    Platform.runLater(() -> {
                        try {
                            switchToFinalScreen(true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

    }

    /**
     * Switches the application view to the color choice screen.
     * Loads the ColorChoice.fxml file as a new scene and sets it in the current stage.
     * Initializes necessary components like chat.
     * Hides the chat pane initially.
     *
     * @throws IOException If an error occurs while loading the ColorChoice.fxml file.
     */
    public void switchToColorChoice() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ColorChoice.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);
        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

        chatPane = (AnchorPane) scene.lookup("#chatPane");
        chatArea = (TextArea) scene.lookup("#chatArea");
        selectNick = (ChoiceBox<String>) scene.getRoot().lookup("#selectNick");
        chatPane.setVisible(false);
    }

    /**
     * Switches the application view to the goal choice screen.
     * Loads the GoalChoice.fxml file as a new scene and sets it in the current stage.
     * Initializes necessary components like chat.
     * Loads and displays the available goal cards for selection.
     *
     * @throws IOException If an error occurs while loading the GoalChoice.fxml file.
     */
    public void switchToGoalChoice() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GoalChoice.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);
        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

        goal1 = (ImageView) scene.lookup("#goal1");
        goal2 = (ImageView) scene.lookup("#goal2");
        goal1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getAvailableGoals().get(0).getCardID())))));
        goal2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getAvailableGoals().get(1).getCardID())))));
    }

    /**
     * Switches the application view to the main game screen.
     * This method starts a background thread (`t1`) to continuously update game elements such as decks, goals, player turns, scores, and board states.
     * Loads the MainGame.fxml file as a new scene and sets it in the current stage.
     * Initializes necessary components like chat, player boards, and other visual elements.
     * Monitors game state changes. if condition are met, switches to the player end game screen.
     *
     * @throws IOException If an error occurs while loading the MainGame.fxml file.
     */
    public void switchToMainGame() throws IOException {
        t1 = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    deck1 = (ImageView) scene.lookup("#deck1");
                    deck2 = (ImageView) scene.lookup("#deck2");
                    deck1TopResource(localModel.getTopResourceCard());
                    deck2TopResource(localModel.getTopGoldCard());

                    publicGoal1 = (ImageView) scene.lookup("#publicGoal1");
                    publicGoal2 = (ImageView) scene.lookup("#publicGoal2");
                    publicGoal1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicGoals().get(0).getCardID())))));
                    publicGoal2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicGoals().get(1).getCardID())))));

                    public1 = (ImageView) scene.lookup("#public1");
                    public2 = (ImageView) scene.lookup("#public2");
                    public3 = (ImageView) scene.lookup("#public3");
                    public4 = (ImageView) scene.lookup("#public4");
                    public1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(0).getCardID())))));
                    public2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(1).getCardID())))));
                    public3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(2).getCardID())))));
                    public4.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(3).getCardID())))));
                    playerTurn = (Label) scene.getRoot().lookup("#playerTurn");
                    if (Objects.equals(localModel.getCurrentTurnPlayerNickname(), nickname)) {
                        playerTurn.setText("Now it's your turn");
                    } else playerTurn.setText("Now it's " + localModel.getCurrentTurnPlayerNickname() + "'s turn");

                    resourceBox = (HBox) scene.lookup("#resourceBox");
                    for (int i = 0; i < 7; i++) {
                        Label label = (Label) resourceBox.getChildren().get(i);
                        label.setText(String.valueOf(localModel.getPlayersBoards().get(nickname).getAnglesCounter()[i]));
                    }

                    for (int i = 0; i < 30; i++) {
                        pin = (ImageView) scene.lookup("#pin" + i);

                        if (pin != null) {
                            pin.setImage(null);
                        }

                        int tempScore;
                        Color tempColor;
                        for (String nick : localModel.getPlayersNickname().keySet()) {
                            tempScore = localModel.getPlayersBoards().get(nick).getScore();
                            tempColor = localModel.getPlayersColor().get(nick);
                            if (tempScore == i) {
                                if (tempColor != null) {
                                    switch (tempColor) {
                                        case RED:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_red.png"))));
                                            break;
                                        case GREEN:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_green.png"))));
                                            break;
                                        case BLUE:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_blue.png"))));
                                            break;
                                        case YELLOW:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_yellow.png"))));
                                            break;
                                    }
                                }
                            }
                        }
                        tempScore = localModel.getPlayersBoards().get(nickname).getScore();
                        tempColor = localModel.getPlayersColor().get(nickname);
                        if (tempScore == i) {
                            switch (tempColor) {
                                case RED:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_red.png"))));
                                    break;
                                case GREEN:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_green.png"))));
                                    break;
                                case BLUE:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_blue.png"))));
                                    break;
                                case YELLOW:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_yellow.png"))));
                                    break;
                            }
                        }
                    }
                    playerScore = (Label) scene.getRoot().lookup("#playerScore");
                    playerScore.setText("Score: " + localModel.getPlayersBoards().get(nickname).getScore());
                });

                if ((localModel.getPlayerState() == GameState.ENDGAME && !localModel.getCurrentTurnPlayerNickname().equals(nickname)) || (localModel.getGameState().equals(GameState.ENDGAME))) {
                    Platform.runLater(() -> {
                        try {
                            switchToPlayerEndGame(false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        flipped1 = false;
        flipped2 = false;
        flipped3 = false;
        rowIndex = -1;
        columnIndex = -1;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainGame.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);

        chatInit(visibleChat);
        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }
        otherPlayersBoardsInit();

        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

        showBoard1 = (Button) scene.lookup("#showBoard1");
        showBoard2 = (Button) scene.lookup("#showBoard2");
        showBoard3 = (Button) scene.lookup("#showBoard3");
        showBoard1.setText(boardNicknames.get(0) + "'s board");
        showBoard2.setVisible(false);
        showBoard3.setVisible(false);

        if (localModel.getPlayersNickname().size() == 2) {
            showBoard2.setVisible(true);
            showBoard2.setText(boardNicknames.get(1) + "'s board");
        } else if (localModel.getPlayersNickname().size() == 3) {
            showBoard2.setText(boardNicknames.get(1) + "'s board");
            showBoard3.setText(boardNicknames.get(2) + "'s board");
            showBoard2.setVisible(true);
            showBoard3.setVisible(true);
        }

        goalCardHand = (ImageView) scene.lookup("#goalCardHand");
        goalCardHand.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, cardID)))));
        goalCardHand.setDisable(true);

        hand1 = (ImageView) scene.lookup("#hand1");
        hand2 = (ImageView) scene.lookup("#hand2");
        hand3 = (ImageView) scene.lookup("#hand3");
        hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
        hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
        hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));

        cardPane = (GridPane) scene.lookup("#cardPane");
        if (cardPane != null) {
            cardPane.setOnScroll(this::handleZoom);
        } else {
            System.err.println("cardPane is null");
        }
        //imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));

        ImageView imageView = new ImageView();
        if (auxFlip) {
            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPlayersBoards().get(nickname).getCardBoard()[42][42].getCard().getCardID())))));
            imageView.setDisable(true);
        } else {
            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPlayersBoards().get(nickname).getCardBoard()[42][42].getCard().getCardID())))));
            imageView.setDisable(true);
        }
        imageView.setFitWidth(155);
        imageView.setFitHeight(103);
        imageView.setOnMouseClicked(this::handleCellClick);
        cardPane.add(imageView, 42, 42);

        for (int i = 43; i < 44; i++) {
            for (int j = 41; j < 44; j++) {
                if (localModel.getPlayersBoards().get(nickname).getCardBoard()[j][i].getCellState().equals(CellState.PLACEABLE)) {
                    imageView = new ImageView();
                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    imageView.setOnMouseClicked(this::handleCellClick);
                    cardPane.add(imageView, i, j);
                }
            }
        }
        for (int i = 41; i < 42; i++) {
            for (int j = 41; j < 44; j++) {
                if (localModel.getPlayersBoards().get(nickname).getCardBoard()[j][i].getCellState().equals(CellState.PLACEABLE)) {
                    imageView = new ImageView();
                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    imageView.setOnMouseClicked(this::handleCellClick);
                    cardPane.add(imageView, i, j);
                }
            }
        }

        playerScore = (Label) scene.getRoot().lookup("#playerScore");
        playerScore.setText("Score: 0");
    }

    /**
     * Switches the application view to the main game screen updating it with all the elements present before the quit.
     * This method starts a background thread (`t1`) to continuously update game elements such as decks, goals, player turns, scores, and board states.
     * Monitors game state changes. if condition are met, switches to the player end game screen.
     *
     * @throws IOException If there is an error loading resources or initializing components for the main game screen.
     */
    public void switchToReMainGame() throws IOException {
        t1 = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    deck1 = (ImageView) scene.lookup("#deck1");
                    deck2 = (ImageView) scene.lookup("#deck2");
                    deck1TopResource(localModel.getTopResourceCard());
                    deck2TopResource(localModel.getTopGoldCard());

                    publicGoal1 = (ImageView) scene.lookup("#publicGoal1");
                    publicGoal2 = (ImageView) scene.lookup("#publicGoal2");
                    publicGoal1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicGoals().get(0).getCardID())))));
                    publicGoal2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicGoals().get(1).getCardID())))));

                    public1 = (ImageView) scene.lookup("#public1");
                    public2 = (ImageView) scene.lookup("#public2");
                    public3 = (ImageView) scene.lookup("#public3");
                    public4 = (ImageView) scene.lookup("#public4");
                    public1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(0).getCardID())))));
                    public2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(1).getCardID())))));
                    public3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(2).getCardID())))));
                    public4.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(3).getCardID())))));
                    playerTurn = (Label) scene.getRoot().lookup("#playerTurn");
                    if (Objects.equals(localModel.getCurrentTurnPlayerNickname(), nickname)) {
                        playerTurn.setText("Now it's your turn");
                    } else playerTurn.setText("Now it's " + localModel.getCurrentTurnPlayerNickname() + "'s turn");

                    resourceBox = (HBox) scene.lookup("#resourceBox");
                    for (int i = 0; i < 7; i++) {
                        Label label = (Label) resourceBox.getChildren().get(i);
                        label.setText(String.valueOf(localModel.getPlayersBoards().get(nickname).getAnglesCounter()[i]));
                    }

                    for (int i = 0; i < 30; i++) {
                        pin = (ImageView) scene.lookup("#pin" + i);

                        if (pin != null) {
                            pin.setImage(null);
                        }

                        int tempScore;
                        Color tempColor;
                        for (String nick : localModel.getPlayersNickname().keySet()) {
                            tempScore = localModel.getPlayersBoards().get(nick).getScore();
                            tempColor = localModel.getPlayersColor().get(nick);
                            if (tempColor != null) {
                                if (tempScore == i) {
                                    switch (tempColor) {
                                        case RED:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_red.png"))));
                                            break;
                                        case GREEN:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_green.png"))));
                                            break;
                                        case BLUE:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_blue.png"))));
                                            break;
                                        case YELLOW:
                                            pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_yellow.png"))));
                                            break;
                                    }
                                }
                            }
                        }
                        tempScore = localModel.getPlayersBoards().get(nickname).getScore();
                        tempColor = localModel.getPlayersColor().get(nickname);
                        if (tempScore == i) {
                            switch (tempColor) {
                                case RED:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_red.png"))));
                                    break;
                                case GREEN:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_green.png"))));
                                    break;
                                case BLUE:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_blue.png"))));
                                    break;
                                case YELLOW:
                                    pin.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/pin_yellow.png"))));
                                    break;
                            }
                        }
                    }
                    playerScore = (Label) scene.getRoot().lookup("#playerScore");
                    playerScore.setText("Score: " + localModel.getPlayersBoards().get(nickname).getScore());
                });

                if ((localModel.getPlayerState() == GameState.ENDGAME && !localModel.getCurrentTurnPlayerNickname().equals(nickname)) || (localModel.getGameState().equals(GameState.ENDGAME))) {
                    Platform.runLater(() -> {
                        try {
                            switchToPlayerEndGame(false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        flipped1 = false;
        flipped2 = false;
        flipped3 = false;
        rowIndex = -1;
        columnIndex = -1;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainGame.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);

        chatInit(visibleChat);
        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }
        otherPlayersBoardsInit();

        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();

        showBoard1 = (Button) scene.lookup("#showBoard1");
        showBoard2 = (Button) scene.lookup("#showBoard2");
        showBoard3 = (Button) scene.lookup("#showBoard3");
        showBoard1.setText(boardNicknames.get(0) + "'s board");
        showBoard2.setVisible(false);
        showBoard3.setVisible(false);

        if (localModel.getPlayersNickname().size() == 2) {
            showBoard2.setVisible(true);
            showBoard2.setText(boardNicknames.get(1) + "'s board");
        } else if (localModel.getPlayersNickname().size() == 3) {
            showBoard2.setText(boardNicknames.get(1) + "'s board");
            showBoard3.setText(boardNicknames.get(2) + "'s board");
            showBoard2.setVisible(true);
            showBoard3.setVisible(true);
        }

        goalCardHand = (ImageView) scene.lookup("#goalCardHand");
        goalCardHand.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getAvailableGoals().get(0).getCardID())))));
        goalCardHand.setDisable(true);

        hand1 = (ImageView) scene.lookup("#hand1");
        hand2 = (ImageView) scene.lookup("#hand2");
        hand3 = (ImageView) scene.lookup("#hand3");
        hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
        hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
        hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));

        cardPane = (GridPane) scene.lookup("#cardPane");
        if (cardPane != null) {
            cardPane.setOnScroll(this::handleZoom);
        } else {
            System.err.println("cardPane is null");
        }

        for (int col = 0; col < 85; col++) {
            for (int row = 0; row < 85; row++) {
                if ((row + col) % 2 == 0) {
                    ImageView imageView = new ImageView();
                    if (localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard() != null) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getFlipped()) {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getCardID())))));
                            imageView.setDisable(true);
                        } else {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getCardID())))));
                            imageView.setDisable(true);
                        }
                    } else if (localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCellState().equals(CellState.PLACEABLE)) {
                        imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                        imageView.setOnMouseClicked(this::handleCellClick);
                    }
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    cardPane.add(imageView, col, row);
                }

            }
        }


        playerScore = (Label) scene.getRoot().lookup("#playerScore");
        playerScore.setText("Score: 0");
    }

    /**
     * Changes the application view to the player's end game screen.
     * If `bol` is false, interrupts the `t1` thread.
     * Loads the PlayerEndGame.fxml file as a new scene and initializes necessary components such.
     * Starts a new thread to continuously monitor the game state. When the game state is ENDGAME, transitions to the final screen.
     *
     * @param bol Flag indicating whether to interrupt the `t1` thread.
     * @throws IOException If an error occurs while loading the PlayerEndGame.fxml file.
     */
    public void switchToPlayerEndGame(boolean bol) throws IOException {
        if (!bol) {
            t1.interrupt();
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/PlayerEndGame.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);
        for (String nick : localModel.getPlayersNickname().keySet()) {
            boardNicknames.add(nick);
            System.out.println(nick);
        }

        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });

        new Thread(() -> {
            while (true) {
                if (localModel.getGameState() == GameState.ENDGAME) {
                    Platform.runLater(() -> {
                        try {
                            switchToFinalScreen(false);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Switches the view to the final screen.
     * Loads the FinalScreen.fxml file as a new scene and initializes necessary components.
     * Displays the winners' information based on the game outcome.
     * If there are two winners, displays their names and scores.
     * If there is one winner, displays their name and score.
     * If `quit` is true, displays a message indicating the player won because other players quit.
     *
     * @param quit Flag indicating if the player won because other players quit.
     * @throws IOException If an error occurs while loading the FinalScreen.fxml file.
     */
    public void switchToFinalScreen(boolean quit) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FinalScreen.fxml")));
        scene = new Scene(root);

        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });


        if (localModel.getWinnersNickname().size() == 2) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel");
            winnerLabel.setText("WE HAVE MORE THAN ONE WINNER \n" + "CONGRATULATIONS TO:\n" + localModel.getWinnersNickname().get(0) + " and " + localModel.getWinnersNickname().get(1) + " \n YOU WON!! \n" + localModel.getPlayersBoards().get(localModel.getWinnersNickname().get(0)).getScore() + "POINTS");
        } else if (localModel.getWinnersNickname().size() == 1) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel");
            winnerLabel.setText("CONGRATULATIONS! \n" + localModel.getWinnersNickname().get(0) + "\nYOU WON!! \n" + localModel.getPlayersBoards().get(localModel.getWinnersNickname().get(0)).getScore() + "\n POINTS");
        } else if (quit) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel");
            winnerLabel.setText("YOU WON!!! \n" + "the others players quit");
        }

        Timer terminationTimer = new Timer();
        TimerTask terminationTask = new TimerTask() {
            @Override
            public void run() {
                System.exit(0); // Exit the application
            }
        };
        terminationTimer.schedule(terminationTask, 10000);
    }

    /**
     * Handles sending a message based on user input from the message field.
     * Sends either a broadcast message or a whisper message, depending on the selected destination nickname.
     * Displays an error alert if no destination nickname is selected before sending the message.
     * Pressing the ENTER key while the message field is focused triggers the message sending process.
     */
    @FXML
    private void sendMessage() {
        String message = messageField.getText();
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!message.isEmpty() && destNickname != null) {
                    messageField.clear();
                    if (destNickname == "broadcast") {
                        ViewMessage broadcastMsg = new BroadcastMessage(message, ID);
                        try {
                            client.update(broadcastMsg);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        ViewMessage whisperMsg = new WhisperMessage(message, destNickname, ID);
                        try {
                            client.update(whisperMsg);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("DESTINATION ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("NO DESTINATION SELECTED... SELECT ONE BEFORE SENDING A MESSAGE...");
                    alert.show();
                }
            }
        });
    }

    /**
     * Toggles the visibility of the chat pane.
     * If the chat pane is currently visible, hides it; otherwise, makes it visible.
     * Updates the visibility state of the chat pane and stores the previous visibility state.
     */
    @FXML
    private void handleShowChat() {
        boolean isVisible = chatPane.isVisible();
        chatPane.setVisible(!isVisible);
        chatPane.setManaged(!isVisible);
        visibleChat = isVisible;
    }

    /**
     * Retrieves the selected nickname from a dropdown menu and assigns it to the destination nickname variable.
     * Used for chatting purposes.
     *
     * @param event The action event triggered when the selection is made.
     */
    public void getDestNickname(ActionEvent event) {
        destNickname = selectNick.getValue();
    }

    /**
     * Confirms the creation of a new game with the specified nickname and player count.
     * Registers the client to the server, sends a message to create the game, and waits for acknowledgment.
     * If successful, switches to the waiting screen and transitions to the connection state.
     * If unsuccessful due to an existing game or invalid parameters, switches to the main menu and displays an error message.
     *
     * @param event The action event triggering the method call.
     * @throws RemoteException   If a remote communication error occurs.
     * @throws NotBoundException If the client is not bound to the server.
     * @throws IOException       If an I/O error occurs during the process.
     */
    public void confirmCreateGame(ActionEvent event) {
        if (nickname != null && playerCount != 0) {
            try {
                assert client != null;
                client.registerToServer(ID, client);
                ViewMessage msg = new CreateGameMessage(nickname, playerCount, ID);
                client.update(msg);
                while (!localModel.isAckReceived()) ;
                if (localModel.isAckSuccessful()) {
                    switchToWaitingScreen(event);
                    inWaitingScreen(GameState.CONNECTION, false);
                } else {
                    localModel.setAckReceived(false);
                    localModel.setAckSuccessful(false);
                    switchToMainMenu(event);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("CREATING GAME ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("YOU ARE TRYING TO CREATE A GAME BUT ONE ALREADY EXISTS... IT'S TIME TO JOIN!!!");
                    alert.show();
                }
            } catch (RemoteException | NotBoundException e) {
                serverWarningHandler();
                System.exit(1);
            } catch (IOException e) {
                IOWarningHandler();
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("CREATE SOME NULL!!!");
            alert.show();
        }
    }

    /**
     * Confirms joining an existing game with the specified nickname.
     * Registers the client to the server, sends a message to join the game, and waits for acknowledgment.
     * If successful, switches to the waiting screen and transitions to the connection state.
     * If unsuccessful due to an existing nickname or attempting to join a non-existent game, returns to the main menu and displays an error message.
     *
     * @param event The action event triggering the method call.
     * @throws RemoteException   If a remote communication error occurs.
     * @throws NotBoundException If the client is not bound to the server.
     * @throws IOException       If an I/O error occurs during the process.
     */
    public void confirmJoinGame(ActionEvent event) {
        if (nickname != null) {
            try {
                assert client != null;
                client.registerToServer(ID, client);
                ViewMessage msg = new JoinGameMessage(nickname, ID);
                client.update(msg);
                while (!localModel.isAckReceived()) ;
                if (localModel.isAckSuccessful()) {
                    switchToWaitingScreen(event);
                    inWaitingScreen(GameState.CONNECTION, false);
                } else {
                    localModel.setAckReceived(false);
                    localModel.setAckSuccessful(false);
                    switchToMainMenu(event);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("JOIN GAME ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("You are trying to join with an existing nickname, or joining a non existent game, create one before!");
                    alert.show();
                }
            } catch (RemoteException | NotBoundException e) {
                serverWarningHandler();
                System.exit(1);
            } catch (IOException e) {
                IOWarningHandler();
                System.exit(1);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("CREATE SOME NULL!!!");
            alert.show();
        }
    }

    /**
     * Confirms rejoining an existing game with the specified nickname.
     * Registers the client to the server, sends a message to rejoin the game, and waits for acknowledgment.
     * If successful, switches to the waiting screen, sends a broadcast message indicating the player's rejoin,
     * creates a thread to manage the chat message flow and monitor certain parameters in localModel for state transitions.
     * If unsuccessful due to an unauthorized rejoin attempt, switches back to the main menu and displays an error message.
     *
     * @param event The action event that triggered the method call.
     * @throws RemoteException   If a remote communication error occurs.
     * @throws IOException       If an I/O error occurs during the process.
     * @throws NotBoundException If the client is not bound to the server.
     */
    public void confirmReJoinGame(ActionEvent event) {
        if (nickname != null) {
            try {
                assert client != null;
                client.registerToServer(ID, client);
                ViewMessage msg = new ReJoinMessage(ID, nickname);
                client.update(msg);
                while (!localModel.isAckReceived()) ;
                if (localModel.isAckSuccessful()) {
                    switchToWaitingScreen(event);
                    while (localModel.getNumberOfMessages() == 0 || localModel.getNumberOfMessages() != localModel.getArrivedMessages())
                        ;
                    ViewMessage broadcastMsg = new BroadcastMessage(nickname + " ReJoined.", ID);
                    try {
                        client.update(broadcastMsg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    if (localModel.getPlayerState() != GameState.STARTER) {
                        new Thread(() -> {
                            while (!Thread.currentThread().isInterrupted()) {
                                if (!localModel.getChat().isEmpty()) {
                                    synchronized (localModel.getChat()) {
                                        Platform.runLater(() -> {
                                            if (!localModel.getChat().isEmpty()) {
                                                chatMsg.add(localModel.getChat().element());
                                                chatArea.appendText(localModel.getChat().poll() + "\n");
                                            }
                                        });
                                    }
                                }
                                if (localModel.getChatError() != null) {
                                    chatMsg.add(localModel.getChatError());
                                    chatArea.appendText(localModel.getChatError() + "\n");
                                    localModel.setChatError(null);
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                if (localModel.getQuitALL()) {
                                    Platform.runLater(() -> {
                                        aloneInGameHandler();
                                        localModel.setQuitAll(false);
                                    });
                                }
                                if (localModel.getLastManStanding()) {
                                    Platform.runLater(() -> {
                                        try {
                                            switchToFinalScreen(true);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }).start();
                    }
                    inWaitingScreen(localModel.getPlayerState(), true);
                } else {
                    localModel.setAckReceived(false);
                    localModel.setAckSuccessful(false);
                    switchToMainMenu(event);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("REJOIN GAME ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("YOU ARE TRYING TO REJOIN A GAME BUT IT IS NOT POSSIBLE... TRY TO JOIN BEFORE!!!");
                    alert.show();
                }
            } catch (RemoteException e) {
                serverWarningHandler();
                System.exit(1);
            } catch (IOException e) {
                IOWarningHandler();
                System.exit(1);
            } catch (NotBoundException e) {
                serverWarningHandler();
                System.exit(1);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("NICKNAME NOT SET...");
            alert.show();
        }
    }

    /**
     * Confirms playing a card from the player's hand based on the current game state and selected card index.
     * If the player state is PLAY:
     * Checks if the card is flipped or if it meets the resource requirements.
     * Displays an error if no cell is selected before playing a card.
     * Updates the game board with the played card's image and disables play and flip buttons after playing.
     * If the player state is not PLAY, displays an error indicating it's not the player's turn to play.
     *
     * @throws RuntimeException If a remote communication error occurs.
     */
    public void confirmPlayCard() {
        if (localModel.getPlayerState() == GameState.PLAY) {
            if (handIndex == 0) {
                flipped = flipped1;
                if (flipped1) flipped1 = false;
            } else if (handIndex == 1) {
                flipped = flipped2;
                if (flipped2) flipped2 = false;
            } else if (handIndex == 2) {
                flipped = flipped3;
                if (flipped3) flipped3 = false;
            }
            if (flipped || (!flipped && localModel.getPersonalHand().get(handIndex).getFront().checkRequiredResources(localModel.getPlayersBoards().get(nickname).getAnglesCounter(), localModel.getPersonalHand().get(handIndex)))) {
                if (rowIndex == -1 || columnIndex == -1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("Chose a cell before play a card...");
                    alert.show();
                } else {
                    try {
                        ImageView image = new ImageView();
                        switch (handIndex) {
                            case 0:
                                if (!flipped) {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                } else {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(0).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                }
                                break;
                            case 1:
                                if (!flipped) {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                } else {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(1).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                }
                                break;
                            case 2:
                                if (!flipped) {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                } else {
                                    image.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(2).getCardID())))));
                                    image.setFitWidth(155);
                                    image.setFitHeight(103);
                                }
                                break;
                        }
                        ViewMessage msg = new PlayCardMessage(handIndex, rowIndex, columnIndex, flipped, ID);
                        client.update(msg);
                        //System.out.println("CARD SENT");
                        switch (handIndex) {
                            case 0:
                                cardPane.add(image, columnIndex, rowIndex);
                                hand1.setImage(null);
                                break;
                            case 1:
                                cardPane.add(image, columnIndex, rowIndex);
                                hand2.setImage(null);
                                break;
                            case 2:
                                cardPane.add(image, columnIndex, rowIndex);
                                hand3.setImage(null);
                                break;
                        }
                        playBTN.setDisable(true);
                        flipBTN.setDisable(true);
                        col = columnIndex;
                        row = rowIndex;
                        //System.out.println("riga: " + rowIndex + "colonna: " + columnIndex);

                        columnIndex = -1;
                        rowIndex = -1;

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                requiredResourcesHandler();
            }
        } else {
            cardChosenHandler();
        }
    }

    /**
     * Sends a message to play the starter card.
     * If successful, switches to the waiting screen and invokes the method passing GameState.STARTER.
     *
     * @param event The action event that triggered the method call.
     * @throws RemoteException If a remote communication error occurs.
     * @throws IOException     If an I/O error occurs during the process.
     */
    public void goToColorChoice(ActionEvent event) {
        try {
            ViewMessage msg = new PlayStarterCardMessage(flipped, ID);
            client.update(msg);
            // WAITING
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.STARTER, false);
        } catch (RemoteException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    /**
     * Sends a message to set the chosen color for the player.
     * If the color has been selected correctly, switches to the waiting screen and invokes the method with the GameState.SETCOLOR.
     * If the color has not been set correctly, displays an error message.
     *
     * @param event The action event that triggered the method call.
     * @throws RemoteException If a remote communication error occurs.
     * @throws IOException     If an I/O error occurs during the process.
     */
    public void goToGoalChoice(ActionEvent event) {
        if (color != null) {
            try {
                //System.out.println(color);
                ViewMessage msg = new SetColorMessage(color, ID);
                client.update(msg);
                // WAITING
                switchToWaitingScreen(event);
                inWaitingScreen(GameState.SETCOLOR, false);
            } catch (RemoteException e) {
                serverWarningHandler();
                System.exit(1);
            } catch (IOException e) {
                IOWarningHandler();
                System.exit(1);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("COLOR NOT SET... CHOOSE A COLOR!!!");
            alert.show();
        }
    }

    /**
     * Sends a message to choose the private goal card for the player.
     * If the choice has been selected correctly, switches to the waiting screen and invokes the method with the GameState.CHOOSECOLOR.
     * If the choice has not been set correctly, displays an error message.
     *
     * @param event The action event that triggered the method call.
     * @throws RemoteException If a remote communication error occurs.
     * @throws IOException     If an I/O error occurs during the process.
     */
    public void goToPlayGame(ActionEvent event) {
        if (choice != 0) {
            try {
                ViewMessage msg = new ChoosePrivateGoalMessage(choice, ID);
                client.update(msg);
                // WAITING
                switchToWaitingScreen(event);
                inWaitingScreen(GameState.CHOOSEGOAL, false);
            } catch (RemoteException e) {
                serverWarningHandler();
                System.exit(1);
            } catch (IOException e) {
                IOWarningHandler();
                System.exit(1);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("GOAL CARD NOT SET... CHOOSE A GOAL CARD!!!");
            alert.show();
        }
    }

    /**
     * Manages transitioning from the waiting screen based on the game state and rejection flag.
     * If `rej` is true, switches the screen based on the specified `state`.
     * If `rej` is false, stores the previous game state and starts a thread to wait for confirmation.
     * Upon receiving confirmation, if unsuccessful, reverts to the previous game state.
     * If successful, calls the `nextGame(state)` method to change screen.
     *
     * @param state The current Player's game state.
     * @param rej   Flag indicating if there was a rejection of action.
     * @throws IOException If an input/output error occurs during the state transition.
     */
    public void inWaitingScreen(GameState state, boolean rej) throws IOException {
        if (rej) {
            switch (state) {
                case STARTER:
                    switchToStarterPlay();
                    break;
                case PLAY:
                    switchToReMainGame();
                    break;
                case NOTPLAYING:
                    switchToReMainGame();
                    break;
                case CHOOSEGOAL:
                    switchToGoalChoice();
                    break;
                case SETCOLOR:
                    switchToColorChoice();
                    break;
                case ENDGAME:
                    switchToPlayerEndGame(true);
                    break;
            }
        } else {
            prevState = state;
            new Thread(() -> {
                while (!localModel.isAckReceived()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("ricevuto");
                localModel.setAckReceived(false);
                //System.out.println("ricevuto e settato a false");
                if (!localModel.isAckSuccessful()) {
                    localModel.setGameState(prevState);
                } else {
                    localModel.setAckSuccessful(false);
                    //System.out.println("funzionato e settato a false");
                    Platform.runLater(() -> {
                        try {
                            nextState(prevState);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }).start();
        }
    }

    /**
     * Handles the interaction when a cell on the player's board is clicked.
     * If the cell is clicked twice, it selects the cell and updates its image to show it as chosen.
     * If another cell was previously clicked, it reverts its image back to the default state.
     * Displays an error alert if the clicked cell is not placeable.
     * Updates the column and row indices.
     *
     * @param event The mouse event triggered by clicking on a cell in the player's board.
     */
    @FXML
    private void handleCellClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ImageView imageView = (ImageView) event.getSource();
            columnIndex1 = GridPane.getColumnIndex(imageView);
            rowIndex1 = GridPane.getRowIndex(imageView);

            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card_chosen.png"))));

            if (prevClickedCell != null && !prevClickedCell.equals(imageView)) {
                prevClickedCell.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
            }

            if (!localModel.getPlayersBoards().get(nickname).getCardBoard()[rowIndex1][columnIndex1].getCellState().equals(CellState.PLACEABLE)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("PLACEABLE ERROR");
                alert.setHeaderText(null);
                alert.setContentText("CELL NOT AVAILABLE...CHOOSE ANOTHER ONE !!!!!");
                alert.show();
                imageView.setImage((new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png")))));
                columnIndex = -1;
                rowIndex = -1;
            } else {
                columnIndex = columnIndex1;
                rowIndex = rowIndex1;
            }

            prevClickedCell = imageView;


        }
    }

    /**
     * Handles the flipping of a card in the player's hand based on the current hand index.
     * Toggles between showing the front and back images of the card.
     * Updates the corresponding image view and toggles the flipped state variable.
     *
     * @throws IllegalArgumentException If the hand index is invalid (not 0, 1, or 2).
     */
    @FXML
    public void handleFlipCard(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == backImage) setFlipped(true);
        else if (clickedImage == frontImage) setFlipped(false);
        auxFlip = flipped;
        //System.out.println("FLIP DEBUG: picked flipped " + flipped);
    }

    /**
     * Handles the selection of a card from the player's hand based on the clicked image view.
     * Sets the index of the selected hand card based on the clicked image view.
     *
     * @param event The mouse event triggered by clicking an image view of a card in the hand.
     */
    @FXML
    public void handleCardChoiceHand(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == hand1)
            handIndex = 0;
        else if (clickedImage == hand2)
            handIndex = 1;
        else if (clickedImage == hand3)
            handIndex = 2;
        //System.out.println("HAND DEBUG: picked hand card no. " + handIndex);
    }

    /**
     * Handles the selection of a goal card based on the clicked imageView in the goal card screen.
     * Sets the choice and adds the ID of the selected card to the cardID variable according to the clicked goal card image.
     *
     * @param event The mouse event triggered by clicking an image view of a goal card.
     */
    @FXML
    public void handlePickGoal(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == goal1) {
            choice = 1;
            cardID = localModel.getAvailableGoals().get(0).getCardID();
        } else if (clickedImage == goal2) {
            choice = 2;
            cardID = localModel.getAvailableGoals().get(1).getCardID();
        }
        //System.out.println("GOAL DEBUG: picked goal no. " + choice);
    }

    /**
     * Handles the flipping of a card in the player's hand based on the current hand index.
     * Toggles between showing the front and back images of the card.
     * Updates the corresponding image view and toggles the flipped state variable.
     *
     * @throws IllegalArgumentException If the hand index is invalid (not 0, 1, or 2).
     */
    @FXML
    public void handleFlipHandCard() {
        switch (handIndex) {
            case 0:
                if (flipped1) {
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped1 = false;
                } else {
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped1 = true;
                }
                break;
            case 1:
                if (flipped2) {
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped2 = false;
                } else {
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped2 = true;
                }
                break;
            case 2:
                if (flipped3) {
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped3 = false;
                } else {
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped3 = true;
                }
                break;
        }
    }

    /**
     * Handles the selection of a color button by scaling down the previously selected button,
     * setting the chosen color, and disabling the button if the color has already been chosen by another player.
     *
     * @param event The mouse event triggered by clicking a color button.
     */
    @FXML
    public void handlePickColor(MouseEvent event) {
        if (prvBTN != null) {
            prvBTN.setScaleX(1.0);
            prvBTN.setScaleY(1.0);
        }
        Button clickedButton = (Button) event.getSource();
        ConcurrentHashMap<String, Color> colorMap = localModel.getPlayersColor();
        prvBTN = clickedButton;
        if (clickedButton == redBTN && colorMap.containsValue(Color.RED)) {
            redBTN.setDisable(true);
            colorChosenHandler();
        } else if (clickedButton == grnBTN && colorMap.containsValue(Color.GREEN)) {
            grnBTN.setDisable(true);
            colorChosenHandler();
        } else if (clickedButton == bluBTN && colorMap.containsValue(Color.BLUE)) {
            bluBTN.setDisable(true);
            colorChosenHandler();
        } else if (clickedButton == ylwBTN && colorMap.containsValue(Color.YELLOW)) {
            ylwBTN.setDisable(true);
            colorChosenHandler();
        }
        if (clickedButton == redBTN && !colorMap.containsValue(Color.RED)) {
            color = Color.RED;
        } else if (clickedButton == grnBTN && !colorMap.containsValue(Color.GREEN)) {
            color = Color.GREEN;
        } else if (clickedButton == bluBTN && !colorMap.containsValue(Color.BLUE)) {
            color = Color.BLUE;
        } else if (clickedButton == ylwBTN && !colorMap.containsValue(Color.YELLOW)) {
            color = Color.YELLOW;
        }
        //System.out.println("COLOR DEBUG: picked color: " + color);
    }

    /**
     * Handles the interaction when the player clicks on deck 1.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the deck,
     * sends a message to update the game state and displays the newly drawn cards and the top resource card from the deck.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on deck 1.
     */
    @FXML
    public void handleDeck1(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                deck = 0;
                ViewMessage msg = new DrawFromTheDeckMessage(deck, ID);
                try {
                    client.update(msg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                deck1TopResource(localModel.getTopResourceCard());
                playBTN.setDisable(false);
                flipBTN.setDisable(false);

                for (int i = row + 1; i < row + 2; i++) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                            boolean alreadySet = false;
                            for (Node node : cardPane.getChildren()) {
                                if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                    alreadySet = true;
                                    break;
                                }
                            }
                            if (!alreadySet) {
                                ImageView imageView = new ImageView();
                                imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                imageView.setFitWidth(155);
                                imageView.setFitHeight(103);
                                imageView.setOnMouseClicked(this::handleCellClick);
                                cardPane.add(imageView, j, i);
                            }
                        }
                    }
                }
                for (int i = row - 1; i < row; i++) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                            boolean alreadySet = false;
                            for (Node node : cardPane.getChildren()) {
                                if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                    alreadySet = true;
                                    break;
                                }
                            }
                            if (!alreadySet) {
                                ImageView imageView = new ImageView();
                                imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                imageView.setFitWidth(155);
                                imageView.setFitHeight(103);
                                imageView.setOnMouseClicked(this::handleCellClick);
                                cardPane.add(imageView, j, i);
                            }
                        }

                    }
                }
            } else {
                deckChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    /**
     * Handles the interaction when the player clicks on deck 2.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the deck,
     * sends a message to update the game state and displays the newly drawn cards and the top resource card from the deck.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on deck 2.
     */
    @FXML
    public void handleDeck2(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                deck = 1;
                ViewMessage msg = new DrawFromTheDeckMessage(deck, ID);
                try {
                    client.update(msg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                deck2TopResource(localModel.getTopGoldCard());
                playBTN.setDisable(false);
                flipBTN.setDisable(false);

                for (int i = row + 1; i < row + 2; i++) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                            boolean alreadySet = false;
                            for (Node node : cardPane.getChildren()) {
                                if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                    alreadySet = true;
                                    break;
                                }
                            }
                            if (!alreadySet) {
                                ImageView imageView = new ImageView();
                                imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                imageView.setFitWidth(155);
                                imageView.setFitHeight(103);
                                imageView.setOnMouseClicked(this::handleCellClick);
                                cardPane.add(imageView, j, i);
                            }
                        }
                    }
                }
                for (int i = row - 1; i < row; i++) {
                    for (int j = col - 1; j < col + 2; j++) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                            boolean alreadySet = false;
                            for (Node node : cardPane.getChildren()) {
                                if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                    alreadySet = true;
                                    break;
                                }
                            }
                            if (!alreadySet) {
                                ImageView imageView = new ImageView();
                                imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                imageView.setFitWidth(155);
                                imageView.setFitHeight(103);
                                imageView.setOnMouseClicked(this::handleCellClick);
                                cardPane.add(imageView, j, i);
                            }
                        }

                    }
                }
            } else {
                deckChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    /**
     * Handles the interaction when the player clicks on the first resource card among those available.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the public cards,
     * sends a message to draw from public cards, displays the newly drawn card, updates the top resource card from the resource deck,
     * and updates the public resource card if it exists.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on resources card 1.
     */
    @FXML
    public void handleCard1(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                if (localModel.getPublicCards().get(0) != null) {
                    publicCard = 0;
                    ViewMessage msg = new DrawFromPublicCardsMessage(publicCard, ID);
                    try {
                        client.update(msg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                    if (localModel.getPublicCards().get(0) == null) {
                        public1.setImage(null);
                    } else {
                        public1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(0).getCardID())))));
                    }
                    deck1TopResource(localModel.getTopResourceCard());
                    playBTN.setDisable(false);
                    flipBTN.setDisable(false);

                    for (int i = row + 1; i < row + 2; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }
                        }
                    }
                    for (int i = row - 1; i < row; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }

                        }
                    }
                } else {
                    publicCardHandler();
                }
            } else {
                cardChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    /**
     * Handles the interaction when the player clicks on the second resource card among those available.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the public cards,
     * sends a message to draw from public cards, displays the newly drawn card, updates the top resource card from the resource deck,
     * and updates the public resource card if it exists.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on resources card 2.
     */
    @FXML
    public void handleCard2(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                if (localModel.getPublicCards().get(1) != null) {
                    publicCard = 1;
                    ViewMessage msg = new DrawFromPublicCardsMessage(publicCard, ID);
                    try {
                        client.update(msg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                    if (localModel.getPublicCards().get(1) == null) {
                        public2.setImage(null);
                    } else {
                        public2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(1).getCardID())))));
                    }
                    deck1TopResource(localModel.getTopResourceCard());
                    playBTN.setDisable(false);
                    flipBTN.setDisable(false);

                    for (int i = row + 1; i < row + 2; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }
                        }
                    }
                    for (int i = row - 1; i < row; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }

                        }
                    }
                } else {
                    publicCardHandler();
                }
            } else {
                cardChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    /**
     * Handles the interaction when the player clicks on the first gold card among those available.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the public cards,
     * sends a message to draw from the public cards, displays the newly drawn card, updates the top resource card from the resource deck,
     * and updates the public gold card if it exists.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on gold card 1.
     */
    @FXML
    public void handleCard3(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                if (localModel.getPublicCards().get(2) != null) {
                    publicCard = 2;
                    ViewMessage msg = new DrawFromPublicCardsMessage(publicCard, ID);
                    try {
                        client.update(msg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                    if (localModel.getPublicCards().get(2) == null) {
                        public3.setImage(null);
                    } else {
                        public3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(2).getCardID())))));
                    }
                    deck2TopResource(localModel.getTopGoldCard());
                    playBTN.setDisable(false);
                    flipBTN.setDisable(false);

                    for (int i = row + 1; i < row + 2; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }
                        }
                    }
                    for (int i = row - 1; i < row; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }

                        }
                    }
                } else {
                    publicCardHandler();
                }
            } else {
                cardChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    /**
     * Handles the interaction when the player clicks on the second gold card among those available.
     * If the player's hand has exactly 2 cards and the game state allows drawing from the public cards,
     * sends a message to draw from the public cards, displays the newly drawn card, updates the top resource card from the resource deck,
     * and updates the public gold card if it exists.
     * Enables buttons for playing or flipping a card from the hand.
     * If conditions are not met, invokes specific methods to display appropriate alerts.
     *
     * @param event The mouse event triggered by clicking on gold card 2.
     */
    @FXML
    public void handleCard4(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY || (localModel.getPlayerState() == GameState.ENDGAME && localModel.getCurrentTurnPlayerNickname().equals(nickname))) {
                if (localModel.getPublicCards().get(3) != null) {
                    publicCard = 3;
                    ViewMessage msg = new DrawFromPublicCardsMessage(publicCard, ID);
                    try {
                        client.update(msg);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));
                    if (localModel.getPublicCards().get(3) == null) {
                        public4.setImage(null);
                    } else {
                        public4.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(3).getCardID())))));
                    }
                    deck2TopResource(localModel.getTopGoldCard());
                    playBTN.setDisable(false);
                    flipBTN.setDisable(false);

                    for (int i = row + 1; i < row + 2; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }
                        }
                    }
                    for (int i = row - 1; i < row; i++) {
                        for (int j = col - 1; j < col + 2; j++) {
                            if (localModel.getPlayersBoards().get(nickname).getCardBoard()[i][j].getCellState().equals(CellState.PLACEABLE)) {
                                boolean alreadySet = false;
                                for (Node node : cardPane.getChildren()) {
                                    if (GridPane.getRowIndex(node) == i && GridPane.getColumnIndex(node) == j && node instanceof ImageView) {
                                        alreadySet = true;
                                        break;
                                    }
                                }
                                if (!alreadySet) {
                                    ImageView imageView = new ImageView();
                                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                                    imageView.setFitWidth(155);
                                    imageView.setFitHeight(103);
                                    imageView.setOnMouseClicked(this::handleCellClick);
                                    cardPane.add(imageView, j, i);
                                }
                            }

                        }
                    }
                } else {
                    publicCardHandler();
                }
            } else {
                cardChosenHandler();
            }
        } else {
            alreadyThree();
        }

    }

    /**
     * Handles the interaction when the player clicks on the quit button to quit the game.
     * Displays a confirmation dialog asking if the player really wants to quit.
     * If the player confirms, broadcasts a message in the chat indicating that the player has left the game.
     * Exits the application when the player confirms quitting.
     */
    @FXML
    public void handleQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitting Game");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ViewMessage broadcastMsg = new BroadcastMessage(nickname + " Quitted.", ID);
                try {
                    client.update(broadcastMsg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            System.exit(0);
        });
    }

    /**
     * Initializes the chat with the specified settings to make it visible or invisible.
     * Retrieves references to the necessary GUI elements for managing the chat.
     * Populates the choice box with player nicknames except the current player's and adds "broadcast".
     * Handles the action of the choice box to select the message recipient.
     * If there are chat messages present, adds them to the chat area and hides the chat pane.
     *
     * @param visibleChat Indicates whether the chat should be initially visible or not.
     */
    private void chatInit(boolean visibleChat) {
        chatPane = (AnchorPane) scene.lookup("#chatPane");
        chatArea = (TextArea) scene.lookup("#chatArea");
        selectNick = (ChoiceBox<String>) scene.getRoot().lookup("#selectNick");
        chatPane.setVisible(visibleChat);
        destNicknames = new ArrayList<>();
        for (String nick : localModel.getPlayersNickname().keySet()) {
            if (!nick.equals(nickname)) {
                destNicknames.add(nick);
            }
        }
        destNicknames.add("broadcast");
        selectNick.getItems().addAll(destNicknames);
        selectNick.setOnAction(this::getDestNickname);
        if (chatMsg != null) {
            for (String msg : chatMsg) {
                chatArea.appendText(msg + "\n");
            }
            chatPane.setVisible(false);
        }
    }

    /**
     * Initializes the display of other players' boards in a separate thread.
     * Retrieves references to the necessary GUI elements for managing the display.
     * Sets up a continuous update loop to refresh the display of the selected player's board.
     * Clears the previous board display and updates it with the current state of cards on the board.
     * Uses separate images for flipped and unflipped cards based on their state.
     * Adjusts the size of each image view to fit the specified dimensions.
     * Updates the grid pane with the newly configured image views.
     * Prints an error message if the grid pane reference is null.
     */
    public void otherPlayersBoardsInit() {
        otherPlayersBoard = (ScrollPane) scene.lookup("#otherPlayersBoard");
        otherPlayersBoard.setVisible(false);
        otherPlayersPane = (GridPane) scene.lookup("#otherPlayersPane");
        if (otherPlayersPane != null) {
            otherPlayersPane.setOnScroll(this::handleZoom2);
        } else {
            System.err.println("otherPlayersPane is null");
        }

        new Thread(() -> {
            while (true) {
                String boardToPrint = boardNicknames.get(chosenBoard);
                Platform.runLater(() -> {
                    otherPlayersPane.getChildren().removeIf(node -> node instanceof ImageView);
                    for (int col = 26; col < 59; col++) {
                        for (int row = 26; row < 59; row++) {
                            if ((row + col) % 2 == 0) {
                                ImageView imageView = new ImageView();
                                if (localModel.getPlayersBoards().get(boardToPrint).getCardBoard()[row][col].getCard() != null) {
                                    if (localModel.getPlayersBoards().get(boardToPrint).getCardBoard()[row][col].getCard().getFlipped()) {
                                        imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPlayersBoards().get(boardToPrint).getCardBoard()[row][col].getCard().getCardID())))));
                                    } else {
                                        imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPlayersBoards().get(boardToPrint).getCardBoard()[row][col].getCard().getCardID())))));
                                    }
                                }
                                imageView.setFitWidth(155);
                                imageView.setFitHeight(103);
                                otherPlayersPane.add(imageView, col, row);
                            }
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Handles the warning related to server connection issues.
     * Shows a warning alert informing the user that the server was not found or something went terribly wrong.
     * Possible causes include: RemoteException, NotBoundException.
     */
    @FXML
    private void serverWarningHandler() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Warning");
        alert.setContentText("Server not found or something went terribly wrong!\nCaused by one of these: RemoteException, NotBoundException");
        alert.showAndWait();
    }

    /**
     * Handles the warning when an IOException occurs.
     * Displays a warning alert informing the user that something went wrong due to an IOException.
     */
    @FXML
    private void IOWarningHandler() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Warning");
        alert.setContentText("Something went terribly wrong!\nCaused by IOException");
        alert.showAndWait();
    }

    /**
     * Handles the error when attempting to choose a color that has already been selected.
     * Displays an error alert informing the user to choose a different color.
     */
    @FXML
    private void colorChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("COLOR ERROR");
        alert.setHeaderText(null);
        alert.setContentText("COLOR ALREADY CHOSEN... CHOOSE ANOTHER ONE!!");
        alert.show();
    }

    /**
     * Handles the error when attempting to draw from a deck when it's not the player's turn.
     * Displays an error alert informing the user to wait for their turn.
     */
    @FXML
    private void deckChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("DRAW ERROR");
        alert.setHeaderText(null);
        alert.setContentText("IS NOT YOUR TURN WAIT...");
        alert.show();
    }

    /**
     * Handles the error when attempting to draw a card when the player already has three cards in his hand.
     * Displays an error alert instructing the user to play a card before drawing a new one.
     */
    @FXML
    private void alreadyThree() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("YOU ALREADY HAVE THREE CARDS IN YOUR HAND... PLAY A CARD BEFORE DRAWING A NEW ONE!!!");
        alert.show();
    }

    /**
     * Handles the error when attempting to play a card when it's not the player's turn.
     * Displays an error alert informing the user to wait for their turn.
     */
    @FXML
    private void cardChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("TURN ERROR");
        alert.setHeaderText(null);
        alert.setContentText("IT'S NOT YOUR TURN, WAIT...");
        alert.show();
}

    /**
     * Handles the error when attempting to draw a card from the public cards, but the public card spot is empty.
     * Displays an error alert informing the user to try drawing from another deck or another public card spot.
     */
    @FXML
    private void publicCardHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("PUBLIC CARD SPOT");
        alert.setHeaderText(null);
        alert.setContentText("PUBLIC CARD SPOT EMPTY...TRY TO DRAW FROM A DECK OR ANOTHER PUBLIC CARD SPOT!!!");
        alert.show();
    }

    /**
     * Handles the error when more resources are needed to play a card.
     * Displays an error alert informing the user to choose another card or flip this one.
     */
    @FXML
    private void requiredResourcesHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("REQUIRED RESOURCES ERROR");
        alert.setHeaderText(null);
        alert.setContentText("MORE RESOURCES NEEDED... CHOOSE ANOTHER CARD OR FLIP THIS ONE!!!!!");
        alert.show();
    }

    /**
     * Handles the error when all other players have quit and the player is alone in the game.
     * Displays an error alert informing the player that they will be declared the winner in 30 seconds if no one rejoins.
     */
    private void aloneInGameHandler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("You are alone in the game, in 30 seconds you will be declared winner if no one rejoins!");
        alert.show();
    }

    /**
     * Controls the visibility of the other players' board pane based on the button clicked.
     * If the same board button is clicked consecutively, toggles the visibility of the pane.
     *
     * @param event The action event triggered by clicking a board button.
     */
    public void showPlayerPane(ActionEvent event) {
        Button clickedName = (Button) event.getSource();
        if (clickedName == showBoard1) {
            chosenBoard = 0;
            if (!otherPlayersBoard.isVisible()) otherPlayersBoard.setVisible(true);
            else if (prevChosen == chosenBoard) otherPlayersBoard.setVisible(false);
        } else if (clickedName == showBoard2) {
            chosenBoard = 1;
            if (!otherPlayersBoard.isVisible()) otherPlayersBoard.setVisible(true);
            else if (prevChosen == chosenBoard) otherPlayersBoard.setVisible(false);
        } else if (clickedName == showBoard3) {
            chosenBoard = 2;
            if (!otherPlayersBoard.isVisible()) otherPlayersBoard.setVisible(true);
            else if (prevChosen == chosenBoard) otherPlayersBoard.setVisible(false);
        }
        prevChosen = chosenBoard;
    }

    /**
     * Toggles the visibility of the scoreboard.
     * This method is invoked when the score board button is clicked. It checks
     * the current visibility state of the  plateauPane and sets it to the
     * opposite state. If the  plateauPane is currently visible, it will
     * be hidden; if it is hidden, it will be made visible.
     *
     * @param event the event that triggered the action, typically a button click.
     */
    @FXML
    public void  handleScoreBoard(ActionEvent event){
        boolean isVisible = plateauPane.isVisible();
        plateauPane.setVisible(!isVisible);
    }

    /**
     * Handles the action triggered when the player clicks on the quit button in the create or join screen.
     * This method displays a confirmation dialog asking if the player really wants to quit the game.
     * If the player confirms the action by clicking "OK", the application will exit.
     *
     * @param event The action event triggered by clicking the quit button.
     */
    @FXML
    public void  handleConnectionQuit(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitting Game");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) System.exit(0);
        });
    }
}