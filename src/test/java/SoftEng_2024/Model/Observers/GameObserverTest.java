package SoftEng_2024.Model.Observers;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.GoldCard;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.BroadcastMessage;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GameObserverTest {

    GameController gameController = new GameController();
    NetworkManager networkManager = new NetworkManager(gameController);
    SocketServer socketServer = new SocketServer(4567, networkManager);
    RMIServer serverRMI = new RMIServer(networkManager, 4567);
    ObServerManager obServerManager = new ObServerManager(serverRMI, socketServer);
    Game game = new Game(new ArrayList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), networkManager);
    GameObserver gameObserver = new GameObserver(obServerManager, game, networkManager);
    GoalCard[] publicGoals = new GoalCard[]{null, null, null};

    GameObserverTest() throws RemoteException {
    }

    @Test
    void updatedDeck() {
        gameObserver.updatedDeck("me", new ResourceCard(null, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0), 0);
        gameObserver.updatedDeck("me", new ResourceCard(null, false, new Angles[]{Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.EMPTY, Angles.FUNGI}, 0), 1);
        gameObserver.updatedDeck("me", null, 0);
        gameObserver.updatedDeck("me", null, 1);
    }

    @Test
    void updatedPublicCards() {
        gameObserver.updatedPublicCards("me", 0);
        gameObserver.updatedPublicCards("me", 1);
        gameObserver.updatedPublicCards("me", 2);
        gameObserver.updatedPublicCards("me", 3);
    }

    @Test
    void updatedPublicGoals() {
        gameObserver.updatedPublicGoals(publicGoals);
    }

    @Test
    void updatedCurrentPlayer() {
        gameObserver.updatedCurrentPlayer("me");
    }

    @Test
    void updatedGameState() {
        gameObserver.updatedGameState(GameState.PLAY);
    }

    @Test
    void updatedAck() {
        gameObserver.updatedAck(true, 12345);
    }

    @Test
    void updatedError() {
        gameObserver.updatedError(12345, "err");
    }

    @Test
    void unRegisterClient() throws IOException {
        gameObserver.unRegisterClient("err", 12345);
    }

    @Test
    void gameIsEnding() {
        gameObserver.gameIsEnding();
    }

    @Test
    void lastPlayerStanding() {
        gameObserver.lastPlayerStanding("me");
    }

    @Test
    void removedPlayer() {
        gameObserver.removedPlayer("me");
    }

    @Test
    void updatedWinners() {
        gameObserver.updatedWinners(new ArrayList<String>());
    }

    @Test
    void winnerDueToForfeit() {
        gameObserver.winnerDueToForfeit("me");
    }

    @Test
    void notifyServer() {
        gameObserver.notifyServer(new BroadcastMessage(12345, "null"));
    }
}