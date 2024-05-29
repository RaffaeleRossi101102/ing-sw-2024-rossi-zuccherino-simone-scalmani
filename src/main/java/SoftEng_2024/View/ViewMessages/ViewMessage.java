package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface ViewMessage extends Serializable {
    void executeMessage(GameController controller) ;
}
