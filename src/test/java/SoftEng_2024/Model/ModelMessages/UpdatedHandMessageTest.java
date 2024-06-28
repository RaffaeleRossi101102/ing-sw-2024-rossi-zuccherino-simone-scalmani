package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedHandMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    List<Card> hand = new ArrayList<>();
    Front mockFront = new ResourceFront(null, 0, null);
    Card mockCard = new ResourceCard(mockFront, false, null, 0);

    UpdatedHandMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        hand.add(mockCard);
        cliViewClient.getLocalModel().setPersonalHand(hand);
        testMessage = new UpdatedHandMessage(12345, mex, hand, "me");

        testMessage.executeMessage(cliViewClient);
        assertNull(cliViewClient.getLocalModel().getOtherPlayersHand().get("me"));

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertNull(cliViewClient.getLocalModel().getOtherPlayersHand().get("me"));
    }
}