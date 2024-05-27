package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class ReJoin implements ViewMessage {

    double ID;
    String nickname;

    public ReJoin(double ID, String nickname) {
        this.ID = ID;
        this.nickname = nickname;
    }

    @Override
    public void executeMessage(GameController controller) {

    }
}
