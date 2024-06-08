package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class RejoinState extends ViewState{
    public RejoinState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }

    @Override
    public void display() {
        Thread newStateDisplayThread;
        ViewState nextState;
        switch(view.getLocalModel().getPlayerState()){
            case STARTER:
                nextState = new StarterState(view, client, ID);
            case SETCOLOR:
                nextState = new SetColorState(view, client, ID);
            case CHOOSEGOAL:
                nextState = new ChooseGoalState(view, client, ID);
            case NOTPLAYING:
                nextState = new ReadyToStartState(view, client, ID);
            default:
                nextState = new ConnectionState(view, client, ID);

        }
        nextState.display();

    }

}
