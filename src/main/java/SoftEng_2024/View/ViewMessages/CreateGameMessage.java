package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

import java.rmi.RemoteException;

public class CreateGameMessage implements ViewMessage {
    String nickname;
    int maxPlayers;
    double ID;
    public CreateGameMessage(String nickname,int maxPlayers,double ID){
        this.ID=ID;
        this.maxPlayers=maxPlayers;
        this.nickname=nickname;
    }
    @Override
    public void executeMessage(GameController controller)  {
        try {
            controller.createGame(maxPlayers,nickname,ID);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
