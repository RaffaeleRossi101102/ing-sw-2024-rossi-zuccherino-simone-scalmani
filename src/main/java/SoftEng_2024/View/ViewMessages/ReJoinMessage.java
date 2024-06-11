package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class ReJoinMessage implements ViewMessage {

    double ID;
    String nickname;

    public ReJoinMessage(double ID, String nickname) {
        this.ID = ID;
        this.nickname = nickname;
    }

    @Override
    public void executeMessage(GameController controller) {
        //controller.reJoinGame(nickname, ID);
    }
}
