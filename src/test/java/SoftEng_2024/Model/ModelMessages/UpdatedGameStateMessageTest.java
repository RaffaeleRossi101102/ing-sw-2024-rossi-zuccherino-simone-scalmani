package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedGameStateMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    UpdatedGameStateMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedGameStateMessage(mex, GameState.STARTER);

        cliViewClient.getLocalModel().setGameState(GameState.CONNECTION);

        testMessage.executeMessage(cliViewClient);
        assertEquals(GameState.STARTER, cliViewClient.getLocalModel().getGameState());

        cliViewClient.getLocalModel().setGameState(GameState.CONNECTION);
        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(GameState.STARTER, cliViewClient.getLocalModel().getGameState());

        cliViewClient.getLocalModel().setGameState(GameState.STARTER);
        testMessage.executeMessage(cliViewClient);
        assertEquals(GameState.STARTER, cliViewClient.getLocalModel().getGameState());
        assertEquals(2, cliViewClient.getLocalModel().getArrivedMessages());
    }
}