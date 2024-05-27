package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;

public class WhisperMessage implements ViewMessage{
    String message;
    String nickname;
    double ID;
    public WhisperMessage(String message, String nickname, double ID){
        this.message=message;
        this.nickname=nickname;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) throws RemoteException {

    }
}
