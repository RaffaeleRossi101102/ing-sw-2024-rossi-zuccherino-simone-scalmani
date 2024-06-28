package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedAvailableGoalsMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    List<GoalCard> avGoals = new ArrayList<>();

    UpdatedAvailableGoalsMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedAvailableGoalsMessage(12345, mex, avGoals, "me");

        testMessage.executeMessage(cliViewClient);
        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}