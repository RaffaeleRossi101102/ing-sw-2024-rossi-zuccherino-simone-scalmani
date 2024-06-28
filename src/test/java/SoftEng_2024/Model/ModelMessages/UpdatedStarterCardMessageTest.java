package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedStarterCardMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    StarterCard card = new StarterCard(new ResourceFront(null, 0, null), false, null, 0);

    UpdatedStarterCardMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedStarterCardMessage(12345, card);

        testMessage.executeMessage(cliViewClient);
        assertNotNull(cliViewClient.getLocalModel().getStarterCard());

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}