package SoftEng_2024.Model;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Observers.BoardObserver;
import SoftEng_2024.Model.Observers.GameObserver;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private GameController gameController;
    private ObServerManager obServerManager;
    private ServerInterface serverRMI;
    private SocketServer socketServer;
    private List<Player> clientPlayers;
    private List<String> winners;
    private Game game;
    private GameObserver gameObserver;
    private NetworkManager networkManager;
    private BoardObserver boardObserver;
    GoalCard[] publicGoals;

    @BeforeEach
    void setUp() throws Exception {
        gameController = new GameController();
        networkManager = new NetworkManager(gameController);
        socketServer = new SocketServer(4567, networkManager);
        serverRMI = new RMIServer(networkManager, 4567);
        obServerManager = new ObServerManager(serverRMI, socketServer);
        boardObserver =  new BoardObserver("paolo", obServerManager);
        game = new Game(new ArrayList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), networkManager);
        publicGoals = new GoalCard[]{null, null};

        gameController.setToViewManager(obServerManager);
        gameController.setNetworkManager(networkManager);
        gameController.setClientPlayers(new ArrayList<>());
        gameController.setServerRMI(serverRMI);
        gameController.setServerSocket(socketServer);

        winners = new ArrayList<>();
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
    void gameEnd() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.updatePublicGoals();
        gameController.handOutPrivateGoals();

        gameController.getGame().gameEnd();
    }

    @Test
    void setWinners() {
        gameController.gameInit();
        gameController.getGame().setWinners(winners);
    }

    @Test
    void turnStart() {

        gameController.gameInit();
        Board testBoard = new Board();

        gameController.getGame().turnStart();

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(1).setNickname("nino",1);
        gameController.getGame().getPlayers().get(1).setOnline(true);

        gameController.getGame().turnStart();

        gameController.getGame().getPlayers().get(0).setOnline(false);
        gameController.getGame().turnStart();
        assertEquals(1, 1%gameController.getGame().getPlayers().size());
    }

    @Test
    void checkIfGameEnd() {
        gameController.gameInit();
        Board testBoard = new Board();

        assertTrue(gameController.getGame().checkIfGameEnd());

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);

        assertFalse(gameController.getGame().checkIfGameEnd());
    }

    @Test
    void checkIfIsLastTurn() {
        gameController.gameInit();
        Board testBoard = new Board();

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);

        gameController.getGame().checkIfIsLastTurn();
        assertNotEquals(GameState.ENDGAME, gameController.getGame().getPlayers().get(0).getPlayerState());

        gameController.getGame().setMaxScore(25);
        gameController.getGame().checkIfIsLastTurn();
        assertEquals(GameState.ENDGAME, gameController.getGame().getPlayers().get(0).getPlayerState());
    }

    @Test
    void turnEnd() {
        gameController.gameInit();
        Board testBoard = new Board();

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);

        gameController.getGame().turnStart();
        gameController.getGame().turnEnd("paolo");

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(1).setNickname("nino",1);
        gameController.getGame().getPlayers().get(1).setOnline(true);

        gameController.getGame().turnStart();
        gameController.getGame().turnEnd("paolo");
    }

    @Test
    void playCard() {
        gameController.gameInit();
        Board testBoard = new Board();

        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(1).setNickname("nino",1);
        gameController.getGame().getPlayers().get(1).setOnline(true);
        gameController.getGame().getPlayers().get(0).getHand().add(gameController.getGame().getStarterDeck().poll());
        gameController.getGame().getPlayers().get(1).getHand().add(gameController.getGame().getStarterDeck().poll());

        assertFalse(gameController.getGame().getPlayers().get(0).getHand().isEmpty());
        assertFalse(gameController.getGame().getPlayers().get(1).getHand().isEmpty());

        int code = gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), 42, 42);
        assertEquals(-3, code);

        gameController.getGame().turnStart();
        gameController.getGame().getPlayers().get(0).addCard(gameController.getGame().getStarterDeck().poll());
        code = gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), 0, 0);
        assertEquals(-1, code);

        gameController.getGame().getPlayers().get(0).addCard(gameController.getGame().getStarterDeck().poll());
        code = gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), 42, 42);
        assertEquals(1, code);
    }

