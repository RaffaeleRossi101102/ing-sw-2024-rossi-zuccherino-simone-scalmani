package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedGoldDeckMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";

    UpdatedGoldDeckMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedGoldDeckMessage(mex, Angles.PLANTS);

        testMessage.executeMessage(cliViewClient);
        assertEquals(Angles.PLANTS, cliViewClient.getLocalModel().getTopGoldCard());

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(Angles.PLANTS, cliViewClient.getLocalModel().getTopGoldCard());
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}