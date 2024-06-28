package SoftEng_2024.Model.Observers;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.BroadcastMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerObserverTest {

    GameController gameController;
    NetworkManager networkManager;
    SocketServer socketServer;
    RMIServer serverRMI;
    ObServerManager obServerManager;
    Game game;
    PlayerObserver playerObserver;
    GameObserver gameObserver;
    List<Player> clientPlayers;

    @BeforeEach
    void setUp() throws Exception {
        gameController = new GameController();
        networkManager = new NetworkManager(gameController);
        socketServer = new SocketServer(4567, networkManager);
        serverRMI = new RMIServer(networkManager, 4567);
        obServerManager = new ObServerManager(serverRMI, socketServer);
        game = new Game(new ArrayList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), networkManager);
        playerObserver = new PlayerObserver(obServerManager, 12345, "me");

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

    PlayerObserverTest() throws RemoteException {
    }

    @Test
    void updatedHand() {
        playerObserver.updatedHand(new ArrayList<>(), "me");
    }

    @Test
    void updatedIsPlayerOnline() {
        playerObserver.updatedIsPlayerOnline(true, "me");
        playerObserver.updatedIsPlayerOnline(false, "me");
    }

    @Test
    void updatedPlayerColor() {
        playerObserver.updatedPlayerColor(Color.YELLOW, "me");
    }

    @Test
    void updatedPlayerState() {
        playerObserver.updatedPlayerState(GameState.STARTER, "me");
    }

    @Test
    void updatedNickname() {
        playerObserver.updatedNickname("me2", 12345);
        playerObserver.updatedNickname("me2", 12344);
    }

    @Test
    void updatedAvailableGoals() {
        playerObserver.updatedAvailableGoals(new ArrayList<>(), "me");
    }

    @Test
    void setReceiverID() {
        playerObserver.setReceiverID(12345);
    }

    @Test
    void updatedStarterCard() {
        playerObserver.updatedStarterCard(new StarterCard(null, false, null, 0));
    }

    @Test
    void getObservedNickname() {
        assertEquals("me", playerObserver.getObservedNickname());
    }

    @Test
    void playerRejoining() {
        String nickname = "me";
        String nickname2 = "Player2";
        double ID = 12345;
        double ID2 = 67890;

        gameController.gameInit();
        gameController.createGame(2, nickname, ID);
        gameController.joinGame(nickname2, ID2);

        playerObserver.playerRejoining(gameController.getGame());
    }

    @Test
    void notifyServer() {
        playerObserver.notifyServer(new BroadcastMessage(12345, "me"));
    }

    @Test
    void getReceiverID() {
        assertEquals(12345, playerObserver.getReceiverID());
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}