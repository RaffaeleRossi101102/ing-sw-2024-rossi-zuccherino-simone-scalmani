package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class WaitingForTurnState extends ViewState{

    private ViewState nextState;

    public WaitingForTurnState(CliViewClient view, ClientInterface client, double ID) {

        super(view, client, ID);
    }

    @Override
    public void display() {
        System.out.println("Wait for your turn to play, meanwhile...");
        defaultCommand(GameState.PLAY);

        Thread newStateDisplayThread;
        if (view.getLocalModel().getState().equals(GameState.ENDGAME)) {
            nextState = new EndGameState(view, client, ID);
        }else{
            nextState.display();
        }
        newStateDisplayThread = new Thread(nextState::display);

        newStateDisplayThread.start();
    }

    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }


}
