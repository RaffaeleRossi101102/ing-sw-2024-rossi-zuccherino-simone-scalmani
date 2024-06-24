package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class RejoinState extends ViewState{
    public RejoinState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }

    @Override
    public void display() {
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
                    listenDefaultCommand();
                    break;
                case NOTPLAYING:
                    nextState = new ReadyToStartState(view, client, ID);
                    break;
                case PLAY:
                    nextState= new ReadyToStartState(view,client,ID);
                    listenDefaultCommand();
                    break;
                default:
                    nextState = new ConnectionState(view, client, ID);
                    break;

            }
            nextState.display();

    }

}
