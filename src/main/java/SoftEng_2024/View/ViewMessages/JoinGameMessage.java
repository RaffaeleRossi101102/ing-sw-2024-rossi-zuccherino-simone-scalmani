package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.IOException;
import java.rmi.RemoteException;
/**
 * Represents a message that instructs the GameController to join a game with a specified nickname.
 */
public class JoinGameMessage implements ViewMessage{

    String nickname;
    double ID;
    /**
     * Constructs a JoinGameMessage with the specified nickname and player ID.
     *
     * @param nickname The nickname of the player joining the game.
     * @param ID       The ID of the player joining the game.
     */
    public JoinGameMessage(String nickname, double ID){
        this.nickname=nickname;
        this.ID=ID;
    }
    /**
     * Executes the operation to join a game with the specified nickname on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        //try {
            controller.joinGame(nickname,ID);
        //} catch (IOException e) {
           // throw new RuntimeException("Something went terribly wrong.");
        //}
    }
}
