package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class OwnWhisperMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    OwnWhisperMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new OwnWhisperMessage(12345, mex, "you", "now");

        testMessage.executeMessage(cliViewClient);
        assertNotNull(cliViewClient.getLocalModel().getChat());
    }
}