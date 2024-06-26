package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
/**
 * Represents the state where the client is waiting for their turn to play.
 * Extends ViewState and handles displaying information and transitioning to other states.
 */
public class WaitingForTurnState extends ViewState{

    private ViewState nextState;
    /**
     * Constructs a WaitingForTurnState object with the specified parameters.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface used for communication with the server.
     * @param ID     The ID associated with this client.
     */
    public WaitingForTurnState(CliViewClient view, ClientInterface client, double ID) {

        super(view, client, ID);
    }
    /**
     * Displays the state's information and manages state transitions.
     *
     * @throws InterruptedException If interrupted while displaying.
     */
    @Override
    public void display() throws InterruptedException {
        defaultCommand(GameState.PLAY,"Wait for your turn to play, meanwhile...");
        if (view.getLocalModel().getState().equals(GameState.ENDGAME)) {
            nextState = new EndGameState(view, client, ID);
        }

        nextState.setDefaultCommandChosen(true);
        nextState.display();
    }
    /**
     * Sets the next state for transition.
     *
     * @param nextState The ViewState to set as the next state.
     */
    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }


}