//    @Test
//    void drawPublicCards() {
//        gameController.gameInit();
//        Board testBoard = new Board();
//
//        gameController.createGame(2, "paolo", 12345);
//        gameController.joinGame("nino", 67890);
//
//        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
//        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
//        gameController.getGame().getPlayers().get(0).setOnline(true);
//
//        int actual = gameController.getGame().drawPublicCards(gameController.getGame().getPlayers().get(0), 0);
//        assertEquals(-2, actual);
//
//        gameController.getGame().turnStart();
//        gameController.getGame().getPlayers().get(0).getHand().add(gameController.getGame().getStarterDeck().poll());
//        gameController.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
//        gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), 42, 42);
//
//        actual = gameController.getGame().drawPublicCards(gameController.getGame().getPlayers().get(0), 0);
//        assertEquals(1, actual);
//
//        gameController.getGame().turnStart();
//        gameController.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
//        gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), 43, 43);
//
//        for(int i=1; i<4; i++) {
//            actual = gameController.getGame().drawPublicCards(gameController.getGame().getPlayers().get(0), i);
//            assertEquals(1, actual);
//            gameController.getGame().turnStart();
//            gameController.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
//            gameController.getGame().playCard(gameController.getGame().getPlayers().get(0).getHand().get(0), gameController.getGame().getPlayers().get(0), i + 43, i + 43);
//        }
//
//        gameController.getGame().getPublicCards().clear();
//        actual = gameController.getGame().drawPublicCards(gameController.getGame().getPlayers().get(0), 0);
//        assertEquals(-1, actual);
//    }

    @Test
    void drawFromTheDeck() {
        gameController.gameInit();
        Board testBoard = new Board();
        int actual;

        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);
        gameController.getGame().turnStart();

        actual = gameController.getGame().drawFromTheDeck(gameController.getGame().getPlayers().get(0), 0);
        assertEquals(0, actual);
    }

    @Test
    void handOutCards() {
        gameController.gameInit();
        Board testBoard = new Board();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(1).setNickname("nino",1);
        gameController.getGame().getPlayers().get(1).setOnline(true);
        gameController.getGame().turnStart();

        gameController.getGame().handOutCards(gameController.getGame().getPlayers().get(0), true);
    }

    @Test
    void updatePublicCards() {
        gameController.gameInit();
        Board testBoard = new Board();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);

        gameController.getGame().updatePublicCards();
    }

    @Test
    void getPublicGoals() {
        assertNull(gameController.getGame().getPublicGoals());
    }

    @Test
    void setAckIdBindingMap() {
        gameController.gameInit();
        Board testBoard = new Board();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        gameController.getGame().setAckIdBindingMap(12345, true);
        gameController.getGame().setAckIdBindingMap(12344, true);
    }

    @Test
    void setErrorMessageBindingMap() {
        gameController.getGame().setErrorMessageBindingMap(12345, "err");
    }

    @Test
    void setAckAndError() {
        gameController.getGame().setAckAndError(12345, "err");
    }

    @Test
    void removePlayerFromList() {
        gameController.gameInit();
        Board testBoard = new Board();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().getPlayers().add(new Player(new ArrayList<>(), testBoard));
        gameController.getGame().getPlayers().get(0).setNickname("paolo",0);
        gameController.getGame().getPlayers().get(0).setOnline(true);

        gameController.getGame().removePlayerFromList(gameController.getGame().getPlayers().get(0), 12345);
    }

    @Test
    void setPublicGoals() {
        gameController.gameInit();
        gameController.getGame().setPublicGoals(publicGoals);
    }

    @Test
    void setGameState() {
        gameController.getGame().setGameState(GameState.PLAY);
        assertEquals(GameState.PLAY, gameController.getGame().getGameState());
    }

    @Test
    void setOnlinePlayersCounter() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        gameController.getGame().setOnlinePlayersCounter(1);
        assertEquals(3, gameController.getGame().getOnlinePlayersCounter());
        gameController.getGame().setOnlinePlayersCounter(-1);
        gameController.getGame().setOnlinePlayersCounter(-1);
        assertEquals(1, gameController.getGame().getOnlinePlayersCounter());
    }

    @Test
    void triggerWinnerDueToForfeit() {
        gameController.gameInit();
        gameController.getGame().triggerWinnerDueToForfeit();
    }

    @Test
    void getGoalCardDeck() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getGoalCardDeck());
    }

    @Test
    void getPlayers() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getPlayers());
    }

    @Test
    void getPublicCards() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getPublicCards());
    }

    @Test
    void getCurrentPlayer() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().turnStart();
        assertEquals(gameController.getGame().getCurrentPlayer(), gameController.getGame().getPlayers().get(0));
    }

    @Test
    void getGoldDeck() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getGoldDeck());
    }

    @Test
    void getResourceDeck() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getResourceDeck());
    }

    @Test
    void getGameObserver() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getGameObserver());
    }

    @Test
    void getStarterDeck() {
        gameController.gameInit();
        assertNotNull(gameController.getGame().getStarterDeck());
    }

    @Test
    void getGameState() {
        gameController.gameInit();
        assertEquals(GameState.CONNECTION, gameController.getGame().getGameState());
    }

    @Test
    void setPlayerIndex() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().setPlayerIndex(1);
    }

    @Test
    void shufflePlayers() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        List<Player> players = gameController.getGame().getPlayers();
        gameController.getGame().shufflePlayers();
        assertEquals(2, gameController.getGame().getPlayers().size());
    }

    @Test
    void getAckIdBindingMap() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        gameController.getGame().setAckIdBindingMap(12345, true);

        assertFalse(gameController.getGame().getAckIdBindingMap().isEmpty());
    }

    @Test
    void getErrorMessageBindingMap() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        assertFalse(gameController.getGame().getErrorMessageBindingMap().isEmpty());
    }

    @Test
    void setGameObserver() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);

        gameController.getGame().setGameObserver(gameObserver);
        assertEquals(gameObserver, gameController.getGame().getGameObserver());
    }

    @Test
    void setMaxScore() {
        gameController.gameInit();
        gameController.getGame().setMaxScore(25);
        assertEquals(25, gameController.getGame().getMaxScore());
    }

    @Test
    void getMaxScore() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        assertEquals(0, gameController.getGame().getMaxScore());
    }

    @Test
    void getOnlinePlayersCounter() {
        gameController.gameInit();
        gameController.createGame(2, "paolo", 12345);
        gameController.joinGame("nino", 67890);
        assertEquals(2, gameController.getGame().getOnlinePlayersCounter());
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}