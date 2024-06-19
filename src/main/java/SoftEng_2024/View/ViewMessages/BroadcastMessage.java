package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;

public class BroadcastMessage implements ViewMessage{
    String message;
    double ID;
    public BroadcastMessage(String message,double ID){
        this.ID=ID;
        this.message=message;
    }
    @Override
    public void executeMessage(GameController controller) {
        controller.broadcast(message,ID);
    }
}
