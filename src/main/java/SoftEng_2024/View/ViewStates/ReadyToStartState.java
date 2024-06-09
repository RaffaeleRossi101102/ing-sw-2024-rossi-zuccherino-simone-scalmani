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
        //TODO: MODIFICARE DEFAULT COMMAND IN MODO TALE CHE NON TERMINI SUBITO APPENA UNO SIA IN PLAY
        PlayState playState = new PlayState(view, client, ID);
        DrawState drawState = new DrawState(view, client, ID);
        WaitingForTurnState waitingForTurnStateState = new WaitingForTurnState(view, client, ID);


        waitingForTurnStateState.setNextState(playState);
        playState.setNextState(drawState);
        drawState.setNextState(waitingForTurnStateState);
        //aspetto che mi arrivino tutte le carte e che venga settato il current player
        while(view.getLocalModel().getCurrentTurnPlayerNickname()==null | !view.getLocalModel().getAllCardsArrived());
        //se è il mio turno e sono il primo, "aggiungo il colore nero tra i colori" e parto col turno
        if (view.getLocalModel().getCurrentTurnPlayerNickname().equals(view.getLocalModel().getNickname())) {
            view.getLocalModel().setFirstPlayer(true);
            playState.display();
        }else{
            waitingForTurnStateState.display();
        }



    }
}
