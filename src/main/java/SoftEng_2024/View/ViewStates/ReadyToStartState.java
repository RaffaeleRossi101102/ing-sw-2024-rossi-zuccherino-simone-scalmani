package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
/**
 * ReadyToStartState represents the state where the game is ready to begin,
 * waiting for players to choose their private goals.
 */
public class ReadyToStartState extends ViewState{
    /**
     * Constructs a ReadyToStartState object.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface through which interactions with the game server occur.
     * @param ID     The unique identifier of the player in the game.
     */
    public ReadyToStartState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }
    /**
     * Displays the ready to start state, waiting for all players to choose their private goals
     * before transitioning to the play phase of the game.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for player actions.
     */
    @Override
    public void display() throws InterruptedException {
        //System.out.println("Waiting for all the players to choose their private goal...");
        listenDefaultCommand(true);
        defaultCommand(GameState.CHOOSEGOAL,"Waiting for all the players to choose their private goal...");
        System.out.println("Now it's time to Play!");
        PlayState playState = new PlayState(view, client, ID);
        DrawState drawState = new DrawState(view, client, ID);
        WaitingForTurnState waitingForTurnState = new WaitingForTurnState(view, client, ID);


        waitingForTurnState.setNextState(playState);
        playState.setNextState(drawState);
        drawState.setNextState(waitingForTurnState);

        //waiting for all the cards and for the current player to be set

        while(view.getLocalModel().getCurrentTurnPlayerNickname()==null );
        //if it's my turn then i also have the black color
        if (view.getLocalModel().getCurrentTurnPlayerNickname().equals(view.getLocalModel().getNickname())) {
            view.getLocalModel().setFirstPlayer(true);
            if(defaultCommandChosen)
                playState.setDefaultCommandChosen(true);
            view.setViewState(playState);
            playState.display();
        }else{
            view.setViewState(playState);
            waitingForTurnState.display();
        }



    }
}
