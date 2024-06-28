package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.GoldCard;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.ScoreTypes;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.GoldFront;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.View.CliViewClient;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class UpdatedPublicGoalsMessageTest {

    ModelMessage testMessage;
    ClientInterface clientInterface = new ClientRMI(12345, "localhost", 4567);
    CliViewClient cliViewClient = new CliViewClient(12345, clientInterface);
    String mex = "hello";
    GoalCard[] publicGoals = new GoalCard[]{null, null, null};

    UpdatedPublicGoalsMessageTest() throws RemoteException {
    }

    @Test
    void executeMessage() {
        testMessage = new UpdatedPublicGoalsMessage(mex, publicGoals);

        testMessage.executeMessage(cliViewClient);
        assertNotNull(cliViewClient.getLocalModel().getPublicGoals());

        testMessage.rejoining = true;
        testMessage.executeMessage(cliViewClient);
        assertEquals(1, cliViewClient.getLocalModel().getArrivedMessages());
    }
}