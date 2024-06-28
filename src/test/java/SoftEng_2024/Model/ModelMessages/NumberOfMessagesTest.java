package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class NumberOfMessagesTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    NumberOfMessagesTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new NumberOfMessages(12345, mex, "me", 2);

        testMessage.executeMessage(cliViewClient);
        assertEquals(2, cliViewClient.getLocalModel().getNumberOfMessages());
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}