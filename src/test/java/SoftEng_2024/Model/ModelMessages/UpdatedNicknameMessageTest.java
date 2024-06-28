package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedNicknameMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    UpdatedNicknameMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedNicknameMessage(12345, mex, "me", true);

        testMessage.executeMessage(cliViewClient);
        assertEquals("me", cliViewClient.getLocalModel().getNickname());

        testMessage = new UpdatedNicknameMessage(12345, mex, "me", false);
        testMessage.executeMessage(cliViewClient);
        assertFalse(cliViewClient.getLocalModel().getPlayersNickname().isEmpty());

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}