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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


// NEEDS TO BE SEPARATED IN EVERY CORRESPONDING CLASS - OR ADD COMMENTS TO EXPLAIN EVERYTHING
public class MainViewController {
    private static Cell[][] cardBoard;
    private static ArrayList<Cell> cardList;
    private static int[] anglesCounter;
    private static int score;
    private static ClientInterface client;
    private static double ID;
    private static boolean flipped, flipped1, flipped2, flipped3, auxFlip, visibleChat = false;
    private static int columnIndex, rowIndex, columnIndex1, rowIndex1;
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
    Button redBTN, ylwBTN, bluBTN, grnBTN, prvBTN, chatBTN, playBTN, flipBTN, showBoard1, showBoard2, showBoard3;
    @FXML
    GridPane cardPane, otherPlayersPane;
    @FXML
    AnchorPane chatPane, plateauPane;
    @FXML
    ScrollPane otherPlayersBoard;
    @FXML
    HBox resourceBox;

    public static void setID(double id) {
        MainViewController.ID = id;
    }

    public static void setClient(ClientInterface clientVal) {
        MainViewController.client = clientVal;
    }

    public static void setFlipped(boolean flippedExt) {
        MainViewController.flipped = flippedExt;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

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

    public void switchToCreateSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CreateScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToJoinSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/JoinScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.show();
    }

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
                    synchronized(localModel.getChat()) {
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
                if(localModel.getQuitALL()){
                    Platform.runLater(()->{
                        aloneInGameHandler();
                        localModel.setQuitAll(false);
                    });
                }
                if(localModel.getLastManStanding()){
                    Platform.runLater(()->{
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
                    if (Objects.equals(localModel.getCurrentTurnPlayerNickname(), nickname)){
                        playerTurn.setText("Now it's your turn");
                    }
                    else playerTurn.setText("Now it's " + localModel.getCurrentTurnPlayerNickname() + "'s turn");

                    resourceBox = (HBox) scene.lookup("#resourceBox");
                    for(int i=0; i<7; i++){
                        Label label = (Label) resourceBox.getChildren().get(i);
                        label.setText(String.valueOf(localModel.getPlayersBoards().get(nickname).getAnglesCounter()[i]));
                    }

                    for(int i=0; i<30; i++) {
                        pin = (ImageView) scene.lookup("#pin" + i);

                        if(pin != null){
                            pin.setImage(null);
                        }

                        int tempScore;
                        Color tempColor;
                        for (String nick : localModel.getPlayersNickname().keySet()) {
                            tempScore = localModel.getPlayersBoards().get(nick).getScore();
                            tempColor = localModel.getPlayersColor().get(nick);
                            if (tempScore == i) {
                                if(tempColor != null) {
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

                if (localModel.getPlayerState() == GameState.ENDGAME) {
                    Platform.runLater(() -> {
                        try {
                            switchToPlayerEndGame();
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

        if (localModel.getPlayersNickname().size() == 2){
            showBoard3.setVisible(false);
            showBoard2.setText(boardNicknames.get(1) + "'s board");
        } else if (localModel.getPlayersNickname().size() == 3){
            showBoard2.setText(boardNicknames.get(1) + "'s board");
            showBoard3.setText(boardNicknames.get(2) + "'s board");
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

        for (int col = 32; col < 53; col++) {
            for (int row = 32; row < 53; row++) {
                if ((row + col) % 2 == 0) {
                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                    if (row == 42 && col == 42) {
                        if (auxFlip) {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPlayersBoards().get(nickname).getCardBoard()[42][42].getCard().getCardID())))));
                            imageView.setDisable(true);
                        } else {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPlayersBoards().get(nickname).getCardBoard()[42][42].getCard().getCardID())))));
                            imageView.setDisable(true);
                        }
                    }
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    imageView.setOnMouseClicked(this::handleCellClick);
                    cardPane.add(imageView, col, row);
                }
            }
        }
        playerScore = (Label) scene.getRoot().lookup("#playerScore");
        playerScore.setText("Score: 0");
    }

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

                if (localModel.getPlayerState() == GameState.ENDGAME) {
                    Platform.runLater(() -> {
                        try {
                            switchToPlayerEndGame();
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

        if (localModel.getPlayersNickname().size() == 2) {
            showBoard3.setVisible(false);
            showBoard2.setText(boardNicknames.get(1) + "'s board");
        } else if (localModel.getPlayersNickname().size() == 3) {
            showBoard2.setText(boardNicknames.get(1) + "'s board");
            showBoard3.setText(boardNicknames.get(2) + "'s board");
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

        for (int col = 32; col < 53; col++) {
            for (int row = 32; row < 53; row++) {
                if ((row + col) % 2 == 0) {
                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card.png"))));
                    if (localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard() != null) {
                        if (localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getFlipped()) {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getCardID())))));
                        } else {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPlayersBoards().get(nickname).getCardBoard()[row][col].getCard().getCardID())))));
                        }
                    }
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    imageView.setOnMouseClicked(this::handleCellClick);
                    cardPane.add(imageView, col, row);
                }
            }
        }
        playerScore = (Label) scene.getRoot().lookup("#playerScore");
        playerScore.setText("Score: 0");
    }

    public void switchToPlayerEndGame() throws IOException {
        t1.interrupt();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/PlayerEndGame.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);
        for(String nick : localModel.getPlayersNickname().keySet()){
            boardNicknames.add(nick);
            System.out.println(nick);
        }

        Platform.runLater(()->{
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

    public void switchToFinalScreen(boolean quit) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FinalScreen.fxml")));
        scene = new Scene(root);

        chatInit(visibleChat);
         for(String nick : localModel.getPlayersNickname().keySet()){
            boardNicknames.add(nick);
            System.out.println(nick);
        }
        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();
        });

        if (localModel.getWinnersNickname().size() == 2) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel");
            winnerLabel.setText("WE HAVE MORE THAN ONE WINNER \n" + "CONGRATULATIONS TO: " + localModel.getWinnersNickname().get(0) + localModel.getWinnersNickname().get(0) + " \n YOU WON!!");
        } else if (localModel.getWinnersNickname().size() == 1) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel");
            winnerLabel.setText("CONGRATULATIONS" + localModel.getWinnersNickname().get(0) + "YOU WON!!");
        } else if (quit) {
            winnerLabel = new Label();
            winnerLabel = (Label) scene.lookup("#winnerLabel") ;
            winnerLabel.setText("YOU WON!!! \n" + "the others players quit");
        }
    }

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

    @FXML
    private void handleShowChat() {
        boolean isVisible = chatPane.isVisible();
        chatPane.setVisible(!isVisible);
        chatPane.setManaged(!isVisible);
        visibleChat = isVisible;
    }

    public void getDestNickname(ActionEvent event) {
        destNickname = selectNick.getValue();
    }

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

    public void confirmJoinGame(ActionEvent event) {
        if(nickname != null) {
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
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("CREATE SOME NULL!!!");
            alert.show();
        }
    }

    public void confirmReJoinGame(ActionEvent event) {
        if(nickname != null) {
            try {
                assert client != null;
                client.registerToServer(ID, client);
                ViewMessage msg = new ReJoinMessage(ID, nickname);
                client.update(msg);
                while (!localModel.isAckReceived()) ;
                if (localModel.isAckSuccessful()) {
                    switchToWaitingScreen(event);
                    while (localModel.getNumberOfMessages() == 0 || localModel.getNumberOfMessages() != localModel.getArrivedMessages());
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
                                    synchronized(localModel.getChat()) {
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
                                if(localModel.getQuitALL()){
                                    Platform.runLater(()->{
                                        aloneInGameHandler();
                                        localModel.setQuitAll(false);
                                    });
                                }
                                if(localModel.getLastManStanding()){
                                    Platform.runLater(()->{
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
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("NICKNAME NOT SET...");
            alert.show();
        }
    }

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
                        System.out.println("CARD SENT");
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
                        rowIndex = -1;
                        columnIndex = -1;

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

    public void goToGoalChoice(ActionEvent event) {
        if (color != null) {
            try {
                System.out.println(color);
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
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("COLOR NOT SET... CHOOSE A COLOR!!!");
            alert.show();
        }
    }

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

    public void inWaitingScreen(GameState state, boolean rej) throws IOException {
        if(rej){
            switch (state){
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
                    switchToPlayerEndGame();
                    break;
            }
        }else {
            prevState = state;
            new Thread(() -> {
                while (!localModel.isAckReceived()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("ricevuto");
                localModel.setAckReceived(false);
                System.out.println("ricevuto e settato a false");
                if (!localModel.isAckSuccessful()) {
                    localModel.setGameState(prevState);
                } else {
                    localModel.setAckSuccessful(false);
                    System.out.println("funzionato e settato a false");
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

    @FXML
    private void handleCellClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ImageView imageView = (ImageView) event.getSource();
            columnIndex1 = GridPane.getColumnIndex(imageView);
            rowIndex1 = GridPane.getRowIndex(imageView);

            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/empty_card_chosen.png"))));

            if (prevClickedCell != null && prevClickedCell != imageView){
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
            }else{
                columnIndex = columnIndex1;
                rowIndex = rowIndex1;
            }

            prevClickedCell = imageView;


        }
    }

    @FXML
    public void handleFlipCard(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == backImage) setFlipped(true);
        else if (clickedImage == frontImage) setFlipped(false);
        auxFlip = flipped;
        System.out.println("FLIP DEBUG: picked flipped " + flipped);
    }

    @FXML
    public void handleCardChoiceHand(MouseEvent event) {
        ImageView clickedImage = (ImageView) event.getSource();
        if (clickedImage == hand1)
            handIndex = 0;
        else if (clickedImage == hand2)
            handIndex = 1;
        else if (clickedImage == hand3)
            handIndex = 2;
        System.out.println("HAND DEBUG: picked hand card no. " + handIndex);
    }

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
        System.out.println("GOAL DEBUG: picked goal no. " + choice);
    }

    @FXML
    public void handleFlipHandCard() {
        switch (handIndex) {
            case 0:
                if(flipped1){
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped1 =false;
                }else{
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped1 = true;
                }
                break;
            case 1:
                if(flipped2){
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped2 = false;
                }else{
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped2 =true;
                }
                break;
            case 2:
                if(flipped3){
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped3 = false;
                }else{
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    flipped3 = true;
                }
                break;
        }
    }

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
        System.out.println("COLOR DEBUG: picked color: " + color);
    }

    @FXML
    public void handleDeck1(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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
            } else {
                deckChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    @FXML
    public void handleDeck2(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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
            } else {
                deckChosenHandler();
            }
        } else {
            alreadyThree();
        }
    }

    @FXML
    public void handleCard1(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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

    @FXML
    public void handleCard2(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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

    @FXML
    public void handleCard3(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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

    @FXML
    public void handleCard4(MouseEvent event) {
        if (localModel.getPersonalHand().size() == 2) {
            if (localModel.getPlayerState() == GameState.PLAY) {
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
                    for (int col = 32; col < 53; col++) {
                        for (int row = 32; row < 53; row++) {
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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @FXML
    private void serverWarningHandler() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Warning");
        alert.setContentText("Server not found or something went terribly wrong!\nCaused by one of these: RemoteException, NotBoundException");
        alert.showAndWait();
    }

    @FXML
    private void IOWarningHandler() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Warning");
        alert.setContentText("Something went terribly wrong!\nCaused by IOException");
        alert.showAndWait();
    }

    @FXML
    private void colorChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("COLOR ERROR");
        alert.setHeaderText(null);
        alert.setContentText("COLOR ALREADY CHOSEN... CHOOSE ANOTHER ONE!!");
        alert.show();
    }

    @FXML
    private void deckChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("DRAW ERROR");
        alert.setHeaderText(null);
        alert.setContentText("IS NOT YOUR TURN WAIT...");
        alert.show();
    }

    @FXML
    private void alreadyThree() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("YOU ALREADY HAVE THREE CARDS IN YOUR HAND... PLAY A CARD BEFORE DRAWING A NEW ONE!!!");
        alert.show();
    }

    @FXML
    private void cardChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("TURN ERROR");
        alert.setHeaderText(null);
        alert.setContentText("IT'S NOT YOUR TURN, WAIT...");
        alert.show();
    }

    @FXML
    private void publicCardHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("PUBLIC CARD SPOT");
        alert.setHeaderText(null);
        alert.setContentText("PUBLIC CARD SPOT EMPTY...TRY TO DRAW FROM A DECK OR ANOTHER PUBLIC CARD SPOT!!!");
        alert.show();
    }

    @FXML
    private void requiredResourcesHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("REQUIRED RESOURCES ERROR");
        alert.setHeaderText(null);
        alert.setContentText("MORE RESOURCES NEEDED... CHOOSE ANOTHER CARD OR FLIP THIS ONE!!!!!");
        alert.show();
    }

    private void aloneInGameHandler(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText("You are alone in the game, in 30 seconds you will be declared winner if no one rejoins!");
        alert.show();
    }

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
}