package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;
/**
 * Represents a message that instructs the GameController to broadcast a message to all players.
 */
public class BroadcastMessage implements ViewMessage{

    String message;
    double ID;
    /**
     * Constructs a BroadcastMessage with the specified message and ID.
     *
     * @param message The message to broadcast.
     * @param ID      The ID associated with the message.
     */
    public BroadcastMessage(String message,double ID){
        this.ID=ID;
        this.message=message;
    }
    /**
     * Executes the broadcast operation on the GameController.
     *
     * @param controller The GameController instance on which to execute the broadcast operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.broadcast(message,ID);
    }
}
