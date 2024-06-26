package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
/**
 * RejoinState represents the state where a player rejoins an ongoing game session.
 * It handles the process of rejoining based on the player's current state in the game.
 */
public class RejoinState extends ViewState{
    /**
     * Constructs a RejoinState object.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface through which interactions with the game server occur.
     * @param ID     The unique identifier of the player in the game.
     */
    public RejoinState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }
    /**
     * Displays the rejoin state, determining the appropriate next state based on the player's
     * current state in the game and transitioning to that state.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for player actions.
     */
    @Override
    public void display() throws InterruptedException {
        System.out.println("lo stato è "+view.getLocalModel().getPlayerState());
        while(view.getLocalModel().getNumberOfMessages()==0 | view.getLocalModel().getNumberOfMessages()!=view.getLocalModel().getArrivedMessages());
            ViewState nextState;
        System.out.println("lo stato è "+view.getLocalModel().getPlayerState());
            switch(view.getLocalModel().getPlayerState()){
                case CONNECTION:
                case STARTER:
                    nextState= new StarterState(view,client,ID);

                    break;
                case SETCOLOR:
                    nextState = new SetColorState(view, client, ID);
                    break;
                case CHOOSEGOAL:
                    nextState = new ChooseGoalState(view, client, ID);
                    listenDefaultCommand(true);
                    break;
                case NOTPLAYING:
                case PLAY:
                    nextState = new ReadyToStartState(view, client, ID);
                    break;
                default:
                    nextState = new ConnectionState(view, client, ID);
                    break;

            }
            view.setViewState(nextState);
            nextState.display();

    }

}
