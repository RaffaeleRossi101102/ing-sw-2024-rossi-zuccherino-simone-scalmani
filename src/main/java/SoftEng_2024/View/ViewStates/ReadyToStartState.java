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

        playState.setDrawState(drawState);
        drawState.setPlayState(playState);

        playState.display();

    }
}
