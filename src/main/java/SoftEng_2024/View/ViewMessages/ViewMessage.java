package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;
/**
 * Interface for messages that can be executed by a GameController.
 * Implementations of this interface define specific actions to be performed on the GameController.
 */
public interface ViewMessage extends Serializable {
    /**
     * Executes the action defined by the implementing class on the given GameController.
     *
     * @param controller The GameController instance on which to execute the action.
     */
    void executeMessage(GameController controller) ;
}
