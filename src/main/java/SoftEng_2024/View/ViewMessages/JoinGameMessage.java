package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;

public class JoinGameMessage implements ViewMessage{
    String nickname;
    double ID;
    public JoinGameMessage(String nickname, double ID){
        this.nickname=nickname;
        this.ID=ID;
    }
    @Override
    public void executeMessage(GameController controller) throws RemoteException{
        controller.joinGame(nickname,ID);
    }
}
