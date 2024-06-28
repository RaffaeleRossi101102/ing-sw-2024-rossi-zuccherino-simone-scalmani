package SoftEng_2024.Model.GoalCard;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.Game;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResourceGoalCardTest {

    private GameController gameController;
    private ObServerManager obServerManager;
    private ServerInterface serverRMI;
    private SocketServer socketServer;
    private List<Player> clientPlayers;
    private Game game;
    private GameObserver gameObserver;
    private NetworkManager networkManager;
    private ResourceGoalCard testGoal;

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

        testGoal = new ResourceGoalCard(Angles.FUNGI, 2, String.format("Get %s points for each triplet of mushrooms on the board", 2), 0);
    }

    @Test
    void calcScore() throws Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException {
        String nickname = "Player1";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        gameController.getClientPlayers().get(0).setNickname(nickname, ID);

        gameController.getGame().getPlayers().get(0).addCard(gameController.getGame().getStarterDeck().poll());
        ResourceFront testFront = new ResourceFront(new Angles[]{Angles.INK, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        ResourceCard testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0);
        gameController.getGame().getPlayers().get(0).addCard(testCard);
        testFront = new ResourceFront(new Angles[]{Angles.INK, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0);
        gameController.getGame().getPlayers().get(0).addCard(testCard);
        testFront = new ResourceFront(new Angles[]{Angles.FEATHER, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0);
        gameController.getGame().getPlayers().get(0).addCard(testCard);
        testFront = new ResourceFront(new Angles[]{Angles.SCROLL, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY}, 0, new boolean[]{false, false, false, false});
        testCard = new ResourceCard(testFront, true, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0);
        gameController.getGame().getPlayers().get(0).addCard(testCard);

        gameController.getGame().getPlayers().get(0).getHand().get(0).setFlipped(true);
        gameController.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(42, 42, gameController.getGame().getPlayers().get(0).getHand().get(0));
        gameController.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 43, gameController.getGame().getPlayers().get(0).getHand().get(1));
        gameController.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 43, gameController.getGame().getPlayers().get(0).getHand().get(2));
        gameController.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(43, 41, gameController.getGame().getPlayers().get(0).getHand().get(3));
        gameController.getGame().getPlayers().get(0).getPlayerBoard().updateBoard(41, 41, gameController.getGame().getPlayers().get(0).getHand().get(4));

        int[] anglesCounter = gameController.getGame().getPlayers().get(0).getPlayerBoard().getAnglesCounter();

        int score = testGoal.calcScore(gameController.getGame().getPlayers().get(0).getPlayerBoard());
        int expectedScore = (anglesCounter[Angles.FUNGI.ordinal()] / 3) * testGoal.getPoints();
        assertEquals(expectedScore, score);
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}