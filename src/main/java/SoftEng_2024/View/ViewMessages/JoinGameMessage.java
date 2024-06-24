package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.io.IOException;
import java.rmi.RemoteException;

public class JoinGameMessage implements ViewMessage{
    String nickname;
    double ID;
    public JoinGameMessage(String nickname, double ID){
        this.nickname=nickname;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) {
        try {
            controller.joinGame(nickname,ID);
        } catch (IOException e) {
            throw new RuntimeException("Something went terribly wrong.");
        }
    }
}
