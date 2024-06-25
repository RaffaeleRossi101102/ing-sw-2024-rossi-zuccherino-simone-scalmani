package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;

public class EndGameState extends ViewState {
    public EndGameState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }

    @Override
    public synchronized void display() {
        System.out.println("Now the final scores will be calculated and we will declare the winner...");
        if (view.getLocalModel().getWinnersNickname().size()==1){
            System.out.println("CONGRATULATIONS "+view.getLocalModel().getWinnersNickname().get(0)+", YOU WON!!!!");
        }else{
            System.out.println("We have more than one winner!");
            System.out.println("CONGRATULATIONS TO: ");
            for (String nick : view.getLocalModel().getWinnersNickname()) {
                System.out.print(nick + " ");
            }
            System.out.print("YOU ALL WON!!!");
        }

        System.exit(0);

        //TODO ?? defaultCommand(GameState.ENDGAME,"The game is ended but you can still:");
    }
}
