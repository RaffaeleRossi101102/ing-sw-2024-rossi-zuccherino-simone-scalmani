package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
/**
 * Represents the state when the game ends, displaying final scores and winners.
 * Extends {@link ViewState}.
 */
public class EndGameState extends ViewState {
    /**
     * Constructs an EndGameState instance with the specified CLI view client, client interface, and player ID.
     *
     * @param view   The CLI view client managing the state transitions.
     * @param client The client interface used for communication with the game server.
     * @param ID     The ID of the player associated with this state.
     */
    public EndGameState(CliViewClient view, ClientInterface client, double ID) {
        super(view, client, ID);
    }
    /**
     * Displays the final scores and declares the winner(s).
     * If there is only one winner, congratulates that player. If there are multiple winners, congratulates all of them.
     * Exits the program after displaying the results.
     */
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
