package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;
/**
 * Represents a message that instructs the GameController to create a new game instance.
 */
public class CreateGameMessage implements ViewMessage {

    String nickname;
    int maxPlayers;
    double ID;
    /**
     * Constructs a CreateGameMessage with the specified parameters.
     *
     * @param nickname   The nickname of the player creating the game.
     * @param maxPlayers The maximum number of players allowed in the game.
     * @param ID         The ID of the player creating the game.
     */
    public CreateGameMessage(String nickname,int maxPlayers,double ID){
        this.ID=ID;
        this.maxPlayers=maxPlayers;
        this.nickname=nickname;
    }
    /**
     * Executes the operation to create a new game on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.createGame(maxPlayers,nickname,ID);
    }
}
