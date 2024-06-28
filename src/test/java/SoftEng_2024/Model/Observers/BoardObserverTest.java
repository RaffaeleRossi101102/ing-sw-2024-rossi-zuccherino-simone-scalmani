package SoftEng_2024.Model.Observers;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.BroadcastMessage;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class BoardObserverTest {

    GameController gameController = new GameController();
    NetworkManager networkManager = new NetworkManager(gameController);
    SocketServer socketServer = new SocketServer(4567, networkManager);
    RMIServer serverRMI = new RMIServer(networkManager, 4567);
    ObServerManager obServerManager = new ObServerManager(serverRMI, socketServer);
    BoardObserver observer = new BoardObserver("me", obServerManager);
    Board board = new Board();

    BoardObserverTest() throws RemoteException {
    }

    @Test
    void updatedBoard() {
        observer.updatedBoard(board);
    }

    @Test
    void notifyServer() {
    }

    @Test
    void getCallerNickname() {
        assertEquals("me", observer.getCallerNickname());
    }
}