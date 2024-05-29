package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import jdk.javadoc.internal.tool.Start;

public class RejoinState extends ViewState{
    public RejoinState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }

    @Override
    public void display() {

        if(view.getLocalModel().getState().equals(GameState.STARTER)){
            new StarterState(view, client, ID).display();
        }
        if(view.getLocalModel().getState().equals(GameState.SETCOLOR)){
            new SetColorState(view, client, ID).display();
        }
        if(view.getLocalModel().getState().equals(GameState.CHOOSEGOAL)){
            new ChooseGoalState(view, client, ID).display();
        }
        if(view.getLocalModel().getState().equals(GameState.PLAY)){
            new PlayState(view, client, ID).display();
        }
    }
}
