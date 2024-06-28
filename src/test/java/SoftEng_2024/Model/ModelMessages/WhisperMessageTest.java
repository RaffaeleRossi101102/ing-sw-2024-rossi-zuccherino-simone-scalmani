package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class WhisperMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    WhisperMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new WhisperMessage(12345, mex, "me", "now");

        testMessage.executeMessage(cliViewClient);
        assertTrue(cliViewClient.getLocalModel().getChat().contains("[now] me has whispered to you: hello"));
    }
}