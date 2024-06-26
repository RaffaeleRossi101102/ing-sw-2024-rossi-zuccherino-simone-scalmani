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
    public void display() throws InterruptedException {
        defaultCommand(GameState.PLAY,"Wait for your turn to play, meanwhile...");
        if (view.getLocalModel().getState().equals(GameState.ENDGAME)) {
            nextState = new EndGameState(view, client, ID);
        }

        nextState.setDefaultCommandChosen(true);
        nextState.display();
    }

    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }


}
