package SoftEng_2024.Controller;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.CellState;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.GoalCard.ObjectsGoalCard;
import SoftEng_2024.Model.Observers.GameObserver;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;
    private ObServerManager obServerManager;
    private ServerInterface serverRMI;
    private SocketServer socketServer;
    private List<Player> clientPlayers;
    private Game game;
    private GameObserver gameObserver;
    private NetworkManager networkManager;

    @BeforeEach
    void setUp() throws Exception {
        gameController = new GameController();
        networkManager = new NetworkManager(gameController);
        socketServer = new SocketServer(4567, networkManager);
        serverRMI = new RMIServer(networkManager, 4567);
        obServerManager = new ObServerManager(serverRMI, socketServer);
        game = new Game(new ArrayList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), networkManager);

        gameController.setToViewManager(obServerManager);
        gameController.setNetworkManager(networkManager);
        gameController.setClientPlayers(new ArrayList<>());
        gameController.setServerRMI(serverRMI);
        gameController.setServerSocket(socketServer);

        clientPlayers = new ArrayList<>();
        gameController.setClientPlayers(clientPlayers);

        setPrivateField(gameController, "game", new Game(new ArrayList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), networkManager));
        setPrivateField(gameController, "playerIdMap", new HashMap<>());
        setPrivateField(gameController, "playerObservers", new ArrayList<>());
        setPrivateField(gameController, "maxPlayers", 0);

        gameObserver = new GameObserver(obServerManager, game, networkManager);
        gameController.getGame().setGameObserver(gameObserver);
    }

    @Test
    void gameInit() {
        gameController.gameInit();

        assertNotNull(gameController.getGame());
        assertEquals(gameController.getClientPlayers(), gameController.getGame().getPlayers());

        Queue<Card> goldDeck = gameController.getGame().getGoldDeck();
        Queue<Card> resourceDeck = gameController.getGame().getResourceDeck();
        Queue<Card> starterDeck = gameController.getGame().getStarterDeck();
        Queue<GoalCard> goalCardDeck = gameController.getGame().getGoalCardDeck();

        assertNotNull(goldDeck);
        assertNotNull(resourceDeck);
        assertNotNull(starterDeck);
        assertNotNull(goalCardDeck);

        assertFalse(goldDeck.isEmpty());
        assertFalse(resourceDeck.isEmpty());
        assertFalse(starterDeck.isEmpty());
        assertFalse(goalCardDeck.isEmpty());

        assertNotNull(gameController.getGame().getGameObserver());
    }

    @Test
    void createGame() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;
        int maxPlayers = 4;

        gameController.createGame(maxPlayers, nickname, ID);

        List<Player> clientPlayers = gameController.getClientPlayers();
        assertEquals(1, clientPlayers.size());
        assertEquals(nickname, clientPlayers.get(0).getNickname());
        assertEquals(ID, clientPlayers.get(0).getPlayerObserver().getReceiverID());

        gameController.joinGame(nickname2, ID2);
        gameController.createGame(maxPlayers, nickname2, ID2);

        Game game = gameController.getGame();
        assertTrue(game.getErrorMessageBindingMap().containsKey(ID2));
        assertNotNull(game.getErrorMessageBindingMap().get(ID2));
    }

    @Test
    void joinGame() throws NoSuchFieldException, IllegalAccessException {
        String nickname = "Player1";
        String nickname2 = "Player2";
        String nickname3 = "Player3";
        double ID = 12345;
        double ID2 = 67890;
        double ID3 = 13254;
        int maxPlayers = 0;

        gameController.getGame().setGameState(GameState.CONNECTION);
        gameController.joinGame(nickname, ID);

        Game game = gameController.getGame();
        assertTrue(game.getErrorMessageBindingMap().containsKey(ID));
        assertNotNull(game.getErrorMessageBindingMap().get(ID));
        assertTrue(gameController.getClientPlayers().isEmpty());

        maxPlayers = 2;

        gameController.createGame(maxPlayers, nickname, ID);
        gameController.joinGame(nickname, ID2);
        gameController.joinGame(nickname2, ID2);

        List<Player> clientPlayers = gameController.getClientPlayers();
        assertEquals(2, clientPlayers.size());
        assertEquals(nickname, clientPlayers.get(0).getNickname());
        assertEquals(ID, clientPlayers.get(0).getPlayerObserver().getReceiverID());
        assertEquals(GameState.STARTER, clientPlayers.get(0).getPlayerState());

        gameController.getGame().setGameState(GameState.CHOOSEGOAL);
        gameController.joinGame(nickname3, ID3);

        assertTrue(game.getErrorMessageBindingMap().containsKey(ID));
        assertNotNull(game.getErrorMessageBindingMap().get(ID3));
        game.setErrorMessageBindingMap(ID3, "You tried to join a game that has already started, please wait for it to finish...Or for us to implement multiple games :)");
        assertEquals("You tried to join a game that has already started, please wait for it to finish...Or for us to implement multiple games :)", game.getErrorMessageBindingMap().get(ID3));
    }

    @Test
    void quit2players() throws Exception {
        String nickname = "Player1";
        String nickname2 = "Player2";
        String nickname3 = "Player3";
        double ID = 12345;
        double ID2 = 67890;
        double ID3 = 13254;

        gameController.createGame(2, nickname, ID);
        gameController.quit(ID);
        gameController.quit(ID3);

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);
        gameController.quit(ID2);
        gameController.quit(ID);


        assertFalse(gameController.getGame().getAckIdBindingMap().containsKey(ID));
        assertFalse(gameController.getGame().getAckIdBindingMap().containsKey(ID2));
        assertEquals(GameState.STARTER, gameController.getGame().getGameState());
    }

    @Test
    void quit3players() throws Exception {
        String nickname = "Player1";
        String nickname2 = "Player2";
        String nickname3 = "Player3";
        double ID = 12345;
        double ID2 = 67890;
        double ID3 = 13254;

        gameController.createGame(3, nickname, ID);
        gameController.joinGame(nickname2, ID2);
        gameController.joinGame(nickname3, ID3);

        gameController.getGame().setGameState(GameState.PLAY);
        gameController.getClientPlayers().get(0).setPlayerState(GameState.PLAY);
        gameController.getClientPlayers().get(1).setPlayerState(GameState.PLAY);
        gameController.getClientPlayers().get(2).setPlayerState(GameState.PLAY);
        gameController.getGame().turnStart();
        gameController.quit(ID3);

        assertEquals(GameState.NOTPLAYING, gameController.getClientPlayers().get(2).getPlayerState());
        assertFalse(gameController.getGame().getAckIdBindingMap().containsKey(ID3));

        gameController.getGame().setGameState(GameState.STARTER);
        gameController.quit(ID2);
        assertEquals(1, gameController.getGame().getOnlinePlayersCounter());
        assertEquals(GameState.NOTPLAYING, gameController.getClientPlayers().get(1).getPlayerState());
    }

    @Test
    void reJoinGame() throws IOException {
        String nickname = "Player1";
        String nickname2 = "Player2";
        String nickname3 = "Player3";
        String nickname4 = "Player4";
        double ID = 12345;
        double ID2 = 67890;
        double ID3 = 13254;
        double ID4 = 29876;
        int maxPlayers = 0;

        gameController.reJoinGame(nickname2, ID2);
        gameController.createGame(3, nickname, ID);
        gameController.reJoinGame(nickname2, ID2);
        gameController.joinGame(nickname2, ID2);
        gameController.joinGame(nickname3, ID3);

        gameController.quit(ID);
        assertEquals(2, gameController.getGame().getOnlinePlayersCounter());
        gameController.reJoinGame(nickname4, ID);
        gameController.getGame().setErrorMessageBindingMap(ID4, "The nickname you chose doesn't belong to anyone that is offline! Please insert the nickname you had before disconnecting...");
        assertEquals("The nickname you chose doesn't belong to anyone that is offline! Please insert the nickname you had before disconnecting...", gameController.getGame().getErrorMessageBindingMap().get(ID4));

        gameController.reJoinGame(nickname, ID);
    }

    @Test
    void reJoinGame_checkNextState() throws Exception {
        String nickname1 = "Player1";
        double ID1 = 12345;
        double newID1 = 54321;
        String nickname2 = "Player2";
        double ID2 = 67890;

        gameController.createGame(3, nickname1, ID1);
        gameController.joinGame(nickname2, ID2);

        gameController.getClientPlayers().get(0).setPlayerState(GameState.CHOOSEGOAL);
        gameController.getClientPlayers().get(0).setOnline(false);
        gameController.getGame().setOnlinePlayersCounter(-1);
        gameController.getGame().setGameState(GameState.STARTER);

        gameController.reJoinGame(nickname1, newID1);

        assertEquals(2, gameController.getGame().getOnlinePlayersCounter());
        assertEquals(newID1, gameController.getClientPlayers().get(0).getPlayerObserver().getReceiverID());
    }

    @Test
    void reJoinGame_turnStart() throws Exception {
        String nickname1 = "Player1";
        double ID1 = 12345;
        double newID1 = 54321;
        String nickname2 = "Player2";
        double ID2 = 67890;

        gameController.createGame(3, nickname1, ID1);
        gameController.joinGame(nickname2, ID2);

        gameController.getClientPlayers().get(0).setPlayerState(GameState.NOTPLAYING);
        gameController.getClientPlayers().get(0).setOnline(false);
        gameController.getGame().setOnlinePlayersCounter(-1);
        gameController.getGame().setGameState(GameState.PLAY);

        gameController.reJoinGame(nickname1, newID1);

        assertTrue(gameController.getGame().getOnlinePlayersCounter() > 0);
        assertEquals(newID1, gameController.getClientPlayers().get(0).getPlayerObserver().getReceiverID());
        assertTrue(gameController.getGame().getAckIdBindingMap().get(newID1));
    }

    @Test
    void playStarterCard() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        StarterCard testCard = new StarterCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0);
        gameController.getClientPlayers().get(0).setStarterCard(testCard);
        gameController.getClientPlayers().get(0).setOnline(false);
        gameController.getGame().setGameState(GameState.STARTER);
        gameController.playStarterCard(true, ID);
        assertNotNull(gameController.getClientPlayers().get(0).getPlayerBoard().getCardBoard()[42][42].getCard());

        gameController.getClientPlayers().get(1).setStarterCard(testCard);
        gameController.getClientPlayers().get(1).getPlayerBoard().getCardBoard()[42][42].setCellState(CellState.NOTPLACEABLE);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            gameController.playStarterCard(true, ID2);
        });

        assertTrue(exception.getMessage().contains("Something went really wrong"));
    }

    @Test
    void setColor() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.getGame().setGameState(GameState.SETCOLOR);
        gameController.getClientPlayers().get(0).setPlayerState(GameState.SETCOLOR);
        gameController.getClientPlayers().get(1).setPlayerState(GameState.SETCOLOR);

        gameController.setColor(Color.RED, ID);
        gameController.setColor(Color.RED, ID2);

        assertTrue(gameController.getGame().getErrorMessageBindingMap().containsKey(ID2));

        gameController.getClientPlayers().get(1).setOnline(false);
        gameController.setColor(Color.GREEN, ID2);

        assertEquals(Color.RED, gameController.getClientPlayers().get(0).getColor().get(0));
    }

    @Test
    void updatePublicGoals() {
        gameController.gameInit();
        gameController.updatePublicGoals();

        assertNotNull(gameController.getGame().getPublicGoals()[0]);
        assertNotNull(gameController.getGame().getPublicGoals()[1]);
    }

    @Test
    void choosePrivateGoals() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        assertEquals(GameState.STARTER, gameController.getGame().getGameState());

        List<GoalCard> goalDeck = new LinkedList<>();
        goalDeck.addAll(gameController.getGame().getGoalCardDeck());
        gameController.getClientPlayers().get(0).setAvailableGoals(goalDeck);
        gameController.getClientPlayers().get(1).setAvailableGoals(goalDeck);

        gameController.getGame().setGameState(GameState.CHOOSEGOAL);
        gameController.getClientPlayers().get(0).setPlayerState(GameState.CHOOSEGOAL);
        gameController.getClientPlayers().get(1).setPlayerState(GameState.CHOOSEGOAL);

        gameController.choosePrivateGoals(1, ID);
        gameController.getClientPlayers().get(1).setOnline(false);
        gameController.choosePrivateGoals(1, ID2);

        assertNotNull(gameController.getClientPlayers().get(0).getAvailableGoals());
        assertNotNull(gameController.getClientPlayers().get(1).getAvailableGoals());
    }

    @Test
    void playCard() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 1, new boolean[]{false, false, false, false});
        StarterCard testCard = new StarterCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0);
        gameController.getClientPlayers().get(0).setStarterCard(testCard);
        gameController.playStarterCard(true, ID);

        gameController.getClientPlayers().get(0).getHand().add(gameController.getGame().getGoldDeck().poll());
        gameController.getClientPlayers().get(0).getHand().add(gameController.getGame().getGoldDeck().poll());
        gameController.getClientPlayers().get(1).getHand().add(gameController.getGame().getGoldDeck().poll());

        gameController.getGame().turnStart();
        gameController.playCard(0, 0, 0, false, ID);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID).contains("You tried to place a card where you couldn't! Please try placing it in a different place."));
        gameController.playCard(0, 0,0, false, ID2);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID2).contains("You tried to place a card during another player's turn! Please wait for your turn to place a card."));
        gameController.playCard(0,43,43, false, ID);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID).contains("You don't have enough resources to play this card! Please choose a different one."));
        gameController.playCard(0,43,43, true, ID);
        gameController.playCard(0,41,43, true, ID);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID).contains("You've already placed a card this turn! You can only draw a card."));
    }

    @Test
    void drawFromTheDeck() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.getGame().turnStart();
        gameController.getClientPlayers().get(0).setPlayerState(GameState.NOTPLAYING);
        gameController.getClientPlayers().get(1).setPlayerState(GameState.NOTPLAYING);

        gameController.drawFromTheDeck(1, ID);
        gameController.drawFromTheDeck(1, ID2);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID2).contains("You tried to draw a card during another player's turn! Please wait for your turn."));
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID).contains("You tried to draw a card but you've already drawn one! Please wait for your next turn to play and draw."));
    }

    @Test
    void drawFromPublicCards() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.getGame().turnStart();
        gameController.getClientPlayers().get(0).setPlayerState(GameState.NOTPLAYING);
        gameController.getClientPlayers().get(1).setPlayerState(GameState.NOTPLAYING);

        gameController.drawFromPublicCards(1, ID);
        gameController.drawFromPublicCards(1, ID2);
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID2).contains("You tried to draw a card during another player's turn! Please wait for your turn."));
        assertTrue(gameController.getGame().getErrorMessageBindingMap().get(ID).contains("You tried to draw a card but you've already drawn one! Please wait for your next turn to play and draw."));
    }

    @Test
    void whisper() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        String nickname3 = "Player3";
        double ID = 12345;
        double ID2 = 67890;
        double ID3 = 13254;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.getGame().getPlayers().get(1).setOnline(true);
        gameController.whisper(nickname2, "hello", ID);

        gameController.getGame().getPlayers().get(1).setOnline(false);
        gameController.whisper(nickname2, "hello", ID);
        gameController.whisper(nickname3, "hello", ID);

        assertNotNull(gameController.getGame().getErrorMessageBindingMap().get(ID));
    }

    @Test
    void broadcast() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.broadcast("hello", ID);
    }

    @Test
    void handOutStarterCards() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 1, new boolean[]{false, false, false, false});
        StarterCard testCard = new StarterCard(testFront, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0);
        gameController.getGame().getStarterDeck().add(testCard);
        gameController.getGame().getStarterDeck().add(testCard);

        gameController.handOutStarterCards();
        assertNotNull(gameController.getClientPlayers().get(0).getStarterCard());
        assertNotNull(gameController.getClientPlayers().get(1).getStarterCard());
    }

    @Test
    void handOutPrivateGoals() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.handOutPrivateGoals();
        assertNotNull(gameController.getClientPlayers().get(0).getAvailableGoals());
        assertNotNull(gameController.getClientPlayers().get(1).getAvailableGoals());
    }

    @Test
    void handOutCards() {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.handOutCards();
        assertNotNull(gameController.getClientPlayers().get(0).getHand());
    }

    @Test
    void getToViewManager() {
        ObServerManager actual = gameController.getToViewManager();
        assertEquals(obServerManager, actual);
    }

    @Test
    void setToViewManager() {
        gameController.setToViewManager(obServerManager);
        assertNotNull(gameController.getToViewManager());
    }

    @Test
    void setServerRMI() {
        gameController.setServerRMI(serverRMI);
        assertNotNull(gameController.getToViewManager().getServerRMI());
    }

    @Test
    void setServerSocket() {
        gameController.setServerSocket(socketServer);
        assertNotNull(gameController.getToViewManager().getSocketServer());
    }

    @Test
    void getGame() {
        Game actual = gameController.getGame();
        assertEquals(gameController.getGame(), actual);
    }

    @Test
    void getClientPlayers() {
        List<Player> actual = gameController.getClientPlayers();
        assertEquals(gameController.getClientPlayers(), actual);
    }

    @Test
    void setClientPlayers() {
        gameController.setClientPlayers(clientPlayers);
        assertNotNull(gameController.getClientPlayers());
    }

    @Test
    void setNetworkManager() {
        gameController.setNetworkManager(networkManager);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}