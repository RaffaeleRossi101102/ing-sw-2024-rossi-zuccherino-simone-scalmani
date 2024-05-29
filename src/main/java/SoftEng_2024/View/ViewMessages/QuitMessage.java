package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;

public class QuitMessage implements ViewMessage {
    double ID;
    public QuitMessage(double ID) {
        this.ID = ID;
    }

    @Override
    public void executeMessage(GameController controller) {
        try {
            controller.quit(ID);
        } catch (RemoteException e) {
            System.err.println("Something went terribly wrong");
            throw new RuntimeException(e);
        }
    }
}
