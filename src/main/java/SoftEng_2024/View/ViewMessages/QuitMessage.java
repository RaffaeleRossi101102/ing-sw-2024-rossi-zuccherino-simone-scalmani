package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.IOException;
import java.rmi.RemoteException;
/**
 * Represents a message that instructs the GameController to handle a quit request from a client.
 */
public class QuitMessage implements ViewMessage {
    double ID;

    /**
     * Constructs a QuitMessage with the specified player ID.
     *
     * @param ID The ID of the player requesting to quit.
     */
    public QuitMessage(double ID) {
        this.ID = ID;
    }
    /**
     * Executes the operation to handle the quit request on the GameController.
     * Throws RuntimeException if there is an IOException or RemoteException.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        try {
            controller.quit(ID);
        } catch (RemoteException e) {
            System.err.println("Something went terribly wrong");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
