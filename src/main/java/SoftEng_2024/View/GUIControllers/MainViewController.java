package SoftEng_2024.View.GUIControllers;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


// NEEDS TO BE SEPARATED IN EVERY CORRESPONDING CLASS - OR ADD COMMENTS TO EXPLAIN EVERYTHING
public class MainViewController {
    private static ClientInterface client;
    private static double ID;
    private static boolean flipped, auxFlip;

    private static int columnIndex, rowIndex;
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
    private static String nickname;
    private static Color color;
    @FXML
    private TextField textField, intField;
    @FXML
    Label nickLabel, pcLabel;
    @FXML
    ImageView backImage, frontImage, goalCardHand, goal1, goal2, hand1, hand2, hand3, deck1, deck2, public1, public2, public3, public4;
    @FXML
    Button redBTN, ylwBTN, bluBTN, grnBTN, prvBTN;
    @FXML
    GridPane cardPane;

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
            if (event.getCode() == KeyCode.ENTER && textField.getText().length() < 11) {
                setNickname(textField.getText());
                nickLabel.setText("Nickname: " + nickname);
                textField.clear();
                nickLabel.requestFocus();
            }
        });
    }

    public void setPlayerCountArea() {
        intField.setOnKeyPressed(event -> {
            String temp;
            if (event.getCode() == KeyCode.ENTER) {
                temp = intField.getText();
                setPlayerCount(Integer.parseInt(temp));
                pcLabel.setText("Player Count: " + playerCount);
                intField.clear();
                pcLabel.requestFocus();
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

    public void switchToWaitingScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/WaitingScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToCreateSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CreateScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToJoinSetup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/JoinScreen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToStarterPlay() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/StarterPlay.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        backImage = (ImageView) scene.lookup("#backImage");
        frontImage = (ImageView) scene.lookup("#frontImage");
        backImage.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getStarterCard().getCardID())))));
        frontImage.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getStarterCard().getCardID())))));
    }

    public void switchToColorChoice() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ColorChoice.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToGoalChoice() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GoalChoice.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        goal1 = (ImageView) scene.lookup("#goal1");
        goal2 = (ImageView) scene.lookup("#goal2");
        goal1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getAvailableGoals().get(0).getCardID())))));
        goal2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getAvailableGoals().get(1).getCardID())))));
    }

    public void switchToMainGame() throws IOException {
        flipped = false;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainGame.fxml")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        goalCardHand = (ImageView) scene.lookup("#goalCardHand");
        goalCardHand.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, cardID)))));

        hand1 = (ImageView) scene.lookup("#hand1");
        hand2 = (ImageView) scene.lookup("#hand2");
        hand3 = (ImageView) scene.lookup("#hand3");
        hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(0).getCardID())))));
        hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(1).getCardID())))));
        hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(2).getCardID())))));

        public1 = (ImageView) scene.lookup("#public1");
        public2 = (ImageView) scene.lookup("#public2");
        public3 = (ImageView) scene.lookup("#public3");
        public4 = (ImageView) scene.lookup("#public4");
        public1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(0).getCardID())))));
        public2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(1).getCardID())))));
        public3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(2).getCardID())))));
        public4.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPublicCards().get(3).getCardID())))));

        deck1 = (ImageView) scene.lookup("#deck1");
        deck2 = (ImageView) scene.lookup("#deck2");
        deck1TopResource(localModel.getTopResourceCard());
        deck2TopResource(localModel.getTopGoldCard());

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
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getStarterCard().getCardID())))));
                        } else {
                            imageView.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getStarterCard().getCardID())))));
                        }
                    }
                    imageView.setFitWidth(155);
                    imageView.setFitHeight(103);
                    imageView.setOnMouseClicked(this::handleCellClick);
                    cardPane.add(imageView, col, row);
                }
            }
        }
        // setAvailableCellsImg();
    }

    public void confirmCreateGame(ActionEvent event) {
        try {
            assert client != null;
            client.registerToServer(ID, client);
            ViewMessage msg = new CreateGameMessage(nickname, playerCount, ID);
            client.update(msg);
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.CONNECTION);
        } catch (RemoteException | NotBoundException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void confirmJoinGame(ActionEvent event) {
        try {
            assert client != null;
            client.registerToServer(ID, client);
            ViewMessage msg = new JoinGameMessage(nickname, ID);
            client.update(msg);
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.CONNECTION);
        } catch (RemoteException | NotBoundException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void confirmReJoinGame(ActionEvent event) {
        try {
            assert client != null;
            ViewMessage msg = new ReJoinMessage(ID, nickname);
            client.update(msg);
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.CONNECTION);
        } catch (RemoteException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void confirmPlayCard() {
        if (localModel.getPlayerState() == GameState.PLAY) {
            if (flipped || (!flipped && localModel.getPersonalHand().get(handIndex).getFront().checkRequiredResources(localModel.getPlayersBoards().get(nickname).getAnglesCounter(), localModel.getPersonalHand().get(handIndex)))) {
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
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
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
            inWaitingScreen(GameState.STARTER);
        } catch (RemoteException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void goToGoalChoice(ActionEvent event) {
        try {
            ViewMessage msg = new SetColorMessage(color, ID);
            client.update(msg);
            // WAITING
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.SETCOLOR);
        } catch (RemoteException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void goToPlayGame(ActionEvent event) {
        try {
            ViewMessage msg = new ChoosePrivateGoalMessage(choice, ID);
            client.update(msg);
            // WAITING
            switchToWaitingScreen(event);
            inWaitingScreen(GameState.CHOOSEGOAL);
        } catch (RemoteException e) {
            serverWarningHandler();
            System.exit(1);
        } catch (IOException e) {
            IOWarningHandler();
            System.exit(1);
        }
    }

    public void inWaitingScreen(GameState state) throws IOException {
        prevState = state;
        new Thread(() -> {
            while (!localModel.isAckReceived()) {
                try {
                    Thread.sleep(10);
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

    @FXML
    private void handleCellClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ImageView imageView = (ImageView) event.getSource();
            columnIndex = GridPane.getColumnIndex(imageView);
            rowIndex = GridPane.getRowIndex(imageView);
            if (!localModel.getPlayersBoards().get(nickname).getCardBoard()[rowIndex][columnIndex].getPlaceable()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("PLACEABLE ERROR");
                alert.setHeaderText(null);
                alert.setContentText("CELL NOT AVAILABLE...CHOSE ANOTHER ONE !!!!!");
                alert.show();
            }
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
        if (clickedImage == hand1) handIndex = 0;
        else if (clickedImage == hand2) handIndex = 1;
        else if (clickedImage == hand3) handIndex = 2;
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
        if (flipped) {
            setFlipped(false);
            switch (handIndex) {
                case 0:
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
                case 1:
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
                case 2:
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_FRONT, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
            }
        } else {
            setFlipped(true);
            switch (handIndex) {
                case 0:
                    hand1.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
                case 1:
                    hand2.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
                case 2:
                    hand3.setImage(new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream(String.format(IMAGE_PATH_BACK, localModel.getPersonalHand().get(handIndex).getCardID())))));
                    break;
            }
        }
        // carica immagine del back o del front nell'if
        System.out.println("FLIP DEBUG: picked flipped " + flipped);
        System.out.println(columnIndex + " " + rowIndex);
    }

    @FXML
    public void handlePickColor(MouseEvent event) {
        if (prvBTN != null) {
            prvBTN.setScaleX(1.0);
            prvBTN.setScaleY(1.0);
        }
        Button clickedButton = (Button) event.getSource();
        clickedButton.setScaleX(1.1);
        clickedButton.setScaleY(1.1);
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
        alert.setTitle("Quitting Game :(");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to quit?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0);
            }
        });
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
        alert.setContentText("COLOR ALREADY CHOSEN... CHOSE ANOTHER ONE!!");
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
        alert.setContentText("YOU ALREADY HAVE THREE CARD ON YOR HAND... PLAY A CARD BEFOR DRAW A NEW ONE!!!");
        alert.show();
    }

    @FXML
    private void cardChosenHandler() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("TURN ERROR");
        alert.setHeaderText(null);
        alert.setContentText("IS NOT YOUR TURN WAIT...");
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
        alert.setContentText("MORE RESOURCES NEEDED... CHOSE ANOTHER CARD OR FLIP THIS ONE!!!!!");
        alert.show();
    }
}