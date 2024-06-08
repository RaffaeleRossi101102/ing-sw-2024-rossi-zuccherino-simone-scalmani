package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class ReadyToStartState extends ViewState{
    public ReadyToStartState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }

    @Override
    public void display() {
        System.out.println("Waiting for all the players to choose their private goal...");
        defaultCommand(GameState.CHOOSEGOAL);
        System.out.println("Now it's time to Play!");

        PlayState playState = new PlayState(view, client, ID);
        DrawState drawState = new DrawState(view, client, ID);
        WaitingForTurnState waitingForTurnStateState = new WaitingForTurnState(view, client, ID);


        waitingForTurnStateState.setNextState(playState);
        playState.setNextState(drawState);
        drawState.setNextState(waitingForTurnStateState);

        //se è il mio turno
        if (view.getLocalModel().getCurrentTurnPlayerNickname().equals(view.getLocalModel().getNickname())) {
            newStateDisplayThread = new Thread(playState::display);
        }else{
            newStateDisplayThread = new Thread(waitingForTurnStateState::display);
        }
        newStateDisplayThread.start();


    }
}
