package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedPlayerStateMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    UpdatedPlayerStateMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedPlayerStateMessage(12345, mex, GameState.PLAY, "me");

        testMessage.executeMessage(cliViewClient);
        assertEquals(GameState.PLAY, cliViewClient.getLocalModel().getPlayerState());

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}