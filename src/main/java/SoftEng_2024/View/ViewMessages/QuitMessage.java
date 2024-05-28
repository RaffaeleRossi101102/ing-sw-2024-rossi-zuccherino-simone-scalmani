package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.IOException;
import java.rmi.RemoteException;

public class QuitMessage implements ViewMessage {
    double ID;
    public QuitMessage(double ID) {
        this.ID = ID;
    }

    @Override
    public void executeMessage(GameController controller) throws RemoteException {
        try {
            controller.quit(ID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
