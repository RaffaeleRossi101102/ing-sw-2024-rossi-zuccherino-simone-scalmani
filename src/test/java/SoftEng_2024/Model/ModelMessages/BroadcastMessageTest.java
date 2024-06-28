package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.View;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class BroadcastMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    BroadcastMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new BroadcastMessage(12345, mex);

        testMessage.executeMessage(cliViewClient);
        assertNotNull(cliViewClient.getLocalModel().getChat());
    }
}